using namespace std;
#include <iostream>
#include "Cellule.h"
#include "Route.h"
#include "RondPoint.h"

int main() {
	Cellule c1 = Cellule();
	Cellule c2 = Cellule();
	Cellule c3 = Cellule();
	Cellule c4 = Cellule();
	Cellule c5 = Cellule();
	c1.setCelluleSuivante(c2);
	Route R = Route(c1);
	R.ajouteCellule(c2);
	R.ajouteCellule(c3);
	R.ajouteCellule(c4);
	R.ajouteCellule(c5);
	
	cout << "Voici notre route :" << endl;
	R.affiche();
	cout << "On va essayer d'afficher la cellule après c1 :" << endl;
	R.getCellulesRoute()[0]->affiche();
	cout << endl;
	cout << "Partant de la première cellule, on va rajouter un élément dans la cellule suivante :" << endl;
	R.getCellulesRoute()[0]->setEstOccupee(true);
	R.affiche();
	
	cout << "L'adresse de c1 est :\t\t\t\t\t" << &c1 << endl;
	cout << "L'adresse pointée par l'élément 1 de la route est :\t" << R.getCellulesRoute()[0] << endl;
	
	cout << "L'adresse de c2 est :\t\t\t\t\t" << &c2 << endl;
	cout << "L'adresse pointée par l'élément 2 de la route est :\t" << R.getCellulesRoute()[1] << endl;
	
	cout << "L'adresse de c3 est :\t\t\t\t\t" << &c3 << endl;
	cout << "L'adresse pointée par l'élément 3 de la route est :\t" << R.getCellulesRoute()[2] << endl;
	
	cout << "L'adresse de c4 est :\t\t\t\t\t" << &c4 << endl;
	cout << "L'adresse pointée par l'élément 4 de la route est :\t" << R.getCellulesRoute()[3] << endl;
	
	cout << "L'adresse de c5 est :\t\t\t\t\t" << &c5 << endl;
	cout << "L'adresse pointée par l'élément 5 de la route est :\t" << R.getCellulesRoute()[4] << endl;
	
	cout << "Essayons de simuler le trafic de la route :" << endl;
	R.getCellulesRoute()[1]->setEstOccupee(true);
	R.affiche();
	vector<int> cellulesPleines = {};
	for (unsigned int j(1); j<=7; ++j)
	{
		cellulesPleines = {};
		for (unsigned int i(0); i < R.getCellulesRoute().size(); ++i)
		{
			if (R.getCellulesRoute()[i]->getEstOccupee())
			{
				cellulesPleines.push_back(i);
			}
		}
		for (unsigned int i(0); i < cellulesPleines.size(); ++i)
		{
			R.getCellulesRoute()[cellulesPleines[i]]->evolue();
		}
		R.affiche();
	}
	
	
	cout << "On va maintenant essayer d'initialiser une nouvelle route avec un vecteur de Cellules !" << endl;
	
	vector<Cellule> vectCellules(10);
	
	cout << "Taille de vectCellules : " << vectCellules.size() << endl;
	
	for (Cellule c: vectCellules)
	{
		c.affiche();
	}
	cout << endl ;
	cout << "On crée une route R2 et on l'affiche : " << endl;
	Route R2(vectCellules);
	
	R2.affiche();
	
	cout << "On va créer un rond-point de 7 cellules dont les deux premières sont remplies : " << endl;
	
	vector<Cellule> vectCellulesRP(7);
	RondPoint RP(vectCellulesRP);
	
	RP.getCellulesRoute()[0]->setEstOccupee(true);
	RP.getCellulesRoute()[1]->setEstOccupee(true);
	RP.affiche();
	
	for (unsigned int j(1); j<=10; ++j)
	{
		cellulesPleines = {};
		for (unsigned int i(0); i < RP.getCellulesRoute().size(); ++i)
		{
			if (RP.getCellulesRoute()[i]->getEstOccupee())
			{
				cellulesPleines.push_back(i);
			}
		}
		for (unsigned int i(0); i < cellulesPleines.size(); ++i)
		{
			RP.getCellulesRoute()[cellulesPleines[i]]->evolue();
		}
		RP.affiche();
	}
	
}

