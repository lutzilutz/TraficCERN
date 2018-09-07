package data;

import elements.Ride;
import elements.Road;
import main.Simulation;
import network.Network;
import states.SimSettingsState;

public class DataManager {
	
	private static int value = 0;
	private static double repartition1 = 0;
	private static double repartition2 = 0;
	private static double repartitionRH = 0;
	private static double repartitionRH7 = 0;
	private static double repartitionRH8 = 0;
	private static double repartitionRH9 = 0;
	
	private static int nFrGe = 12500;
	public static int nFrGe_fromSW = 30;
	public static int nFrGe_fromNW = 40;
	public static int nFrGe_fromNE = 30;
	
	public static int nToE = 1605;
	public static int nToE_fromSW = 30;
	public static int nToE_fromNW = 65;
	public static int nToE_fromNE = 5;
	public static int nToE_7 = 33;
	public static int nToE_8 = 47;
	public static int nToE_9 = 20;
	
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
		
		if (!simulation.getSimState().getNetwork().isRandomGeneration()) {
			applyDataToRides(simulation);
			applyRidesToRoads(simulation);
		}
	}
	public static void applyDataToRides(Simulation simulation) {
		
		Network n = simulation.getSimState().getNetwork();
		SimSettingsState settings = simulation.getSimSettingsState();
		
		for (Ride r: n.getAllRides("rD884NE").getNetworkRides()) {
			resetValues();
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = (int) settings.fromFrToGe().getCurrentValue();
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue1() / (100.0);
				repartitionRH = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
			} else if (lastRoadIs(r, "rSortieCERNSE") || lastRoadIs(r, "rD884CERN")) {
				value = (int) settings.toEntranceE().getCurrentValue()*8;
				repartition2 = settings.toEntranceERepartition().getCurrentValue1() / (100.0);
				repartitionRH7 = settings.toEntranceERepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceERepartitionRH().getCurrentValue2() - settings.toEntranceERepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100-settings.toEntranceERepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRH = 1;
			}
			r.setFlow((int) (value*(repartition2-repartition1)*(1-repartitionRH)/24.0));
			
			if (repartitionRH7 > 0 && repartitionRH8 > 0 && repartitionRH9 > 0) {
				r.setFlow(7, 8, (int) (value*(repartition2-repartition1)*repartitionRH*repartitionRH7/24.0));
				r.setFlow(8, 9, (int) (value*(repartition2-repartition1)*repartitionRH*repartitionRH8/24.0));
				r.setFlow(9, 10, (int) (value*(repartition2-repartition1)*repartitionRH*repartitionRH9/24.0));
			}
		}
		// Rue de Genève to Geneva
		for (Ride r: n.getAllRides("rRueDeGeneveSE").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = (int) settings.fromFrToGe().getCurrentValue();
				repartition1 = settings.fromFrToGeRepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue2() / (100.0);
				repartitionRH = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
			} else if (lastRoadIs(r, "rSortieCERNSE") || lastRoadIs(r, "rD884CERN")) {
				value = (int) settings.toEntranceE().getCurrentValue()*8;
				repartition1 = settings.toEntranceERepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.toEntranceERepartition().getCurrentValue2() / (100.0);
				repartitionRH7 = settings.toEntranceERepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceERepartitionRH().getCurrentValue2() - settings.toEntranceERepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100-settings.toEntranceERepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRH = 1;
			}
			r.setFlow((int) (value*(repartition2-repartition1)*(1-repartitionRH)/24.0));
			
			if (repartitionRH7 > 0 && repartitionRH8 > 0 && repartitionRH9 > 0) {
				r.setFlow(7, 8, (int) (value*(repartition2-repartition1)*repartitionRH*repartitionRH7/24.0));
				r.setFlow(8, 9, (int) (value*(repartition2-repartition1)*repartitionRH*repartitionRH8/24.0));
				r.setFlow(9, 10, (int) (value*(repartition2-repartition1)*repartitionRH*repartitionRH9/24.0));
			}
		}
		// Rue de Genève to Geneva
		for (Ride r: n.getAllRides("rRueGermaineTillionSW").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = (int) settings.fromFrToGe().getCurrentValue();
				repartition1 = settings.fromFrToGeRepartition().getCurrentValue2() / (100.0);
				repartition2 = 1;
				repartitionRH = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
			} else if (lastRoadIs(r, "rSortieCERNSE") || lastRoadIs(r, "rD884CERN")) {
				value = (int) settings.toEntranceE().getCurrentValue()*8;
				repartition1 = settings.toEntranceERepartition().getCurrentValue2() / (100.0);
				repartition2 = 1;
				repartitionRH7 = settings.toEntranceERepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceERepartitionRH().getCurrentValue2() - settings.toEntranceERepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100-settings.toEntranceERepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRH = 1;
			}
			r.setFlow((int) (value*(repartition2-repartition1)*(1-repartitionRH)/24.0));
			
			if (repartitionRH7 > 0 && repartitionRH8 > 0 && repartitionRH9 > 0) {
				r.setFlow(7, 8, (int) (value*(repartition2-repartition1)*repartitionRH*repartitionRH7/24.0));
				r.setFlow(8, 9, (int) (value*(repartition2-repartition1)*repartitionRH*repartitionRH8/24.0));
				r.setFlow(9, 10, (int) (value*(repartition2-repartition1)*repartitionRH*repartitionRH9/24.0));
			}
		}
	}
	public static void applyRidesToRoads(Simulation simulation) {
		
		Network n = simulation.getSimState().getNetwork();
		
		for (Road road: n.getRoads()) {
			if (n.getAllRides(road.getName()) != null) {
				if (road.getName().equals("rRueDeGeneveSE")) {
					System.out.println(road.getName() + " : ");
					for (Ride ride: n.getAllRides(road.getName()).getNetworkRides()) {
						ride.print();
						System.out.println();
						for (Integer i: ride.getFlow()) {
							System.out.print(i + " ");
						}
						System.out.println();
					}
				}
			}
			for (int h=0 ; h<24 ; h++) {
				int sum = 0;
				if (n.getAllRides(road.getName()) != null) {
					for (Ride ride: n.getAllRides(road.getName()).getNetworkRides()) {
						sum += ride.getFlow().get(h);
						
					}
				}
				road.setGenerateVehicules(h, h+1, sum);
			}
		}
		
	}
	public static void resetValues() {
		value = 0;
		repartition1 = 0;
		repartition2 = 0;
		repartitionRH = 0;
		repartitionRH7 = 0;
		repartitionRH8 = 0;
		repartitionRH9 = 0;
	}
	public static int getFromFrToGe() {
		return nFrGe;
	}
}
