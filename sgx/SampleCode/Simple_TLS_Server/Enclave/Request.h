//
// Created by chenpeigen on 23-4-20.
//

#ifndef SGX_REQUEST_H
#define SGX_REQUEST_H

#include <string>

class Request {
public:

    std::string url;

    std::string filter;

    Request(std::string url, std::string filter);

    void print();
};


#endif //SGX_REQUEST_H
