package elements;

import network.Network;

public class MultiLaneRoundAbout {
	private RoundAbout[] lanes;
	private Network n;
	private double x=0,y=0;
	private int direction=0;
	
	public MultiLaneRoundAbout(Network n, int nLanes, int length) {
		this.n = n;
		int nCells = length;
		lanes = new RoundAbout[nLanes];
		for (int i=0; i<nLanes; ++i) {
			nCells = (int) (length-i*2*Math.PI*this.n.getCellHeight()/this.n.getCellWidth());
			if (nCells > 0) {
				lanes[i] = new RoundAbout(n, nCells);
			}
		}
		
	}
	
	public void connectToIn(MultiLaneRoad MLR, int i) {
		MLR.connectToIn(this, i);
	}
	
	public void connectToOut(MultiLaneRoad MLR, int i) {
		MLR.connectToOut(this, i);
	}
	
	public void setPositionInFrom(MultiLaneRoad MLR) {
		this.getLanes()[0].setPositionFrom(MLR.getLanesIN()[0],0);
		for (int i=1; i<this.getLanes().length; ++i) {
			this.getLanes()[i].setX(this.getLanes()[0].getX());
			this.getLanes()[i].setY(this.getLanes()[0].getY());
		}
	}
	
	public void setPositionOUTFrom(MultiLaneRoad MLR) {
		this.getLanes()[0].setPositionFrom(MLR.getLanesOUT()[0],0);
		for (int i=1; i<this.getLanes().length; ++i) {
			this.getLanes()[i].setX(this.getLanes()[0].getX());
			this.getLanes()[i].setY(this.getLanes()[0].getY());
		}
	}
	
	public void connectTo(Road R, int i) {
		this.getLanes()[0].connectTo(R, i);
	}
	
	
	public RoundAbout[] getLanes() {
		return lanes;
	}
	public void setLanes(RoundAbout[] lanes) {
		this.lanes = lanes;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
		for (int i=0; i<this.lanes.length; ++i) {
			this.lanes[i].setX(x);
		}
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
		for (int i=0; i<this.lanes.length; ++i) {
			this.lanes[i].setY(y);
		}
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
		
		for (int i=0; i<this.lanes.length; ++i) {
			this.lanes[i].setDirection(direction);
		}
	}
	
	

}