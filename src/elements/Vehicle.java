package elements;

import network.Network;

public class Vehicle {

	private static int idCounter = 1;
	private Network n;
	
	private int id;
	private Cell cell;
	private Cell nextPlace;
	private int speed = 1;
	private boolean hasToLeave = false;
	private Ride ride;
	//private ArrayList<String> ride;
	
	public Vehicle(Network n) {
		this.n = n;
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
			if (nextPlace == null) {
				System.out.println("Err : nextPlace is null for this Vehicle");
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
		}
	}
	public int checkNextCells(int nCells) {
		int i = 0;
		i += this.getCell().checkNextCells(nCells, i);
		return i;
	}
	public boolean checkPreviousCells(int nCells, Cell cell) {
		Cell tmp = cell;
		for (int j=0; j < this.getSpeed()+1; ++j) {
			tmp.getPreviousCell();
			if (tmp.getVehicle() != null) {
				if (tmp.getVehicle().getSpeed()>=j) {
					return false;
				}
			}
		}
		return true;
	}
	public void leaveNetwork() {
		hasToLeave = true;
		nextPlace = null;
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
	public void decelerate() {
		--speed;
	}
	public void decelerate(int vit) {
		if (vit > 0) {
			this.speed = vit;
		} else {
			this.speed = 0;
		}
	}
	public int distanceFromNextConnection() {
		int i = -1;
		if (this.getCell() != null && this.getRide().getNextConnections() != null) {
			if (this.getCell() != null && this.getCell().isInRoundAbout()) {
				int l = this.getCell().getRoadLength();
				i = (((this.getRide().getNextConnections().get(0).getPosition() - this.getCell().getPosition()) % l )+ l) % l;
			} else {
				if (this.getRide().getNextConnections().size() > 0) {
					i = this.getRide().getNextConnections().get(0).getPosition() - this.getCell().getPosition();
				}
			}
		}
		return i;
	}
	public int distFromNextVehicle() {
		int i = -1;
		Cell tmp = this.getCell();
		if (tmp != null && tmp.isInRoundAbout()) {
			for (int j=1; j<tmp.getRoadLength(); ++j) {
				if (tmp.getNextCell().getVehicle() != null) {
					return j;
				}
				tmp = tmp.getNextCell();
			}
		} else if (tmp != null){
			for (int j=1; j<tmp.getRoadLength()-this.getCell().getPosition();++j) {
				if (tmp.getNextCell().getVehicle() != null) {
					return j;
				}
				tmp = tmp.getNextCell();
			}
		}
		return i;
	}
	public void nextSpeed() {
		int dc = this.distanceFromNextConnection();
		int dv = this.distFromNextVehicle();
		
		if (this.getCell() == null) {
			this.speed = Math.min(this.speed+1, this.n.getMaxSpeed());
		} else {
			this.speed = Math.min(this.speed+1, this.getCell().getMaxSpeed());
		}
		
		if (dv > 0) {
			this.speed = Math.min(this.speed, dv);
		}
		if (dc > 0) {
			this.speed = Math.min(this.speed, dc);
		}
		if (this.speed > 1) {
			if (Math.random() < 0.05) {
				this.speed = Math.max(this.speed-1, 0);
			}
		}
		
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
		//return cell;
		return this.nextPlace;
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
}
