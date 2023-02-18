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

/** for auth only **/
CURLcode sgx_auth(const std::string &url, const std::string &data, std::string &response, int timeout) {
    CURLcode res;
    CURL *curl = curl_easy_init();
    struct curl_slist *headers = nullptr;
    headers = curl_slist_append(headers, "Content-Type:application/json;charset=UTF-8");
    if (curl == nullptr) {
        return CURLE_FAILED_INIT;
    }
    curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
    curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);
    curl_easy_setopt(curl, CURLOPT_TIMEOUT_MS, timeout);
    curl_easy_setopt(curl, CURLOPT_POSTFIELDS, data.c_str());
    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_data);
    curl_easy_setopt(curl, CURLOPT_COOKIEJAR, "/tmp/cookies.txt");
    std::stringstream out;
    curl_easy_setopt(curl, CURLOPT_WRITEDATA, &out);
    res = curl_easy_perform(curl);
    curl_easy_cleanup(curl);
    response = out.str();
    return res;
}

/** for http post **/
CURLcode sgx_post(const std::string &url, const std::string &data, std::string &response,
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
    curl_easy_setopt(curl, CURLOPT_COOKIEFILE, "/tmp/cookies.txt");
    curl_easy_setopt(curl, CURLOPT_TIMEOUT_MS, timeout);
    curl_easy_setopt(curl, CURLOPT_POSTFIELDS, data.c_str());
    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_data);
    std::stringstream out;
    curl_easy_setopt(curl, CURLOPT_WRITEDATA, &out);
    res = curl_easy_perform(curl);
    curl_easy_cleanup(curl);
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
    std::string response;

    CURLcode res = sgx_auth(url, valid_text, response, 300);
    std::cout << res << std::endl;
    std::cout << response << std::endl;

    url = "http://localhost:5984/albums";
    valid_text = R"(
            {
              "servings": 4,
              "subtitle": "Delicious with fresh bread",
              "title": "Fish Stew"
            }
        )";
    res = sgx_post(url, valid_text, response, 300);
    std::cout << res << std::endl;
    std::cout << response << std::endl;

}