#include "CrossRoadsCell.h"


CrossRoadsCell::CrossRoadsCell() : Cell(), nextCellIN(NULL)
{
	
}

CrossRoadsCell* &CrossRoadsCell::getNextCellIN()
{
	return nextCellIN;
}

void CrossRoadsCell::setNextCellIN(CrossRoadsCell &c)
{
	nextCellIN = &c;
}
