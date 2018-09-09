package data;

import elements.Ride;
import elements.Road;
import main.Simulation;
import network.Network;
import states.SimSettingsState;
import utils.Utils;

public class DataManager {
	
	private static int value = 0;
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
	
	// Theoritical default values -------------------------
	public static int nFrGe = 12500;
	public static int nFrGe_fromSW = 57;
	public static int nFrGe_fromNW = 33;
	public static int nFrGe_fromNE = 5;
	public static int nFrGe_fromTun = 5;
	public static int nFrGe_7 = 30;
	public static int nFrGe_8 = 40;
	public static int nFrGe_9 = 30;
	
	public static int nGeFr = 12500;
	public static int nGeFr_toSW = 54;
	public static int nGeFr_toNW = 27;
	public static int nGeFr_toNE = 9;
	public static int nGeFr_toTun = 10;
	public static int nGeFr_17 = 30;
	public static int nGeFr_18 = 40;
	public static int nGeFr_19 = 30;
	
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
	
	// Theoritical chosen values --------------------------
	public static int nFrGeChosen = 0;
	public static int nGeFrChosen = 0;
	public static int nToEChosen = 0;
	public static int nFromEChosen = 0;
	public static int nToAChosen = 0;
	public static int nFromAChosen = 0;
	public static int nToBChosen = 0;
	
	// Empirical values -----------------------------------
	public static int nFrGeEmpiric = 0;
	public static int nGeFrEmpiric = 0;
	public static int nToEEmpiric = 0;
	public static int nFromEEmpiric = 0;
	public static int nToAEmpiric = 0;
	public static int nFromAEmpiric = 0;
	public static int nToBEmpiric = 0;
	
	public static void loadData(Simulation simulation) {
		
		//applyDataToRides(simulation);
		//applyRidesToRoads(simulation);
		
	}
	public static boolean lastRoadIs(Ride r, String roadName) {
		if (r.getNextConnections().get(r.getNextConnections().size()-1).getName().equals(roadName)) {
			return true;
		} else {
			return false;
		}
	}
	public static void applyData(Simulation simulation) {
		
		nFrGeChosen = simulation.getSimSettingsState().fromFrToGe().getCurrentValue();
		nGeFrChosen = simulation.getSimSettingsState().fromGeToFr().getCurrentValue();
		nToEChosen = simulation.getSimSettingsState().toEntranceE().getCurrentValue();
		nFromEChosen = simulation.getSimSettingsState().fromEntranceE().getCurrentValue();
		nToAChosen = simulation.getSimSettingsState().toEntranceA().getCurrentValue();
		nFromAChosen = simulation.getSimSettingsState().fromEntranceA().getCurrentValue();
		nToBChosen = simulation.getSimSettingsState().toEntranceB().getCurrentValue();
		
		if (!simulation.getSimState().getNetwork().isRandomGeneration()) {
			Utils.log("applying data to Network ... ");
			applyDataToRides(simulation);
			applyRidesToRoads(simulation);
			Utils.log("done\n");
		} else {
			Utils.log("applying random gen to Network ... done\n");
		}
	}
	public static void applyFlowFromVariables(Ride r) {
		
		r.setFlow((int) (value*(repartition2-repartition1)*(1-repartitionRHMorning-repartitionRHEvening)/21.0));
		
		if (repartitionRH7 > 0 && repartitionRH8 > 0 && repartitionRH9 > 0) {
			r.setFlow(7, 8, (int) (value*(repartition2-repartition1)*repartitionRHMorning*repartitionRH7));
			r.setFlow(8, 9, (int) (value*(repartition2-repartition1)*repartitionRHMorning*repartitionRH8));
			r.setFlow(9, 10, (int) (value*(repartition2-repartition1)*repartitionRHMorning*repartitionRH9));
		} else {
			if (repartitionRHMorning > 0) {
				r.setFlow(7, 10, (int) (value*(repartition2-repartition1)*repartitionRHMorning/3.0));
			}
		}
		
		if (repartitionRH17 > 0 && repartitionRH18 > 0 && repartitionRH19 > 0) {
			r.setFlow(17, 18, (int) (value*(repartition2-repartition1)*repartitionRHEvening*repartitionRH17));
			r.setFlow(18, 19, (int) (value*(repartition2-repartition1)*repartitionRHEvening*repartitionRH18));
			r.setFlow(19, 20, (int) (value*(repartition2-repartition1)*repartitionRHEvening*repartitionRH19));
		} else {
			if (repartitionRHEvening > 0) {
				r.setFlow(17, 20, (int) (value*(repartition2-repartition1)*repartitionRHEvening/3.0));
			}
		}
	}
	public static void applyDataToRides(Simulation simulation) {
		
		Network n = simulation.getSimState().getNetwork();
		SimSettingsState settings = simulation.getSimSettingsState();
		
		// France to Geneva =================================================================================
		for (Ride r: n.getAllRides("rD884NE").getNetworkRides()) {
			resetValues();
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = (int) settings.fromFrToGe().getCurrentValue();
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue1() / (100.0);
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			} else if (lastRoadIs(r, "rD884CERN")) {
				value = (int) settings.toEntranceE().getCurrentValue();
				repartition2 = settings.toEntranceERepartition().getCurrentValue1() / (100.0);
				repartitionRH7 = settings.toEntranceERepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceERepartitionRH().getCurrentValue2() - settings.toEntranceERepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100-settings.toEntranceERepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRHMorning = 1;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = (int) settings.toEntranceA().getCurrentValue();
				repartition2 = (100 - settings.toEntranceARepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = (int) settings.toEntranceB().getCurrentValue();
				repartition2 = (100 - settings.toEntranceBRepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
			}
			applyFlowFromVariables(r);
		}
		
		for (Ride r: n.getAllRides("rRueDeGeneveSE").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = (int) settings.fromFrToGe().getCurrentValue();
				repartition1 = settings.fromFrToGeRepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue2() / (100.0);
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			} else if (lastRoadIs(r, "rSortieCERNSE")) {
				value = (int) settings.toEntranceE().getCurrentValue();
				repartition1 = settings.toEntranceERepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.toEntranceERepartition().getCurrentValue2() / (100.0);
				repartitionRH7 = settings.toEntranceERepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceERepartitionRH().getCurrentValue2() - settings.toEntranceERepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100-settings.toEntranceERepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRHMorning = 1;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = (int) settings.toEntranceA().getCurrentValue();
				repartition2 = (100 - settings.toEntranceARepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = (int) settings.toEntranceB().getCurrentValue();
				repartition2 = (100 - settings.toEntranceBRepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
			}
			applyFlowFromVariables(r);
		}
		
		for (Ride r: n.getAllRides("rRueGermaineTillionSW").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = (int) settings.fromFrToGe().getCurrentValue();
				repartition1 = settings.fromFrToGeRepartition().getCurrentValue2() / (100.0);
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue3() / (100.0);
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			} else if (lastRoadIs(r, "rSortieCERNSE")) {
				value = (int) settings.toEntranceE().getCurrentValue();
				repartition1 = settings.toEntranceERepartition().getCurrentValue2() / (100.0);
				repartition2 = 1;
				repartitionRH7 = settings.toEntranceERepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceERepartitionRH().getCurrentValue2() - settings.toEntranceERepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100-settings.toEntranceERepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRHMorning = 1;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = (int) settings.toEntranceA().getCurrentValue();
				repartition2 = (100 - settings.toEntranceARepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = (int) settings.toEntranceB().getCurrentValue();
				repartition2 = (100 - settings.toEntranceBRepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
			}
			applyFlowFromVariables(r);
		}
		for (Ride r: n.getAllRides("rC5SW").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = (int) settings.fromFrToGe().getCurrentValue();
				repartition1 = settings.fromFrToGeRepartition().getCurrentValue3() / (100.0);
				repartition2 = 1;
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = (int) settings.toEntranceA().getCurrentValue();
				repartition2 = (100 - settings.toEntranceARepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = (int) settings.toEntranceB().getCurrentValue();
				repartition2 = (100 - settings.toEntranceBRepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
			}
			applyFlowFromVariables(r);
		}
		
		// Geneva to France =================================================================================
		
		for (Ride r: n.getAllRides("rRouteDeMeyrinSouthNW").getNetworkRides()) {
			
			resetValues();
			
			repartitionRH17 = settings.fromGeToFrRepartitionRH2().getCurrentValue1() / 100.0;
			repartitionRH18 = (settings.fromGeToFrRepartitionRH2().getCurrentValue2() - settings.fromGeToFrRepartitionRH2().getCurrentValue1()) / 100.0;
			repartitionRH19 = (100 - settings.fromGeToFrRepartitionRH2().getCurrentValue2()) / 100.0;
			
			if (lastRoadIs(r, "rD884SW")) {
				value = (int) settings.fromGeToFr().getCurrentValue();
				repartition1 = 0;
				repartition2 = settings.fromGeToFrRepartition().getCurrentValue1() / (100.0);
				repartitionRHEvening = settings.fromGeToFrDuringRH2().getCurrentValue() / 100.0;
			} else if (lastRoadIs(r, "rRueDeGeneveNW")) {
				value = (int) settings.fromGeToFr().getCurrentValue();
				repartition1 = settings.fromGeToFrRepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.fromGeToFrRepartition().getCurrentValue2() / (100.0);
				repartitionRHEvening = settings.fromGeToFrDuringRH2().getCurrentValue() / 100.0;
			} else if (lastRoadIs(r, "rRueGermaineTillionNE")) {
				value = (int) settings.fromGeToFr().getCurrentValue();
				repartition1 = settings.fromGeToFrRepartition().getCurrentValue2() / (100.0);
				repartition2 = settings.fromGeToFrRepartition().getCurrentValue3() / (100.0);
				repartitionRHEvening = settings.fromGeToFrDuringRH2().getCurrentValue() / 100.0;
			} else if (lastRoadIs(r, "rC5NE")) {
				value = (int) settings.fromGeToFr().getCurrentValue();
				repartition1 = settings.fromGeToFrRepartition().getCurrentValue3() / (100.0);
				repartition2 = 1;
				repartitionRHEvening = settings.fromGeToFrDuringRH2().getCurrentValue() / 100.0;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = (int) settings.toEntranceA().getCurrentValue();
				repartition2 = (settings.toEntranceARepartition().getCurrentValue()) / (100.0);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRH17 = 0;
				repartitionRH18 = 0;
				repartitionRH19 = 0;
			}  else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = (int) settings.toEntranceB().getCurrentValue();
				repartition2 = (settings.toEntranceBRepartition().getCurrentValue()) / (100.0);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRH17 = 0;
				repartitionRH18 = 0;
				repartitionRH19 = 0;
			}
			applyFlowFromVariables(r);
		}
		
		// From entrance E to France ========================================================================
		
		for (Ride r: n.getAllRides("rSortieCERNNW").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rD884SW")) {
				value = (int) settings.fromEntranceE().getCurrentValue();
				repartition1 = 0;
				repartition2 = settings.fromEntranceERepartition().getCurrentValue1() / (100.0);
			} else if (lastRoadIs(r, "rRueDeGeneveNW")) {
				value = (int) settings.fromEntranceE().getCurrentValue();
				repartition1 = settings.fromEntranceERepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.fromEntranceERepartition().getCurrentValue2() / (100.0);
			} else if (lastRoadIs(r, "rRueGermaineTillionNE")) {
				value = (int) settings.fromEntranceE().getCurrentValue();
				repartition1 = settings.fromEntranceERepartition().getCurrentValue2() / (100.0);
				repartition2 = 1;
			}
			repartitionRH17 = settings.fromEntranceERepartitionRH2().getCurrentValue1() / 100.0;
			repartitionRH18 = (settings.fromEntranceERepartitionRH2().getCurrentValue2() - settings.fromEntranceERepartitionRH2().getCurrentValue1()) / 100.0;
			repartitionRH19 = (100 - settings.fromEntranceERepartitionRH2().getCurrentValue2()) / 100.0;
			repartitionRHEvening = 1;
			applyFlowFromVariables(r);
		}
		
		// From entrance A ==================================================================================
		
		for (Ride r: n.getAllRides("rRouteBellNE").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rD884SW") || lastRoadIs(r, "rRueDeGeneveNW") || lastRoadIs(r, "rRueGermaineTillionNE") || lastRoadIs(r, "rC5NE")) {
				value = (int) settings.fromEntranceA().getCurrentValue();
				repartition2 = (100-settings.fromEntranceARepartition().getCurrentValue()) / (100.0*4);
			} else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = (int) settings.fromEntranceA().getCurrentValue();
				repartition2 = (settings.fromEntranceARepartition().getCurrentValue()) / (100.0);
			}
			repartitionRH17 = settings.fromEntranceARepartitionRH2().getCurrentValue1() / 100.0;
			repartitionRH18 = (settings.fromEntranceARepartitionRH2().getCurrentValue2() - settings.fromEntranceARepartitionRH2().getCurrentValue1()) / 100.0;
			repartitionRH19 = (100 - settings.fromEntranceARepartitionRH2().getCurrentValue2()) / 100.0;
			repartitionRHEvening = 1;
			applyFlowFromVariables(r);
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
				int sum = 0;
				if (n.getAllRides(road.getName()) != null) {
					
					for (Ride ride: n.getAllRides(road.getName()).getNetworkRides()) {
						//Utils.saveCheckingValues("(" + Integer.toString(ride.getFlow().get(h)) + ") - ");
						sum += ride.getFlow().get(h);
						
					}
				}
				Utils.saveCheckingValues("(" + Integer.toString(sum) + ") ");
				road.setGenerateVehicules(h, h+1, sum);
				tmp += sum;
			}
			Utils.saveCheckingValues("\n");
			Utils.saveCheckingValues("Total : " + tmp + "\n");
		}
		
		/*for (Road road2: n.getRoads()) {
			if (road2.getName().equals("rSortieCERNNW")) {
				System.out.println(road2.getName() + " : ");
				for (int i=0; i<road2.getFlow().size() ; i++) {
					System.out.print(road2.getFlow().get(i) + " ");
				}
				System.out.println();
			}
		}*/
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
	}
}
