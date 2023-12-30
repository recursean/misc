#include <stdio.h>

#define MAX_SIZE 1024

static int size = 0;
static int stack[MAX_SIZE];

int pop(void) {
    return size > 0 ? stack[--size] : -1;
}

void push(int element) {
    if(size < MAX_SIZE) {
        stack[size++] = element;
    }
    else {
        printf("ERROR: Stack full.");
    }
}

int peek(void) {
    return size > 0 ? stack[size - 1] : -1;
}

void clear(void) {
    size = 0;
}