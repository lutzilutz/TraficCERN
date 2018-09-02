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
	private boolean generateVehicules = false; // if generate Vehicles
	private boolean isTrafficLightRed = false;
	private EnumSet<Direction> directions;
	private ArrayList<Point> reorientations = new ArrayList<Point>();
	private int maxOutflow = 0; // maximum outflow, in seconds between 2 vehicles
	private int outflowCounter = 0; // outflow counter
	private boolean isBlocked = false;
	private boolean startOutflowCount = false;
	
	// Display
	private double x,y; // position in pixels from left upper corner
	private int direction; // from 0 to 360 (north), 90 (east), 180 (south), 270 (west)
	
	public Road(Network n, int length) {
		this.n = n;
		this.maxSpeed = n.getMaxSpeed();
		id = idCounter;
		idCounter++;
		this.length = length;
		for (int i=0; i<length; i++) {
			
			Cell tmp = new Cell();
			
			if (i>0) {
				tmp.setPreviousCell(roadCells.get(i-1));
				roadCells.get(i-1).setNextCell(tmp);
			}
			
			tmp.setPosition(i);
			tmp.setRoadLength(length);
			tmp.setMaxSpeed(this.maxSpeed);
			tmp.setInRoundAbout(false);
			
			roadCells.add(tmp);
		}
	}
	
	public Road(Network n, int length, String name) {
		this.n = n;
		this.maxSpeed = n.getMaxSpeed();
		id = idCounter;
		idCounter++;
		this.length = length;
		this.name = name;
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
	}
	public void outflowTick() {
		
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
	public void setUnderground(int i, int j, boolean isUnderground) {
		for (int k=i ; k<=j ; k++) {
			this.getRoadCells().get(k).setUnderground(isUnderground);
		}
	}
	public static void resetID() {
		idCounter = 1;
	}
	public ArrayList<Point> getReorientations() {
		return this.reorientations;
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
	public void connectTo(RoundAbout ra, int i) {
		this.getRoadCells().get(this.getLength()-1).setOutCell(ra.getRoadCells().get(i));
		this.addExit(ra.getName(), this.getLength()-1);
		ra.addEnter(this.getName(), i);
	}
	public void connectTo(CrossRoad CR, int i) {
		i = i % 4;
		this.getRoadCells().get(this.getLength()-1).setOutCell(CR.getMiddleCells()[i]);
		this.addExit(CR.getName(), this.getLength()-1);
		CR.addEnter(this.getName(), i);
	}
	
	public void generateRides(int n) {
		this.generateRidesAux(n, new Ride(this.getName()));
	}
	
	public void generateRidesAux(int n, Ride ride) {
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
					if (e.getName().equals(r.getName())) {
						if (!ride.getNextConnections().isEmpty()) {
							for (Connection ent: this.getEnters()) {
								
								if(ride.getNextConnections().size() > 1 && ent.getName().equals(ride.getNextConnections().get(ride.getNextConnections().size()-2).getName())) {
									if (ent.getPosition() >= e.getPosition()) {
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
				for (RoundAbout ra: this.n.getRoundAbouts()) {
					if (e.getName().equals(ra.getName())) {
						ride.addNextConnection(e.clone());
						ra.generateRidesAux(n-1, ride);
					}
				}
				for (CrossRoad cr: this.n.getCrossRoads()) {
					if (e.getName().equals(cr.getName())) {
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
	
	public void display() {
		for (Cell c: roadCells) {
			c.display();
		}
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
	public boolean getGenerateVehicules() {
		return this.generateVehicules;
	}
	public void setGenerateVehicules(boolean b) {
		generateVehicules = b;
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
}
