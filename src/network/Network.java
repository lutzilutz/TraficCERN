package network;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import elements.Cell;
import elements.Connection;
import elements.CrossRoad;
import elements.MaxVehicleOutflow;
import elements.MultiLaneRoundAbout;
import elements.Phase;
import elements.Ride;
import elements.Road;
import elements.RoundAbout;
import elements.TrafficLightsSystem;
import elements.Vehicle;
import main.Simulator;
import utils.Utils;

public class Network {

	// list of button's title and description
	private static String[] titles;
	private static String[] descriptions;
	
	private Simulator sim; // link to the simulator
	private int n; // id of the current Network
	private ArrayList<Road> roads = new ArrayList<Road>(); // list of all roads
	private ArrayList<MultiLaneRoundAbout> multiLaneRoundAbouts = new ArrayList<MultiLaneRoundAbout>(); // list of all multilane round-abouts
	private ArrayList<RoundAbout> roundAbouts = new ArrayList<RoundAbout>(); // list of all round-abouts
	private ArrayList<CrossRoad> crossRoads = new ArrayList<CrossRoad>(); // list of all cross-roads
	private ArrayList<TrafficLightsSystem> trafficLightsSystems = new ArrayList<TrafficLightsSystem>(); // list of all traffic lights
	private ArrayList<AllNetworkRides> allNetworkRides = new ArrayList<AllNetworkRides>(); // list of all rides
	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>(); // list of all vehicles
	private ArrayList<MaxVehicleOutflow> maxVehicleOutflows = new ArrayList<MaxVehicleOutflow>(); // list of all maximum outflow
	private int numberOfVehicles = 0; // number of vehicles in the network
	private int cellWidth=10, cellHeight=cellWidth; // cells width and height
	
	private ArrayList<Polygon> zones = new ArrayList<Polygon>(); // visual area of the CERN
	
	private double xOffset=0, yOffset=0; // offset of the network on screen
	private double xDefaultOffset=0, yDefaultOffset=0; // default offset values
	private double rotation=0; // rotation of the network on screen
	
	private int maxSpeed = 2; // maximum speed of vehicles
	
	public int artificiallyDestroyedVehicles = 0; // count of all artifically destroyed vehicles
	
	public Network(Simulator sim, int n, int size) {
		this.setCellWidth((int) (Math.pow(2, 1+size)));
		this.setCellHeight((int) (Math.pow(2, 1+size)));
		this.sim = sim;
		this.n = n;
		Road.resetID();
	
		if (n >= 0) {
			Utils.logInfo("Creating Network #" + n + " ");
		}
		
		initCERNArea();
		
		switch (n) {
		case 0:
			Utils.logln("(current scenario) ... ");
			createCurrentNetwork();
			break;
		case 1:
			Utils.logln("(scenario RA entrance B) ... ");
			createScenarioRAEntranceB();
			break;
		}
		
		if (n==0 || n==1) {
			this.generateAllNetworkRides(50);
			this.cleanAllNetworkRides(2);
		}
		
		titles = new String[2];
		titles[0] = "CERN network";
		titles[1] = "Scenario 1";

		descriptions = new String[2];
		descriptions[0] = "Actual network arount the CERN, multi lanes";
		descriptions[1] = "Entrance B crossroad replaced by round-about";
	}
	private void createCurrentNetwork() {
		
		// Porte de France
		MultiLaneRoundAbout raPorteDeFrance = genPorteDeFrance();
		
		// Rue de Genève N-W (out) and S-E (in)
		genRueDeGeneveNW(raPorteDeFrance);
		genRueDeGeneveSE(raPorteDeFrance);
		
		// Rue Germaine Tillion N-E (out) and S-W (in)
		genRueGermaineTillionNE(raPorteDeFrance);
		genRueGermaineTillionSW(raPorteDeFrance);
		
		// D984F North S-E (out) and N-W (in)
		Road rD984FSE = genD984FSE(raPorteDeFrance);
		Road rD984FNW = genD984FNW(raPorteDeFrance);
		
		// D884 S-W (out) and N-E (in)
		genD884SW(raPorteDeFrance);
		Road rD884NE = genD884NE(raPorteDeFrance);
		
		// SortieCERN S-E (out) and N-W (in)
		Road rSortieCERNSE = genSortieCERNSE(raPorteDeFrance);
		genSortieCERNNW(raPorteDeFrance);
		
		// D884CERN
		genD884CERN(rD884NE, rSortieCERNSE);
		
		// LHC
		RoundAbout raLHC = genLHC(rD984FSE, rD984FNW);
		
		// D984F South S-E (out), 3 lanes
		Road rD984FSES = genD984FSES(raLHC);
		Road rD984FSES2 = genD984FSES2(rD984FSES);
		Road rD984FSES3 = genD984FSES3_Scenario0(rD984FSES2);
		
		// N-W (in)
		Road rD984FNWS = genD984FNWS(raLHC, null);
		Road rD984FNWS2 = genD984FNWS2(rD984FNWS, null);
		
		// CrossRoad middle roads W -> E
		Road rWE1 = genWE1(rD984FSES);
		Road rWE2 = genWE2(rD984FSES2, rWE1);
		
		// Route de Meyrin NORTH (SE)	
		Road rRouteDeMeyrinNorthSE1 = genRouteDeMeyrinNorthSE1(rD984FSES2, rWE1, null);	
		Road rRouteDeMeyrinNorthSE2 = genRouteDeMeyrinNorthSE2(rD984FSES2, rRouteDeMeyrinNorthSE1, rWE2, null);
		Road rRouteDeMeyrinNorthSE1_2 = genRouteDeMeyrinNorthSE1_2(rRouteDeMeyrinNorthSE2, rD984FSES2);
		
		// RA entree A
		MultiLaneRoundAbout raEntreeA = genRaEntreeA(rRouteDeMeyrinNorthSE1_2, rRouteDeMeyrinNorthSE2);
		
		Road rRouteDeMeyrinNorthNW1 = genRouteDeMeyrinNorthNW1(raEntreeA, null);
		Road rRouteDeMeyrinNorthNW2 = genRouteDeMeyrinNorthNW2(rRouteDeMeyrinNorthNW1, null);
		Road rRouteDeMeyrinNorthNW3 = genRouteDeMeyrinNorthNW3(rRouteDeMeyrinNorthNW2, null);
		genRouteDeMeyrinNorthNW1_2(rRouteDeMeyrinNorthNW1, raEntreeA);
		
		// CrossRoad middle roads E -> W
		Road rEW1 = genEW1(rRouteDeMeyrinNorthNW3, rD984FNWS2);
		Road rEW2 = genEW2(rRouteDeMeyrinNorthNW2, rD984FNWS2, rEW1);
		
		// Route Pauli North 
		Road rRoutePauliNorthNE = genRoutePauliNorthNE(rEW1, null);
		Road rRoutePauliNorthSW = genRoutePauliNorthSW(rRoutePauliNorthNE);
		
		// Route Pauli South 
		Road rRoutePauliSouthNERight = genRoutePauliSouthNERight(rWE2, null);
		Road rRoutePauliSouthNELeft = genRoutePauliSouthNELeft(rRoutePauliSouthNERight, null);
		Road rRoutePauliSouthSW = genRoutePauliSouthSW(rRoutePauliSouthNELeft, null);

		// Cross-road entrance B
		genCrossRoadEntranceB(rRoutePauliSouthNERight, rRoutePauliNorthNE, rRoutePauliNorthSW, rRoutePauliSouthSW, rEW1, rD984FNWS2, rWE1, rRouteDeMeyrinNorthSE1, rRoutePauliSouthNELeft, rEW2, rRouteDeMeyrinNorthSE2, rD984FSES3, rRouteDeMeyrinNorthNW1, rWE2);
		
		// Bell
		genRouteBellSW(raEntreeA);
		genRouteBellNE(raEntreeA);
		genRouteBellNERight(raEntreeA);
		
		// Route de Meyrin South
		genRouteDeMeyrinSouthSE(raEntreeA);
		genRouteDeMeyrinSouthNW(raEntreeA);
		
		// Chemin de Maisonnex
		genCheminMaisonnexN(raEntreeA);
		genCheminMaisonnexS(raEntreeA);
		
		// C5 N-E (out) and S-W (in)
		Road rC5NE = genC5NE(raLHC);
		Road rC5SW = genC5SW(raLHC);
		
		// Tunnel inter-site S-E and N-W
		Road rTunnelSE = genTunnelSE(rC5NE, rC5SW);
		Road rTunnelNW = genTunnelNW(rTunnelSE, rC5NE, rC5SW);
		
		// CrossRoadsPhases:
		genLightPhases(rD984FSES, rD984FSES2, rD984FSES3, rRouteDeMeyrinNorthNW1, rRouteDeMeyrinNorthNW2, rRouteDeMeyrinNorthNW3, rRoutePauliSouthNELeft, rRoutePauliSouthNERight, rRoutePauliNorthNE);
		
		
		rC5NE.getRoadCells().get(9).getOverlapedCells().add(rTunnelNW.getRoadCells().get(rTunnelNW.getLength()-1));
		rTunnelNW.getRoadCells().get(rTunnelNW.getLength()-1).getOverlapedCells().add(rC5NE.getRoadCells().get(9));
		
		rC5NE.getRoadCells().get(7).getOverlapedCells().add(rTunnelSE.getRoadCells().get(1));
		rTunnelSE.getRoadCells().get(1).getOverlapedCells().add(rC5NE.getRoadCells().get(7));
		
		rC5SW.getRoadCells().get(22).getOverlapedCells().add(rTunnelSE.getRoadCells().get(0));
		rTunnelSE.getRoadCells().get(0).getOverlapedCells().add(rC5SW.getRoadCells().get(22));
				
		rC5SW.connectFromiToj(rTunnelSE, 21, 1);
		rTunnelNW.connectFromiToj(rC5SW, rTunnelNW.getLength()-1, 21);
		rTunnelNW.connectFromiToj(rC5NE, rTunnelNW.getLength()-2, 10);
		
	}
	private void createScenarioRAEntranceB() {
		
		// Porte de France
		MultiLaneRoundAbout raPorteDeFrance = genPorteDeFrance();
		
		// Rue de Genève N-W (out) and S-E (in)
		genRueDeGeneveNW(raPorteDeFrance);
		genRueDeGeneveSE(raPorteDeFrance);
		
		// Rue Germaine Tillion N-E (out) and S-W (in)
		genRueGermaineTillionNE(raPorteDeFrance);
		genRueGermaineTillionSW(raPorteDeFrance);
		
		// D984F North S-E (out) and N-W (in)
		Road rD984FSE = genD984FSE(raPorteDeFrance);
		Road rD984FNW = genD984FNW(raPorteDeFrance);
		
		// D884 S-W (out) and N-E (in)
		genD884SW(raPorteDeFrance);
		Road rD884NE = genD884NE(raPorteDeFrance);
		
		// SortieCERN S-E (out) and N-W (in)
		Road rSortieCERNSE = genSortieCERNSE(raPorteDeFrance);
		genSortieCERNNW(raPorteDeFrance);
		
		// D884CERN
		genD884CERN(rD884NE, rSortieCERNSE);
		
		// LHC
		RoundAbout raLHC = genLHC(rD984FSE, rD984FNW);
		
		// D984F South S-E (out), 3 lanes
		Road rD984FSES = genD984FSES(raLHC);
		Road rD984FSES2 = genD984FSES2(rD984FSES);
		Road rD984FSES3 = genD984FSES3_Scenario1(rD984FSES2);
		
		// Entrance B
		MultiLaneRoundAbout raEntreeB = genRaEntreeB(rD984FSES,rD984FSES2,rD984FSES3);
		
		// D984F South N-W (in), 2 lanes
		Road rD984FNWS = genD984FNWS(raLHC, raEntreeB);
		genD984FNWS2(rD984FNWS, raEntreeB);
		
		// CrossRoad middle roads W -> E
		Road rWE1 = genWE1(rD984FSES);
		Road rWE2 = genWE2(rD984FSES2, rWE1);
		
		// Route de Meyrin NORTH (SE)
		Road rRouteDeMeyrinNorthSE1 = genRouteDeMeyrinNorthSE1(rD984FSES2, rWE1, raEntreeB);
		Road rRouteDeMeyrinNorthSE2 = genRouteDeMeyrinNorthSE2(rD984FSES2, rRouteDeMeyrinNorthSE1, rWE2, raEntreeB);
		Road rRouteDeMeyrinNorthSE1_2 = genRouteDeMeyrinNorthSE1_2(rRouteDeMeyrinNorthSE2, rD984FSES2);
		
		// RA entrance A
		MultiLaneRoundAbout raEntreeA = genRaEntreeA(rRouteDeMeyrinNorthSE1_2, rRouteDeMeyrinNorthSE2);
		
		Road rRouteDeMeyrinNorthNW1 = genRouteDeMeyrinNorthNW1(raEntreeA, raEntreeB);
		Road rRouteDeMeyrinNorthNW2 = genRouteDeMeyrinNorthNW2(rRouteDeMeyrinNorthNW1, raEntreeB);
		Road rRouteDeMeyrinNorthNW3 = genRouteDeMeyrinNorthNW3(rRouteDeMeyrinNorthNW2, raEntreeB);
		genRouteDeMeyrinNorthNW1_2(rRouteDeMeyrinNorthNW1, raEntreeA);
		
		// CrossRoad middle roads E -> W
		Road rEW1 = genEW1(rRouteDeMeyrinNorthNW3, null);
		genEW2(rRouteDeMeyrinNorthNW2, null, rEW1);
		
		// Route Pauli North 
		Road rRoutePauliNorthNE = genRoutePauliNorthNE(rEW1, raEntreeB);
		genRoutePauliNorthSW(rRoutePauliNorthNE);
		
		// Route Pauli South 
		Road rRoutePauliSouthNERight = genRoutePauliSouthNERight(rWE2, raEntreeB);
		Road rRoutePauliSouthNELeft = genRoutePauliSouthNELeft(rRoutePauliSouthNERight, raEntreeB);
		genRoutePauliSouthSW(rRoutePauliSouthNELeft, raEntreeB);
		
		// Bell
		genRouteBellSW(raEntreeA);
		genRouteBellNE(raEntreeA);
		genRouteBellNERight(raEntreeA);
		
		// Route de Meyrin South
		genRouteDeMeyrinSouthSE(raEntreeA);
		genRouteDeMeyrinSouthNW(raEntreeA);
		
		// Chemin de Maisonnex
		genCheminMaisonnexN(raEntreeA);
		genCheminMaisonnexS(raEntreeA);
		
		// C5 N-E (out) and S-W (in)
		Road rC5NE = genC5NE(raLHC);
		Road rC5SW = genC5SW(raLHC);
		
		// Tunnel inter-site S-E and N-W
		Road rTunnelSE = genTunnelSE(rC5NE, rC5SW);
		genTunnelNW(rTunnelSE, rC5NE, rC5SW);
		
		raLHC.setMaxSpeed(1);
		
	}
	public MultiLaneRoundAbout genPorteDeFrance() {
		MultiLaneRoundAbout raPorteDeFrance = new MultiLaneRoundAbout(this, 3, 48, "raPorteDeFrance");
		raPorteDeFrance.setX(0);
		raPorteDeFrance.setY(0);
		raPorteDeFrance.setDirection(0);
		multiLaneRoundAbouts.add(raPorteDeFrance);
		for (RoundAbout ra: raPorteDeFrance.getLanes()) {
			this.roundAbouts.add(ra);
		}
		return raPorteDeFrance;
	}
	public void genRueDeGeneveNW(MultiLaneRoundAbout raPorteDeFrance) {
		Road rRueDeGeneveNW = new Road(this, 15, "rRueDeGeneveNW");
		rRueDeGeneveNW.setStartPositionFrom(raPorteDeFrance.getLanes()[0], 7);
		rRueDeGeneveNW.setDirection(271);
		rRueDeGeneveNW.addPoint(new Point(4,291));
		roads.add(rRueDeGeneveNW);
		raPorteDeFrance.connectTo(rRueDeGeneveNW, 7);
	}
	public void genRueDeGeneveSE(MultiLaneRoundAbout raPorteDeFrance) {
		Road rRueDeGeneveSE = new Road(this, 15, "rRueDeGeneveSE");
		rRueDeGeneveSE.setDirection(111);
		rRueDeGeneveSE.addPoint(new Point(11, 131));
		rRueDeGeneveSE.setEndPositionFrom(raPorteDeFrance.getLanes()[0],11,111);
		roads.add(rRueDeGeneveSE);
		rRueDeGeneveSE.connectTo(raPorteDeFrance, 11);
		rRueDeGeneveSE.setGenerateVehicules(200);
	}
	public void genRueGermaineTillionNE(MultiLaneRoundAbout raPorteDeFrance) {
		Road rRueGermaineTillionNE = new Road(this, 25, "rRueGermaineTillionNE");
		rRueGermaineTillionNE.setStartPositionFrom(raPorteDeFrance.getLanes()[0], raPorteDeFrance.getLanes()[0].getLength()-8);
		rRueGermaineTillionNE.setDirection(24);
		rRueGermaineTillionNE.addPoint(new Point(10, 38));
		roads.add(rRueGermaineTillionNE);
		raPorteDeFrance.connectTo(rRueGermaineTillionNE, raPorteDeFrance.getLanes()[0].getLength()-8);
	}
	public void genRueGermaineTillionSW(MultiLaneRoundAbout raPorteDeFrance) {
		Road rRueGermaineTillionSW = new Road(this, 25, "rRueGermaineTillionSW");
		rRueGermaineTillionSW.setDirection(218);
		rRueGermaineTillionSW.addPoint(new Point(15,232));
		rRueGermaineTillionSW.setEndPositionFrom(raPorteDeFrance.getLanes()[0], raPorteDeFrance.getLanes()[0].getLength()-2,230);
		roads.add(rRueGermaineTillionSW);
		rRueGermaineTillionSW.connectTo(raPorteDeFrance, raPorteDeFrance.getLanes()[0].getLength()-2);
		rRueGermaineTillionSW.setGenerateVehicules(100);
	}
	public Road genD984FSE(MultiLaneRoundAbout raPorteDeFrance) {
		Road rD984FSE = new Road(this, 110, "rD984FSE");
		rD984FSE.setStartPositionFrom(raPorteDeFrance.getLanes()[0], raPorteDeFrance.getLanes()[0].getLength()-17);
		rD984FSE.setStartDirection(93);
		rD984FSE.addPoint(new Point(4,113));
		rD984FSE.addPoint(new Point(rD984FSE.getLength()-4,97));
		roads.add(rD984FSE);
		raPorteDeFrance.connectTo(rD984FSE, raPorteDeFrance.getLanes()[0].getLength()-18);
		rD984FSE.setCounter(0.5, "counter 1A");
		return rD984FSE;
	}
	public Road genD984FNW(MultiLaneRoundAbout raPorteDeFrance) {
		Road rD984FNW = new Road(this, 109, "rD984FNW");
		rD984FNW.setDirection(274);
		rD984FNW.addPoint(new Point(5,293));
		rD984FNW.addPoint(new Point(105,313));
		rD984FNW.setEndPositionFrom(raPorteDeFrance.getLanes()[0], raPorteDeFrance.getLanes()[0].getLength()-13,293);
		roads.add(rD984FNW);
		rD984FNW.connectTo(raPorteDeFrance, raPorteDeFrance.getLanes()[0].getLength()-13);
		rD984FNW.setCounter(0.49, "counter 1B");
		return rD984FNW;
	}
	public void genD884SW(MultiLaneRoundAbout raPorteDeFrance) {
		Road rD884SW = new Road(this, 15, "rD884SW");
		rD884SW.setStartPositionFrom(raPorteDeFrance.getLanes()[0], raPorteDeFrance.getLanes()[0].getLength()-32);
		rD884SW.setDirection(198);
		rD884SW.addPoint(new Point(2, 218));
		roads.add(rD884SW);
		raPorteDeFrance.connectTo(rD884SW, raPorteDeFrance.getLanes()[0].getLength()-32);
	}
	public Road genD884NE(MultiLaneRoundAbout raPorteDeFrance) {
		Road rD884NE = new Road(this, 15+45, "rD884NE");
		rD884NE.setDirection(38);
		rD884NE.addPoint(new Point(13+45,58));
		rD884NE.setEndPositionFrom(raPorteDeFrance.getLanes()[0], raPorteDeFrance.getLanes()[0].getLength()-26,38);
		roads.add(rD884NE);
		rD884NE.connectTo(raPorteDeFrance,  raPorteDeFrance.getLanes()[0].getLength()-26);
		rD884NE.setGenerateVehicules(50);
		return rD884NE;
	}
	public Road genSortieCERNSE(MultiLaneRoundAbout raPorteDeFrance) {
		Road rSortieCERNSE = new Road(this, 15, "rSortieCERNSE");
		rSortieCERNSE.setStartPositionFrom(raPorteDeFrance.getLanes()[0], raPorteDeFrance.getLanes()[0].getLength()-23);
		rSortieCERNSE.setDirection(150);
		rSortieCERNSE.addPoint(new Point(3,170));
		rSortieCERNSE.addPoint(new Point(10,150));
		roads.add(rSortieCERNSE);
		raPorteDeFrance.connectTo(rSortieCERNSE, raPorteDeFrance.getLanes()[0].getLength()-23);
		rSortieCERNSE.setMaxOutflow(8);
		rSortieCERNSE.setCounter(0.5, "counter EntranceE right");
		return rSortieCERNSE;
	}
	public Road genSortieCERNNW(MultiLaneRoundAbout raPorteDeFrance) {
		Road rSortieCERNNW = new Road(this, 15, "rSortieCERNNW");
		rSortieCERNNW.setDirection(330);
		rSortieCERNNW.addPoint(new Point(5,350));
		rSortieCERNNW.addPoint(new Point(12,5));
		rSortieCERNNW.setEndPositionFrom(raPorteDeFrance.getLanes()[0], raPorteDeFrance.getLanes()[0].getLength()-20,330);
		roads.add(rSortieCERNNW);
		rSortieCERNNW.connectTo(raPorteDeFrance,  raPorteDeFrance.getLanes()[0].getLength()-20);
		rSortieCERNNW.setGenerateVehicules(50);
		return rSortieCERNNW;
	}
	public void genD884CERN(Road rD884NE, Road rSortieCERNSE) {
		Road rD884CERN = new Road(this, 26+20, "rD884CERN");
		rD884CERN.setDirection(38);
		rD884CERN.addPoint(new Point(8+20,90));
		rD884CERN.addPoint(new Point(14+20,170));
		rD884CERN.addPoint(new Point(20+20,150));
		rD884CERN.setX(rD884NE.getX()+getCellWidth()*(Math.cos(2*Math.PI*rD884NE.getDirection()/360.0) + 27*Math.sin(2*Math.PI*rD884NE.getDirection()/360.0)));
		rD884CERN.setY(rD884NE.getY()+getCellWidth()*(Math.sin(2*Math.PI*rD884NE.getDirection()/360.0) - 27*Math.cos(2*Math.PI*rD884NE.getDirection()/360.0)));
		roads.add(rD884CERN);
		rD884NE.getRoadCells().get(27).setOutCell(rD884CERN.getRoadCells().get(0));
		rD884NE.addExit("rD884CERN", 27);
		rD884CERN.getRoadCells().get(0).setInCell(rD884NE.getRoadCells().get(27));
		rD884CERN.addEnter("rD884NE", 0);
		rD884CERN.setMaxOutflow(8);
		rD884CERN.setCounter(0.5, "counter EntranceE left");
		
		MaxVehicleOutflow outflowEntranceE = new MaxVehicleOutflow(rD884CERN, 4);
		outflowEntranceE.addRoad(rSortieCERNSE);
		maxVehicleOutflows.add(outflowEntranceE);
	}
	public RoundAbout genLHC(Road rD984FSE, Road rD984FNW) {
		RoundAbout raLHC = new RoundAbout(this, 17, "raLHC");
		raLHC.setDirection(0);
		raLHC.setPositionFrom(rD984FSE, 5);
		roundAbouts.add(raLHC);
		rD984FSE.connectTo(raLHC, 5);
		raLHC.connectTo(rD984FNW, 3);
		return raLHC;
	}
	public Road genD984FSES(RoundAbout raLHC) {
		Road rD984FSES = new Road(this, 92, "rD984FSES");
		rD984FSES.setStartPositionFrom(raLHC, raLHC.getLength()-7);
		rD984FSES.setStartDirection(129);
		rD984FSES.addPoint(new Point(4,113));
		roads.add(rD984FSES);
		raLHC.connectTo(rD984FSES, raLHC.getLength()-7);
		
		rD984FSES.setCounter(0.3, "counter 2A");
		
		return rD984FSES;
	}
	public Road genD984FSES2(Road rD984FSES) {
		Road rD984FSES2 = new Road(this, 47, "rD984FSES2");
		rD984FSES2.setStartPositionFrom(rD984FSES, 45, 113, 1, (113+90));
		
		for (int i = 0; i<2; ++i) {
			rD984FSES.connectFromiToj(rD984FSES2, 44+i, i);
		}
		roads.add(rD984FSES2);
		return rD984FSES2;
	}
	public Road genD984FSES3_Scenario0(Road rD984FSES2) {
		Road rD984FSES3 = new Road(this, 46, "rD984FSES3");
		rD984FSES3.setStartPositionFrom(rD984FSES2, 1, 113, 1, (113+90));
		for (int i=0; i<2; ++i) {
			rD984FSES2.connectFromiToj(rD984FSES3, i, i);
		}
		roads.add(rD984FSES3);
		return rD984FSES3;
	}
	public Road genD984FSES3_Scenario1(Road rD984FSES2) {
		Road rD984FSES3 = new Road(this, 46, "rD984FSES3");
		rD984FSES3.setStartPositionFrom(rD984FSES2, 1, 113, 1, (113+90));
		rD984FSES2.connectFromiTo(rD984FSES3, 0);
		roads.add(rD984FSES3);
		return rD984FSES3;
	}
	public Road genD984FNWS(RoundAbout raLHC, MultiLaneRoundAbout raEntreeB) {
		Road rD984FNWS = new Road(this, 91, "rD984FNWS");
		rD984FNWS.setDirection(293);
		rD984FNWS.addPoint(new Point(rD984FNWS.getLength()-5,313));
		rD984FNWS.setEndPositionFrom(raLHC, raLHC.getLength()-5,293);
		roads.add(rD984FNWS);
		rD984FNWS.connectTo(raLHC, raLHC.getLength()-5);
		if (this.n == 0) {
			
		} else if (this.n == 1) {
			raEntreeB.connectTo(rD984FNWS, 2);
		}
		rD984FNWS.setCounter(0.702, "coutner 2B");
		return rD984FNWS;
	}
	public MultiLaneRoundAbout genRaEntreeB(Road rD984FSES, Road rD984FSES2, Road rD984FSES3) {
		MultiLaneRoundAbout raEntreeB = new MultiLaneRoundAbout(this, 2, 16, "raEntreeB");
		raEntreeB.getLanes()[0].setStartPositionFrom(rD984FSES, rD984FSES.getLength()-1, 0, 4, rD984FSES.getDirection());
		raEntreeB.setDirection(0);
		raEntreeB.setX(raEntreeB.getLanes()[0].getX()+3/4*this.getCellWidth());
		raEntreeB.setY(raEntreeB.getLanes()[0].getY()-this.getCellHeight());
		this.multiLaneRoundAbouts.add(raEntreeB);
		for (RoundAbout ra: raEntreeB.getLanes()) {
			this.roundAbouts.add(ra);
		}
		rD984FSES.connectTo(raEntreeB, 3);
		rD984FSES2.connectTo(raEntreeB, 4);
		rD984FSES3.connectTo(raEntreeB, 5);
		return raEntreeB;
	}
	public Road genD984FNWS2(Road rD984FNWS, MultiLaneRoundAbout raEntreeB) {
		Road rD984FNWS2 = new Road(this, 46, "rD984FNWS2");
		rD984FNWS2.setDirection(293);
		rD984FNWS2.setStartPositionFrom(rD984FNWS, 0, 293, 1, 293-270); // 293+90 == 293-270
		if (this.n == 0) {
			rD984FNWS2.connectTo(rD984FNWS, 46);
		} else if (this.n == 1) {
			rD984FNWS2.setX(rD984FNWS2.getX()+this.cellWidth);
			rD984FNWS2.setY(rD984FNWS2.getY()+0.5*this.cellWidth);
			raEntreeB.connectTo(rD984FNWS2, 1);
			rD984FNWS2.connectTo(rD984FNWS, 47);
		}
		roads.add(rD984FNWS2);
		return rD984FNWS2;
	}
	public Road genWE1(Road rD984FSES) {
		Road rWE1 = new Road(this, 5, "rWE1");
		rWE1.setStartPositionFrom(rD984FSES, rD984FSES.getLength()-1, rD984FSES.getDirection(), 1, rD984FSES.getDirection());
		rWE1.setDirection(rWE1.getDirection()-10);
		rWE1.setX(rWE1.getX()+this.getCellWidth()/4);
		rWE1.setY(rWE1.getY()-this.getCellWidth()/4);
		if (this.n == 0) {
			rD984FSES.connectTo(rWE1, 0);
			this.roads.add(rWE1);
		} else if (this.n == 1) {
			
		}
		return rWE1;
	}
	public Road genWE2(Road rD984FSES2, Road rWE1) {
		Road rWE2 = new Road(this, 5, "rWE2");
		rWE2.setStartPositionFrom(rD984FSES2, rD984FSES2.getLength()-1, rD984FSES2.getDirection(), 1, rD984FSES2.getDirection());
		rWE2.setDirection(rWE1.getDirection());
		if (this.n == 0) {
			rD984FSES2.connectTo(rWE2, 0);
			this.roads.add(rWE2);
		} else if (this.n == 1) {
			
		}
		return rWE2;
	}
	public Road genRouteDeMeyrinNorthSE1(Road rD984FSES2, Road rWE1, MultiLaneRoundAbout raEntreeB) {
		Road rRouteDeMeyrinNorthSE1 = new Road(this, 7, "rRouteDeMeyrinNorthSE1");
		rRouteDeMeyrinNorthSE1.setStartPositionFrom(rWE1, rWE1.getLength()-1, rWE1.getDirection(), 1, rWE1.getDirection());
		rRouteDeMeyrinNorthSE1.setDirection(rD984FSES2.getDirection());
		roads.add(rRouteDeMeyrinNorthSE1);
		if (this.n == 0) {
			rWE1.connectTo(rRouteDeMeyrinNorthSE1, 0);
		} else if (this.n == 1) {
			raEntreeB.connectTo(rRouteDeMeyrinNorthSE1, 11);
		}
		return rRouteDeMeyrinNorthSE1;
	}
	public Road genRouteDeMeyrinNorthSE2(Road rD984FSES2, Road rRouteDeMeyrinNorthSE1, Road rWE2, MultiLaneRoundAbout raEntreeB) {
		Road rRouteDeMeyrinNorthSE2 = new Road(this, 46, "rRouteDeMeyrinNorthSE2");
		rRouteDeMeyrinNorthSE2.setStartPositionFrom(rWE2, rWE2.getLength()-1, rWE2.getDirection(), 1, rWE2.getDirection());
		rRouteDeMeyrinNorthSE2.setDirection(rD984FSES2.getDirection());
		roads.add(rRouteDeMeyrinNorthSE2);
		if (this.n == 0) {
			rWE2.connectTo(rRouteDeMeyrinNorthSE2, 0);
		} else if (this.n == 1) {
			raEntreeB.connectTo(rRouteDeMeyrinNorthSE2, 10);
		}
		rRouteDeMeyrinNorthSE1.connectFromiToj(rRouteDeMeyrinNorthSE2, 6, 7);
		rRouteDeMeyrinNorthSE2.addPoint(new Point(16, rRouteDeMeyrinNorthSE2.getDirection()+20));
		rRouteDeMeyrinNorthSE2.addPoint(new Point(19, rRouteDeMeyrinNorthSE2.getDirection()));
		return rRouteDeMeyrinNorthSE2;
	}
	public Road genRouteDeMeyrinNorthSE1_2(Road rRouteDeMeyrinNorthSE2, Road rD984FSES2) {
		Road rRouteDeMeyrinNorthSE1_2 = new Road(this, 13, "rRouteDeMeyrinNorthSE1_2");
		rRouteDeMeyrinNorthSE1_2.setStartPositionFrom(rRouteDeMeyrinNorthSE2, 33, rRouteDeMeyrinNorthSE2.getDirection(), 1, rRouteDeMeyrinNorthSE2.getDirection()-90);
		rRouteDeMeyrinNorthSE1_2.setDirection(rD984FSES2.getDirection());
		roads.add(rRouteDeMeyrinNorthSE1_2);
		if (this.n == 0) {
			for (int i=0; i<1;++i) {
				rRouteDeMeyrinNorthSE2.connectFromiToj(rRouteDeMeyrinNorthSE1_2, 32+i, i);
			}
		} else if (this.n == 1) {
			rRouteDeMeyrinNorthSE2.connectFromiTo(rRouteDeMeyrinNorthSE1_2, 32);
		}
		return rRouteDeMeyrinNorthSE1_2;
	}
	public MultiLaneRoundAbout genRaEntreeA(Road rRouteDeMeyrinNorthSE1_2, Road rRouteDeMeyrinNorthSE2) {
		MultiLaneRoundAbout raEntreeA = new MultiLaneRoundAbout(this, 1, 16, "raEntreeA");
		raEntreeA.setDirection(0);
		raEntreeA.getLanes()[0].setPositionFrom(rRouteDeMeyrinNorthSE1_2, 4);
		raEntreeA.setX(raEntreeA.getLanes()[0].getX());
		raEntreeA.setY(raEntreeA.getLanes()[0].getY());
		this.multiLaneRoundAbouts.add(raEntreeA);
		for (RoundAbout ra: raEntreeA.getLanes()) {
			this.roundAbouts.add(ra);
		}
		rRouteDeMeyrinNorthSE1_2.connectTo(raEntreeA, 4);
		rRouteDeMeyrinNorthSE2.connectTo(raEntreeA, 5);
		return raEntreeA;
	}
	public Road genRouteDeMeyrinNorthNW1(MultiLaneRoundAbout raEntreeA, MultiLaneRoundAbout raEntreeB) {
		Road rRouteDeMeyrinNorthNW1 = new Road(this, 46, "rRouteDeMeyrinNorthNW1");
		rRouteDeMeyrinNorthNW1.setStartPositionFrom(raEntreeA.getLanes()[0], 1);
		rRouteDeMeyrinNorthNW1.setDirection(293);
		roads.add(rRouteDeMeyrinNorthNW1);
		if (this.n == 0) {
			raEntreeA.connectTo(rRouteDeMeyrinNorthNW1, 1);
		} else if (this.n == 1) {
			raEntreeA.connectTo(rRouteDeMeyrinNorthNW1, 1);
			rRouteDeMeyrinNorthNW1.connectTo(raEntreeB, 13);
		}
		return rRouteDeMeyrinNorthNW1;
	}
	public Road genRouteDeMeyrinNorthNW2(Road rRouteDeMeyrinNorthNW1, MultiLaneRoundAbout raEntreeB) {
		Road rRouteDeMeyrinNorthNW2 = new Road(this, 8, "rRouteDeMeyrinNorthNW2");
		rRouteDeMeyrinNorthNW2.setStartPositionFrom(rRouteDeMeyrinNorthNW1, 38, 293, 1, 293-270);
		rRouteDeMeyrinNorthNW2.setDirection(293);
		rRouteDeMeyrinNorthNW1.connectFromiToj(rRouteDeMeyrinNorthNW2, 38, 0);
		if (this.n == 0) {
			roads.add(rRouteDeMeyrinNorthNW2);
		} else if (this.n == 1) {
			rRouteDeMeyrinNorthNW2.connectTo(raEntreeB, 14);
		}
		return rRouteDeMeyrinNorthNW2;
	}
	public Road genRouteDeMeyrinNorthNW3(Road rRouteDeMeyrinNorthNW2, MultiLaneRoundAbout raEntreeB) {
		Road rRouteDeMeyrinNorthNW3 = new Road(this, 7, "rRouteDeMeyrinNorthNW3");
		rRouteDeMeyrinNorthNW3.setStartPositionFrom(rRouteDeMeyrinNorthNW2, 1, 293, 1, 293-270);
		rRouteDeMeyrinNorthNW3.setDirection(293);
		rRouteDeMeyrinNorthNW2.connectFromiToj(rRouteDeMeyrinNorthNW3, 0, 0);
		if (this.n == 0) {
			roads.add(rRouteDeMeyrinNorthNW3);
		} else if (this.n == 1) {
			rRouteDeMeyrinNorthNW3.connectTo(raEntreeB, 15);
		}
		return rRouteDeMeyrinNorthNW3;
	}
	public void genRouteDeMeyrinNorthNW1_2(Road rRouteDeMeyrinNorthNW1, MultiLaneRoundAbout raEntreeA) {
		Road rRouteDeMeyrinNorthNW1_2 = new Road(this, 11, "rRouteDeMeyrinNorthNW1_2");
		rRouteDeMeyrinNorthNW1_2.setStartPositionFrom(raEntreeA.getLanes()[0], 2);
		rRouteDeMeyrinNorthNW1_2.setDirection(293);
		rRouteDeMeyrinNorthNW1_2.connectFromiToj(rRouteDeMeyrinNorthNW1, rRouteDeMeyrinNorthNW1_2.getRoadCells().size()-1, 11);
		raEntreeA.connectTo(rRouteDeMeyrinNorthNW1_2, 2);
		roads.add(rRouteDeMeyrinNorthNW1_2);
	}
	public Road genEW1(Road rRouteDeMeyrinNorthNW3, Road rD984FNWS2) {
		Road rEW1 = new Road(this, 6, "rEW1");
		rEW1.setStartPositionFrom(rRouteDeMeyrinNorthNW3, rRouteDeMeyrinNorthNW3.getLength()-1, rRouteDeMeyrinNorthNW3.getDirection(), 1, rRouteDeMeyrinNorthNW3.getDirection());
		rEW1.setDirection(rEW1.getDirection()-13);
		rEW1.setX(rEW1.getX());
		rEW1.setY(rEW1.getY());
		if (this.n == 0) {
			rRouteDeMeyrinNorthNW3.connectTo(rEW1, 0);
			rEW1.connectTo(rD984FNWS2, 0);
			this.roads.add(rEW1);
		}
		return rEW1;
	}
	public Road genEW2(Road rRouteDeMeyrinNorthNW2, Road rD984FNWS, Road rEW1) {
		Road rEW2 = new Road(this, 6, "rEW2");
		rEW2.setStartPositionFrom(rRouteDeMeyrinNorthNW2, rRouteDeMeyrinNorthNW2.getLength()-1, rRouteDeMeyrinNorthNW2.getDirection(), 1, rRouteDeMeyrinNorthNW2.getDirection());
		rEW2.setDirection(rEW1.getDirection());
		if (this.n == 0) {
			rRouteDeMeyrinNorthNW2.connectTo(rEW2, 0);
			rEW2.connectTo(rD984FNWS, 0);
			this.roads.add(rEW2);
		}
		return rEW2;
	}
	public Road genRoutePauliNorthNE(Road rEW1, MultiLaneRoundAbout raEntreeB) {
		Road rRoutePauliNorthNE = new Road(this, 5, "rRoutePauliNorthNE");
		if (this.n == 0) {
			rRoutePauliNorthNE.setStartPositionFrom(rEW1, 2, 15, 2, 15);
			rRoutePauliNorthNE.addPoint(new Point(3, 45));
		} else if (this.n == 1) {
			rRoutePauliNorthNE.setDirection(15);
			rRoutePauliNorthNE.setStartPositionFrom(raEntreeB.getLanes()[0], 15);
			rRoutePauliNorthNE.setX(rRoutePauliNorthNE.getX()+1.5*this.getCellWidth());
			rRoutePauliNorthNE.setY(rRoutePauliNorthNE.getY()+0.5*this.getCellWidth());
			raEntreeB.connectTo(rRoutePauliNorthNE, 0);
			rRoutePauliNorthNE.addPoint(new Point(3, 45));
		}
		roads.add(rRoutePauliNorthNE);
		return rRoutePauliNorthNE;
	}
	public Road genRoutePauliNorthSW(Road rRoutePauliNorthNE) {
		Road rRoutePauliNorthSW = new Road(this, 6, "rRoutePauliNorthSW");
		rRoutePauliNorthSW.setStartPositionFrom(rRoutePauliNorthNE, 5, 225, 1.5, 315);
		rRoutePauliNorthSW.addPoint(new Point(3, 195));
		roads.add(rRoutePauliNorthSW);
		return rRoutePauliNorthSW;
	}
	public Road genRoutePauliSouthNERight(Road rWE2, MultiLaneRoundAbout raEntreeB) {
		Road rRoutePauliSouthNERight = new Road(this, 3, "rRoutePauliSouthNERight");
		rRoutePauliSouthNERight.setStartPositionFrom(rWE2, 4, rWE2.getDirection()-90, 5, rWE2.getDirection()+90);
		roads.add(rRoutePauliSouthNERight);
		if (this.n == 0) {
			
		} else if (this.n == 1) {
			rRoutePauliSouthNERight.connectTo(raEntreeB, 8);
		}
		rRoutePauliSouthNERight.setGenerateVehicules(50);
		rRoutePauliSouthNERight.setCounter(0.5, "counter EntranceB right");
		return rRoutePauliSouthNERight;
	}
	public Road genRoutePauliSouthNELeft(Road rRoutePauliSouthNERight, MultiLaneRoundAbout raEntreeB) {
		Road rRoutePauliSouthNELeft = new Road(this, 3, "rRoutePauliSouthNELeft");
		rRoutePauliSouthNELeft.setStartPositionFrom(rRoutePauliSouthNERight, 0, rRoutePauliSouthNERight.getDirection(), 1, rRoutePauliSouthNERight.getDirection()-90);
		roads.add(rRoutePauliSouthNELeft);
		if (this.n == 0) {
			
		} else if (this.n == 1) {
			rRoutePauliSouthNELeft.connectTo(raEntreeB, 7);
		}
		rRoutePauliSouthNELeft.setGenerateVehicules(50);
		rRoutePauliSouthNELeft.setCounter(0.5, "counter EntranceB left");
		return rRoutePauliSouthNELeft;
	}
	public Road genRoutePauliSouthSW(Road rRoutePauliSouthNELeft, MultiLaneRoundAbout raEntreeB) {
		Road rRoutePauliSouthSW = new Road(this, 3, "rRoutePauliSouthSW");
		rRoutePauliSouthSW.setStartPositionFrom(rRoutePauliSouthNELeft, 3, rRoutePauliSouthNELeft.getDirection()+180, 1.5, rRoutePauliSouthNELeft.getDirection()-90);
		roads.add(rRoutePauliSouthSW);
		if (this.n == 1) {
			rRoutePauliSouthSW.setMaxOutflow(8);
			raEntreeB.connectTo(rRoutePauliSouthSW, 6);
		}
		return rRoutePauliSouthSW;
	}
	public void genCrossRoadEntranceB(Road rRoutePauliSouthNERight, Road rRoutePauliNorthNE, Road rRoutePauliNorthSW, Road rRoutePauliSouthSW, Road rEW1, Road rD984FNWS2, Road rWE1, Road rRouteDeMeyrinNorthSE1, Road rRoutePauliSouthNELeft, Road rEW2, Road rRouteDeMeyrinNorthSE2, Road rD984FSES3, Road rRouteDeMeyrinNorthNW1, Road rWE2) {
		
		// CrossRoad middle roads S -> N
		Road rSN = new Road(this, 8, "rSN");
		rSN.setStartPositionFrom(rRoutePauliSouthNERight, 2, 17, 1.25, rRoutePauliSouthNERight.getDirection());
		rRoutePauliSouthNERight.connectFromiToj(rSN, 2, 0);
		rSN.connectFromiToj(rRoutePauliNorthNE, rSN.getLength()-1, 0);
		//rSN.setMaxSpeed(1);
		roads.add(rSN);

		// CrossRoad middle roads N -> S
		Road rNS = new Road(this, 8, "rNS");
		rNS.setStartPositionFrom(rRoutePauliNorthSW, rRoutePauliNorthSW.getRoadCells().size()-1, 202, 1, 192);
		rNS.connectFromiToj(rRoutePauliSouthSW, rNS.getLength()-1, 0);
		rRoutePauliNorthSW.connectFromiToj(rNS, rRoutePauliNorthSW.getLength()-1, 0);
		//rNS.setMaxSpeed(1);
		roads.add(rNS);

		// CrossRoad middle roads N -> E
		Road rNE = new Road(this, 1, "rNE");
		rNE.setStartPositionFrom(rNS, 2, rEW1.getDirection(), 0.75, rEW1.getDirection());
		rNS.connectFromiToj(rNE, 2, 0);
		rNE.connectFromiToj(rD984FNWS2, 0, 0);
		//rNE.setMaxSpeed(1);
		roads.add(rNE);
		
		// CrossRoad middle roads N -> W
		Road rNW = new Road(this, 3, "rNW");
		rNW.setStartPositionFrom(rNS, 5, rWE1.getDirection(), 0.7, rWE1.getDirection());
		rNS.connectFromiToj(rNW, 4, 0);
		rNW.connectFromiToj(rRouteDeMeyrinNorthSE1, 2, 0);
		//rNW.setMaxSpeed(1);
		roads.add(rNW);
		
		// CrossRoad middle roads S -> W
		Road rSW = new Road(this, 5, "rSW");
		rSW.setStartPositionFrom(rRoutePauliSouthNELeft, rRoutePauliSouthNELeft.getLength()-1, 355, 1, rRoutePauliSouthNELeft.getDirection());
		rRoutePauliSouthNELeft.connectFromiToj(rSW, rRoutePauliSouthNELeft.getLength()-1, 0);
		rSW.connectFromiToj(rEW2, rSW.getLength()-1, 5);
		//rSW.setMaxSpeed(1);
		roads.add(rSW);
		
		// CrossRoad middle roads S -> E
		Road rSE = new Road(this, 1, "rSE");
		rSE.setStartPositionFrom(rSN, 1, 100, 0.5, rSN.getDirection()+90);
		rSN.connectFromiToj(rSE, 0, 0);
		rSE.connectFromiToj(rRouteDeMeyrinNorthSE2, 0, 0);
		//rSE.setMaxSpeed(1);
		roads.add(rSE); 
		
		// CrossRoad middle roads E -> S
		Road rES = new Road(this, 1, "rES");
		rES.setStartPositionFrom(rD984FSES3, rD984FSES3.getLength()-1, rD984FSES3.getDirection(), 1, rD984FSES3.getDirection());
		rD984FSES3.connectFromiToj(rES, rD984FSES3.getLength()-1, 0);
		rES.connectFromiToj(rNS, 0, rNS.getLength()-1);
		//rES.setMaxSpeed(1);
		roads.add(rES);
		
		// CrossRoad middle roads E -> N
		Road rEN = new Road(this, 4, "rEN");
		rEN.setStartPositionFrom(rWE1, 2, rWE1.getDirection()-90, 1, rWE1.getDirection()-90);
		rWE1.connectFromiToj(rEN, 1, 0);
		rEN.connectFromiToj(rRoutePauliNorthNE, rEN.getLength()-1, 0);
		//rEN.setMaxSpeed(1);
		roads.add(rEN);
		
		// CrossRoad middle roads W -> S
		Road rWS = new Road(this, 4, "rWS");
		rWS.setStartPositionFrom(rRouteDeMeyrinNorthNW1, rRouteDeMeyrinNorthNW1.getLength()-1, rRouteDeMeyrinNorthNW1.getDirection()-30, 1, rRouteDeMeyrinNorthNW1.getDirection());
		rRouteDeMeyrinNorthNW1.connectFromiToj(rWS, rRouteDeMeyrinNorthNW1.getLength()-1, 0);
		rWS.connectFromiToj(rNS, rWS.getLength()-1, 5);
		//rWS.setMaxSpeed(1);
		roads.add(rWS);
		
		// CrossRoad middle roads W -> N
		Road rWN = new Road(this, 1, "rWN");
		rWN.setStartPositionFrom(rEW1, 1, rEW1.getDirection()+90, 0.75, rEW1.getDirection()+90);
		rEW1.connectFromiToj(rWN, 0, 0);
		rWN.connectFromiToj(rRoutePauliNorthNE, 0, 0);
		//rWN.setMaxSpeed(1);
		roads.add(rWN);
		
		// rWS and rWE1:
		rWS.getRoadCells().get(3).getOverlapedCells().add(rWE1.getRoadCells().get(2));
		rWE1.getRoadCells().get(2).getOverlapedCells().add(rWS.getRoadCells().get(3));
		
		// rNS and rWE1:
		rNS.getRoadCells().get(5).getOverlapedCells().add(rWE1.getRoadCells().get(1));
		rWE1.getRoadCells().get(1).getOverlapedCells().add(rNS.getRoadCells().get(5));
		
		// rNS and rWE2:
		rNS.getRoadCells().get(6).getOverlapedCells().add(rWE2.getRoadCells().get(1));
		rWE2.getRoadCells().get(1).getOverlapedCells().add(rNS.getRoadCells().get(6));
	}
	public void genRouteBellSW(MultiLaneRoundAbout raEntreeA) {
		Road rRouteBellSW = new Road(this, 3, "rRouteBellSW");
		rRouteBellSW.setStartPositionFrom(raEntreeA.getLanes()[0], 6);
		rRouteBellSW.setDirection(200);
		raEntreeA.connectTo(rRouteBellSW, 6);
		roads.add(rRouteBellSW);
	}
	public void genRouteBellNE(MultiLaneRoundAbout raEntreeA) {
		Road rRouteBellNE = new Road(this, 3, "rRouteBellNE");
		rRouteBellNE.setEndPositionFrom(raEntreeA.getLanes()[0], 7);
		rRouteBellNE.setX(rRouteBellNE.getX()+this.getCellHeight());
		rRouteBellNE.setDirection(0);
		rRouteBellNE.connectTo(raEntreeA, 7);
		roads.add(rRouteBellNE);
		rRouteBellNE.setGenerateVehicules(50);
	}
	public void genRouteBellNERight(MultiLaneRoundAbout raEntreeA) {
		Road rRouteBellNERight = new Road(this, 3, "rRouteBellNERight");
		rRouteBellNERight.setEndPositionFrom(raEntreeA.getLanes()[0], 8);
		rRouteBellNERight.setDirection(0);
		rRouteBellNERight.connectTo(raEntreeA, 8);
		roads.add(rRouteBellNERight);
	}
	public void genRouteDeMeyrinSouthSE(MultiLaneRoundAbout raEntreeA) {
		Road rRouteDeMeyrinSouthSE = new Road(this, 20, "rRouteDeMeyrinSouthSE");
		rRouteDeMeyrinSouthSE.setStartPositionFrom(raEntreeA.getLanes()[0], 10);
		rRouteDeMeyrinSouthSE.setStartDirection(100);
		rRouteDeMeyrinSouthSE.addPoint(new Point(1, 113));
		roads.add(rRouteDeMeyrinSouthSE);
		raEntreeA.connectTo(rRouteDeMeyrinSouthSE, 10);
	}
	public void genRouteDeMeyrinSouthNW(MultiLaneRoundAbout raEntreeA) {
		Road rRouteDeMeyrinSouthNW = new Road(this, 20, "rRouteDeMeyrinSouthNW");
		rRouteDeMeyrinSouthNW.setDirection(293);
		rRouteDeMeyrinSouthNW.setEndPositionFrom(raEntreeA.getLanes()[0], raEntreeA.getLanes()[0].getLength()-4,293);
		roads.add(rRouteDeMeyrinSouthNW);
		rRouteDeMeyrinSouthNW.connectTo(raEntreeA, raEntreeA.getLanes()[0].getLength()-4);
		rRouteDeMeyrinSouthNW.setGenerateVehicules(50);
	}
	public void genCheminMaisonnexN(MultiLaneRoundAbout raEntreeA) {
		Road rCheminMaisonnexN = new Road(this, 2, "rCheminMaisonnexN");
		rCheminMaisonnexN.setStartPositionFrom(raEntreeA.getLanes()[0], raEntreeA.getLanes()[0].getLength()-1);
		rCheminMaisonnexN.setStartDirection(15);
		roads.add(rCheminMaisonnexN);
		raEntreeA.connectTo(rCheminMaisonnexN, raEntreeA.getLanes()[0].getLength()-1);
	}
	public void genCheminMaisonnexS(MultiLaneRoundAbout raEntreeA) {
		Road rCheminMaisonnexS = new Road(this, 2, "rCheminMaisonnexS");
		rCheminMaisonnexS.setDirection(195);
		rCheminMaisonnexS.setEndPositionFrom(raEntreeA.getLanes()[0], 0,195);
		roads.add(rCheminMaisonnexS);
		rCheminMaisonnexS.connectTo(raEntreeA, 0);
	}
	public Road genC5NE(RoundAbout raLHC) {
		Road rC5NE = new Road(this, 30, "rC5NE");
		rC5NE.setStartPositionFrom(raLHC, raLHC.getLength()-2);
		rC5NE.setStartDirection(30);
		roads.add(rC5NE);
		raLHC.connectTo(rC5NE, raLHC.getLength()-2);
		return rC5NE;
	}
	public Road genC5SW(RoundAbout raLHC) {
		Road rC5SW = new Road(this, 30, "rC5SW");
		rC5SW.setDirection(210);
		rC5SW.setEndPositionFrom(raLHC, raLHC.getLength()-1,293);
		roads.add(rC5SW);
		rC5SW.connectTo(raLHC, raLHC.getLength()-1);
		rC5SW.setGenerateVehicules(50);
		return rC5SW;
	}
	public Road genTunnelSE(Road rC5NE, Road rC5SW) {
		Road rTunnelSE;
		if (this.n == 0) {
			rTunnelSE = new Road(this, 38, "rTunnelSE");
			rTunnelSE.setStartPositionFrom(rC5SW, 23, 120, 0.4, 0);
			rTunnelSE.addPoint(new Point(2, 180));
			rTunnelSE.addPoint(new Point(21, 110));
			rTunnelSE.addPoint(new Point(28, 185));
			roads.add(rTunnelSE);
			rC5NE.getRoadCells().get(7).setOutCell(rTunnelSE.getRoadCells().get(2));
		} else {
			rTunnelSE = new Road(this, 35, "rTunnelSE");
			rTunnelSE.setStartPositionFrom(rC5NE, 7, 120);
			rTunnelSE.addPoint(new Point(2, 180));
			rTunnelSE.addPoint(new Point(20, 110));
			rTunnelSE.addPoint(new Point(27, 185));
			roads.add(0,rTunnelSE);
			rC5NE.getRoadCells().get(7).setOutCell(rTunnelSE.getRoadCells().get(0));
		}
		
		rC5NE.addExit("rTunnelSE", 7);
		rTunnelSE.addEnter("rC5NE", 2);
		rTunnelSE.getRoadCells().get(0).setInCell(rC5NE.getRoadCells().get(7));
		rTunnelSE.setUnderground(14, 16, true);
		return rTunnelSE;
	}
	public Road genTunnelNW(Road rTunnelSE, Road rC5NE, Road rC5SW) {
		Road rTunnelNW = new Road(this, 39, "rTunnelNW");
		rTunnelNW.setDirection(5);
		if (this.n == 0) {
			rTunnelNW.addPoint(new Point(11, 290));
			rTunnelNW.addPoint(new Point(18, 0));
			rTunnelNW.addPoint(new Point(rTunnelNW.getRoadCells().size()-2, 300));
			rTunnelNW.setX(rTunnelSE.getX()+9.15*cellWidth);
			rTunnelNW.setY(rTunnelSE.getY()+32*cellWidth);
			roads.add(rTunnelNW);
			rTunnelNW.getRoadCells().get(rTunnelNW.getLength()-2).setOutCell(rC5NE.getRoadCells().get(13));
			rC5NE.getRoadCells().get(13).setInCell(rTunnelNW.getRoadCells().get(rTunnelNW.getLength()-2));
			rTunnelNW.addExit(rC5NE.getName(), rTunnelNW.getLength()-2);
			rTunnelNW.setMaxSpeed(1);
		} else {
			rTunnelNW.addPoint(new Point(9, 290));
			rTunnelNW.addPoint(new Point(16, 0));
			rTunnelNW.setX(rTunnelSE.getX()+9*cellWidth);
			rTunnelNW.setY(rTunnelSE.getY()+29*cellWidth);
			roads.add(0,rTunnelNW);
			rTunnelNW.getRoadCells().get(rTunnelNW.getLength()-1).setOutCell(rC5NE.getRoadCells().get(13));
			rTunnelNW.addExit(rC5NE.getName(), rTunnelNW.getLength()-1);
		}
		rC5NE.addEnter("rTunnelNW", 13);
		rTunnelNW.setUnderground(17, 20, true);
		rTunnelNW.setGenerateVehicules(40);
		return rTunnelNW;
	}
	public void genLightPhases(Road rD984FSES, Road rD984FSES2, Road rD984FSES3, Road rRouteDeMeyrinNorthNW1, Road rRouteDeMeyrinNorthNW2, Road rRouteDeMeyrinNorthNW3, Road rRoutePauliSouthNELeft, Road rRoutePauliSouthNERight, Road rRoutePauliNorthNE) {
		// Phase 1-----------------------------------------------------------------------------------------
		Phase P1 = new Phase(this, 10, 15);
		
		P1.addConcernedRoad(rRouteDeMeyrinNorthNW1);
		P1.addConcernedRoad(rRouteDeMeyrinNorthNW2);
		P1.addConcernedRoad(rRouteDeMeyrinNorthNW3);
		
		P1.addRoadToCheck(rRouteDeMeyrinNorthNW1);
		// Phase 2-----------------------------------------------------------------------------------------
		Phase P2 = new Phase(this, 30, 40);
		
		P2.addConcernedRoad(rRouteDeMeyrinNorthNW2);
		P2.addConcernedRoad(rRouteDeMeyrinNorthNW3);
		P2.addConcernedRoad(rD984FSES);
		P2.addConcernedRoad(rD984FSES2);
		P2.addConcernedRoad(rD984FSES3);
		
		P2.addRoadToCheck(rRouteDeMeyrinNorthNW2);
		P2.addRoadToCheck(rRouteDeMeyrinNorthNW3);
		P2.addRoadToCheck(rD984FSES);
		P2.addRoadToCheck(rD984FSES2);
		
		// Phase 3-----------------------------------------------------------------------------------------
		Phase P3 = new Phase(this, 10, 15);
		
		P3.addConcernedRoad(rD984FSES3);
		P3.addConcernedRoad(rRoutePauliSouthNELeft);
		P3.addConcernedRoad(rRoutePauliSouthNERight);
		
		P3.addRoadToCheck(rD984FSES3);
		P3.addRoadToCheck(rRoutePauliSouthNELeft);
		P3.addRoadToCheck(rRoutePauliSouthNERight);
		
		// Phase 4-----------------------------------------------------------------------------------------
		Phase P4 = new Phase(this, 10, 15);
		
		P4.addConcernedRoad(rRoutePauliNorthNE);
		
		P4.addRoadToCheck(rRoutePauliNorthNE);
		
		// CrossRoads Entree B TLS-------------------------------------------------------------------------
		TrafficLightsSystem crEntreeBTLS = new TrafficLightsSystem();
		crEntreeBTLS.addPhase(P1);
		crEntreeBTLS.addPhase(P2);
		crEntreeBTLS.addPhase(P3);
		crEntreeBTLS.addPhase(P4);
		
		this.trafficLightsSystems.add(crEntreeBTLS);
		crEntreeBTLS.setTrafficLightsRed();
		rRouteDeMeyrinNorthNW1.setTrafficLightRed(true);
		crEntreeBTLS.initializePhases();
	}
	private void initCERNArea() {
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
		tmpX[0] = 115*cellWidth;
		tmpX[1] = 131*cellWidth;
		tmpX[2] = 250*cellWidth;
		tmpX[3] = 236*cellWidth;
		tmpX[4] = 119*cellWidth;
		tmpY[0] = 43*cellWidth;
		tmpY[1] = 17*cellWidth;
		tmpY[2] = 60*cellWidth;
		tmpY[3] = 98*cellWidth;
		tmpY[4] = 48*cellWidth;
		Polygon tmp2 = new Polygon(tmpX, tmpY, 5);
		zones.add(tmp2);
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
	}
	public Road getRoad(String name) {
		for (Road r: this.getRoads()) {
			if (r.getName().equals(name)) {
				return r;
			}
		}
		return null;
	}
	public AllNetworkRides getAllRides(String roadName) {
		
		for (AllNetworkRides anr: allNetworkRides) {
			if (anr.getRoadName().equals(roadName)) {
				return anr;
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
	public ArrayList<Ride> selectRidesWithProbability(String roadName) {
		ArrayList<Ride> allRides = new ArrayList<Ride>();
		Ride chosenRide = new Ride();
		ArrayList<Float> probas = new ArrayList<Float>();
		float totalProba = 0;
		for (AllNetworkRides anr: allNetworkRides) {
			if (anr.getRoadName().equals(roadName)) {
				
				for (int i=0 ; i<anr.getNetworkRides().size() ; i++) {
					
					Ride ride = anr.getNetworkRides().get(i);
					
					if (ride.getFlow().get(sim.getSimState().getHours())>0) {
						totalProba += ride.getFlow().get(sim.getSimState().getHours());
						probas.add(totalProba);
					} else {
						probas.add(0f);
					}
					
				}
				double random = Math.random();
				
				if (probas.size() == 0) {
					Utils.logErrorln("No probability in selectARideWithProbability() for " + roadName + "");
					chosenRide = new Ride();
					//return voidRide;
				} else if (probas.size() == 1) {
					//Utils.log("Only one probability int selectARideWithProbability for " + roadName + "\n");
					//return anr.getNetworkRides().get(0).clone();
					chosenRide = anr.getNetworkRides().get(0).clone();
				} else {
					for (int i=0 ; i<probas.size() ; i++) {
						if (random < probas.get(i) / (float) totalProba) {
							chosenRide = anr.getNetworkRides().get(i).clone();
							allRides.add(anr.getNetworkRides().get(i).clone());
							//return anr.getNetworkRides().get(i).clone();
						}
					}
					if (chosenRide.getNextConnections().isEmpty()) {
						Utils.logError("No adapted probability found for " + roadName + " : ");
						for (Float f: chosenRide.getFlow()) {
							System.out.print(f + " ");
						}
						for (Road road: this.getRoads()) {
							if (this.getAllRides(road.getName()) != null) {
								if (road.getName().equals(roadName)) {
									System.out.println(road.getFlow());
								}
							}
						}
					}
				}
			}
		}
		
		if (chosenRide == null) {
			chosenRide = new Ride();
		}
		
		for (AllNetworkRides anr: allNetworkRides) {
			
			if (anr.getRoadName().equals(roadName)) {
				for (Ride ride: anr.getNetworkRides()) {
					if (ride.getNextConnections().size() > 0 && chosenRide.getNextConnections().size() > 0) {
						if (ride.getNextConnections().get(ride.getNextConnections().size()-1).getName().equals(chosenRide.getNextConnections().get(chosenRide.getNextConnections().size()-1).getName())) {
							allRides.add(ride.clone());
						}
					}
				}
			}
		}
		
		return allRides;
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
	public void cleanAllNetworkRides(int x) {
		if (!this.getAllNetworkRides().isEmpty()) {
			for (AllNetworkRides ANR: this.getAllNetworkRides()) {
				ArrayList<Integer> elmtsToChange = new ArrayList<Integer>();
				for (int i=0; i<ANR.getNetworkRides().size()-2; ++i) {
					for (int j=(i+1); j<ANR.getNetworkRides().size()-1; ++j) {
						ArrayList<Connection> riConnections = ANR.getNetworkRides().get(i).getNextConnections();
						ArrayList<Connection> rjConnections = ANR.getNetworkRides().get(j).getNextConnections();
						if (riConnections.get(riConnections.size()-1).getName().equals(rjConnections.get(rjConnections.size()-1).getName())) {
							if (rjConnections.size() > riConnections.size()+x) {
								elmtsToChange.add(j);
							} else if (rjConnections.size()+x < riConnections.size()){
								elmtsToChange.add(i);
							}
						}
					}
				}
				HashSet<Integer> set = new HashSet<Integer>();
				set.addAll(elmtsToChange);
				elmtsToChange = new ArrayList<Integer>(set);
				Collections.sort(elmtsToChange);
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
		Utils.logInfo("Generating Rides ... ");
		Utils.tick();
		for (Road r: this.roads) {
			boolean generateAtLeastOne = false;
			for (Float i: r.getFlow()) {
				if (i>0) {
					generateAtLeastOne = true;
				}
			}
			if (generateAtLeastOne) {
				r.generateRides(n);
			}
		}
		int i=0;
		for (AllNetworkRides anr: allNetworkRides) {
			i += anr.getNetworkRides().size();
		}
		Utils.log(Integer.toString(i));
		Utils.logTime();
		
		if (i != 356 && i != 535) {
			Utils.logWarning("Number of rides has changed, expected ");
			if (this.n == 0)
				Utils.logln("535, got " + i);
			else if (this.n == 1)
				Utils.logln("356, got " + i);
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
		Utils.logErrorln("While trying to find " + roadName + " in network");
		return null;
	}
	public void setNetworkSize() {
		this.setCellWidth((int) (Math.pow(2, 1+sim.getMenuState().getSizeOfNetwork().getCurrentValue())));
		this.setCellHeight((int) (Math.pow(2, 1+sim.getMenuState().getSizeOfNetwork().getCurrentValue())));
	}
	public void restart() {
		vehicles = new ArrayList<Vehicle>();
		for (Road road: roads) {
			for (Cell cell: road.getRoadCells()) {
				cell.setVehicle(null);
			}
			road.setLeakyBucket(new ArrayList<Vehicle>());
		}
		for (RoundAbout ra: roundAbouts) {
			for (Cell cell: ra.getRoadCells()) {
				cell.setVehicle(null);
			}
		}
		for (CrossRoad cr: crossRoads) {
			for (Cell cell: cr.getMiddleCells()) {
				cell.setVehicle(null);
			}
		}
		numberOfVehicles = 0;
	}
	// Getters & setters ====================================================================================
	public ArrayList<TrafficLightsSystem> getTrafficLightsSystems() {
		return trafficLightsSystems;
	}
	public int getN() {
		return n;
	}
	public int getNumberOfVehicles() {
		return numberOfVehicles;
	}
	public void increaseNumberOfVehicles(int n) {
		numberOfVehicles += n;
	}
	public void setVehicles(ArrayList<Vehicle> vehicles) {
		this.vehicles = vehicles;
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
	public Simulator getSimulation() {
		return this.sim;
	}
	public ArrayList<CrossRoad> getCrossRoads() {
		return this.crossRoads;
	}
	public ArrayList<Road> getRoads() {
		return this.roads;
	}
	public ArrayList<MultiLaneRoundAbout> getMultiLaneRoundAbouts() {
		return multiLaneRoundAbouts;
	}
	public ArrayList<RoundAbout> getRoundAbouts() {
		return this.roundAbouts;
	}
	public ArrayList<MaxVehicleOutflow> getMaxVehicleOutflows() {
		return this.maxVehicleOutflows;
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
