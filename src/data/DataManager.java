package data;

import java.util.ArrayList;

import elements.Ride;
import elements.Road;
import main.Simulation;
import network.AllNetworkRides;
import network.Network;
import states.SimSettingsState;
import utils.Utils;

public class DataManager {
	
	private static int value = 0;
	private static double repartition1 = 0;
	private static double repartition2 = 0;
	private static double repartitionRH = 0;
	
	private static int nFrGe = 12500;
	public static int nFrGe_fromSW = 30;
	public static int nFrGe_fromNW = 40;
	public static int nFrGe_fromNE = 30;
	
	public static int nToE = 1605;
	public static int nToE_fromSW = 30;
	public static int nToE_fromNW = 65;
	public static int nToE_fromNE = 5;

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
				repartitionRH = 1;
			}
			r.setFlow((int) (value*(repartition2-repartition1)*(1-repartitionRH)/24.0));
			r.setFlowRH((int) (value*(repartition2-repartition1)*repartitionRH/24.0));
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
				repartitionRH = 1;
			}
			r.setFlow((int) (value*(repartition2-repartition1)*(1-repartitionRH)/24.0));
			r.setFlowRH((int) (value*(repartition2-repartition1)*repartitionRH/24.0));
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
				repartitionRH = 1;
			}
			r.setFlow((int) (value*(repartition2-repartition1)*(1-repartitionRH)/24.0));
			r.setFlowRH((int) (value*(repartition2-repartition1)*repartitionRH/24.0));
			
		}
	}
	public static void applyRidesToRoads(Simulation simulation) {
		
		Network n = simulation.getSimState().getNetwork();
		ArrayList<AllNetworkRides> anrAL = n.getAllNetworkRides();
		
		for (Ride tmp: n.getAllRides("rRueDeGeneveSE").getNetworkRides()) {
			//tmp.print();
			//System.out.println(tmp.getFlow());
		}
		
		
		for (Road road: n.getRoads()) {
			int sum = 0;
			int sumRH = 0;
			if (n.getAllRides(road.getName()) != null) {
				for (Ride ride: n.getAllRides(road.getName()).getNetworkRides()) {
					sum += ride.getFlow();
					sumRH += ride.getFlowRH();
				}
			}
			//System.out.print(sum + " ");
			road.setGenerateVehicules(sum);
			road.setGenerateVehiculesRH(sumRH);
			
			if (road.getName().equals("rRueDeGeneveSE")) {
				System.out.println("NotRH : " + road.getGenerateVehicules() + " , RH :" + road.getGenerateVehiculesRH());
			}
		}
		//System.out.println();
		
	}
	public static void resetValues() {
		value = 0;
		repartition1 = 0;
		repartition2 = 0;
		repartitionRH = 0;
	}
	public static int getFromFrToGe() {
		return nFrGe;
	}
}
