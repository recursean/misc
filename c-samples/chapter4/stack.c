#include <stdio.h>

#define MAX_SIZE 1024

int size = 0;
int stack[MAX_SIZE];

int pop(void);
void push(int element);
int peek(void);
void clear(void);

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

int main() {
    push(1);
    push(2);
    push(3);
    push(4);
    push(5);

    printf("%d\n", pop());
    printf("%d\n", peek());
    printf("%d\n", pop());
    printf("%d\n", peek());
    printf("%d\n", pop());

    clear();
    push(15);
 
    printf("%d\n", peek());
    printf("%d\n", pop());
    printf("%d\n", pop());
}