//
// Created by chenpeigen on 23-2-12.
//
#include "Enclave_t.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include <openssl/ssl.h>
#include <openssl/x509.h>
#include <openssl/pem.h>
#include <openssl/err.h>
#include <string>

#define KEY_LENGTH  2048             // 密钥长度

std::string encrypt(const unsigned char *src, size_t src_len,
                    const unsigned char *pub_key, size_t key_len) {
    BIO *keybio = nullptr;
    keybio = BIO_new_mem_buf(pub_key, key_len);
    if (keybio == nullptr) {
//        LOGD("init key error null");
        return "";
    }
    RSA *rsa = nullptr;
    rsa = PEM_read_bio_RSAPublicKey(keybio, nullptr, nullptr, nullptr);
    if (rsa == nullptr) {
//        LOGD("init RSA error null");
        BIO_free_all(keybio);
        return "";
    }

    int len = RSA_size(rsa);
    std::string rsa_ret(len, 0);
    int ret = RSA_public_encrypt(src_len, src, (unsigned char *) rsa_ret.c_str(), rsa, RSA_PKCS1_PADDING);
    RSA_free(rsa);
    BIO_free_all(keybio);
    CRYPTO_cleanup_all_ex_data();
    if (ret > 0) return rsa_ret;
    return "";
}

std::string decrypt(const unsigned char *src, size_t src_len,
                    const unsigned char *pri_key, size_t key_len) {
    BIO *keybio = nullptr;
    keybio = BIO_new_mem_buf(pri_key, key_len);
    if (keybio == nullptr) {
//        LOGD("init key error null");
        return "";
    }
    RSA *rsa = nullptr;
    rsa = PEM_read_bio_RSAPrivateKey(keybio, nullptr, nullptr, nullptr);
    if (rsa == nullptr) {
//        LOGD("init RSA error null");
        BIO_free_all(keybio);
        return "";
    }
    int len = RSA_size(rsa);
    std::string rsa_ret(len, 0);
    int ret = RSA_private_decrypt(len, src, (unsigned char *) rsa_ret.c_str(), rsa, RSA_PKCS1_PADDING);
    RSA_free(rsa);
    BIO_free_all(keybio);
    CRYPTO_cleanup_all_ex_data();
    if (ret > 0) return rsa_ret;
    return "";
}

void createRSAKey(std::string &pub_key_str, std::string &pri_key_str) {
    RSA *rsa = RSA_generate_key(1024, RSA_3, nullptr, nullptr);
    if (!rsa) {
        pub_key_str.assign("ras init error");
        pri_key_str.assign("ras init error");
        return;
    }
    // 生成私公钥
    BIO *pri_bio = BIO_new(BIO_s_mem());
    PEM_write_bio_RSAPrivateKey(pri_bio, rsa, NULL, NULL, 0, NULL, NULL);
    BIO *pub_bio = BIO_new(BIO_s_mem());
    PEM_write_bio_RSAPublicKey(pub_bio, rsa);

    //获取长度并读取
    size_t pri_len = BIO_pending(pri_bio);
    size_t pub_len = BIO_pending(pub_bio);
    //对应RSA_generate_key(1024...)
    char pri_key[1024]{0};
    char pub_key[512]{0};
    BIO_read(pri_bio, pri_key, pri_len);
    BIO_read(pub_bio, pub_key, pub_len);

    //释放资源
//    RSA_free(rsa);
    BIO_free_all(pri_bio);
    BIO_free_all(pub_bio);
    CRYPTO_cleanup_all_ex_data();

    pub_key_str.assign(pub_key, pub_len);
    pri_key_str.assign(pri_key, pri_len);
}


/*
制造密钥对：私钥和公钥
**/
void GenerateRSAKey(std::string &out_pub_key, std::string &out_pri_key) {
    size_t pri_len = 0; // 私钥长度
    size_t pub_len = 0; // 公钥长度
    char *pri_key = nullptr; // 私钥
    char *pub_key = nullptr; // 公钥

    // 生成密钥对
    RSA *keypair = RSA_generate_key(KEY_LENGTH, RSA_3, nullptr, nullptr);

    BIO *pri = BIO_new(BIO_s_mem());
    BIO *pub = BIO_new(BIO_s_mem());

    // 生成私钥
    PEM_write_bio_RSAPrivateKey(pri, keypair, nullptr, nullptr, 0, nullptr, nullptr);
    // 注意------生成第1种格式的公钥
    //PEM_write_bio_RSAPublicKey(pub, keypair);
    // 注意------生成第2种格式的公钥（此处代码中使用这种）
    PEM_write_bio_RSAPublicKey(pub, keypair);

    // 获取长度
    pri_len = BIO_pending(pri);
    pub_len = BIO_pending(pub);

    // 密钥对读取到字符串
    pri_key = (char *) malloc(pri_len + 1);
    pub_key = (char *) malloc(pub_len + 1);

    BIO_read(pri, pri_key, pri_len);
    BIO_read(pub, pub_key, pub_len);

    pri_key[pri_len] = '\0';
    pub_key[pub_len] = '\0';

    out_pub_key = pub_key;
    out_pri_key = pri_key;
    // 释放内存
    RSA_free(keypair);
    BIO_free_all(pub);
    BIO_free_all(pri);

    free(pri_key);
    free(pub_key);
}

/*
@brief : 私钥加密
@para  : clear_text  -[i] 需要进行加密的明文
         pri_key     -[i] 私钥
@return: 加密后的数据
**/
std::string RsaPriEncrypt(const std::string &clear_text, std::string &pri_key) {
    std::string encrypt_text;
    BIO *keybio = BIO_new_mem_buf((unsigned char *) pri_key.c_str(), -1);
    RSA *rsa = RSA_new();
    rsa = PEM_read_bio_RSAPrivateKey(keybio, &rsa, NULL, nullptr);
    if (!rsa) {
        BIO_free_all(keybio);
        return std::string("");
    }

    // 获取RSA单次可以处理的数据块的最大长度
    int key_len = RSA_size(rsa);
    int block_len = key_len - 11;    // 因为填充方式为RSA_PKCS1_PADDING, 所以要在key_len基础上减去11

    // 申请内存：存贮加密后的密文数据
    char *sub_text = new char[key_len + 1];
    memset(sub_text, 0, key_len + 1);
    int ret = 0;
    int pos = 0;
    std::string sub_str;
    // 对数据进行分段加密（返回值是加密后数据的长度）
    while (pos < clear_text.length()) {
        sub_str = clear_text.substr(pos, block_len);
        memset(sub_text, 0, key_len + 1);
        ret = RSA_private_encrypt(sub_str.length(), (const unsigned char *) sub_str.c_str(), (unsigned char *) sub_text,
                                  rsa, RSA_PKCS1_PADDING);
        if (ret >= 0) {
            encrypt_text.append(std::string(sub_text, ret));
        }
        pos += block_len;
    }

    // 释放内存
    delete sub_text;
    BIO_free_all(keybio);
    RSA_free(rsa);

    return encrypt_text;
}

/*
@brief : 公钥解密
@para  : cipher_text -[i] 加密的密文
         pub_key     -[i] 公钥
@return: 解密后的数据
**/
std::string RsaPubDecrypt(const std::string &cipher_text, const std::string &pub_key) {
    std::string decrypt_text;
    BIO *keybio = BIO_new_mem_buf((unsigned char *) pub_key.c_str(), -1);
    RSA *rsa = RSA_new();

    // 注意-------使用第1种格式的公钥进行解密
    //rsa = PEM_read_bio_RSAPublicKey(keybio, &rsa, NULL, NULL);
    // 注意-------使用第2种格式的公钥进行解密（我们使用这种格式作为示例）
    rsa = PEM_read_bio_RSAPublicKey(keybio, &rsa, nullptr, nullptr);
    if (!rsa) {
        unsigned long err = ERR_get_error(); //获取错误号
        char err_msg[1024] = {0};
        ERR_error_string(err, err_msg); // 格式：error:errId:库:函数:原因
        printf("err msg: err:%ld, msg:%s\n", err, err_msg);
        BIO_free_all(keybio);

        return decrypt_text;
    }

    // 获取RSA单次处理的最大长度
    int len = RSA_size(rsa);
    char *sub_text = new char[len + 1];
    memset(sub_text, 0, len + 1);
    int ret = 0;
    std::string sub_str;
    int pos = 0;
    // 对密文进行分段解密
    while (pos < cipher_text.length()) {
        sub_str = cipher_text.substr(pos, len);
        memset(sub_text, 0, len + 1);
        ret = RSA_public_decrypt(sub_str.length(), (const unsigned char *) sub_str.c_str(), (unsigned char *) sub_text,
                                 rsa, RSA_PKCS1_PADDING);
        if (ret >= 0) {
            decrypt_text.append(std::string(sub_text, ret));
            printf("pos:%d, sub: %s\n", pos, sub_text);
            pos += len;
        }
    }

    // 释放内存
    delete sub_text;
    BIO_free_all(keybio);
    RSA_free(rsa);

    return decrypt_text;
}

/*
@brief : 公钥加密
@para  : clear_text  -[i] 需要进行加密的明文
         pri_key     -[i] 私钥
@return: 加密后的数据
**/
std::string RsaPubEncrypt(const std::string &clear_text, const std::string &pub_key) {
    std::string encrypt_text;
    BIO *keybio = BIO_new_mem_buf((unsigned char *) pub_key.c_str(), -1);
    RSA *rsa = RSA_new();
    // 注意-----第1种格式的公钥
    //rsa = PEM_read_bio_RSAPublicKey(keybio, &rsa, NULL, NULL);
    // 注意-----第2种格式的公钥（这里以第二种格式为例）
    rsa = PEM_read_bio_RSAPublicKey(keybio, &rsa, nullptr, nullptr);

    // 获取RSA单次可以处理的数据块的最大长度
    int key_len = RSA_size(rsa);
    int block_len = key_len - 11;    // 因为填充方式为RSA_PKCS1_PADDING, 所以要在key_len基础上减去11

    // 申请内存：存贮加密后的密文数据
    char *sub_text = new char[key_len + 1];
    memset(sub_text, 0, key_len + 1);
    int ret = 0;
    int pos = 0;
    std::string sub_str;
    // 对数据进行分段加密（返回值是加密后数据的长度）
    while (pos < clear_text.length()) {
        sub_str = clear_text.substr(pos, block_len);
        memset(sub_text, 0, key_len + 1);
        ret = RSA_public_encrypt(sub_str.length(), (const unsigned char *) sub_str.c_str(), (unsigned char *) sub_text,
                                 rsa, RSA_PKCS1_PADDING);
        if (ret >= 0) {
            encrypt_text.append(std::string(sub_text, ret));
        }
        pos += block_len;
    }

    // 释放内存
    BIO_free_all(keybio);
    RSA_free(rsa);
    delete[] sub_text;

    return encrypt_text;
}

/*
@brief : 私钥解密
@para  : cipher_text -[i] 加密的密文
         pub_key     -[i] 公钥
@return: 解密后的数据
**/
std::string RsaPriDecrypt(const std::string &cipher_text, const std::string &pri_key) {
    std::string decrypt_text;
    RSA *rsa = RSA_new();
    BIO *keybio;
    keybio = BIO_new_mem_buf((unsigned char *) pri_key.c_str(), -1);

    rsa = PEM_read_bio_RSAPrivateKey(keybio, &rsa, nullptr, nullptr);
    if (rsa == nullptr) {
        unsigned long err = ERR_get_error(); //获取错误号
        char err_msg[1024] = {0};
        ERR_error_string(err, err_msg); // 格式：error:errId:库:函数:原因
        printf("err msg: err:%ld, msg:%s\n", err, err_msg);
        return std::string();
    }

    // 获取RSA单次处理的最大长度
    int key_len = RSA_size(rsa);
    char *sub_text = new char[key_len + 1];
    memset(sub_text, 0, key_len + 1);
    int ret = 0;
    std::string sub_str;
    int pos = 0;
    // 对密文进行分段解密
    while (pos < cipher_text.length()) {
        sub_str = cipher_text.substr(pos, key_len);
        memset(sub_text, 0, key_len + 1);
        ret = RSA_private_decrypt(sub_str.length(), (const unsigned char *) sub_str.c_str(), (unsigned char *) sub_text,
                                  rsa, RSA_PKCS1_PADDING);
        if (ret >= 0) {
            decrypt_text.append(std::string(sub_text, ret));
            printf("pos:%d, sub: %s\n", pos, sub_text);
            pos += key_len;
        }
    }
    // 释放内存
    delete[] sub_text;
    BIO_free_all(keybio);
    RSA_free(rsa);

    return decrypt_text;
}