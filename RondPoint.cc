#include "RondPoint.h"


RondPoint::RondPoint(vector<Cellule> &vCellules)
	: Route(vCellules)
{
	ajouteCellule(*(getCellulesRoute().front()));
}

void RondPoint::affiche()
{
	for (unsigned int i(0); i < getCellulesRoute().size()-1; ++i)
	{
		getCellulesRoute()[i]->affiche();
	}
	cout << endl;
}


