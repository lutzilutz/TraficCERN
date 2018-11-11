package data;

import java.text.DecimalFormat;
import java.util.ArrayList;

import elements.Ride;
import elements.Road;
import main.Simulation;
import network.AllNetworkRides;
import network.Network;
import states.SimSettingsState;
import utils.Utils;

public class DataManager {
	
	public static boolean useProbabilities = true;
	
	// ============================================================================================
	// Numerical values ===========================================================================
	// ============================================================================================
	
	private static float value = 0;
	private static double repartition1 = 0;
	private static double repartition2 = 0;
	private static double repartitionRHMorning = 0;
	private static double repartitionRHEvening = 0;
	private static double repartitionRH7 = 0;
	private static double repartitionRH8 = 0;
	private static double repartitionRH9 = 0;
	private static double repartitionRH17 = 0;
	private static double repartitionRH18 = 0;
	private static double repartitionRH19 = 0;
	private static int numberOfSameRide = 0;
	private static double repartitionFrToFr = 0;
	private static boolean useFrToFr = false;
	
	// Theoritical default values -------------------------
	
	public static int nFrGe = 12500;
	public static int nFrGe_fromSW = 88;
	public static int nFrGe_fromNW = 1;
	public static int nFrGe_fromNE = 2;
	public static int nFrGe_fromTun = 9;
	public static int nFrGe_7 = 30;
	public static int nFrGe_8 = 40;
	public static int nFrGe_9 = 30;
	
	public static int nGeFr = 12500;
	public static int nGeFr_toSW = 88;
	public static int nGeFr_toNW = 1;
	public static int nGeFr_toNE = 2;
	public static int nGeFr_toTun = 9;
	public static int nGeFr_17 = 30;
	public static int nGeFr_18 = 40;
	public static int nGeFr_19 = 30;
	
	public static int nFrFr = 6750;
	
	public static int nToE = 1605;
	public static int nToE_fromSW = 30;
	public static int nToE_fromNW = 65;
	public static int nToE_fromNE = 5;
	public static int nToE_7 = 33;
	public static int nToE_8 = 47;
	public static int nToE_9 = 20;
	
	public static int nFromE = 1500;
	public static int nFromE_toSW = 30;
	public static int nFromE_toNW = 65;
	public static int nFromE_toNE = 5;
	public static int nFromE_17 = 33;
	public static int nFromE_18 = 40;
	public static int nFromE_19 = 27;
	
	public static int nToA = 554;
	public static int nToA_fromFr = 30;
	public static int nToA_fromGe = 70;
	public static int nToA_7 = 20;
	public static int nToA_8 = 47;
	public static int nToA_9 = 33;
	
	public static int nFromA = 116;
	public static int nFromA_toFr = 30;
	public static int nFromA_toGe = 70;
	public static int nFromA_17 = 62;
	public static int nFromA_18 = 34;
	public static int nFromA_19 = 4;
	
	public static int nToB = 970;
	public static int nToB_fromFr = 70;
	public static int nToB_fromGe = 30;
	public static int nToB_7 = 23;
	public static int nToB_8 = 47;
	public static int nToB_9 = 40;
	
	public static int nFromB = 351;
	public static int nFromB_toFr = 70;
	public static int nFromB_toGe = 30;
	public static int nFromB_17 = 33;
	public static int nFromB_18 = 32;
	public static int nFromB_19 = 35;
	
	// Theoritical chosen values --------------------------
	public static int nFrGeChosen = 0;
	public static int nGeFrChosen = 0;
	public static int nToEChosen = 0;
	public static int nFromEChosen = 0;
	public static int nToAChosen = 0;
	public static int nFromAChosen = 0;
	public static int nToBChosen = 0;
	public static int nFromBChosen = 0;
	
	// Empirical values -----------------------------------
	public static int nFrGeEmpiric = 0;
	public static int nGeFrEmpiric = 0;
	public static int nToEEmpiric = 0;
	public static int nFromEEmpiric = 0;
	public static int nToAEmpiric = 0;
	public static int nFromAEmpiric = 0;
	public static int nToBEmpiric = 0;
	public static int nFromBEmpiric = 0;
	
	// ============================================================================================
	// Statistical values =========================================================================
	// ============================================================================================
	
	public static double[][] probas = new double[][]{
		
		{0.000,	0.327,	0.368,	0.325,	0.405,	0.531,	0.405,	0.604},
		{0.049,	0.000,	0.039,	0.035,	0.043,	0.057,	0.043,	0.065},
		{0.210,	0.148,	0.000,	0.148,	0.184,	0.242,	0.184,	0.274},
		{0.219,	0.155,	0.175,	0.155,	0.000,	0.000,	0.000,	0.000},
		{0.044,	0.031,	0.035,	0.000,	0.039,	0.051,	0.039,	0.058},
		{0.070,	0.049,	0.056,	0.049,	0.000,	0.080,	0.000,	0.000},
		{0.374,	0.265,	0.299,	0.264,	0.329,	0.000,	0.329,	0.000},
		{0.034,	0.024,	0.027,	0.024,	0.000,	0.039,	0.000,	0.000}
	};
	public static int[] flowPerExit = new int[] {
		1828, 671, 717, 213, 75, 195, 43, 0, 704, 142, 583, 663, 141, 226, 1172, 111
	};
	
	
	// ============================================================================================
	// Light system ===============================================================================
	// ============================================================================================
	
	public static int cycle1LTSmin = 0;
	public static int cycle1LTSmax = 15;
	public static int cycle2LTSmin = 0;
	public static int cycle2LTSmax = 35;
	public static int cycle3LTSmin = 0;
	public static int cycle3LTSmax = 15;
	public static int cycle4LTSmin = 0;
	public static int cycle4LTSmax = 15;
	
	public static int randomValue = 0;
	
	public static void loadData(Simulation simulation) {
		
		//applyDataToRides(simulation);
		//applyRidesToRoads(simulation);
		
	}
	public static boolean lastRoadIs(Ride r, String roadName) {
		if (r.getNextConnections().size()>0) {
			if (r.getNextConnections().get(r.getNextConnections().size()-1).getName().equals(roadName)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public static void applyDataProba(Simulation simulation) {
		
		System.out.println("Trying to apply ...");
		
		if (useProbabilities) {
			
			Utils.log("applying data (proba) to Network ... ");
			applyDataToRidesProba(simulation);
			applyDataToRoadsProba(simulation);
			Utils.log("done\n");
			
		}
	}
	public static void applyDataToRidesProba(Simulation simulation) {
		
		Network n = simulation.getSimState().getNetwork();
		SimSettingsState settings = simulation.getSimSettingsState();
		
		for (AllNetworkRides anr: n.getAllNetworkRides()) {
			for (Ride r: anr.getNetworkRides()) {
				r.setNumberOfSameRide(numberOfSameRides(simulation, r.getRoadName(), r.getNextConnections().get(r.getNextConnections().size()-1).getName()));
			}
		}
		
		/*System.out.println("Printing all rides -----");
		for (AllNetworkRides anr: n.getAllNetworkRides()) {
			for (Ride ride: anr.getNetworkRides()) {
				System.out.println(ride);
			}
		}
		System.out.println("-----");*/
		// From A ===========================================================================================
		for (Ride r: n.getAllRides("rD884NE").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {r.setFlow((float) (probas[0][0])*flowPerExit[0] / (float) (r.getNumberOfSameRide()));}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {r.setFlow((float) (probas[1][0])*flowPerExit[0] / (float) (r.getNumberOfSameRide()));}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {r.setFlow((float) (probas[2][0])*flowPerExit[0] / (float) (r.getNumberOfSameRide()));}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rD884CERN")) {r.setFlow((float) (probas[3][0])*flowPerExit[0] / (float) (r.getNumberOfSameRide()));}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {r.setFlow((float) (probas[4][0])*flowPerExit[0] / (float) (r.getNumberOfSameRide()));}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {r.setFlow((float) (probas[5][0])*flowPerExit[0] / (float) (r.getNumberOfSameRide()));}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {r.setFlow((float) (probas[6][0])*flowPerExit[0] / (float) (r.getNumberOfSameRide()));}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {r.setFlow((float) (probas[7][0])*flowPerExit[0] / (float) (r.getNumberOfSameRide()));}
			
			else {r.setFlow(0);}
			
		}
		// From B ===========================================================================================
		for (Ride r: n.getAllRides("rRueDeGeneveSE").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {r.setFlow((float) (probas[0][1])*flowPerExit[1] / (float) (r.getNumberOfSameRide()));}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {r.setFlow((float) (probas[1][1])*flowPerExit[1] / (float) (r.getNumberOfSameRide()));}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {r.setFlow((float) (probas[2][1])*flowPerExit[1] / (float) (r.getNumberOfSameRide()));}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {r.setFlow((float) (probas[3][1])*flowPerExit[1] / (float) (r.getNumberOfSameRide()));}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {r.setFlow((float) (probas[4][1])*flowPerExit[1] / (float) (r.getNumberOfSameRide()));}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {r.setFlow((float) (probas[5][1])*flowPerExit[1] / (float) (r.getNumberOfSameRide()));}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {r.setFlow((float) (probas[6][1])*flowPerExit[1] / (float) (r.getNumberOfSameRide()));}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {r.setFlow((float) (probas[7][1])*flowPerExit[1] / (float) (r.getNumberOfSameRide()));}
			
			else {r.setFlow(0);}
			
		}
		// From C ===========================================================================================
		for (Ride r: n.getAllRides("rRueGermaineTillionSW").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {r.setFlow((float) (probas[0][2])*flowPerExit[2] / (float) (r.getNumberOfSameRide()));}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {r.setFlow((float) (probas[1][2])*flowPerExit[2] / (float) (r.getNumberOfSameRide()));}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {r.setFlow((float) (probas[2][2])*flowPerExit[2] / (float) (r.getNumberOfSameRide()));}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {r.setFlow((float) (probas[3][2])*flowPerExit[2] / (float) (r.getNumberOfSameRide()));}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {r.setFlow((float) (probas[4][2])*flowPerExit[2] / (float) (r.getNumberOfSameRide()));}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {r.setFlow((float) (probas[5][2])*flowPerExit[2] / (float) (r.getNumberOfSameRide()));}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {r.setFlow((float) (probas[6][2])*flowPerExit[2] / (float) (r.getNumberOfSameRide()));}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {r.setFlow((float) (probas[7][2])*flowPerExit[2] / (float) (r.getNumberOfSameRide()));}
			
			else {r.setFlow(0);}
		}
		// From D ===========================================================================================
		for (Ride r: n.getAllRides("rC5SW").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {r.setFlow((float) (probas[0][3])*flowPerExit[3] / (float) (r.getNumberOfSameRide()));}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {r.setFlow((float) (probas[1][3])*flowPerExit[3] / (float) (r.getNumberOfSameRide()));}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {r.setFlow((float) (probas[2][3])*flowPerExit[3] / (float) (r.getNumberOfSameRide()));}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {r.setFlow((float) (probas[3][3])*flowPerExit[3] / (float) (r.getNumberOfSameRide()));}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {r.setFlow((float) (probas[4][3])*flowPerExit[3] / (float) (r.getNumberOfSameRide()));}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {r.setFlow((float) (probas[5][3])*flowPerExit[3] / (float) (r.getNumberOfSameRide()));}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {r.setFlow((float) (probas[6][3])*flowPerExit[3] / (float) (r.getNumberOfSameRide()));}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {r.setFlow((float) (probas[7][3])*flowPerExit[3] / (float) (r.getNumberOfSameRide()));}
			
			else {r.setFlow(0);}
		}
		// From E1 (left) ===================================================================================
		for (Ride r: n.getAllRides("rRoutePauliSouthNELeft").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {r.setFlow((float) (probas[0][4])*flowPerExit[4] / (float) (r.getNumberOfSameRide()));}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {r.setFlow((float) (probas[1][4])*flowPerExit[4] / (float) (r.getNumberOfSameRide()));}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {r.setFlow((float) (probas[2][4])*flowPerExit[4] / (float) (r.getNumberOfSameRide()));}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {r.setFlow((float) (probas[3][4])*flowPerExit[4] / (float) (r.getNumberOfSameRide()));}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {r.setFlow((float) (probas[4][4])*flowPerExit[4] / (float) (r.getNumberOfSameRide()));}
			
			else {r.setFlow(0);}
		}
		// From E1 (right) ==================================================================================
		for (Ride r: n.getAllRides("rRoutePauliSouthNERight").getNetworkRides()) {
			// To L1 -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rRoutePauliSouthSW")) {r.setFlow((float) (probas[5][4])*flowPerExit[4] / (float) (r.getNumberOfSameRide()));}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {r.setFlow((float) (probas[6][4])*flowPerExit[4] / (float) (r.getNumberOfSameRide()));}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {r.setFlow((float) (probas[7][4])*flowPerExit[4] / (float) (r.getNumberOfSameRide()));}
			
			else {r.setFlow(0);}
		}
		// From E3 ===========================================================================================
		for (Ride r: n.getAllRides("rRouteDeMeyrinSouthNW").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {r.setFlow((float) (probas[0][5])*flowPerExit[5] / (float) (r.getNumberOfSameRide()));}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {r.setFlow((float) (probas[1][5])*flowPerExit[5] / (float) (r.getNumberOfSameRide()));}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {r.setFlow((float) (probas[2][5])*flowPerExit[5] / (float) (r.getNumberOfSameRide()));}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {r.setFlow((float) (probas[3][5])*flowPerExit[5] / (float) (r.getNumberOfSameRide()));}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {r.setFlow((float) (probas[4][5])*flowPerExit[5] / (float) (r.getNumberOfSameRide()));}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {r.setFlow((float) (probas[5][5])*flowPerExit[5] / (float) (r.getNumberOfSameRide()));}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {r.setFlow((float) (probas[6][5])*flowPerExit[5] / (float) (r.getNumberOfSameRide()));}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {r.setFlow((float) (probas[7][5])*flowPerExit[5] / (float) (r.getNumberOfSameRide()));}
			
			else {r.setFlow(0);}
		}
		// From E4 ===========================================================================================
		for (Ride r: n.getAllRides("rRouteBellNE").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {r.setFlow((float) (probas[0][6])*flowPerExit[6] / (float) (r.getNumberOfSameRide()));}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {r.setFlow((float) (probas[1][6])*flowPerExit[6] / (float) (r.getNumberOfSameRide()));}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {r.setFlow((float) (probas[2][6])*flowPerExit[6] / (float) (r.getNumberOfSameRide()));}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {r.setFlow((float) (probas[3][6])*flowPerExit[6] / (float) (r.getNumberOfSameRide()));}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {r.setFlow((float) (probas[4][6])*flowPerExit[6] / (float) (r.getNumberOfSameRide()));}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {r.setFlow((float) (probas[5][6])*flowPerExit[6] / (float) (r.getNumberOfSameRide()));}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {r.setFlow((float) (probas[6][6])*flowPerExit[6] / (float) (r.getNumberOfSameRide()));}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {r.setFlow((float) (probas[7][6])*flowPerExit[6] / (float) (r.getNumberOfSameRide()));}
			
			else {r.setFlow(0);}
		}
		// From F ===========================================================================================
		for (Ride r: n.getAllRides("rSortieCERNNW").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {r.setFlow((float) (probas[0][7])*flowPerExit[7] / (float) (r.getNumberOfSameRide()));}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {r.setFlow((float) (probas[1][7])*flowPerExit[7] / (float) (r.getNumberOfSameRide()));}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {r.setFlow((float) (probas[2][7])*flowPerExit[7] / (float) (r.getNumberOfSameRide()));}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {r.setFlow((float) (probas[3][7])*flowPerExit[7] / (float) (r.getNumberOfSameRide()));}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {r.setFlow((float) (probas[4][7])*flowPerExit[7] / (float) (r.getNumberOfSameRide()));}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {r.setFlow((float) (probas[5][7])*flowPerExit[7] / (float) (r.getNumberOfSameRide()));}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {r.setFlow((float) (probas[6][7])*flowPerExit[7] / (float) (r.getNumberOfSameRide()));}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {r.setFlow((float) (probas[7][7])*flowPerExit[7] / (float) (r.getNumberOfSameRide()));}
			
			else {r.setFlow(0);}
		}
		
	}
	public static void applyDataToRoadsProba(Simulation simulation) {
		
		System.out.println("Applying ...");
		
		Network n = simulation.getSimState().getNetwork();
		for (Road road: n.getRoads()) {
			if (n.getAllRides(road.getName()) != null) {
				if (road.getName().equals("rD884NE")) {road.setGenerateVehicules(flowPerExit[0]);}
				else if (road.getName().equals("rRueDeGeneveSE")) {road.setGenerateVehicules(flowPerExit[1]);}
				else if (road.getName().equals("rRueGermaineTillionSW")) {road.setGenerateVehicules(flowPerExit[2]);}
				else if (road.getName().equals("rC5SW")) {road.setGenerateVehicules(flowPerExit[3]);}
				//else if (road.getName().equals("rRoutePauliSouthNELeft")) {road.setGenerateVehicules(flowPerExit[4]);}
				else if (road.getName().equals("rRouteDeMeyrinSouthNW")) {road.setGenerateVehicules(flowPerExit[5]);}
				else if (road.getName().equals("rRouteBellNE")) {road.setGenerateVehicules(flowPerExit[6]);}
				else if (road.getName().equals("rSortieCERNNW")) {road.setGenerateVehicules(flowPerExit[7]);}
				
				else {
					road.setGenerateVehicules(0);
				}
			}
			
		}
		/*for (Ride ride: n.getAllRides("rD884NE").getNetworkRides()) {
			System.out.print(ride.getNextConnections().get(ride.getNextConnections().size()-1).getName() + " ");
			System.out.println(ride.getFlow());
		}*/
		
		if (n.getN() == 1) {
			n.getTrafficLightsSystems().get(0).getPhases().get(0).setMin(simulation.getSimSettingsState().crEntreeB_phase1().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(0).setMax(simulation.getSimSettingsState().crEntreeB_phase1().getCurrentValue2());
			
			n.getTrafficLightsSystems().get(0).getPhases().get(1).setMin(simulation.getSimSettingsState().crEntreeB_phase2().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(1).setMax(simulation.getSimSettingsState().crEntreeB_phase2().getCurrentValue2());
			
			n.getTrafficLightsSystems().get(0).getPhases().get(2).setMin(simulation.getSimSettingsState().crEntreeB_phase3().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(2).setMax(simulation.getSimSettingsState().crEntreeB_phase3().getCurrentValue2());
			
			n.getTrafficLightsSystems().get(0).getPhases().get(3).setMin(simulation.getSimSettingsState().crEntreeB_phase4().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(3).setMax(simulation.getSimSettingsState().crEntreeB_phase4().getCurrentValue2());
		}
	}
	public static void applyDataNumerical(Simulation simulation) {
		
		if (!useProbabilities) {
			nFrGeChosen = simulation.getSimSettingsState().fromFrToGe().getCurrentValue();
			nGeFrChosen = simulation.getSimSettingsState().fromGeToFr().getCurrentValue();
			nToEChosen = simulation.getSimSettingsState().toEntranceE().getCurrentValue();
			nFromEChosen = simulation.getSimSettingsState().fromEntranceE().getCurrentValue();
			nToAChosen = simulation.getSimSettingsState().toEntranceA().getCurrentValue();
			nFromAChosen = simulation.getSimSettingsState().fromEntranceA().getCurrentValue();
			nToBChosen = simulation.getSimSettingsState().toEntranceB().getCurrentValue();
			nFromBChosen = simulation.getSimSettingsState().fromEntranceB().getCurrentValue();
			
			if (!simulation.getSimState().getNetwork().isRandomGeneration()) {
				//Utils.log("applying data to Network ... ");
				applyDataToRidesNumerical(simulation);
				applyRidesToRoads(simulation);
				//Utils.log("done\n");
			} else {
				//Utils.log("applying random gen to Network ... done\n");
			}	
		}
	}
	public static void applyFlowFromVariablesNumerical(Simulation sim, Ride r) {
		
		/*if (r.getNextConnections().size() > 0) {
			numberOfSameRide = numberOfSameRides(sim, r.getRoadName(), r.getNextConnections().get(r.getNextConnections().size()-1).getName());
		} else {
			numberOfSameRide = 1;
		}*/
//		int tmp = randomValue;
//		if (!useFrToFr) {
//			randomValue = 0;
//		}
		
		if (!useProbabilities) {
			numberOfSameRide = r.getNumberOfSameRide();
			
			r.setFlow((float) ((value)*(repartition2-repartition1)*(1-repartitionRHMorning-repartitionRHEvening)/(21.0*numberOfSameRide)));
			
			if (repartitionRH7 > 0 && repartitionRH8 > 0 && repartitionRH9 > 0) {
				r.setFlow(7, 8, (float) ((value)*(repartition2-repartition1)*repartitionRHMorning*repartitionRH7/((float) numberOfSameRide)));
				r.setFlow(8, 9, (float) ((value)*(repartition2-repartition1)*repartitionRHMorning*repartitionRH8/((float) numberOfSameRide)));
				r.setFlow(9, 10, (float) ((value)*(repartition2-repartition1)*repartitionRHMorning*repartitionRH9/((float) numberOfSameRide)));
			} else {
				if (repartitionRHMorning > 0) {
					r.setFlow(7, 10, (float) ((value-randomValue)*(repartition2-repartition1)*repartitionRHMorning/((float) numberOfSameRide*3.0)));
				}
			}
			
			if (repartitionRH17 > 0 && repartitionRH18 > 0 && repartitionRH19 > 0) {
				r.setFlow(17, 18, (float) ((value)*(repartition2-repartition1)*repartitionRHEvening*repartitionRH17/((float) numberOfSameRide)));
				r.setFlow(18, 19, (float) ((value)*(repartition2-repartition1)*repartitionRHEvening*repartitionRH18/((float) numberOfSameRide)));
				r.setFlow(19, 20, (float) ((value)*(repartition2-repartition1)*repartitionRHEvening*repartitionRH19/((float) numberOfSameRide)));
			} else {
				if (repartitionRHEvening > 0) {
					r.setFlow(17, 20, (float) ((value)*(repartition2-repartition1)*repartitionRHEvening/((float) numberOfSameRide*3.0)));
				}
			}
		}
		
		//randomValue = tmp;
	}
	public static void applyDataToRidesNumerical(Simulation simulation) {
		
		Network n = simulation.getSimState().getNetwork();
		SimSettingsState settings = simulation.getSimSettingsState();
		
		for (AllNetworkRides anr: n.getAllNetworkRides()) {
			for (Ride r: anr.getNetworkRides()) {
				r.setNumberOfSameRide(numberOfSameRides(simulation, r.getRoadName(), r.getNextConnections().get(r.getNextConnections().size()-1).getName()));
			}
		}
		
		// France to Geneva =================================================================================
		
		for (Ride r: n.getAllRides("rD884NE").getNetworkRides()) {
			resetValues();
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				//repartitionFrToFr = settings.fromFrToFr().getCurrentValue()/100.0;
				//System.out.print(settings.fromFrToGe().getCurrentValue() + " - ");
				value = (int) (settings.fromFrToGe().getCurrentValue());
				//value = (int) (settings.fromFrToGe().getCurrentValue()*(1-repartitionFrToFr));
				//System.out.println(value);
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue1() / (100.0);
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
				//randomValue = (int) (settings.fromFrToGe().getCurrentValue()*(repartitionFrToFr));
			} else if (lastRoadIs(r, "rD884CERN")) {
				value = settings.toEntranceE().getCurrentValue();
				repartition2 = settings.toEntranceERepartition().getCurrentValue1() / (100.0);
				repartitionRH7 = settings.toEntranceERepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceERepartitionRH().getCurrentValue2() - settings.toEntranceERepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100-settings.toEntranceERepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRHMorning = 1;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = settings.toEntranceA().getCurrentValue();
				repartition2 = (100 - settings.toEntranceARepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = settings.toEntranceB().getCurrentValue();
				repartition2 = (100 - settings.toEntranceBRepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRueDeGeneveNW")) {
				value = settings.fromFrToFr().getCurrentValue()/4;
				repartition2 = 1;
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRueGermaineTillionNE")) {
				value = settings.fromFrToFr().getCurrentValue()/4;
				repartition2 = 1;
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			}/* else  if (lastRoadIs(r, "rRueGermaineTillionNE")) {
				value = (int) (randomValue/2.0);
				repartition2 = 1;
				useFrToFr = true;
			}*/
			applyFlowFromVariablesNumerical(simulation, r);
		}
		
		for (Ride r: n.getAllRides("rRueDeGeneveSE").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = settings.fromFrToGe().getCurrentValue();
				repartition1 = settings.fromFrToGeRepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue2() / (100.0);
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			} else if (lastRoadIs(r, "rSortieCERNSE")) {
				value = settings.toEntranceE().getCurrentValue();
				repartition1 = settings.toEntranceERepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.toEntranceERepartition().getCurrentValue2() / (100.0);
				repartitionRH7 = settings.toEntranceERepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceERepartitionRH().getCurrentValue2() - settings.toEntranceERepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100-settings.toEntranceERepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRHMorning = 1;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = settings.toEntranceA().getCurrentValue();
				repartition2 = (100 - settings.toEntranceARepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = settings.toEntranceB().getCurrentValue();
				repartition2 = (100 - settings.toEntranceBRepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rD884SW")) {
				value = settings.fromFrToFr().getCurrentValue()/4;
				repartition2 = 1;
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			}
			applyFlowFromVariablesNumerical(simulation, r);
		}
		
		for (Ride r: n.getAllRides("rRueGermaineTillionSW").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = settings.fromFrToGe().getCurrentValue();
				repartition1 = settings.fromFrToGeRepartition().getCurrentValue2() / (100.0);
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue3() / (100.0);
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			} else if (lastRoadIs(r, "rSortieCERNSE")) {
				value = settings.toEntranceE().getCurrentValue();
				repartition1 = settings.toEntranceERepartition().getCurrentValue2() / (100.0);
				repartition2 = 1;
				repartitionRH7 = settings.toEntranceERepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceERepartitionRH().getCurrentValue2() - settings.toEntranceERepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100-settings.toEntranceERepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRHMorning = 1;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = settings.toEntranceA().getCurrentValue();
				repartition2 = (100 - settings.toEntranceARepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = settings.toEntranceB().getCurrentValue();
				repartition2 = (100 - settings.toEntranceBRepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rD884SW")) {
				value = settings.fromFrToFr().getCurrentValue()/4;
				repartition2 = 1;
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			}
			applyFlowFromVariablesNumerical(simulation, r);
		}
		for (Ride r: n.getAllRides("rC5SW").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = settings.fromFrToGe().getCurrentValue();
				repartition1 = settings.fromFrToGeRepartition().getCurrentValue3() / (100.0);
				repartition2 = 1;
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = settings.toEntranceA().getCurrentValue();
				repartition2 = (100 - settings.toEntranceARepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = settings.toEntranceB().getCurrentValue();
				repartition2 = (100 - settings.toEntranceBRepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
			}
			applyFlowFromVariablesNumerical(simulation, r);
		}
		
		// Geneva to France =================================================================================
		
		for (Ride r: n.getAllRides("rRouteDeMeyrinSouthNW").getNetworkRides()) {
			
			resetValues();
			
			repartitionRH17 = settings.fromGeToFrRepartitionRH2().getCurrentValue1() / 100.0;
			repartitionRH18 = (settings.fromGeToFrRepartitionRH2().getCurrentValue2() - settings.fromGeToFrRepartitionRH2().getCurrentValue1()) / 100.0;
			repartitionRH19 = (100 - settings.fromGeToFrRepartitionRH2().getCurrentValue2()) / 100.0;
			
			if (lastRoadIs(r, "rD884SW")) {
				value = settings.fromGeToFr().getCurrentValue();
				repartition1 = 0;
				repartition2 = settings.fromGeToFrRepartition().getCurrentValue1() / (100.0);
				repartitionRHEvening = settings.fromGeToFrDuringRH2().getCurrentValue() / 100.0;
			} else if (lastRoadIs(r, "rRueDeGeneveNW")) {
				value = settings.fromGeToFr().getCurrentValue();
				repartition1 = settings.fromGeToFrRepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.fromGeToFrRepartition().getCurrentValue2() / (100.0);
				repartitionRHEvening = settings.fromGeToFrDuringRH2().getCurrentValue() / 100.0;
			} else if (lastRoadIs(r, "rRueGermaineTillionNE")) {
				value = settings.fromGeToFr().getCurrentValue();
				repartition1 = settings.fromGeToFrRepartition().getCurrentValue2() / (100.0);
				repartition2 = settings.fromGeToFrRepartition().getCurrentValue3() / (100.0);
				repartitionRHEvening = settings.fromGeToFrDuringRH2().getCurrentValue() / 100.0;
			} else if (lastRoadIs(r, "rC5NE")) {
				value = settings.fromGeToFr().getCurrentValue();
				repartition1 = settings.fromGeToFrRepartition().getCurrentValue3() / (100.0);
				repartition2 = 1;
				repartitionRHEvening = settings.fromGeToFrDuringRH2().getCurrentValue() / 100.0;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = settings.toEntranceA().getCurrentValue();
				repartition2 = (settings.toEntranceARepartition().getCurrentValue()) / (100.0);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRH17 = 0;
				repartitionRH18 = 0;
				repartitionRH19 = 0;
			}  else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = settings.toEntranceB().getCurrentValue();
				repartition2 = (settings.toEntranceBRepartition().getCurrentValue()) / (100.0);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRH17 = 0;
				repartitionRH18 = 0;
				repartitionRH19 = 0;
			}
			applyFlowFromVariablesNumerical(simulation, r);
		}
		
		// From entrance E to France ========================================================================
		
		for (Ride r: n.getAllRides("rSortieCERNNW").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rD884SW")) {
				value = settings.fromEntranceE().getCurrentValue();
				repartition1 = 0;
				repartition2 = settings.fromEntranceERepartition().getCurrentValue1() / (100.0);
			} else if (lastRoadIs(r, "rRueDeGeneveNW")) {
				value = settings.fromEntranceE().getCurrentValue();
				repartition1 = settings.fromEntranceERepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.fromEntranceERepartition().getCurrentValue2() / (100.0);
			} else if (lastRoadIs(r, "rRueGermaineTillionNE")) {
				value = settings.fromEntranceE().getCurrentValue();
				repartition1 = settings.fromEntranceERepartition().getCurrentValue2() / (100.0);
				repartition2 = 1;
			}
			repartitionRH17 = settings.fromEntranceERepartitionRH2().getCurrentValue1() / 100.0;
			repartitionRH18 = (settings.fromEntranceERepartitionRH2().getCurrentValue2() - settings.fromEntranceERepartitionRH2().getCurrentValue1()) / 100.0;
			repartitionRH19 = (100 - settings.fromEntranceERepartitionRH2().getCurrentValue2()) / 100.0;
			repartitionRHEvening = 1;
			applyFlowFromVariablesNumerical(simulation, r);
		}
		
		// From entrance A ==================================================================================
		
		for (Ride r: n.getAllRides("rRouteBellNE").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rD884SW") || lastRoadIs(r, "rRueDeGeneveNW") || lastRoadIs(r, "rRueGermaineTillionNE") || lastRoadIs(r, "rC5NE")) {
				value = settings.fromEntranceA().getCurrentValue();
				repartition2 = (100-settings.fromEntranceARepartition().getCurrentValue()) / (100.0*4);
			} else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = settings.fromEntranceA().getCurrentValue();
				repartition2 = (settings.fromEntranceARepartition().getCurrentValue()) / (100.0);
			}
			repartitionRH17 = settings.fromEntranceARepartitionRH2().getCurrentValue1() / 100.0;
			repartitionRH18 = (settings.fromEntranceARepartitionRH2().getCurrentValue2() - settings.fromEntranceARepartitionRH2().getCurrentValue1()) / 100.0;
			repartitionRH19 = (100 - settings.fromEntranceARepartitionRH2().getCurrentValue2()) / 100.0;
			repartitionRHEvening = 1;
			applyFlowFromVariablesNumerical(simulation, r);
		}
		
		// From entrance B ==================================================================================
		
		ArrayList<AllNetworkRides> anrEntreeB = new ArrayList<AllNetworkRides>();
		
		// if one road out of entrance B
		if (n.getAllRides("rRoutePauliSouthNE") != null) {
			anrEntreeB.add(n.getAllRides("rRoutePauliSouthNE"));
		} else {
			if (n.getAllRides("rRoutePauliSouthNELeft") != null) {
				anrEntreeB.add(n.getAllRides("rRoutePauliSouthNELeft"));
			}
			if (n.getAllRides("rRoutePauliSouthNERight") != null) {
				anrEntreeB.add(n.getAllRides("rRoutePauliSouthNERight"));
			}
			
		}
		for (AllNetworkRides anr: anrEntreeB) {
			for (Ride r: anr.getNetworkRides()) {
				
				resetValues();
				
				if (lastRoadIs(r, "rD884SW") || lastRoadIs(r, "rRueDeGeneveNW") || lastRoadIs(r, "rRueGermaineTillionNE") || lastRoadIs(r, "rC5NE")) {
					value = settings.fromEntranceB().getCurrentValue();
					repartition2 = (100-settings.fromEntranceBRepartition().getCurrentValue()) / (100.0*4);
				} else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
					value = settings.fromEntranceB().getCurrentValue();
					repartition2 = (settings.fromEntranceBRepartition().getCurrentValue()) / (100.0);
				}
				repartitionRH17 = settings.fromEntranceBRepartitionRH2().getCurrentValue1() / 100.0;
				repartitionRH18 = (settings.fromEntranceBRepartitionRH2().getCurrentValue2() - settings.fromEntranceBRepartitionRH2().getCurrentValue1()) / 100.0;
				repartitionRH19 = (100 - settings.fromEntranceBRepartitionRH2().getCurrentValue2()) / 100.0;
				repartitionRHEvening = 1;
				applyFlowFromVariablesNumerical(simulation, r);
			}
		}
		
		for (Road road: n.getRoads()) {
			if (road.getName().equals("rSortieCERNSE") || road.getName().equals("rD884CERN") || road.getName().equals("rRoutePauliSouthSW") || road.getName().equals("rRouteBellSW")) {
				road.setMaxOutflow(settings.timePerVhcEntrance().getCurrentValue());
			}
		}
		
		/*for (AllNetworkRides anr: n.getAllNetworkRides()) {
			
			for (Ride ride: anr.getNetworkRides()) {
				//if ((ride.getRoadName().equals("rRueDeGeneveSE") || ride.getRoadName().equals("rD884NE") || ride.getRoadName().equals("rRueGermaineTillionSW") || ride.getRoadName().equals("rC5SW")) && lastRoadIs(ride,"rRoutePauliSouthSW")) {
				if ((ride.getRoadName().equals("rD884NE")) && lastRoadIs(ride,"rRoutePauliSouthSW")) {
					ride.print();
					System.out.println();
				}
			}
		}*/
	}
	public static void applyRidesToRoads(Simulation simulation) {
		
		Network n = simulation.getSimState().getNetwork();
		
		for (Road road: n.getRoads()) {
			/*if (n.getAllRides(road.getName()) != null) {
				if (road.getName().equals("rSortieCERNNW")) {//rRueDeGeneveSE//rSortieCERNNW
					System.out.println(road.getName() + " : ");
					for (Ride ride: n.getAllRides(road.getName()).getNetworkRides()) {
						ride.print();
						System.out.println();
						int tmp = 0;
						for (Integer i: ride.getFlow()) {
							System.out.print(i + " ");
							tmp += i;
						}
						System.out.println();
						System.out.println(tmp);
					}
				}
			}*/
			int tmp = 0;
			
			for (int h=0 ; h<24 ; h++) {
				Utils.saveCheckingValues(Integer.toString(h) + " ");
				float sum = 0;
				if (n.getAllRides(road.getName()) != null) {
					
					for (Ride ride: n.getAllRides(road.getName()).getNetworkRides()) {
						//Utils.saveCheckingValues("(" + Integer.toString(ride.getFlow().get(h)) + ") - ");
						sum += ride.getFlow().get(h);
						
					}
				}
				Utils.saveCheckingValues("(" + Float.toString(sum) + ") ");
				road.setGenerateVehicules(h, h+1, sum);
				tmp += sum;
			}
			Utils.saveCheckingValues("\n");
			Utils.saveCheckingValues("Total : " + tmp + "\n");
		}
		/*
		for (AllNetworkRides anr: n.getAllNetworkRides()) {
			for (Ride ride: anr.getNetworkRides()) {
				System.out.print(ride.getRoadName() + " to " + ride.getNextConnections().get(ride.getNextConnections().size()-1).getName() + " : ");
				for (Float flow: ride.getFlow()) {
					System.out.print(flow + " ");
				}
				System.out.println();
			}
		}
		for (Road road: n.getRoads()) {
			System.out.println(road.getName() + " : ");
			for (int i=0; i<road.getFlow().size() ; i++) {
				System.out.print(road.getFlow().get(i) + " ");
			}
			System.out.println();
		}*/
		
		if (n.getN() == 1) {
			n.getTrafficLightsSystems().get(0).getPhases().get(0).setMin(simulation.getSimSettingsState().crEntreeB_phase1().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(0).setMax(simulation.getSimSettingsState().crEntreeB_phase1().getCurrentValue2());
			
			n.getTrafficLightsSystems().get(0).getPhases().get(1).setMin(simulation.getSimSettingsState().crEntreeB_phase2().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(1).setMax(simulation.getSimSettingsState().crEntreeB_phase2().getCurrentValue2());
			
			n.getTrafficLightsSystems().get(0).getPhases().get(2).setMin(simulation.getSimSettingsState().crEntreeB_phase3().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(2).setMax(simulation.getSimSettingsState().crEntreeB_phase3().getCurrentValue2());
			
			n.getTrafficLightsSystems().get(0).getPhases().get(3).setMin(simulation.getSimSettingsState().crEntreeB_phase4().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(3).setMax(simulation.getSimSettingsState().crEntreeB_phase4().getCurrentValue2());
		}
	}
	public static int numberOfSameRides(Simulation sim, String start, String end) {
		Network n = sim.getSimState().getNetwork();
		int count = 1;
		for (AllNetworkRides anr: n.getAllNetworkRides()) {
			
			for (Ride ride: anr.getNetworkRides()) {
				if (ride.getRoadName().equals(start) && ride.getNextConnections().size()>0) {
					if (ride.getNextConnections().get(ride.getNextConnections().size()-1).getName().equals(end)) {
						count++;
					}
				}
			}
		}
		count--;
		return count;
	}
	public static void resetValues() {
		value = 0;
		repartition1 = 0;
		repartition2 = 0;
		repartitionRHMorning = 0;
		repartitionRHEvening = 0;
		repartitionRH7 = 0;
		repartitionRH8 = 0;
		repartitionRH9 = 0;
		repartitionRH17 = 0;
		repartitionRH18 = 0;
		repartitionRH19 = 0;
		numberOfSameRide = 0;
		useFrToFr = false;
		repartitionFrToFr = 0;
	}
	public static void switchNumProba() {
		useProbabilities = !useProbabilities;
	}
}
