using namespace std;
#include <iostream>
#include "Cell.h"
#include "Road.h"
#include "RoundAbout.h"

int main() {
	Cell c1 = Cell();
	Cell c2 = Cell();
	Cell c3 = Cell();
	Cell c4 = Cell();
	Cell c5 = Cell();
	c1.setNextCell(c2);
	Road R = Road(c1);
	R.addCell(c2);
	R.addCell(c3);
	R.addCell(c4);
	R.addCell(c5);
	
	cout << "Voici notre route :" << endl;
	R.display();
	cout << "On va essayer d'afficher la cellule après c1 :" << endl;
	R.getRoadCells()[0]->display();
	cout << endl;
	cout << "Partant de la première cellule, on va rajouter un élément dans la cellule suivante :" << endl;
	R.getRoadCells()[0]->setIsOccupied(true);
	R.display();
	
	cout << "L'adresse de c1 est :\t\t\t\t\t" << &c1 << endl;
	cout << "L'adresse pointée par l'élément 1 de la route est :\t" << R.getRoadCells()[0] << endl;
	
	cout << "L'adresse de c2 est :\t\t\t\t\t" << &c2 << endl;
	cout << "L'adresse pointée par l'élément 2 de la route est :\t" << R.getRoadCells()[1] << endl;
	
	cout << "L'adresse de c3 est :\t\t\t\t\t" << &c3 << endl;
	cout << "L'adresse pointée par l'élément 3 de la route est :\t" << R.getRoadCells()[2] << endl;
	
	cout << "L'adresse de c4 est :\t\t\t\t\t" << &c4 << endl;
	cout << "L'adresse pointée par l'élément 4 de la route est :\t" << R.getRoadCells()[3] << endl;
	
	cout << "L'adresse de c5 est :\t\t\t\t\t" << &c5 << endl;
	cout << "L'adresse pointée par l'élément 5 de la route est :\t" << R.getRoadCells()[4] << endl;
	
	cout << "Essayons de simuler le trafic de la route :" << endl;
	R.getRoadCells()[1]->setIsOccupied(true);
	R.display();
	vector<int> fullCells = {};
	for (unsigned int j(1); j<=7; ++j)
	{
		fullCells = {};
		for (unsigned int i(0); i < R.getRoadCells().size(); ++i)
		{
			if (R.getRoadCells()[i]->getIsOccupied())
			{
				fullCells.push_back(i);
			}
		}
		for (unsigned int i(0); i < fullCells.size(); ++i)
		{
			R.getRoadCells()[fullCells[i]]->evolve();
		}
		R.display();
	}
	
	
	cout << "On va maintenant essayer d'initialiser une nouvelle route avec un vecteur de Cellules !" << endl;
	
	vector<Cell> vectCells(10);
	
	cout << "Taille de vectCellules : " << vectCells.size() << endl;
	
	for (Cell c: vectCells)
	{
		c.display();
	}
	cout << endl ;
	cout << "On crée une route R2 et on l'affiche : " << endl;
	Road R2(vectCells);
	
	R2.display();
	
	cout << "On va créer un rond-point de 7 cellules dont les deux premières sont remplies : " << endl;
	
	vector<Cell> vectRACells(7);
	RoundAbout RA(vectRACells);
	
	RA.getRoadCells()[0]->setIsOccupied(true);
	RA.getRoadCells()[1]->setIsOccupied(true);
	RA.display();
	
	for (unsigned int j(1); j<=10; ++j)
	{
		fullCells = {};
		for (unsigned int i(0); i < RA.getRoadCells().size(); ++i)
		{
			if (RA.getRoadCells()[i]->getIsOccupied())
			{
				fullCells.push_back(i);
			}
		}
		for (unsigned int i(0); i < fullCells.size(); ++i)
		{
			RA.getRoadCells()[fullCells[i]]->evolve();
		}
		RA.display();
	}
	
}

