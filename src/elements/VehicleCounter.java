package elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import graphics.Assets;
import graphics.Text;
import network.Network;
import network.NetworkRendering;

public class VehicleCounter {
	
	private Network network;
	private Road road;
	private double x=0,y=0;
	private double flow = 0;
	private static double flowCoefficient = 1/8.0;
	private int counter = 0;
	private boolean hasVehicle = false;
	private double location;
	private String name;
	
	public VehicleCounter(Network network, Road road, double location, String name) {
		this.network = network;
		this.road = road;
		this.location = location;
		this.name = name;
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
	public void render(Graphics g) {
		Graphics2D gg = (Graphics2D) g.create();
		gg.translate(-NetworkRendering.bounds.x, -NetworkRendering.bounds.y);
		gg.setColor(Color.yellow);
		DecimalFormat df = new DecimalFormat("##0.00");
		gg.drawLine((int) (road.getRoadCells().get((int) (getLocation()*road.getLength())).getX() + 2*network.getCellWidth()*3*Math.cos(-2*Math.PI*road.getReorientations().get(1).getY()/360.0)),
					(int) (road.getRoadCells().get((int) (getLocation()*road.getLength())).getY() - 2*network.getCellWidth()*3*Math.sin(-2*Math.PI*road.getReorientations().get(1).getY()/360.0)),
					(int) (road.getRoadCells().get((int) (getLocation()*road.getLength())).getX() + 0*network.getCellWidth()*3*Math.cos(-2*Math.PI*road.getReorientations().get(1).getY()/360.0)),
					(int) (road.getRoadCells().get((int) (getLocation()*road.getLength())).getY() - 0*network.getCellWidth()*3*Math.sin(-2*Math.PI*road.getReorientations().get(1).getY()/360.0)));
		int x = (int) (road.getRoadCells().get((int) (getLocation()*road.getLength())).getX() + 2*network.getCellWidth()*3*Math.cos(-2*Math.PI*road.getReorientations().get(1).getY()/360.0));
		int y = (int) (road.getRoadCells().get((int) (getLocation()*road.getLength())).getY() - 3*network.getCellWidth()*3*Math.sin(-2*Math.PI*road.getReorientations().get(1).getY()/360.0));
		
		Text.drawString(gg, name, Color.yellow, x, y, true, Assets.normalBoldFont);
		Text.drawString(gg, df.format(getFlow()) + " / min", Color.yellow, x, y+15, true, Assets.normalBoldFont);
		
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
