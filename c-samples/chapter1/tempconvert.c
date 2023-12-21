#include <stdio.h>

#define LOWER 0 
#define UPPER 300
#define STEP  20 

float convert(int degreesF) {
    return (5.0 / 9.0) * (degreesF - 32.0);
}

int main() {
    printf("Far\tCel\n");
    for(int i = LOWER; i <= UPPER; i+=STEP) {
        float f = i;
        printf("%3.0f\t%6.1f\n", f, convert(f));
    }
}