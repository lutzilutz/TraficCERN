package main;

import java.util.ArrayList;

import elements.Road;

public class Network {

	ArrayList<Road> roads = new ArrayList<Road>();
	
	public Network() {
		Road r1 = new Road(10);
		roads.add(r1);
	}
	public void display() {
		for (Road r: roads) {
			r.display();
		}
		System.out.print("\n");
	}
	
	// Update Cell of the Road according to the next state
	public void evolve() {
		for (Road r: roads) {
			for (int i=0; i < r.getLength(); ++i) {
				r.getRoadCells().get(i).evolve();
			}
		}
	}
	// Compute future state of the Cells of the Road
	public void computeEvolution() {
		for (Road r: roads) {
			for (int i=0; i < r.getLength(); ++i) {
				
				if (i < r.getLength()-1) {
					
					if (r.getRoadCells().get(i).isOccupied()) {
						
						if (r.getRoadCells().get(i+1).isOccupied()) {
							
							r.getRoadCells().get(i).setIsOccupiedNext(1);
							
						} else {
							r.getRoadCells().get(i).setIsOccupiedNext(0);
							r.getRoadCells().get(i+1).setIsOccupiedNext(1);
							
						}
					} else {
						
						if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							r.getRoadCells().get(i).setIsOccupiedNext(0);;
						}
					}
				}
				// If Cell is at the end of the road
				else {
					// If Cell has not been visited
					if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
						// Cell stay inoccupied
						r.getRoadCells().get(i).setIsOccupiedNext(0);;
					}
				}
			}
		}
	}
	// Compute future state of the Cells of the RoundAbout
	/*void Network::computeEvolutionRoundAbout() {
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
	}*/
}
