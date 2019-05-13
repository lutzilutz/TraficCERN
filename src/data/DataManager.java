package data;

import java.util.ArrayList;

import elements.MaxVehicleOutflow;
import elements.Ride;
import elements.Road;
import graphics.Assets;
import main.Simulation;
import network.AllNetworkRides;
import network.Network;
import utils.Defaults;
import utils.OriginDestinationCalculator;
import utils.Utils;

public class DataManager {

	//private static int numberOfSameRide = 0;

	public static double[][][] probas = new double[24][9][9];
	public static double[][] inputMatrixEntrance = new double[24][9];
	public static double[][] inputMatrixExit = new double[24][9];
	
	public static void initProbas() {
		
		for (int i=0; i<24; i++) {
			String[] line = Assets.inputDataEntrance.get(i).split("\\s+");
			if (line.length == 9) {
				for (int j=0; j<line.length; j++) {
					int tmpValue = Utils.parseInt(line[j]);
					if (tmpValue >= 0) {
						inputMatrixEntrance[i][j] = tmpValue;
					} else {
						Utils.log("    WARNING : Negative value in inputData_entrance.txt at line " + i + " col " + j + "\n");
					}
				}
			} else {
				Utils.log("    WARNING : Line of size not 9 in inputData_entrance.txt at line " + i + "\n");
			}
		}
		for (int i=0; i<24; i++) {
			String[] line = Assets.inputDataExit.get(i).split("\\s+");
			if (line.length == 9) {
				for (int j=0; j<line.length; j++) {
					int tmpValue = Utils.parseInt(line[j]);
					if (tmpValue >= 0) {
						inputMatrixExit[i][j] = tmpValue;
					} else {
						Utils.log("    WARNING : Negative value in inputData_exit.txt at line " + i + " col " + j + "\n");
					}
				}
			} else {
				Utils.log("    WARNING : Line of size not 9 in inputData_exit.txt at line " + i + "\n");
			}
		}
		
		for (int i=0; i<inputMatrixExit.length; ++i) {
			for (int j=0; j<inputMatrixExit[i].length; ++j) {
				if (inputMatrixExit[i][j]==0) {
					inputMatrixExit[i][j]=1;
				} else {
					inputMatrixExit[i][j] *= 1000000;
				}
			}
		}

		for (int i=0; i<inputMatrixEntrance.length; ++i) {
			for (int j=0; j<inputMatrixEntrance[i].length; ++j) {
				if (inputMatrixEntrance[i][j]==0) {
					inputMatrixEntrance[i][j]=1;
				} else {
					inputMatrixEntrance[i][j] *= 1000000;
				}
			}
		}


		boolean[][] liensMN =
			{		//	G 		H		I		J		K		D2		L1		L3		L4
					{	false,	true,	true,	true,	true,	false,	true,	true,	true	},	// A
					{	true, 	false, 	true,	true,	true,	false,	true, 	true,	true	},	// B
					{	true,	true,	false,	true,	true,	false,	true,	true,	true	},	// C
					{	true, 	true,	true,	true,	false,	true,	true,	true,	true	},	// D
					{	false,	false,	false,	false,	true,	false,	false,	false,	false	}, 	// K1
					{	true,	true,	true,	false,	true, 	false,	false,	true,	false	},	// E1
					{	true, 	true,	true,	false,	true,	false,	true,	false,	true	},	// E3
					{	true,	true,	true,	false,	true,	false,	false,	true,	false	},	// E4
					{	true, 	true,	true,	false,	true,	false,	false,	false,	false	}	// F
			};

		OriginDestinationCalculator odc = new OriginDestinationCalculator(inputMatrixEntrance, inputMatrixExit, liensMN);

		odc.computation();
		for (int in=0; in<odc.getProbas().length; ++in) {
			for (int out=0; out<odc.getProbas()[in].length; ++out) {
				for (int k=0; k<odc.getProbas()[in][out].length; ++k) {
					probas[k][out][in]=odc.getProbas()[in][out][k];
				}
			}
		}
	}

	public static int[][] flowPerExit = new int[24][18];

	public static int randomValue = 0;

	public static void initFlowPerExit() {
		
		for (int i=0; i<inputMatrixEntrance[0].length+inputMatrixExit[0].length; ++i) {
			for (int j=0; j<24; ++j) {
				if (i<inputMatrixEntrance[0].length) {
					flowPerExit[j][i]=(int) (inputMatrixEntrance[j][i]/1000000.0);
				} else {
					flowPerExit[j][i]=(int) (inputMatrixExit[j][i-inputMatrixEntrance[0].length]/1000000.0);
				}
			}
		}
	}

	// ============================================================================================
	// Time spent on network ======================================================================
	// ============================================================================================

	public static ArrayList<Integer> timeSpentTransit = new ArrayList<Integer>();
	public static ArrayList<Integer> timeSpentCERN = new ArrayList<Integer>();
	public static double meanTime = 0;

	// Is roadName the last road of the ride r ?
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

		Utils.log("    INFO : Applying data (proba) to Network ... ");
		initProbas();
		initFlowPerExit();
		applyDataToRidesProba(simulation);
		applyDataToRoadsProba(simulation);
		for (Road road: simulation.getSimState().getNetwork().getRoads()) {
			if (road.getName().equals("rSortieCERNSE") || road.getName().equals("rD884CERN") || road.getName().equals("rRoutePauliSouthSW") || road.getName().equals("rRouteBellSW")) {
				road.setMaxOutflow(simulation.getSimSettingsStateProba().timePerVhcEntrance().getCurrentValue());
				for (MaxVehicleOutflow maxOutflow: simulation.getSimState().getNetwork().getMaxVehicleOutflows()) {
					maxOutflow.setGlobalOutflow(simulation.getSimSettingsStateProba().timePerVhcEntrance().getCurrentValue()/2);
				}
			}
		}
		
		Utils.log("done\n");
	}
	public static void applyDataToRidesProba(Simulation simulation) {

		Network n = simulation.getSimState().getNetwork();

		for (AllNetworkRides anr: n.getAllNetworkRides()) {
			for (Ride r: anr.getNetworkRides()) {
				r.setNumberOfSameRide(numberOfSameRides(simulation, r.getRoadName(), r.getNextConnections().get(r.getNextConnections().size()-1).getName()));
			}
		}

		// From A ===========================================================================================
		for (Ride r: n.getAllRides("rD884NE").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 0, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 0, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 0, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rD884CERN")) {saveFlowIntoRide(r, 0, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 0, 4);}
			// To K2 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rTunnelSE")) {saveFlowIntoRide(r, 0, 5);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 0, 6);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 0, 7);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 0, 8);}

			else {r.setFlow(0);}

		}
		// From B ===========================================================================================
		for (Ride r: n.getAllRides("rRueDeGeneveSE").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 1, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 1, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 1, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 1, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 1, 4);}
			// To K2 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rTunnelSE")) {saveFlowIntoRide(r, 1, 5);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 1, 6);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 1, 7);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 1, 8);}

			else {r.setFlow(0);}

		}
		// From C ===========================================================================================
		for (Ride r: n.getAllRides("rRueGermaineTillionSW").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 2, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 2, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 2, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 2, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 2, 4);}
			// To K2 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rTunnelSE")) {saveFlowIntoRide(r, 2, 5);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 2, 6);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 2, 7);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 2, 8);}

			else {r.setFlow(0);}
		}
		// From D ===========================================================================================
		for (Ride r: n.getAllRides("rC5SW").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 3, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 3, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 3, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 3, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 3, 4);}
			// To K2 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rTunnelSE")) {saveFlowIntoRide(r, 3, 5);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 3, 6);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 3, 7);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 3, 8);}

			else {r.setFlow(0);}
		}
		// From D2 ==========================================================================================
		for (Ride r: n.getAllRides("rTunnelNW").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 4, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 4, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 4, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 4, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 4, 4);}
			// To K2 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rTunnelSE")) {saveFlowIntoRide(r, 4, 5);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 4, 6);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 4, 7);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 4, 8);}

			else {r.setFlow(0);}
		}
		// From E1 (left) ===================================================================================
		for (Ride r: n.getAllRides("rRoutePauliSouthNELeft").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 5, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 5, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 5, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 5, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 5, 4);}
			// To K2 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rTunnelSE")) {saveFlowIntoRide(r, 5, 5);}

			else {r.setFlow(0);}
		}
		// From E1 (right) ==================================================================================
		for (Ride r: n.getAllRides("rRoutePauliSouthNERight").getNetworkRides()) {
			// To L1 -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 5, 6);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 5, 7);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 5, 8);}

			else {r.setFlow(0);}
		}
		// From E3 ===========================================================================================
		for (Ride r: n.getAllRides("rRouteDeMeyrinSouthNW").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 6, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 6, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 6, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 6, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 6, 4);}
			// To K2 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rTunnelSE")) {saveFlowIntoRide(r, 6, 5);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 6, 6);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 6, 7);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 6, 8);}

			else {r.setFlow(0);}
		}
		// From E4 ===========================================================================================
		for (Ride r: n.getAllRides("rRouteBellNE").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 7, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 7, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 7, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 7, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 7, 4);}
			// To K2 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rTunnelSE")) {saveFlowIntoRide(r, 7, 5);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 7, 6);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 7, 7);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 7, 8);}

			else {r.setFlow(0);}
		}
		// From F ===========================================================================================
		for (Ride r: n.getAllRides("rSortieCERNNW").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 8, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 8, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 8, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 8, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 8, 4);}
			// To K2 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rTunnelSE")) {saveFlowIntoRide(r, 8, 5);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 8, 6);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 8, 7);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 8, 8);}

			else {r.setFlow(0);}
		}

	}
	// in : entering road index in probas ; out : exiting road index in probas
	public static void saveFlowIntoRide(Ride r, int in, int out) {
		for (int i=0; i<24; i++) {
			
			r.setFlow(i, i+1, (float) (Defaults.getGlobalFlowMultiplier()*probas[i][out][in])*flowPerExit[i][in] / (float) (r.getNumberOfSameRide()));
		}
	}
	public static void saveFlowIntoRoad(Road road, int index, int specialCase) {
		for (int i=0; i<24; i++) {
			
			// All roads
			if (specialCase == 0) {
				road.setGenerateVehicules(i, i+1, (float) (Defaults.getGlobalFlowMultiplier()*flowPerExit[i][index]));
			}
			// RoutePauliSouthNELeft
			else if (specialCase == 1) {
				road.setGenerateVehicules(i, i+1, (float) (Defaults.getGlobalFlowMultiplier()*(probas[i][0][5]+probas[i][1][5]+probas[i][2][5]+probas[i][3][5]+probas[i][4][5]+probas[i][5][5]) * flowPerExit[i][index]));
			}
			// RoutePauliSouthNERight
			else if (specialCase == 2) {
				road.setGenerateVehicules(i, i+1, (float) (Defaults.getGlobalFlowMultiplier()*(probas[i][6][5]+probas[i][7][5]+probas[i][8][5]) * flowPerExit[i][index]));
			} else {
				Utils.log("    WARNING : Special case not considered in DataManager.saveFlowIntoRoad()\n");
			}
		}
	}
	public static void applyDataToRoadsProba(Simulation simulation) {

		Network n = simulation.getSimState().getNetwork();
		for (Road road: n.getRoads()) {
			if (n.getAllRides(road.getName()) != null) {
				if (road.getName().equals("rD884NE")) {saveFlowIntoRoad(road, 0, 0);}
				else if (road.getName().equals("rRueDeGeneveSE")) {saveFlowIntoRoad(road, 1, 0);}
				else if (road.getName().equals("rRueGermaineTillionSW")) {saveFlowIntoRoad(road, 2, 0);}
				else if (road.getName().equals("rC5SW")) {saveFlowIntoRoad(road, 3, 0);}
				else if (road.getName().equals("rTunnelNW")) {saveFlowIntoRoad(road, 4, 0);}
				else if (road.getName().equals("rRoutePauliSouthNELeft")) {saveFlowIntoRoad(road, 5, 1);}
				else if (road.getName().equals("rRoutePauliSouthNERight")) {saveFlowIntoRoad(road, 5, 2);}
				else if (road.getName().equals("rRouteDeMeyrinSouthNW")) {saveFlowIntoRoad(road, 6, 0);}
				else if (road.getName().equals("rRouteBellNE")) {saveFlowIntoRoad(road, 7, 0);}
				else if (road.getName().equals("rSortieCERNNW")) {saveFlowIntoRoad(road, 8, 0);}

				else {road.setGenerateVehicules(0);}
			}
		}

		if (n.getN() == 1) {
			n.getTrafficLightsSystems().get(0).getPhases().get(0).setMin(simulation.getSimSettingsStateProba().crEntreeB_phase1().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(0).setMax(simulation.getSimSettingsStateProba().crEntreeB_phase1().getCurrentValue2());

			n.getTrafficLightsSystems().get(0).getPhases().get(1).setMin(simulation.getSimSettingsStateProba().crEntreeB_phase2().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(1).setMax(simulation.getSimSettingsStateProba().crEntreeB_phase2().getCurrentValue2());

			n.getTrafficLightsSystems().get(0).getPhases().get(2).setMin(simulation.getSimSettingsStateProba().crEntreeB_phase3().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(2).setMax(simulation.getSimSettingsStateProba().crEntreeB_phase3().getCurrentValue2());

			n.getTrafficLightsSystems().get(0).getPhases().get(3).setMin(simulation.getSimSettingsStateProba().crEntreeB_phase4().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(3).setMax(simulation.getSimSettingsStateProba().crEntreeB_phase4().getCurrentValue2());
		}
	}
	public static void applyRidesToRoads(Simulation simulation) {

		Network n = simulation.getSimState().getNetwork();

		for (Road road: n.getRoads()) {
			
			for (int h=0 ; h<24 ; h++) {
				float sum = 0;
				if (n.getAllRides(road.getName()) != null) {

					for (Ride ride: n.getAllRides(road.getName()).getNetworkRides()) {
						sum += ride.getFlow().get(h);
					}
				}
				road.setGenerateVehicules(h, h+1, sum);
			}
		}

		if (n.getN() == 1) {
			n.getTrafficLightsSystems().get(0).getPhases().get(0).setMin(simulation.getSimSettingsStateProba().crEntreeB_phase1().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(0).setMax(simulation.getSimSettingsStateProba().crEntreeB_phase1().getCurrentValue2());

			n.getTrafficLightsSystems().get(0).getPhases().get(1).setMin(simulation.getSimSettingsStateProba().crEntreeB_phase2().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(1).setMax(simulation.getSimSettingsStateProba().crEntreeB_phase2().getCurrentValue2());

			n.getTrafficLightsSystems().get(0).getPhases().get(2).setMin(simulation.getSimSettingsStateProba().crEntreeB_phase3().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(2).setMax(simulation.getSimSettingsStateProba().crEntreeB_phase3().getCurrentValue2());

			n.getTrafficLightsSystems().get(0).getPhases().get(3).setMin(simulation.getSimSettingsStateProba().crEntreeB_phase4().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(3).setMax(simulation.getSimSettingsStateProba().crEntreeB_phase4().getCurrentValue2());
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
}
