//
// Created by chenpeigen on 23-4-19.
//
#include "Enclave_t.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include <string>
#include <vector>
#include "Request.h"

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

    int length = sgx_json_arr_length(source.c_str());

    printf("xxxxxxxxxxxxxxxxx %d", length);

    std::vector <Request> res;
    std::string request_data = "db_name=fake1&filter={\n"
                               "    \"execution_stats\": true,\n"
                               "    \"selector\": {\n"
                               "        \"data.countryid\": {\n"
                               "            \"$gt\": -1\n"
                               "        }\n"
                               "    }\n"
                               "}";
    res.push_back(Request("http://127.0.0.1:8080/_find", request_data));
    res.push_back(Request("http://127.0.0.1:8081/_find", request_data));
    return res;
}