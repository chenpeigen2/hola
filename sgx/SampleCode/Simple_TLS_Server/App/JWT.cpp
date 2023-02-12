//
// Created by chenpeigen on 23-2-12.
//
#include "JWT.h"
#include <jwt-cpp/jwt.h>
#include <stdio.h>
#include <iostream>

void hello_jwt() {
    auto token = jwt::create()
            .set_issuer("auth0")
            .set_type("JWS")
            .set_payload_claim("sample", jwt::claim(std::string("test")))
            .sign(jwt::algorithm::hs256{"secret"});

    std::cout << token << std::endl;
    printf("=================================hello jwt============================================= \n");

}