//
// Created by chenpeigen on 23-2-19.
//
#include "Utils.h"
#include <string>
#include <cstring>
#include <iostream>
#include <string>
#include <vector>
#include <fstream>

using namespace std;

void transform(char *str, std::string &src, int len) {
    strncpy(str, src.c_str(), len);
}

void toString(char *source, std::string &dest) {
    dest = source;
}

vector <string> split(const string &str, const string &pattern) {
    vector <string> res;
    if (pattern.empty()) return res;
    size_t start = 0, end = 0;
    while ((end = str.find(pattern, start)) != string::npos) {
        if (end > start) res.push_back(str.substr(start, end - start));
        start = end + pattern.size();
    }
    if (start < str.size()) res.push_back(str.substr(start));
    return res;
}

vector <string> params(const string &params) {
    vector <string> res;
    vector <string> p1 = split(params, "&");
    for (const auto &re1: p1) {
        vector <string> p2 = split(re1, "=");
        for (const auto &re2: p2) {
            res.push_back(re2);
        }
    }
    return res;
}

std::unordered_set <std::string> convert_to_set(const std::string &&source) {
    std::unordered_set <std::string> set;
    std::stringstream ss(source);
    std::string token;
    while (std::getline(ss, token, '#')) {
        if (set.count(token) == 0) {
            set.insert(token);
        }
    }
    return set;
}