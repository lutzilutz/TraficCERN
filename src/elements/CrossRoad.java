package elements;

public class CrossRoad {
	
	private RoundAbout middle;
	private Road[] roadsIN;
	private Road[] roadsOUT;

	public CrossRoad() {
		middle = new RoundAbout(4);
		roadsIN = new Road[4];
		roadsOUT = new Road[4];
	}

	public RoundAbout getMiddle() {
		return middle;
	}

	public void setMiddle(RoundAbout middle) {
		this.middle = middle;
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
