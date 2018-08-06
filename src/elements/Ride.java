package elements;

import java.util.ArrayList;

public class Ride implements Cloneable {
	String roadName;
	ArrayList<Exit> nextExits = new ArrayList<Exit>();
	
	public Ride() {
		this.roadName = "" ;
	}
	
	public Ride(String name) {
		this.roadName = name ;
	}
	
	public void print() {
		System.out.print("Ride: ");
		for (Exit e: nextExits) {
			e.print();
		}
	}
	
	public void addNextExit(Exit e) {
		this.nextExits.add(e);
	}
	
	public void removeLastExit() {
		this.nextExits.remove(this.nextExits.size()-1);
	}
	
	public Ride clone() {
		Ride ride = null;
		try {
			ride = (Ride) super.clone();
		} catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace(System.err);
		}
		ride.nextExits = new ArrayList<Exit>(this.getNextExits());
		return ride;
		
	}
	
	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public ArrayList<Exit> getNextExits() {
		return nextExits;
	}
	
	
	

}
