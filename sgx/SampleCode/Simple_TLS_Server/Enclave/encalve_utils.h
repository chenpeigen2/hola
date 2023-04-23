//
// Created by chenpeigen on 23-4-23.
//

#ifndef SGX_ENCALVE_UTILS_H
#define SGX_ENCALVE_UTILS_H

#include <string>

std::string generateAESKey();

std::string getAESKey();

void auth_if_needed(std::string &&proto, std::string &&domain, short unsigned &&port);

std::string curl_get(const char *url, const char *request);

#endif //SGX_ENCALVE_UTILS_H
