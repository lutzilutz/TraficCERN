package data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import elements.Road;
import graphics.Assets;
import graphics.Text;
import network.Network;
import network.NetworkRendering;

public class VehicleCounter {
	
	private Network network; // link to the current network
	private Road road; // link to the current road
	private double flow = 0; // flow of the counter
	private static double flowCoefficient = 1/8.0; // coefficient representing impact of previous flows
	private int counter = 0; // actual counter
	private boolean hasVehicle = false; // if the counter has a vehicle on it
	private double location; // location of counter on the road, 0 is start of the road, 1 is the end
	private String name; // name of the counter
	
	// Constructor
	public VehicleCounter(Network network, Road road, double location, String name) {
		this.network = network;
		this.road = road;
		this.location = location;
		this.name = name;
	}

	// Compute a step of the counter
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
	
	// True if a vehicle will be on counter at next step
	private boolean vehicleComingToCounter() {
		boolean vehicleOnCell = (road.getRoadCells().get((int) (location*road.getLength())).getVehicle() != null);
		boolean vehicleOnPreviousCellSpeed2 = (road.getRoadCells().get((int) (location*road.getLength() - 1)).getVehicle() != null && road.getRoadCells().get((int) (location*road.getLength() - 1)).getVehicle().getSpeed()==2);
		return vehicleOnCell || vehicleOnPreviousCellSpeed2;
	}
	
	// Compute the new flow of the counter
	public void computeFlow() {
		flow = flowCoefficient * flow + (1-flowCoefficient) * counter;
		counter = 0;
	}
	
	// Render method
	public void render(Graphics g) {
		Graphics2D gg = (Graphics2D) g.create();
		gg.translate(-NetworkRendering.bounds.x, -NetworkRendering.bounds.y);
		gg.setColor(Color.yellow);
		DecimalFormat df = new DecimalFormat("##0.00");
		
		int lastReorientation = 0;
		for (int i=0; i<road.getReorientations().size(); i++) {
			if ((int) (getLocation()*road.getLength()) < road.getReorientations().get(i).getX()) {
				lastReorientation = i;
			}
		}
		
		gg.drawLine((int) (road.getRoadCells().get((int) (getLocation()*road.getLength())).getX() + 2*network.getCellWidth()*3*Math.cos(-2*Math.PI*road.getReorientations().get(lastReorientation).getY()/360.0)),
				(int) (road.getRoadCells().get((int) (getLocation()*road.getLength())).getY() - 2*network.getCellWidth()*3*Math.sin(-2*Math.PI*road.getReorientations().get(lastReorientation).getY()/360.0)),
				(int) (road.getRoadCells().get((int) (getLocation()*road.getLength())).getX() + 0*network.getCellWidth()*3*Math.cos(-2*Math.PI*road.getReorientations().get(lastReorientation).getY()/360.0)),
				(int) (road.getRoadCells().get((int) (getLocation()*road.getLength())).getY() - 0*network.getCellWidth()*3*Math.sin(-2*Math.PI*road.getReorientations().get(lastReorientation).getY()/360.0)));
		
		int x = (int) (road.getRoadCells().get((int) (getLocation()*road.getLength())).getX() + 2*network.getCellWidth()*3*Math.cos(-2*Math.PI*road.getReorientations().get(lastReorientation).getY()/360.0));
		int y = (int) (road.getRoadCells().get((int) (getLocation()*road.getLength())).getY() - 3*network.getCellWidth()*3*Math.sin(-2*Math.PI*road.getReorientations().get(lastReorientation).getY()/360.0));
		
		Text.drawString(gg, name, Color.yellow, x, y, true, Assets.normalBoldFont);
		Text.drawString(gg, df.format(getFlow()) + " / min", Color.yellow, x, y+15, true, Assets.normalBoldFont);	
	}
	private void incrementCounter() {
		counter++;
	}
	
	// Getters & setters ====================================================================================
	public int getCounter() {
		return counter;
	}
	public double getFlow() {
		return flow;
	}
	public double getLocation() {
		return location;
	}
}
