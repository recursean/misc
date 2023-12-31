#include <stdio.h>

#define MAXLEN 25

void strcatp(char *source, char *target);

void strcatp(char *source, char *target) {
    // point to end of source string
    while(*source) {
        source++;
    }
    
    // copy target
    while(*source++ = *target++);

    *source = '\0';
}

int main() {
    char source[MAXLEN] = "Begin: ";

    strcatp(source, "Hello, ");
    strcatp(source, "world!\n");

    printf("%s", source);
}