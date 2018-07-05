package elements;

public class Cell {
	
	// Simulation
	private boolean isOccupied;
	private int isOccupiedNext;
	private Cell nextCell;
	
	// Display
	private int x,y; // center of the Cell
	
	public Cell() {
		isOccupied = false;
		isOccupiedNext = -1;
		nextCell = null;
	}

	public void evolve() {
		if (isOccupiedNext == 0) {
			isOccupied = false;
		} else if (isOccupiedNext == 1) {
			isOccupied = true;
		} else if (isOccupiedNext == -1) {
			System.out.println("Error - Unupdated Cell ========");
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
	
}
