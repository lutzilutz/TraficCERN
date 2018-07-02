#ifndef ROUNDABOUT_H
#define ROUNDABOUT_H

#include "Road.h"
#include <iostream>


class RoundAbout: public Road
{
	private:
	
	public:
		RoundAbout(vector<Cell> &vCells);
		virtual void display();
};



#endif
