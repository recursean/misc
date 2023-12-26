#include <stdio.h>
#include <string.h>

void squeeze(char s1[], char s2[]);

void squeeze(char s1[], char s2[]) {
    char alpha[255];
    int idx = 0;

    for(int i = 0; i < 255; i++) {
        alpha[i] = 0;
    }

    for(int i = 0; i < strlen(s2); i++) {
        alpha[s2[i]] = 1;
    }

    for(int i = 0; i < strlen(s1); i++) {
        if(alpha[s1[i]] == 0) {
            s1[idx++] = s1[i];
        }
    }
    s1[idx] = '\0';
}

int main() {
    // primary string
    char s1[] = "Hello, world!";
    
    // characters to remove from s1
    char s2[] = "lo!";

    squeeze(s1, s2);
    printf("%s\n", s1);
}