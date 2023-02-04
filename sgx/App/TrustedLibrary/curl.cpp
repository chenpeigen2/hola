//
// Created by chenpeigen on 23-2-4.
//
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

void get_data() {
    // cur test
    CURL *curl;
    CURLcode res;
    curl = curl_easy_init();

    std::stringstream out;

    if (curl) {
        curl_easy_setopt(curl, CURLOPT_URL, "https://www.baidu.com");
        //写出数据
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_data);//设置回调函数
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &out);
        res = curl_easy_perform(curl);


        curl_easy_cleanup(curl);
    }

    std::string s = out.str();
    std::cout << s << std::endl;
    std::cout << "what are you doing !!!" << std::endl;
}
