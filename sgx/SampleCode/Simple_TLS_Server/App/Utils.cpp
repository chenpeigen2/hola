//
// Created by chenpeigen on 23-2-19.
//
#include "Utils.h"
#include <string>
#include <cstring>

void transform(char *str, std::string &src, int len) {
    strncpy(str, src.c_str(), len);
}

void toString(char *source, std::string &dest) {
    dest = source;
}