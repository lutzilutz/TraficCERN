package network;

import java.util.Iterator;

import elements.Cell;
import elements.CrossRoad;
import elements.Road;
import elements.RoundAbout;
import elements.Vehicle;

public class NetworkComputing {
	
	private static int margin;

	// Computing operations #########################################################################################################################
	// ##############################################################################################################################################	
	public static void computeCellsPosition(Network n) {
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
			double radius = (r.getLength()*n.getCellWidth())/(2*Math.PI);
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
	// Update Cell of the Road according to the next state
	public static void evolve(Network n) {
		
		for (Vehicle v: n.getVehicles()) {
			if (v.getNextPlace() == null) {
				//System.out.println("Coucou !");
			}
			v.evolve();
		}
		
		Iterator<Vehicle> iter = n.getVehicles().iterator();
		while (iter.hasNext()) {
		    Vehicle vec = iter.next();
		    if (vec.hasToLeave()) {
		    	iter.remove();
		    }
		}
	}
	// Compute future state of the Cells of the Road
	
	
	public static void computeEvolution(Network n) {
		// generation of new Vehicles
		for (Road r: n.getRoads()) {
			if (r.getGenerateVehicules() && r.getRoadCells().get(0).getVehicle() == null && Math.random()<0.05) {
				Vehicle tmp = new Vehicle(n);
				tmp.setRide(n.selectARide(r.getName()));
				tmp.setNextPlace(r.getRoadCells().get(0));
				n.getVehicles().add(tmp);
			}
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
			if (v.getRide() == null || v.getRide().getNextConnections().isEmpty()) {
				if (v.getCell() != null) {
					//NEXT and OUT cells
					if (v.getCell().getOutCell() != null && v.getCell().getNextCell() != null) {
						if (v.getCell().getOutCell().getVehicle() == null && Math.random() < 0.5) {
							v.goToOutCell();
							v.setSpeed(1);
						} else {
							if (v.getSpeed() > 0) {
								Cell ci = v.getCell();
								for (int iter=0; iter < v.getSpeed(); ++iter) {
									if (ci.getNextCell() != null) {
										if (ci.getNextCell().getVehicle() != null) {
											v.setSpeed(iter);
											continue;
										}
										ci = ci.getNextCell();
									} else {
										v.setSpeed(iter);
										continue;
									}
								}
								v.setNextPlace(ci);
							} else {
								v.stayHere();
								v.setSpeed(0);
							}
						}
					}
					// only NEXT cell
					else if (v.getCell().getNextCell() != null) {
						if (v.getSpeed() > 0) {
							Cell ci = v.getCell();
							for (int iter=0; iter < v.getSpeed(); ++iter) {
								if (ci.getNextCell() != null) {
									if (ci.getNextCell().getVehicle() != null) {
										v.setSpeed(iter);
										continue;
									}
									ci = ci.getNextCell();
								} else {
									v.setSpeed(iter);
									continue;
								}
							}
							v.setNextPlace(ci);
						} else {
							v.stayHere();
							v.setSpeed(0);
						}
					}
					// only OUT cell
					else if (v.getCell().getOutCell() != null) {
						if (n.getRoad(v.getCell().getRoadName()) != null) {
							if (n.getRoad(v.getCell().getRoadName()).isTrafficLightRed()) {
								v.stayHere();
								v.setSpeed(0);
								continue;
							}
						}
						if (v.checkPreviousCells(n.getMaxSpeed()+1, v.getCell().getOutCell())) {
							v.goToOutCell();
						} else {
							v.stayHere();
							v.setSpeed(0);
						}
					}
					// no NEXT or OUT cell
					else if (v.getCell().getNextCell() == null && v.getCell().getOutCell() == null) {
						v.leaveNetwork();
					}
					// should never happen ...
					else {
						v.stayHere();
						v.setSpeed(0);
					}
				}
			} else {
				if (v.getCell() != null) {
					//NEXT and OUT cells
					if (v.getCell().getOutCell() != null && v.getCell().getNextCell() != null) {
						if (v.getCell().getOutCell().getVehicle() == null  && v.getCell().getPosition() == v.getRide().getNextConnections().get(0).getPosition()) {
							if (!v.getRide().getNextConnections().isEmpty()) {
								v.getRide().getNextConnections().remove(0);
							}
							v.goToOutCell();
							v.setSpeed(1);
						} else if (v.getCell().getNextCell().getVehicle() == null) {
							if (v.getSpeed() > 0) {
								Cell ci = v.getCell();
								for (int iter=0; iter < v.getSpeed(); ++iter) {
									if (ci.getNextCell() != null) {
										if (ci.getNextCell().getVehicle() != null) {
											v.setSpeed(iter);
											continue;
										}
										ci = ci.getNextCell();
									} else {
										v.setSpeed(iter);
										continue;
									}
								}
								v.setNextPlace(ci);
							} else {
								v.stayHere();
								v.setSpeed(0);
							}
						} else {
							v.stayHere();
							v.setSpeed(0);
						}
					}
					// only NEXT cell
					else if (v.getCell().getNextCell() != null) {
						if (v.getSpeed() > 0) {
							Cell ci = v.getCell();
							for (int iter=0; iter < v.getSpeed(); ++iter) {
								if (ci.getNextCell() != null) {
									if (ci.getNextCell().getVehicle() != null) {
										v.setSpeed(iter);
										continue;
									}
									ci = ci.getNextCell();
								} else {
									v.setSpeed(iter);
									continue;
								}
							}
							v.setNextPlace(ci);
						} else {
							v.stayHere();
							v.setSpeed(0);
						}
					}
					// only OUT cell
					else if (v.getCell().getOutCell() != null) {
						if (n.getRoad(v.getCell().getRoadName()) != null) {
							if (n.getRoad(v.getCell().getRoadName()).isTrafficLightRed()) {
								v.stayHere();
								v.setSpeed(0);
								continue;
							}
						}
						if (v.checkPreviousCells(n.getMaxSpeed()+1, v.getCell().getOutCell())) {
							if (!v.getRide().getNextConnections().isEmpty()) {
								v.getRide().getNextConnections().remove(0);
							}
							v.goToOutCell();
						} else {
							v.stayHere();
							v.setSpeed(0);
						}
					}
					// no NEXT or OUT cell
					else if (v.getCell().getNextCell() == null && v.getCell().getOutCell() == null) {
						v.leaveNetwork();
					}
					// should never happen ...
					else {
						v.stayHere();
						v.setSpeed(0);
					}
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
		
		/*
		
		for (Vehicle v: n.getVehicles()) {
			if (v.getRide() == null || v.getRide().getNextConnections().isEmpty()) {
				if (v.getCell() != null) {
					// NEXT and OUT cells
					if (v.getCell().getNextCell() != null && v.getCell().getOutCell() != null) {
						if (v.getCell().getOutCell().getVehicle() == null && Math.random() < 0.5) {
							v.goToOutCell();
						} else if (v.getCell().getNextCell().getVehicle() == null) {
							v.goToNextCell();
						} else {
							v.stayHere();
						}
					}
					// only NEXT cell
					else if (v.getCell().getNextCell() != null) {
						if (v.getCell().getNextCell().getVehicle() == null) {
							v.goToNextCell();
						} else {
							v.stayHere();
						}
					}
					// only OUT cell
					else if (v.getCell().getOutCell() != null) {
						if (n.getRoad(v.getCell().getRoadName()) != null) {
							if (n.getRoad(v.getCell().getRoadName()).isTrafficLightRed()) {
								v.stayHere();
								continue;
							}
						}
						if (v.getCell().getOutCell().getVehicle() == null) {
							if (v.getCell().getOutCell().getPreviousCell() != null) {
								// if there is no vehicle in the road where Vehicle must go
								if (v.getCell().getOutCell().getPreviousCell().getVehicle() == null) {
									if (v.getCell().getOutCell().getPreviousCell().getPreviousCell() != null) {
										// if there is no vehicle in the road where Vehicle must go, a cell before
										if (v.getCell().getOutCell().getPreviousCell().getPreviousCell().getVehicle() == null) {
											v.goToOutCell();
										} else {
											v.stayHere();
										}
									}
								} else {
									v.stayHere();
								}
							} else {
								v.goToOutCell();
							}
							
						} else {
							v.stayHere();
						}
					}
					// no NEXT or OUT cell
					else if (v.getCell().getNextCell() == null && v.getCell().getOutCell() == null) {
						v.leaveNetwork();
					}
					// should never happen ...
					else {
						v.stayHere();
					}
				}
			} else {
				if (v.getCell() != null) {
					// NEXT and OUT cells
					if (v.getCell().getNextCell() != null && v.getCell().getOutCell() != null) {
						if (v.getCell().getOutCell().getVehicle() == null && v.getCell().getPosition() == v.getRide().getNextConnections().get(0).getPosition()) {
							if (!v.getRide().getNextConnections().isEmpty()) {
								v.getRide().getNextConnections().remove(0);
							}
							v.goToOutCell();
						} else if (v.getCell().getNextCell().getVehicle() == null) {
							v.goToNextCell();
						} else {
							v.stayHere();
						}
					}
					// only NEXT cell
					else if (v.getCell().getNextCell() != null) {
						if (v.getCell().getNextCell().getVehicle() == null) {
							v.goToNextCell();
						} else {
							v.stayHere();
						}
					}
					// only OUT cell
					else if (v.getCell().getOutCell() != null) {
						if (n.getRoad(v.getCell().getRoadName()) != null) {
							if (n.getRoad(v.getCell().getRoadName()).isTrafficLightRed()) {
								v.stayHere();
								continue;
							}
						}
						if (v.getCell().getOutCell().getVehicle() == null) {
							if (v.getCell().getOutCell().getPreviousCell() != null) {
								// if there is no vehicle in the road where Vehicle must go
								if (v.getCell().getOutCell().getPreviousCell().getVehicle() == null) {
									if (v.getCell().getOutCell().getPreviousCell().getPreviousCell() != null) {
										// if there is no vehicle in the road where Vehicle must go, a cell before
										if (v.getCell().getOutCell().getPreviousCell().getPreviousCell().getVehicle() == null) {
											v.getRide().getNextConnections().remove(0);
											v.goToOutCell();
										} else {
											v.stayHere();
										}
									}
								} else {
									v.stayHere();
								}
							} else {
								if (!v.getRide().getNextConnections().isEmpty()) {
									v.getRide().getNextConnections().remove(0);
								}
								v.goToOutCell();
							}
							
						} else {
							v.stayHere();
						}
					}
					// no NEXT or OUT cell
					else if (v.getCell().getNextCell() == null && v.getCell().getOutCell() == null) {
						v.leaveNetwork();
					}
					// should never happen ...
					else {
						v.stayHere();
					}
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
		} */
	}
	// ### OLD ### Update Cell of the Road according to the next state
	public static void evolveOLD(Network n) {
		for (CrossRoad cr: n.getCrossRoads()) {
			for (int i=0; i < 4; ++i) {
				cr.getMiddleCells()[i].evolve();
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
	
	// ### OLD ### Compute future state of the Cells of the Road
	public static void computeEvolutionOld(Network n) {
		// Crossroads -----------------------------------------------------------------------------
		for (CrossRoad cr: n.getCrossRoads()) {
			if (!cr.getRoadsIN()[(cr.getStateOfTrafficLight()+1)%4].isTrafficLightRed()) {
				cr.setTrafficLightState(cr.getStateOfTrafficLight()%4);
			}
			for (int i=0; i < 4; ++i) {
				if (cr.getMiddleCells()[i].getOutCell() == null) {
					if (cr.getMiddleCells()[i].isOccupied()) {
						if (cr.getMiddleCells()[i].getNextCell().isOccupied()) {
							cr.getMiddleCells()[i].setIsOccupiedNext(1);
						} else {
							cr.getMiddleCells()[i].setIsOccupiedNext(0);
							cr.getMiddleCells()[i].getNextCell().setIsOccupiedNext(1);
						}
					} else {
						if (cr.getMiddleCells()[i].getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							cr.getMiddleCells()[i].setIsOccupiedNext(0);
						}
					}
				} else {
					if (cr.getMiddleCells()[i].isOccupied()) {
						if ((int)(Math.random()*2)==1) {
							if (cr.getMiddleCells()[i].getOutCell().isOccupied()) {
								cr.getMiddleCells()[i].setIsOccupiedNext(1);
							} else {
								cr.getMiddleCells()[i].setIsOccupiedNext(0);
								cr.getMiddleCells()[i].getOutCell().setIsOccupiedNext(1);
							}
						} else {
							if (cr.getMiddleCells()[i].getNextCell().isOccupied()) {
								cr.getMiddleCells()[i].setIsOccupiedNext(1);	
							} else {
								cr.getMiddleCells()[i].setIsOccupiedNext(0);
								cr.getMiddleCells()[i].getNextCell().setIsOccupiedNext(1);	
							}
						}
						
					} else {
						if (cr.getMiddleCells()[i].getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							cr.getMiddleCells()[i].setIsOccupiedNext(0);
						}
					}
				}
			}
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
		// Roads ----------------------------------------------------------------------------------
		for (Road r: n.getRoads()) {
			for (int i=0; i < r.getLength(); ++i) {
				
				if (r.getRoadCells().get(i).isOccupied()) {
					//System.out.print("1");
				} else {
					//System.out.print("0");
				}
				if (i < r.getLength()-1) {
					if (r.getRoadCells().get(i).isOccupied()) {
						if (r.getRoadCells().get(i).getNextCell().isOccupied()) {
							r.getRoadCells().get(i).setIsOccupiedNext(1);
						} else {
							if (r.getRoadCells().get(i).getOutCell() != null && Math.random()<0.5) {
								r.getRoadCells().get(i).setIsOccupiedNext(0);
								r.getRoadCells().get(i).getOutCell().setIsOccupiedNext(1);
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
				// If Cell is at the end of the road
				else {
					if (r.getRoadCells().get(i).getOutCell() != null) {
						if (r.getRoadCells().get(i).isOccupied()) {
							if (r.getRoadCells().get(i).getOutCell().isOccupied() || r.getRoadCells().get(i).getOutCell().getPreviousCell().isOccupied() || r.isTrafficLightRed()) {
								r.getRoadCells().get(i).setIsOccupiedNext(1);
							} else {
								r.getRoadCells().get(i).setIsOccupiedNext(0);
								r.getRoadCells().get(i).getOutCell().setIsOccupiedNext(1);
							}
						}
					}
					// If Cell has not been visited
					if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
						// Cell stay inoccupied
						r.getRoadCells().get(i).setIsOccupiedNext(0);;
					}
				}
				// Out cells
				if (r.getRoadCells().get(i).getOutCell() != null) {
					if (Math.random()<0.5) {
						
					}
				}
			}
			// Random generation
			if (r.getRoadCells().get(0).getIsOccupiedNext() != 1 && r.getGenerateVehicules()) {
				if (Math.random()<0.1) {
					r.getRoadCells().get(0).setIsOccupiedNext(1);
				}
			}
			//System.out.print("\n");
		}
		// Roundabouts ----------------------------------------------------------------------------
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
