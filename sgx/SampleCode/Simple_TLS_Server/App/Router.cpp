//
// Created by chenpeigen on 23-2-12.
//
#include "Router.h"

#include <wfrest/json.hpp>

#include "HttpClient.h"
#include <iostream>

using namespace wfrest;
using namespace std;
using Json = nlohmann::json;

void router(wfrest::HttpServer &server) {

    /** filter complex for **/
    server.POST("/_complex", [](const HttpReq *req, HttpResp *resp) {
        if (req->content_type() != APPLICATION_JSON) {
            resp->String("NOT APPLICATION_JSON");
        }
        // 遍历数组并输出每个元素
        auto data = req->json().dump();
        std::string result = complex_filter(data);
        std::cout << "the data from the encalve: " << result << std::endl;
        resp->String(result);
    });

    server.POST("/_auth", [](const HttpReq *req, HttpResp *resp) {
        if (req->content_type() != APPLICATION_JSON) {
            resp->String("NOT APPLICATION_JSON");
            return;
        }
        auto data = req->json().dump();
        auto ret = auth_sgx(data);
        fprintf(stderr, "Json : %s", req->json().dump(4).c_str());
    });
}