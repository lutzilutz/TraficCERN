package elements;

import java.util.ArrayList;

public class Vehicle {

	private static int idCounter = 1;
	
	private int id;
	private Cell cell;
	private int speed = 1;
	private ArrayList<Integer> ride;
	
	public Vehicle() {
		id = idCounter;
		idCounter++;
		cell = null;
		ride = new ArrayList<Integer>();
	}
	
	//public void vehicleEvolution
	
	public void goToNextCell() {
		this.cell = this.cell.getNextCell();
	}
	
	public void goToOutCell( ) {
		this.cell = this.cell.getOutCell();
	}

	public void accelerate() {
		++speed;
	}
	
	public void decelerate( ) {
		--speed;
	}
	
	// Getters and setters ----------------------
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Cell getCell() {
		return cell;
	}
	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public ArrayList<Integer> getRide() {
		return ride;
	}
	
	
	
}
