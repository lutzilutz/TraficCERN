#include "Route.h"

Route::Route(Case &c)
{
	casesRoute = {&c};
};

Route::Route(vector<Case> &vCases)
{
	casesRoute = {&(vCases[0])};
	for (unsigned int i(1); i < vCases.size(); ++i)
	{
		ajouteCase(vCases[i]);
	}
}

void Route::affiche()
{
	for (unsigned int i(0); i < casesRoute.size(); ++i)
	{
		casesRoute[i]->affiche();
	}
	cout << endl;
}

void Route::ajouteCase(Case &c)
{
	casesRoute.back()->setCaseSuivante(c);
	casesRoute.push_back(&c); 
}

vector<Case*> &Route::getCasesRoute()
{
	return casesRoute;
}
