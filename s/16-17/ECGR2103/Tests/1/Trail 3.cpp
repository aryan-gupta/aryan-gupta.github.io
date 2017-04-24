//TRAIL 3
#include <cstdlib>         // verify all of your libraries are correct & included
#include <ctime>
#include <iostream>
using namespace std;

int main() {              //don't forget to seed your number generator
   srand(9);
   int age = 0;
   
   int month = (rand() % 12) + 1;
   int day = (rand() % 31) + 1;
   int year = (rand() % 37) + 1980;
   
   cout << month << endl << day << endl << year << endl;
   cout << month << "/" << day << "/" << year << endl;
   
   age = 2016 - year;
   
   if(month < 9) {
      age--;
   }
   
   cout << age;
   
   return 0;
}

