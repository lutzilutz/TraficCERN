package elements;

import java.util.ArrayList;

import utils.Utils;

public class Ride implements Cloneable {
	String roadName;
	ArrayList<Connection> nextConnections = new ArrayList<Connection>();
	
	public Ride() {
		this.roadName = "" ;
	}
	
	public Ride(String name) {
		this.roadName = name ;
	}
	
	public void print() {
		System.out.print("Ride: ");
		for (Connection e: nextConnections) {
			e.print();
		}
	}
	
	public void addNextConnection(Connection e) {
		this.nextConnections.add(e);
	}
	
	public void removeLastConnection() {
		this.nextConnections.remove(this.nextConnections.size()-1);
	}
	
	public Ride clone() {
		Ride ride = null;
		try {
			ride = (Ride) super.clone();
		} catch (CloneNotSupportedException e) {
			//cnse.printStackTrace(System.err);
			Utils.log(e);
		}
		ride.nextConnections = new ArrayList<Connection>(this.getNextConnections());
		return ride;
		
	}
	
	// Getters & setters ====================================================================================
	public String getRoadName() {
		return roadName;
	}
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	public ArrayList<Connection> getNextConnections() {
		return nextConnections;
	}
}
