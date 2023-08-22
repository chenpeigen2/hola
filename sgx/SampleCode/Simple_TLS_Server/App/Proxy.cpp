//
// Created by chenpeigen on 23-2-12.
//

#include "Proxy.h"
#include "wfrest/HttpServer.h"
#include "wfrest/json.hpp"
#include "Router.h"

void Proxy::start() {
    if (server.start(port) == 0) {
        getchar();
        stop();
    } else {
        fprintf(stderr, "Cannot start server");
        exit(1);
    }
}

void Proxy::stop() {
    fprintf(stdout, "server stopped");
    server.stop();
}

wfrest::HttpServer *Proxy::getServer() {
    return &server;
}

void Proxy::makeRouter() {
    router(server);
}


Proxy::Proxy(int port) {
    this->port = port;
}

Proxy::~Proxy() {
    server.shutdown();
    server.wait_finish();
    fprintf(stdout, "server stopped");
}

// instance expected
Proxy proxy(8888);

