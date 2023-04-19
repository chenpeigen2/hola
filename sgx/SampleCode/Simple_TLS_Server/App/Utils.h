//
// Created by chenpeigen on 23-2-19.
//

#ifndef SGX_UTILS_H
#define SGX_UTILS_H

#include <string>
#include <vector>

using namespace std;

void transform(char *str, std::string &src, int len);

void toString(char *source, std::string &dest);

std::vector <std::string> params(const string &params);

#endif //SGX_UTILS_H
