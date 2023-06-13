#include "Ocall_implements.h"
#include "Enclave_u.h"
#include <fstream>
#include <cstring>
#include <iostream>
#include "App.h"
#include "Curl.h"
#include <iomanip>
#include <nlohmann/json.hpp>
#include <vector>
#include <string>
#include <unordered_set>
#include "_json.hpp"
#include "Utils.h"

using json = nlohmann::json;

#define PUB_KEY_FILE "pubkey.pem"
#define PRI_KEY_FILE "prikey.pem"

long ocall_sgx_clock(void) {
    struct timespec tstart = {0, 0};
    clock_gettime(CLOCK_MONOTONIC, &tstart);
    return tstart.tv_sec * 1000000 + tstart.tv_nsec / 1000; // Return micro seconds
}

time_t ocall_sgx_time(time_t *timep, int t_len) {
    return time(timep);
}

struct tm *ocall_sgx_localtime(const time_t *timep, int t_len) {
    return localtime(timep);
}

struct tm *ocall_sgx_gmtime_r(const time_t *timep, int t_len, struct tm *tmp, int tmp_len) {
    return gmtime_r(timep, tmp);
}

int ocall_sgx_gettimeofday(void *tv, int tv_size) {
    return gettimeofday((struct timeval *) tv, NULL);
}

int ocall_sgx_getsockopt(int s, int level, int optname, char *optval, int optval_len, int *optlen) {
    return getsockopt(s, level, optname, optval, (socklen_t *) optlen);
}

int ocall_sgx_setsockopt(int s, int level, int optname, const void *optval, int optlen) {
    return setsockopt(s, level, optname, optval, optlen);
}

int ocall_sgx_socket(int af, int type, int protocol) {
    int retv;
    retv = socket(af, type, protocol);
    return retv;
}

int ocall_sgx_bind(int s, const void *addr, int addr_size) {
    return bind(s, (struct sockaddr *) addr, addr_size);
}

int ocall_sgx_listen(int s, int backlog) {
    return listen(s, backlog);
}

int ocall_sgx_connect(int s, const void *addr, int addrlen) {
    int retv = connect(s, (struct sockaddr *) addr, addrlen);
    return retv;
}

int ocall_sgx_accept(int s, void *addr, int addr_size, int *addrlen) {
    return accept(s, (struct sockaddr *) addr, (socklen_t *) addrlen);
}

int ocall_sgx_shutdown(int fd, int how) {
    return shutdown(fd, how);
}

int ocall_sgx_read(int fd, void *buf, int n) {
    return read(fd, buf, n);
}

int ocall_sgx_write(int fd, const void *buf, int n) {
    return write(fd, buf, n);
}

int ocall_sgx_close(int fd) {
    return close(fd);
}

int ocall_sgx_getenv(const char *env, int envlen, char *ret_str, int ret_len) {
    const char *env_val = getenv(env);
    if (env_val == NULL) {
        return -1;
    }
    memcpy(ret_str, env_val, strlen(env_val) + 1);
    return 0;
}

void ocall_print_string(const char *str) {
    printf("%s", str);
}

void ocall_save_rsa(const char *publickey, const char *privatekey) {
    std::ofstream pub_file(PUB_KEY_FILE, std::ios::out);
    if (!pub_file.is_open()) {
        perror("pub key file open fail:");
        return;
    }
    pub_file << publickey;
    pub_file.close();
    std::ofstream pri_file(PRI_KEY_FILE, std::ios::out);
    if (!pri_file.is_open()) {
        perror("pri key file open fail:");
        return;
    }
    pri_file << privatekey;
    pri_file.close();
}

/** may not be prepare to localization **/
char *ocall_curl_get(const char *url) {
    std::string response;
    sgx_get(url, response, 300); // curl
    int len = response.length();
    char *p = new char[len + 1];
    strncpy(p, response.c_str(), len);
    p[len] = '\0';
    return p;
}

char *ocall_curl_post(const char *url, const char *body) {
    std::string response;
    sgx_post(url, body, response, 300);
    int len = response.length();
    char *p = new char[len + 1];
    strncpy(p, response.c_str(), len);
    p[len] = '\0';
    return p;
}

/** arr we are restoring **/
json j_arr = nullptr;

int ocall_json_arr_length(const char *data) {
    if (json::accept(data)) {
        j_arr = json::parse(data);
        if (!j_arr.is_array()) return -1;
        return j_arr.size();
    } else {
        return -1;
    }
}

char *ocall_json_query(int idx, const char *key) {
    if (j_arr != nullptr) {
        auto j_object = j_arr.at(idx);
        std::cout << j_object.contains(key) << std::endl;
        if (j_object.contains(key)) {
            std::string response;
            if (j_object.at(key).is_object()) {
                response = j_object.at(key).dump();
            } else {
                response = j_object.at(key).get<std::string>();
            }
            int len = response.length();
            char *p = new char[len + 1];
            strncpy(p, response.c_str(), len);
            p[len] = '\0';
            return p;
        }
    }
    return nullptr;
}


char *ocall_json_object(const char *data, const char *key) {
    if (json::accept(data)) {
        auto j_object = json::parse(data);
        if (!j_object.is_object()) return nullptr;
        if (j_object.contains(key)) {
            std::string response;
            if (j_object.at(key).is_object()) {
                response = j_object.at(key).dump();
            } else if (j_object.at(key).is_string()) {
                response = j_object.at(key).get<std::string>();
            }
            int len = response.length();
            char *p = new char[len + 1];
            strncpy(p, response.c_str(), len);
            p[len] = '\0';
            return p;
        }
    }
    return nullptr;
}

json *json_data;
json *key_data;

/** read the given json file object and get the key if needed **/
int ocall_json_file_read(const char *key, const char *file_name) {
//    key_map_t
    if (json_data == nullptr) {
        read_json_file(&json_data, file_name);
    }
    if (json_data == nullptr) return -2;
    // we expect the json_data must be the object
    if (json_data->is_object()) {
        if (json_data->contains(key)) {
            if (key_data != nullptr) {
                delete key_data;
            }
            key_data = new json(json_data->at(key));
            if (key_data->is_array()) {
                std::cout << key_data->dump() << std::endl;
                std::cout << key_data->at(0).dump() << std::endl;
                return key_data->size();
            }
            return 0;
        }
        return -1;
    }
    return -3;
}

/** only the config.json array usage **/
struct key_map_t ocall_key_map_struct(int idx) {
    struct key_map_t ret{idx};
    json key_object = key_data->at(idx);
    std::string query = key_object.at("query").get<std::string>();
    std::string db_name = key_object.at("db_name").get<std::string>();
    // provide query
    int len = query.length();
    ret.query = new char[len + 1];
    strncpy(ret.query, query.c_str(), len);
    ret.query[len] = '\0';
    // provide db_name
    len = db_name.length();
    ret.db_name = new char[len + 1];
    strncpy(ret.db_name, db_name.c_str(), len);
    ret.db_name[len] = '\0';
    // return the data
    return ret;
}


std::vector <std::vector<std::string>> *simple_split_res;
std::unordered_set <std::string> *simple_keys_set;

/** simple impl for split execution **/
int ocall_simple_split(const char *data) {
    // we need to parser the given json_string for further execution
    json j_test = json::parse(data);

    auto selector = j_test.at("selector");

    std::unordered_set <std::string> keys_set;

    std::vector <std::vector<std::string>> nestedVector;
    std::cout << nestedVector.size() << std::endl;
    if (selector.contains("$or")) {
        auto $or = selector.at("$or");
        for (auto &number_object: $or) {
            std::vector <std::string> vec; // define a vec
            // 遍历对象并打印每个键
            for (auto &element: number_object.items()) {
                std::cout << element.key() << std::endl;
                if (keys_set.count(element.key()) == 0) {
                    keys_set.insert(element.key());
                }
                vec.push_back(element.key());
            }
            nestedVector.push_back(vec);
        }
    }
    if (nestedVector.empty()) {
        std::vector <std::string> vec;
        for (auto &element: selector.items()) {
            if (keys_set.count(element.key()) == 0) {
                keys_set.insert(element.key());
            }
            vec.push_back(element.key());
        }
        nestedVector.push_back(vec);
    } else {
        for (auto &element: selector.items()) {
            if (element.key() != "$or") {
                for (auto &number_object: nestedVector) {
                    if (keys_set.count(element.key()) == 0) {
                        keys_set.insert(element.key());
                    }
                    number_object.push_back(element.key());
                }
            }
        }
    }
    if (simple_split_res != nullptr) {
        delete simple_split_res;
    }
    if (simple_keys_set != nullptr) {
        delete simple_keys_set;
    }
    simple_split_res = new std::vector <std::vector<std::string>>(nestedVector);
    simple_keys_set = new std::unordered_set<std::string>(keys_set);
    std::cout << selector.dump() << std::endl;
    return simple_split_res->size();
}

void iter_arr(json & obj, std::unordered_set < std::string > &keys) {
    if (!obj.is_array()) return;
    int i = 0;
    std::vector<int> vec;
    for (auto &element: obj.items()) {
        iter_object(element.value(), keys);
        if (element.value().size() == 0) {
            vec.push_back(i);
        }
        i++;
    }
    for (const auto &item: vec) {
        obj.erase(item);
    }
}

void iter_object(json & obj, std::unordered_set < std::string > &keys) {
    if (!obj.is_object()) return;
    for (auto it = obj.begin(); it != obj.end();) {
        if (it.value().is_array()) {
            iter_arr(it.value(), keys);
            ++it;
            continue;
        }
        if (keys.count(it.key()) > 0) {
            it = obj.erase(it);
        } else {
            ++it;
        }
    }
}


char *ocall_query_trim(const char *json_str, const char *keys) {
    // we need to parser the given json_string for further execution
    json j_test = json::parse(json_str);
    std::cout << j_test.dump() << std::endl;
    auto &selector = j_test.at("selector"); // find the selector
    // constructor a keys_set
    auto keys_set = convert_to_set(keys);
    iter_object(selector, keys_set);

    std::cout << "keys: " << keys << std::endl;

    std::cout << "dumppppppppppppp" << j_test.dump() << std::endl;
    std::string str = "the result return from the ocall_query_trim";
    size_t len = str.length();
    char *p = new char[len + 1];
    str.copy(p, len);
    p[len] = '\0';
    return p;
}

char *ocall_acquire_vector(int idx) {
    auto vec = simple_split_res->at(idx);
    std::string str;
    for (auto it = vec.begin(); it != vec.end(); ++it) {
        str += *it;
        if (it != vec.end() - 1) {
            str += "#";
        }
    }

    size_t len = str.length();
    char *p = new char[len + 1];
    str.copy(p, len);
    p[len] = '\0';
    return p;
}

char *ocall_acquire_set() {
    std::string str = "";
    for (auto it = simple_keys_set->begin(); it != simple_keys_set->end(); ++it) {
        str += *it + "#";
    }
    str.pop_back();

    size_t len = str.length();
    char *p = new char[len + 1];
    str.copy(p, len);
    p[len] = '\0';
    return p;
}


void ocall_sgx_exit(int e) {
    exit(e);
}
