package elements;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumSet;

import network.Network;

public class Road {

	private static int idCounter = 1; // Roads ID counter
	protected Network n;
	
	// Simulation
	private int id; // Roads ID
	private int length;
	private ArrayList<Cell> roadCells = new ArrayList<Cell>();
	private boolean generateVehicules = false; // if generate Vehicles
	private boolean isTrafficLightRed = false;
	private EnumSet<Direction> directions;
	private ArrayList<Point> reorientations = new ArrayList<Point>();
	
	// Display
	private double x,y; // position in pixels from left upper corner
	private int direction; // from 0 to 360 (north), 90 (east), 180 (south), 270 (west)
	
	public Road(Network n, int length) {
		this.n = n;
		id = idCounter;
		idCounter++;
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
	public ArrayList<Point> getReorientations() {
		return this.reorientations;
	}
	public void addPoint(Point point) {
		reorientations.add(point);
	}
	// Set position and direction from a RoundAbout cell (out road)
	public void setStartPositionFrom(RoundAbout ra, int i) {
		setDirection((int) (ra.getDirection() - i/(float)(ra.getLength()) * 360));
		this.setX((int) (ra.getX() + (ra.getLength()*n.getCellWidth()/(2*Math.PI) + n.getCellHeight()/2) * Math.sin(2*Math.PI*this.getDirection()/360.0)));
		this.setY((int) (ra.getY() - (ra.getLength()*n.getCellWidth()/(2*Math.PI) + n.getCellHeight()/2) * Math.cos(2*Math.PI*this.getDirection()/360.0)));
	}
	// Set position and direction from a Road cell (out road)
	public void setStartPositionFrom(Road r, int i, int direction) {
		setDirection(direction);
		this.setX((int) (r.getX() + (r.getLength()*n.getCellWidth() + n.getCellHeight()/2) * Math.sin(2*Math.PI*r.getDirection()/360.0)));
		this.setY((int) (r.getY() - (r.getLength()*n.getCellWidth() + n.getCellHeight()/2) * Math.cos(2*Math.PI*r.getDirection()/360.0)));
	}
	// Set position and direction from a RoundAbout cell (in road)
	public void setEndPositionFrom(RoundAbout ra, int i, int direction) {
		if (this.getReorientations().size()==0) {
			setDirection(direction);
		} else {
			
		}
		double x = (int) (ra.getX() - (ra.getLength()*n.getCellWidth()/(2*Math.PI))*Math.sin(2*Math.PI*(i/(float)ra.getLength())));
		double y = (int) (ra.getY() - (ra.getLength()*n.getCellWidth()/(2*Math.PI))*Math.cos(2*Math.PI*(i/(float)ra.getLength())));
		double last = 0;
		for (int j=0 ; j<this.getReorientations().size() ; j++) {
			// Last segment
			if (j == this.getReorientations().size()-1) {
				x += n.getCellWidth() * (this.getLength()+0.5-this.getReorientations().get(j).getX()) * Math.sin(2*Math.PI*this.getReorientations().get(j).getY()/360.0 + Math.PI);
				y += - n.getCellWidth() * (this.getLength()+0.5-this.getReorientations().get(j).getX()) * Math.cos(2*Math.PI*this.getReorientations().get(j).getY()/360.0 + Math.PI);
			}
			// All others
			else {
				x += n.getCellWidth() * (this.getReorientations().get(j+1).getX()-last) * Math.sin(2*Math.PI*this.getReorientations().get(j).getY()/360.0 + Math.PI);
				y += - n.getCellWidth() * (this.getReorientations().get(j+1).getX()-last) * Math.cos(2*Math.PI*this.getReorientations().get(j).getY()/360.0 + Math.PI);
				last = this.getReorientations().get(j+1).getX();
			}
		}
		this.setX(x);
		this.setY(y);
	}
	// DEPRECATED ###
	// Set position and direction from a RoundAbout cell (in road)
	public void setEndPositionFrom(RoundAbout ra, int i) {
		this.direction = (int) (ra.getDirection() - i/(float)(ra.getLength()) * 360 + 180);
		this.setX((int) (ra.getX() + (ra.getLength()*n.getCellWidth()/(2*Math.PI) + n.getCellHeight()/2 + length*n.getCellWidth()) * Math.sin(Math.PI + 2*Math.PI*this.getDirection()/360.0) ));
		this.setY((int) (ra.getY() - (ra.getLength()*n.getCellWidth()/(2*Math.PI) + n.getCellHeight()/2 + length*n.getCellWidth()) * Math.cos(Math.PI + 2*Math.PI*this.getDirection()/360.0) ));
	}
	public void setPositionInFrom(CrossRoad CR, int i) {
		i = i % 4;
		this.direction = (int) ((CR.getDirection()+((3-i)%4)*90)%360);
		double valInter1 = n.getCellHeight()/2;
		double valInter2 = n.getCellWidth()*this.getLength()+n.getCellHeight();
		double angle =  Math.atan(valInter1/valInter2);
		angle = Math.toDegrees(angle);
		angle = this.getDirection()+180-angle;
		double posX = (CR.getX() + (Math.sqrt( Math.pow(n.getCellWidth()*this.getLength()+n.getCellHeight(), 2) + Math.pow(n.getCellHeight(), 2)/4 ) * Math.sin(2*Math.PI*(angle/360.0)) ));
		this.setX((int) posX);
		double posY = (CR.getY() - (Math.sqrt( Math.pow(n.getCellWidth()*this.getLength()+n.getCellHeight(), 2) + Math.pow(n.getCellHeight(), 2)/4 ) * Math.cos(2*Math.PI*(angle/360.0)) ));
		this.setY((int) posY);
		CR.addRoadIn(this, i);
	}
	public void setPositionOutFrom(CrossRoad CR, int i) {
		i = i % 4;
		this.direction = (int) ((CR.getDirection()-(i % 4)*90)%360);
		double angle =  Math.atan(2);
		angle = Math.toDegrees(angle);
		this.setX((int) (CR.getX() + (n.getCellHeight()/2 * Math.sqrt(5.0) * Math.sin(2*Math.PI*((this.getDirection()+90-angle)%360)/360))));
		this.setY((int) (CR.getY() - (n.getCellHeight()/2 * Math.sqrt(5.0) * Math.cos(2*Math.PI*((this.getDirection()+90-angle)%360)/360))));
		CR.addRoadOut(this, i);
	}
	
	// Connect "pointer" of last Cell to Cell i of RoundAbout
	public void connectTo(Road r, int i) {
		this.getRoadCells().get(this.getLength()-1).setNextCell(r.getRoadCells().get(i));
		r.getRoadCells().get(i).setPreviousCell(this.getRoadCells().get(this.getLength()-1));
	}
	public void connectTo(RoundAbout ra, int i) {
		this.getRoadCells().get(this.getLength()-1).setNextCell(ra.getRoadCells().get(i));
	}
	public void connectTo(CrossRoad CR, int i) {
		i = i % 4;
		this.getRoadCells().get(this.getLength()-1).setNextCell(CR.getMiddleCells()[i]);
	}
	
	public void display() {
		for (Cell c: roadCells) {
			c.display();
		}
	}
	
	public void addRoadDirection(Direction d) {
		this.directions.add(d);
	}
	
	public void removeRoadDirection(Direction d) {
		this.directions.remove(d);
	}
	
	public void removeAllRoadDirections() {
		this.directions.clear();
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
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
		this.reorientations.add(new Point(0,direction));
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
	public boolean isTrafficLightRed() {
		return isTrafficLightRed;
	}
	public void setTrafficLightRed(boolean isTrafficLightRed) {
		this.isTrafficLightRed = isTrafficLightRed;
	}
	public EnumSet<Direction> getDirections() {
		return directions;
	}
	public void setDirections(EnumSet<Direction> directions) {
		this.directions = directions;
	}
	
	
}
