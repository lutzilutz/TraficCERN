#include "Route.h"

Route::Route(Cellule &c)
{
	cellulesRoute = {&c};
};

Route::Route(vector<Cellule> &vCellules)
{
	cellulesRoute = {&(vCellules[0])};
	for (unsigned int i(1); i < vCellules.size(); ++i)
	{
		ajouteCellule(vCellules[i]);
	}
}

void Route::affiche()
{
	for (unsigned int i(0); i < cellulesRoute.size(); ++i)
	{
		cellulesRoute[i]->affiche();
	}
	cout << endl;
}

void Route::ajouteCellule(Cellule &c)
{
	cellulesRoute.back()->setCelluleSuivante(c);
	cellulesRoute.push_back(&c); 
}

vector<Cellule*> &Route::getCellulesRoute()
{
	return cellulesRoute;
}
