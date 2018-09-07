package network;

import java.util.ArrayList;

import elements.Connection;
import elements.Ride;

public class AllNetworkRides {

	private String roadName;
	private ArrayList<Ride> networkRides = new ArrayList<Ride>();
	
	public AllNetworkRides(String name) {
		this.roadName = name;
	}
	
	public void print() {
		System.out.println("Initial road: " + this.roadName);
		for (Ride ride: networkRides) {
			System.out.print("Connections: ");
			for (Connection e: ride.getNextConnections()) {
				System.out.print(" => ");
				e.print();
				
			}
			System.out.println("");
		}
	}
	public void addRide(Ride r) {
		networkRides.add(r);
	}

	// Getters & setters ====================================================================================
	public ArrayList<Ride> getNetworkRides() {
		return networkRides;
	}
	public String getRoadName() {
		return roadName;
	}
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
}
