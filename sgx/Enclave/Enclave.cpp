#include "Enclave.h"
#include "Enclave_t.h" /* print_string */
#include <string.h>

// https://en.cppreference.com/w/c/header
// https://en.cppreference.com/w/cpp/header

void ecall_hello_from_enclave(char *buf, size_t len) {

//    get_data();

    ocall_print_string(buf);

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