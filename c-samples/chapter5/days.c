#include <stdio.h>

static char monthDays[2][13] = {
    {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
    {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}
};

int day_of_year(int year, int month, int day) {
    int leap = year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    for(int i = 1; i < month; i++) {
        // day += monthDays[leap][i];

        day += *(*(monthDays + (leap * 13)) + i);
    }

    return day;
}

int main() {
    int year = 2023;
    int month = 12;
    int day = 31;
    printf("%d\n", day_of_year(year, month, day));
}