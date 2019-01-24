package elements;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumSet;

import network.Network;

public class Road {

	private static int idCounter = 1; // Roads ID counter
	protected Network n;
	
	// Simulation
	private String name;
	private ArrayList<Connection> exits = new ArrayList<Connection>();
	private ArrayList<Connection> enters = new ArrayList<Connection>();
	private int id; // Roads ID
	private int length;
	private int maxSpeed;
	private ArrayList<Cell> roadCells = new ArrayList<Cell>();
	private ArrayList<Float> flow = new ArrayList<Float>();
	private boolean isTrafficLightRed = false;
	private EnumSet<Direction> directions;
	private ArrayList<Point> reorientations = new ArrayList<Point>();
	private int maxOutflow = 0; // maximum outflow, in seconds between 2 vehicles
	private int outflowCounter = 0; // outflow counter
	private boolean useSingleOutflow = true; // false if using MaxVehicleOutflow for multiple roads
	private boolean isBlocked = false;
	private boolean startOutflowCount = false;
	private VehicleCounter vehicleCounter = null;
	private ArrayList<Vehicle> leakyBucket = new ArrayList<Vehicle>();
	
	// Display
	private double x,y; // position in pixels from left upper corner
	private int direction; // from 0 to 360 (north), 90 (east), 180 (south), 270 (west)
	
	public Road(Network n, int length) {
		this.n = n;
		this.length = length;
		initFields();
	}
	public Road(Network n, int length, String name) {
		this.n = n;
		this.length = length;
		this.name = name;
		initFields();
	}
	public void initFields() {
		this.maxSpeed = n.getMaxSpeed();
		id = idCounter;
		idCounter++;
		for (int i=0; i<length; i++) {
			
			Cell tmp = new Cell(name);
			
			if (i>0) {
				tmp.setPreviousCell(roadCells.get(i-1));
				roadCells.get(i-1).setNextCell(tmp);
			}
			
			tmp.setPosition(i);
			tmp.setRoadLength(length);
			tmp.setMaxSpeed(this.getMaxSpeed());
			tmp.setInRoundAbout(false);
			
			roadCells.add(tmp);
		}
		for (int i=0; i<24 ; i++) {
			flow.add(0f);
		}
	}
	public void outflowTick() {
		
		if (useSingleOutflow) {
		
			if (startOutflowCount) {
				if (outflowCounter <= 0) {
					outflowCounter = maxOutflow;
					isBlocked = false;
					startOutflowCount = false;
				} else {
					outflowCounter--;
					isBlocked = true;
				}
			} else {
				isBlocked = false;
			}
			
			if (roadCells.get(roadCells.size()-1).getVehicle() != null) {
				if (!startOutflowCount) {
					startOutflowCount = true;
				}
			}
			
			roadCells.get(roadCells.size()-1).setBlocked(isBlocked);
		}
	}
	public void setCounter(double location, String name) {
		vehicleCounter = new VehicleCounter(n, this, location, name);
	}
	public void setUnderground(int i, int j, boolean isUnderground) {
		for (int k=i ; k<=j ; k++) {
			this.getRoadCells().get(k).setUnderground(isUnderground);
		}
	}
	public static void resetID() {
		idCounter = 1;
	}
	public void addPoint(Point point) {
		reorientations.add(point);
	}
	// Set position and direction from a RoundAbout cell (out road)
	public void setStartPositionFrom(RoundAbout ra, int i) {
		if (this.getReorientations().size() == 0) {
			setDirection((int) (ra.getDirection() - i/(float)(ra.getLength()) * 360));
		}
		this.setX((int) (ra.getX() + (ra.getLength()*n.getCellWidth()/(2*Math.PI) + n.getCellHeight()/2) * Math.sin(2*Math.PI*this.getDirection()/360.0)));
		this.setY((int) (ra.getY() - (ra.getLength()*n.getCellWidth()/(2*Math.PI) + n.getCellHeight()/2) * Math.cos(2*Math.PI*this.getDirection()/360.0)));
	}
	// Set position and direction from a Road cell (out road)
	public void setStartPositionFrom(Road r, int i, int direction) {
		if (this.getReorientations().size() == 0) {
			setDirection(direction);
		}
		this.setX((int) (1*n.getCellWidth()/2 * Math.sin(2*Math.PI*this.getDirection()/360.0) + r.getX() + (i*n.getCellWidth() + n.getCellHeight()/2) * Math.sin(2*Math.PI*r.getDirection()/360.0)));
		this.setY((int) (-1*n.getCellWidth()/2 * Math.cos(2*Math.PI*this.getDirection()/360.0) + r.getY() - (i*n.getCellWidth() + n.getCellHeight()/2) * Math.cos(2*Math.PI*r.getDirection()/360.0)));
	}
	public void setStartPositionFrom(Road r, int i, int direction, double offsetNumberOfCells, double offsetDirection) {
		if (this.getReorientations().size() == 0) {
			setDirection(direction);
		}
		
		double x = (int) (r.getX());
		double y = (int) (r.getY());
		double last = 0;
		
		for (int j=0 ; j<r.getReorientations().size() ; j++) {
			// Last segment
			if (j == r.getReorientations().size()-1) {
				
				if (r.getReorientations().get(r.getReorientations().size()-1).x < i) {
				
					x += n.getCellWidth() * (i-r.getReorientations().get(j).getX()) * Math.sin(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
					y += - n.getCellWidth() * (i-r.getReorientations().get(j).getX()) * Math.cos(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
				}
			}
			// All others
			else {
				
				if (r.getReorientations().get(j+1).getX() >= i) {
					x += n.getCellWidth() * (i-last) * Math.sin(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
					y += - n.getCellWidth() * (i-last) * Math.cos(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
					last = r.getReorientations().get(j+1).getX();
				} else {
					x += n.getCellWidth() * (r.getReorientations().get(j+1).getX()-last) * Math.sin(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
					y += - n.getCellWidth() * (r.getReorientations().get(j+1).getX()-last) * Math.cos(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
					last = r.getReorientations().get(j+1).getX();
				}
			}
		}
		
		x += offsetNumberOfCells*n.getCellWidth()*Math.sin(2*Math.PI*offsetDirection/360.0);
		y -= offsetNumberOfCells*n.getCellWidth()*Math.cos(2*Math.PI*offsetDirection/360.0);
		
		this.setX(x);
		this.setY(y);
		
	}
	// Set position and direction from a RoundAbout cell (in road)
	public void setEndPositionFrom(RoundAbout ra, int i, int direction) {
		if (this.getReorientations().size()==0) {
			setDirection(direction);
		} else {
			
		}
		double x = (int) (ra.getX() - (ra.getLength()*n.getCellWidth()/(2*Math.PI))*Math.sin(2*Math.PI*(i/(float)ra.getLength())));
		double y = (int) (ra.getY() - (ra.getLength()*n.getCellWidth()/(2*Math.PI))*Math.cos(2*Math.PI*(i/(float)ra.getLength())));
		double last = 0;
		for (int j=0 ; j<this.getReorientations().size() ; j++) {
			// Last segment
			if (j == this.getReorientations().size()-1) {
				x += n.getCellWidth() * (this.getLength()+0.5-this.getReorientations().get(j).getX()) * Math.sin(2*Math.PI*this.getReorientations().get(j).getY()/360.0 + Math.PI);
				y += - n.getCellWidth() * (this.getLength()+0.5-this.getReorientations().get(j).getX()) * Math.cos(2*Math.PI*this.getReorientations().get(j).getY()/360.0 + Math.PI);
			}
			// All others
			else {
				x += n.getCellWidth() * (this.getReorientations().get(j+1).getX()-last) * Math.sin(2*Math.PI*this.getReorientations().get(j).getY()/360.0 + Math.PI);
				y += - n.getCellWidth() * (this.getReorientations().get(j+1).getX()-last) * Math.cos(2*Math.PI*this.getReorientations().get(j).getY()/360.0 + Math.PI);
				last = this.getReorientations().get(j+1).getX();
			}
		}
		this.setX(x);
		this.setY(y);
	}
	// DEPRECATED ###
	// Set position and direction from a RoundAbout cell (in road)
	public void setEndPositionFrom(RoundAbout ra, int i) {
		this.direction = (int) (ra.getDirection() - i/(float)(ra.getLength()) * 360 + 180);
		this.setX((int) (ra.getX() + (ra.getLength()*n.getCellWidth()/(2*Math.PI) + n.getCellHeight()/2 + length*n.getCellWidth()) * Math.sin(Math.PI + 2*Math.PI*this.getDirection()/360.0) ));
		this.setY((int) (ra.getY() - (ra.getLength()*n.getCellWidth()/(2*Math.PI) + n.getCellHeight()/2 + length*n.getCellWidth()) * Math.cos(Math.PI + 2*Math.PI*this.getDirection()/360.0) ));
	}
	// ##############
	public void setPositionInFrom(CrossRoad CR, int i) {
		i = i % 4;
		//this.direction = (int) ((CR.getDirection()+((3-i)%4)*90)%360);
		setDirection((int) ((CR.getDirection()+((3-i)%4)*90)%360));
		//System.out.println(this.getReorientations().size());
		//System.out.println(this.getDirection());
		double valInter1 = n.getCellHeight()/2;
		double valInter2 = n.getCellWidth()*this.getLength()+n.getCellHeight();
		double angle =  Math.atan(valInter1/valInter2);
		angle = Math.toDegrees(angle);
		angle = this.getDirection()+180-angle;
		double posX = (CR.getX() + (Math.sqrt( Math.pow(n.getCellWidth()*this.getLength()+n.getCellHeight(), 2) + Math.pow(n.getCellHeight(), 2)/4 ) * Math.sin(2*Math.PI*(angle/360.0)) ));
		this.setX((int) posX);
		double posY = (CR.getY() - (Math.sqrt( Math.pow(n.getCellWidth()*this.getLength()+n.getCellHeight(), 2) + Math.pow(n.getCellHeight(), 2)/4 ) * Math.cos(2*Math.PI*(angle/360.0)) ));
		this.setY((int) posY);
		CR.addRoadIn(this, i);
	}
	public void setPositionOutFrom(CrossRoad CR, int i) {
		i = i % 4;
		//this.direction = (int) ((CR.getDirection()-(i % 4)*90)%360);
		setDirection((int) ((CR.getDirection()-(i % 4)*90)%360));
		double angle =  Math.atan(2);
		angle = Math.toDegrees(angle);
		this.setX((int) (CR.getX() + (n.getCellHeight()/2 * Math.sqrt(5.0) * Math.sin(2*Math.PI*((this.getDirection()+90-angle)%360)/360))));
		this.setY((int) (CR.getY() - (n.getCellHeight()/2 * Math.sqrt(5.0) * Math.cos(2*Math.PI*((this.getDirection()+90-angle)%360)/360))));
		CR.addRoadOut(this, i);
	}
	
	// Connect "pointer" of last Cell to Cell i of RoundAbout
	public void connectTo(Road r, int i) {
		this.getRoadCells().get(this.getLength()-1).setOutCell(r.getRoadCells().get(i));
		r.getRoadCells().get(i).setInCell(this.getRoadCells().get(this.getLength()-1));
		this.addExit(r.getName(), this.getLength()-1);
		r.addEnter(this.getName(), i);
	}
	public void connectFromiTo(Road r, int i) {
		this.getRoadCells().get(i).setOutCell(r.getRoadCells().get(0));
		r.getRoadCells().get(0).setInCell(this.getRoadCells().get(i));
		this.addExit(r.getName(), i);
		r.addEnter(this.getName(), 0);
	}
	public void connectFromiToj(Road r, int i, int j) {
		this.getRoadCells().get(i).setOutCell(r.getRoadCells().get(j));
		r.getRoadCells().get(j).setInCell(this.getRoadCells().get(i));
		this.addExit(r.getName(), i);
		r.addEnter(this.getName(), j);
	}
	
	public void connectTo(RoundAbout ra, int i) {
		this.getRoadCells().get(this.getLength()-1).setOutCell(ra.getRoadCells().get(i));
		ra.getRoadCells().get(i).setInCell(this.getRoadCells().get(this.getLength()-1));
		this.addExit(ra.getName(), this.getLength()-1);
		ra.addEnter(this.getName(), i);
	}
	public void connectTo(CrossRoad CR, int i) {
		i = i % 4;
		this.getRoadCells().get(this.getLength()-1).setOutCell(CR.getMiddleCells()[i]);
		CR.getMiddleCells()[i].setInCell(this.getRoadCells().get(this.getLength()-1));
		this.addExit(CR.getName(), this.getLength()-1);
		CR.addEnter(this.getName(), i);
	}
	public void connectTo(MultiLaneRoundAbout MLRA, int i) {
		int raSize = MLRA.getLanes()[0].getLength();
		i = ((i % raSize)+raSize)%raSize;
		//int i1 = i;
		//int i2 = i+1;
		
		this.getRoadCells().get(this.getLength()-1).setOutCell(MLRA.getLanes()[0].getRoadCells().get(i));
		MLRA.getLanes()[0].getRoadCells().get(i).setInCell(this.getRoadCells().get(this.getLength()-1));
		this.addExit(MLRA.getName(), this.getLength()-1);
		MLRA.addEnter(this.getName(), i);
		
		for (int j=1; j<MLRA.getLanes().length; ++j) {
			//i1 = ((i1 % raSize)+raSize)%raSize;
			//i2 = ((i2 % raSize)+raSize)%raSize;
			MLRA.getLanes()[j-1].getRoadCells().get(i).setOutCell(MLRA.getLanes()[j].getRoadCells().get(i));
			MLRA.getLanes()[j].getRoadCells().get(i).setInCell(MLRA.getLanes()[j-1].getRoadCells().get(i));
			//i1 = i2;
			//i2 = i2+1;
		}
	}
	
	public void removeLastGoInGoOutConnections(Ride ride) {

		while (ride.getNextConnections().get(ride.getNextConnections().size()-1).getName().equals("GO IN") || ride.getNextConnections().get(ride.getNextConnections().size()-1).getName().equals("GO OUT")) {
			ride.getNextConnections().remove(ride.getNextConnections().size()-1);
		}
	}
	
	public void generateRides(int n) {
		this.generateRidesAux(n, new Ride(this.getName()));
	}
	
	public void generateRidesAux(int n, Ride ride) {
		if (!ride.getNextConnections().isEmpty()) {
			this.removeLastGoInGoOutConnections(ride);
		}
		if(n==0) {
			if (this.getRoadCells().get(this.getLength()-1).getNextCell() == null && this.getRoadCells().get(this.getLength()-1).getOutCell() == null) {
				this.n.addARideToAllNetworkRides(ride.clone());
			}
			ride.removeLastConnection();
			return;
		} else if (n > 0) {
			for (Connection e: this.exits) {
				boolean canAdd = true;
				for (Road r: this.n.getRoads()) {
					if (e.getName() != null) {
						if (e.getName().equals(r.getName())) {
							if (!ride.getNextConnections().isEmpty()) {
								for (Connection ent: this.getEnters()) {
									
									if(ride.getNextConnections().size() > 1 && ent.getName().equals(ride.getNextConnections().get(ride.getNextConnections().size()-2).getName())) {
										if (ent.getPosition() > e.getPosition()) {
											canAdd = false;
										}
									} else if (ride.getNextConnections().size() == 1) {
										if (ent.getName().equals(ride.getRoadName())) {
											if (ent.getPosition() >= e.getPosition()) {
												canAdd = false;
											}
										}
									}
								}
							}
							if (canAdd) {
								ride.addNextConnection(e.clone());
								r.generateRidesAux(n-1, ride);
							}
							canAdd = true;
							
						}
					}
				}
				for (MultiLaneRoundAbout MLRA: this.n.getMultiLaneRoundAbouts()) {
					if (e.getName() != null) {
						if (e.getName().equals(MLRA.getName())) {
							for(Connection c: ride.getNextConnections()) {
								if (c.getName().equals(MLRA.getName())) {
									if (!ride.getNextConnections().isEmpty()) {
										ride.removeLastConnection();
										this.removeLastGoInGoOutConnections(ride);
									}
									return;
								}
							}
							Connection etilde = e.clone();
							//etilde.setName(MLRA.getLanes()[0].getName());
							ride.addNextConnection(etilde);
							MLRA.generateRidesAux(n-1, ride);
						}
					}
				}
				for (RoundAbout ra: this.n.getRoundAbouts()) {
					if (e.getName() != null) {
						if (e.getName().equals(ra.getName())) {
							for(Connection c: ride.getNextConnections()) {
								if (c.getName().equals(ra.getName())) {
									if (!ride.getNextConnections().isEmpty()) {
										ride.removeLastConnection();
										this.removeLastGoInGoOutConnections(ride);
									}
									return;
								}
							}
							
							ride.addNextConnection(e.clone());
							ra.generateRidesAux(n-1, ride);
						}
					}
				}
				for (CrossRoad cr: this.n.getCrossRoads()) {
					if (e.getName().equals(cr.getName())) {
						for(Connection c: ride.getNextConnections()) {
							if (c.getName().equals(cr.getName())) {
								if (!ride.getNextConnections().isEmpty()) {
									ride.removeLastConnection();
									this.removeLastGoInGoOutConnections(ride);
								}
								return;
							}
						}
						
						ride.addNextConnection(e.clone());
						cr.generateRidesAux(n-1, ride);
					}
				}	
			}
			if (this.getRoadCells().get(this.getLength()-1).getNextCell() == null && this.getRoadCells().get(this.getLength()-1).getOutCell() == null) {
				this.n.addARideToAllNetworkRides(ride.clone());
			}
			if (!ride.getNextConnections().isEmpty()) {
				ride.removeLastConnection();
			}
			return;
		} else {
			if (!ride.getNextConnections().isEmpty()) {
				ride.removeLastConnection();
			}
			return;
		}
		
	}
	// get number of vehicle on the last cells of road (number of cells is "size" field)
	public int getNumberOfVehiclesAtEnd(int size) {
		int n = 0;
		for (int i=roadCells.size()-1 ; i>=roadCells.size()-1-size ; i--) {
			if (roadCells.get(i).getVehicle() != null) {
				n++;
			}
		}
		return n;
	}
	public int getNumberOfVehicles(int speed) {
		int n = 0;
		for (Cell cell: roadCells) {
			if (cell.getVehicle() != null) {
				if (cell.getVehicle().getSpeed() == speed) {
					n++;
				}
			}
		}
		return n;
	}
	public void addRoadDirection(Direction d) {
		this.directions.add(d);
	}
	public void removeRoadDirection(Direction d) {
		this.directions.remove(d);
	}
	public void removeAllRoadDirections() {
		this.directions.clear();
	}
	public void addExit(String name, int position) {
		exits.add(new Connection(name, position));
	}
	public void addEnter(String name, int position) {
		enters.add(new Connection(name, position));
	}
	
	// Getters & setters ====================================================================================
	public ArrayList<Point> getReorientations() {
		return this.reorientations;
	}
	public ArrayList<Vehicle> getLeakyBucket() {
		return leakyBucket;
	}
	public void setLeakyBucket(ArrayList<Vehicle> leakyBucket) {
		this.leakyBucket = leakyBucket;
	}
	public void addNewVehicle(Vehicle v) {
		leakyBucket.add(v);
		v.setCurrentRoadName(name);
	}
	public void removeVehicleFromBucket(Vehicle v) {
		leakyBucket.remove(v);
	}
	public VehicleCounter getVehicleCounter() {
		return this.vehicleCounter;
	}
	public void setMaxOutflow(int maxOutflow) {
		this.maxOutflow = maxOutflow;
		outflowCounter = maxOutflow;
	}
	public boolean isBlocked() {
		return isBlocked;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<Float> getFlow() {
		return flow;
	}
	public void setGenerateVehicules(int hourStart, int hourEnd, float value) {
		for (int i=hourStart ; i<hourEnd ; i++) {
			flow.set(i, value);
		}
	}
	public void setGenerateVehicules(float value) {
		for (int i=0 ; i<flow.size() ; i++) {
			flow.set(i, value);
		}
		/*System.out.print(name + " : ");
		for (Integer i: flow) {
			System.out.print(i + " ");
		}
		System.out.println();*/
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
		this.reorientations.add(new Point(0,direction));
	}
	public void setStartDirection(int direction) {
		if (this.getReorientations().size() > 0) {
			this.direction = direction;
			this.reorientations.set(0, new Point (0, direction));
		}
	}
	public int getLength() {
		return length;
	}
	public ArrayList<Cell> getRoadCells() {
		return roadCells;
	}
	public void setRoadCells(ArrayList<Cell> roadCells) {
		this.roadCells = roadCells;
	}
	public boolean isTrafficLightRed() {
		return isTrafficLightRed;
	}
	public void setTrafficLightRed(boolean isTrafficLightRed) {
		this.isTrafficLightRed = isTrafficLightRed;
	}
	public EnumSet<Direction> getDirections() {
		return directions;
	}
	public void setDirections(EnumSet<Direction> directions) {
		this.directions = directions;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Connection> getExits() {
		return exits;
	}
	public ArrayList<Connection> getEnters() {
		return enters;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
		for (Cell c: this.getRoadCells()) {
			c.setMaxSpeed(maxSpeed);
		}
	}
	public void useSingleOutflow(boolean useSingleOutflow) {
		this.useSingleOutflow = useSingleOutflow;
	}
	public void setIsBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
}
