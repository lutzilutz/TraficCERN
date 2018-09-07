package elements;

import java.util.ArrayList;

import utils.Utils;

public class Ride implements Cloneable {
	
	private String roadName;
	private ArrayList<Connection> nextConnections = new ArrayList<Connection>();
	private ArrayList<Integer> flow = new ArrayList<Integer>();
	
	public Ride(String name) {
		this.roadName = name ;
		for (int i=0; i<24 ; i++) {
			flow.add(0);
		}
	}
	public Ride() {
		this.roadName = "" ;
		for (int i=0; i<24 ; i++) {
			flow.add(0);
		}
	}
	
	public void print() {
		System.out.print("Ride: ");
		for (Connection c: nextConnections) {
			c.print();
		}
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
		ride.flow = new ArrayList<Integer>(this.flow);
		return ride;
		
	}
	
	// Getters & setters ====================================================================================
	public void setFlow(int value) {
		for (int i = 0 ; i<24 ; i++) {
			flow.set(i, value);
		}
		/*System.out.print(roadName + " : ");
		for (Integer i: flow) {
			System.out.print(i + " ");
		}
		System.out.println();*/
	}
	public void setFlow(int hourStart, int hourEnd, int value) {
		for (int i = hourStart ; i<hourEnd ; i++) {
			flow.set(i, value);
		}
	}
	public ArrayList<Integer> getFlow() {
		return flow;
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
