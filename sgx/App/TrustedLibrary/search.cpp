#include "../App.h"
#include "Enclave_u.h"
#include <iostream>

using namespace std;

int maxSearch(int a, int b) {
    cout << a << "  " << b;
    if (a > b) {
        return a;
    } else {
        return b;
    }
}