package elements;

import java.util.ArrayList;
import java.util.Collections;

import network.Network;
import utils.SortByPos;

public class CrossRoad {
	private Network n;
	
	private String name;
	private Road[] roadsIN;
	private Road[] roadsOUT;
	private Cell[] middleCells;
	private ArrayList<Connection> exits = new ArrayList<Connection>();
	private ArrayList<Connection> enters = new ArrayList<Connection>();
	private int greenTrafficLight = 1;
	private int timeTrafficLight = 30;
	private int counter = 0;
	private int stateOfTrafficLight = 0;
	private int numberOfRoadsIn = 0;
	private int maxSpeed;
	private int direction;
	private double x,y;

	public CrossRoad(Network n) {
		this.n = n;
		
		initFields();
	}
	
	public CrossRoad(Network n, String name) {
		this.n = n;
		this.name = name;
		
		initFields();
	}
	public void initFields() {
		this.maxSpeed = 1;
		this.x = 0;
		this.y = 0;
		this.numberOfRoadsIn = 0;
		this.roadsIN = new Road[4];
		this.roadsOUT = new Road[4];
		middleCells = new Cell[4];
		middleCells[0] = new Cell(name);
		middleCells[0].setPosition(0);
		middleCells[0].setInRoundAbout(false);
		middleCells[0].setRoadLength(4);
		middleCells[0].setMaxSpeed(maxSpeed);

		middleCells[1] = new Cell(name);
		middleCells[1].setPosition(1);
		middleCells[1].setInRoundAbout(false);
		middleCells[1].setRoadLength(4);
		middleCells[1].setMaxSpeed(maxSpeed);

		middleCells[2] = new Cell(name);
		middleCells[2].setPosition(2);
		middleCells[2].setInRoundAbout(false);
		middleCells[2].setRoadLength(4);
		middleCells[2].setMaxSpeed(maxSpeed);

		middleCells[3] = new Cell(name);
		middleCells[3].setPosition(3);
		middleCells[3].setInRoundAbout(false);
		middleCells[3].setRoadLength(4);
		middleCells[3].setMaxSpeed(maxSpeed);

		for (int i=0; i<4; ++i) {
			middleCells[i].setNextCell(middleCells[(i+1)%4]);
		}
		for (int i=0; i<4; ++i) {
			middleCells[i].setPreviousCell(middleCells[(i+3)%4]);
		}
	}
	public void sortEnters() {
		Collections.sort(this.enters, new SortByPos());
	}
	
	public void sortExits() {
		Collections.sort(this.exits, new SortByPos());
	}
	
	public void setAllTrafficLightsRed() {
		for (Road r: this.roadsIN) {
			r.setTrafficLightRed(true);
		}
	}
	
	public void setTrafficLightState(int i) {
		i = ((i % 4) + 4) % 4;
		for (int j=0; j<4; ++j) {
			if (j==0) {
				this.roadsIN[(i+j)%4].setTrafficLightRed(false);
			} else {
				this.roadsIN[(i+j)%4].setTrafficLightRed(true);
			}
			this.middleCells[j].setNextCell(this.middleCells[(j+1)%4]);
		}
		this.middleCells[(i+2)%4].setNextCell(this.roadsOUT[(i+2)%4].getRoadCells().get(0));
	}
	
	public void setPositionFromIn(Road r, int i) {
		i = ((i % 4) + 4) % 4;
		this.direction = (int) ((r.getDirection()-((3-i)%4)*90)%360);
		double valInter1 = n.getCellHeight()/2;
		double valInter2 = n.getCellWidth()*r.getLength()+n.getCellHeight();
		double angle =  Math.atan(valInter1/valInter2);
		angle = Math.toDegrees(angle);
		this.setX((int) (r.getX() + Math.sqrt(Math.pow(n.getCellWidth()*r.getLength()+n.getCellHeight(), 2) + Math.pow(n.getCellHeight()/2, 2) * Math.sin(2*Math.PI*(r.getDirection()+90+angle)/360))));
		this.setY((int) (r.getY() - Math.sqrt(Math.pow(n.getCellWidth()*r.getLength()+n.getCellHeight(), 2) + Math.pow(n.getCellHeight()/2, 2) * Math.cos(2*Math.PI*(r.getDirection()+90+angle)/360))));
		addRoadIn(r, i);
	}
	
	public void setPositionFromOut(Road r, int i) {
		i = ((i % 4) + 4) % 4;
		this.direction = (int) ((r.getDirection()+(i % 4)*90)%360);
		this.setX((int) (r.getX() - (1*n.getCellHeight()/2 * Math.sin(2*Math.PI*r.getDirection()/360.0))));
		this.setY((int) (r.getY() + (5*n.getCellHeight()/2 * Math.cos(2*Math.PI*r.getDirection()/360.0))));
		addRoadOut(r, i);
	}
	
	public void connectTo(Road r, int i) {
		i = ((i % 4) + 4) % 4;
		this.middleCells[i].setOutCell(r.getRoadCells().get(0));
		r.getRoadCells().get(0).setInCell(this.middleCells[i]);
		addRoadOut(r, i);
		this.addExit(r.getName(), i);
		r.addEnter(this.getName(), 0);
	}
	
	public void addRoadIn(Road r, int i) {
		if (this.roadsIN == null) {
			++ this.numberOfRoadsIn;
		}
		this.roadsIN[i] = r;
	}
	
	public void addRoadOut(Road r, int i) {
		this.roadsOUT[i] = r;
	}
	
	public void addExit(String name, int position) {
		exits.add(new Connection(name, position));
	}	
	
	public void addEnter(String name, int position) {
		enters.add(new Connection(name, position));
	}
	
	public void generateRidesAux(int n, Ride ride) {
		if (n > 0) {
			for (Connection e: this.exits) {
				for (Road r: this.n.getRoads()) {
					if (e.getName().equals(r.getName())) {
						ride.addNextConnection(e.clone());
						r.generateRidesAux(n-1, ride);
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
			if (!ride.getNextConnections().isEmpty()) {
				ride.removeLastConnection();
			}
		} else {
			if (!ride.getNextConnections().isEmpty()) {
				ride.removeLastConnection();
			}
		}
		
	}
	
	// Getters & setters ====================================================================================
	public Road[] getRoadsIN() {
		return roadsIN;
	}
	public void setRoadsIN(Road[] roadsIN) {
		this.roadsIN = roadsIN;
	}
	public Road[] getRoadsOUT() {
		return roadsOUT;
	}
	public void setRoadsOUT(Road[] roadsOUT) {
		this.roadsOUT = roadsOUT;
	}
	public Cell[] getMiddleCells() {
		return middleCells;
	}
	public void setMiddleCells(Cell[] middleCells) {
		this.middleCells = middleCells;
	}
	public int getGreenTrafficLight() {
		return greenTrafficLight;
	}
	public void setGreenTrafficLight(int greenTrafficLight) {
		this.greenTrafficLight = greenTrafficLight;
	}
	public int getTimeTrafficLight() {
		return timeTrafficLight;
	}
	public void setTimeTrafficLight(int timeTrafficLight) {
		this.timeTrafficLight = timeTrafficLight;
	}
	public int getNumberOfRoadsIn() {
		return numberOfRoadsIn;
	}
	public void setNumberOfRoadsIn(int numberOfRoadsIn) {
		this.numberOfRoadsIn = numberOfRoadsIn;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
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
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public int getStateOfTrafficLight() {
		return stateOfTrafficLight;
	}
	public void setStateOfTrafficLight(int stateOfTrafficLight) {
		this.stateOfTrafficLight = stateOfTrafficLight;
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
		for (Cell c: this.getMiddleCells()) {
			c.setMaxSpeed(maxSpeed);
		}
	}
}
