#include <stdio.h>

unsigned int setbits(unsigned int x, int p, int n, unsigned int y);

unsigned int setbits(unsigned int x, int p, int n, unsigned int y) {
    unsigned int yBits = y & (~(~0 << n));
    yBits = yBits << (p + 1 - n);

    unsigned int mask = ~((~(~0 << n)) << (p + 1 -n));
    x = x & mask;

    return x | yBits;
}

int main() {
    unsigned int x = 789; // 0x315
    unsigned int y = 19;  // 0x13 
    int p = 13;
    int n = 5;
    printf("%u\n", setbits(x, p, n, y));

    // ans = 0x2715 = 10005
}