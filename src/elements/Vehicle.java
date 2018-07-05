package elements;

public class Vehicle {

	private static int idCounter = 1;
	
	private int id;
	private Cell cell;
	
	public Vehicle() {
		id = idCounter;
		idCounter++;
		cell = null;
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
}
