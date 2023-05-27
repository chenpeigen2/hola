//
// Created by chenpeigen on 23-4-19.
//
#include "utils.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include "Request.h"
#include <map>
#include <vector>
#include <string>
#include <sstream>
#include "Encrypt.h"

using namespace std;

std::vector <std::string> split(const std::string &str, const std::string &pattern) {
    vector <string> res;
    if (pattern.empty()) return res;
    size_t start = 0, end = 0;
    while ((end = str.find(pattern, start)) != string::npos) {
        if (end > start) res.push_back(str.substr(start, end - start));
        start = end + pattern.size();
    }
    if (start < str.size()) res.push_back(str.substr(start));
    return res;
}

std::vector <std::string> params(const std::string &params) {
    vector <string> res;
    vector <string> p1 = split(params, "&");
    for (const auto &re1: p1) {
        vector <string> p2 = split(re1, "=");
        for (const auto &re2: p2) {
            res.push_back(re2);
        }
    }
    return res;
}

/** provide the query associate with the backbend **/
std::vector <Request> provide_query(const std::string &&url_path, const std::string &source) {

    int len = sgx_json_arr_length(source.c_str());
    std::vector <Request> res;

    for (int i = 0; i < len; ++i) {
        // we acquire the uid associate with the aes key
        std::string uid = sgx_json_query(i, "uid");
        std::string aesKey = client_get_aes_key(uid);
        printf("the aes key: %s \n", aesKey.c_str());

        std::string filter = sgx_json_query(i, "filter");
        // we acquire the filter
        filter = decode(filter.c_str(), filter.length());
        filter = ecb_decrypt(filter.c_str(), filter.length(),
                             reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                             aesKey.length());

        // here we expect to create our own new provide query execution
        int len = sgx_simple_split(filter.c_str());
        for (int i = 0; i < len; ++i) {
            std::string text = sgx_acquire_vector(i);
            printf("the acquired vector %s \n", text.c_str());
            auto vec = convert_to_vec(text);
            std::string urls = sgx_curl_post("http://localhost:3000/aggregate_com",
                                             convert_vec_to_json(vec).c_str());
            auto url_vec = convert_json_to_vec(urls);
            for (const auto &item: url_vec) {
                res.push_back(Request(item + url_path, filter));
            }
        }
    }

    for (const auto &item: res) {
        printf("\n the url: %s  the filter: %s \n", item.url.c_str(), item.filter.c_str());
    }

    return res;
}

/** client-sgx auth execution **/
std::map <std::string, std::string> client_auth_map;

bool client_auth(const std::string &source) {
    std::string &&uid = sgx_json_object(source.c_str(), "uid");
    std::string &&key = sgx_json_object(source.c_str(), "key");
    printf("client_auth %s %s\n", uid.c_str(), key.c_str());

    key = decode(key.c_str(), key.length());
    key = decrypt(reinterpret_cast<const unsigned char *>(key.c_str()), key.length(),
                  (const unsigned char *) PRIVATE_KEY, strlen(PRIVATE_KEY));

    client_auth_map[uid] = key.substr(0, key.find('\0'));
    printf("client_auth %s %s\n", uid.c_str(), key.c_str());
    return true;
}

std::string client_get_aes_key(const std::string &uid) {
    if (client_auth_map.count(uid) > 0) {
        return client_auth_map[uid];
    }
    return "null";
}


std::string convert_vec_to_json(std::vector <std::string> &vec) {
    std::string ret = "[";
    for (auto it = vec.begin(); it != vec.end(); ++it) {
        ret += "\"" + *it + "\"";
        if (it != vec.end() - 1) {
            ret += ",";
        }
    }
    ret += "]";
    return ret;
}

std::vector <std::string> convert_json_to_vec(std::string basicString) {
    std::vector <std::string> vec;
    std::string token;
    for (size_t i = 1; i < basicString.size() - 1; ++i) {
        if (basicString[i] == '[') {
            continue;
        } else if (basicString[i] == ',') {
            vec.push_back(token);
            token = "";
        } else if (basicString[i] == ']') {
            break;
        } else if (basicString[i] != '\"') {
            token += basicString[i];
        }
    }
    vec.push_back(token);
    return vec;
}

std::vector <std::string> convert_to_vec(const std::string &source) {
    std::vector <std::string> vec;
    std::stringstream ss(source);
    std::string token;
    while (std::getline(ss, token, '#')) {
        vec.push_back(token);
    }
    return vec;
}