package elements;

import network.Network;

public class MultiLaneRoad {
	private Road[] lanesIN;
	private Road[] lanesOUT;
	private Network n;
	private double x,y;
	private int direction;
	public MultiLaneRoad(Network N, int nIN, int nOUT, int length) {
		this.n=N;
		lanesIN = new Road[nIN];
		for (int i=0; i<nIN; ++i) {
			lanesIN[i] = new Road(N, length);
		}
		lanesOUT = new Road[nOUT];
		for (int i=0; i<nOUT; ++i) {
			lanesOUT[i] = new Road(N, length);
		}
	}
	
	public void setPositionFrom(RoundAbout RA, int i) {
		int l = RA.getLength();
		this.lanesIN[0].setEndPositionFrom(RA, i%l);
		for (int j=1; j<lanesIN.length; j=j+1) {
			//this.lanesIN[j].setEndPositionFrom(RA, (i+j)%l);
			this.lanesIN[j].setStartPositionFrom(RA, (i+j)%l);
			this.lanesIN[j].setX((int) this.lanesIN[j].getX() + (this.lanesIN[j].getLength()*this.n.getCellWidth())* Math.sin(Math.toRadians(this.lanesIN[j-1].getDirection()+180)));
			this.lanesIN[j].setY((int) this.lanesIN[j].getY() - (this.lanesIN[j].getLength()*this.n.getCellWidth())* Math.cos(Math.toRadians(this.lanesIN[j-1].getDirection()+180)));
			this.lanesIN[j].setDirection(lanesIN[j-1].getDirection());
		}
		for (int j=0; j<lanesOUT.length; ++j) {
			this.getLanesOUT()[j].setStartPositionFrom(RA, (i-(j+1))%l);
			this.lanesOUT[j].setDirection(180+lanesIN[0].getDirection());
		}
	}
	
	public void setPositionFrom(MultiLaneRoundAbout MLRA, int i) {
		this.setPositionFrom(MLRA.getLanes()[0], i);
	}
	
	public void connectToIn(RoundAbout RA, int i) {
		int l = RA.getLength();
		for (int j=0; j<lanesIN.length; ++j) {
			lanesIN[j].connectTo(RA, (i+j)%l);
		}
		for (int j=0; j<lanesOUT.length; ++j) {
			int rest = (i-(j+1)) %l;
			rest = (l+rest) % l;
			RA.connectTo(this.getLanesOUT()[j], rest );
		}
	}
	
	public void connectToOut(RoundAbout RA, int i) {
		int l = RA.getLength();
		for (int j=0; j<lanesOUT.length; ++j) {
			lanesOUT[j].connectTo(RA, (i+j)%l);
		}
		for (int j=0; j<lanesIN.length; ++j) {
			int rest = (i-(j+1)) %l;
			rest = (l+rest) % l;
			RA.connectTo(this.getLanesIN()[j], rest );
		}
	}
	
	public void connectToIn(MultiLaneRoundAbout MLRA, int i) {
		i = i%(MLRA.getLanes()[0].getLength());
		int i1 = 0;
		int i2 = 0;
		int length = MLRA.getLanes()[0].getLength();
		this.connectToIn(MLRA.getLanes()[0], i);
		for (int laneRoad=0; laneRoad<this.getLanesIN().length; ++laneRoad) {
			i1 = (i+laneRoad) % length;
			for (int laneRA=0; laneRA<MLRA.getLanes().length -1; ++laneRA) {
				i2 = (int) Math.ceil(((double) i1)*(((double) MLRA.getLanes()[laneRA+1].getRoadCells().size())/((double) MLRA.getLanes()[laneRA].getRoadCells().size())) );
				i2 = i2 % (MLRA.getLanes()[laneRA+1].getLength());
				MLRA.getLanes()[laneRA].getRoadCells().get(i1).setOutCell(MLRA.getLanes()[laneRA+1].getRoadCells().get(i2));
				i1 = i2;
			}
		}
		for (int laneRoad=1; laneRoad<this.getLanesOUT().length+1; ++laneRoad) {
			i1 = (((i-laneRoad) % length) + length) % length;
			for (int laneRA=1; laneRA<MLRA.getLanes().length; ++laneRA) {
				i2 = (int) Math.floor(((double) i1)*(((double) MLRA.getLanes()[laneRA].getRoadCells().size())/((double) MLRA.getLanes()[laneRA-1].getRoadCells().size())) );
				i2 = i2 % (MLRA.getLanes()[laneRA].getLength());
				MLRA.getLanes()[laneRA].getRoadCells().get(i2).setOutCell(MLRA.getLanes()[laneRA-1].getRoadCells().get(i1));
				i1 = i2;
			}
		}
	}
	
	public void connectToOut(MultiLaneRoundAbout MLRA, int i) {
		i = i%(MLRA.getLanes()[0].getLength());
		int i1 = 0;
		int i2 = 0;
		int length = MLRA.getLanes()[0].getLength();
		this.connectToOut(MLRA.getLanes()[0], i);
		for (int laneRoad=0; laneRoad<this.getLanesOUT().length; ++laneRoad) {
			i1 = (i+laneRoad) % length;
			for (int laneRA=0; laneRA<MLRA.getLanes().length -1; ++laneRA) {
				i2 = (int) Math.ceil(((double) i1)*(((double) MLRA.getLanes()[laneRA+1].getRoadCells().size())/((double) MLRA.getLanes()[laneRA].getRoadCells().size())) );
				i2 = i2 % (MLRA.getLanes()[laneRA+1].getLength());
				MLRA.getLanes()[laneRA].getRoadCells().get(i1).setOutCell(MLRA.getLanes()[laneRA+1].getRoadCells().get(i2));
				i1 = i2;
			}
		}
		//MLRA.connectTo(this, i);
		for (int laneRoad=1; laneRoad<this.getLanesIN().length+1; ++laneRoad) {
			i1 = (((i-laneRoad) % length) + length) % length;
			for (int laneRA=1; laneRA<MLRA.getLanes().length; ++laneRA) {
				i2 = (int) Math.floor(((double) i1)*(((double) MLRA.getLanes()[laneRA].getRoadCells().size())/((double) MLRA.getLanes()[laneRA-1].getRoadCells().size())) );
				i2 = i2 % (MLRA.getLanes()[laneRA].getLength());
				MLRA.getLanes()[laneRA].getRoadCells().get(i2).setOutCell(MLRA.getLanes()[laneRA-1].getRoadCells().get(i1));
				i1 = i2;
			}
		}
	}

	public Road[] getLanesIN() {
		return lanesIN;
	}

	public void setLanesIN(Road[] lanesIN) {
		this.lanesIN = lanesIN;
	}

	public Road[] getLanesOUT() {
		return lanesOUT;
	}

	public void setLanesOUT(Road[] lanesOUT) {
		this.lanesOUT = lanesOUT;
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
		
		for (int i=0; i<this.lanesIN.length; ++i) {
			this.lanesIN[i].setDirection(direction);
		}
		for (int i=0; i<this.lanesOUT.length; ++i) {
			this.lanesIN[i].setDirection(direction);
		}
	}
	
	

	
}
