#ifndef RONDPOINT_H
#define RONDPOINT_H

#include "Route.h"


class RondPoint: public Route
{
	private:
	
	public:
		RondPoint(vector<Cellule> &vCellules);
		virtual void affiche();
};



#endif
