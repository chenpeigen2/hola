/*
 * Copyright (C) 2011-2021 Intel Corporation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *   * Neither the name of Intel Corporation nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

#ifndef _ENCLAVE_H_
#define _ENCLAVE_H_

#include <stdlib.h>
#include <assert.h>
#include <string>
#include <vector>

#if defined(__cplusplus)
extern "C" {
#endif

/**
* 填充模式
*/
enum PaddingModel {
    ZERO,  //ZERO pading
    PKCS5OR7 //pkcs5 pkcs7 padding
};

void GenerateRSAKey(std::string &out_pub_key, std::string &out_pri_key);

std::string RsaPriEncrypt(const std::string &clear_text, std::string &pri_key);

std::string RsaPubDecrypt(const std::string &cipher_text, const std::string &pub_key);

std::string RsaPubEncrypt(const std::string &clear_text, const std::string &pub_key);

std::string RsaPriDecrypt(const std::string &cipher_text, const std::string &pri_key);

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

std::vector <std::string> split(const std::string &str, const std::string &pattern);

std::vector <std::string> params(const std::string &params);

std::vector <std::string> provide_query(const std::string source);

#if defined(__cplusplus)
}
#endif

#endif /* !_ENCLAVE_H_ */
