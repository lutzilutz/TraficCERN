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
	
	public static int franceToFrance = 50;
	
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
	
	public static int cycle1LTSmin = 10;
	public static int cycle1LTSmax = 15;
	public static int cycle2LTSmin = 30;
	public static int cycle2LTSmax = 35;
	public static int cycle3LTSmin = 10;
	public static int cycle3LTSmax = 15;
	public static int cycle4LTSmin = 10;
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
	public static void applyData(Simulation simulation) {
		
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
			applyDataToRides(simulation);
			applyRidesToRoads(simulation);
			//Utils.log("done\n");
		} else {
			//Utils.log("applying random gen to Network ... done\n");
		}
	}
	public static void applyFlowFromVariables(Simulation sim, Ride r) {
		
		/*if (r.getNextConnections().size() > 0) {
			numberOfSameRide = numberOfSameRides(sim, r.getRoadName(), r.getNextConnections().get(r.getNextConnections().size()-1).getName());
		} else {
			numberOfSameRide = 1;
		}*/
//		int tmp = randomValue;
//		if (!useFrToFr) {
//			randomValue = 0;
//		}
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
		//randomValue = tmp;
	}
	public static void applyDataToRides(Simulation simulation) {
		
		Network n = simulation.getSimState().getNetwork();
		SimSettingsState settings = simulation.getSimSettingsState();
		
		for (AllNetworkRides anr: n.getAllNetworkRides()) {
			for (Ride r: anr.getNetworkRides()) {
				r.setNumberOfSameRide(numberOfSameRides(simulation, r.getRoadName(), r.getNextConnections().get(r.getNextConnections().size()-1).getName()));
			}
		}
		
		// France to Geneva =================================================================================
		
		if (n.getAllRides("rD884NE") == null) {
			System.out.println("No AllNetworkRides for D884NE");
		} else {
			if (n.getAllRides("rD884NE").getNetworkRides().isEmpty()) {
				
			}
		}
		
		/*for (Ride r: n.getAllRides("rD884NE").getNetworkRides()) {
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
			}
			applyFlowFromVariables(simulation, r);
		}*/
		
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
				//value = (int) (randomValue/1.0);
				//repartition2 = 1;
				//useFrToFr = true;
			}/* else  if (lastRoadIs(r, "rRueGermaineTillionNE")) {
				value = (int) (randomValue/2.0);
				repartition2 = 1;
				useFrToFr = true;
			}*/
			applyFlowFromVariables(simulation, r);
		}

		/*for (Ride r: n.getAllRides("rD884NE").getNetworkRides()) {
			
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				r.ratioFlow((float) (settings.fromFrToFr().getCurrentValue()/100.0));
			} else if (lastRoadIs(r, "rRueDeGeneveNW")) {
				r.ratioFlow((float) (1-settings.fromFrToFr().getCurrentValue()/100.0));
			} else if (lastRoadIs(r, "rRueGermaineTillionNE")) {
				r.ratioFlow((float) (1-settings.fromFrToFr().getCurrentValue()/100.0));
			}
		}
		randomValue = 0;*/
		
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
			}
			applyFlowFromVariables(simulation, r);
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
			}
			applyFlowFromVariables(simulation, r);
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
			applyFlowFromVariables(simulation, r);
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
			applyFlowFromVariables(simulation, r);
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
			applyFlowFromVariables(simulation, r);
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
			applyFlowFromVariables(simulation, r);
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
				applyFlowFromVariables(simulation, r);
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
}
