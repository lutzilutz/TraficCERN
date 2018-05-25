using namespace std;
#include <iostream>
#include "Case.h"
#include "Route.h"
#include "RondPoint.h"
int main()
{
	Case c1 = Case();
	Case c2 = Case();
	Case c3 = Case();
	Case c4 = Case();
	Case c5 = Case();
	c1.setCaseSuivante(c2);
	Route R = Route(c1);
	R.ajouteCase(c2);
	R.ajouteCase(c3);
	R.ajouteCase(c4);
	R.ajouteCase(c5);
	
	cout << "Voici notre route :" << endl;
	R.affiche();
	cout << "On va essayer d'afficher la case après c1 :" << endl;
	R.getCasesRoute()[0]->affiche();
	cout << endl;
	cout << "Partant de la première case, on va rajouter un élément dans la case suivante :" << endl;
	R.getCasesRoute()[0]->setEstOccupee(true);
	R.affiche();
	
	cout << "L'adresse de c1 est :\t\t\t\t\t" << &c1 << endl;
	cout << "L'adresse pointée par l'élément 1 de la route est :\t" << R.getCasesRoute()[0] << endl;
	
	cout << "L'adresse de c2 est :\t\t\t\t\t" << &c2 << endl;
	cout << "L'adresse pointée par l'élément 2 de la route est :\t" << R.getCasesRoute()[1] << endl;
	
	cout << "L'adresse de c3 est :\t\t\t\t\t" << &c3 << endl;
	cout << "L'adresse pointée par l'élément 3 de la route est :\t" << R.getCasesRoute()[2] << endl;
	
	cout << "L'adresse de c4 est :\t\t\t\t\t" << &c4 << endl;
	cout << "L'adresse pointée par l'élément 4 de la route est :\t" << R.getCasesRoute()[3] << endl;
	
	cout << "L'adresse de c5 est :\t\t\t\t\t" << &c5 << endl;
	cout << "L'adresse pointée par l'élément 5 de la route est :\t" << R.getCasesRoute()[4] << endl;
	
	cout << "Essayons de simuler le trafic de la route :" << endl;
	R.getCasesRoute()[1]->setEstOccupee(true);
	R.affiche();
	vector<int> casesPleines = {};
	for (unsigned int j(1); j<=7; ++j)
	{
		casesPleines = {};
		for (unsigned int i(0); i < R.getCasesRoute().size(); ++i)
		{
			if (R.getCasesRoute()[i]->getEstOccupee())
			{
				casesPleines.push_back(i);
			}
		}
		for (unsigned int i(0); i < casesPleines.size(); ++i)
		{
			R.getCasesRoute()[casesPleines[i]]->evolue();
		}
		R.affiche();
	}
	
	
	cout << "On va maintenant essayer d'initialiser une nouvelle route avec un vecteur de Cases !" << endl;
	
	vector<Case> vectCases(10);
	
	cout << "Taille de vectCases : " << vectCases.size() << endl;
	
	for (Case c: vectCases)
	{
		c.affiche();
	}
	cout << endl ;
	cout << "On crée une route R2 et on l'affiche : " << endl;
	Route R2(vectCases);
	
	R2.affiche();
	
	cout << "On va créer un rond-point de 7 cases dont les deux premières sont remplies : " << endl;
	
	vector<Case> vectCasesRP(7);
	RondPoint RP(vectCasesRP);
	
	RP.getCasesRoute()[0]->setEstOccupee(true);
	RP.getCasesRoute()[1]->setEstOccupee(true);
	RP.affiche();
	
	for (unsigned int j(1); j<=10; ++j)
	{
		casesPleines = {};
		for (unsigned int i(0); i < RP.getCasesRoute().size(); ++i)
		{
			if (RP.getCasesRoute()[i]->getEstOccupee())
			{
				casesPleines.push_back(i);
			}
		}
		for (unsigned int i(0); i < casesPleines.size(); ++i)
		{
			RP.getCasesRoute()[casesPleines[i]]->evolue();
		}
		RP.affiche();
	}
	
}

