package elements;

import java.util.ArrayList;

import utils.Utils;

public class Ride implements Cloneable {
	
	private String roadName;
	private ArrayList<Connection> nextConnections = new ArrayList<Connection>();
	private ArrayList<Float> flow = new ArrayList<Float>();
	private int numberOfSameRide = 1;
	
	public Ride(String name) {
		this.roadName = name ;
		initFields();
	}
	public Ride() {
		this.roadName = "" ;
		initFields();
	}
	public void initFields() {
		for (int i=0; i<24 ; i++) {
			flow.add(0f);
		}
	}
	public void print() {
		System.out.print("Ride: ");
		for (Connection c: nextConnections) {
			c.print();
		}
		System.out.println();
	}
	
	public void addNextConnection(Connection c) {
		this.nextConnections.add(c);
	}
	
	public void removeLastConnection() {
		this.nextConnections.remove(this.nextConnections.size()-1);
	}
	
	public Ride clone() {
		Ride ride = null;
		try {
			ride = (Ride) super.clone();
		} catch (CloneNotSupportedException e) {
			Utils.log(e);
		}
		ride.nextConnections = new ArrayList<Connection>(this.getNextConnections());
		ride.flow = new ArrayList<Float>(this.flow);
		return ride;
		
	}
	
	// Getters & setters ====================================================================================
	public void ratioFlow(float value) {
		for (int i = 0 ; i<24 ; i++) {
			flow.set(i, flow.get(i)*value);
		}
	}
	public void setFlow(float value) {
		for (int i = 0 ; i<24 ; i++) {
			flow.set(i, value);
		}
	}
	public void setFlow(int hourStart, int hourEnd, float value) {
		for (int i = hourStart ; i<hourEnd ; i++) {
			flow.set(i, value);
		}
	}
	public ArrayList<Float> getFlow() {
		return flow;
	}
	public void setNumberOfSameRide(int numberOfSameRide) {
		this.numberOfSameRide = numberOfSameRide;
	}
	public int getNumberOfSameRide() {
		return this.numberOfSameRide;
	}
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
