#ifndef ROUNDABOUTCELL_H
#define ROUNDABOUTCELL_H

#include <iostream>
#include "Cell.h"
using namespace std;


class RoundAboutCell: public Cell
{
	private:
		Cell* outCell;
	public:
		RoundAboutCell();
		Cell* &getOutCell();
		void setOutCell(Cell &c);
};

#endif

