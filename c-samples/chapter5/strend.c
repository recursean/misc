#include <stdio.h>
#include <string.h>

int strend(char *source, char *target);

int strend(char *source, char *target) {
    while(*source) {
        source++;
    }

    source -= strlen(target);

    while(*source && *source++ == *target++);

    if(*source == '\0') {
        return 1;
    }
    else {
        return 0;
    }
}

int main() {
    char source[] = "Hello, world!";
    char target[] = "world!";

    printf("%d\n", strend(source, target)); 
}