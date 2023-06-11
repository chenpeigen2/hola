//
// Created by chenpeigen on 23-6-10.
//

#include "match.h"
#include <string>
#include <vector>
#include "utils.h"

// define the simple match backbend func
std::vector <std::string> simple_match(const std::string &&body) {

    std::string urls = sgx_curl_post("http://localhost:3000/aggregate_com",
                                     body.c_str());
    return convert_json_to_vec(urls);
}

//define the simple match process
