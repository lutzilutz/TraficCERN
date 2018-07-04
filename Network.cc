#include "Network.h"

Network::Network() {
	road = NULL;
	roundAbout = NULL;
};

void Network::displayRoad() {
	road->display();
}

void Network::displayRoundAbout() {
	roundAbout->display();
}

void Network::setRoad(Road &r) {
	road = &r;
}

void Network::setRoundAbout(RoundAbout &ra) {
	roundAbout = &ra;
}

void Network::evolveRoad() {
	for (unsigned int i(0); i < road->getRoadCells().size(); ++i) {
		road->getRoadCells()[i]->evolve();
	}
}

void Network::computeEvolutionRoad() {
	for (unsigned int i(0); i < road->getRoadCells().size(); ++i) {
		// If Cell is not at the end of the road
		if (road->getRoadCells()[i]->getNextCell() != NULL) {
			// If Cell is occupied
			if (road->getRoadCells()[i]->getIsOccupied()) {
				// If next Cell is occupied
				if (road->getRoadCells()[i]->getNextCell()->getIsOccupied()) {
					road->getRoadCells()[i]->setIsOccupiedNext(1);
				} else {
					road->getRoadCells()[i]->setIsOccupiedNext(0);
					road->getRoadCells()[i]->getNextCell()->setIsOccupiedNext(1);
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

void Network::evolveRoundAbout() {
	for (unsigned int i(0); i < roundAbout->getRoadCells().size()-1; ++i) {
		//cout << i << " - ";
		roundAbout->getRoadCells()[i]->evolve();
	}
}

void Network::computeEvolutionRoundAbout() {
	for (unsigned int i(0); i < roundAbout->getRoadCells().size(); ++i) {
		// If Cell is not at the end of the road
		if (roundAbout->getRoadCells()[i]->getNextCell() != NULL) {
			// If Cell is occupied
			if (roundAbout->getRoadCells()[i]->getIsOccupied()) {
				// If next Cell is occupied
				if (roundAbout->getRoadCells()[i]->getNextCell()->getIsOccupied()) {
					roundAbout->getRoadCells()[i]->setIsOccupiedNext(1);
				} else {
					roundAbout->getRoadCells()[i]->setIsOccupiedNext(0);
					roundAbout->getRoadCells()[i]->getNextCell()->setIsOccupiedNext(1);
				}
				
			} else {
				if (roundAbout->getRoadCells()[i]->getIsOccupiedNext() == -1) {
					roundAbout->getRoadCells()[i]->setIsOccupiedNext(0);
				}
			}
		} else {
			if (roundAbout->getRoadCells()[i]->getIsOccupiedNext() == -1) {
				roundAbout->getRoadCells()[i]->setIsOccupiedNext(0);
			}
		}
		// Checking all Cells are updated
		if (roundAbout->getRoadCells()[i]->getIsOccupiedNext() == -1) {
			cout << "Update problem at Cell #" << i << endl;
		} else {
			//cout << "Cell #" << i << " updated" << endl;
		}
	}
}
