#include "Enclave_t.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include <string.h>
#include <string>
#include <iostream>
#include <cstring>
#include <random>
#include <time.h>

#include <array>
#include <set>
#include <map>
#include "http_utils.h"
#include "encalve_utils.h"
#include "utils.h"

// TODO provide query 重写
// TODO attestion process
#define PUBLICKEY "-----BEGIN RSA PUBLIC KEY-----\nMIGHAoGBALZrtub6dr1iEJ+JZhlTvH730yS0xb16XyyN36/QAS22H3k2H/p/tbNz\n3J3Cy44lK7w4oNadOwRgITjDInhmbDqcKlFiI7XPCC351EubsgiF1yOwjVKMsvKm\n1OgknVpvYTd2roKTWJ8yknfqWPxwsAUspGpbx51Jt/zuYXLFhlj/AgED\n-----END RSA PUBLIC KEY-----"
#define PRIVATE_KEY "-----BEGIN RSA PRIVATE KEY-----\nMIICWwIBAAKBgQC2a7bm+na9YhCfiWYZU7x+99MktMW9el8sjd+v0AEtth95Nh/6\nf7Wzc9ydwsuOJSu8OKDWnTsEYCE4wyJ4Zmw6nCpRYiO1zwgt+dRLm7IIhdcjsI1S\njLLyptToJJ1ab2E3dq6Ck1ifMpJ36lj8cLAFLKRqW8edSbf87mFyxYZY/wIBAwKB\ngHmdJJn8TyjsCxUGRBDifan6jMMjLn5RlMhelR/gAMkkFPt5aqb/zneikxPXMl7D\ncn17FeRo0gLqwNCCFvru8tCcvtCCbgkwre72cGuL/ggN0MvLFQZGwr3tCmhWSAGJ\nnSyg3zzqyiUay1FTqBDNqGmlXyLwIcCzqqQVJlcBmv87AkEA5grvj0F75uSHzzOn\n90FSim17VXWNVzr7iSerg6nAFnrTqk/dOe0/KYQ1O9gi1ZWvPPFdpI+YRbCfZaOi\nBpI6PQJBAMsBKQ89LB8ev7j4AlJzqeawdqp4O8tT2jodoB+HmAp6oNsPyfk8KGDd\nSLEEwGadt3ekWE8FY/aHZ2kETjyLn+sCQQCZXJ+01lKZ7a/fd8VPgOGxnlI4+Qjk\n0f0GGnJXxoAO/I0cNT4mniobrXjSkBc5DnTTS5PDCmWDyxTubRavDCbTAkEAh1Yb\nX34dahR/0KVW4aJxRHWkcaV9Mjfm0WkValplXFHAkgqGpiga6z4wdgMq7xPPpRg6\n31jtTwTvm1g0KF0VRwJAY0rj3NWey/4apiEdrNV143w83iQtFTDliKXB8voAxk6y\npZkunO7IzRcbnC2+132e0/G9LdLGbVqgHO4i2leMYw==\n-----END RSA PRIVATE KEY-----"

using namespace std;
using namespace oceanstar;

std::string sk;

// indicate the authed set
std::set <std::string> authSet;
std::map <std::string, std::string> auth_map;

//// indicate the refresh rate
#define ELAPSE_MINUTES 15
time_t start;

bool refreshIfNeeded() {
    time_t now = sgx_time(nullptr);
    if (start == 0) {
        start = now;
    }
    double seconds = difftime(now, start);
    if (seconds >= ELAPSE_MINUTES * 60) {
        start = now;
        return true;
    }
    return false;
}

string random_string(int len) {
    string str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    random_device rd;
    mt19937 generator(rd());
    shuffle(str.begin(), str.end(), generator);
    return str.substr(0, len);
}

std::string generateAESKey() {
    return random_string(24);
}

std::string auth_if_needed(std::string &&proto, std::string &&domain, short unsigned &&port) {
    std::string url = proto + "://" + domain + ":" + to_string(port) + "/_auth";
    bool refresh = refreshIfNeeded();
    if (auth_map.count(url) > 0 && !refresh) {
        return auth_map.at(url);
    }
    std::string &&aesKey = generateAESKey();
    std::string encryptKey = encrypt(reinterpret_cast<const unsigned char *>(aesKey.c_str()), aesKey.length(),
                                     (const unsigned char *) PUBLICKEY, strlen(PUBLICKEY));
    encryptKey = encode(encryptKey.c_str(), encryptKey.length());
    std::string body = "{\"key\":\"" +
                       encryptKey +
                       "\"}";
    sgx_curl_post(url.c_str(), body.c_str());
    auth_map[url] = aesKey;
    return aesKey;
}

std::string curl_get(const char *url, const char *request) {
    http_url httpUrl;
    if (!httpUrl.parse(url)) {
        exit(0);
    }

    std::string aesKey = auth_if_needed(string(httpUrl.get_proto()), string(httpUrl.get_domain()),
                                        httpUrl.get_port());

    vector <string> param = params(request);
    std::string base_url(url);
    std::string res;
    for (int i = 0; i < param.size(); ++i) {
        std::string p = param[i];
        if (i != 0 && i % 2 == 0) {
            res += "&";
        }
        if (i % 2 != 0) {
            res += "=";
            p = ecb_encrypt(p.c_str(), reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                            aesKey.length(), PaddingModel::PKCS5OR7);
            p = encode(p.c_str(), p.length());
        }
        res += p;
    }
    std::string final_url = base_url + "?" + res;
    /** curl get **/
    printf("Encalve final url: %s \n", final_url.c_str());
    std::string response = sgx_curl_get(final_url.c_str());
    printf("The RESPONSE: %s\n", response.c_str());
    // decrypt
    response = decode(response.c_str(), response.length());
    response = ecb_decrypt(response.c_str(), response.length(),
                           reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                           aesKey.length());
    return response;
}


// ### below are the ecall ###

char *ecall_curl_get(const char *url, const char *request) {
    std::string response = curl_get(url, request);
    // assemble
    int len = response.length();
    char *p = new char[len + 1];
    response.copy(p, len);
    p[len] = '\0';
    return p;
}

char *ecall_complex_curl(const char *data) {
    // provide the requests
    auto provide_decode_data = provide_query(data);
    // the result
    std::string response;
    // perform execution
    for (const auto &decode_data: provide_decode_data) {
        std::string &&return_data = curl_get(decode_data.url.c_str(), decode_data.request_data.c_str());
        response.append(return_data);
    }
    // assemble
    int len = response.length();
    char *p = new char[len + 1];
    response.copy(p, len);
    p[len] = '\0';
    return p;
}

void ecall_curl_get_response(const char *response) {
    printf("response return the data from the APP side: %s\n", response);
}

char *ecall_test() {
    std::string str = "abcdefghijklmnopqrstuvwxyz123456789010 excited so ssososososo, Trusted function will be treated as [private] w/o the [public]";
    int len = str.length();
    printf("the length %d \n", len);
    char *p = new char[len];
    strncpy(p, str.c_str(), len);
    return p;
}

char *ecall_test_params(const char *request) {
    std::string str = "Trust the ecall_test_params";
    int len = str.length();
    printf("the length %d \n", len);
    char *p = new char[len];
    strncpy(p, str.c_str(), len);
    return p;
}

std::string ecall_string_test() {
    std::string str = "after milline of da Following keywords/attributes are supported for untrusted functions: we should treat it ok";
    return str;
}

void ecall_generate_rsa() {
    std::string pub_key;
    std::string pri_key;
    GenerateRSAKey(pub_key, pri_key);
    sgx_save_rsa(pub_key.c_str(), pri_key.c_str());
}

void ecall_start_tls_server(void) {
    /* Handle SSL/TLS connections */
    std::string src_text = "test begin\n this is an rsa test example!!! this is an rsa test example!!! this is an rsa test example!!! this is an rsa test example!!! this is an rsa test example!!! this is an rsa test example!!! this is an rsa test example!!! this is an rsa test example!!! this is an rsa test example!!! this is an rsa test example!!! this is an rsa test example!!! this is an rsa test example!!! this is an rsa test example!!! \ntest end";
    //src_text = "rsa test";

    std::string encrypt_text;
    std::string decrypt_text;
    // 生成密钥对
    std::string pub_key;
    std::string pri_key;
    GenerateRSAKey(pub_key, pri_key);
    printf("public key:\n");
    printf("%s\n", pub_key.c_str());
    printf("private key:\n");
    printf("%s\n", pri_key.c_str());

    // 私钥加密-公钥解密
    encrypt_text = RsaPriEncrypt(src_text, pri_key);
    printf("encrypt: len=%d\n", encrypt_text.length());
    decrypt_text = RsaPubDecrypt(encrypt_text, pub_key);
    printf("decrypt: len=%d\n", decrypt_text.length());
    printf("decrypt: %s\n", decrypt_text.c_str());

    // 公钥加密-私钥解密
    encrypt_text = RsaPubEncrypt(src_text, pub_key);
    printf("encrypt: len=%d\n", encrypt_text.length());
    decrypt_text = RsaPriDecrypt(encrypt_text, pri_key);
    printf("decrypt: len=%d\n", decrypt_text.length());
    printf("decrypt: %s\n", decrypt_text.c_str());

    // test RSA
    char *data = "ABCDEFGHIJ123456";
    auto abc = encrypt((unsigned char *) data, strlen(data),
                       (const unsigned char *) PUBLICKEY, strlen(PUBLICKEY));
    printf("encoded string: %s\n", encode(abc.c_str(), abc.length()).c_str());

    // test AES
    std::string src = "123ABC";
    std::string aesKey = "0123456789abcdefghijklmn";
    std::string ecb_aes = ecb_encrypt(src.c_str(), reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                                      strlen(aesKey.c_str()), PaddingModel::PKCS5OR7);
    printf("encoded aes string: %s\n", encode(ecb_aes.c_str(), ecb_aes.length()).c_str());
}

void ecall_type_struct(struct struct_foo_t val) {

}


int ecall_auth(const char *data) {
    printf("the ecall_auth: %s", data);
    return 1;
}
