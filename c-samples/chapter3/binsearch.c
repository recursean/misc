#include <stdio.h>

int binsearch(int target, int vals[], int len);
int binsearchRecursive(int target, int vals[], int len);
int binsearchR(int target, int vals[], int low, int high);

int binsearch(int target, int vals[], int len) {
    int low = 0;
    int high = len - 1;
    int mid;

    while(low <= high) {
        mid = (low + high) / 2;
        if(target < vals[mid]) {
            high = mid - 1;
        }
        else if(target > vals[mid]) {
            low = mid + 1;
        }
        else {
            return mid;
        }
    }
    return -1;
}

int binsearchRecursive(int target, int vals[], int len) {
    return binsearchR(target, vals, 0, len - 1);
}

int binsearchR(int target, int vals[], int low, int high) {
    if(low > high) {
        return -1;
    }
    
    int mid = (low + high) / 2;
    if(target < vals[mid]) {
        return binsearchR(target, vals, low, mid - 1);
    }
    else if(target > vals[mid]) {
        return binsearchR(target, vals, mid + 1, high);
    }

    return mid;
}

int main() {
    int vals[] = {1, 3, 5, 7, 9, 11, 13, 15};
    int target = 11;
    printf("%d\n", binsearch(target, vals, 8));
    printf("%d\n", binsearchRecursive(target, vals, 8));
}