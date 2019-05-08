package network;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import data.DataManager;
import elements.Cell;
import elements.CrossRoad;
import elements.MaxVehicleOutflow;
import elements.Ride;
import elements.Road;
import elements.RoundAbout;
import elements.TrafficLightsSystem;
import elements.Vehicle;
import graphics.Assets;
import main.Simulation;
import utils.Defaults;
import utils.Utils;

public class NetworkComputing {
	
	private static int margin;
	private static boolean countersUpdated = false;
	
	// Pre-processing operations ####################################################################################################################
	// ##############################################################################################################################################
	
	// Compute position on-screen of all cells
	public static void computeCellsPosition(Network n) {
		Utils.log("        Computing Cells position ... ");
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
	// Corrects bounds of the network image
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
	
	// Updates vehicle counter of roads
	public static void computeFlows(Network n) {
		for (Road r: n.getRoads()) {
			if (r.getVehicleCounter() != null) {
				r.getVehicleCounter().computeFlow();
			}
		}
	}
	// Update Cell of the Road according to the next state
	public static void evolve(Network n) {
		
		// Updates vehicle counters
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
		
		// Evolves all vehicles
		for (Vehicle v: n.getVehicles()) {
			v.evolve();
		}
		
		// Removes vehicles leaving the network
		Iterator<Vehicle> iter = n.getVehicles().iterator();
		while (iter.hasNext()) {
			Vehicle vec = iter.next();
			if (vec.hasToLeave()) {
				iter.remove();
				n.increaseNumberOfVehicles(-1);
			}
		}
	}
	// Updates data with the new ride
	public static void saveRideIntoData(Ride ride, Simulation simulation) {
		
		String startRoad = ride.getRoadName();
		String endRoad = ride.getNextConnections().get(ride.getNextConnections().size()-1).getName();
		
		if (!DataManager.useProbabilities) {
			
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
		} else {
			//int hours = simulation.getSimState().getHours();
			
			if (startRoad.equals("rD884NE")) {DataManager.flowPerExitEmpiric[0]++;}
			else if (startRoad.equals("rRueDeGeneveSE")) {DataManager.flowPerExitEmpiric[1]++;}
			else if (startRoad.equals("rRueGermaineTillionSW")) {DataManager.flowPerExitEmpiric[2]++;}
			else if (startRoad.equals("rC5SW")) {DataManager.flowPerExitEmpiric[3]++;}
			else if (startRoad.equals("rRoutePauliSouthNELeft") || startRoad.equals("rRoutePauliSouthNERight")) {DataManager.flowPerExitEmpiric[4]++;}
			else if (startRoad.equals("rRouteDeMeyrinSouthNW")) {DataManager.flowPerExitEmpiric[5]++;}
			else if (startRoad.equals("rRouteBellNW")) {DataManager.flowPerExitEmpiric[6]++;}
			else if (startRoad.equals("rSortieCERNNW")) {DataManager.flowPerExitEmpiric[7]++;}
			
			if (endRoad.equals("rD884SW")) {DataManager.flowPerExitEmpiric[8]++;}
			else if (endRoad.equals("rRueDeGeneveNW")) {DataManager.flowPerExitEmpiric[9]++;}
			else if (endRoad.equals("rRueGermaineTillionNE")) {DataManager.flowPerExitEmpiric[10]++;}
			else if (endRoad.equals("rSortieCERNSE") || endRoad.equals("rD884CERN")) {DataManager.flowPerExitEmpiric[11]++;}
			else if (endRoad.equals("rC5NE")) {DataManager.flowPerExitEmpiric[12]++;}
			else if (endRoad.equals("rRoutePauliSouthSW")) {DataManager.flowPerExitEmpiric[13]++;}
			else if (endRoad.equals("rRouteDeMeyrinSouthSE")) {DataManager.flowPerExitEmpiric[14]++;}
			else if (endRoad.equals("rRouteBellSW")) {DataManager.flowPerExitEmpiric[15]++;}
		}
	}
	public static ArrayList<Ride> applyTransfers(Network n, ArrayList<Ride> rides) {
		String firstConnection = rides.get(0).getRoadName();
		String lastConnection = rides.get(0).getNextConnections().get(rides.get(0).getNextConnections().size()-1).getName();
		ArrayList<Ride> newRides = new ArrayList<Ride>();
		double percentage = 0;
		
		// Entrance B to E ----------------------------------------------------------------------------------
		if (lastConnection.equals("rRoutePauliSouthSW")) {
			
			if (Defaults.getTransferScenario()==1) {percentage = 0.3;}
			else if (Defaults.getTransferScenario()==2) {percentage = 0.7;}
			
			for (AllNetworkRides anr: n.getAllNetworkRides()) {
				for (Ride r: anr.getNetworkRides()) {
					
					if (r.getNextConnections().size()>0) {
						if (r.getRoadName().equals(firstConnection)) {
							if (firstConnection.equals("rD884NE")) {
								if (Math.random()*100 > Defaults.getRepartitionETunnel()) {
									if (r.getNextConnections().get(r.getNextConnections().size()-1).getName().equals("rD884CERN")) {
										if (Math.random()<percentage) {
											newRides.add(r.clone());
											return newRides;
										}
									}
								} else {
									if (r.getNextConnections().get(r.getNextConnections().size()-1).getName().equals("rTunnelSE")) {
										if (Math.random()<percentage) {
											newRides.add(r.clone());
											return newRides;
										}
									}
								}
							} else if (firstConnection.equals("rRueDeGeneveSE")) {
								if (Math.random()*100 > Defaults.getRepartitionETunnel()) {
									if (r.getNextConnections().get(r.getNextConnections().size()-1).getName().equals("rSortieCERNSE")) {
										if (Math.random()<percentage) {
											newRides.add(r.clone());
											return newRides;
										}
									}
								} else {
									if (r.getNextConnections().get(r.getNextConnections().size()-1).getName().equals("rTunnelSE")) {
										if (Math.random()<percentage) {
											newRides.add(r.clone());
											return newRides;
										}
									}
								}
							} else if (firstConnection.equals("rRueGermaineTillionSW")) {
								if (Math.random()*100 > Defaults.getRepartitionETunnel()) {
									if (r.getNextConnections().get(r.getNextConnections().size()-1).getName().equals("rSortieCERNSE")) {
										if (Math.random()<percentage) {
											newRides.add(r.clone());
											return newRides;
										}
									}
								} else {
									if (r.getNextConnections().get(r.getNextConnections().size()-1).getName().equals("rTunnelSE")) {
										if (Math.random()<percentage) {
											newRides.add(r.clone());
											return newRides;
										}
									}
								}
							}
						}
					}
				}
			}
			return rides;
		}
		
		// Entrance A to E ----------------------------------------------------------------------------------
		else if (lastConnection.equals("rRouteBellSW")) {
			
			if (Defaults.getTransferScenario()==1) {percentage = 0.2;}
			else if (Defaults.getTransferScenario()==2) {percentage = 0.5;}
			
			for (AllNetworkRides anr: n.getAllNetworkRides()) {
				for (Ride r: anr.getNetworkRides()) {
					
					if (r.getNextConnections().size()>0) {
						if (r.getRoadName().equals(firstConnection)) {
							if (firstConnection.equals("rD884NE")) {
								if (r.getNextConnections().get(r.getNextConnections().size()-1).getName().equals("rD884CERN")) {
									if (Math.random()<percentage) {
										newRides.add(r.clone());
										return newRides;
									}
								}
							} else if (firstConnection.equals("rRueDeGeneveSE")) {
								if (r.getNextConnections().get(r.getNextConnections().size()-1).getName().equals("rSortieCERNSE")) {
									if (Math.random()<percentage) {
										newRides.add(r.clone());
										return newRides;
									}
								}
							} else if (firstConnection.equals("rRueGermaineTillionSW")) {
								if (r.getNextConnections().get(r.getNextConnections().size()-1).getName().equals("rSortieCERNSE")) {
									if (Math.random()<percentage) {
										newRides.add(r.clone());
										return newRides;
									}
								}
							}
						}
					}
				}
			}
			return rides;
		} else {
			return rides;
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
						c.getVehicle().setSrcColor(Color.magenta);
						c.getVehicle().setDstColor(Color.magenta);
					}
				}
			}
		}
		
		// pre-process
		for (Road r: n.getRoads()) {
			
			// generation of new Vehicles
			double rnd = Math.random();
			if (r.getFlow().get(n.getSimulation().getSimState().getHours()) > 0 && rnd < r.getFlow().get(n.getSimulation().getSimState().getHours()) / 3600.0) {
				Vehicle tmp = new Vehicle(n);
				
				r.addNewVehicle(tmp);
				n.getVehicles().add(tmp);
				n.increaseNumberOfVehicles(1);
			} else if (n.isRandomGeneration() && rnd < r.getFlow().get(n.getSimulation().getSimState().getHours()) / 3600.0) {
				Vehicle tmp = new Vehicle(n);
				tmp.addRide(n.selectARide(r.getName()));
				r.addNewVehicle(tmp);
				n.getVehicles().add(tmp);
				n.increaseNumberOfVehicles(1);
			} 
			
			// tick for outflow
			r.outflowTick();
		}
		
		for (MaxVehicleOutflow maxVhcOutflow: n.getMaxVehicleOutflows()) {
			
			maxVhcOutflow.tick();
			
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
				if (v.hasANextCell() && v.hasAnOutCell()) {
					
					if (!v.isOutCellOccupied() && (v.isOnNextConnection() || v.isRideEmpty() && Math.random() < 0.5)) {
						
						// Check PREVIOUS cells
						if ((v.getCell().isInRoundAbout() || v.getCell().getOutCell().getPreviousCell() == null || v.checkPreviousCells(n.getMaxSpeed()+1, v.getCell().getOutCell()))) {
							v.goToOutCell();
							v.removeCurrentConnection();
							v.setSpeed(1);
						} else {
							v.stayHere();
							v.setSpeed(0);
						}
						
					// Else if NEXT cell EMPTY
					} else if (!v.isNextCellOccupied() && v.getSpeed() > 0){
						v.goToXthNextCell(v.getSpeed());
					} else {
						v.stayHere();
						v.setSpeed(0);
					}
				}
				
				// only NEXT cell
				else if (v.hasANextCell()) {
					
					if (v.getSpeed() > 0) {
						v.goToXthNextCell(v.getSpeed());
					} else {
						v.stayHere();
						v.setSpeed(0);
					}
				}
				
				// only OUT cell
				else if (v.hasAnOutCell()) {
					
					// check road NAME to find road and see if isTrafficLightRed:
					if (v.isTrafficLightRedOnTheRoad()) {
						v.stayHere();
						v.setSpeed(0);
						continue;
					}
					
					// Check PREVIOUS cells
					if (!v.isOutCellOccupied() && (v.getCell().getOutCell().isInRoundAbout() || v.getCell().getOutCell().getPreviousCell() == null || v.checkPreviousCells(n.getMaxSpeed()+1, v.getCell().getOutCell()))) {
						v.goToOutCell();
						v.removeCurrentConnection();
					} else {
						v.stayHere();
						v.setSpeed(0);
					}
				}
				
				// no NEXT or OUT cell
				else if (!v.hasANextCell() && !v.hasAnOutCell()) {
					
					if (v.getCell().isBlocked()) {
						v.stayHere();
						v.setSpeed(0);
					} else {
						v.leaveNetwork();
					}
				}
				
				// should never happen (exhaustive checking of hasANextCell() and hasAnOutCell()
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
			boolean skipThisVehicle = false;
			if (r.getLeakyBucket().size()>0 && r.getRoadCells().get(0).getVehicle() == null) {
				Vehicle v = r.getLeakyBucket().get(0);
				v.setNextPlace(r.getRoadCells().get(0));
				v.setInBucket(false);
				r.removeVehicleFromBucket(r.getLeakyBucket().get(0));
				
				if (!n.isRandomGeneration()) {
					ArrayList<Ride> tmpRides = new ArrayList<Ride>();
					tmpRides = n.selectRidesWithProbability(r.getName());
					if (Defaults.getTransferScenario()!=0 && (n.getSimulation().getSimState().isRushHoursMorning())) {
						v.addRide(applyTransfers(n, tmpRides));
					} else {
						v.addRide(tmpRides);
					}
					if (v.getRide().size()==0) {
						Utils.log("        ERROR : Vehicle with empty ride\n");
					}
					if (v.getRide().size()>0) {
						skipThisVehicle = false;
						saveRideIntoData(v.getRide().get(v.getIdCurrentRide()), n.getSimulation());
					} else {
						skipThisVehicle = true;
						Utils.log("        ERROR : Can't save ride into data (NetworkComputing.computeEvolution) ... trying to skip !\n");
					}
				} else {
					v.addRide(n.selectARide(r.getName()));
				}
				if (!skipThisVehicle) {
					chooseAspect(v);
				} else {
					v.destroyIt();
				}
				
			}
		}
		
		// Removes vehicles instantly (debug)
		Iterator<Vehicle> iter = n.getVehicles().iterator();
		while (iter.hasNext()) {
			Vehicle vec = iter.next();
			if (vec.instantDestroy()) {
				n.artificiallyDestroyedVehicles++;
				iter.remove();
				n.increaseNumberOfVehicles(-1);
			}
		}
		
		if (n.getSimulation().getSimState().getStep()%60 == 0) { // every minute
			writeDataMinutes(n);
		}
		if (n.getSimulation().getSimState().getStep()%900 == 0) { // every 15 minutes
			writeData15Minutes(n);
		}
		if ((n.getSimulation().getSimState().getStep()+1)%3600 == 0) { // every hour
			writeDataHours(n);
			Utils.saveData();
		}
		if ((n.getSimulation().getSimState().getStep()+1)%86400 == 0) { // every day
			
			writeData24Hours(n);
			if (n.getSimulation().getSimState().getSimulationID() == n.getSimulation().getSimState().getNumberOfSimulations()) {
				writeFinalData(n);
			}
		}
	}
	
	// Change aspect of vehicle based on its ride
	public static void chooseAspect(Vehicle v) {
		
		if (v.isSource("rD884NE")) {v.setSrcColor(Assets.vhcFranceCol1);}
		else if (v.isSource("rRueDeGeneveSE")) {v.setSrcColor(Assets.vhcFranceCol2);}
		else if (v.isSource("rRueGermaineTillionSW")) {v.setSrcColor(Assets.vhcFranceCol3);}
		else if (v.isSource("rC5SW")) {v.setSrcColor(Assets.vhcFranceCol4);}
		else if (v.isSource("rRouteDeMeyrinSouthNW")) {v.setSrcColor(Assets.vhcSuisseCol);}
		else if (v.isSource("rRouteBellNE") || v.isSource("rRouteBellNELeft")) {v.setSrcColor(Assets.vhcCERNCol3);}
		else if (v.isSource("rRoutePauliSouthNELeft") || v.isSource("rRoutePauliSouthNERight")) {v.setSrcColor(Assets.vhcCERNCol2);}
		else if (v.isSource("rRoutePauliNorthSW")) {v.setSrcColor(Assets.vhcCERNCol4);}
		else if (v.isSource("rSortieCERNNW")) {v.setSrcColor(Assets.vhcCERNCol1);}
		
		if (!v.getRide().get(v.getIdCurrentRide()).getNextConnections().isEmpty()) {
			if (v.isDestination("rD884SW")) {v.setDstColor(Assets.vhcFranceCol1);}
			else if (v.isDestination("rRueDeGeneveNW")) {v.setDstColor(Assets.vhcFranceCol2);}
			else if (v.isDestination("rRueGermaineTillionNE")) {v.setDstColor(Assets.vhcFranceCol3);}
			else if (v.isDestination("rC5NE")) {v.setDstColor(Assets.vhcFranceCol4);}
			else if (v.isDestination("rRouteDeMeyrinSouthSE")) {v.setDstColor(Assets.vhcSuisseCol);}
			else if (v.isDestination("rRouteBellSW")) {v.setDstColor(Assets.vhcCERNCol3);}
			else if (v.isDestination("rRoutePauliSouthSW")) {v.setDstColor(Assets.vhcCERNCol2);}
			else if (v.isDestination("rRoutePauliNorthNE")) {v.setDstColor(Assets.vhcCERNCol4);}
			else if (v.isDestination("rSortieCERNSE") || v.isDestination("rD884CERN")) {v.setDstColor(Assets.vhcCERNCol1);}
		}
	}
	
	// Write data at the end of N simulations
	public static void writeFinalData(Network n) {
		
		// Leaky buckets --------------------------------------------------------------------------
		for (int i=0 ; i<24*4-1 ; i++) {
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrD884NE().getEsperance().get(i)) + " ");
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrD884NE().getEcartType().get(i)) + " ");
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrRueDeGeneveSE().getEsperance().get(i)) + " ");
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrRueDeGeneveSE().getEcartType().get(i)) + " ");
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrRueGermaineTillionSW().getEsperance().get(i)) + " ");
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrRueGermaineTillionSW().getEcartType().get(i)) + " ");
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrC5SW().getEsperance().get(i)) + " ");
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrC5SW().getEcartType().get(i)) + " ");
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrRouteDeMeyrinSouthNW().getEsperance().get(i)) + " ");
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrRouteDeMeyrinSouthNW().getEcartType().get(i)) + " ");
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrRoutePauliSouthNELeft().getEsperance().get(i)) + " ");
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrRoutePauliSouthNELeft().getEcartType().get(i)) + " ");
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrRoutePauliSouthNERight().getEsperance().get(i)) + " ");
			Utils.writeDataLeakyBucketsAll(Float.toString(n.getSimulation().getSimState().getLBrRoutePauliSouthNERight().getEcartType().get(i)) + "\n");
		}
		
		Utils.saveDataLeakyBucketsAll();
		
		// Time spent -----------------------------------------------------------------------------
		
		for (int i=0 ; i<24 ; i++) {
			Utils.writeDataMeanTimeSpentAll(Float.toString(n.getSimulation().getSimState().getMeanTimeSpent().getEsperance().get(i)) + " ");
			Utils.writeDataMeanTimeSpentAll(Float.toString(n.getSimulation().getSimState().getMeanTimeSpent().getEcartType().get(i)) + "\n");
		}
		
		Utils.saveDataMeanTimeSpentAll();
		
		// Counters -------------------------------------------------------------------------------
		
		for (int i=0 ; i<24*60-1 ; i++) {
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter1A().getEsperance().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter1A().getEcartType().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter1B().getEsperance().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter1B().getEcartType().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter2A().getEsperance().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter2A().getEcartType().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter2B().getEsperance().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter2B().getEcartType().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounterEntranceBLeft().getEsperance().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounterEntranceBLeft().getEcartType().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounterEntranceBRight().getEsperance().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounterEntranceBRight().getEcartType().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounterEntranceELeft().getEsperance().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounterEntranceELeft().getEcartType().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounterEntranceERight().getEsperance().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounterEntranceERight().getEcartType().get(i)) + "\n");
		}
		
		Utils.saveDataCountersAll();

	}
	// Write data at the end of a simulation
	public static void writeData24Hours(Network n) {
		n.getSimulation().getSimState().getLBrD884NE().saveTemp();
		n.getSimulation().getSimState().getLBrRueDeGeneveSE().saveTemp();
		n.getSimulation().getSimState().getLBrRueGermaineTillionSW().saveTemp();
		n.getSimulation().getSimState().getLBrC5SW().saveTemp();
		n.getSimulation().getSimState().getLBrRouteDeMeyrinSouthNW().saveTemp();
		n.getSimulation().getSimState().getLBrRoutePauliSouthNELeft().saveTemp();
		n.getSimulation().getSimState().getLBrRoutePauliSouthNERight().saveTemp();
		
		n.getSimulation().getSimState().getMeanTimeSpent().saveTemp();
		
		n.getSimulation().getSimState().getCounter1A().saveTemp();
		n.getSimulation().getSimState().getCounter1B().saveTemp();
		n.getSimulation().getSimState().getCounter2A().saveTemp();
		n.getSimulation().getSimState().getCounter2B().saveTemp();
		n.getSimulation().getSimState().getCounterEntranceBLeft().saveTemp();
		n.getSimulation().getSimState().getCounterEntranceBRight().saveTemp();
		n.getSimulation().getSimState().getCounterEntranceELeft().saveTemp();
		n.getSimulation().getSimState().getCounterEntranceERight().saveTemp();
	}
	// Write data every hour
	public static void writeDataHours(Network n) {
		if (!n.isRandomGeneration()) {
			
			double meanTimeLastHour = 0;
			for (Integer i: DataManager.timeSpent) {
				meanTimeLastHour += i;
			}
			meanTimeLastHour = meanTimeLastHour / (double) (DataManager.timeSpent.size());
			
			n.getSimulation().getSimState().getMeanTimeSpent().addTemp((int) meanTimeLastHour);
		}
	}
	// Write data every 15 minutes into output
	public static void writeData15Minutes(Network n) {
		
		if (!n.isRandomGeneration()) {
			
			Utils.writeDataLeakyBuckets(Integer.toString(n.selectARoad("rD884NE").getLeakyBucket().size()) + "\t");
			n.getSimulation().getSimState().getLBrD884NE().addTemp(n.selectARoad("rD884NE").getLeakyBucket().size());
			
			Utils.writeDataLeakyBuckets(Integer.toString(n.selectARoad("rRueDeGeneveSE").getLeakyBucket().size()) + "\t");
			n.getSimulation().getSimState().getLBrRueDeGeneveSE().addTemp(n.selectARoad("rRueDeGeneveSE").getLeakyBucket().size());
			
			Utils.writeDataLeakyBuckets(Integer.toString(n.selectARoad("rRueGermaineTillionSW").getLeakyBucket().size()) + "\t");
			n.getSimulation().getSimState().getLBrRueGermaineTillionSW().addTemp(n.selectARoad("rRueGermaineTillionSW").getLeakyBucket().size());
			
			Utils.writeDataLeakyBuckets(Integer.toString(n.selectARoad("rC5SW").getLeakyBucket().size()) + "\t");
			n.getSimulation().getSimState().getLBrC5SW().addTemp(n.selectARoad("rC5SW").getLeakyBucket().size());
			
			if (n.selectARoad("rRouteDeMeyrinSouthNW") != null) {
				Utils.writeDataLeakyBuckets(Integer.toString(n.selectARoad("rRouteDeMeyrinSouthNW").getLeakyBucket().size()) + "\t");
				n.getSimulation().getSimState().getLBrRouteDeMeyrinSouthNW().addTemp(n.selectARoad("rRouteDeMeyrinSouthNW").getLeakyBucket().size());
			}
			
			Utils.writeDataLeakyBuckets(Integer.toString(n.selectARoad("rRoutePauliSouthNELeft").getLeakyBucket().size()) + "\t");
			n.getSimulation().getSimState().getLBrRoutePauliSouthNELeft().addTemp(n.selectARoad("rRoutePauliSouthNELeft").getLeakyBucket().size());
			
			Utils.writeDataLeakyBuckets(Integer.toString(n.selectARoad("rRoutePauliSouthNERight").getLeakyBucket().size()) + "\n");
			n.getSimulation().getSimState().getLBrRoutePauliSouthNERight().addTemp(n.selectARoad("rRoutePauliSouthNERight").getLeakyBucket().size());
			
		}
	}
	// Write data every minute into output
	public static void writeDataMinutes(Network n) {
		
		if (!n.isRandomGeneration()) {
			
			n.getSimulation().getSimState().getCounter1A().addTemp(n.selectARoad("rD984FSE").getVehicleCounter().getCounter());
			n.getSimulation().getSimState().getCounter1B().addTemp(n.selectARoad("rD984FNW").getVehicleCounter().getCounter());
			n.getSimulation().getSimState().getCounter2A().addTemp(n.selectARoad("rD984FSES").getVehicleCounter().getCounter());
			n.getSimulation().getSimState().getCounter2B().addTemp(n.selectARoad("rD984FNWS").getVehicleCounter().getCounter());
			n.getSimulation().getSimState().getCounterEntranceBLeft().addTemp(n.selectARoad("rRoutePauliSouthNELeft").getVehicleCounter().getCounter());
			n.getSimulation().getSimState().getCounterEntranceBRight().addTemp(n.selectARoad("rRoutePauliSouthNERight").getVehicleCounter().getCounter());
			n.getSimulation().getSimState().getCounterEntranceELeft().addTemp(n.selectARoad("rD884CERN").getVehicleCounter().getCounter());
			n.getSimulation().getSimState().getCounterEntranceERight().addTemp(n.selectARoad("rSortieCERNSE").getVehicleCounter().getCounter());
			
			Utils.writeDataCounters(n.getSimulation().getSimState().getTime() + " ");
			Utils.writeDataCounters(Integer.toString(n.selectARoad("rD984FSE").getVehicleCounter().getCounter()) + " ");
			Utils.writeDataCounters(Integer.toString(n.selectARoad("rD984FNW").getVehicleCounter().getCounter()) + " ");
			Utils.writeDataCounters(Integer.toString(n.selectARoad("rD984FSES").getVehicleCounter().getCounter()) + " ");
			Utils.writeDataCounters(Integer.toString(n.selectARoad("rD984FNWS").getVehicleCounter().getCounter()) + "\n");
			
			Utils.writeDataSegmentCounters(n.getSimulation().getSimState().getTime() + " ");
			Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FSE").getNumberOfVehicles(0)) + " ");
			Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FSE").getNumberOfVehicles(1)) + " ");
			Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FSE").getNumberOfVehicles(2)) + " ");
			Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FNW").getNumberOfVehicles(0)) + " ");
			Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FNW").getNumberOfVehicles(1)) + " ");
			Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FNW").getNumberOfVehicles(2)) + " ");
			if (n.selectARoad("rD984FSES2") != null && n.selectARoad("rD984FSES3") != null) {
				Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FSES").getNumberOfVehicles(0) + n.selectARoad("rD984FSES2").getNumberOfVehicles(0) + n.selectARoad("rD984FSES3").getNumberOfVehicles(0)) + " ");
				Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FSES").getNumberOfVehicles(1) + n.selectARoad("rD984FSES2").getNumberOfVehicles(0) + n.selectARoad("rD984FSES3").getNumberOfVehicles(1)) + " ");
				Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FSES").getNumberOfVehicles(2) + n.selectARoad("rD984FSES2").getNumberOfVehicles(0) + n.selectARoad("rD984FSES3").getNumberOfVehicles(2)) + " ");
			} else {
				Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FSES").getNumberOfVehicles(0)) + " ");
				Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FSES").getNumberOfVehicles(1)) + " ");
				Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FSES").getNumberOfVehicles(2)) + " ");
			}
			
			if (n.selectARoad("rD984FNWS2") != null) {
				Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FNWS").getNumberOfVehicles(0) + n.selectARoad("rD984FNWS2").getNumberOfVehicles(0)) + " ");
				Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FNWS").getNumberOfVehicles(1) + n.selectARoad("rD984FNWS2").getNumberOfVehicles(1)) + " ");
				Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FNWS").getNumberOfVehicles(2) + n.selectARoad("rD984FNWS2").getNumberOfVehicles(2)) + "\n");
			} else {
				Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FNWS").getNumberOfVehicles(0)) + " ");
				Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FNWS").getNumberOfVehicles(1)) + " ");
				Utils.writeDataSegmentCounters(Integer.toString(n.selectARoad("rD984FNWS").getNumberOfVehicles(2)) + "\n");
			}
		}
	}
	public static void clearVehicles(Network n) {
		n.restart();
	}
}
