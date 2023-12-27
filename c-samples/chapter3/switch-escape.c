#include <stdio.h>
#include <string.h>

void escape(char source[], char target[]);

void escape(char source[], char target[]) {
    int idx = 0;
    for(int i = 0; i < strlen(source); i++) {
        switch(source[i]) {
            case '\n':
                target[idx++] = '\\';
                target[idx++] = 'n';
                break;
            case '\t':
                target[idx++] = '\\';
                target[idx++] = 't';
                break;
            default:
                target[idx++] = source[i];
                break;
        }
    }
    target[idx] = '\0';
}

int main() {
    char source[] = "Hello,\tworld\nThis is my source\n";
    char target[strlen(source) * 2];

    printf("%s\n", source);
    escape(source, target);
    printf("%s\n", target);
}