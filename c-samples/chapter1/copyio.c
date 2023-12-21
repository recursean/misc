#include <stdio.h>

int main() {
    printf("Value of EOF : %d\n", EOF);

    int c;
    while((c = getchar()) != EOF) {
        putchar(c);
    }
}