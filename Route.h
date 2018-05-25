#ifndef ROUTE_H
#define ROUTE_H

using namespace std;
#include <vector>
#include "Cellule.h"

class Route 
{
private:
	vector<Cellule*> cellulesRoute;
	
public:
	Route(Cellule &c);
	Route(vector<Cellule> &vCellules);
	virtual void affiche();
	void ajouteCellule(Cellule &c);
	vector<Cellule*> &getCellulesRoute();
	
};

#endif
