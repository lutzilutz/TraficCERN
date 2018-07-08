package elements;

import main.Network;

public class CrossRoad {
	private Road[] roadsIN;
	private Road[] roadsOUT;
	private Cell[] middleCells;
	private int greenTrafficLight = 1;
	private int timeTrafficLight = 8;
	private int type;
	private int direction;

	public CrossRoad(Network n, int x, int y, int dir, int type) {
		if (type == 3) {
			this.direction = dir % 360;
			this.type = type;
			roadsIN = new Road[3];
			roadsOUT = new Road[3];
			middleCells = new Cell[4];
			
			Road r1IN = new Road(n, 10);
			r1IN.setX(x);
			r1IN.setY(y);
			r1IN.setDirection(dir % 360);
			
			middleCells[0] = new Cell();
			middleCells[0].setX((int) (r1IN.getX()+(r1IN.getLength()*n.getCellWidth() + n.getCellWidth()/2)*Math.sin(2*Math.PI*direction/360.0)));
			middleCells[0].setY((int) (r1IN.getY()-(r1IN.getLength()*n.getCellHeight() + n.getCellHeight()/2)*Math.cos(2*Math.PI*direction/360.0)));
			
			middleCells[1] = new Cell();
			middleCells[1].setX((int) (middleCells[0].getX()+(3*n.getCellWidth()/2)*Math.sin(2*Math.PI*direction/360.0)));
			middleCells[1].setY((int) (middleCells[0].getY()-(3*n.getCellHeight()/2)*Math.cos(2*Math.PI*direction/360.0)));
			
			middleCells[2] = new Cell();
			middleCells[2].setX((int) (middleCells[1].getX()+(n.getCellWidth()/2)*Math.sin(2*Math.PI*((direction-90)%360)/360.0)));
			middleCells[2].setY((int) (middleCells[1].getY()-(n.getCellHeight()/2)*Math.cos(2*Math.PI*((direction-90)%360)/360.0)));
			
			middleCells[3] = new Cell();
			middleCells[3].setX((int) (middleCells[0].getX()+(n.getCellWidth()/2)*Math.sin(2*Math.PI*((direction)%360)/360.0)));
			middleCells[3].setY((int) (middleCells[0].getY()-(3*n.getCellHeight()/2)*Math.cos(2*Math.PI*((direction)%360)/360.0)));
			
			Road r1OUT = new Road(n, 10);
			r1OUT.setDirection((direction+90)%360);
			r1OUT.setX((int) (x+(r1OUT.getLength()*n.getCellWidth()+3*n.getCellWidth()/2)*Math.sin(2*Math.PI*(direction)/360.0)+n.getCellWidth()/2*Math.sin(2*Math.PI*((direction+90)%360)/360.0)));
			r1OUT.setY((int) (y-(r1OUT.getLength()*n.getCellHeight()+3*n.getCellHeight()/2)*Math.cos(2*Math.PI*(direction)/360.0)+n.getCellWidth()/2*Math.cos(2*Math.PI*((direction+90)%360)/360.0)));
			
			
			Road r2OUT = new Road(n, 10);
			r2OUT.setDirection(direction);
			r2OUT.setX((int) (x+(r1IN.getLength()*n.getCellWidth()+2*n.getCellWidth())*Math.sin(2*Math.PI*direction/360.0)));
			r2OUT.setY((int) (y-(r1IN.getLength()*n.getCellHeight()+2*n.getCellHeight())*Math.cos(2*Math.PI*direction/360.0)));
			
			Road r3OUT = new Road(n, 10);
			r3OUT.setDirection((direction+180)%360);
			r3OUT.setX((int) (x+(r1IN.getLength()*n.getCellWidth()-n.getCellWidth())*Math.sin(2*Math.PI*direction/360.0)+n.getCellWidth()*Math.sin(2*Math.PI*((direction-90)%360)/360.0)));
			r3OUT.setY((int) (y-(r1IN.getLength()*n.getCellHeight()-n.getCellHeight())*Math.cos(2*Math.PI*direction/360.0)+n.getCellHeight()*Math.sin(2*Math.PI*((direction-90)%360)/360.0)));
			
			Road r2IN = new Road(n, 10);
			r2IN.setDirection((r1OUT.getDirection()+180)%360);
			r2IN.setX((int) (r1OUT.getX()+(r1OUT.getLength()*n.getCellWidth())*Math.sin(2*Math.PI*r1OUT.getDirection()/360.0)+n.getCellWidth()*Math.sin(2*Math.PI*((r1OUT.getDirection()-90)%360)/360.0)));
			r2IN.setY((int) (r1OUT.getY()-(r1OUT.getLength()*n.getCellHeight())*Math.cos(2*Math.PI*r1OUT.getDirection()/360.0)+n.getCellHeight()*Math.cos(2*Math.PI*((r1OUT.getDirection()-90)%360)/360.0)));
			
			Road r3IN = new Road(n, 10);
			r3IN.setDirection((r3OUT.getDirection())%360);
			r3IN.setX((int) (r3OUT.getX()+(r3IN.getLength()*n.getCellWidth() + 2*n.getCellWidth())*Math.sin(2*Math.PI*((r3IN.getDirection())%360)/360)));
			r3IN.setY((int) (r3OUT.getY()+(r3IN.getLength()*n.getCellHeight() + 2*n.getCellHeight())*Math.cos(2*Math.PI*((r3IN.getDirection())%360)/360)));
			
			roadsIN[0] = r1IN;
			roadsIN[1] = r2IN;
			roadsIN[2] = r3IN;
			
			roadsOUT[0] = r1OUT;
			roadsOUT[1] = r2OUT;
			roadsOUT[2] = r3OUT;
			
			middleCells[0].setNextCell(middleCells[1]);
			middleCells[1].setPreviousCell(middleCells[0]);
			middleCells[1].setNextCell(middleCells[2]);
			middleCells[2].setPreviousCell(middleCells[1]);
			middleCells[2].setNextCell(middleCells[3]);
			middleCells[3].setPreviousCell(middleCells[2]);
			middleCells[3].setNextCell(middleCells[0]);
			middleCells[0].setPreviousCell(middleCells[3]);
			
			middleCells[0].setOutCell(r1OUT.getRoadCells().get(0));
			r1OUT.getRoadCells().get(0).setPreviousCell(middleCells[0]);
			middleCells[1].setOutCell(r2OUT.getRoadCells().get(0));
			r2OUT.getRoadCells().get(0).setPreviousCell(middleCells[1]);
			middleCells[3].setOutCell(r3OUT.getRoadCells().get(0));
			r3OUT.getRoadCells().get(0).setPreviousCell(middleCells[3]);

			
			r1IN.getRoadCells().get(r1IN.getLength()-1).setNextCell(middleCells[0]);
			r2IN.getRoadCells().get(r2IN.getLength()-1).setNextCell(middleCells[1]);
			r3IN.getRoadCells().get(r3IN.getLength()-1).setNextCell(middleCells[2]);
			
			n.addRoadtoRoads(r1IN);
			n.addRoadtoRoads(r2IN);
			n.addRoadtoRoads(r3IN);
			
			n.addRoadtoRoads(r1OUT);
			n.addRoadtoRoads(r2OUT);
			n.addRoadtoRoads(r3OUT);
			
			r1IN.setGenerateVehicules(true);
			
			
		} else {
			this.direction = dir % 360;
			this.type = 4;
			roadsIN = new Road[4];
			roadsOUT = new Road[4];
			middleCells = new Cell[4];
			
			Road r1IN = new Road(n, 10);
			r1IN.setX(x);
			r1IN.setY(y);
			r1IN.setDirection(dir);
			middleCells[0].setX((int) (r1IN.getX()+(r1IN.getLength()*n.getCellWidth() + n.getCellWidth()/2)*Math.sin(2*Math.PI*direction/360)));
			middleCells[0].setY((int) (r1IN.getY()-(r1IN.getLength()*n.getCellHeight() + n.getCellHeight()/2)*Math.cos(2*Math.PI*direction/360)));
			
			middleCells[1].setX((int) (middleCells[0].getX()+(n.getCellWidth() + n.getCellWidth()/2)*Math.sin(2*Math.PI*direction/360)));
			middleCells[1].setY((int) (middleCells[0].getY()-(n.getCellHeight() + n.getCellHeight()/2)*Math.cos(2*Math.PI*direction/360)));
			
			middleCells[2].setX((int) (middleCells[1].getX()+(n.getCellWidth() + n.getCellWidth()/2)*Math.sin(2*Math.PI*((direction-90)%360)/360)));
			middleCells[2].setY((int) (middleCells[1].getY()-(n.getCellHeight() + n.getCellHeight()/2)*Math.cos(2*Math.PI*((direction-90)%360)/360)));
			
			middleCells[3].setX((int) (middleCells[0].getX()+(n.getCellWidth() + n.getCellWidth()/2)*Math.sin(2*Math.PI*((direction-90)%360)/360)));
			middleCells[3].setY((int) (middleCells[0].getY()-(n.getCellHeight() + n.getCellHeight()/2)*Math.cos(2*Math.PI*((direction-90)%360)/360)));
			
			Road r1OUT = new Road(n, 10);
			r1OUT.setDirection((direction+90)%360);
			r1OUT.setX((int) (middleCells[0].getX()+(n.getCellWidth() + n.getCellWidth()/2)*Math.sin(2*Math.PI*((direction+90)%360)/360)));
			r1OUT.setY((int) (middleCells[0].getY()+(n.getCellHeight() + n.getCellHeight()/2)*Math.cos(2*Math.PI*((direction+90)%360)/360)));
			
			Road r2OUT = new Road(n, 10);
			r2OUT.setDirection(direction);
			r2OUT.setX((int) (middleCells[1].getX()+(n.getCellWidth() + n.getCellWidth()/2)*Math.sin(2*Math.PI*(direction)/360)));
			r2OUT.setY((int) (middleCells[1].getY()+(n.getCellHeight() + n.getCellHeight()/2)*Math.cos(2*Math.PI*(direction)/360)));
			
			Road r3OUT = new Road(n, 10);
			r3OUT.setDirection((direction+180)%360);
			r3OUT.setX((int) (middleCells[3].getX()+(n.getCellWidth() + n.getCellWidth()/2)*Math.sin(2*Math.PI*((direction+180)%360)/360)));
			r3OUT.setY((int) (middleCells[3].getY()+(n.getCellHeight() + n.getCellHeight()/2)*Math.cos(2*Math.PI*((direction+180)%360)/360)));
			
			Road r2IN = new Road(n, 10);
			r2IN.setDirection((r1OUT.getDirection()-90)%360);
			r2IN.setX((int) (middleCells[1].getX()+(r2IN.getLength()*n.getCellWidth() + n.getCellWidth()/2)*Math.sin(2*Math.PI*((r2IN.getDirection()+180)%360)/360)));
			r2IN.setY((int) (middleCells[1].getY()+(r2IN.getLength()*n.getCellHeight() + n.getCellHeight()/2)*Math.cos(2*Math.PI*((r2IN.getDirection()+180)%360)/360)));
			
			Road r3IN = new Road(n, 10);
			r3IN.setDirection((r1OUT.getDirection()-90)%360);
			r3IN.setX((int) (middleCells[2].getX()+(r3IN.getLength()*n.getCellWidth() + n.getCellWidth()/2)*Math.sin(2*Math.PI*((r3IN.getDirection()+180)%360)/360)));
			r3IN.setY((int) (middleCells[2].getY()+(r3IN.getLength()*n.getCellHeight() + n.getCellHeight()/2)*Math.cos(2*Math.PI*((r3IN.getDirection()+180)%360)/360)));
		}
	}

	public Road[] getRoadsIN() {
		return roadsIN;
	}

	public void setRoadsIN(Road[] roadsIN) {
		this.roadsIN = roadsIN;
	}

	public Road[] getRoadsOUT() {
		return roadsOUT;
	}

	public void setRoadsOUT(Road[] roadsOUT) {
		this.roadsOUT = roadsOUT;
	}

	public Cell[] getMiddleCells() {
		return middleCells;
	}

	public void setMiddleCells(Cell[] middleCells) {
		this.middleCells = middleCells;
	}

	public int getGreenTrafficLight() {
		return greenTrafficLight;
	}

	public void setGreenTrafficLight(int greenTrafficLight) {
		this.greenTrafficLight = greenTrafficLight;
	}

	public int getTimeTrafficLight() {
		return timeTrafficLight;
	}

	public void setTimeTrafficLight(int timeTrafficLight) {
		this.timeTrafficLight = timeTrafficLight;
	}

	public int getType() {
		return type;
	}

	public int getDirection() {
		return direction;
	}
	

}
