//
// Created by chenpeigen on 23-4-2.
//

#include "HttpClient.h"

#include "sgx_urts.h"
#include "App.h"
#include "Enclave_u.h"

#include "Proxy.h"
#include "Router.h"
#include "Curl.h"
#include "JWT.h"
#include "Utils.h"
#include <iostream>
#include <string>
#include "Utils.h"
#include <iostream>


std::string filter_find(const std::string &db_name, const std::string &filter) {
    /** test for curl get API **/
    char *response;
    std::string res;
    const std::string url = "http://127.0.0.1:8080/_find";
    std::string request = "db_name=" + db_name + "&filter=" + filter;
    std::cout << "passing into the enclave url: " << url << " , and the request_data: " << request << endl;
    ecall_curl_get(global_eid, &response, url.c_str(), request.c_str());
    printf("APP side response: %s\n", response);
    toString(response, res);
    return res;
}

std::string complex_filter(const std::string &data) {
    char *response;
    std::string res;
    ecall_complex_curl(global_eid, &response, data.c_str());
    toString(response, res);
    return res;
}

std::string auth_sgx(const std::string &data) {
    int runned = 0;
    ecall_auth(global_eid, &runned, data.c_str());
    return "hello world";
}