#ifndef NETWORK_H
#define NETWORK_H

#include "Road.h"
#include "RoundAbout.h"
#include <iostream>

using namespace std;

class Network {
	
	private:
		Road* road;
		RoundAbout* roundAbout;
	public:
		Network();
		void displayRoad();
		void displayRoundAbout();
		void setRoad(Road &r);
		void setRoundAbout(RoundAbout &ra);
		void evolveRoad();
		void computeEvolutionRoad();
		void evolveRoundAbout();
		void computeEvolutionRoundAbout();
};

#endif

