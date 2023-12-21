#include <stdio.h>

int main() {
    char c;
    int space = 0;

    while((c = getchar()) != '\n') {
        if(c == ' ') {
            if(space != 1) {
                putchar(c);
            }
            space = 1;
        }
        else {
            space = 0;
            putchar(c);
        }
    }
    printf("\n");
}