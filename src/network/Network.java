package network;

import java.awt.Point;
import java.util.ArrayList;

import elements.CrossRoad;
import elements.MultiLaneRoad;
import elements.MultiLaneRoundAbout;
import elements.Road;
import elements.RoundAbout;
import main.Simulation;

public class Network {

	private Simulation sim;
	private ArrayList<Road> roads = new ArrayList<Road>();
	private ArrayList<RoundAbout> roundAbouts = new ArrayList<RoundAbout>();
	private ArrayList<CrossRoad> crossRoads = new ArrayList<CrossRoad>();
	private int cellWidth=10, cellHeight=cellWidth;
	
	private boolean drawWire = true; // true for rendering the border of the cells
	private boolean drawColors = true; // true for rendering color codes (end of road, out cells, ...)
	private boolean drawRoadID = false; // true for rendering roads ID
	private boolean drawCenters = false; // true for rendering centers (x,y position)
	
	private double xOffset=0, yOffset=0; // offset of the network on screen
	private double xDefaultOffset, yDefaultOffset;
	private double rotation=0;
	
	public Network(Simulation sim, int n) {
		this.setCellHeight(8);
		this.setCellWidth(8);
		
		this.sim = sim;
		
		xOffset = 0;
		yOffset = 0;
		xDefaultOffset = 0;
		yDefaultOffset = 0;
		
		switch (n) {
		case 1:
			createTestNetwork1();
		case 2:
			createTestNetwork2();
		case 3:
			createTestNetwork3();
		case 4:
			createRealNetwork();
		}
	}
	public void createTestNetwork3() {
		Road test = new Road(this, 50);
		test.setDirection(110);
		test.addPoint(new Point(18,90));
		test.addPoint(new Point(23,70));
		test.addPoint(new Point(27,60));
		test.addPoint(new Point(30,270));
		test.addPoint(new Point(35,300));
		test.addPoint(new Point(45,270));
		test.setX(200);
		test.setY(400);
		roads.add(test);
		test.setGenerateVehicules(true);
		
		Road test2 = new Road(this, 10);
		test2.setDirection(270);
		test2.setX(400);
		test2.setY(500);
		roads.add(test2);
		test2.setGenerateVehicules(true);
		
	}
	public void createTestNetwork2() {
		MultiLaneRoundAbout porteDeFrance = new MultiLaneRoundAbout(this, 2, 50);
		porteDeFrance.setX(400);
		porteDeFrance.setY(400);
		for (int i=0; i<porteDeFrance.getLanes().length; ++i) {
			this.roundAbouts.add(porteDeFrance.getLanes()[i]);
		}
		
		MultiLaneRoad rueGermaineTillion = new MultiLaneRoad(this, 2, 1, 200);
		rueGermaineTillion.connectToIn(porteDeFrance, 46);
		rueGermaineTillion.setPositionFrom(porteDeFrance, 46);
		for (int i=0; i<rueGermaineTillion.getLanesIN().length; ++i) {
			this.roads.add(rueGermaineTillion.getLanesIN()[i]);
			rueGermaineTillion.getLanesIN()[i].setGenerateVehicules(true);
		}
		for (int i=0; i<rueGermaineTillion.getLanesOUT().length; ++i) {
			this.roads.add(rueGermaineTillion.getLanesOUT()[i]);
		}
		
		MultiLaneRoad rueDeGeneveE = new MultiLaneRoad(this, 1, 1, 29);
		rueDeGeneveE.connectToIn(porteDeFrance, 10);
		rueDeGeneveE.setPositionFrom(porteDeFrance, 10);
		for (int i=0; i<rueDeGeneveE.getLanesIN().length; ++i) {
			this.roads.add(rueDeGeneveE.getLanesIN()[i]);
		}
		for (int i=0; i<rueDeGeneveE.getLanesOUT().length; ++i) {
			this.roads.add(rueDeGeneveE.getLanesOUT()[i]);
		}
		
		MultiLaneRoundAbout rondPointRueDeGeneveE = new MultiLaneRoundAbout(this, 1, 13);
		rondPointRueDeGeneveE.setPositionOUTFrom(rueDeGeneveE);
		rondPointRueDeGeneveE.connectToOut(rueDeGeneveE, 9);
		for (int i=0; i<rondPointRueDeGeneveE.getLanes().length; ++i) {
			this.roundAbouts.add(rondPointRueDeGeneveE.getLanes()[i]);
		}
		
		MultiLaneRoad routeDeMeyrin = new MultiLaneRoad(this, 1, 1, 100);
		routeDeMeyrin.connectToIn(porteDeFrance, 35);
		routeDeMeyrin.setPositionFrom(porteDeFrance, 35);
		for (int i=0; i<routeDeMeyrin.getLanesIN().length; ++i) {
			this.roads.add(routeDeMeyrin.getLanesIN()[i]);
			routeDeMeyrin.getLanesIN()[i].setGenerateVehicules(true);
		}
		for (int i=0; i<routeDeMeyrin.getLanesOUT().length; ++i) {
			this.roads.add(routeDeMeyrin.getLanesOUT()[i]);
		}
	}
	
	public void createTestNetwork1() {
		CrossRoad CR = new CrossRoad(this);
		CR.setX(300);
		CR.setY(300);
		CR.setDirection(110);
		crossRoads.add(CR);

		Road ri1 = new Road(this, 8);
		ri1.setX(250);
		ri1.setY(250);
		ri1.setDirection(135);
		roads.add(ri1);
		
		ri1.connectTo(CR, 1);
		ri1.setPositionInFrom(CR, 1);
		
		Road ri2 = new Road(this, 17);
		roads.add(ri2);
		ri2.connectTo(CR, 2);
		ri2.setPositionInFrom(CR, 2);
		
		Road ri3 = new Road(this, 20);
		roads.add(ri3);
		ri3.connectTo(CR, 3);
		ri3.setPositionInFrom(CR, 3);
		
		Road ri4 = new Road(this, 8);
		roads.add(ri4);
		ri4.connectTo(CR, 4);
		ri4.setPositionInFrom(CR, 4);
		
		//ri1.setGenerateVehicules(true);
		ri2.setGenerateVehicules(true);
		ri3.setGenerateVehicules(true);
		//ri4.setGenerateVehicules(true);
		
		
		Road ro1 = new Road(this, 17);
		roads.add(ro1);
		CR.connectTo(ro1, 1);
		ro1.setPositionOutFrom(CR, 1);
		
		Road ro2 = new Road(this, 20);
		roads.add(ro2);
		CR.connectTo(ro2, 2);
		ro2.setPositionOutFrom(CR, 2);
		
		Road ro3 = new Road(this, 8);
		roads.add(ro3);
		CR.connectTo(ro3, 3);
		ro3.setPositionOutFrom(CR, 3);
		
		Road ro4 = new Road(this, 8);
		roads.add(ro4);
		CR.connectTo(ro4, 4);
		ro4.setPositionOutFrom(CR, 4);
		
		RoundAbout RA1 = new RoundAbout(this, 30);
		RA1.setPositionFrom(ro4);
		RA1.setDirection(180+ro4.getDirection());
		this.roundAbouts.add(RA1);
		
		Road rlol = new Road(this, 10);
		rlol.setEndPositionFrom(RA1, 4);
		this.roads.add(rlol);
		
		RoundAbout RA2 = new RoundAbout(this, 30);
		RA2.setPositionFrom(ro3);
		RA2.setDirection(180+ro3.getDirection());
		this.roundAbouts.add(RA2);
		
		ro4.connectTo(RA1, 0);
		RA1.connectTo(ri1, 29);
		rlol.connectTo(RA1, 4);
		ro3.connectTo(RA2, 0);
		RA2.connectTo(ri4, 29);
		RA2.connectTo(rlol, 25);
		
		Road R = new Road(this, 17);
		R.setStartPositionFrom(RA1, 25);
		RA1.connectTo(R, 25);
		this.roads.add(R);
		//CR.setTimeTrafficLight(20);
	}
	
	public void createRealNetwork() {
		
		// Porte de France
		RoundAbout raPorteDeFrance = new RoundAbout(this, 48);
		raPorteDeFrance.setX(250);
		raPorteDeFrance.setY(300);
		raPorteDeFrance.setDirection(0);
		roundAbouts.add(raPorteDeFrance);
		
		// Rue de Genève ------------------------------------------------------------------------------------
		// N-W (out)
		Road rRueDeGeneveNW = new Road(this, 15);
		rRueDeGeneveNW.setStartPositionFrom(raPorteDeFrance, 7);
		rRueDeGeneveNW.setDirection(271);
		rRueDeGeneveNW.addPoint(new Point(4,291));
		roads.add(rRueDeGeneveNW);
		raPorteDeFrance.connectTo(rRueDeGeneveNW, 7);
		
		// S-E (in)
		Road rRueDeGeneveSE = new Road(this, 15);
		rRueDeGeneveSE.setDirection(111);
		rRueDeGeneveSE.addPoint(new Point(11, 131));
		rRueDeGeneveSE.setEndPositionFrom(raPorteDeFrance,11,111);
		roads.add(rRueDeGeneveSE);
		rRueDeGeneveSE.connectTo(raPorteDeFrance, 11);
		
		// Rue Germaine Tillion -----------------------------------------------------------------------------
		// N-E (out)
		Road rRueGermaineTillionNE = new Road(this, 25);
		rRueGermaineTillionNE.setStartPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-8);
		rRueGermaineTillionNE.setDirection(24);
		rRueGermaineTillionNE.addPoint(new Point(10, 38));
		roads.add(rRueGermaineTillionNE);
		raPorteDeFrance.connectTo(rRueGermaineTillionNE, raPorteDeFrance.getLength()-8);
		
		// S-W (in)
		Road rRueGermaineTillionSW = new Road(this, 25);
		rRueGermaineTillionSW.setDirection(218);
		rRueGermaineTillionSW.addPoint(new Point(15,232));
		rRueGermaineTillionSW.setEndPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-2,230);
		roads.add(rRueGermaineTillionSW);
		rRueGermaineTillionSW.connectTo(raPorteDeFrance, raPorteDeFrance.getLength()-2);
		
		// D984F --------------------------------------------------------------------------------------------
		// S-E (out)
		Road rD984FSE = new Road(this, 205);
		rD984FSE.setStartPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-17);
		//rD984FSE.setDirection(113);
		rD984FSE.setDirection(93);
		rD984FSE.addPoint(new Point(4,113));
		
		roads.add(rD984FSE);
		raPorteDeFrance.connectTo(rD984FSE, raPorteDeFrance.getLength()-17);
		
		// N-W (in)
		Road rD984FNW = new Road(this, 205);
		rD984FNW.setDirection(293);
		rD984FNW.addPoint(new Point(201,313));
		rD984FNW.setEndPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-13,293);
		
		roads.add(rD984FNW);
		rD984FNW.connectTo(raPorteDeFrance, raPorteDeFrance.getLength()-13);
		
		// D884 --------------------------------------------------------------------------------------------
		// S-W (out)
		Road rD884SW = new Road(this, 15);
		rD884SW.setStartPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-32);
		rD884SW.setDirection(198);
		rD884SW.addPoint(new Point(2, 218));
		roads.add(rD884SW);
		raPorteDeFrance.connectTo(rD884SW, raPorteDeFrance.getLength()-32);
		
		// N-E (in)
		Road rD884NE = new Road(this, 15);
		rD884NE.setDirection(38);
		rD884NE.addPoint(new Point(13,58));
		rD884NE.setEndPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-26,38);
		roads.add(rD884NE);
		rD884NE.connectTo(raPorteDeFrance,  raPorteDeFrance.getLength()-26);
		
		// SortieCERN ---------------------------------------------------------------------------------------
		// S-E (out)
		Road rSortieCERNSE = new Road(this, 15);
		rSortieCERNSE.setStartPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-23);
		rSortieCERNSE.setDirection(150);
		rSortieCERNSE.addPoint(new Point(3,170));
		rSortieCERNSE.addPoint(new Point(10,150));
		roads.add(rSortieCERNSE);
		raPorteDeFrance.connectTo(rSortieCERNSE, raPorteDeFrance.getLength()-23);
		
		// N-W (in)
		Road rSortieCERNNW = new Road(this, 15);
		rSortieCERNNW.setDirection(330);
		rSortieCERNNW.addPoint(new Point(5,350));
		rSortieCERNNW.addPoint(new Point(12,5));
		rSortieCERNNW.setEndPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-20,330);
		roads.add(rSortieCERNNW);
		rSortieCERNNW.connectTo(raPorteDeFrance,  raPorteDeFrance.getLength()-20);
		
		rRueDeGeneveSE.setGenerateVehicules(true);
		rRueGermaineTillionSW.setGenerateVehicules(true);
		rD984FNW.setGenerateVehicules(true);
		rD884NE.setGenerateVehicules(true);
		rSortieCERNNW.setGenerateVehicules(true);
	}
	public double getRotation() {
		return rotation;
	}
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	public double getxDefaultOffset() {
		return xDefaultOffset;
	}
	public void setxDefaultOffset(double xDefaultOffset) {
		this.xDefaultOffset = xDefaultOffset;
	}
	public double getyDefaultOffset() {
		return yDefaultOffset;
	}
	public void setyDefaultOffset(double yDefaultOffset) {
		this.yDefaultOffset = yDefaultOffset;
	}
	public double getxOffset() {
		return xOffset;
	}
	public void setxOffset(double xOffset) {
		this.xOffset = xOffset;
	}
	public double getyOffset() {
		return yOffset;
	}
	public void setyOffset(double yOffset) {
		this.yOffset = yOffset;
	}
	public void switchDrawWire() {
		drawWire = !drawWire;
	}
	public void switchDrawColors() {
		drawColors = !drawColors;
	}
	public void switchDrawRoadID() {
		drawRoadID = !drawRoadID;
	}
	public void switchDrawCenters() {
		drawCenters = !drawCenters;
	}
	public Simulation getSimulation() {
		return this.sim;
	}
	public ArrayList<CrossRoad> getCrossRoads() {
		return this.crossRoads;
	}
	public ArrayList<Road> getRoads() {
		return this.roads;
	}
	public ArrayList<RoundAbout> getRoundAbouts() {
		return this.roundAbouts;
	}
	public boolean getDrawWire() {
		return this.drawWire;
	}
	public boolean getDrawColors() {
		return this.drawColors;
	}
	public boolean getDrawRoadID() {
		return this.drawRoadID;
	}
	public boolean getDrawCenters() {
		return this.drawCenters;
	}
	public void addRoadtoRoads(Road r) {
		this.roads.add(r);
	}
	public int getCellWidth() {
		return cellWidth;
	}
	public void setCellWidth(int cellWidth) {
		this.cellWidth = cellWidth;
	}
	public int getCellHeight() {
		return cellHeight;
	}
	public void setCellHeight(int cellHeight) {
		this.cellHeight = cellHeight;
	}
}
