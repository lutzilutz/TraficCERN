package elements;

import main.Network;

public class CrossRoad {
	private Network n;
	private Road[] roadsIN;
	private Road[] roadsOUT;
	private Cell[] middleCells;
	private int greenTrafficLight = 1;
	private int timeTrafficLight = 30;
	private int counter = 0;
	private int stateOfTrafficLight = 0;
	private int numberOfRoadsIn = 0;
	private int direction;
	private double x,y;

	public CrossRoad(Network n) {
		this.n = n;
		this.x = 0;
		this.y = 0;
		this.numberOfRoadsIn = 0;
		this.roadsIN = new Road[4];
		this.roadsOUT = new Road[4];
		middleCells = new Cell[4];
		middleCells[0] = new Cell();
		middleCells[1] = new Cell();
		middleCells[2] = new Cell();
		middleCells[3] = new Cell();
		
		for (int i=0; i<4; ++i) {
			middleCells[i].setNextCell(middleCells[(i+1)%4]);
		}
		for (int i=0; i<4; ++i) {
			middleCells[i].setPreviousCell(middleCells[(i+3)%4]);
		}
		
	}
	
	public void setAllTrafficLightsRed() {
		for (Road r: this.roadsIN) {
			r.setTrafficLightRed(true);
		}
	}
	
	public void setTrafficLightState(int i) {
		i = i % 4;
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
		i = i % 4;
		this.direction = (int) ((r.getDirection()-((3-i)%4)*90)%360);
		double valInter1 = n.getCellHeight()/2;
		double valInter2 = n.getCellWidth()*r.getLength()+n.getCellHeight();
		double angle =  Math.atan(valInter1/valInter2);
		angle = Math.toDegrees(angle);
		this.setX((int) (r.getX() + Math.sqrt(Math.pow(n.getCellWidth()*r.getLength()+n.getCellHeight(), 2) + Math.pow(n.getCellHeight()/2, 2) * Math.sin(2*Math.PI*(r.getDirection()+90+angle)/360))));
		this.setY((int) (r.getY() - Math.sqrt(Math.pow(n.getCellWidth()*r.getLength()+n.getCellHeight(), 2) + Math.pow(n.getCellHeight()/2, 2) * Math.cos(2*Math.PI*(r.getDirection()+90+angle)/360))));
		if (this.roadsIN == null) {
			++ this.numberOfRoadsIn;
		}
		this.roadsIN[i] = r;
	}
	
	public void setPositionFromOut(Road r, int i) {
		i = i % 4;
		this.direction = (int) ((r.getDirection()+(i % 4)*90)%360);
		double angle = Math.atan(1/2);
		angle = Math.toDegrees(angle);
		this.setX((int) (r.getX() + (n.getCellHeight()/2 * Math.sqrt(5.0) * Math.sin(2*Math.PI*((r.getDirection()+angle)%360)/360))));
		this.setY((int) (r.getY() - (n.getCellHeight()/2 * Math.sqrt(5.0) * Math.sin(2*Math.PI*((r.getDirection()+angle)%360)/360))));
		this.roadsOUT[i] = r;
	}
	
	public void connectTo(Road r, int i) {
		i = i % 4;
		this.middleCells[i].setOutCell(r.getRoadCells().get(0));
		r.getRoadCells().get(0).setPreviousCell(this.middleCells[i]);
		this.roadsOUT[i] = r;
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
	
	// Getters and setters ================================================================
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
}
