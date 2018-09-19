package elements;

import java.util.ArrayList;
import java.util.Collections;

import network.Network;
import utils.SortByPos;

public class MultiLaneRoundAbout {
	private String name;
	private ArrayList<Connection> exits = new ArrayList<Connection>();
	private ArrayList<Connection> enters = new ArrayList<Connection>();
	private RoundAbout[] lanes;
	private Network n;
	private double x=0,y=0;
	private int direction=0;
	
	public MultiLaneRoundAbout(Network n, int nLanes, int length) {
		this.n = n;
		//int nCells = length;
		lanes = new RoundAbout[nLanes];
		for (int i=0; i<nLanes; ++i) {
			/*nCells = (int) (length-i*2*Math.PI*this.n.getCellHeight()/this.n.getCellWidth());
			if (nCells > 0) {
				lanes[i] = new RoundAbout(n, nCells);
			}*/
			lanes[i] = new RoundAbout(n, length, i);
		}
		
	}
	
	public MultiLaneRoundAbout(Network n, int nLanes, int length, String name) {
		this.name = name;
		this.n = n;
		//int nCells = length;
		lanes = new RoundAbout[nLanes];
		for (int i=0; i<nLanes; ++i) {
			/*nCells = (int) (length-i*2*Math.PI*this.n.getCellHeight()/this.n.getCellWidth());
			if (nCells > 0) {
				lanes[i] = new RoundAbout(n, nCells, name + (i+1));
			}*/
			lanes[i] = new RoundAbout(n, length, i);
			lanes[i].setName(name + (i+1));
		}
		
	}
	
	public void connectTo(Road R, int i) {
		int raSize = this.getLanes()[0].getLength();
		i = ((i % raSize)+raSize)%raSize;
		//int i1 = i;
		//int i2 = i-1;
		this.getLanes()[0].getRoadCells().get(i).setOutCell(R.getRoadCells().get(0));
		R.getRoadCells().get(0).setInCell(this.getLanes()[0].getRoadCells().get(i));
		this.addExit(R.getName(), i);
		R.addEnter(this.getName(), 0);
		for (int j=1; j<this.getLanes().length; ++j) {
			//i1 = ((i1 % raSize)+raSize)%raSize;
			//i2 = ((i2 % raSize)+raSize)%raSize;
			this.getLanes()[j].getRoadCells().get(i).setOutCell(this.getLanes()[j-1].getRoadCells().get(i));
			this.getLanes()[j-1].getRoadCells().get(i).setInCell(this.getLanes()[j].getRoadCells().get(i));
			//i1 = i2;
			//i2 = i2-1;
		}
	}
	
	public void generateRidesAux(int n, Ride ride) {
		this.removeLastGoInGoOutConnections(ride);
		Collections.sort(this.getExits(), new SortByPos());
		
		if(n==0) {
			ride.removeLastConnection();
			return;
		} else if (n > 0) {
			//int L = this.getLanes()[0].getLength();
			int numOfLanes = this.getLanes().length;
			int laneInt = 0;
			int indexOfEnter = -1;
			for (Connection ent: this.getEnters()) {
				if (ride.getNextConnections().size()>1) {
					if (ent.getName().equals(ride.getNextConnections().get(ride.getNextConnections().size()-2).getName())) {
						indexOfEnter = ent.getPosition();
					}
				} else if (ride.getNextConnections().size()==1) {
					if (ent.getName().equals(ride.getRoadName())) {
						indexOfEnter = ent.getPosition();
					}
				}
			}
			if (indexOfEnter != -1) {
				for (Connection e: this.getExits()) {
					if (e.getPosition()>indexOfEnter) {

						ArrayList<Connection> connectionsIN = new ArrayList<Connection>();
						ArrayList<Connection> connectionsOUT = new ArrayList<Connection>();
						if (laneInt/*/2*/ > 0) {
							for (int i=0; i<laneInt-1/*/2*/; ++i) {
								connectionsIN.add(new Connection("GO IN", indexOfEnter/*+i*/));
								connectionsOUT.add(0, new Connection("GO OUT", e.getPosition()/*-(i+1)*/));
							}
						}
						ride.getNextConnections().addAll(connectionsIN);
						ride.getNextConnections().addAll(connectionsOUT);

						for (Road r: this.n.getRoads()) {
							if (e.getName().equals(r.getName())) {
								ride.addNextConnection(e.clone());
								r.generateRidesAux(n-1, ride);
								this.removeLastGoInGoOutConnections(ride);
							}
						}
						for (RoundAbout ra: this.n.getRoundAbouts()) {
							if (e.getName().equals(ra.getName())) {
								ride.addNextConnection(e.clone());
								ra.generateRidesAux(n-1, ride);
								this.removeLastGoInGoOutConnections(ride);
							}
						}
						for (CrossRoad cr: this.n.getCrossRoads()) {
							if (e.getName().equals(cr.getName())) {
								ride.addNextConnection(e.clone());
								cr.generateRidesAux(n-1, ride);
								this.removeLastGoInGoOutConnections(ride);
							}
						}
						this.removeLastGoInGoOutConnections(ride);

						++ laneInt;
						if (laneInt > /*2**/numOfLanes) {
							laneInt = /*2**/numOfLanes;
						}
					}
				}
				for (Connection e: this.getExits()) {
					if (e.getPosition()<indexOfEnter) {
						ArrayList<Connection> connectionsIN = new ArrayList<Connection>();
						ArrayList<Connection> connectionsOUT = new ArrayList<Connection>();
						if (laneInt/*/2*/ > 0) {
							for (int i=0; i<laneInt-1/*/2*/; ++i) {
								connectionsIN.add(new Connection("GO IN", indexOfEnter/*+i*/));
								connectionsOUT.add(0, new Connection("GO OUT", e.getPosition()/*-(i+1)*/));
							}
						}
						ride.getNextConnections().addAll(connectionsIN);
						ride.getNextConnections().addAll(connectionsOUT);

						for (Road r: this.n.getRoads()) {
							if (e.getName().equals(r.getName())) {
								ride.addNextConnection(e.clone());
								r.generateRidesAux(n-1, ride);
								this.removeLastGoInGoOutConnections(ride);
							}
						}
						for (RoundAbout ra: this.n.getRoundAbouts()) {
							if (e.getName().equals(ra.getName())) {
								ride.addNextConnection(e.clone());
								ra.generateRidesAux(n-1, ride);
								this.removeLastGoInGoOutConnections(ride);
							}
						}
						for (CrossRoad cr: this.n.getCrossRoads()) {
							if (e.getName().equals(cr.getName())) {
								ride.addNextConnection(e.clone());
								cr.generateRidesAux(n-1, ride);
								this.removeLastGoInGoOutConnections(ride);
							}
						}
						this.removeLastGoInGoOutConnections(ride);

						++ laneInt;
						if (laneInt > /*2**/numOfLanes) {
							laneInt = /*2**/numOfLanes;
						}
					}
				}
			} else {
				if (!ride.getNextConnections().isEmpty()) {
					ride.removeLastConnection();
				}
				return;
			}
			if (!ride.getNextConnections().isEmpty()) {
				ride.removeLastConnection();
			}
			return;
		} else {
			if (!ride.getNextConnections().isEmpty()) {
				ride.removeLastConnection();
			}
			return;
		}
	}
	
	public void removeLastGoInGoOutConnections(Ride ride) {

		while (ride.getNextConnections().get(ride.getNextConnections().size()-1).getName().equals("GO IN") || ride.getNextConnections().get(ride.getNextConnections().size()-1).getName().equals("GO OUT")) {
			ride.getNextConnections().remove(ride.getNextConnections().size()-1);
		}
	}
	
	public void addExit(String name, int position) {
		exits.add(new Connection(name, position));
	}
	public void addEnter(String name, int position) {
		enters.add(new Connection(name, position));
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Connection> getExits() {
		return exits;
	}

	public ArrayList<Connection> getEnters() {
		return enters;
	}

	
	

}