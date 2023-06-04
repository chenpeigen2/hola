//
// Created by chenpeigen on 23-4-23.
//

#ifndef SGX_ENCALVE_UTILS_H
#define SGX_ENCALVE_UTILS_H

#include <string>
#include "Request.h"
#include <utility>

std::string generateAESKey();

std::pair <std::string, std::string> auth_if_needed(std::string &&proto, std::string &&domain, short unsigned &&port);

std::string curl_get(const char *url, const char *request);

std::string curl_post(const char *url, const Request &request);

#endif //SGX_ENCALVE_UTILS_H
