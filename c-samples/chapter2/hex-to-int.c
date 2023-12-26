#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <ctype.h>

int htoi(char hexNum[]);

int htoi(char hexNum[]) {
    int intNum = 0;
    int hexNumLen = strlen(hexNum);
    int workInt = 0;

    if(hexNumLen < 2 || hexNum[0] != '0' || (hexNum[1] != 'x' && hexNum[1] != 'X')) {
        printf("ERROR: Invalid hex number");
    }
    else {
        for(int i = 2; i < hexNumLen; i++) {
            if(!isxdigit(hexNum[i])) {
                printf("ERROR: Invalid hex number");
            }
            else {
                if(hexNum[i] == 'a' || hexNum[i] == 'A') {
                    workInt = 10;
                }
                else if(hexNum[i] == 'b' || hexNum[i] == 'B') {
                    workInt = 11;
                }
                else if(hexNum[i] == 'c' || hexNum[i] == 'C') {
                    workInt = 12;
                }
                else if(hexNum[i] == 'd' || hexNum[i] == 'D') {
                    workInt = 13;
                }
                else if(hexNum[i] == 'e' || hexNum[i] == 'E') {
                    workInt = 14;
                }
                else if(hexNum[i] == 'f' || hexNum[i] == 'F') {
                    workInt = 15;
                }
                else {
                    workInt = hexNum[i] - '0';
                }
                intNum += workInt * (int)pow(16.0, hexNumLen - i - 1);
            }
        }
    }

    return intNum;

}

int main() {
    char hexNum[] = "0x7FFFFFFF";
    printf("hex:\t%s\nint:\t%d\n", hexNum, htoi(hexNum));
}