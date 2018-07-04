#include "Network.h"

Network::Network() {
	road = NULL;
};

void Network::display() {
	road->display();
}

void Network::setRoad(Road &r) {
	road = &r;
}

void Network::test() {
	
}

void Network::evolve() {
	for (unsigned int i(0); i < road->getRoadCells().size(); ++i) {
		//cout << i << " - ";
		road->getRoadCells()[i]->evolve();
	}
}

void Network::computeEvolution() {
	for (unsigned int i(0); i < road->getRoadCells().size(); ++i) {
		// If Cell is not at the end of the road
		if (road->getRoadCells()[i]->getNextCell() != NULL) {
			// If Cell is occupied
			if (road->getRoadCells()[i]->getIsOccupied()) {
				// If next Cell is occupied
				if (road->getRoadCells()[i+1]->getIsOccupied()) {
					road->getRoadCells()[i]->setIsOccupiedNext(1);
				} else {
					road->getRoadCells()[i]->setIsOccupiedNext(0);
					road->getRoadCells()[i+1]->setIsOccupiedNext(1);
				}
			} else {
				if (road->getRoadCells()[i]->getIsOccupiedNext() == -1) {
					road->getRoadCells()[i]->setIsOccupiedNext(0);
				}
			}
		} else {
			if (road->getRoadCells()[i]->getIsOccupiedNext() == -1) {
				road->getRoadCells()[i]->setIsOccupiedNext(0);
			}
		}
		
	}
}
