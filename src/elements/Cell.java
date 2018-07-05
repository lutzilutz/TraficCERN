package elements;

public class Cell {
	
	private boolean isOccupied;
	private int isOccupiedNext;
	
	public Cell() {
		isOccupied = false;
		isOccupiedNext = -1;
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
