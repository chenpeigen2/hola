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

        // we insert here to create our own filter
        std::string key = "{\"selector\":{\"data.countryid\":{\"$gt\":-1}}}";
        std::string file_name = "config.json";
        int find_num = sgx_json_file_read(key.c_str(), file_name.c_str());
        for (int j = 0; j < find_num; j++) {
            auto data_struct = sgx_key_map_struct(j);

            std::string query = data_struct.query;
            std::string db_name = data_struct.db_name;
            res.push_back(Request(query, db_name, filter));
        }
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