package network;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import elements.CrossRoad;
import elements.MultiLaneRoad;
import elements.MultiLaneRoundAbout;
import elements.Road;
import elements.RoundAbout;
import main.Simulation;

public class Network {

	private static String[] titles;
	private static String[] descriptions;
	
	private Simulation sim;
	private ArrayList<Road> roads = new ArrayList<Road>();
	private ArrayList<RoundAbout> roundAbouts = new ArrayList<RoundAbout>();
	private ArrayList<CrossRoad> crossRoads = new ArrayList<CrossRoad>();
	private int cellWidth=10, cellHeight=cellWidth;
	
	private boolean drawWire = true; // true for rendering the border of the cells
	private boolean drawColors = true; // true for rendering color codes (end of road, out cells, ...)
	private boolean drawRoadID = false; // true for rendering roads ID
	private boolean drawCenters = false; // true for rendering centers (x,y position)
	private ArrayList<Polygon> zones = new ArrayList<Polygon>();
	
	private double xOffset=0, yOffset=0; // offset of the network on screen
	private double xDefaultOffset, yDefaultOffset;
	private double rotation=0;
	
	//private String title="", description="";
	
	public Network(Simulation sim, int n) {
		this.setCellHeight(8);
		this.setCellWidth(8);
		this.sim = sim;
		Road.resetID();
		xOffset = 0;
		yOffset = 0;
		xDefaultOffset = 0;
		yDefaultOffset = 0;
	
		switch (n) {
		case 0:
			createTestNetwork1();
			break;
		case 1:
			createTestNetwork2();
			break;
		case 2:
			createRealNetwork();
			break;
		}
		
		titles = new String[3];
		titles[0] = "Test 1";
		titles[1] = "Test 2";
		titles[2] = "CERN network";

		descriptions = new String[3];
		descriptions[0] = "Test network with round-abouts and crossroad";
		descriptions[1] = "Test road for turning roads";
		descriptions[2] = "Actual network around the CERN";
	}
	public void createTestNetwork2() {
		
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
	public void createTestNetwork3() {
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
		RA1.setPositionFrom(ro4,0);
		RA1.setDirection(180+ro4.getDirection());
		this.roundAbouts.add(RA1);
		RA1.connectTo(ri1, 29);
		ro4.connectTo(RA1, 0);
		
		Road rlol = new Road(this, 10);
		//rlol.setDirection(0);
		rlol.setEndPositionFrom(RA1, 10,63);
		this.roads.add(rlol);
		rlol.connectTo(RA1, 4);
		
		RoundAbout RA2 = new RoundAbout(this, 30);
		RA2.setPositionFrom(ro3,0);
		RA2.setDirection(180+ro3.getDirection());
		this.roundAbouts.add(RA2);
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
		
		Polygon tmp = new Polygon();
		tmp.npoints = 4;
		tmp.xpoints[0] = -99;
		tmp.xpoints[1] = 1359;
		tmp.xpoints[2] = 1178;
		tmp.xpoints[3] = -180;
		tmp.ypoints[0] = 12;
		tmp.ypoints[1] = 632;
		tmp.ypoints[2] = 1037;
		tmp.ypoints[3] = 151;
		zones.add(tmp);
		
		int[] tmpX = new int[5];
		int[] tmpY = new int[5];
		tmpX[0] = 722;
		tmpX[1] = 837;
		tmpX[2] = 1464;
		tmpX[3] = 1376;
		tmpX[4] = 750;
		tmpY[0] = 293;
		tmpY[1] = 92;
		tmpY[2] = 379;
		tmpY[3] = 602;
		tmpY[4] = 336;
		Polygon tmp2 = new Polygon(tmpX, tmpY, 5);
		zones.add(tmp2);
		
		// Porte de France
		RoundAbout raPorteDeFrance = new RoundAbout(this, 48);
		raPorteDeFrance.setX(-200);
		raPorteDeFrance.setY(-50);
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
		
		// D984F North --------------------------------------------------------------------------------------
		// S-E (out)
		Road rD984FSE = new Road(this, 110);
		rD984FSE.setStartPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-17);
		rD984FSE.setStartDirection(93);
		//rD984FSE.setDirection(113);
		rD984FSE.addPoint(new Point(4,113));
		rD984FSE.addPoint(new Point(rD984FSE.getLength()-4,97));
		roads.add(rD984FSE);
		raPorteDeFrance.connectTo(rD984FSE, raPorteDeFrance.getLength()-17);
		
		// N-W (in)
		Road rD984FNW = new Road(this, 109);
		rD984FNW.setDirection(274);
		rD984FNW.addPoint(new Point(5,293));
		rD984FNW.addPoint(new Point(105,313));
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
		
		// LHC ----------------------------------------------------------------------------------------------
		RoundAbout raLHC = new RoundAbout(this, 17);
		raLHC.setDirection(0);
		raLHC.setPositionFrom(rD984FSE, 5);
		roundAbouts.add(raLHC);
		rD984FSE.connectTo(raLHC, 5);
		raLHC.connectTo(rD984FNW, 3);
		
		// D984F South --------------------------------------------------------------------------------------
		// S-E (out)
		Road rD984FSES = new Road(this, 91);
		rD984FSES.setStartPositionFrom(raLHC, raLHC.getLength()-7);
		rD984FSES.setStartDirection(129);
		rD984FSES.addPoint(new Point(4,113));
		roads.add(rD984FSES);
		raLHC.connectTo(rD984FSES, raLHC.getLength()-7);
		
		// N-W (in)
		Road rD984FNWS = new Road(this, 91);
		rD984FNWS.setDirection(293);
		rD984FNWS.addPoint(new Point(rD984FNWS.getLength()-5,313));
		rD984FNWS.setEndPositionFrom(raLHC, raLHC.getLength()-5,293);
		roads.add(rD984FNWS);
		rD984FNWS.connectTo(raLHC, raLHC.getLength()-5);
		
		// C5 -----------------------------------------------------------------------------------------------
		// N-E (out)
		Road rC5NE = new Road(this, 30);
		rC5NE.setStartPositionFrom(raLHC, raLHC.getLength()-2);
		rC5NE.setStartDirection(30);
		roads.add(rC5NE);
		raLHC.connectTo(rC5NE, raLHC.getLength()-2);
		
		// S-W (in)
		Road rC5SW = new Road(this, 30);
		rC5SW.setDirection(210);
		//rC5SW.addPoint(new Point(rC5SW.getLength()-7,215));
		rC5SW.setEndPositionFrom(raLHC, raLHC.getLength()-1,293);
		roads.add(rC5SW);
		rC5SW.connectTo(raLHC, raLHC.getLength()-1);
		
		// Tunnel inter-site --------------------------------------------------------------------------------
		// S-E
		Road rTunnelSE = new Road(this, 35);
		rTunnelSE.setStartPositionFrom(rC5NE, 7, 120);
		rTunnelSE.addPoint(new Point(2, 180));
		rTunnelSE.addPoint(new Point(20, 110));
		rTunnelSE.addPoint(new Point(27, 185));
		roads.add(0,rTunnelSE);
		rC5NE.getRoadCells().get(7).setOutCell(rTunnelSE.getRoadCells().get(0));
		rTunnelSE.getRoadCells().get(0).setPreviousCell(rC5NE.getRoadCells().get(7));
		rTunnelSE.setUnderground(14, 16, true);
		
		// N-W
		Road rTunnelNW = new Road(this, 39);
		rTunnelNW.setDirection(5);
		rTunnelNW.addPoint(new Point(9, 290));
		rTunnelNW.addPoint(new Point(16, 0));
		rTunnelNW.setX(813);
		rTunnelNW.setY(475);
		roads.add(0,rTunnelNW);
		rTunnelNW.getRoadCells().get(rTunnelNW.getLength()-1).setNextCell(rC5NE.getRoadCells().get(13));
		rTunnelNW.setUnderground(17, 20, true);
		
		// ##################################################################################################
		// ##################################################################################################
		// Carrefour entree B -------------------------------------------------------------------------------
		CrossRoad crEntreeB = new CrossRoad(this);
		crEntreeB.setX(1400);
		crEntreeB.setY(650);
		crEntreeB.setDirection(0);
		crossRoads.add(crEntreeB);
		crEntreeB.setTimeTrafficLight(20);

		crEntreeB.setPositionFromStart(rD984FNWS, 0);
		crEntreeB.setDirection(rD984FNWS.getDirection());
		Road ri1 = new Road(this, 8);
		ri1.setX(250);
		ri1.setY(250);
		ri1.setDirection(135);
		roads.add(ri1);
		
		ri1.connectTo(crEntreeB, 1);
		ri1.setPositionInFrom(crEntreeB, 1);
		
		Road ri2 = new Road(this, 17);
		//roads.add(ri2);
		ri2.connectTo(crEntreeB, 2);
		ri2.setPositionInFrom(crEntreeB, 2);
		rD984FSES.connectTo(crEntreeB, 2);
		//rD984FSES.setPositionInFrom(crEntreeB, 2);
		
		Road ri3 = new Road(this, 20);
		roads.add(ri3);
		ri3.connectTo(crEntreeB, 3);
		ri3.setPositionInFrom(crEntreeB, 3);
		
		Road ri4 = new Road(this, 8);
		roads.add(ri4);
		ri4.connectTo(crEntreeB, 4);
		ri4.setPositionInFrom(crEntreeB, 4);
		
		//ri1.setGenerateVehicules(true);
		//ri2.setGenerateVehicules(true);
		ri3.setGenerateVehicules(true);
		//ri4.setGenerateVehicules(true);
		
		
		Road ro1 = new Road(this, 17);
		roads.add(ro1);
		crEntreeB.connectTo(ro1, 1);
		ro1.setPositionOutFrom(crEntreeB, 1);
		
		Road ro2 = new Road(this, 20);
		roads.add(ro2);
		crEntreeB.connectTo(ro2, 2);
		ro2.setPositionOutFrom(crEntreeB, 2);
		
		Road ro3 = new Road(this, 8);
		roads.add(ro3);
		crEntreeB.connectTo(ro3, 3);
		ro3.setPositionOutFrom(crEntreeB, 3);
		
		Road ro4 = new Road(this, 8);
		roads.add(ro4);
		crEntreeB.connectTo(ro4, 4);
		ro4.setPositionOutFrom(crEntreeB, 4);
		
		// ##################################################################################################
		// ##################################################################################################
		
		rRueDeGeneveSE.setGenerateVehicules(true);
		rRueGermaineTillionSW.setGenerateVehicules(true);
		rD884NE.setGenerateVehicules(true);
		rSortieCERNNW.setGenerateVehicules(true);
		rD984FNWS.setGenerateVehicules(true);
		rC5SW.setGenerateVehicules(true);
		rTunnelNW.setGenerateVehicules(true);
	}
	public ArrayList<Polygon> getZones() {
		return zones;
	}
	public static String getTitle(int n) {
		return titles[n];
	}
	public static String getDescription(int n) {
		return descriptions[n];
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
