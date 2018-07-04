#ifndef CELL_H
#define CELL_H

#include <iostream>
using namespace std;

class Cell 
{
	private:
		Cell* nextCell;
		bool isOccupied;
		int isOccupiedNext;//1-yes 0-no -1-error
	public:
		Cell();
		void display();
		Cell* &getNextCell();
		void setNextCell(Cell &c);
		bool getIsOccupied();
		void setIsOccupied(bool b);
		int getIsOccupiedNext();
		void setIsOccupiedNext(int i);
		void evolve();
};

#endif
