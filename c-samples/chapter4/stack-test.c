#include <stdio.h>
#include "stack.h"

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