#include <cstdlib>
#include <array>
#include <iostream>

using namespace std;

int main() {
	
	int grade[] = {76, 82, 95, 32, 90, 92, 50, 100};
	char letter[] = {'C', 'B', 'A', 'F', 'A', 'A', 'F', 'A'};

	for(int i = 0; i < 8; i++)
		if(grade[i] >= 90)
			cout << letter[i] << " ";
	
	cout << endl;

	return 0;
}
