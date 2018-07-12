package network;

import elements.CrossRoad;
import elements.Road;
import elements.RoundAbout;

public class NetworkComputing {

	// Computing operations #########################################################################################################################
	// ##############################################################################################################################################	
	public static void computeCellsPosition(Network n) {
		for (CrossRoad cr: n.getCrossRoads()) {
			for (int i=0 ; i<4 ; i++) {
				cr.getMiddleCells()[i].setX(cr.getX() - n.getCellWidth()/2 - n.getCellWidth()/2*Math.sqrt(2.0)*Math.sin(2*Math.PI*(-cr.getDirection() - 90 + 45 + i*90)/360));
				cr.getMiddleCells()[i].setY(cr.getY() - n.getCellHeight()/2 - n.getCellWidth()/2*Math.sqrt(2.0)*Math.cos(2*Math.PI*(-cr.getDirection() - 90 + 45 + i*90)/360));
			}
		}
		for (Road r: n.getRoads()) {
			for (int i=0 ; i<r.getLength() ; i++) {
				r.getRoadCells().get(i).setX(r.getX()-n.getCellWidth()/2+(i*n.getCellWidth() + n.getCellWidth()/2)*Math.sin(2*Math.PI*r.getDirection()/360));
				r.getRoadCells().get(i).setY(r.getY()-n.getCellHeight()/2-(i*n.getCellWidth() + n.getCellWidth()/2)*Math.cos(2*Math.PI*r.getDirection()/360));
			}
		}
		for (RoundAbout r: n.getRoundAbouts()) {
			double radius = (r.getLength()*n.getCellWidth())/(2*Math.PI);
			for (int i=0 ; i<r.getLength() ; i++) {
				double angle = 2*Math.PI*i/r.getLength() - 2*Math.PI*r.getDirection()/360;
				r.getRoadCells().get(i).setX(r.getX()-radius*Math.sin(angle));
				r.getRoadCells().get(i).setY(r.getY()-radius*Math.cos(angle));
			}
		}
	}
	// Update Cell of the Road according to the next state
	public static void evolve(Network n) {
		for (CrossRoad CR: n.getCrossRoads()) {
			for (int i=0; i < 4; ++i) {
				CR.getMiddleCells()[i].evolve();
			}
		}
		for (Road r: n.getRoads()) {
			for (int i=0; i < r.getLength(); ++i) {
				r.getRoadCells().get(i).evolve();
			}
		}
		for (RoundAbout r: n.getRoundAbouts()) {
			for (int i=0; i < r.getLength(); ++i) {
				r.getRoadCells().get(i).evolve();
			}
		}
	}
	// Compute future state of the Cells of the Road
	public static void computeEvolution(Network n) {
		for (CrossRoad CR: n.getCrossRoads()) {
			if (!CR.getRoadsIN()[(CR.getStateOfTrafficLight()+1)%4].isTrafficLightRed()) {
				CR.setTrafficLightState(CR.getStateOfTrafficLight()%4);
			}
			for (int i=0; i < 4; ++i) {
				if (CR.getMiddleCells()[i].getOutCell() == null) {
					if (CR.getMiddleCells()[i].isOccupied()) {
						
						if (CR.getMiddleCells()[i].getNextCell().isOccupied()) {
							
							CR.getMiddleCells()[i].setIsOccupiedNext(1);
							
						} else {
							CR.getMiddleCells()[i].setIsOccupiedNext(0);
							CR.getMiddleCells()[i].getNextCell().setIsOccupiedNext(1);
							
						}
					} else {
						
						if (CR.getMiddleCells()[i].getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							CR.getMiddleCells()[i].setIsOccupiedNext(0);
						}
					}
				} else {
					if (CR.getMiddleCells()[i].isOccupied()) {
						if ((int)(Math.random()*2)==1) {
							if (CR.getMiddleCells()[i].getOutCell().isOccupied()) {
								
								CR.getMiddleCells()[i].setIsOccupiedNext(1);
								
							} else {
								CR.getMiddleCells()[i].setIsOccupiedNext(0);
								CR.getMiddleCells()[i].getOutCell().setIsOccupiedNext(1);
								
							}
						} else {
							if (CR.getMiddleCells()[i].getNextCell().isOccupied()) {
								
								CR.getMiddleCells()[i].setIsOccupiedNext(1);
								
							} else {
								CR.getMiddleCells()[i].setIsOccupiedNext(0);
								CR.getMiddleCells()[i].getNextCell().setIsOccupiedNext(1);
								
							}
						}
						
					} else {
						
						if (CR.getMiddleCells()[i].getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							CR.getMiddleCells()[i].setIsOccupiedNext(0);
						}
					}
				}
			}
			CR.setCounter(CR.getCounter()+1);
			if (CR.getCounter()>=CR.getTimeTrafficLight()) {
				if (CR.getCounter()>=CR.getTimeTrafficLight()+5) {
					CR.setStateOfTrafficLight((CR.getStateOfTrafficLight()+1)%4);
					CR.setTrafficLightState(CR.getStateOfTrafficLight());
					CR.setCounter(0);
				} else if (CR.getCounter()==CR.getTimeTrafficLight()) {
					CR.setAllTrafficLightsRed();
				}
				
				
			}
		}
		for (Road r: n.getRoads()) {
			for (int i=0; i < r.getLength(); ++i) {
				
				if (i < r.getLength()-1) {
					
					if (r.getRoadCells().get(i).isOccupied()) {
						
						if (r.getRoadCells().get(i).getNextCell().isOccupied()) {
							
							r.getRoadCells().get(i).setIsOccupiedNext(1);
							
						} else {
							r.getRoadCells().get(i).setIsOccupiedNext(0);
							r.getRoadCells().get(i).getNextCell().setIsOccupiedNext(1);
							
						}
					} else {
						
						if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							r.getRoadCells().get(i).setIsOccupiedNext(0);
						}
					}
				}
				// If Cell is at the end of the road
				else {
					if (r.getRoadCells().get(i).getNextCell() != null) {
						if (r.getRoadCells().get(i).isOccupied()) {
							if (r.getRoadCells().get(i).getNextCell().isOccupied() || r.getRoadCells().get(i).getNextCell().getPreviousCell().isOccupied() || r.isTrafficLightRed()) {
								r.getRoadCells().get(i).setIsOccupiedNext(1);
							} else {
								r.getRoadCells().get(i).setIsOccupiedNext(0);
								r.getRoadCells().get(i).getNextCell().setIsOccupiedNext(1);
							}
						}
					}
					// If Cell has not been visited
					if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
						// Cell stay inoccupied
						r.getRoadCells().get(i).setIsOccupiedNext(0);;
					}
				}
			}
			// Random generation
			if (r.getRoadCells().get(0).getIsOccupiedNext() != 1 && r.getGenerateVehicules()) {
				if (Math.random()<0.1) {
					r.getRoadCells().get(0).setIsOccupiedNext(1);
				}
			}
		}
		for (RoundAbout r: n.getRoundAbouts()) {
			for (int i=0; i < r.getLength(); ++i) {
				
				if (r.getRoadCells().get(i).getOutCell() == null) {
					if (r.getRoadCells().get(i).isOccupied()) {
						
						if (r.getRoadCells().get(i).getNextCell().isOccupied()) {
							
							r.getRoadCells().get(i).setIsOccupiedNext(1);
							
						} else {
							r.getRoadCells().get(i).setIsOccupiedNext(0);
							r.getRoadCells().get(i).getNextCell().setIsOccupiedNext(1);
							
						}
					} else {
						
						if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							r.getRoadCells().get(i).setIsOccupiedNext(0);
						}
					}
				} else {
					if (r.getRoadCells().get(i).isOccupied()) {
						if ((int)(Math.random()*2)==1) {
							if (r.getRoadCells().get(i).getOutCell().isOccupied()) {
								
								r.getRoadCells().get(i).setIsOccupiedNext(1);
								
							} else {
								r.getRoadCells().get(i).setIsOccupiedNext(0);
								r.getRoadCells().get(i).getOutCell().setIsOccupiedNext(1);
								
							}
						} else {
							if (r.getRoadCells().get(i).getNextCell().isOccupied()) {
								
								r.getRoadCells().get(i).setIsOccupiedNext(1);
								
							} else {
								r.getRoadCells().get(i).setIsOccupiedNext(0);
								r.getRoadCells().get(i).getNextCell().setIsOccupiedNext(1);
								
							}
						}
						
					} else {
						
						if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							r.getRoadCells().get(i).setIsOccupiedNext(0);
						}
					}
				}
			}
			// Random generation
			/*if (r.getRoadCells().get(0).getIsOccupiedNext() != 1) {
				if (Math.random()<0.1) {
					r.getRoadCells().get(0).setIsOccupiedNext(1);
				}
			}*/
		}
	}
}
