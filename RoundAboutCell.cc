#include "RoundAboutCell.h"

RoundAboutCell::RoundAboutCell() : Cell()
{
	outCell = NULL;
}

Cell* &RoundAboutCell::getOutCell()
{
	return outCell;
}

void RoundAboutCell::setOutCell(Cell &c)
{
	outCell = &c;
}
