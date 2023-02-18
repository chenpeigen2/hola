//
// Created by chenpeigen on 23-2-12.
//
#include "Curl.h"

#include <iostream>
#include <iostream>
#include <string>
#include <chrono>
#include <string_view>
#include "curl/curl.h"
#include <sstream>

//回调函数
size_t write_data(void *ptr, size_t size, size_t nmemb, void *stream) {
    std::string data((const char *) ptr, (size_t) size * nmemb);
    *((std::stringstream *) stream) << data << std::endl;
    return size * nmemb;
}

void hello_world_curl() {
    CURL *curl;
    curl = curl_easy_init();
    std::stringstream out;
    if (curl) {
        curl_easy_setopt(curl, CURLOPT_URL, "http://localhost:5984/");
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_data);//设置回调函数
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &out);
        curl_easy_perform(curl);
        curl_easy_cleanup(curl);
    }
    std::string s = out.str();
    std::cout << s << std::endl;
}

CURLcode sgx_post(const std::string &url, const std::string &data, std::string &header, std::string &response,
                  int timeout) {
    CURLcode res;
    CURL *curl = curl_easy_init();
    struct curl_slist *headers = nullptr;
    headers = curl_slist_append(headers, "Content-Type:application/json;charset=UTF-8");
    if (curl == nullptr) {
        return CURLE_FAILED_INIT;
    }
    curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
    curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);
    curl_easy_setopt(curl, CURLOPT_HEADER, 0);
    curl_easy_setopt(curl, CURLOPT_TIMEOUT_MS, timeout);
    curl_easy_setopt(curl, CURLOPT_POSTFIELDS, data.c_str());
    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_data);
    std::stringstream out;
    std::stringstream header_out;
    curl_easy_setopt(curl, CURLOPT_WRITEDATA, &out);
    curl_easy_setopt(curl, CURLOPT_HEADERDATA, &header_out);
    res = curl_easy_perform(curl);
    curl_easy_cleanup(curl);
    header = header_out.str();
    response = out.str();
    return res;
}

void sgx_curl_test() {

    hello_world_curl();
    std::string valid_text = R"(
        {
            "name": "admin",
            "password": "password"
        }
        )";

    std::string url = "http://localhost:5984/_session";
    std::string header;
    std::string response;

    CURLcode res = sgx_post(url, valid_text, header, response, 300);
    std::cout << res << std::endl;
    std::cout << header << std::endl;
    std::cout << response << std::endl;
}