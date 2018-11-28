package elements;

import java.util.ArrayList;

public class Cell {
	
	// Simulation
	private Cell inCell;
	private Cell previousCell;
	private Cell nextCell;
	private Cell outCell;
	private ArrayList<Cell> overlapedCells = new ArrayList<Cell>();
	private Vehicle vehicle;
	private String roadName;
	private int roadLength;
	private int maxSpeed = 1;
	private int position;
	private boolean isInRoundAbout;
	private boolean isBlocked;
	
	// Display
	private double x,y; // center of the Cell
	private boolean isUnderground = false;
	
	public Cell(String name) {
		this.roadName = name;
		inCell = null;
		previousCell = null;
		nextCell = null;
		outCell = null;
		vehicle = null;
	}
	
	public int checkNextCells(int nCells, int i) {
		if (nCells <= 0) {
			return i;
		}
		if (this.getNextCell().getVehicle() == null) {
			i = 10*i + 0;
		} else {
			i = 10*i + 1;
		}
		return this.getNextCell().checkNextCells(nCells-1, i) ;
	}
	
	public boolean isAnOverlapedCellOccupied() {
		for (Cell c: this.overlapedCells) {
			if (c.getVehicle() != null) {
				return true;
			}
		}
		return false;
	}

	// Getters & setters ------------------------------------------------------------------------------------
	public boolean isBlocked() {
		return isBlocked;
	}
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
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
	public Cell getInCell() {
		return inCell;
	}
	public void setInCell(Cell inCell) {
		this.inCell = inCell;
	}
	public int getRoadLength() {
		return roadLength;
	}
	public void setRoadLength(int roadLength) {
		this.roadLength = roadLength;
	}
	public boolean isInRoundAbout() {
		return isInRoundAbout;
	}
	public void setInRoundAbout(boolean isInRoundAbout) {
		this.isInRoundAbout = isInRoundAbout;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public ArrayList<Cell> getOverlapedCells() {
		return overlapedCells;
	}
}