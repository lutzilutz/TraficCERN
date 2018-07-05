package elements;

import java.util.ArrayList;

public class Road {

	int length;
	ArrayList<Cell> roadCells = new ArrayList<Cell>();
	
	public Road(int length) {
		this.length = length;
		for (int i=0; i<length; i++) {
			Cell tmp = new Cell();
			if (i==0 || i==1 || i==2) {
				tmp.setOccupied(true);
			}
			roadCells.add(tmp);
		}
	}
	
	public void display() {
		for (Cell c: roadCells) {
			c.display();
		}
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
