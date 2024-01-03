#include <stdio.h>

int main(int argc, char *argv[]) {
    int c = 0;
    
    while(--argc > 0 && (*++argv)[0] == '-') {
        while((c = *++argv[0]) != '\0') {
            printf("%c\n", c);
        }
    }

    return 0;
}