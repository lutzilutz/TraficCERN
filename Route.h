#ifndef ROUTE_H
#define ROUTE_H

using namespace std;
#include <vector>
#include "Case.h"

class Route 
{
private:
	vector<Case*> casesRoute;
	
public:
	Route(Case &c);
	Route(vector<Case> &vCases);
	virtual void affiche();
	void ajouteCase(Case &c);
	vector<Case*> &getCasesRoute();
	
};

#endif
