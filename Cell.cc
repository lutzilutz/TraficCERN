#include "Cell.h"

Cell::Cell() : nextCell(NULL), isOccupied(false)
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

void Cell::evolve()
{
	if (isOccupied)
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
	}
}
