//TRAIL 1
#include <cstdlib>
#include <string> 
#include <iostream>  //make sure that all necessary libraries are included & correct
using namespace std;

int main() {
  string name; 
  int number;
  double numDivided; 
  
   cout << "What is your name?" << endl;
   getline(cin, name);
   cout << "Hello " << name << endl;
   cout << "What is your favorite whole number?" << endl;
   cin >> number;
   cout << number << endl;
   cout << "Your favorite number divided by 3 is:"<< endl;
   numDivided = (double)number / 3;
   cout << numDivided;
   

   return 0;
}




