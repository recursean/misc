#include <stdio.h>
#include <limits.h>

int main() {
    // char
    printf("Unsigned char: %d - %d\n", 0, UCHAR_MAX);
    printf("Signed char: %d - %d\n\n", SCHAR_MIN, SCHAR_MAX);
    
    // int
    printf("Unsigned int: %d - %u\n", 0, UINT_MAX);
    printf("Signed int: %d - %d\n\n", INT_MIN, INT_MAX);
    
    // short
    printf("Unsigned short: %d - %u\n", 0, USHRT_MAX);
    printf("Signed short: %d - %d\n\n", SHRT_MIN, SHRT_MAX);

    // long
    printf("Unsigned long: %d - %lu\n", 0, ULONG_MAX);
    printf("Signed long: %ld - %ld\n\n", LONG_MIN, LONG_MAX);
}