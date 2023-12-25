#include <stdio.h>
#define MAX_LINE 256

// remove commented lines from a c program

int getUncommentedLine(char line[]);

int getUncommentedLine(char line[]) {
    int c = 0;
    int i = 0;
    int prevSlash, foundNonWS, inComment;
    prevSlash = foundNonWS = inComment = 0;

    while(i < MAX_LINE && (c = getchar()) != EOF && c != '\n') {
        if(inComment != 1) {
            if(c == '/') {
                // single line comment found
                if(prevSlash == 1) { // can it remove this comment?
                    inComment = 1;
                    i--;
                    continue;
                }
                prevSlash = 1;
            }
            else {
                prevSlash = 0;
                if(c != ' ' && c != '\t') {
                    foundNonWS = 1;
                }
            }
            line[i++] = c;
        }
    }

    if(c == '\n') {
        line[i++] = c;
    }
    
    if(inComment && !foundNonWS) {
        line[0] = '\0';
    }
    else {
        line[i] = '\0';
    }
    return i;
}

int main() {
    int c = 0;
    char line[MAX_LINE];

    while(getUncommentedLine(line) > 0) {
        printf("%s", line);
    }
}