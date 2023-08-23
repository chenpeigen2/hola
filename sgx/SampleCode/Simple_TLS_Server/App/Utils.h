//
// Created by chenpeigen on 23-2-19.
//

#ifndef SGX_UTILS_H
#define SGX_UTILS_H

#include <string>
#include <vector>
#include <unordered_set>
#include <nlohmann/json.hpp>

using json = nlohmann::json;

using namespace std;

void transform(char *str, std::string &src, int len);

void toString(char *source, std::string &dest);

std::vector <std::string> params(const string &params);

std::unordered_set <std::string> convert_to_set(const std::string &&source);

void iter_arr(json &obj, std::unordered_set <std::string> &keys);

void iter_object(json &obj, std::unordered_set <std::string> &keys);

#endif //SGX_UTILS_H
