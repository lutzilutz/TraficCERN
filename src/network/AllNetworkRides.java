package network;

import java.util.ArrayList;

import elements.Exit;
import elements.Ride;

public class AllNetworkRides {

	private String roadName;
	private ArrayList<Ride> networkRides = new ArrayList<Ride>();
	
	public AllNetworkRides(String name) {
		this.roadName = name;
		// TODO Auto-generated constructor stub
	}
	
	public void print() {
		System.out.println("Initial road: " + this.roadName);
		for (Ride ride: networkRides) {
			System.out.print("Exits: ");
			for (Exit e: ride.getNextExits()) {
				System.out.print(" => ");
				e.print();
				
			}
			System.out.println("");
		}
	}
	public void addRide(Ride r) {
		networkRides.add(r);
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	

}
