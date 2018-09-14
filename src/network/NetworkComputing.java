package network;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import data.DataManager;
import elements.Cell;
import elements.CrossRoad;
import elements.Ride;
import elements.Road;
import elements.RoundAbout;
import elements.TrafficLightsSystem;
import elements.Vehicle;
import utils.Utils;

public class NetworkComputing {
	
	private static int margin;
	private static boolean countersUpdated = false;
	
	// Pre-processing operations ####################################################################################################################
	// ##############################################################################################################################################	
	public static void computeCellsPosition(Network n) {
		Utils.log("computing Cells position ... ");
		Utils.tick();
		
		margin = n.getCellWidth()*10;
		for (CrossRoad cr: n.getCrossRoads()) {
			for (int i=0 ; i<4 ; i++) {
				cr.getMiddleCells()[i].setX(cr.getX() - n.getCellWidth()/2 - n.getCellWidth()/2*Math.sqrt(2.0)*Math.sin(2*Math.PI*(-cr.getDirection() - 90 + 45 + i*90)/360));
				cr.getMiddleCells()[i].setY(cr.getY() - n.getCellHeight()/2 - n.getCellWidth()/2*Math.sqrt(2.0)*Math.cos(2*Math.PI*(-cr.getDirection() - 90 + 45 + i*90)/360));
			}
		}
		for (Road r: n.getRoads()) {
			int tmp = 0;
			double x = r.getX() - n.getCellWidth()/2.0;
			double y = r.getY() - n.getCellWidth()/2.0;
			for (int i=0 ; i<r.getLength() ; i++) {
				
				if (r.getReorientations().size()-1>tmp) {
					if (r.getReorientations().get(tmp+1).getX() == i) {
						tmp++;
					}
				}
				
				r.getRoadCells().get(i).setX(x + (n.getCellWidth()/2.0)*Math.sin(2*Math.PI*r.getReorientations().get(tmp).getY()/360.0));
				r.getRoadCells().get(i).setY(y - (n.getCellWidth()/2.0)*Math.cos(2*Math.PI*r.getReorientations().get(tmp).getY()/360.0));
				
				x += n.getCellWidth()*Math.sin(2*Math.PI*r.getReorientations().get(tmp).getY()/360.0);
				y += -(n.getCellWidth())*Math.cos(2*Math.PI*r.getReorientations().get(tmp).getY()/360.0);
				
				correctBounds(x, y);
			}
		}
		for (RoundAbout r: n.getRoundAbouts()) {
			double radius = (r.getLength()*n.getCellWidth())/(2*Math.PI) - r.getInnerLane()*n.getCellWidth();
			for (int i=0 ; i<r.getLength() ; i++) {
				double angle = 2*Math.PI*i/r.getLength() - 2*Math.PI*r.getDirection()/360;
				double x = r.getX()-radius*Math.sin(angle);
				r.getRoadCells().get(i).setX(x);
				double y = r.getY()-radius*Math.cos(angle);
				r.getRoadCells().get(i).setY(y);
				correctBounds(x, y);
			}
		}
		
		for (int j=0 ; j<n.getZones().size() ; j++) {
			for (int i=0 ; i<n.getZones().get(j).npoints ; i++) {
				correctBounds(n.getZones().get(j).xpoints[i], n.getZones().get(j).ypoints[i]);
			}
		}
		n.setxDefaultOffset(NetworkRendering.bounds.x);
		n.setyDefaultOffset(NetworkRendering.bounds.y);
		n.setxOffset(n.getxDefaultOffset());
		n.setyOffset(n.getyDefaultOffset());
		
		Utils.log("done");
		Utils.logTime();
	}
	public static void correctBounds(double x, double y) {
		if (x<NetworkRendering.bounds.x) {
			NetworkRendering.bounds.width += NetworkRendering.bounds.x - x ;//+ margin;
			NetworkRendering.bounds.x = (int) x-margin;
		} else if (x>NetworkRendering.bounds.width+NetworkRendering.bounds.x - margin) {
			NetworkRendering.bounds.width = (int) x-NetworkRendering.bounds.x + 1*margin;
		}
		
		if (y<NetworkRendering.bounds.y) {
			NetworkRendering.bounds.height += NetworkRendering.bounds.y - y;// + margin;
			NetworkRendering.bounds.y = (int) y - margin;
		} else if (y>NetworkRendering.bounds.height+NetworkRendering.bounds.y - margin) {
			NetworkRendering.bounds.height = (int) y-NetworkRendering.bounds.y + 1*margin;
		}
	}
	
	// Computing operations #########################################################################################################################
	// ##############################################################################################################################################	
	
	public static void computeFlows(Network n) {
		for (Road r: n.getRoads()) {
			if (r.getVehicleCounter() != null) {
				r.getVehicleCounter().computeFlow();
			}
		}
	}
	// Update Cell of the Road according to the next state
	public static void evolve(Network n) {
		
		for (Vehicle v: n.getVehicles()) {
			if (v.getNextPlace() == null) {
				//System.out.println("Coucou !");
			}
			
			for (Road r: n.getRoads()) {
				if (r.getVehicleCounter() != null) {
					r.getVehicleCounter().computeCounter();
				}
			}
			if (n.getSimulation().getSimState().getStep()%60 == 0) {
				if (!countersUpdated) {
					computeFlows(n);
					countersUpdated = true;
				}
			} else {
				countersUpdated = false;
			}
			v.evolve();
		}
		
		Iterator<Vehicle> iter = n.getVehicles().iterator();
		while (iter.hasNext()) {
			Vehicle vec = iter.next();
			if (vec.hasToLeave()) {
				iter.remove();
				n.increaseNumberOfVehicles(-1);
			}
		}
	}
	public static void saveRideIntoData(Ride ride) {
		String startRoad = ride.getRoadName();
		String endRoad = ride.getNextConnections().get(ride.getNextConnections().size()-1).getName();
		
		if (startRoad.equals("rD884NE")) {
			if (endRoad.equals("rRouteDeMeyrinSouthSE")) {
				DataManager.nFrGeEmpiric++;
			} else if (endRoad.equals("rSortieCERNSE") || endRoad.equals("rD884CERN")) {
				DataManager.nToEEmpiric++;
			} else if (endRoad.equals("rRouteBellSW")) {
				DataManager.nToAEmpiric++;
			} else if (endRoad.equals("rRoutePauliSouthSW")) {
				DataManager.nToBEmpiric++;
			}
		} else if (startRoad.equals("rRueDeGeneveSE")) {
			if (endRoad.equals("rRouteDeMeyrinSouthSE")) {
				DataManager.nFrGeEmpiric++;
			} else if (endRoad.equals("rSortieCERNSE")) {
				DataManager.nToEEmpiric++;
			} else if (endRoad.equals("rRouteBellSW")) {
				DataManager.nToAEmpiric++;
			} else if (endRoad.equals("rRoutePauliSouthSW")) {
				DataManager.nToBEmpiric++;
			}
		} else if (startRoad.equals("rRueGermaineTillionSW")) {
			if (endRoad.equals("rRouteDeMeyrinSouthSE")) {
				DataManager.nFrGeEmpiric++;
			} else if (endRoad.equals("rSortieCERNSE")) {
				DataManager.nToEEmpiric++;
			} else if (endRoad.equals("rRouteBellSW")) {
				DataManager.nToAEmpiric++;
			} else if (endRoad.equals("rRoutePauliSouthSW")) {
				DataManager.nToBEmpiric++;
			}
		} else if (startRoad.equals("rC5SW")) {
			if (endRoad.equals("rRouteDeMeyrinSouthSE")) {
				DataManager.nFrGeEmpiric++;
			} else if (endRoad.equals("rRouteBellSW")) {
				DataManager.nToAEmpiric++;
			} else if (endRoad.equals("rRoutePauliSouthSW")) {
				DataManager.nToBEmpiric++;
			}
		} else if (startRoad.equals("rRouteDeMeyrinSouthNW")) {
			if (endRoad.equals("rD884SW") || endRoad.equals("rRueDeGeneveNW") || endRoad.equals("rRueGermaineTillionNE") || endRoad.equals("rC5NE")) {
				DataManager.nGeFrEmpiric++;
			} else if (endRoad.equals("rRouteBellSW")) {
				DataManager.nToAEmpiric++;
			} else if (endRoad.equals("rRoutePauliSouthSW")) {
				DataManager.nToBEmpiric++;
			}
		} else if (startRoad.equals("rSortieCERNNW")) {
			if (endRoad.equals("rD884SW") || endRoad.equals("rRueDeGeneveNW") || endRoad.equals("rRueGermaineTillionNE")) {
				DataManager.nFromEEmpiric++;
			}
		} else if (startRoad.equals("rRouteBellNE")) {
			if (endRoad.equals("rD884SW") || endRoad.equals("rRueDeGeneveNW") || endRoad.equals("rRueGermaineTillionNE") || endRoad.equals("rC5NE") || endRoad.equals("rRouteDeMeyrinSouthSE")) {
				DataManager.nFromAEmpiric++;
			}
		} else if (startRoad.equals("rRoutePauliSouthNE") || startRoad.equals("rRoutePauliSouthNELeft") || startRoad.equals("rRoutePauliSouthNERight")) {
			if (endRoad.equals("rD884SW") || endRoad.equals("rRueDeGeneveNW") || endRoad.equals("rRueGermaineTillionNE") || endRoad.equals("rC5NE") || endRoad.equals("rRouteDeMeyrinSouthSE")) {
				DataManager.nFromBEmpiric++;
			}
		}
	}
	// Compute future state of the Cells of the Road
	public static void computeEvolution(Network n) {
		
		for (TrafficLightsSystem tls: n.getTrafficLightsSystems()) {
			tls.nextStep();
		}
		
		for (Road r: n.getRoads()) {
			for (Cell c: r.getRoadCells()) {
				if (c.getVehicle() != null) {
					if (!c.getVehicle().getCurrentRoadName().equals(r.getName())) {
						c.getVehicle().setColor(Color.magenta);
					}
				}
			}
		}
		
		// pre-process
		for (Road r: n.getRoads()) {
			
			/*for (Road road2: n.getRoads()) {
				if (road2.getName().equals("rSortieCERNNW")) {
					System.out.println(road2.getName() + " : ");
					for (int i=0; i<road2.getFlow().size() ; i++) {
						System.out.print(road2.getFlow().get(i) + " ");
					}
					System.out.println();
				}
			}*/
			// generation of new Vehicles
			if (r.getFlow().get(n.getSimulation().getSimState().getHours()) > 0 && Math.random() < r.getFlow().get(n.getSimulation().getSimState().getHours()) / 3600.0) {
				Vehicle tmp = new Vehicle(n);
				//System.out.println("vhc gen");
				if (!n.isRandomGeneration()) {
					tmp.addRide(n.selectRidesWithProbability(r.getName()));
					/*System.out.println("--------------------------------------------------------------------------");
					for (Ride ride: tmp.getRide()) {
						ride.print();
						System.out.println();
					}*/
					/*if (r.getName().equals("rD884NE")) {// || r.getName().equals("rRoutePauliSouthNERight")) {
						System.out.print(r.getName() + " - ");
						for (Ride ride: tmp.getRide()) {
							ride.print();
							System.out.println();
						}
					}*/
					
					saveRideIntoData(tmp.getRide().get(tmp.getIdCurrentRide()));
				} else {
					tmp.addRide(n.selectARide(r.getName()));
				}
				chooseAspect(tmp);
				r.addNewVehicle(tmp);
				n.getVehicles().add(tmp);
				n.increaseNumberOfVehicles(1);
			} else if (n.isRandomGeneration() && Math.random() < r.getFlow().get(n.getSimulation().getSimState().getHours()) / 3600.0) {
				Vehicle tmp = new Vehicle(n);
				tmp.addRide(n.selectARide(r.getName()));
				r.addNewVehicle(tmp);
				n.getVehicles().add(tmp);
				n.increaseNumberOfVehicles(1);
			} 
			
			// tick for outflow
			r.outflowTick();
		}
		
		
		
		for (CrossRoad cr: n.getCrossRoads()) {
			if (!cr.getRoadsIN()[(cr.getStateOfTrafficLight()+1)%4].isTrafficLightRed()) {
				cr.setTrafficLightState(cr.getStateOfTrafficLight()%4);
			}
		}
		
		for (Vehicle v: n.getVehicles()) {
			v.nextSpeed();
		}
		
		
		// evolution of existing vehicles
		for (Vehicle v: n.getVehicles()) {
			// Vehicle in a CELL
			if (v.getCell() != null) {
				
				//NEXT and OUT cells
				if (v.getCell().getOutCell() != null && v.getCell().getNextCell() != null) {
					
					// Lane choice based on number of vehicles
					
					/*if (hasMultipleLaneChoice(v) && n.selectARoad(v.getCurrentRoadName()) != null) {
						System.out.println("Multiple choices at #" + v.getCell().getPosition() + " of " + v.getCurrentRoadName());
						//for (Ride ride: v.getRide()) {
							//ride.print();
							//System.out.println();
						//}
						ArrayList<Integer> index = new ArrayList<Integer>();
						for (Ride ride: v.getRide()) {
							if (!index.contains(ride.getNextConnections().get(0).getPosition())) {
								index.add(ride.getNextConnections().get(0).getPosition());
							}
						}
						ArrayList<Integer> numberOfVhcOnRoad = new ArrayList<Integer>();
						
						chooseBestLane(n, v);
					}*/
					
					// OUT cell EMPTY + Vehicle has NOT RIDE + RANDOM generation:
					if (v.getCell().getOutCell().getVehicle() == null && !v.getCell().getOutCell().isAnOverlapedCellOccupied() && (v.getRide() == null || v.getRide().get(v.getIdCurrentRide()).getNextConnections().isEmpty()) && Math.random() < 0.5) {// < 0.5) {
						// Check PREVIOUS cells
						if (v.getCell().getOutCell().getVehicle() == null && !(v.getCell().getOutCell().isAnOverlapedCellOccupied()) && (v.getCell().isInRoundAbout() || v.getCell().getOutCell().getPreviousCell() == null || v.checkPreviousCells(n.getMaxSpeed()+1, v.getCell().getOutCell()))) {
							v.goToOutCell();
							v.removeCurrentConnection();
							v.setSpeed(1);
						} else {
							v.stayHere();
							v.setSpeed(0);
						}
						//v.goToOutCell();
						//v.setSpeed(1);
						
					// OUT cell EMPTY + Vehicle has RIDE + Vehicle on the NEXT Connection:
					} else if (v.getRide().get(v.getIdCurrentRide()) != null && !v.getRide().get(v.getIdCurrentRide()).getNextConnections().isEmpty() && v.getCell().getPosition() == v.getRide().get(v.getIdCurrentRide()).getNextConnections().get(0).getPosition()) {
						if (v.getCell().getOutCell().getVehicle() == null && !(v.getCell().getOutCell().isAnOverlapedCellOccupied()) && (v.getCell().isInRoundAbout() || v.getCell().getOutCell().getPreviousCell() == null || v.checkPreviousCells(n.getMaxSpeed()+1, v.getCell().getOutCell()))) {
							v.goToOutCell();
							v.removeCurrentConnection();
							v.setSpeed(1);
						} else {
							v.stayHere();
							v.setSpeed(0);
						}
					
					// Else if NEXT cell EMPTY
					} else if (v.getCell().getNextCell().getVehicle() == null && !v.getCell().getNextCell().isAnOverlapedCellOccupied() && v.getSpeed() > 0){
						v.goToXthNextCell(v.getSpeed());
						
					} else {
						v.stayHere();
						v.setSpeed(0);
					}
				}
				
				// only NEXT cell
				else if (v.getCell().getNextCell() != null) {
					if (v.getSpeed() > 0) {
						v.goToXthNextCell(v.getSpeed());
					} else {
						v.stayHere();
						v.setSpeed(0);
					}
				}
				
				// only OUT cell
				else if (v.getCell().getOutCell() != null) {
					
					// check road NAME to find road and see if isTrafficLightRed:
					if (n.getRoad(v.getCell().getRoadName()) != null) {
						if (n.getRoad(v.getCell().getRoadName()).isTrafficLightRed()) {
							v.stayHere();
							v.setSpeed(0);
							continue;
						}
					}
					
					// Check PREVIOUS cells
					if (v.getCell().getOutCell().getVehicle() == null && !(v.getCell().getOutCell().isAnOverlapedCellOccupied()) && (v.getCell().getOutCell().isInRoundAbout() || v.getCell().getOutCell().getPreviousCell() == null || v.checkPreviousCells(n.getMaxSpeed()+1, v.getCell().getOutCell()))) {
						v.goToOutCell();
						v.removeCurrentConnection();
					} else {
						v.stayHere();
						v.setSpeed(0);
					}
				}
				
				// no NEXT or OUT cell
				else if (v.getCell().getNextCell() == null && v.getCell().getOutCell() == null) {
					if (v.getCell().isBlocked()) {
						v.stayHere();
						v.setSpeed(0);
					} else {
						//v.getRide().print();
						//System.out.println();
						v.leaveNetwork();
					}
				}
				
				// should never happen ...
				else {
					v.stayHere();
					v.setSpeed(0);
				}	
			}
		}
		
		
		for (CrossRoad cr: n.getCrossRoads()) {
			cr.setCounter(cr.getCounter()+1);
			if (cr.getCounter()>=cr.getTimeTrafficLight()) {
				if (cr.getCounter()>=cr.getTimeTrafficLight()+5) {
					cr.setStateOfTrafficLight((cr.getStateOfTrafficLight()+1)%4);
					cr.setTrafficLightState(cr.getStateOfTrafficLight());
					cr.setCounter(0);
				} else if (cr.getCounter()==cr.getTimeTrafficLight()) {
					cr.setAllTrafficLightsRed();
				}
			}
		}
		
		for (Road r: n.getRoads()) {
			if (r.getLeakyBucket().size()>0 && r.getRoadCells().get(0).getVehicle() == null) {
				Vehicle v = r.getLeakyBucket().get(0);
				v.setNextPlace(r.getRoadCells().get(0));
				v.setInBucket(false);
				r.removeVehicleFromBucket(r.getLeakyBucket().get(0));
			}
			
			// Prints ride of vehicles coming in entrance B (should all be empty)
			
			/*if (r.getName().equals("rRoutePauliSouthSW")) {
				if (r.getRoadCells().get(0).getVehicle() != null) {
					r.getRoadCells().get(0).getVehicle().getRide().print();
					System.out.println();
				}
			}*/
		}
		
		if (n.getSimulation().getSimState().getStep()%60 == 0) {
			writeData(n);
		}
		if ((n.getSimulation().getSimState().getStep()+1)%3600 == 0) {
			Utils.saveData();
		}
	}
	public static boolean hasMultipleLaneChoice(Vehicle v) {
		int tmp = -1;
		if (!v.getRide().get(v.getIdCurrentRide()).getNextConnections().isEmpty()) {
			v.getRide().get(v.getIdCurrentRide()).getNextConnections().get(0).getPosition();
		}
		for (Ride ride: v.getRide()) {
			if (!ride.getNextConnections().isEmpty()) {
				if (ride.getNextConnections().get(0).getPosition() != tmp) {
					return true;
				}
			}
		}
		return false;
	}
	public static void chooseBestLane(Network n, Vehicle v) {
		ArrayList<Integer> numberOfVhcPerRoad = new ArrayList<Integer>();
		int min = 1000;
		for (Ride ride: v.getRide()) {
			
			if (v.getCell().getPosition() == ride.getNextConnections().get(0).getPosition()) {
				numberOfVhcPerRoad.add(v.numberOfVhcAhead(n.selectARoad(ride.getNextConnections().get(0).getName())));
				
			} else if (v.getCell().getPosition() < ride.getNextConnections().get(0).getPosition()) {
				numberOfVhcPerRoad.add(v.numberOfVhcAhead(n.selectARoad(v.getCurrentRoadName())));
			} else {
				System.out.println("Probleme in chooseBestLane");
			}
			
			if (numberOfVhcPerRoad.get(numberOfVhcPerRoad.size()-1) < min) {
				min = numberOfVhcPerRoad.get(numberOfVhcPerRoad.size()-1);
			}
		}
		
		for (int i=0 ; i<numberOfVhcPerRoad.size() ; i++) {
			if (numberOfVhcPerRoad.get(i) == min) {
				
				v.setIdCurrentRide(i);
			}
		}
		
	}
	public static void chooseAspect(Vehicle v) {
		if (!v.getRide().get(v.getIdCurrentRide()).getNextConnections().isEmpty()) {
			if (v.getRide().get(v.getIdCurrentRide()).getNextConnections().get(v.getRide().get(v.getIdCurrentRide()).getNextConnections().size()-1).getName().equals("rSortieCERNSE")
					|| v.getRide().get(v.getIdCurrentRide()).getNextConnections().get(v.getRide().get(v.getIdCurrentRide()).getNextConnections().size()-1).getName().equals("rD884CERN")) {
				v.setColor(Color.red);
			} else if (v.getRide().get(v.getIdCurrentRide()).getNextConnections().get(v.getRide().get(v.getIdCurrentRide()).getNextConnections().size()-1).getName().equals("rRoutePauliSouthSW")) {
				v.setColor(Color.green);
			} else if (v.getRide().get(v.getIdCurrentRide()).getNextConnections().get(v.getRide().get(v.getIdCurrentRide()).getNextConnections().size()-1).getName().equals("rRouteBellSW")) {
				v.setColor(Color.blue);
			} else if (v.getRide().get(v.getIdCurrentRide()).getNextConnections().get(v.getRide().get(v.getIdCurrentRide()).getNextConnections().size()-1).getName().equals("rRouteDeMeyrinSouthSE")) {
				v.setColor(Color.black);
				v.setIsTransiting(true);
			} else if (v.getRide().get(v.getIdCurrentRide()).getRoadName().equals("rRouteDeMeyrinSouthNW")) {
				v.setColor(Color.black);
				v.setIsTransiting(true);
			} else {
				v.setColor(Color.darkGray);
			}
		}
	}
	public static void writeData(Network n) {
		
		if (!n.isRandomGeneration()) {
			Utils.writeDataCounters(n.getSimulation().getSimState().getTime() + " ");
			Utils.writeDataCounters(Integer.toString(n.selectARoad("rD984FSE").getVehicleCounter().getCounter()) + " ");
			Utils.writeDataCounters(Integer.toString(n.selectARoad("rD984FNW").getVehicleCounter().getCounter()) + " ");
			Utils.writeDataCounters(Integer.toString(n.selectARoad("rD984FSES").getVehicleCounter().getCounter()) + " ");
			Utils.writeDataCounters(Integer.toString(n.selectARoad("rD984FNWS").getVehicleCounter().getCounter()) + "\n");
			Utils.writeDataLeakyBuckets(n.getSimulation().getSimState().getTime() + " ");
			Utils.writeDataLeakyBuckets(Integer.toString(n.selectARoad("rD884NE").getLeakyBucket().size()) + " ");
			Utils.writeDataLeakyBuckets(Integer.toString(n.selectARoad("rRueDeGeneveSE").getLeakyBucket().size()) + " ");
			Utils.writeDataLeakyBuckets(Integer.toString(n.selectARoad("rRueGermaineTillionSW").getLeakyBucket().size()) + " ");
			Utils.writeDataLeakyBuckets(Integer.toString(n.selectARoad("rC5SW").getLeakyBucket().size()) + " ");
			if (n.selectARoad("rRouteDeMeyrinSouthNW") != null) {
				Utils.writeDataLeakyBuckets(Integer.toString(n.selectARoad("rRouteDeMeyrinSouthNW").getLeakyBucket().size()) + "\n");
			}
		}
		
		
	}
}
