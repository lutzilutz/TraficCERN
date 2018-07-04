#ifndef NETWORK_H
#define NETWORK_H

#include "Road.h"
#include <iostream>

using namespace std;

class Network {
	
	private:
		Road* road;
		Road* nextRoad;
	public:
		Network();
		void display();
		void setRoad(Road &road);
		void test();
		void evolve();
};

#endif

