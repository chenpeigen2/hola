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