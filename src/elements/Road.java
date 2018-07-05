package elements;

import java.util.ArrayList;

import main.Network;

public class Road {

	private static int idCounter = 1;
	protected Network n;
	
	// Simulation
	private int id;
	private int length;
	private ArrayList<Cell> roadCells = new ArrayList<Cell>();
	private boolean generateVehicules = false;
	
	// Display
	private int x,y; // position in pixels from left upper corner
	private int direction; // from 0 to 360 (north)
	
	public Road(Network n, int length) {
		this.n = n;
		id = idCounter++;
		this.length = length;
		for (int i=0; i<length; i++) {
			
			Cell tmp = new Cell();
			
			if (i>0) {
				tmp.setPreviousCell(roadCells.get(i-1));
				roadCells.get(i-1).setNextCell(tmp);
			}
			
			roadCells.add(tmp);
		}
	}
	public void setPositionFrom(RoundAbout ra, int i) {
		this.direction = (int) (ra.getDirection() - i/(float)(ra.getLength()) * 360);
		this.setX((int) (ra.getX() + (ra.getLength()*n.getCellWidth()/(2*Math.PI) + n.getCellWidth()/2) * Math.sin(2*Math.PI*this.getDirection()/360.0)));
		this.setY((int) (ra.getY() - (ra.getLength()*n.getCellHeight()/(2*Math.PI) + n.getCellHeight()/2) * Math.cos(2*Math.PI*this.getDirection()/360.0)));
	}
	public void connectTo(RoundAbout ra, int i) {
		this.getRoadCells().get(this.getLength()-1).setNextCell(ra.getRoadCells().get(i));
	}
	public void display() {
		for (Cell c: roadCells) {
			c.display();
		}
	}

	// Getters and setters ----------------------
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean getGenerateVehicules() {
		return this.generateVehicules;
	}
	public void setGenerateVehicules(boolean b) {
		generateVehicules = b;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getLength() {
		return length;
	}
	public ArrayList<Cell> getRoadCells() {
		return roadCells;
	}
	public void setRoadCells(ArrayList<Cell> roadCells) {
		this.roadCells = roadCells;
	}
}
