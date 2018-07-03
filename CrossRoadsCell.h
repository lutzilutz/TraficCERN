#ifndef CROSSROADSCELL_H
#define CROSSROADSCELL_H

#include <iostream>
#include "Cell.h"
using namespace std;

class CrossRoadsCell : public Cell
{
	private:
		CrossRoadsCell* nextCellIN;
	
	public:
		CrossRoadsCell();
		CrossRoadsCell* &getNextCellIN();
		void setNextCellIN(CrossRoadsCell &c);
};

#endif
