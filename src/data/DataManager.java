package data;

import java.util.ArrayList;

import elements.MaxVehicleOutflow;
import elements.Ride;
import elements.Road;
import graphics.Assets;
import main.Simulation;
import network.AllNetworkRides;
import network.Network;
import states.SimSettingsStateNum;
import utils.Defaults;
import utils.OriginDestinationCalculator;
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
	//private static double repartitionFrToFr = 0;
	//private static boolean useFrToFr = false;

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

	public static double[][][] probas = new double[24][9][9];

	public static double[][] inputMatrixEntrance = new double[24][9];
	
	public static double[][] inputMatrixExit = new double[24][9];
	
	public static void initProbas() {
		/*double[][] Matrice_N =
			{
				{	77,		10,		191,	0,		0,		29,		94,		1,		0	},
				{	43,		10,		93,		0,		0,		25,		35,		1,		0	},
				{	28,		10,		65,		0,		0,		13,		28,		1,		0	},
				{	52,		20,		56,		0,		0,		6,		29,		1,		0	},
				{	209,	20,		56,		0,		0,		8,		45,		1,		0	},
				{	558,	100,	133,	0,		0,		10,		177,	1,		0	},
				{	1315,	150,	349,	0,		0,		19,		293,	1,		0	},
				{	1828,	671,	717,	240,	29,		75,		500,	43,		0	},
				{	1651,	623,	829,	330,	111,	144,	500,	103,	0	},
				{	1191,	463,	628,	254,	118,	174,	500,	73,		0	},
				{	1074,	150,	312,	0,		0,		156,	519,	75,		0	},
				{	1006,	150,	370,	0,		0,		258,	302,	162,	0	},
				{	963,	150,	435,	0,		0,		306,	200,	283,	0	},
				{	1216,	150,	357,	0,		0,		214,	300,	145,	0	},
				{	1011,	150,	423,	0,		0,		178,	500,	102,	0	},
				{	1001,	150,	470,	0,		0,		198,	606,	112,	0	},
				{	1000,	150,	642,	0,		0,		243,	860,	150,	0	},
				{	1109,	115,	471,	187,	76,		239,	1200,	210,	500	},
				{	1078,	218,	691,	163,	1,		204,	1000,	203,	600	},
				{	999,	199,	762,	150,	0,		292,	800,	4,		400	},
				{	609,	200,	342,	0,		0,		119,	307,	18,		0	},
				{	298,	200,	242,	0,		0,		139,	172,	4,		0	},
				{	260,	200,	216,	0,		0,		105,	174,	1,		0	},
				{	192,	200,	189,	0,		0,		196,	48,		1,		0	}
			};

		double[][] Matrice_M =
			{
				{	264,	14,		53,		0+0,		0,		0,		12,		58,		1	},
				{	126,	14,		29,		0+0,		0,		0,		7,		30,		1	},
				{	87,		13,		19,		0+0,		0,		0,		2,		23,		1	},
				{	68,		24,		31,		0+0,		0,		0,		4,		37,		0	},
				{	88,		21,		110,	0+0,		0,		0,		8,		139,	0	},
				{	147,	111,	309,	0+0,		0,		0,		47,		363,	2	},
				{	365,	157,	688,	0+0,		0,		0,		149,	800,	0	},
				{	704,	142,	583,	332+331,	170,	27,		226,	1200,	111	},
				{	686,	225,	712,	386+386,	278,	114,	354,	1100,	263	},
				{	758,	178,	683,	143+144,	252,	99,		390,	900,	180	},
				{	914,	147,	525,	0+0,		0,		0,		295,	500,	106	},
				{	1026,	139,	465,	0+0,		0,		0,		239,	286,	94	},
				{	1212,	139,	448,	0+0,		0,		0,		256,	216,	124	},
				{	981,	138,	557,	0+0,		0,		0,		453,	64,		226	},
				{	1177,	139,	469,	0+0,		0,		0,		293,	213,	118	},
				{	1311,	140,	466,	0+0,		0,		0,		183,	373,	65	},
				{	1810,	141,	470,	0+0,		0,		0,		154,	409,	62	},
				{	1807,	553,	591,	0+0,		251,	73,		116,	500,	72	},
				{	1644,	585,	779,	0+0,		204,	1,		115,	550,	40	},
				{	1304,	487,	661,	0+0,		163,	0,		120,	600,	4	},
				{	684,	200,	305,	0+0,		0,		0,		50,		344,	12	},
				{	495,	205,	152,	0+0,		0,		0,		31,		169,	3	},
				{	441,	205,	152,	0+0,		0,		0,		23,		154,	0	},
				{	389,	206,	99,		0+0,		0,		0,		12,		118,	2	}
			};*/

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
		for (int in=0; in<odc.getP().length; ++in) {
			for (int out=0; out<odc.getP()[in].length; ++out) {
				for (int k=0; k<odc.getP()[in][out].length; ++k) {
					probas[k][out][in]=odc.getP()[in][out][k];
				}
			}
		}
		//ODC2.println(0);
	}

	public static int[][] flowPerExit = new int[24][18];

	public static int[] flowPerExitEmpiric = new int[16];

	// ============================================================================================
	// Light system ===============================================================================
	// ============================================================================================

	public static int randomValue = 0;

	public static void initFlowPerExit() {
		/*double[][] Matrice_N =
			{
				{	77,		10,		191,	0,		0,		29,		94,		1,		0	},
				{	43,		10,		93,		0,		0,		25,		35,		1,		0	},
				{	28,		10,		65,		0,		0,		13,		28,		1,		0	},
				{	52,		20,		56,		0,		0,		6,		29,		1,		0	},
				{	209,	20,		56,		0,		0,		8,		45,		1,		0	},
				{	558,	100,	133,	0,		0,		10,		177,	1,		0	},
				{	1315,	150,	349,	0,		0,		19,		293,	1,		0	},
				{	1828,	671,	717,	240,	29,		75,		500,	43,		0	},
				{	1651,	623,	829,	330,	111,	144,	500,	103,	0	},
				{	1191,	463,	628,	254,	118,	174,	500,	73,		0	},
				{	1074,	150,	312,	0,		0,		156,	519,	75,		0	},
				{	1006,	150,	370,	0,		0,		258,	302,	162,	0	},
				{	963,	150,	435,	0,		0,		306,	200,	283,	0	},
				{	1216,	150,	357,	0,		0,		214,	300,	145,	0	},
				{	1011,	150,	423,	0,		0,		178,	500,	102,	0	},
				{	1001,	150,	470,	0,		0,		198,	606,	112,	0	},
				{	1000,	150,	642,	0,		0,		243,	860,	150,	0	},
				{	1109,	115,	471,	187,	76,		239,	1200,	210,	500	},
				{	1078,	218,	691,	163,	1,		204,	1000,	203,	600	},
				{	999,	199,	762,	150,	0,		292,	800,	4,		400	},
				{	609,	200,	342,	0,		0,		119,	307,	18,		0	},
				{	298,	200,	242,	0,		0,		139,	172,	4,		0	},
				{	260,	200,	216,	0,		0,		105,	174,	1,		0	},
				{	192,	200,	189,	0,		0,		196,	48,		1,		0	}
			};

		double[][] Matrice_M =
			{
				{	264,	14,		53,		0+0,		0,		0,		12,		58,		1	},
				{	126,	14,		29,		0+0,		0,		0,		7,		30,		1	},
				{	87,		13,		19,		0+0,		0,		0,		2,		23,		1	},
				{	68,		24,		31,		0+0,		0,		0,		4,		37,		0	},
				{	88,		21,		110,	0+0,		0,		0,		8,		139,	0	},
				{	147,	111,	309,	0+0,		0,		0,		47,		363,	2	},
				{	365,	157,	688,	0+0,		0,		0,		149,	800,	0	},
				{	704,	142,	583,	332+331,	170,	27,		226,	1200,	111	},
				{	686,	225,	712,	386+386,	278,	114,	354,	1100,	263	},
				{	758,	178,	683,	143+144,	252,	99,		390,	900,	180	},
				{	914,	147,	525,	0+0,		0,		0,		295,	500,	106	},
				{	1026,	139,	465,	0+0,		0,		0,		239,	286,	94	},
				{	1212,	139,	448,	0+0,		0,		0,		256,	216,	124	},
				{	981,	138,	557,	0+0,		0,		0,		453,	64,		226	},
				{	1177,	139,	469,	0+0,		0,		0,		293,	213,	118	},
				{	1311,	140,	466,	0+0,		0,		0,		183,	373,	65	},
				{	1810,	141,	470,	0+0,		0,		0,		154,	409,	62	},
				{	1807,	553,	591,	0+0,		251,	73,		116,	500,	72	},
				{	1644,	585,	779,	0+0,		204,	1,		115,	550,	40	},
				{	1304,	487,	661,	0+0,		163,	0,		120,	600,	4	},
				{	684,	200,	305,	0+0,		0,		0,		50,		344,	12	},
				{	495,	205,	152,	0+0,		0,		0,		31,		169,	3	},
				{	441,	205,	152,	0+0,		0,		0,		23,		154,	0	},
				{	389,	206,	99,		0+0,		0,		0,		12,		118,	2	}
			};*/
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

		if (useProbabilities) {
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
	// OBSOLTED
	public static void applyDataNumerical(Simulation simulation) {

		if (!useProbabilities) {
			nFrGeChosen = simulation.getSimSettingsStateNum().fromFrToGe().getCurrentValue();
			nGeFrChosen = simulation.getSimSettingsStateNum().fromGeToFr().getCurrentValue();
			nToEChosen = simulation.getSimSettingsStateNum().toEntranceE().getCurrentValue();
			nFromEChosen = simulation.getSimSettingsStateNum().fromEntranceE().getCurrentValue();
			nToAChosen = simulation.getSimSettingsStateNum().toEntranceA().getCurrentValue();
			nFromAChosen = simulation.getSimSettingsStateNum().fromEntranceA().getCurrentValue();
			nToBChosen = simulation.getSimSettingsStateNum().toEntranceB().getCurrentValue();
			nFromBChosen = simulation.getSimSettingsStateNum().fromEntranceB().getCurrentValue();

			if (!simulation.getSimState().getNetwork().isRandomGeneration()) {
				applyDataToRidesNumerical(simulation);
				applyRidesToRoads(simulation);
			} else {
				
			}
		}
	}
	// OBSOLTED
	public static void applyFlowFromVariablesNumerical(Simulation sim, Ride r) {

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
	}
	public static void applyDataToRidesNumerical(Simulation simulation) {

		Network n = simulation.getSimState().getNetwork();
		SimSettingsStateNum settings = simulation.getSimSettingsStateNum();

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
			}
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
	}
	public static void applyRidesToRoads(Simulation simulation) {

		Network n = simulation.getSimState().getNetwork();

		for (Road road: n.getRoads()) {
			int tmp = 0;

			for (int h=0 ; h<24 ; h++) {
				Utils.saveCheckingValues(Integer.toString(h) + " ");
				float sum = 0;
				if (n.getAllRides(road.getName()) != null) {

					for (Ride ride: n.getAllRides(road.getName()).getNetworkRides()) {
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
		//useFrToFr = false;
		//repartitionFrToFr = 0;
	}
	public static void switchNumProba() {
		useProbabilities = !useProbabilities;
	}
}
