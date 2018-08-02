package elements;

public class Cell {
	
	// Simulation
	private boolean isOccupied;
	private int isOccupiedNext;
	private Cell previousCell;
	private Cell nextCell;
	private Cell outCell;
	private Vehicle vehicle;
	private String roadName;
	private int position;
	private int typeOfRoad;
	
	// Display
	private double x,y; // center of the Cell
	private boolean isUnderground = false;
	
	public Cell() {
		isOccupied = false;
		isOccupiedNext = -1;
		previousCell = null;
		nextCell = null;
		outCell = null;
		vehicle = null;
	}
	
	public Cell(String name) {
		this.roadName = name;
		isOccupied = false;
		isOccupiedNext = -1;
		previousCell = null;
		nextCell = null;
		outCell = null;
		vehicle = null;
	}

	public void evolve() {
		if (isOccupiedNext == 0) {
			isOccupied = false;
		} else if (isOccupiedNext == 1) {
			isOccupied = true;
		} else if (isOccupiedNext == -1) {
			System.out.println("Error - Unupdated Cell ========");
		} else {
			System.out.println("Error - Invalid value (isOccupiedNext) ========");
		}
		
		isOccupiedNext = -1;
	}
	public void display() {
		if (isOccupied) {
			System.out.print("[X]");
		} else {
			System.out.print("[ ]");
		}
	}
	public boolean isUnderground() {
		return this.isUnderground;
	}
	public void setUnderground(boolean isUnderground) {
		this.isUnderground = isUnderground;
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
	public Cell getPreviousCell() {
		return previousCell;
	}
	public void setPreviousCell(Cell previousCell) {
		this.previousCell = previousCell;
	}
	public Cell getNextCell() {
		return nextCell;
	}
	public void setNextCell(Cell nextCell) {
		this.nextCell = nextCell;
	}
	public boolean isOccupied() {
		return isOccupied;
	}
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	public int getIsOccupiedNext() {
		return isOccupiedNext;
	}
	public void setIsOccupiedNext(int isOccupiedNext) {
		this.isOccupiedNext = isOccupiedNext;
	}
	public Cell getOutCell() {
		return outCell;
	}
	public void setOutCell(Cell outCell) {
		this.outCell = outCell;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getTypeOfRoad() {
		return typeOfRoad;
	}

	public void setTypeOfRoad(int typeOfRoad) {
		this.typeOfRoad = typeOfRoad;
	}
}
