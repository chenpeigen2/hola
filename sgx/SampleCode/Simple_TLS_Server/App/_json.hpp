//
// Created by chenpeigen on 23-5-14.
//

#ifndef SGX__JSON_H
#define SGX__JSON_H

// read_json_file START
#include <fstream>
#include <nlohmann/json.hpp>
#include <iostream>
#include <fstream>
#include <iosfwd>
#include <ios>

using json = nlohmann::json;

void read_json_file(json **data, const std::string &file_name) {
    std::ifstream file(file_name);
    std::ifstream valid_file(file_name);
    if (!valid_file.is_open() || !json::accept(valid_file)) {
        file.close();
        valid_file.close();
        return;
    };
    json j = json::parse(file);
    *data = new json(std::move(j));
    file.close();
}

// read_json_file END

#endif //SGX__JSON_H
