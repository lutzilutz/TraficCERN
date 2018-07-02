#include "RoundAbout.h"

RoundAbout::RoundAbout(vector<Cell> &vCells)
	: Road(vCells)
{
	addCell(*(getRoadCells().front()));
}

void RoundAbout::display()
{
	for (unsigned int i(0); i < getRoadCells().size()-1; ++i)
	{
		getRoadCells()[i]->display();
	}
	cout << endl;
}
