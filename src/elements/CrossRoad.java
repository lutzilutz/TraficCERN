package elements;

import main.Network;

public class CrossRoad {
	private Road[] roadsIN;
	private Road[] roadsOUT;

	public CrossRoad(Network n) {
		roadsIN = new Road[4];
		roadsOUT = new Road[4];
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
	

}
