//
// Created by chenpeigen on 23-5-9.
//

#ifndef SGX_UTILS_H
#define SGX_UTILS_H

#include <cstdio>
#include <iostream>
#include <string.h>
#include "http_utils.h"
#include "Enclave_t.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include "string.h"
#include "string"


std::vector <std::string> split(const std::string &str, const std::string &pattern);

std::vector <std::string> params(const std::string &params);

std::vector <Request> provide_query(const std::string &source);

#endif //SGX_UTILS_H
