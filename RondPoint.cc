#include "RondPoint.h"


RondPoint::RondPoint(vector<Case> &vCases)
	: Route(vCases)
{
	ajouteCase(*(getCasesRoute().front()));
}

void RondPoint::affiche()
{
	for (unsigned int i(0); i < getCasesRoute().size()-1; ++i)
	{
		getCasesRoute()[i]->affiche();
	}
	cout << endl;
}


