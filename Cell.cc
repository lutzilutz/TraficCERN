#include "Cell.h"

Cell::Cell() : nextCell(NULL), isOccupied(false), isOccupiedNext(-1)
{
	
}

void Cell::display()
{
	if (isOccupied)
	{
		cout << "[x]" ;
	} else {
		cout << "[ ]" ;
	}
}

Cell* &Cell::getNextCell()
{
	return nextCell;
}

void Cell::setNextCell(Cell &c)
{
	nextCell = &c;
}

bool Cell::getIsOccupied()
{
	return isOccupied;
}

void Cell::setIsOccupied(bool b)
{
	isOccupied = b;
}

int Cell::getIsOccupiedNext()
{
	return isOccupiedNext;
}

void Cell::setIsOccupiedNext(int i)
{
	isOccupiedNext = i;
}

void Cell::evolve()
{
	if (isOccupiedNext == 0) {
		isOccupied = false;
	} else if (isOccupiedNext == 1) {
		isOccupied = true;
	} else if (isOccupiedNext == -1) {
		cout << "ERROR ===" << endl;
	}
	
	isOccupiedNext = -1;
	/*if (isOccupied)
	{
		if (not(nextCell==NULL))
		{
			if (not(nextCell->getIsOccupied()))
			{
				setIsOccupied(false);
				nextCell->setIsOccupied(true);
			}
		} else 
		{
			setIsOccupied(false);
		}
	}*/
}
