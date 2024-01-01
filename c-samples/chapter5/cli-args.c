#include <stdio.h>

int main(int argc, char *argv[]) {
    // indexes
    for(int i = 0; i < argc; i++) {
        printf("%s\n", argv[i]);
    }

    printf("\n");

    // pointers
    for(int i = 0; i < argc; i++) {
        printf("%s\n", *argv++);
    }
}