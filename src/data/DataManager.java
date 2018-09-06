package data;

import java.util.ArrayList;

import elements.Ride;
import elements.Road;
import main.Simulation;
import network.AllNetworkRides;
import network.Network;

public class DataManager {
	
	private static int nFrGe = 12500;
	public static int nFrGe_fromSW = 30;
	public static int nFrGe_fromNW = 40;
	public static int nFrGe_fromNE = 30;

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
		
		applyDataToRides(simulation);
		applyRidesToRoads(simulation);
		
	}
	public static void applyDataToRides(Simulation simulation) {
		
		Network n = simulation.getSimState().getNetwork();
		
		for (Ride r: n.getAllRides("rD884NE").getNetworkRides()) {
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				r.setFlow((int) (simulation.getSimSettingsState().fromFrToGe().getCurrentValue()*simulation.getSimSettingsState().fromFrToGeRepartition().getCurrentValue1() / (24.0*100.0)));
			} else {
				r.setFlow(0);
			}
			
		}
		// Rue de Genève to Geneva
		for (Ride r: n.getAllRides("rRueDeGeneveSE").getNetworkRides()) {
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				r.setFlow((int) (simulation.getSimSettingsState().fromFrToGe().getCurrentValue()*(simulation.getSimSettingsState().fromFrToGeRepartition().getCurrentValue2()-simulation.getSimSettingsState().fromFrToGeRepartition().getCurrentValue1()) / (24.0*100.0)));
			} else {
				r.setFlow(0);
			}
			r.print();
			System.out.println(" -> " + r.getFlow());
		}
		// Rue de Genève to Geneva
		for (Ride r: n.getAllRides("rRueGermaineTillionSW").getNetworkRides()) {
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				r.setFlow((int) (simulation.getSimSettingsState().fromFrToGe().getCurrentValue()*(100-simulation.getSimSettingsState().fromFrToGeRepartition().getCurrentValue2()) / (24.0*100.0)));
			} else {
				r.setFlow(0);
			}
		}
	}
	public static void applyRidesToRoads(Simulation simulation) {
		
		Network n = simulation.getSimState().getNetwork();
		ArrayList<AllNetworkRides> anrAL = n.getAllNetworkRides();
		
		for (Ride tmp: n.getAllRides("rRueDeGeneveSE").getNetworkRides()) {
			tmp.print();
			System.out.println(tmp.getFlow());
		}
		
		
		for (Road road: n.getRoads()) {
			int sum = 0;
			
			if (n.getAllRides(road.getName()) != null) {
				for (Ride ride: n.getAllRides(road.getName()).getNetworkRides()) {
					
					sum += ride.getFlow();
					
				}
			}
			/*for (AllNetworkRides anr: anrAL.g) {
				if (anr.getRoadName().equals(road.getName())) {
					//for (Ride ride: anr.getNetworkRides()) {
					for (Ride ride: anr.getNetworkRides()) {
							if (road.getName().equals("rRueDeGeneveSE")) {
							ride.print();
							System.out.println(" -> " + ride.getFlow());
						}
						sum += ride.getFlow();
					}
				}
			}*/
			System.out.print(sum + " ");
			road.setGenerateVehicules(sum);
			
		}
		System.out.println();
		
	}
	public static int getFromFrToGe() {
		return nFrGe;
	}
}
