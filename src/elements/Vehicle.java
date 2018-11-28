package elements;

import java.awt.Color;
import java.util.ArrayList;

import data.DataManager;
import network.Network;

public class Vehicle {

	private static int idCounter = 1;
	private Network n;
	
	private int id;
	private Cell cell;
	private Cell nextPlace;
	private int speed = 1;
	private boolean hasToLeave = false;
	private ArrayList<Ride> ride = new ArrayList<Ride>();
	private int idCurrentRide = 0;
	private boolean inBucket = true;
	private Color srcColor = new Color(100,0,0);
	private Color dstColor = new Color(100,0,0);
	private boolean isTransiting = false;
	private String currentRoadName;
	
	private int enteringTime = 0;
	private int exitingTime = 0;
	
	public Vehicle(Network n) {
		this.n = n;
		id = idCounter;
		idCounter++;
		cell = null;
		nextPlace = null;
		enteringTime = n.getSimulation().getSimState().getStep();
		exitingTime = enteringTime;
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
			exitingTime = n.getSimulation().getSimState().getStep();
			DataManager.timeSpent.add(exitingTime-enteringTime);
			//System.out.println(exitingTime - enteringTime);
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
			if (tmp.getPreviousCell() != null) {
				tmp.getPreviousCell();
				if (tmp.getVehicle() != null) {
					if (tmp.getVehicle().getSpeed() > 0 && tmp.getVehicle().getSpeed() >= j-1) {
						return false;
					}
				}
				tmp = tmp.getPreviousCell();
			}
		}
		return true;
	}
	
	public boolean checkInCell(Cell c) {
		if (c.getInCell() == null || c.getInCell().getVehicle() == null || !(c.getInCell().getVehicle().getRide().get(getIdCurrentRide()).getNextConnections().get(0).getPosition()==c.getPosition())) {
			return true;
		}
		return false;
	}
	
	public boolean hasANextCell() {
		return getCell().getNextCell() != null;
	}
	
	public boolean hasAnOutCell() {
		return getCell().getOutCell() != null;
	}
	
	public boolean isTrafficLightRedOnTheRoad() {
		return (n.getRoad(getCell().getRoadName()) != null && n.getRoad(getCell().getRoadName()).isTrafficLightRed());
	}
	
	public boolean isOutCellOccupied() {
		return getCell().getOutCell().getVehicle() != null || getCell().getOutCell().isAnOverlapedCellOccupied();
	}
	
	public boolean isNextCellOccupied() {
		return getCell().getNextCell().getVehicle() != null || getCell().getNextCell().isAnOverlapedCellOccupied();
	}
	
	public boolean isRideEmpty() {
		return (getRide() == null || getRide().get(getIdCurrentRide()).getNextConnections().isEmpty());
	}
	
	public boolean isOnNextConnection() {
		return (!isRideEmpty() && (getCell().getPosition() == getRide().get(getIdCurrentRide()).getNextConnections().get(0).getPosition()));
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
		if (!ride.isEmpty()) {
			if (!ride.get(idCurrentRide).getNextConnections().isEmpty()) {
				currentRoadName = ride.get(idCurrentRide).getNextConnections().get(0).getName();
			} else {
				//System.out.println("------- " + currentRoadName + " ------------");
				/*for (Ride r: ride) {
					r.print();
					System.out.println();
				}*/
				//System.out.println("-----------------------");
				
			}
		}
	}
	public void accelerate() {
		++speed;
	}
	public void decelerate() {
		--speed;
	}
	public void removeCurrentConnection() {
		for (int i=0 ; i<ride.size() ; i++) {
			if (i != idCurrentRide) {
				if (!ride.get(i).getNextConnections().isEmpty() && !ride.get(idCurrentRide).getNextConnections().isEmpty()) {
					if (ride.get(i).getNextConnections().get(0).getPosition() == ride.get(idCurrentRide).getNextConnections().get(0).getPosition()
							&& ride.get(i).getNextConnections().get(0).getName().equals(ride.get(idCurrentRide).getNextConnections().get(0).getName())) {
						this.getRide().get(i).getNextConnections().remove(0);
					}
				}
			}
		}
		if (!this.getRide().get(idCurrentRide).getNextConnections().isEmpty()) {
			this.getRide().get(idCurrentRide).getNextConnections().remove(0);
		}
	}
	public void goToXthNextCell(int x) {
		Cell ci = this.getCell();
		for (int iter=0; iter < x; ++iter) {
			if (ci.getNextCell() != null) {
				if (ci.getNextCell().getVehicle() != null || ci.getNextCell().isAnOverlapedCellOccupied() || (ci.isInRoundAbout() && !this.checkInCell(ci.getNextCell()))) {
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
		if (this.getCell() != null && !this.getRide().get(idCurrentRide).getNextConnections().isEmpty()) {
			if (this.getCell() != null && this.getCell().isInRoundAbout()) {
				int l = this.getCell().getRoadLength();
				i = (((this.getRide().get(idCurrentRide).getNextConnections().get(0).getPosition() - this.getCell().getPosition()) % l )+ l) % l;
			} else {
				if (this.getRide().get(idCurrentRide).getNextConnections().size() > 0) {
					i = this.getRide().get(idCurrentRide).getNextConnections().get(0).getPosition() - this.getCell().getPosition();
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
	public int numberOfVhcAhead(Road r) {
		int n = 0;
		Cell tmp;
		if (this.currentRoadName.equals(r.getName())) {
			tmp = this.getCell();
		} else {
			tmp = r.getRoadCells().get(0);
		}
		
		if (tmp != null) {
			for (int j=1; j<tmp.getRoadLength()-this.getCell().getPosition();++j) {
				if (tmp.getNextCell().getVehicle() != null) {
					n++;
				}
				tmp = tmp.getNextCell();
			}
		}
		return n;
	}
	public void nextSpeed() {
		int dc = this.distanceFromNextConnection();
		int dv = this.distFromNextVehicle();
		
		if (this.getCell() == null) {
			this.speed = Math.min(this.speed+1, this.n.getMaxSpeed());
		} else {
			this.speed = Math.min(this.speed+1, this.getCell().getMaxSpeed());
		}
		
		if (dv >= 0) {
			this.speed = Math.min(this.speed, dv);
		}
		if (dc >= 0) {
			this.speed = Math.min(this.speed, dc);
		}
		/*if (this.speed > 1) {
			if (Math.random() < 0.05) {
				this.speed = Math.max(this.speed-1, 0);
			}
		}*/
		if (this.getCell() != null) {
			if (this.getRide().get(idCurrentRide) != null && !this.getRide().get(idCurrentRide).getNextConnections().isEmpty()) {
				if (this.getCell().getPosition() == this.getRide().get(idCurrentRide).getNextConnections().get(0).getPosition()) {
					if (this.getCell().getOutCell() != null && this.getCell().getOutCell().getVehicle() != null) {
						this.setSpeed(0);
					}
				}
			}
		}
		
	}
	
	// Getters & setters ====================================================================================
	public boolean isDestination(String destination) {
		return this.getRide().get(this.getIdCurrentRide()).getNextConnections().get(this.getRide().get(this.getIdCurrentRide()).getNextConnections().size()-1).getName().equals(destination);
	}
	public boolean isSource(String source) {
		return this.getRide().get(this.getIdCurrentRide()).getRoadName().equals(source);
	}
	public String getCurrentRoadName() {
		return this.currentRoadName;
	}
	public void setCurrentRoadName(String currentRoadName) {
		this.currentRoadName = currentRoadName;
	}
	public boolean isTransiting() {
		return isTransiting;
	}
	public void setIsTransiting(boolean isTransiting) {
		this.isTransiting = isTransiting;
	}
	public Color getSrcColor() {
		return srcColor;
	}
	public void setSrcColor(Color srcColor) {
		this.srcColor = srcColor;
	}
	public Color getDstColor() {
		return dstColor;
	}
	public void setDstColor(Color dstColor) {
		this.dstColor = dstColor;
	}
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
	public int getIdCurrentRide() {
		return this.idCurrentRide;
	}
	public void setIdCurrentRide(int idCurrentRide) {
		this.idCurrentRide = idCurrentRide;
	}
	public ArrayList<Ride> getRide() {
		return ride;
	}
	public void addRide(ArrayList<Ride> ride) {
		for (Ride tmp: ride) {
			this.ride.add(tmp);
		}
	}
	public void addRide(Ride ride) {
		this.ride.add(ride);
	}
}
