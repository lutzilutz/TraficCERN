#ifndef CASE_H
#define CASE_H

#include <iostream>
using namespace std;

class Cellule 
{
private:
	Cellule* celluleSuivante;
	bool estOccupee;
	
public:
	Cellule();
	void affiche();
	Cellule* &getCelluleSuivante();
	void setCelluleSuivante(Cellule &c);
	bool getEstOccupee();
	void setEstOccupee(bool b);
	void evolue();
};

#endif
