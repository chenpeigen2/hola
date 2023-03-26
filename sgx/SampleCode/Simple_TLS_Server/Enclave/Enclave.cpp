#include "Enclave_t.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include "string.h"
#include <cstring>
#include <openssl/ssl.h>
#include <openssl/x509.h>
#include <openssl/pem.h>
#include <openssl/err.h>
#include <string>
#include <ostream>
#include <iostream>

void ecall_hello_world_string() {
    printf("ecall_hello_world_string\n");
}

void ecall_curl_get(const char *request, char *response) {
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

    /** test for curl get **/
    printf("test for curl get begin ---------\n");
    std::string params = "9876543";
    char response[2000] = {};
    printf("response before: %s\n", response);
    sgx_curl_get(params.c_str(), response);
    printf("response after: %s\n", response);
    printf("test for curl get end ---------\n");
}
