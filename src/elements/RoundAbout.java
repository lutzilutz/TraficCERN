package elements;

import network.Network;

public class RoundAbout extends Road {

	public RoundAbout(Network n, int length) {
		super(n, length);
		this.n = n;
		this.getRoadCells().get(this.getLength()-1).setNextCell(this.getRoadCells().get(0));
		this.getRoadCells().get(0).setPreviousCell(this.getRoadCells().get(this.getLength()-1));
	}
	
	public RoundAbout(Network n, int length, String name) {
		super(n, length, name);
		this.n = n;
		this.getRoadCells().get(this.getLength()-1).setNextCell(this.getRoadCells().get(0));
		this.getRoadCells().get(0).setPreviousCell(this.getRoadCells().get(this.getLength()-1));
	}
	
	public void connectTo(Road r, int i) {
		this.getRoadCells().get(i).setOutCell(r.getRoadCells().get(0));
		r.getRoadCells().get(0).setInCell(getRoadCells().get(i));
		this.addExit(r.getName(), i);
		r.addEnter(this.getName(), 0);
	}
	
	
	public void generateRidesAux(int n, Ride ride) {
		if(n==0) {
			if (this.getRoadCells().get(this.getLength()-1).getNextCell() == null && this.getRoadCells().get(this.getLength()-1).getOutCell() == null) {
				this.n.addARideToAllNetworkRides(ride.clone());
			}
			ride.removeLastConnection();
			return;
		} else if (n > 0) {
			for (Connection e: this.getExits()) {
				for (Road r: this.n.getRoads()) {
					if (e.getName().equals(r.getName())) {
						ride.addNextConnection(e.clone());
						r.generateRidesAux(n-1, ride);
					}
				}
				for (RoundAbout ra: this.n.getRoundAbouts()) {
					if (e.getName().equals(ra.getName())) {
						ride.addNextConnection(e.clone());
						ra.generateRidesAux(n-1, ride);
					}
				}
				for (CrossRoad cr: this.n.getCrossRoads()) {
					if (e.getName().equals(cr.getName())) {
						ride.addNextConnection(e.clone());
						cr.generateRidesAux(n-1, ride);
					}
				}	
			}
			if (this.getRoadCells().get(this.getLength()-1).getNextCell() == null && this.getRoadCells().get(this.getLength()-1).getOutCell() == null) {
				this.n.addARideToAllNetworkRides(ride.clone());
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
	
	public void setPositionFrom(Road r, int i) {
		double x = (int) (r.getX());
		double y = (int) (r.getY());
		double last = 0;
		for (int j=0 ; j<r.getReorientations().size() ; j++) {
			// Last segment
			if (j == r.getReorientations().size()-1) {
				x += n.getCellWidth() * (r.getLength()+0.5-r.getReorientations().get(j).getX()) * Math.sin(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
				y += - n.getCellWidth() * (r.getLength()+0.5-r.getReorientations().get(j).getX()) * Math.cos(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
			}
			// All others
			else {
				x += n.getCellWidth() * (r.getReorientations().get(j+1).getX()-last) * Math.sin(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
				y += - n.getCellWidth() * (r.getReorientations().get(j+1).getX()-last) * Math.cos(2*Math.PI*r.getReorientations().get(j).getY()/360.0);
				last = r.getReorientations().get(j+1).getX();
			}
		}
		
		x += (0*n.getCellWidth() + n.getCellWidth()*this.getLength()/(2*Math.PI))*Math.sin(2*Math.PI*(i/(float)this.getLength()));
		y += (0*n.getCellWidth() + n.getCellWidth()*this.getLength()/(2*Math.PI))*Math.cos(2*Math.PI*(i/(float)this.getLength()));
		//x = (int) (r.getX() + n.getCellWidth() * (r.getReorientations().get(1).getX()-r.getReorientations().get(0).getX()) * Math.sin(2*Math.PI*r.getReorientations().get(0).getY()/360.0 + Math.PI));
		//y = (int) (r.getY());
		
		this.setX(x);
		this.setY(y);
	}
}
