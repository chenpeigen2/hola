//
// Created by chenpeigen on 23-3-19.
//
#include "Enclave_t.h"
#include "Ocall_wrappers.h"
#include "Enclave.h"
#include <openssl/ssl.h>
#include <openssl/x509.h>
#include <openssl/pem.h>
#include <openssl/err.h>
#include <string>
#include <openssl/aes.h>


void padding(std::string &src, int alignSize, PaddingModel mode) {
    int remainder = src.length() % alignSize;
    int paddingSize = (remainder == 0) ? alignSize : (alignSize - remainder);
    switch (mode) {
        case PKCS5OR7:
            src.append(paddingSize, paddingSize);
            break;
        case ZERO:
        default:
            src.append(paddingSize, 0);
            break;
    }
}

void unpadding(std::string &src) {
    int c = src[src.length() - 1];
    if (c > 0) src.erase(src.length() - c, c);
}

std::string ecb_encrypt(const char *src,
                        const unsigned char *key, size_t key_len,
                        PaddingModel mode) {
    AES_KEY aes_key;
    std::string str_result;
    if (AES_set_encrypt_key(key, key_len * 8, &aes_key) == 0) {
        std::string str_data = src;
        padding(str_data, AES_BLOCK_SIZE, mode);

        unsigned char out[AES_BLOCK_SIZE]{0};
        int blockSize = str_data.length() / AES_BLOCK_SIZE;
        for (int i = 0; i < blockSize; ++i) {
            const unsigned char *in = (const unsigned char *) str_data.c_str() + i * AES_BLOCK_SIZE;
            AES_ecb_encrypt(in, out, &aes_key, AES_ENCRYPT);
            str_result += std::string((const char *) out, AES_BLOCK_SIZE);
            memset(out, 0, AES_BLOCK_SIZE);
        }
    }
    return str_result;
}

std::string ecb_decrypt(const char *src, size_t src_len,
                        const unsigned char *key, size_t key_len) {
    AES_KEY aes_key;
    std::string str_result;
    if (AES_set_decrypt_key(key, key_len * 8, &aes_key) == 0) {
        unsigned char out[AES_BLOCK_SIZE]{0};
        int blockSize = src_len / AES_BLOCK_SIZE;
        for (int i = 0; i < blockSize; ++i) {
            const unsigned char *in = (const unsigned char *) src + i * AES_BLOCK_SIZE;
            AES_ecb_encrypt(in, out, &aes_key, AES_DECRYPT);
            str_result += std::string((const char *) out, AES_BLOCK_SIZE);
            memset(out, 0, AES_BLOCK_SIZE);
        }
        unpadding(str_result);
    }
    return str_result;
}