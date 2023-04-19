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
    server.GET("/json1", [](const HttpReq *req, HttpResp *resp) {
        Json json;
        json["test"] = 123;
        json["json"] = "test json";
        resp->Json(json);
    });
    /** filter find for **/
    server.POST("/_find", [](const HttpReq *req, HttpResp *resp) {
        if (req->content_type() != APPLICATION_JSON) {
            resp->String("NOT APPLICATION_JSON");
        }
        if (req->has_query("db_name")) {
            const std::string &db_name = req->query("db_name");
            auto filter = req->json().dump(4);
            std::string result = filter_find(db_name, filter);
            resp->Json(result);
        } else {
            resp->String("the query has no db_name or filter");
        }
    });

    /** filter complex for **/
    server.POST("/_complex", [](const HttpReq *req, HttpResp *resp) {
        if (req->content_type() != APPLICATION_JSON) {
            resp->String("NOT APPLICATION_JSON");
        }
        // 遍历数组并输出每个元素
        auto data = req->json().dump();
        std::string result = complex_filter(data);
        resp->String("we are fineee");
    });

    // curl -v http://ip:port/json2
    server.GET("/json2", [](const HttpReq *req, HttpResp *resp) {
        std::string valid_text = R"(
        {
            "numbers": [1, 2, 3]
        }
        )";
        resp->Json(valid_text);
    });

    // curl -v http://ip:port/json3
    server.GET("/json3", [](const HttpReq *req, HttpResp *resp) {
        std::string invalid_text = R"(
        {
            "strings": ["extra", "comma", ]
        }
        )";
        resp->Json(invalid_text);
    });
    server.GET("/json5", [](const HttpReq *req, HttpResp *resp) {
        std::string innnerText = R"(
        {
            "numbers": [1, 2, 3]
        })";
        resp->Json(innnerText);
    });

    // recieve json
    //   curl -X POST http://ip:port/json4
    //   -H 'Content-Type: application/json'
    //   -d '{"login":"my_login","password":"my_password"}'
    server.POST("/json4", [](const HttpReq *req, HttpResp *resp) {
        if (req->content_type() != APPLICATION_JSON) {
            resp->String("NOT APPLICATION_JSON");
            return;
        }
        fprintf(stderr, "Json : %s", req->json().dump(4).c_str());
    });

    server.POST("/json6", [](const HttpReq *req, HttpResp *resp) {
        if (req->content_type() != APPLICATION_JSON) {
            resp->String("NOT APPLICATION_JSON");
            return;
        }
        auto item = req->json().dump(4);
        cout << item << endl;
    });
}