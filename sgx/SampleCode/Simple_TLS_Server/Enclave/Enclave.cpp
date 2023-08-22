#include "Enclave_t.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include <string.h>
#include <string>
#include <iostream>
#include <cstring>
#include <random>
#include <time.h>

#include <array>
#include <unordered_set>
#include <map>
#include <unordered_map>
#include <utility>
#include "http_utils.h"
#include "encalve_utils.h"
#include "utils.h"
#include "Encrypt.h"

// TODO provide query 重写
// TODO attestion process

using namespace std;
using namespace oceanstar;

std::string sk;

// indicate the authed set
std::unordered_set <std::string> authSet;
std::unordered_map <std::string, std::string> auth_map;
std::unordered_map <std::string, std::string> auth_uuid_map;

//// indicate the refresh rate
#define ELAPSE_MINUTES 15
time_t start;

bool refreshIfNeeded() {
    time_t now = sgx_time(nullptr);
    if (start == 0) {
        start = now;
    }
    double seconds = difftime(now, start);
    if (seconds >= ELAPSE_MINUTES * 60) {
        start = now;
        return true;
    }
    return false;
}

string random_string(int len) {
    string str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    random_device rd;
    mt19937 generator(rd());
    shuffle(str.begin(), str.end(), generator);
    return str.substr(0, len);
}

std::string generateAESKey() {
    return random_string(24);
}

std::pair <std::string, std::string> auth_if_needed(std::string &&proto, std::string &&domain, short unsigned &&port) {
    std::string url = proto + "://" + domain + ":" + to_string(port) + "/_auth";
    bool refresh = refreshIfNeeded();
    if (auth_map.count(url) > 0 && !refresh) {
        return make_pair(auth_map.at(url), auth_uuid_map.at(url));
    }
    std::string &&aesKey = generateAESKey();
    std::string encryptKey = encrypt(reinterpret_cast<const unsigned char *>(aesKey.c_str()), aesKey.length(),
                                     (const unsigned char *) PUBLICKEY, strlen(PUBLICKEY));
    encryptKey = encode(encryptKey.c_str(), encryptKey.length());
    std::string body = "{\"key\":\"" +
                       encryptKey +
                       "\"}";
    std::string &&post_data = sgx_curl_post(url.c_str(), body.c_str()); // execute the auth execution
    auto resp_vec = convert_json_to_vec(post_data); // the resp

    // restore in a map
    auth_uuid_map[url] = resp_vec.at(0);
    auth_map[url] = aesKey;

    return make_pair(aesKey, resp_vec.at(0));
}

std::string curl_post(const char *url, const Request &request) {
    http_url httpUrl;
    if (!httpUrl.parse(url)) {
        exit(0);
    }

    auto auth_result = auth_if_needed(string(httpUrl.get_proto()), string(httpUrl.get_domain()),
                                      httpUrl.get_port());

    std::string aesKey = auth_result.first;
    std::string uuid = auth_result.second;

    std::string filter = request.filter;
    filter = ecb_encrypt(filter.c_str(), reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                         aesKey.length(), PaddingModel::PKCS5OR7);
    filter = encode(filter.c_str(), filter.length());

    std::string body = "{\n"
                       "  \"uuid\" : \"" + uuid +
                       "\",\n"
                       "  \"filter\" : \"" + filter + "\"\n"
                                                      "}";
    /** curl post **/
    std::string response = sgx_curl_post(url, body.c_str());

    // decrypt
    response = decode(response.c_str(), response.length());
    response = ecb_decrypt(response.c_str(), response.length(),
                           reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                           aesKey.length());

    printf("The RESPONSE: %s\n", response.c_str());

    return response;
}

std::string curl_get(const char *url, const char *request) {
    http_url httpUrl;
    if (!httpUrl.parse(url)) {
        exit(0);
    }

    auto auth_result = auth_if_needed(string(httpUrl.get_proto()), string(httpUrl.get_domain()),
                                      httpUrl.get_port());

    std::string aesKey = auth_result.first;
    std::string uuid = auth_result.second;

    vector <string> param = params(request);
    std::string base_url(url);
    std::string res;
    for (int i = 0; i < param.size(); ++i) {
        std::string p = param[i];
        if (i != 0 && i % 2 == 0) {
            res += "&";
        }
        if (i % 2 != 0) {
            res += "=";
            p = ecb_encrypt(p.c_str(), reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                            aesKey.length(), PaddingModel::PKCS5OR7);
            p = encode(p.c_str(), p.length());
        }
        res += p;
    }
    std::string final_url = base_url + "?" + res;
    /** curl get **/
    printf("Encalve final url: %s \n", final_url.c_str());
    std::string response = sgx_curl_get(final_url.c_str());
    printf("The RESPONSE: %s\n", response.c_str());
    // decrypt
    response = decode(response.c_str(), response.length());
    response = ecb_decrypt(response.c_str(), response.length(),
                           reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                           aesKey.length());
    return response;
}


// ### below are the ecall ###

char *ecall_curl_get(const char *url, const char *request) {
    std::string response = curl_get(url, request);
    // assemble
    int len = response.length();
    char *p = new char[len + 1];
    response.copy(p, len);
    p[len] = '\0';
    return p;
}

char *ecall_complex_curl(const char *data) {
    // provide the requests
    auto provide_decode_data = provide_query(data);
    // the result
    std::vector <string> resp;

    // perform execution
    for (const auto &decode_data: provide_decode_data) {
        std::string &&return_data = curl_post(decode_data.url.c_str(), decode_data);
        resp.push_back(return_data);
    }

    // aggregation process
    std::string &&response = simple_aggregation(resp);

    // encrypt
    std::string uid = "e51dce84-f2a4-43db-9b8f-311250ff0ba1";
    std::string aesKey = client_get_aes_key(uid);
    response = ecb_encrypt(response.c_str(), reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                           aesKey.length(), PaddingModel::PKCS5OR7);
    response = encode(response.c_str(), response.length());

    // assemble
    int len = response.length();
    char *p = new char[len + 1];
    response.copy(p, len);
    p[len] = '\0';
    return p;
}

int ecall_auth(const char *data) {
    client_auth(data);
    printf("the ecall_auth: %s", data);
    return 1;
}
