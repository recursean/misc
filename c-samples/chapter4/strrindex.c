#include <stdio.h>
#include <string.h>

int strrindex(char source[], char target[]);

int strrindex(char source[], char target[]) {
    for(int i = strlen(source) - 1; i >= 0; i--) {
        for(int j = strlen(target) - 1; j >= 0 && i >= 0; j--, i--) {
            if(source[i] == target[j]) {
                if(j == 0) {
                    return i;
                }
            }
            else {
                break;
            }
        }
    }
    return -1;
}

int main() {
    char source[] = "Hello, world!";
    char target[] = "llo";

    printf("%d\n", strrindex(source, target));
}