package elements;

import network.Network;

public class VehicleCounter {
	
	private Road road;
	private double x=0,y=0;
	private double flow = 0;
	private static double flowCoefficient = 1/8.0;
	private int counter = 0;
	private boolean hasVehicle = false;
	private double location;
	
	public VehicleCounter(Network network, Road road, double location) {
		this.road = road;
		this.location = location;
	}

	public void computeCounter() {
		if (vehicleComingToCounter()) {
			if (!hasVehicle) {
				hasVehicle = true;
				incrementCounter();
			}
		} else {
			hasVehicle = false;
		}
	}
	public boolean vehicleComingToCounter() {
		boolean vehicleOnCell = (road.getRoadCells().get((int) (location*road.getLength())).getVehicle() != null);
		boolean vehicleOnPreviousCellSpeed2 = (road.getRoadCells().get((int) (location*road.getLength() - 1)).getVehicle() != null && road.getRoadCells().get((int) (location*road.getLength() - 1)).getVehicle().getSpeed()==2);
		return vehicleOnCell || vehicleOnPreviousCellSpeed2;
	}
	public void computeFlow() {
		flow = flowCoefficient * flow + (1-flowCoefficient) * counter;
		counter = 0;
	}
	public double getFlow() {
		return flow;
	}
	public double getLocation() {
		return location;
	}
	public void incrementCounter() {
		counter++;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
}
