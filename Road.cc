#include "Road.h"

Road::Road(Cell &c)
{
	roadCells = {&c};
};

Road::Road(vector<Cell> &vCells)
{
	roadCells = {&(vCells[0])};
	for (unsigned int i(1); i < vCells.size(); ++i)
	{
		addCell(vCells[i]);
	}
}

void Road::display()
{
	for (unsigned int i(0); i < roadCells.size(); ++i)
	{
		roadCells[i]->display();
	}
	cout << endl;
}

void Road::addCell(Cell &c)
{
	roadCells.back()->setNextCell(c);
	roadCells.push_back(&c); 
}

vector<Cell*> &Road::getRoadCells()
{
	return roadCells;
}
