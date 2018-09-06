package elements;

import java.util.ArrayList;

import utils.Utils;

public class Ride implements Cloneable {
	
	private String roadName;
	private ArrayList<Connection> nextConnections = new ArrayList<Connection>();
	private boolean ignoreFlow = false;
	private int flow = 0;
	//private int[] flow = new int[2]; // 0-day (without rush-hours), 1-rush-hours (7 to 10 am)
	
	public Ride(int flowDay, int flowRushHours) {
		this.roadName = "" ;
		//flow[0] = flowDay;
		//flow[1] = flowRushHours;
	}
	public Ride(String name, int flowDay, int flowRushHours) {
		this.roadName = name ;
		//flow[0] = flowDay;
		//flow[1] = flowRushHours;
	}
	public Ride(String name) {
		this.roadName = name ;
		ignoreFlow = true;
	}
	public Ride() {
		this.roadName = "" ;
		ignoreFlow = true;
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
			//cnse.printStackTrace(System.err);
			Utils.log(e);
		}
		ride.nextConnections = new ArrayList<Connection>(this.getNextConnections());
		return ride;
		
	}
	
	// Getters & setters ====================================================================================
	public void setFlow(int flow) {
		this.flow = flow;
	}
	public int getFlow() {
		return flow;
	}
	public boolean ignoreFlow() {
		return ignoreFlow;
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
