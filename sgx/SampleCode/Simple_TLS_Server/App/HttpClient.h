//
// Created by chenpeigen on 23-4-2.
//

#ifndef SGX_HTTPCLIENT_H
#define SGX_HTTPCLIENT_H

#include <string>

std::string hello_world();

std::string filter_find(const std::string &db_name, const std::string &filter);

std::string complex_filter(const std::string &data);

std::string auth_sgx(const std::string &data);

#endif //SGX_HTTPCLIENT_H
