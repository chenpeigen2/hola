#include "Enclave.h"
#include "Enclave_t.h" /* print_string */
#include <string.h>
//#include "TrustedLibrary/sgxssl/include/tsgxsslio.h"
//#include "TrustedLibrary/test.h"
// https://en.cppreference.com/w/c/header
// https://en.cppreference.com/w/cpp/header

#include "sgx_tcrypto.h"
#include "tsgxssli.h"

void ecall_hello_from_enclave(char *buf, size_t len) {
    ocall_print_string(buf);
//    int a = Add(1,2);
    const char *hello = "Hello world from the encalve return"
                        "=============================="
                        "========================";
    size_t size = len;
    if (strlen(hello) < len) {
        size = strlen(hello) + 1;
    }
    memcpy(buf, hello, size - 1);
    buf[size - 1] = '\0';
}