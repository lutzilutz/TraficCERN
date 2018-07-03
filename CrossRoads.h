#ifndef CROSSROADS_H
#define CROSSROADS_H

#include <iostream>
#include "Road.h"
#include "CrossRoadsCell.h"
using namespace std;

class CrossRoads
{
	public:
		Road* roadsIN[4];
		Road* roadsOUT[4];
		CrossRoadsCell* cells[4];
	public:
		CrossRoads(Road &Ri1, Road &Ri2, Road &Ri3, Road &Ri4, Road &Ro1, Road &Ro2, Road &Ro3, Road &Ro4, CrossRoadsCell &C1, CrossRoadsCell &C2, CrossRoadsCell &C3, CrossRoadsCell &C4);
		
};

#endif
