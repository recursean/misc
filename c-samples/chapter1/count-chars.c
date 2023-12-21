#include <stdio.h>

int main() {
    int c;
    int ndigits[10];
    int nwhite, nother;

    nwhite = nother = 0;
    for(int i = 0; i < 10; i++) {
        ndigits[i] = 0;
    }

    while((c = getchar()) != EOF) {
        if(c >= '0' && c <= '9') {
            ndigits[c - '0']++;
        }
        else if(c == ' ' || c == '\t' || c == '\n') {
            nwhite++;
        }
        else {
            nother++;
        }
    }

    for(int i = 0; i < 10; i++) {
        printf("%5d : %d\n", i, ndigits[i]);
    }   
    printf("White : %d\n", nwhite);
    printf("Other : %d\n", nother);
}