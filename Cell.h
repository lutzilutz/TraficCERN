#ifndef CELL_H
#define CELL_H

#include <iostream>
using namespace std;

class Cell 
{
	private:
		Cell* nextCell;
		bool isOccupied;
		
	public:
		Cell();
		void display();
		Cell* &getNextCell();
		void setNextCell(Cell &c);
		bool getIsOccupied();
		void setIsOccupied(bool b);
		void evolve();
};

#endif
