#ifndef ROAD_H
#define ROAD_H

using namespace std;
#include <vector>
#include "Cell.h"

class Road
{
	private:
		vector<Cell*> roadCells;
		
	public:
		Road(Cell &c);
		Road(vector<Cell> &vCells);
		virtual void display();
		void addCell(Cell &c);
		vector<Cell*> &getRoadCells();
	
};

#endif
