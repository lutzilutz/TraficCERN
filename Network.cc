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
// Update Cell of the Road according to the next state
void Network::evolveRoad() {
	for (unsigned int i(0); i < road->getRoadCells().size(); ++i) {
		road->getRoadCells()[i]->evolve();
	}
}
// Update Cell of the RoundAbout according to the next state
void Network::evolveRoundAbout() {
	for (unsigned int i(0); i < roundAbout->getRoadCells().size()-1; ++i) {
		roundAbout->getRoadCells()[i]->evolve();
	}
}
// Compute future state of the Cells of the Road
void Network::computeEvolutionRoad() {
	for (unsigned int i(0); i < road->getRoadCells().size(); ++i) {
		// If Cell is not at the end of the road
		if (road->getRoadCells()[i]->getNextCell() != NULL) {
			// If Cell is occupied
			if (road->getRoadCells()[i]->getIsOccupied()) {
				// If next Cell is occupied
				if (road->getRoadCells()[i]->getNextCell()->getIsOccupied()) {
					// Cell stay occupied
					road->getRoadCells()[i]->setIsOccupiedNext(1);
				}
				// If next Cell is not occupied
				else {
					// Vehicule will move to next Cell
					road->getRoadCells()[i]->setIsOccupiedNext(0);
					road->getRoadCells()[i]->getNextCell()->setIsOccupiedNext(1);
				}
			}
			// If Cell is not occupied
			else {
				// If Cell has not been visited
				if (road->getRoadCells()[i]->getIsOccupiedNext() == -1) {
					// Cell stay inoccupied
					road->getRoadCells()[i]->setIsOccupiedNext(0);
				}
			}
		}
		// If Cell is at the end of the road
		else {
			// If Cell has not been visited
			if (road->getRoadCells()[i]->getIsOccupiedNext() == -1) {
				// Cell stay inoccupied
				road->getRoadCells()[i]->setIsOccupiedNext(0);
			}
		}
	}
}
// Compute future state of the Cells of the RoundAbout
void Network::computeEvolutionRoundAbout() {
	for (unsigned int i(0); i < roundAbout->getRoadCells().size(); ++i) {
		// If Cell is not at the end of the road
		if (roundAbout->getRoadCells()[i]->getNextCell() != NULL) {
			// If Cell is occupied
			if (roundAbout->getRoadCells()[i]->getIsOccupied()) {
				// If next Cell is occupied
				if (roundAbout->getRoadCells()[i]->getNextCell()->getIsOccupied()) {
					// Cell stay occupied
					roundAbout->getRoadCells()[i]->setIsOccupiedNext(1);
				}
				// If next Cell is not occupied
				else {
					// Vehicule will move to next Cell
					roundAbout->getRoadCells()[i]->setIsOccupiedNext(0);
					roundAbout->getRoadCells()[i]->getNextCell()->setIsOccupiedNext(1);
				}	
			}
			// If Cell is not occupied
			else {
				// If Cell has not been visited
				if (roundAbout->getRoadCells()[i]->getIsOccupiedNext() == -1) {
					// Cell stay inoccupied
					roundAbout->getRoadCells()[i]->setIsOccupiedNext(0);
				}
			}
		}
		// If Cell is at the end of the road
		else {
			// If Cell has not been visited
			if (roundAbout->getRoadCells()[i]->getIsOccupiedNext() == -1) {
				// Cell stay inoccupied
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
