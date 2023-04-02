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

std::string hello_world() {
    std::string hello_world = "hello world";
    return hello_world;
}

std::string filter_find() {
    /** test for curl get API **/
    char *response;
    std::string res;
    std::string url = "http://127.0.0.1:8080/_find";
    std::string request = "db_name=fake1&filter={\"selector\": {\"data.countryid\": {\"$gt\": -1}},\"execution_stats\": true}";
    ecall_curl_get(global_eid, &response, url.c_str(), request.c_str());
    printf("APP side response: %s\n", response);
    toString(response, res);
    return res;
}