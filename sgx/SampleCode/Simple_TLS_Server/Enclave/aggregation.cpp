//
// Created by chenpeigen on 23-5-22.
//
#include "utils.h"
#include <string>
#include <vector>

std::string simple_aggregation(std::vector <std::string> &resp) {
    std::string ret;
    for (const auto &item: resp) {
        ret.append(item);
    }
    return ret;
}