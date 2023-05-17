//
// Created by chenpeigen on 23-4-19.
//
#include "utils.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include "Request.h"
#include <map>
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

std::vector <Request> provide_query(const std::string &source) {
    int len = sgx_json_arr_length(source.c_str());
    std::vector <Request> res;

    {
        std::string key = "hello";
        std::string file_name = "config.json";
        int value = sgx_json_file_read(key.c_str(), file_name.c_str());
        printf("\n======================== %d", value);
        sgx_struct();
    }

    std::string uid = "e51dce84-f2a4-43db-9b8f-311250ff0ba1";
    std::string aesKey = client_get_aes_key(uid);
    printf("the aes key: %s \n", aesKey.c_str());

    for (int i = 0; i < len; ++i) {
        std::string query = sgx_json_query(i, "query");
        std::string db_name = sgx_json_query(i, "db_name");
        std::string filter = sgx_json_query(i, "filter");

        // decrypt
        db_name = decode(db_name.c_str(), db_name.length());
        db_name = ecb_decrypt(db_name.c_str(), db_name.length(),
                              reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                              aesKey.length());

        filter = decode(filter.c_str(), filter.length());
        filter = ecb_decrypt(filter.c_str(), filter.length(),
                             reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                             aesKey.length());


        std::string request_data = "db_name=" + db_name + "&filter=" + filter;
        printf("the data decode from the json: %s %s \n", query.c_str(), request_data.c_str());
        res.push_back(Request(query, request_data));
        res.push_back(Request(query, request_data));
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