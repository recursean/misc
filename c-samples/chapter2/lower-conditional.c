#include <stdio.h>

char lower(char c);

char lower(char c) {
    return c >= 'A' && c <= 'Z' ? c + 'a' - 'A' : c;
}

int main() {
    char c = 'J';
    printf("%c\n", lower(c));
}