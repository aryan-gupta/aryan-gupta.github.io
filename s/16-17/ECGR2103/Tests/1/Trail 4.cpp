//TRAIL 4
#include <iostream>
#include <string>
using namespace std;

int main(){
	//add in any code you may need anywhere
	char status;
	int age = 0;
   
	cout << "What is your age? ";
   cin >> age;
	cout << age << endl;

	//Utilize switch statements to determine if they are eligible to register to vote & to ask if they have registered
	
	switch(age >= 18 ) {
	   case true:
	      cout << "Are you registered to vote?" << endl;
	      cin >> status;
	      cout << status << endl;
	      switch(status) {
	         case 'N':
	         case 'n':
	            cout << "Please register if you are eligible" << endl; break;
	         case 'Y':
	         case 'y':
	            cout << "Don't forget to vote!" << endl; break;
	         default:
	            cout << "You entered a wrong option" << endl; break;
	            
	      }
	      break;
	   case false:
	      cout << "You are not eligible to vote\n" << "Please register if you are eligible" << endl; break;
	   default:
	      cout << "The world ended. oops :)" << endl; break;
	}
	return 0;
}
