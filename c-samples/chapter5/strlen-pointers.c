#include <stdio.h>

int strlenp(char *str);

int strlenp(char *str) {
    int len;

    for(len = 0; *str!= '\0'; len++, str++);

    return len;
}

int main() {
    char str[] = "Hello, world!";
    printf("%d\n", strlenp(str));
}