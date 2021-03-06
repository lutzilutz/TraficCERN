package network;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import data.DataManager;
import elements.Cell;
import elements.MaxVehicleOutflow;
import elements.Ride;
import elements.Road;
import elements.RoundAbout;
import elements.TrafficLightsSystem;
import elements.Vehicle;
import graphics.Assets;
import utils.Defaults;
import utils.Utils;

public class NetworkComputing {
	
	private static int margin;
	private static boolean countersUpdated = false;
	public static boolean writtenFinalData = false;
	
	// Pre-processing operations ############################################################################
	// ######################################################################################################
	
	// Compute position on-screen of all cells
	public static void computeCellsPosition(Network n) {
		
		// Log beginning
		Utils.logInfo("Computing Cells position ... ");
		Utils.tick();
		
		// Compute offset and all cells position
		computeOffset(n);
		computeRoadCellsPosition(n);
		computeRoundaboutCellsPosition(n);
		
		// Log ending
		Utils.log("done");
		Utils.logTime();
	}
	private static void computeOffset(Network n) {
		margin = n.getCellWidth()*10;
		n.setxDefaultOffset(NetworkRendering.bounds.x);
		n.setyDefaultOffset(NetworkRendering.bounds.y);
		n.setxOffset(n.getxDefaultOffset());
		n.setyOffset(n.getyDefaultOffset());
		for (int j=0 ; j<n.getZones().size() ; j++) {
			for (int i=0 ; i<n.getZones().get(j).npoints ; i++) {
				correctBounds(n.getZones().get(j).xpoints[i], n.getZones().get(j).ypoints[i]);
			}
		}
	}
	private static void computeRoadCellsPosition(Network n) {
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
	}
	private static void computeRoundaboutCellsPosition(Network n) {
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
	}
	// Corrects bounds of the network image
	private static void correctBounds(double x, double y) {
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
	
	// Computing operations #################################################################################
	// ######################################################################################################
	
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
	// Updates vehicle counter of roads
	private static void computeFlows(Network n) {
		for (Road r: n.getRoads()) {
			if (r.getVehicleCounter() != null) {
				r.getVehicleCounter().computeFlow();
			}
		}
	}
	private static ArrayList<Ride> applyTransfers(Network n, ArrayList<Ride> rides) {
		String firstConnection = rides.get(0).getRoadName();
		String lastConnection = rides.get(0).getNextConnections().get(rides.get(0).getNextConnections().size()-1).getName();
		ArrayList<Ride> newRides = new ArrayList<Ride>();
		double percentage = 0;
		
		// Entrance B to E ----------------------------------------------------------------------------------
		if (lastConnection.equals("rRoutePauliSouthSW")) {
			
			if (Defaults.getTransferScenario()==1) {percentage = DataManager.inputMatrixReports[0][0]/100.0;}
			else if (Defaults.getTransferScenario()==2) {percentage = DataManager.inputMatrixReports[0][1]/100.0;}
			
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
			
			if (Defaults.getTransferScenario()==1) {percentage = DataManager.inputMatrixReports[1][0]/100.0;}
			else if (Defaults.getTransferScenario()==2) {percentage = DataManager.inputMatrixReports[1][1]/100.0;}
			
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
			} 
			
			// tick for outflow
			r.outflowTick();
		}
		
		for (MaxVehicleOutflow maxVhcOutflow: n.getMaxVehicleOutflows()) {
			maxVhcOutflow.tick();
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
					Utils.logWarningln("else block called in NetworkComputing.computeEvolution() that shouldn't be called");
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
				
				ArrayList<Ride> tmpRides = new ArrayList<Ride>();
				tmpRides = n.selectRidesWithProbability(r.getName());
				if (Defaults.getTransferScenario()!=0 && (n.getSimulation().getSimState().isRushHoursMorning())) {
					v.addRide(applyTransfers(n, tmpRides));
				} else {
					v.addRide(tmpRides);
				}
				if (v.getRide().size()==0) {
					Utils.logErrorln("Vehicle with empty ride");
				}
				if (v.getRide().size()>0) {
					skipThisVehicle = false;
				} else {
					skipThisVehicle = true;
					Utils.logErrorln("Can't save ride into data (NetworkComputing.computeEvolution) ... trying to skip !");
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
		int nVhcMoving = 0;
		while (iter.hasNext()) {
			Vehicle vec = iter.next();
			if (vec.instantDestroy()) {
				n.artificiallyDestroyedVehicles++;
				iter.remove();
				n.increaseNumberOfVehicles(-1);
			}
			if (vec.getSpeed() != 0) {
				nVhcMoving++;
			}
		}
		if (n.getVehicles().size() > 200 && nVhcMoving == 0) {
			Utils.logErrorln("Network blocked !");
		}
		
		if (n.getSimulation().getSimState().getStep()%60 == 0) { // every minute
			writeDataMinutes(n);
		}
		if (n.getSimulation().getSimState().getStep()%900 == 0) { // every 15 minutes
			writeData15Minutes(n);
		}
		if ((n.getSimulation().getSimState().getStep()+1)%3600 == 0) { // every hour
			writeDataHours(n);
		}
		if ((n.getSimulation().getSimState().getStep()+1)%86400 == 0) { // every day
			
			writeData24Hours(n);
			if (n.getSimulation().getSimState().getSimulationID() == n.getSimulation().getSimState().getNumberOfSimulations()) {
				writeFinalData(n);
			}
		}
	}
	
	// Change aspect of vehicle based on its ride
	private static void chooseAspect(Vehicle v) {
		
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
	private static void writeFinalData(Network n) {
		Utils.logInfo("Writing output data into files ... ");
		n.getSimulation().getSimState().incrementWritingState();
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
		n.getSimulation().getSimState().incrementWritingState();
		
		// Time spent -----------------------------------------------------------------------------
		
		for (int i=0 ; i<24 ; i++) {
			Utils.writeDataMeanTimeSpentAllTransit(Float.toString(n.getSimulation().getSimState().getMeanTimeSpentTransit().getEsperance().get(i)) + " ");
			Utils.writeDataMeanTimeSpentAllTransit(Float.toString(n.getSimulation().getSimState().getMeanTimeSpentTransit().getEcartType().get(i)) + " ");
			Utils.writeDataMeanTimeSpentAllTransit(Float.toString(n.getSimulation().getSimState().getDistanceTravelledTransit().getEsperance().get(i)) + " ");
			Utils.writeDataMeanTimeSpentAllTransit(Float.toString(n.getSimulation().getSimState().getDistanceTravelledTransit().getEcartType().get(i)) + "\n");
			Utils.writeDataMeanTimeSpentAllCERN(Float.toString(n.getSimulation().getSimState().getMeanTimeSpentCERN().getEsperance().get(i)) + " ");
			Utils.writeDataMeanTimeSpentAllCERN(Float.toString(n.getSimulation().getSimState().getMeanTimeSpentCERN().getEcartType().get(i)) + " ");
			Utils.writeDataMeanTimeSpentAllCERN(Float.toString(n.getSimulation().getSimState().getDistanceTravelledCERN().getEsperance().get(i)) + " ");
			Utils.writeDataMeanTimeSpentAllCERN(Float.toString(n.getSimulation().getSimState().getDistanceTravelledCERN().getEcartType().get(i)) + "\n");
		}
		
		Utils.saveDataMeanTimeSpentAll();
		n.getSimulation().getSimState().incrementWritingState();
		
		// Counters -------------------------------------------------------------------------------
		
		for (int i=0 ; i<24*60-1 ; i++) {
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter1A().getEsperance().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter1A().getEcartType().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter1B().getEsperance().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter1B().getEcartType().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter2A().getEsperance().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter2A().getEcartType().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter2B().getEsperance().get(i)) + " ");
			Utils.writeDataCountersAll(Float.toString(n.getSimulation().getSimState().getCounter2B().getEcartType().get(i)) + "\n");
		}
		
		Utils.saveDataCountersAll();
		n.getSimulation().getSimState().setFinalDataWritingState(-1);
		writtenFinalData = true;
		Utils.logln("done");
	}
	// Write data at the end of a simulation
	private static void writeData24Hours(Network n) {
		n.getSimulation().getSimState().getLBrD884NE().saveTemp();
		n.getSimulation().getSimState().getLBrRueDeGeneveSE().saveTemp();
		n.getSimulation().getSimState().getLBrRueGermaineTillionSW().saveTemp();
		n.getSimulation().getSimState().getLBrC5SW().saveTemp();
		n.getSimulation().getSimState().getLBrRouteDeMeyrinSouthNW().saveTemp();
		n.getSimulation().getSimState().getLBrRoutePauliSouthNELeft().saveTemp();
		n.getSimulation().getSimState().getLBrRoutePauliSouthNERight().saveTemp();
		
		n.getSimulation().getSimState().getMeanTimeSpentTransit().saveTemp();
		n.getSimulation().getSimState().getMeanTimeSpentCERN().saveTemp();
		n.getSimulation().getSimState().getDistanceTravelledTransit().saveTemp();
		n.getSimulation().getSimState().getDistanceTravelledCERN().saveTemp();
		
		n.getSimulation().getSimState().getCounter1A().saveTemp();
		n.getSimulation().getSimState().getCounter1B().saveTemp();
		n.getSimulation().getSimState().getCounter2A().saveTemp();
		n.getSimulation().getSimState().getCounter2B().saveTemp();
	}
	// Write data every hour
	private static void writeDataHours(Network n) {
			
		double meanTimeLastHourTransit = 0;
		for (Integer i: DataManager.timeSpentTransit) {
			meanTimeLastHourTransit += i;
		}
		meanTimeLastHourTransit = meanTimeLastHourTransit / (double) (DataManager.timeSpentTransit.size());
		n.getSimulation().getSimState().getMeanTimeSpentTransit().addTemp((int) meanTimeLastHourTransit);
		DataManager.timeSpentTransit = new ArrayList<Integer>();
		
		double meanTimeLastHourCERN = 0;
		for (Integer i: DataManager.timeSpentCERN) {
			meanTimeLastHourCERN += i;
		}
		meanTimeLastHourCERN = meanTimeLastHourCERN / (double) (DataManager.timeSpentCERN.size());
		n.getSimulation().getSimState().getMeanTimeSpentCERN().addTemp((int) meanTimeLastHourCERN);
		DataManager.timeSpentCERN = new ArrayList<Integer>();
		
		double distanceLastHourTransit = 0;
		for (Integer i: DataManager.distanceTravelledTransit) {
			distanceLastHourTransit += i;
		}
		distanceLastHourTransit = distanceLastHourTransit / (double) (DataManager.distanceTravelledTransit.size());
		n.getSimulation().getSimState().getDistanceTravelledTransit().addTemp((int) distanceLastHourTransit);
		DataManager.distanceTravelledTransit = new ArrayList<Integer>();
		
		double distanceLastHourCERN = 0;
		for (Integer i: DataManager.distanceTravelledCERN) {
			distanceLastHourCERN += i;
		}
		distanceLastHourCERN = distanceLastHourCERN / (double) (DataManager.distanceTravelledCERN.size());
		n.getSimulation().getSimState().getDistanceTravelledCERN().addTemp((int) distanceLastHourCERN);
		DataManager.distanceTravelledCERN = new ArrayList<Integer>();
	}
	// Write data every 15 minutes into output
	private static void writeData15Minutes(Network n) {
		
		n.getSimulation().getSimState().getLBrD884NE().addTemp(n.selectARoad("rD884NE").getLeakyBucket().size());
		n.getSimulation().getSimState().getLBrRueDeGeneveSE().addTemp(n.selectARoad("rRueDeGeneveSE").getLeakyBucket().size());
		n.getSimulation().getSimState().getLBrRueGermaineTillionSW().addTemp(n.selectARoad("rRueGermaineTillionSW").getLeakyBucket().size());
		n.getSimulation().getSimState().getLBrC5SW().addTemp(n.selectARoad("rC5SW").getLeakyBucket().size());
		
		if (n.selectARoad("rRouteDeMeyrinSouthNW") != null) {
			n.getSimulation().getSimState().getLBrRouteDeMeyrinSouthNW().addTemp(n.selectARoad("rRouteDeMeyrinSouthNW").getLeakyBucket().size());
		}
		
		n.getSimulation().getSimState().getLBrRoutePauliSouthNELeft().addTemp(n.selectARoad("rRoutePauliSouthNELeft").getLeakyBucket().size());
		n.getSimulation().getSimState().getLBrRoutePauliSouthNERight().addTemp(n.selectARoad("rRoutePauliSouthNERight").getLeakyBucket().size());
		
	}
	// Write data every minute into output
	private static void writeDataMinutes(Network n) {
		n.getSimulation().getSimState().getCounter1A().addTemp(n.selectARoad("rD984FSE").getVehicleCounter().getCounter());
		n.getSimulation().getSimState().getCounter1B().addTemp(n.selectARoad("rD984FNW").getVehicleCounter().getCounter());
		n.getSimulation().getSimState().getCounter2A().addTemp(n.selectARoad("rD984FSES").getVehicleCounter().getCounter());
		n.getSimulation().getSimState().getCounter2B().addTemp(n.selectARoad("rD984FNWS").getVehicleCounter().getCounter());
	}
}
