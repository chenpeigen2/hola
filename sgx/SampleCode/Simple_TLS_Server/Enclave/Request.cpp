//
// Created by chenpeigen on 23-4-20.
//

#include "Request.h"
#include <cstdio>
#include "Enclave_t.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include "string.h"
#include "vector"
#include <string>

Request::Request(std::string url, std::string db_name, std::string filter) {
    this->url = url;
    this->db_name = db_name;
    this->filter = filter;
}

/** impl for only for print the data **/
void Request::print() {
    printf("the url: %s  , the value: %s %s \n", this->url, this->db_name, this->filter);
}