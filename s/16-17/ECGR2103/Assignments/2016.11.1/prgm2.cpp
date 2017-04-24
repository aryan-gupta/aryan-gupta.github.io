#include <cstdlib>
#include <iostream>

using namespace std;

int main() {
	int c = 1;

	for(int o = 0; o < 4; o++) {
		for(int i = 0; i < 7; i++) {
			cout << c << " = " << (c * 28) << "\t";
			c++;
		}
		cout << endl;
	}
	return 0;
}
