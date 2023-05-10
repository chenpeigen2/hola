//
// Created by chenpeigen on 23-5-10.
//

#ifndef SGX_ENCRYPT_H
#define SGX_ENCRYPT_H

std::string decrypt(const unsigned char *src, size_t src_len,
                    const unsigned char *pri_key, size_t key_len);

std::string encrypt(const unsigned char *src, size_t src_len,
                    const unsigned char *pub_key, size_t key_len);

void createRSAKey(std::string &pub_key_str, std::string &pri_key_str);

std::string decode(const char *src, size_t src_len);

std::string encode(const char *src, size_t src_len);

std::string ecb_encrypt(const char *src,
                        const unsigned char *key, size_t key_len,
                        PaddingModel mode);

std::string ecb_decrypt(const char *src, size_t src_len,
                        const unsigned char *key, size_t key_len);

#endif //SGX_ENCRYPT_H
