package elements;

public class Vehicle {

	private static int idCounter = 1;
	
	private int id;
	private Cell cell;
	private Cell nextPlace;
	private int speed = 1;
	private boolean hasToLeave = false;
	private Ride ride;
	//private ArrayList<String> ride;
	
	public Vehicle() {
		id = idCounter;
		idCounter++;
		cell = null;
		nextPlace = null;
		//ride = new ArrayList<String>();
	}
	
	public void evolve() {
		if (!hasToLeave) {
			if (cell != null) {
				cell.setVehicle(null);
			}
			cell = nextPlace;
			cell.setVehicle(this);
			nextPlace = null;
		} else {
			cell.setVehicle(null);
			cell = null;
			if (nextPlace != null) {
				nextPlace.setVehicle(null);
			}
			nextPlace = null;
			System.out.println("have leaved !");
		}
	}
	public void leaveNetwork() {
		hasToLeave = true;
		nextPlace = null;
		System.out.println(id + " has to leave");
	}
	public void stayHere() {
		nextPlace = cell;
	}
	public void goToNextCell() {
		nextPlace = cell.getNextCell();
		//this.cell.setVehicle(null);
		//this.cell = this.cell.getNextCell();
		//this.cell.setVehicle(this);
	}
	public void goToOutCell( ) {
		nextPlace = cell.getOutCell();
		//this.cell = this.cell.getOutCell();
	}
	public void accelerate() {
		++speed;
	}
	public void decelerate( ) {
		--speed;
	}
	
	// Getters and setters ----------------------
	public boolean hasToLeave() {
		return hasToLeave;
	}
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
		//cell.setVehicle(this);
	}
	public Cell getNextPlace() {
		return cell;
	}
	public void setNextPlace(Cell nextPlace) {
		this.nextPlace = nextPlace;
		//cell.setVehicle(this);
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}
	
	/*
	public ArrayList<String> getRide() {
		return ride;
	}
	*/
	
	
}
