#include <stdio.h>
#include <string.h>

#define MAXLINES 5000
#define MAXLEN   1000

int getLine(char line[]);
int readlines(char *lines[], char *lineStorage);
void writelines(char *lines[], int numLines);

int getLine(char line[]) {
    int c = 0;
    int idx = 0;

    while(idx < MAXLEN && (c = getchar()) != EOF && c != '\n') {
        line[idx++] = c;
    }
    
    if(c == '\n') {
        line[idx++] = c;
    }

    line[idx++] = '\0';
    return idx;
}

int readlines(char *lines[], char *lineStorage) {
    char line[MAXLEN];
    char *storagePtr = lineStorage;

    int lineLen = 0;
    int numLines = 0;

    while(numLines < MAXLINES && (lineLen = getLine(line))) {
        strcpy(storagePtr, line);
        lines[numLines++] = storagePtr;
        storagePtr += lineLen;
    }

    return numLines;
}

void writelines(char *lines[], int numLines) {
    // for(int i = 0; i < numLines; i++) {
    //     printf("%s", lines[i]);
    // }

    while(numLines-- > 0) {
        printf("%s", *lines++);
    }
}

int main() {
    char lineStorage[MAXLINES * MAXLEN];
    char *lines[MAXLINES];

    int numLines = readlines(lines, lineStorage);
    writelines(lines, numLines);
}