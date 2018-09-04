package network;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import elements.Connection;
import elements.CrossRoad;
import elements.MultiLaneRoundAbout;
import elements.Ride;
import elements.Road;
import elements.RoundAbout;
import elements.Vehicle;
import main.Simulation;
import utils.Utils;

public class Network {

	private static String[] titles;
	private static String[] descriptions;
	
	private Simulation sim;
	private int n;
	private ArrayList<Road> roads = new ArrayList<Road>();
	private ArrayList<RoundAbout> roundAbouts = new ArrayList<RoundAbout>();
	private ArrayList<CrossRoad> crossRoads = new ArrayList<CrossRoad>();
	private ArrayList<AllNetworkRides> allNetworkRides = new ArrayList<AllNetworkRides>();
	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	private int numberOfVehicles = 0;
	private int cellWidth=10, cellHeight=cellWidth;
	
	private boolean drawWire = true; // true for rendering the border of the cells
	private boolean drawColors = true; // true for rendering color codes (end of road, out cells, ...)
	private boolean drawRoadID = false; // true for rendering roads ID
	private boolean drawNames = false; // true for rendering names of road
	private boolean drawCenters = false; // true for rendering centers (x,y position)
	private ArrayList<Polygon> zones = new ArrayList<Polygon>();
	
	private double xOffset=0, yOffset=0; // offset of the network on screen
	private double xDefaultOffset, yDefaultOffset;
	private double rotation=0;
	
	private int maxSpeed = 2;
	
	// Probabilities (data)
	private int fromFrToGe = 12500;
	private double fromFrToGeFrom7To10 = 0.68;
	private double fromStGenisToGe = 0.6;
	private double fromFerneyToGe = 0.2;
	
	public Network(Simulation sim, int n) {
		this.setCellHeight(6);
		this.setCellWidth(6);
		this.sim = sim;
		this.n = n;
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
		titles[0] = "CERN network WIP";
		titles[1] = "WIP on RoundAbout";
		titles[2] = "CERN network";

		descriptions = new String[3];
		descriptions[0] = "Work in progress of actual network";
		descriptions[1] = "Work in progress of multi-lanes RoundAbouts";
		descriptions[2] = "Actual network around the CERN";
	}
	public void createTestNetwork2() {
		/*
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
		test2.setGenerateVehicules(true); */
		
		MultiLaneRoundAbout MLRA = new MultiLaneRoundAbout(this, 3, 48, "RAPDF");
		MLRA.setX(400);
		MLRA.setY(400);

		for (RoundAbout RA: MLRA.getLanes()) {
			this.roundAbouts.add(RA);
		}
		
		Road rIN1 = new Road (this, 15, "rIN1");
		rIN1.connectTo(MLRA.getLanes()[0], 5);
		rIN1.setEndPositionFrom(MLRA.getLanes()[0], 5, 180);
		this.roads.add(rIN1);
		rIN1.setGenerateVehicules(50);
		
		
		System.out.println("");
		
	}
	public void createTestNetwork3() {
		/*
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
		*/
	}
	
	public void createTestNetwork1() {
		
		Polygon tmp = new Polygon();
		tmp.npoints = 4;
		tmp.xpoints[0] = 13*cellWidth;
		tmp.xpoints[1] = 235*cellWidth;
		tmp.xpoints[2] = 212*cellWidth;
		tmp.xpoints[3] = 3*cellWidth;
		tmp.ypoints[0] = 8*cellWidth;
		tmp.ypoints[1] = 102*cellWidth;
		tmp.ypoints[2] = 150*cellWidth;
		tmp.ypoints[3] = 25*cellWidth;
		zones.add(tmp);
		
		int[] tmpX = new int[5];
		int[] tmpY = new int[5];
		tmpX[0] = 115*cellWidth;//722;
		tmpX[1] = 131*cellWidth;//837;
		tmpX[2] = 250*cellWidth;//1464;
		tmpX[3] = 236*cellWidth;//1376;
		tmpX[4] = 119*cellWidth;//750;
		tmpY[0] = 43*cellWidth;//293;
		tmpY[1] = 17*cellWidth;//92;
		tmpY[2] = 60*cellWidth;//379;
		tmpY[3] = 98*cellWidth;//602;
		tmpY[4] = 48*cellWidth;//336;
		Polygon tmp2 = new Polygon(tmpX, tmpY, 5);
		zones.add(tmp2);
		
		// Porte de France
		RoundAbout raPorteDeFrance = new RoundAbout(this, 48, "raPorteDeFrance");
		raPorteDeFrance.setX(0);
		raPorteDeFrance.setY(0);
		raPorteDeFrance.setDirection(0);
		roundAbouts.add(raPorteDeFrance);
		
		// Rue de Genève ----------------------------------------------------------------------------------------------
		// N-W (out)
		Road rRueDeGeneveNW = new Road(this, 15, "rRueDeGeneveNW");
		rRueDeGeneveNW.setStartPositionFrom(raPorteDeFrance, 7);
		rRueDeGeneveNW.setDirection(271);
		rRueDeGeneveNW.addPoint(new Point(4,291));
		roads.add(rRueDeGeneveNW);
		raPorteDeFrance.connectTo(rRueDeGeneveNW, 7);
		
		// S-E (in)
		Road rRueDeGeneveSE = new Road(this, 15, "rRueDeGeneveSE");
		rRueDeGeneveSE.setDirection(111);
		rRueDeGeneveSE.addPoint(new Point(11, 131));
		rRueDeGeneveSE.setEndPositionFrom(raPorteDeFrance,11,111);
		roads.add(rRueDeGeneveSE);
		rRueDeGeneveSE.connectTo(raPorteDeFrance, 11);
		
		// Rue Germaine Tillion ---------------------------------------------------------------------------------------
		// N-E (out)
		Road rRueGermaineTillionNE = new Road(this, 25, "rRueGermaineTillionNE");
		rRueGermaineTillionNE.setStartPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-8);
		rRueGermaineTillionNE.setDirection(24);
		rRueGermaineTillionNE.addPoint(new Point(10, 38));
		roads.add(rRueGermaineTillionNE);
		raPorteDeFrance.connectTo(rRueGermaineTillionNE, raPorteDeFrance.getLength()-8);
		
		// S-W (in)
		Road rRueGermaineTillionSW = new Road(this, 25, "rRueGermaineTillionSW");
		rRueGermaineTillionSW.setDirection(218);
		rRueGermaineTillionSW.addPoint(new Point(15,232));
		rRueGermaineTillionSW.setEndPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-2,230);
		roads.add(rRueGermaineTillionSW);
		rRueGermaineTillionSW.connectTo(raPorteDeFrance, raPorteDeFrance.getLength()-2);
		
		// D984F North ------------------------------------------------------------------------------------------------
		// S-E (out)
		Road rD984FSE = new Road(this, 110, "rD984FSE");
		rD984FSE.setStartPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-17);
		rD984FSE.setStartDirection(93);
		//rD984FSE.setDirection(113);
		rD984FSE.addPoint(new Point(4,113));
		rD984FSE.addPoint(new Point(rD984FSE.getLength()-4,97));
		roads.add(rD984FSE);
		raPorteDeFrance.connectTo(rD984FSE, raPorteDeFrance.getLength()-18);
		
		// N-W (in)
		Road rD984FNW = new Road(this, 109, "rD984FNW");
		rD984FNW.setDirection(274);
		rD984FNW.addPoint(new Point(5,293));
		rD984FNW.addPoint(new Point(105,313));
		rD984FNW.setEndPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-13,293);
		
		roads.add(rD984FNW);
		rD984FNW.connectTo(raPorteDeFrance, raPorteDeFrance.getLength()-13);
		
		// D884 ------------------------------------------------------------------------------------------------------
		// S-W (out)
		Road rD884SW = new Road(this, 15, "rD884SW");
		rD884SW.setStartPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-32);
		rD884SW.setDirection(198);
		rD884SW.addPoint(new Point(2, 218));
		roads.add(rD884SW);
		raPorteDeFrance.connectTo(rD884SW, raPorteDeFrance.getLength()-32);
		
		// N-E (in)
		Road rD884NE = new Road(this, 15, "rD884NE");
		rD884NE.setDirection(38);
		rD884NE.addPoint(new Point(13,58));
		rD884NE.setEndPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-26,38);
		roads.add(rD884NE);
		rD884NE.connectTo(raPorteDeFrance,  raPorteDeFrance.getLength()-26);
		
		// SortieCERN -------------------------------------------------------------------------------------------------
		// S-E (out)
		Road rSortieCERNSE = new Road(this, 15, "rSortieCERNSE");
		rSortieCERNSE.setStartPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-23);
		rSortieCERNSE.setDirection(150);
		rSortieCERNSE.addPoint(new Point(3,170));
		rSortieCERNSE.addPoint(new Point(10,150));
		roads.add(rSortieCERNSE);
		raPorteDeFrance.connectTo(rSortieCERNSE, raPorteDeFrance.getLength()-23);
		rSortieCERNSE.setMaxOutflow(8);
		
		// N-W (in)
		Road rSortieCERNNW = new Road(this, 15, "rSortieCERNNW");
		rSortieCERNNW.setDirection(330);
		rSortieCERNNW.addPoint(new Point(5,350));
		rSortieCERNNW.addPoint(new Point(12,5));
		rSortieCERNNW.setEndPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-20,330);
		roads.add(rSortieCERNNW);
		rSortieCERNNW.connectTo(raPorteDeFrance,  raPorteDeFrance.getLength()-20);
		
		// D884CERN
		Road rD884CERN = new Road(this, 26, "rD884CERN");
		rD884CERN.setDirection(38);
		rD884CERN.addPoint(new Point(8,90));
		rD884CERN.addPoint(new Point(14,170));
		rD884CERN.addPoint(new Point(20,150));
		rD884CERN.setX(rD884NE.getX()+getCellWidth()*(Math.cos(2*Math.PI*rD884NE.getDirection()/360.0) + 2*Math.sin(2*Math.PI*rD884NE.getDirection()/360.0)));
		rD884CERN.setY(rD884NE.getY()+getCellWidth()*(Math.sin(2*Math.PI*rD884NE.getDirection()/360.0) - 2*Math.cos(2*Math.PI*rD884NE.getDirection()/360.0)));
		roads.add(rD884CERN);
		rD884NE.getRoadCells().get(2).setOutCell(rD884CERN.getRoadCells().get(0));
		rD884NE.addExit("rD884CERN", 2);
		rD884CERN.getRoadCells().get(0).setPreviousCell(rD884NE.getRoadCells().get(2));
		rD884CERN.addEnter("rD884NE", 0);
		rD884CERN.setMaxOutflow(40);
		
		// LHC --------------------------------------------------------------------------------------------------------
		RoundAbout raLHC = new RoundAbout(this, 17, "raLHC");
		raLHC.setDirection(0);
		raLHC.setPositionFrom(rD984FSE, 5);
		roundAbouts.add(raLHC);
		rD984FSE.connectTo(raLHC, 5);
		raLHC.connectTo(rD984FNW, 3);
		
		// D984F South ------------------------------------------------------------------------------------------------
		// S-E (out)
		Road rD984FSES = new Road(this, 91, "rD984FSES");
		rD984FSES.setStartPositionFrom(raLHC, raLHC.getLength()-7);
		rD984FSES.setStartDirection(129);
		rD984FSES.addPoint(new Point(4,113));
		roads.add(rD984FSES);
		raLHC.connectTo(rD984FSES, raLHC.getLength()-7);
		
		Road rD984FSES2 = new Road(this, 46, "rD984FSES2");
		rD984FSES2.setStartPositionFrom(rD984FSES, 45, 113, 1, (113+90));
		//rD984FSES.setStartPositionFrom(raLHC, raLHC.getLength()-7);
		//rD984FSES.setStartDirection(129);
		//rD984FSES2.addPoint(new Point(4,113));
		roads.add(rD984FSES2);
		//raLHC.connectTo(rD984FSES, raLHC.getLength()-7);
		
		// N-W (in)
		Road rD984FNWS = new Road(this, 91, "rD984FNWS");
		rD984FNWS.setDirection(293);
		rD984FNWS.addPoint(new Point(rD984FNWS.getLength()-5,313));
		rD984FNWS.setEndPositionFrom(raLHC, raLHC.getLength()-5,293);
		roads.add(rD984FNWS);
		rD984FNWS.connectTo(raLHC, raLHC.getLength()-5);
		
		Road rD984FNWS2 = new Road(this, 46, "rD984FNWS2");
		rD984FNWS2.setDirection(293);
		rD984FNWS2.setStartPositionFrom(rD984FNWS, 0, 293, 1, 293-270); // 293+90 == 293-270
		//rD984FNWS2.setX(rD984FNWS.getX());
		//rD984FNWS2.setY(rD984FNWS.getY()-this.getCellHeight());
		//rD984FNWS2.setEndPositionFrom(raLHC, raLHC.getLength()-4,293);
		roads.add(rD984FNWS2);
		rD984FNWS2.connectTo(rD984FNWS, 46);
		
		
		
		// C5 ---------------------------------------------------------------------------------------------------------
		// N-E (out)
		Road rC5NE = new Road(this, 30, "rC5NE");
		rC5NE.setStartPositionFrom(raLHC, raLHC.getLength()-2);
		rC5NE.setStartDirection(30);
		roads.add(rC5NE);
		raLHC.connectTo(rC5NE, raLHC.getLength()-2);
		
		// S-W (in)
		Road rC5SW = new Road(this, 30, "rC5SW");
		rC5SW.setDirection(210);
		//rC5SW.addPoint(new Point(rC5SW.getLength()-7,215));
		rC5SW.setEndPositionFrom(raLHC, raLHC.getLength()-1,293);
		roads.add(rC5SW);
		rC5SW.connectTo(raLHC, raLHC.getLength()-1);
		
		// Tunnel inter-site ------------------------------------------------------------------------------------------
		// S-E
		Road rTunnelSE = new Road(this, 35, "rTunnelSE");
		rTunnelSE.setStartPositionFrom(rC5NE, 7, 120);
		rTunnelSE.addPoint(new Point(2, 180));
		rTunnelSE.addPoint(new Point(20, 110));
		rTunnelSE.addPoint(new Point(27, 185));
		roads.add(0,rTunnelSE);
		rC5NE.getRoadCells().get(7).setOutCell(rTunnelSE.getRoadCells().get(0));
		rC5NE.addExit("rTunnelSE", 7);
		rTunnelSE.addEnter("rC5NE", 0);
		rTunnelSE.getRoadCells().get(0).setPreviousCell(rC5NE.getRoadCells().get(7));
		rTunnelSE.setUnderground(14, 16, true);
		
		// N-W
		Road rTunnelNW = new Road(this, 39, "rTunnelNW");
		rTunnelNW.setDirection(5);
		rTunnelNW.addPoint(new Point(9, 290));
		rTunnelNW.addPoint(new Point(16, 0));
		rTunnelNW.setX(rTunnelSE.getX()+9*cellWidth);
		rTunnelNW.setY(rTunnelSE.getY()+29*cellWidth);
		roads.add(0,rTunnelNW);
		rTunnelNW.getRoadCells().get(rTunnelNW.getLength()-1).setOutCell(rC5NE.getRoadCells().get(13));
		rTunnelNW.addExit(rC5NE.getName(), rTunnelNW.getLength()-1);
		rC5NE.addEnter("rTunnelNW", 13);
		rTunnelNW.setUnderground(17, 20, true);
		
		/*
		
		// Carrefour entree B -----------------------------------------------------------------------------------------
		CrossRoad crEntreeB = new CrossRoad(this, "crEntreeB");
		crEntreeB.setX(1400);
		crEntreeB.setY(650);
		crEntreeB.setDirection(0);
		crossRoads.add(crEntreeB);
		crEntreeB.setTimeTrafficLight(20);
		crEntreeB.setPositionFromOut(rD984FNWS, 0);
		crEntreeB.setDirection(rD984FNWS.getDirection());
		crEntreeB.connectTo(rD984FNWS, 0);
		rD984FSES.connectTo(crEntreeB, 1);
		crEntreeB.addRoadIn(rD984FSES, 1);
		
		// Route Pauli ------------------------------------------------------------------------------------------------
		// North ------------------------------------------------------------------------------------------------------
		Road rRoutePauliNorthSW = new Road(this, 15, "rRoutePauliNorthSW");
		rRoutePauliNorthSW.setPositionInFrom(crEntreeB, 0);
		roads.add(rRoutePauliNorthSW);
		rRoutePauliNorthSW.connectTo(crEntreeB, 0);
		
		Road rRoutePauliNorthNE = new Road(this, 15, "rRoutePauliNorthNE");
		rRoutePauliNorthNE.setPositionOutFrom(crEntreeB, 3);
		crEntreeB.connectTo(rRoutePauliNorthNE, 3);
		roads.add(rRoutePauliNorthNE);
		
		// South ------------------------------------------------------------------------------------------------------
		Road rRoutePauliSouthNE = new Road(this, 15, "rRoutePauliSouthNE");
		rRoutePauliSouthNE.setPositionInFrom(crEntreeB, 2);
		rRoutePauliSouthNE.connectTo(crEntreeB, 2);
		roads.add(rRoutePauliSouthNE);
		
		Road rRoutePauliSouthSW = new Road(this, 15, "rRoutePauliSouthSW");
		rRoutePauliSouthSW.setPositionOutFrom(crEntreeB, 1);
		crEntreeB.connectTo(rRoutePauliSouthSW, 1);
		roads.add(rRoutePauliSouthSW);
		
		// Route de Meyrin NORTH---------------------------------------------------------------------------------------
		Road rRouteDeMeyrinNorthNW = new Road(this, 33, "rRouteDeMeyrinNorthNW");
		rRouteDeMeyrinNorthNW.setPositionInFrom(crEntreeB, 3);
		rRouteDeMeyrinNorthNW.connectTo(crEntreeB, 3);
		roads.add(rRouteDeMeyrinNorthNW);
		
		Road rRouteDeMeyrinNorthSE = new Road(this, 33, "rRouteDeMeyrinNorthSE");
		rRouteDeMeyrinNorthSE.setPositionOutFrom(crEntreeB, 2);
		crEntreeB.connectTo(rRouteDeMeyrinNorthSE, 2);
		roads.add(rRouteDeMeyrinNorthSE);
		
		// RA entree A ------------------------------------------------------------------------------------------------
		RoundAbout raEntreeA = new RoundAbout(this, 15, "raEntreeA");
		raEntreeA.setDirection(0);
		raEntreeA.setPositionFrom(rRouteDeMeyrinNorthSE, 3);
		roundAbouts.add(raEntreeA);
		rRouteDeMeyrinNorthSE.connectTo(raEntreeA, 3);
		raEntreeA.connectTo(rRouteDeMeyrinNorthNW, 2);
		
		// Chemin de Maisonnex ----------------------------------------------------------------------------------------
		Road rCheminMaisonnexN = new Road(this, 5, "rCheminMaisonnexN");
		rCheminMaisonnexN.setStartPositionFrom(raEntreeA, raEntreeA.getLength()-1);
		rCheminMaisonnexN.setStartDirection(15);
		roads.add(rCheminMaisonnexN);
		raEntreeA.connectTo(rCheminMaisonnexN, raEntreeA.getLength()-1);
		
		Road rCheminMaisonnexS = new Road(this, 5, "rCheminMaisonnexS");
		rCheminMaisonnexS.setDirection(195);
		rCheminMaisonnexS.setEndPositionFrom(raEntreeA, 0,195);
		roads.add(rCheminMaisonnexS);
		rCheminMaisonnexS.connectTo(raEntreeA, 0);
		
		// Route de Meyrin SOUTH---------------------------------------------------------------------------------------
		Road rRouteDeMeyrinSouthSE = new Road(this, 20, "rRouteDeMeyrinSouthSE");
		rRouteDeMeyrinSouthSE.setStartPositionFrom(raEntreeA, raEntreeA.getLength()-5);
		rRouteDeMeyrinSouthSE.setStartDirection(113);
		roads.add(rRouteDeMeyrinSouthSE);
		raEntreeA.connectTo(rRouteDeMeyrinSouthSE, raEntreeA.getLength()-5);
		
		Road rRouteDeMeyrinSouthNW = new Road(this, 20, "rRouteDeMeyrinSouthNW");
		rRouteDeMeyrinSouthNW.setDirection(293);
		rRouteDeMeyrinSouthNW.setEndPositionFrom(raEntreeA, raEntreeA.getLength()-4,293);
		roads.add(rRouteDeMeyrinSouthNW);
		rRouteDeMeyrinSouthNW.connectTo(raEntreeA, 3);
		
		// Route Bell---------------------------------------------------------------------------------------
		Road rRouteBellSW = new Road(this, 20, "rRouteBellSW");
		rRouteBellSW.setStartPositionFrom(raEntreeA, 6);
		rRouteBellSW.setStartDirection(226);
		roads.add(rRouteBellSW);
		raEntreeA.connectTo(rRouteBellSW, 6);
		
		Road rRouteBellNE = new Road(this, 20, "rRouteBellNE");
		rRouteBellNE.setDirection(46);
		rRouteBellNE.setEndPositionFrom(raEntreeA, 8, 46);
		roads.add(rRouteBellNE);
		rRouteBellNE.connectTo(raEntreeA, 8);
		*/
		
		// Network settings =================================================================================
		rRueDeGeneveSE.setGenerateVehicules(1200);
		rRueGermaineTillionSW.setGenerateVehicules(50);
		rD884NE.setGenerateVehicules(50);
		rSortieCERNNW.setGenerateVehicules(200);
		//rD984FNWS.setGenerateVehicules(true);
		rC5SW.setGenerateVehicules(100);
		rTunnelNW.setGenerateVehicules(40);
		rD984FNWS2.setGenerateVehicules(50);

		/*
		rRoutePauliNorthSW.setGenerateVehicules(true);
		rRoutePauliSouthNE.setGenerateVehicules(true);
		rCheminMaisonnexS.setGenerateVehicules(true);
		rRouteDeMeyrinSouthNW.setGenerateVehicules(true);
		rRouteBellNE.setGenerateVehicules(true);
		
		raEntreeA.setMaxSpeed(1); */
		raLHC.setMaxSpeed(1);
		
		//printNames();
		this.generateAllNetworkRides(7);
		this.cleanAllNetworkRides();
		
		rD984FSE.setCounter(0.5);
		rD984FNW.setCounter(0.49);
		
		rD984FSES.setCounter(0.3);
		rD984FNWS.setCounter(0.702);
		
		createActualData();
	}
	public void createActualData() {
		for (AllNetworkRides anr: allNetworkRides) {
			if (anr.getRoadName().equals("rD884NE")) {
				anr.print();
				/*for (Ride ride: anr.getNetworkRides()) {
					if (ride.getNextConnections().get(ride.getNextConnections().size()-1).getName().equals("rRouteDeMeyrinSouthSE")) {
						System.out.println("Premier trajer trouvé !");
					}
				}*/
			}
		}
	}
	public void createRealNetwork() {
		
		Polygon tmp = new Polygon();
		tmp.npoints = 4;
		tmp.xpoints[0] = 13*cellWidth;
		tmp.xpoints[1] = 235*cellWidth;
		tmp.xpoints[2] = 212*cellWidth;
		tmp.xpoints[3] = 3*cellWidth;
		tmp.ypoints[0] = 8*cellWidth;
		tmp.ypoints[1] = 102*cellWidth;
		tmp.ypoints[2] = 150*cellWidth;
		tmp.ypoints[3] = 25*cellWidth;
		zones.add(tmp);
		
		int[] tmpX = new int[5];
		int[] tmpY = new int[5];
		tmpX[0] = 115*cellWidth;//722;
		tmpX[1] = 131*cellWidth;//837;
		tmpX[2] = 250*cellWidth;//1464;
		tmpX[3] = 236*cellWidth;//1376;
		tmpX[4] = 119*cellWidth;//750;
		tmpY[0] = 43*cellWidth;//293;
		tmpY[1] = 17*cellWidth;//92;
		tmpY[2] = 60*cellWidth;//379;
		tmpY[3] = 98*cellWidth;//602;
		tmpY[4] = 48*cellWidth;//336;
		Polygon tmp2 = new Polygon(tmpX, tmpY, 5);
		zones.add(tmp2);
		
		// Porte de France
		RoundAbout raPorteDeFrance = new RoundAbout(this, 48, "raPorteDeFrance");
		raPorteDeFrance.setX(0);
		raPorteDeFrance.setY(0);
		raPorteDeFrance.setDirection(0);
		roundAbouts.add(raPorteDeFrance);
		
		// Rue de Genève ----------------------------------------------------------------------------------------------
		// N-W (out)
		Road rRueDeGeneveNW = new Road(this, 15, "rRueDeGeneveNW");
		rRueDeGeneveNW.setStartPositionFrom(raPorteDeFrance, 7);
		rRueDeGeneveNW.setDirection(271);
		rRueDeGeneveNW.addPoint(new Point(4,291));
		roads.add(rRueDeGeneveNW);
		raPorteDeFrance.connectTo(rRueDeGeneveNW, 7);
		
		// S-E (in)
		Road rRueDeGeneveSE = new Road(this, 15, "rRueDeGeneveSE");
		rRueDeGeneveSE.setDirection(111);
		rRueDeGeneveSE.addPoint(new Point(11, 131));
		rRueDeGeneveSE.setEndPositionFrom(raPorteDeFrance,11,111);
		roads.add(rRueDeGeneveSE);
		rRueDeGeneveSE.connectTo(raPorteDeFrance, 11);
		
		// Rue Germaine Tillion ---------------------------------------------------------------------------------------
		// N-E (out)
		Road rRueGermaineTillionNE = new Road(this, 25, "rRueGermaineTillionNE");
		rRueGermaineTillionNE.setStartPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-8);
		rRueGermaineTillionNE.setDirection(24);
		rRueGermaineTillionNE.addPoint(new Point(10, 38));
		roads.add(rRueGermaineTillionNE);
		raPorteDeFrance.connectTo(rRueGermaineTillionNE, raPorteDeFrance.getLength()-8);
		
		// S-W (in)
		Road rRueGermaineTillionSW = new Road(this, 25, "rRueGermaineTillionSW");
		rRueGermaineTillionSW.setDirection(218);
		rRueGermaineTillionSW.addPoint(new Point(15,232));
		rRueGermaineTillionSW.setEndPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-2,230);
		roads.add(rRueGermaineTillionSW);
		rRueGermaineTillionSW.connectTo(raPorteDeFrance, raPorteDeFrance.getLength()-2);
		
		// D984F North ------------------------------------------------------------------------------------------------
		// S-E (out)
		Road rD984FSE = new Road(this, 110, "rD984FSE");
		rD984FSE.setStartPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-17);
		rD984FSE.setStartDirection(93);
		//rD984FSE.setDirection(113);
		rD984FSE.addPoint(new Point(4,113));
		rD984FSE.addPoint(new Point(rD984FSE.getLength()-4,97));
		roads.add(rD984FSE);
		raPorteDeFrance.connectTo(rD984FSE, raPorteDeFrance.getLength()-18);
		
		// N-W (in)
		Road rD984FNW = new Road(this, 109, "rD984FNW");
		rD984FNW.setDirection(274);
		rD984FNW.addPoint(new Point(5,293));
		rD984FNW.addPoint(new Point(105,313));
		rD984FNW.setEndPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-13,293);
		
		roads.add(rD984FNW);
		rD984FNW.connectTo(raPorteDeFrance, raPorteDeFrance.getLength()-13);
		
		// D884 ------------------------------------------------------------------------------------------------------
		// S-W (out)
		Road rD884SW = new Road(this, 15, "rD884SW");
		rD884SW.setStartPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-32);
		rD884SW.setDirection(198);
		rD884SW.addPoint(new Point(2, 218));
		roads.add(rD884SW);
		raPorteDeFrance.connectTo(rD884SW, raPorteDeFrance.getLength()-32);
		
		// N-E (in)
		Road rD884NE = new Road(this, 15, "rD884NE");
		rD884NE.setDirection(38);
		rD884NE.addPoint(new Point(13,58));
		rD884NE.setEndPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-26,38);
		roads.add(rD884NE);
		rD884NE.connectTo(raPorteDeFrance,  raPorteDeFrance.getLength()-26);
		
		// SortieCERN -------------------------------------------------------------------------------------------------
		// S-E (out)
		Road rSortieCERNSE = new Road(this, 15, "rSortieCERNSE");
		rSortieCERNSE.setStartPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-23);
		rSortieCERNSE.setDirection(150);
		rSortieCERNSE.addPoint(new Point(3,170));
		rSortieCERNSE.addPoint(new Point(10,150));
		roads.add(rSortieCERNSE);
		raPorteDeFrance.connectTo(rSortieCERNSE, raPorteDeFrance.getLength()-23);
		rSortieCERNSE.setMaxOutflow(8);
		
		// N-W (in)
		Road rSortieCERNNW = new Road(this, 15, "rSortieCERNNW");
		rSortieCERNNW.setDirection(330);
		rSortieCERNNW.addPoint(new Point(5,350));
		rSortieCERNNW.addPoint(new Point(12,5));
		rSortieCERNNW.setEndPositionFrom(raPorteDeFrance, raPorteDeFrance.getLength()-20,330);
		roads.add(rSortieCERNNW);
		rSortieCERNNW.connectTo(raPorteDeFrance,  raPorteDeFrance.getLength()-20);
		
		// D884CERN
		Road rD884CERN = new Road(this, 26, "rD884CERN");
		rD884CERN.setDirection(38);
		rD884CERN.addPoint(new Point(8,90));
		rD884CERN.addPoint(new Point(14,170));
		rD884CERN.addPoint(new Point(20,150));
		rD884CERN.setX(rD884NE.getX()+getCellWidth()*(Math.cos(2*Math.PI*rD884NE.getDirection()/360.0) + 2*Math.sin(2*Math.PI*rD884NE.getDirection()/360.0)));
		rD884CERN.setY(rD884NE.getY()+getCellWidth()*(Math.sin(2*Math.PI*rD884NE.getDirection()/360.0) - 2*Math.cos(2*Math.PI*rD884NE.getDirection()/360.0)));
		roads.add(rD884CERN);
		rD884NE.getRoadCells().get(2).setOutCell(rD884CERN.getRoadCells().get(0));
		rD884NE.addExit("rD884CERN", 2);
		rD884CERN.getRoadCells().get(0).setPreviousCell(rD884NE.getRoadCells().get(2));
		rD884CERN.addEnter("rD884NE", 0);
		rD884CERN.setMaxOutflow(40);
		
		// LHC --------------------------------------------------------------------------------------------------------
		RoundAbout raLHC = new RoundAbout(this, 17, "raLHC");
		raLHC.setDirection(0);
		raLHC.setPositionFrom(rD984FSE, 5);
		roundAbouts.add(raLHC);
		rD984FSE.connectTo(raLHC, 5);
		raLHC.connectTo(rD984FNW, 3);
		
		// D984F South ------------------------------------------------------------------------------------------------
		// S-E (out)
		Road rD984FSES = new Road(this, 91, "rD984FSES");
		rD984FSES.setStartPositionFrom(raLHC, raLHC.getLength()-7);
		rD984FSES.setStartDirection(129);
		rD984FSES.addPoint(new Point(4,113));
		roads.add(rD984FSES);
		raLHC.connectTo(rD984FSES, raLHC.getLength()-7);
		
		// N-W (in)
		Road rD984FNWS = new Road(this, 91, "rD984FNWS");
		rD984FNWS.setDirection(293);
		rD984FNWS.addPoint(new Point(rD984FNWS.getLength()-5,313));
		rD984FNWS.setEndPositionFrom(raLHC, raLHC.getLength()-5,293);
		roads.add(rD984FNWS);
		rD984FNWS.connectTo(raLHC, raLHC.getLength()-5);
		
		Road rD984FNWS2 = new Road(this, 46, "rD984FNWS2");
		rD984FNWS2.setDirection(293);
		rD984FNWS2.setX(rD984FNWS.getX());
		rD984FNWS2.setY(rD984FNWS.getY()-this.getCellHeight());
		//rD984FNWS2.setEndPositionFrom(raLHC, raLHC.getLength()-4,293);
		roads.add(rD984FNWS2);
		rD984FNWS2.connectTo(rD984FNWS, 46);
		
		
		
		
		// C5 ---------------------------------------------------------------------------------------------------------
		// N-E (out)
		Road rC5NE = new Road(this, 30, "rC5NE");
		rC5NE.setStartPositionFrom(raLHC, raLHC.getLength()-2);
		rC5NE.setStartDirection(30);
		roads.add(rC5NE);
		raLHC.connectTo(rC5NE, raLHC.getLength()-2);
		
		// S-W (in)
		Road rC5SW = new Road(this, 30, "rC5SW");
		rC5SW.setDirection(210);
		//rC5SW.addPoint(new Point(rC5SW.getLength()-7,215));
		rC5SW.setEndPositionFrom(raLHC, raLHC.getLength()-1,293);
		roads.add(rC5SW);
		rC5SW.connectTo(raLHC, raLHC.getLength()-1);
		
		// Tunnel inter-site ------------------------------------------------------------------------------------------
		// S-E
		Road rTunnelSE = new Road(this, 35, "rTunnelSE");
		rTunnelSE.setStartPositionFrom(rC5NE, 7, 120);
		rTunnelSE.addPoint(new Point(2, 180));
		rTunnelSE.addPoint(new Point(20, 110));
		rTunnelSE.addPoint(new Point(27, 185));
		roads.add(0,rTunnelSE);
		rC5NE.getRoadCells().get(7).setOutCell(rTunnelSE.getRoadCells().get(0));
		rC5NE.addExit("rTunnelSE", 7);
		rTunnelSE.addEnter("rC5NE", 0);
		rTunnelSE.getRoadCells().get(0).setPreviousCell(rC5NE.getRoadCells().get(7));
		rTunnelSE.setUnderground(14, 16, true);
		
		// N-W
		Road rTunnelNW = new Road(this, 39, "rTunnelNW");
		rTunnelNW.setDirection(5);
		rTunnelNW.addPoint(new Point(9, 290));
		rTunnelNW.addPoint(new Point(16, 0));
		rTunnelNW.setX(rTunnelSE.getX()+9*cellWidth);
		rTunnelNW.setY(rTunnelSE.getY()+29*cellWidth);
		roads.add(0,rTunnelNW);
		rTunnelNW.getRoadCells().get(rTunnelNW.getLength()-1).setOutCell(rC5NE.getRoadCells().get(13));
		rTunnelNW.addExit(rC5NE.getName(), rTunnelNW.getLength()-1);
		rC5NE.addEnter("rTunnelNW", 13);
		rTunnelNW.setUnderground(17, 20, true);
		
		// Carrefour entree B -----------------------------------------------------------------------------------------
		CrossRoad crEntreeB = new CrossRoad(this, "crEntreeB");
		crEntreeB.setX(1400);
		crEntreeB.setY(650);
		crEntreeB.setDirection(0);
		crossRoads.add(crEntreeB);
		crEntreeB.setTimeTrafficLight(20);
		crEntreeB.setPositionFromOut(rD984FNWS, 0);
		crEntreeB.setDirection(rD984FNWS.getDirection());
		crEntreeB.connectTo(rD984FNWS, 0);
		rD984FSES.connectTo(crEntreeB, 1);
		crEntreeB.addRoadIn(rD984FSES, 1);
		
		// Route Pauli ------------------------------------------------------------------------------------------------
		// North ------------------------------------------------------------------------------------------------------
		Road rRoutePauliNorthSW = new Road(this, 15, "rRoutePauliNorthSW");
		rRoutePauliNorthSW.setPositionInFrom(crEntreeB, 0);
		roads.add(rRoutePauliNorthSW);
		rRoutePauliNorthSW.connectTo(crEntreeB, 0);
		
		Road rRoutePauliNorthNE = new Road(this, 15, "rRoutePauliNorthNE");
		rRoutePauliNorthNE.setPositionOutFrom(crEntreeB, 3);
		crEntreeB.connectTo(rRoutePauliNorthNE, 3);
		roads.add(rRoutePauliNorthNE);
		
		// South ------------------------------------------------------------------------------------------------------
		Road rRoutePauliSouthNE = new Road(this, 15, "rRoutePauliSouthNE");
		rRoutePauliSouthNE.setPositionInFrom(crEntreeB, 2);
		rRoutePauliSouthNE.connectTo(crEntreeB, 2);
		roads.add(rRoutePauliSouthNE);
		
		Road rRoutePauliSouthSW = new Road(this, 15, "rRoutePauliSouthSW");
		rRoutePauliSouthSW.setPositionOutFrom(crEntreeB, 1);
		crEntreeB.connectTo(rRoutePauliSouthSW, 1);
		roads.add(rRoutePauliSouthSW);
		
		// Route de Meyrin NORTH---------------------------------------------------------------------------------------
		Road rRouteDeMeyrinNorthNW = new Road(this, 33, "rRouteDeMeyrinNorthNW");
		rRouteDeMeyrinNorthNW.setPositionInFrom(crEntreeB, 3);
		rRouteDeMeyrinNorthNW.connectTo(crEntreeB, 3);
		roads.add(rRouteDeMeyrinNorthNW);
		
		Road rRouteDeMeyrinNorthSE = new Road(this, 33, "rRouteDeMeyrinNorthSE");
		rRouteDeMeyrinNorthSE.setPositionOutFrom(crEntreeB, 2);
		crEntreeB.connectTo(rRouteDeMeyrinNorthSE, 2);
		roads.add(rRouteDeMeyrinNorthSE);
		
		// RA entree A ------------------------------------------------------------------------------------------------
		RoundAbout raEntreeA = new RoundAbout(this, 15, "raEntreeA");
		raEntreeA.setDirection(0);
		raEntreeA.setPositionFrom(rRouteDeMeyrinNorthSE, 3);
		roundAbouts.add(raEntreeA);
		rRouteDeMeyrinNorthSE.connectTo(raEntreeA, 3);
		raEntreeA.connectTo(rRouteDeMeyrinNorthNW, 2);
		
		// Chemin de Maisonnex ----------------------------------------------------------------------------------------
		Road rCheminMaisonnexN = new Road(this, 5, "rCheminMaisonnexN");
		rCheminMaisonnexN.setStartPositionFrom(raEntreeA, raEntreeA.getLength()-1);
		rCheminMaisonnexN.setStartDirection(15);
		roads.add(rCheminMaisonnexN);
		raEntreeA.connectTo(rCheminMaisonnexN, raEntreeA.getLength()-1);
		
		Road rCheminMaisonnexS = new Road(this, 5, "rCheminMaisonnexS");
		rCheminMaisonnexS.setDirection(195);
		rCheminMaisonnexS.setEndPositionFrom(raEntreeA, 0,195);
		roads.add(rCheminMaisonnexS);
		rCheminMaisonnexS.connectTo(raEntreeA, 0);
		
		// Route de Meyrin SOUTH---------------------------------------------------------------------------------------
		Road rRouteDeMeyrinSouthSE = new Road(this, 20, "rRouteDeMeyrinSouthSE");
		rRouteDeMeyrinSouthSE.setStartPositionFrom(raEntreeA, raEntreeA.getLength()-5);
		rRouteDeMeyrinSouthSE.setStartDirection(113);
		roads.add(rRouteDeMeyrinSouthSE);
		raEntreeA.connectTo(rRouteDeMeyrinSouthSE, raEntreeA.getLength()-5);
		
		Road rRouteDeMeyrinSouthNW = new Road(this, 20, "rRouteDeMeyrinSouthNW");
		rRouteDeMeyrinSouthNW.setDirection(293);
		rRouteDeMeyrinSouthNW.setEndPositionFrom(raEntreeA, raEntreeA.getLength()-4,293);
		roads.add(rRouteDeMeyrinSouthNW);
		rRouteDeMeyrinSouthNW.connectTo(raEntreeA, raEntreeA.getLength()-4);
		
		// Route Bell---------------------------------------------------------------------------------------
		Road rRouteBellSW = new Road(this, 20, "rRouteBellSW");
		rRouteBellSW.setStartPositionFrom(raEntreeA, 6);
		rRouteBellSW.setStartDirection(226);
		roads.add(rRouteBellSW);
		raEntreeA.connectTo(rRouteBellSW, 6);
		
		Road rRouteBellNE = new Road(this, 20, "rRouteBellNE");
		rRouteBellNE.setDirection(46);
		rRouteBellNE.setEndPositionFrom(raEntreeA, 8, 46);
		roads.add(rRouteBellNE);
		rRouteBellNE.connectTo(raEntreeA, 8);
		
		// Network settings =================================================================================
		rRueDeGeneveSE.setGenerateVehicules(50);
		rRueGermaineTillionSW.setGenerateVehicules(50);
		rD884NE.setGenerateVehicules(50);
		rSortieCERNNW.setGenerateVehicules(50);
		//rD984FNWS.setGenerateVehicules(true);
		rC5SW.setGenerateVehicules(50);
		rTunnelNW.setGenerateVehicules(50);
		rRoutePauliNorthSW.setGenerateVehicules(50);
		rRoutePauliSouthNE.setGenerateVehicules(50);
		rCheminMaisonnexS.setGenerateVehicules(50);
		rRouteDeMeyrinSouthNW.setGenerateVehicules(50);
		rRouteBellNE.setGenerateVehicules(50);
		rD984FNWS2.setGenerateVehicules(50);
		
		raEntreeA.setMaxSpeed(1);
		raLHC.setMaxSpeed(1);
		
		//printNames();
		this.generateAllNetworkRides(6);
		this.cleanAllNetworkRides();
		
		rD984FSE.setCounter(0.5);
		rD984FNW.setCounter(0.49);
		
		rD984FSES.setCounter(0.3);
		rD984FNWS.setCounter(0.702);
		
		createActualData();
	}
	
	public void printNames() {
		System.out.println("====== Road names: ======");
		for (Road r: this.roads) {
			System.out.println("");
			System.out.println(r.getName());
			System.out.print("Exits: ");
			for (Connection e: r.getExits()) {
				e.print();
			}
			System.out.println("");
			System.out.print("Enters: ");
			for (Connection e: r.getEnters()) {
				e.print();
			}
			System.out.println("");
		}
		System.out.println("");
		
		System.out.println("====== Roundabout names: ======");
		for (RoundAbout ra: this.roundAbouts) {
			System.out.println("");
			System.out.println(ra.getName());
			System.out.print("Exits: ");
			for (Connection e: ra.getExits()) {
				e.print();
			}
			System.out.println("");
			System.out.print("Enters: ");
			for (Connection e: ra.getEnters()) {
				e.print();
			}
			System.out.println("");
		}
		System.out.println("");
		System.out.println("====== Crossroad names: ======");
		for (CrossRoad cr: this.crossRoads) {
			System.out.println("");
			System.out.println(cr.getName());
			System.out.print("Exits: ");
			for (Connection e: cr.getExits()) {
				e.print();
			}
			System.out.println("");
			System.out.print("Enters: ");
			for (Connection e: cr.getEnters()) {
				e.print();
			}
			System.out.println("");
		}
		System.out.println("");
		
		for (AllNetworkRides ANR: this.allNetworkRides) {
			ANR.print();
			System.out.println("");
		}
		System.out.println("(=^-^=)");
		
		
		for (AllNetworkRides ANR: this.allNetworkRides) {
			ANR.print();
			System.out.println("");
		}
	}
	public Road getRoad(String name) {
		for (Road r: this.getRoads()) {
			if (r.getName().equals(name)) {
				return r;
			}
		}
		return null;
	}
	
	public Ride selectARide(String roadName) {
		Ride r = new Ride();
		for (AllNetworkRides ANR: allNetworkRides) {
			if (ANR.getRoadName().equals(roadName)) {
				int length = ANR.getNetworkRides().size();
				int index = (int) (Math.random() * length) ;
				return ANR.getNetworkRides().get(index).clone();
			}
		}
		return r;
	}
	public Ride selectARide(String startName, String endName) {
		Ride r = new Ride();
		for (AllNetworkRides anr: allNetworkRides) {
			if (anr.getRoadName().equals(startName)) {
				for (Ride ride: anr.getNetworkRides()) {
					if (ride.getRoadName().equals(endName)) {
						return ride.clone();
					}
				}
				
			}
		}
		return r;
	}
	public void cleanAllNetworkRides() {
		if (!this.getAllNetworkRides().isEmpty()) {
			for (AllNetworkRides ANR: this.getAllNetworkRides()) {
				ArrayList<Integer> elmtsToChange = new ArrayList<Integer>();
				for (int i=0; i<ANR.getNetworkRides().size()-2; ++i) {
					for (int j=(i+1); j<ANR.getNetworkRides().size()-1; ++j) {
						ArrayList<Connection> riConnections = ANR.getNetworkRides().get(i).getNextConnections();
						ArrayList<Connection> rjConnections = ANR.getNetworkRides().get(j).getNextConnections();
						if (riConnections.get(riConnections.size()-1).getName().equals(rjConnections.get(rjConnections.size()-1).getName())) {
							if (rjConnections.size() > riConnections.size()) {
								elmtsToChange.add(j);
							} else if (rjConnections.size() < riConnections.size()){
								elmtsToChange.add(j);
							}
						}
					}
				}
				HashSet<Integer> set = new HashSet<Integer>();
				set.addAll(elmtsToChange);
				elmtsToChange = new ArrayList<Integer>(set);
				Collections.reverse(elmtsToChange);
				if (!elmtsToChange.isEmpty()) {
					for (int n: elmtsToChange) {
						ANR.getNetworkRides().remove(n);
					}
				}
			}
		}
	}
	
	public void generateAllNetworkRides(int n) {
		for (Road r: this.roads) {
			if (r.getGenerateVehicules() > 0) {
				r.generateRides(n);
			}
		}
	}
	
	public void addARideToAllNetworkRides(Ride ride) {
		int index = -1;
		for (int i=0; i<this.allNetworkRides.size(); ++i) {
			if (this.allNetworkRides.get(i).getRoadName().equals(ride.getRoadName())) {
				index = i;
			}
		}
		if (index == -1) {
			AllNetworkRides ANR = new AllNetworkRides(ride.getRoadName());
			ANR.addRide(ride);
			this.allNetworkRides.add(ANR);
		} else {
			this.allNetworkRides.get(index).addRide(ride);
		}
	}
	public Road selectARoad(String roadName) {
		for (Road r: roads) {
			if (r.getName().equals(roadName)) {
				return r;
			}
		}
		Utils.log("Error - while trying to find " + roadName + " in network");
		return null;
	}
	// Getters & setters ====================================================================================
	public int getN() {
		return n;
	}
	public int getNumberOfVehicles() {
		return numberOfVehicles;
	}
	public void increaseNumberOfVehicles(int n) {
		numberOfVehicles += n;
	}
	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
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
	public void switchDrawNames() {
		drawNames = !drawNames;
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
	public boolean getDrawNames() {
		return this.drawNames;
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
	public ArrayList<AllNetworkRides> getAllNetworkRides() {
		return allNetworkRides;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
}
