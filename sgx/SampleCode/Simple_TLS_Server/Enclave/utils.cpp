//
// Created by chenpeigen on 23-4-19.
//
#include "utils.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include "Request.h"
#include <map>

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
        std::string query = sgx_json_query(i, "query");
        std::string db_name = sgx_json_query(i, "db_name");
        std::string filter = sgx_json_query(i, "filter");
        std::string request_data = "db_name=" + db_name + "&filter=" + filter;
        printf("the data decode from the json: %s %s \n", query.c_str(), request_data.c_str());
        res.push_back(Request(query, request_data));
        res.push_back(Request(query, request_data));
    }
    return res;
}

std::map <std::string, std::string> client_auth_map;

bool client_auth(const std::string &source) {
    std::string &&uid = sgx_json_object(source.c_str(), "uid");
    std::string &&key = sgx_json_object(source.c_str(), "key");
    printf("client_auth %s %s\n", uid.c_str(), key.c_str());

    key = decode(key.c_str(), key.length());
    key = decrypt(reinterpret_cast<const unsigned char *>(key.c_str()), key.length(),
                  (const unsigned char *) PRIVATE_KEY, strlen(PRIVATE_KEY));

    client_auth_map[uid] = key;
    printf("client_auth %s %s\n", uid.c_str(), key.c_str());
    return true;
}