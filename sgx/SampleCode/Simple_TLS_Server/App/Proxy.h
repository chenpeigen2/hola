//
// Created by chenpeigen on 23-2-12.
//

#ifndef SGX_PROXY_H
#define SGX_PROXY_H


#include "wfrest/HttpServer.h"
#include "wfrest/json.hpp"
#include <iostream>
#include <string>


class Proxy {
private:
    int port;

    wfrest::HttpServer server;

public:

    Proxy(int port);

    ~Proxy();

    // start the server
    void start();

    // stop the server
    void stop();

    wfrest::HttpServer *getServer();

    void makeRouter();

};

extern Proxy proxy;


#endif //SGX_PROXY_H
