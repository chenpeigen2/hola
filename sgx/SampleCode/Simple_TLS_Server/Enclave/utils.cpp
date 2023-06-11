//
// Created by chenpeigen on 23-4-19.
//
#include "utils.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include "Request.h"
#include <vector>
#include <string>
#include <sstream>
#include <unordered_set>
#include <unordered_map>
#include "Encrypt.h"

using namespace std;

std::vector <std::string> split(const std::string &str, const std::string &pattern) {
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

std::vector <std::string> params(const std::string &params) {
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

/** provide the query associate with the backbend **/
std::vector <Request> provide_query(const std::string &source) {

    int len = sgx_json_arr_length(source.c_str());
    std::vector <Request> res;
    std::unordered_set <std::string> sorted; // 去重逻辑

    for (int i = 0; i < len; ++i) {
        // we acquire the uid associate with the aes key
        std::string uid = sgx_json_query(i, "uid");
        std::string aesKey = client_get_aes_key(uid);
        printf("the aes key: %s \n", aesKey.c_str());

        std::string filter = sgx_json_query(i, "filter");
        // we acquire the filter
        filter = decode(filter.c_str(), filter.length());
        filter = ecb_decrypt(filter.c_str(), filter.length(),
                             reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                             aesKey.length());

        // here we expect to create our own new provide query execution
        int len = sgx_simple_split(filter.c_str());

        // acquire the hashset, which contains all the keys* we need
        auto keys_set = convert_to_set(sgx_acquire_set());

        // restore the data that need to remove for the filter
        std::unordered_map <std::string, std::unordered_set<std::string>> url_remove_map;

        for (int i = 0; i < len; ++i) {
            std::string text = sgx_acquire_vector(i);
            printf("the acquired vector %s \n", text.c_str());
            auto vec = convert_to_vec(text);

            // compute the vec start
            std::unordered_set <std::string> do_has;
            std::unordered_set <std::string> do_not_has;

            // init the do has
            for (const auto &item: vec) {
                if (do_has.count(item) == 0) {
                    do_has.insert(item);
                }
            }
            // init the do not has
            for (const auto &item: keys_set) {
                if (do_has.count(item) == 0 && do_not_has.count(item) == 0) {
                    do_not_has.insert(item);
                }
            }
            // compute the vec end

            std::string urls = sgx_curl_post("http://localhost:3000/aggregate_com",
                                             convert_vec_to_json(vec).c_str());
            auto url_vec = convert_json_to_vec(urls);

            // compute the url_remove_map start
            for (auto it = url_vec.begin(); it != url_vec.end(); ++it) {
                if (it->compare("$url") == 0) {
                    ++it; // move to $url value
                    auto bb_url = *it;
                    ++it; // move to $find
                    if (it->compare("$find") == 0) {
                        ++it; // move to $find value
                        auto bb_find = *it;

                        // compute the url_remove_map for info
                        if (url_remove_map.find(bb_url + bb_find) == 0) {
                            std::unordered_set <std::string> set(do_not_has);
                            url_remove_map.insert(set);
                        } else {
                            auto set = url_remove_map.at(bb_url + bb_find);

                            // if prev the key in that map, we need to remove the key
                            for (const auto &item: do_has) {
                                if (set.count(item) > 0) {
                                    set.erase(item);
                                }
                            }
                            // if now the key do not in the map, we should  add in the map
                            for (const auto &item: do_not_has) {
                                if (set.count(item) == 0) {
                                    set.insert(item);
                                }
                            }

                        }

                    }
                }
            }
            // compute the url_remove_map end

            // iterator
            for (auto it = url_vec.begin(); it != url_vec.end(); ++it) {
                if (it->compare("$url") == 0) {
                    ++it; // move to $url value
                    auto bb_url = *it;
                    if (sorted.count(bb_url) > 0) continue;
                    sorted.insert(bb_url);
                    ++it; // move to $find
                    if (it->compare("$find") == 0) {
                        ++it; // move to $find value
                        auto bb_find = *it;
                        res.push_back(Request(bb_url + bb_find, filter));
                    }
                }
            }

        }
    }

    printf("\n===================================\n");
    for (const auto &item: res) {
        printf("\n the url: %s  the filter: %s \n", item.url.c_str(), item.filter.c_str());
    }
    printf("\n===================================\n");

    return res;
}

/** client-sgx auth execution **/
std::unordered_map <std::string, std::string> client_auth_map;

bool client_auth(const std::string &source) {
    std::string &&uid = sgx_json_object(source.c_str(), "uid");
    std::string &&key = sgx_json_object(source.c_str(), "key");
    printf("client_auth %s %s\n", uid.c_str(), key.c_str());

    key = decode(key.c_str(), key.length());
    key = decrypt(reinterpret_cast<const unsigned char *>(key.c_str()), key.length(),
                  (const unsigned char *) PRIVATE_KEY, strlen(PRIVATE_KEY));

    client_auth_map[uid] = key.substr(0, key.find('\0'));
    printf("client_auth %s %s\n", uid.c_str(), key.c_str());
    return true;
}

std::string client_get_aes_key(const std::string &uid) {
    if (client_auth_map.count(uid) > 0) {
        return client_auth_map[uid];
    }
    return "null";
}


std::string convert_vec_to_json(std::vector <std::string> &vec) {
    std::string ret = "[";
    for (auto it = vec.begin(); it != vec.end(); ++it) {
        ret += "\"" + *it + "\"";
        if (it != vec.end() - 1) {
            ret += ",";
        }
    }
    ret += "]";
    return ret;
}

std::vector <std::string> convert_json_to_vec(std::string basicString) {
    std::vector <std::string> vec;
    std::string token;
    for (size_t i = 1; i < basicString.size() - 1; ++i) {
        if (basicString[i] == '[') {
            continue;
        } else if (basicString[i] == ',') {
            vec.push_back(token);
            token = "";
        } else if (basicString[i] == ']') {
            break;
        } else if (basicString[i] != '\"') {
            token += basicString[i];
        }
    }
    vec.push_back(token);
    return vec;
}

std::vector <std::string> convert_to_vec(const std::string &source) {
    std::vector <std::string> vec;
    std::stringstream ss(source);
    std::string token;
    while (std::getline(ss, token, '#')) {
        vec.push_back(token);
    }
    return vec;
}

std::unordered_set <std::string> convert_to_set(const std::string &source) {
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