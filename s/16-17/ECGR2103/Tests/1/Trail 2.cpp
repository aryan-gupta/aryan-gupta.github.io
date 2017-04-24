//TRAIL 2
#include <iostream>
#include <cstdlib>
#include <cmath>
using namespace std;                //verify that all of your libraries ar correct & included

int main() {
   //vars
   int number1 = 0;
   int number2 = 0;
   double radius = 0.0;
   double num2Sqrt = 0.0;
   double sphereVolume = 0.0;
   double pi = 3.14;
   int x = 0;
   int num1Pow = 0;
   
   //get user values
   cin >> number1;
   cin >> number2;
   cin >> radius;
   
   //calculate
   num1Pow = pow(number1, 4);
   cout << num1Pow << endl;
   
   num2Sqrt = pow(number2, 0.5);
   cout << num2Sqrt << endl;
   
   number2 = round(num2Sqrt);
   cout << number2 << endl;
   
   sphereVolume = ((4.0/3.0) * pi * pow(radius, 3));
   cout << sphereVolume << endl;
   
   x =(number1 < 20)? number1 + 10:number1;
   cout << x;
   return 0;
}