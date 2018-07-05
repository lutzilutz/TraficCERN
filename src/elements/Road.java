package elements;

import java.util.ArrayList;

public class Road {

	// Simulation
	private int length;
	private ArrayList<Cell> roadCells = new ArrayList<Cell>();
	
	// Display
	private int x,y; // position in pixels from left upper corner
	private int direction; // from 0 to 360 (north)
	
	public Road(int length) {
		this.length = length;
		for (int i=0; i<length; i++) {
			Cell tmp = new Cell();
			//if (i==0 || i==1 || i==2) {
			//	tmp.setOccupied(true);
			//}
			roadCells.add(tmp);
		}
	}
	
	public void display() {
		for (Cell c: roadCells) {
			c.display();
		}
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
