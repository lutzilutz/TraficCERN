package elements;

import main.Network;

public class CrossRoad {
	private Road[] roadsIN;
	private Road[] roadsOUT;
	private Cell[] middleCells;
	private int greenTrafficLight = 1;
	private int timeTrafficLight = 8;
	private int numberOfRoadsIn;
	private int direction;
	private int x,y;

	public CrossRoad(Network n, int x, int y, int dir) {
		this.numberOfRoadsIn = 0;
		this.direction = dir % 360;
		this.x = x;
		this.y = y;
		middleCells = new Cell[4];
		middleCells[0] = new Cell();
		middleCells[1] = new Cell();
		middleCells[2] = new Cell();
		middleCells[3] = new Cell();
		
		for (int i=0; i<4; ++i) {
			middleCells[i].setNextCell(middleCells[(i+1)%4]);
			middleCells[i].setPreviousCell(middleCells[(i-1)%4]);
		}
		
	}
	
	public void setPositionInFrom(Road r) {
		
	}
	
	public void connectTo(Road r, int i) {
		i = i % 4;
		this.middleCells[i].setOutCell(r.getRoadCells().get(0));
		r.getRoadCells().get(0).setPreviousCell(this.middleCells[i]);
		this.roadsOUT[i] = r;
	}
	
	// Getters and setters:
	
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

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	

}
