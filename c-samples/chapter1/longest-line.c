#include <stdio.h>

#define MAX_LINE 1000

int getLine(char line[]);
void copyLine(char source[], char target[]);

int getLine(char line[]) {
    int c = 0;
    int i = 0;
    while(i < MAX_LINE && (c = getchar()) != EOF && c != '\n') {
        line[i++] = c;
    }

    if(c == '\n') {
        line[i++] = c;
    }
    
    line[i] = '\0';
    return i;
}

void copyLine(char source[], char target[]) {
    int i = 0;
    while(source[i] != '\0') {
        target[i] = source[i];
        i++;
    }
}

int main() {
    int len, maxlen;
    len = maxlen = 0;

    char line[MAX_LINE];
    char maxline[MAX_LINE];

    while((len = getLine(line)) > 0) {
        if(len > maxlen) {
            maxlen = len; 
            copyLine(line, maxline);
        }
    }

    if(maxlen > 0) {
        printf("Max length line is length %d :\n %s\n", maxlen, maxline);
    }
    else {
        printf("Max length line is 0");
    }
}