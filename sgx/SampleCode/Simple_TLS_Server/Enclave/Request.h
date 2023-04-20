//
// Created by chenpeigen on 23-4-20.
//

#ifndef SGX_REQUEST_H
#define SGX_REQUEST_H

#include <string>

class Request {
public:

    std::string url;

    std::string request_data;

    Request(std::string url, std::string request_data);

    void print();
};


#endif //SGX_REQUEST_H
