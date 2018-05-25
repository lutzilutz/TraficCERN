#ifndef CASE_H
#define CASE_H

#include <iostream>
using namespace std;

class Case 
{
private:
	Case* caseSuivante;
	bool estOccupee;
	
public:
	Case();
	void affiche();
	Case* &getCaseSuivante();
	void setCaseSuivante(Case &c);
	bool getEstOccupee();
	void setEstOccupee(bool b);
	void evolue();
};

#endif
