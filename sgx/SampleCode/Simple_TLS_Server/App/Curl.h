//
// Created by chenpeigen on 23-2-12.
//

#ifndef SGX_CURL_H
#define SGX_CURL_H

#include "curl/curl.h"
#include <string>

#if defined(__cplusplus)
extern "C" {
#endif

CURLcode sgx_auth(const std::string &url, const std::string &data, std::string &response, int timeout);

CURLcode sgx_post(const std::string &url, const std::string &data, std::string &response,
                  int timeout);

CURLcode sgx_get(const std::string &url, std::string &response, int timeout);

#if defined(__cplusplus)
}
#endif

#endif //SGX_CURL_H
