#include "Network.h"

Network::Network() {
	road = NULL;
	nextRoad = NULL;
};

void Network::display() {
	road->display();
	nextRoad->display();
}

void Network::setRoad(Road &r) {
	road = &r;
	nextRoad = &r;
}

void Network::test() {
	
}

void Network::evolve() {
	
}
