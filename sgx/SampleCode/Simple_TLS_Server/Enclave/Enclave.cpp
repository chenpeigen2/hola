#include "Enclave_t.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include "string.h"
#include "vector"

#include <iostream>
#include <string>
#include <vector>

using namespace std;

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

char *ecall_curl_get(const char *url, const char *request) {
    vector <string> param = params(request);
    std::string base_url(url);
    std::string aesKey = "0123456789abcdefghijklmn";
    // db_name=MjRpTSD5v4iCxUTzs%2BbrYw%3D%3D&filter=mMksrdiK%2Ba2gsXr7zZoMRPT06wcXxHhQ6OyXLzsVVBQYCvY9k9sElFXtPphQHH1rYnvIpPk7jIVZY9fUQJPoZyRYz%2FF3B7PvyzBv6IEHVfJjCxeiTcd%2F14DO%2BcW%2B3WiC
    // db_name=MjRpTSD5v4iCxUTzs+brYw==&filter=P9kMC8Zp/s+917GG7P0LCl1wlBMMmFFzqa2CNQeYsxsp/8xWCL6zkZlNVwB+oZHClEt9wWFQ7TF8sOJCOM1tlX1BjYb+XxtsztatwIzPJJk=
    std::string res;
    for (int i = 0; i < param.size(); ++i) {
        std::string p = param[i];
        if (i != 0 && i % 2 == 0) {
            res += "&";
        }
        if (i % 2 != 0) {
            res += "=";
            p = ecb_encrypt(p.c_str(), reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                            strlen(aesKey.c_str()), PaddingModel::PKCS5OR7);
            p = encode(p.c_str(), p.length());
        }
        res += p;
    }
    std::string final_url = base_url + "?" + res;
    /** test for curl get **/
    printf("Encalve final url: %s \n", final_url.c_str());
    std::string response = sgx_curl_get(final_url.c_str());
    printf("\nThe RESPONSE: %s\n", response.c_str());
    // decrypt
    std::string dede = ecb_decrypt(response.c_str(), strlen(response.c_str()),
                                   reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                                   strlen(aesKey.c_str()));
    dede = encode(dede.c_str(), dede.length());
    printf("\ndede %s", dede.c_str());


    {
        std::string p1 = "we are fucking okkkkkkkk";
        p1 = ecb_encrypt(p1.c_str(), reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                         strlen(aesKey.c_str()), PaddingModel::PKCS5OR7);
        printf("\n%s", p1.c_str());
        p1 = encode(p1.c_str(), p1.length());
        printf("\n%s", p1.c_str());

        std::string ree11 = "W12EPeq+s0pZXlyNBOvMAyeBFwIcjVe/cv0he21xtio=";

        ree11 = decode(ree11.c_str(), strlen(ree11.c_str()));

        std::string text = ecb_decrypt(ree11.c_str(), strlen(ree11.c_str()),
                                       reinterpret_cast<const unsigned char *>(aesKey.c_str()),
                                       strlen(aesKey.c_str()));
//        text = decode(text.c_str(), text.length());
        printf("\nthe text: %s\n", text.c_str());
    }


    std::string test1 = "we are ok\"selector\"";
    printf("\n%s\n", test1.c_str());
    std::string str = "abcdefghijklmnopqrstuvwxyz123456789010 excited so ssososososo, Trusted function will be treated as [private] w/o the [public]";
    int len = str.length();
    printf("the length %d \n", len);
    char *p = new char[len + 1];
    strncpy(p, str.c_str(), len);
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

#define PUBLICKEY "-----BEGIN RSA PUBLIC KEY-----\nMIGHAoGBALZrtub6dr1iEJ+JZhlTvH730yS0xb16XyyN36/QAS22H3k2H/p/tbNz\n3J3Cy44lK7w4oNadOwRgITjDInhmbDqcKlFiI7XPCC351EubsgiF1yOwjVKMsvKm\n1OgknVpvYTd2roKTWJ8yknfqWPxwsAUspGpbx51Jt/zuYXLFhlj/AgED\n-----END RSA PUBLIC KEY-----"
#define PRIVATE_KEY "-----BEGIN RSA PRIVATE KEY-----\nMIICWwIBAAKBgQC2a7bm+na9YhCfiWYZU7x+99MktMW9el8sjd+v0AEtth95Nh/6\nf7Wzc9ydwsuOJSu8OKDWnTsEYCE4wyJ4Zmw6nCpRYiO1zwgt+dRLm7IIhdcjsI1S\njLLyptToJJ1ab2E3dq6Ck1ifMpJ36lj8cLAFLKRqW8edSbf87mFyxYZY/wIBAwKB\ngHmdJJn8TyjsCxUGRBDifan6jMMjLn5RlMhelR/gAMkkFPt5aqb/zneikxPXMl7D\ncn17FeRo0gLqwNCCFvru8tCcvtCCbgkwre72cGuL/ggN0MvLFQZGwr3tCmhWSAGJ\nnSyg3zzqyiUay1FTqBDNqGmlXyLwIcCzqqQVJlcBmv87AkEA5grvj0F75uSHzzOn\n90FSim17VXWNVzr7iSerg6nAFnrTqk/dOe0/KYQ1O9gi1ZWvPPFdpI+YRbCfZaOi\nBpI6PQJBAMsBKQ89LB8ev7j4AlJzqeawdqp4O8tT2jodoB+HmAp6oNsPyfk8KGDd\nSLEEwGadt3ekWE8FY/aHZ2kETjyLn+sCQQCZXJ+01lKZ7a/fd8VPgOGxnlI4+Qjk\n0f0GGnJXxoAO/I0cNT4mniobrXjSkBc5DnTTS5PDCmWDyxTubRavDCbTAkEAh1Yb\nX34dahR/0KVW4aJxRHWkcaV9Mjfm0WkValplXFHAkgqGpiga6z4wdgMq7xPPpRg6\n31jtTwTvm1g0KF0VRwJAY0rj3NWey/4apiEdrNV143w83iQtFTDliKXB8voAxk6y\npZkunO7IzRcbnC2+132e0/G9LdLGbVqgHO4i2leMYw==\n-----END RSA PRIVATE KEY-----"


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
