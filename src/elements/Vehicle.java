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
	private boolean inBucket = true;
	
	public Vehicle(Network n) {
		this.n = n;
		id = idCounter;
		idCounter++;
		cell = null;
		nextPlace = null;
	}
	
	public void evolve() {
		if (!hasToLeave) {
			if (cell != null) {
				cell.setVehicle(null);
			}
			if (nextPlace == null && !inBucket) {
				System.out.println("Err : nextPlace is null for this Vehicle (id:" + id);
			}
			if (!inBucket) {
				cell = nextPlace;
				cell.setVehicle(this);
				nextPlace = null;
			}
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
		if (tmp.getVehicle() != null) {
			return false;
		}
		for (int j=1; j < nCells+1; ++j) {
			tmp.getPreviousCell();
			if (tmp.getVehicle() != null) {
				if (tmp.getVehicle().getSpeed() == 0) {
					return true;
				} else if (tmp.getVehicle().getSpeed() >= j-1) {
					return false;
				}
			}
			tmp = tmp.getPreviousCell();
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
	}
	public void goToOutCell( ) {
		nextPlace = cell.getOutCell();
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
	public void removeCurrentConnection() {
		if (!this.getRide().getNextConnections().isEmpty()) {
			this.getRide().getNextConnections().remove(0);
		}
	}
	public void goToXthNextCell(int x) {
		Cell ci = this.getCell();
		for (int iter=0; iter < x; ++iter) {
			if (ci.getNextCell() != null) {
				if (ci.getNextCell().getVehicle() != null) {
					this.setSpeed(iter);
					break;
				}
				ci = ci.getNextCell();
			} else {
				this.setSpeed(iter);
				break;
			}
		}
		this.setNextPlace(ci);
	}
	public int distanceFromNextConnection() {
		int i = -1;
		if (this.getCell() != null && !this.getRide().getNextConnections().isEmpty()) {
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
		if (this.getCell() != null) {
			if (this.getRide() != null && !this.getRide().getNextConnections().isEmpty()) {
				if (this.getCell().getPosition() == this.getRide().getNextConnections().get(0).getPosition()) {
					if (this.getCell().getOutCell() != null && this.getCell().getOutCell().getVehicle() != null) {
						this.setSpeed(0);
					}
				}
			}
		}
		
	}
	
	// Getters & setters ====================================================================================
	public boolean inBucket() {
		return inBucket;
	}
	public void setInBucket(boolean inBucket) {
		this.inBucket = inBucket;
	}
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
	}
	public Cell getNextPlace() {
		return this.nextPlace;
	}
	public void setNextPlace(Cell nextPlace) {
		this.nextPlace = nextPlace;
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
