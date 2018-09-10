package elements;

import java.util.ArrayList;

import network.Network;

public class Phase {
	private Network n;
	private int t=0;
	private int tMin;
	private int tMax;
	private ArrayList<Road> concernedRoads = new ArrayList<Road>();
	private ArrayList<Road> roadsToCheck = new ArrayList<Road>();
	
	public Phase(Network n, int min, int max) {
		this.n = n;
		if (min < max) {
			this.tMax = max;
			this.tMin = min;
		} else {
			this.tMax = min;
			this.tMin = max;
		}
	}
	
	public boolean tBetweenMinAndMax() {
		if (t <= this.tMax && t >= this.tMin) {
			return true;
		} else {
			return false;
		}
	}
	
	public void increment() {
		t = t+1;
	}
	
	public void tReset() {
		t = 0;
	}
	
	public boolean checkRoadsToCheck() {
		for (Road r: this.roadsToCheck) {
			for (int i=0; i<this.n.getMaxSpeed(); ++i) {
				if (r.getRoadCells().get(r.getLength()-1-i) != null && r.getRoadCells().get(r.getLength()-1-i).getVehicle() != null) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean stopCondition() {
		if (t > tMax) {
			return true;
		} 
		else if(this.tBetweenMinAndMax() && this.checkRoadsToCheck()) {
			return true;
		}
		return false;
	}
	
	public void setAllConcernedTrafficLightsRed(boolean b) {
		for (Road r: this.concernedRoads) {
			r.setTrafficLightRed(b);
		}
	}
	
	public void startPhase() {
		this.tReset();
		this.setAllConcernedTrafficLightsRed(false);
	}
	
	public void endPhase() {
		this.setAllConcernedTrafficLightsRed(true);
	}
	
	public void addRoadToCheck (Road r) {
		this.roadsToCheck.add(r);
	}
	
	public void addConcernedRoad(Road r) {
		this.concernedRoads.add(r);
	}
	
	public void clearRoadsToCheck() {
		this.roadsToCheck.clear();
	}
	
	public void clearConcernedRoad() {
		this.concernedRoads.clear();
	}
	
}
