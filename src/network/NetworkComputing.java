package network;

import java.util.Iterator;

import data.DataManager;
import elements.CrossRoad;
import elements.Ride;
import elements.Road;
import elements.RoundAbout;
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
			}
		} else if (startRoad.equals("rRueDeGeneveSE")) {
			if (endRoad.equals("rRouteDeMeyrinSouthSE")) {
				DataManager.nFrGeEmpiric++;
			} else if (endRoad.equals("rSortieCERNSE")) {
				DataManager.nToEEmpiric++;
			}
		} else if (startRoad.equals("rRueGermaineTillionSW")) {
			if (endRoad.equals("rRouteDeMeyrinSouthSE")) {
				DataManager.nFrGeEmpiric++;
			} else if (endRoad.equals("rSortieCERNSE")) {
				DataManager.nToEEmpiric++;
			}
		} else if (startRoad.equals("rC5SW")) {
			if (endRoad.equals("rRouteDeMeyrinSouthSE")) {
				DataManager.nFrGeEmpiric++;
			}
		} else if (startRoad.equals("rRouteDeMeyrinSouthNW")) {
			if (endRoad.equals("rD884SW") || endRoad.equals("rRueDeGeneveNW") || endRoad.equals("rRueGermaineTillionNE") || endRoad.equals("rC5NE")) {
				DataManager.nGeFrEmpiric++;
			}
		}
	}
	// Compute future state of the Cells of the Road
	public static void computeEvolution(Network n) {
		
		// pre-process
		for (Road r: n.getRoads()) {
			
			// generation of new Vehicles
			if (r.getFlow().get(n.getSimulation().getSimState().getHours()) > 0 && Math.random() < r.getFlow().get(n.getSimulation().getSimState().getHours()) / 3600.0) {
				Vehicle tmp = new Vehicle(n);
				if (!n.isRandomGeneration()) {
					tmp.setRide(n.selectARideWithProbability(r.getName()));
					saveRideIntoData(tmp.getRide());
				} else {
					tmp.setRide(n.selectARide(r.getName()));
					//tmp.getRide().print();
				}
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
					// OUT cell EMPTY + Vehicle has NOT RIDE + RANDOM generation:
					if (v.getCell().getOutCell().getVehicle() == null && (v.getRide() == null || v.getRide().getNextConnections().isEmpty()) && Math.random() < 0.5) {
						v.goToOutCell();
						v.setSpeed(1);
						
					// OUT cell EMPTY + Vehicle has RIDE + Vehicle on the NEXT Connection:
					} else if (v.getRide() != null && !v.getRide().getNextConnections().isEmpty() && v.getCell().getPosition() == v.getRide().getNextConnections().get(0).getPosition()) {
						if (v.getCell().getOutCell().getVehicle() == null) {
							v.goToOutCell();
							v.removeCurrentConnection();
							v.setSpeed(1);
						} else {
							v.stayHere();
							v.setSpeed(0);
						}
					
					// Else if NEXT cell EMPTY
					} else if (v.getCell().getNextCell().getVehicle() == null && v.getSpeed() > 0){
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
					if (v.getCell().getOutCell().getPreviousCell() == null || v.checkPreviousCells(n.getMaxSpeed()+1, v.getCell().getOutCell())) {
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
						v.getRide().print();
						System.out.println();
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
		}
		
		if (n.getSimulation().getSimState().getStep()%60 == 0) {
			writeData(n);
		}
		if ((n.getSimulation().getSimState().getStep()+1)%3600 == 0) {
			Utils.saveData();
		}
	}
	public static void writeData(Network n) {
		
		if (!n.isRandomGeneration()) {
			Utils.writeData(n.getSimulation().getSimState().getTime() + " ");
			Utils.writeData(Integer.toString(n.selectARoad("rD984FSE").getVehicleCounter().getCounter()) + " ");
			Utils.writeData(Integer.toString(n.selectARoad("rD984FNW").getVehicleCounter().getCounter()) + " ");
			Utils.writeData(Integer.toString(n.selectARoad("rD984FSES").getVehicleCounter().getCounter()) + " ");
			Utils.writeData(Integer.toString(n.selectARoad("rD984FNWS").getVehicleCounter().getCounter()) + "\n");
		}
	}
}
