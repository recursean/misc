#include <stdio.h>

#define MAX_LEN 256;

void reverse(char source[], char target[]);
void reverseRecursive(char source[], char target[]);
char reverseR(int i, int len, char source[], char target[]);
int stringLen(char string[]);

void reverse(char source[], char target[]) {
    int targetIdx = stringLen(target) - 1;

    int i = 0;
    while(source[i] != '\0') {
        target[targetIdx - i] = source[i];
        i++;
    }
}

void reverseRecursive(char source[], char target[]) {
    reverseR(0, stringLen(source), source, target);
}

char reverseR(int i, int len, char source[], char target[]) {
    if(source[i] != '\0') {
        target[len - i - 1] = reverseR(i + 1, len, source, target);
    }
    return source[i - 1];
}

int stringLen(char string[]) {
    int i = 0;
    while(string[i] != '\0') {
        i++;
    }
    return i; 
}

int main() {
    char forwardStr[] = "Hello, world!";

    // reserve 1 extra character for null
    int reverseStrLen = stringLen(forwardStr) + 1;
    char reverseStr[reverseStrLen];

    // init reverseStr 
    for(int i = 0; i < reverseStrLen; i++) {
        reverseStr[i] = ' ';
    }

    // mark end of string 
    reverseStr[reverseStrLen - 1] = '\0';

    // reverse(forwardStr, reverseStr);
    reverseRecursive(forwardStr, reverseStr);
    printf("forward: %s\nreverse: %s\n", forwardStr, reverseStr);
}