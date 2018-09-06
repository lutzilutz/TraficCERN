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
		SimSettingsState settings = simulation.getSimSettingsState();
		
		
		for (Ride r: n.getAllRides("rD884NE").getNetworkRides()) {
			int value = 0;
			double repartition1 = 0;
			double repartition2 = 0;
			double repartitionRH = 0;
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = (int) settings.fromFrToGe().getCurrentValue();
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue1() / (100.0);
				repartitionRH = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				//r.setFlow((int) (simulation.getSimSettingsState().fromFrToGe().getCurrentValue()*simulation.getSimSettingsState().fromFrToGeRepartition().getCurrentValue1() / (100.0)));	
			}
			
			r.setFlow((int) (value*(repartition2-repartition1)*(1-repartitionRH)));
			r.setFlowRH((int) (value*(repartition2-repartition1)*repartitionRH));
		}
		// Rue de Genève to Geneva
		for (Ride r: n.getAllRides("rRueDeGeneveSE").getNetworkRides()) {
			int value = 0;
			double repartition1 = 0;
			double repartition2 = 0;
			double repartitionRH = 0;
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = (int) settings.fromFrToGe().getCurrentValue();
				repartition1 = settings.fromFrToGeRepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue2() / (100.0);
				repartitionRH = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				//r.setFlow((int) (simulation.getSimSettingsState().fromFrToGe().getCurrentValue()*(simulation.getSimSettingsState().fromFrToGeRepartition().getCurrentValue2()-simulation.getSimSettingsState().fromFrToGeRepartition().getCurrentValue1()) / (100.0)));
			}
			r.setFlow((int) (value*(repartition2-repartition1)*(1-repartitionRH)));
			r.setFlowRH((int) (value*(repartition2-repartition1)*repartitionRH));
			//r.print();
			//System.out.println(" -> " + r.getFlow());
		}
		// Rue de Genève to Geneva
		for (Ride r: n.getAllRides("rRueGermaineTillionSW").getNetworkRides()) {
			int value = 0;
			double repartition1 = 0;
			double repartition2 = 0;
			double repartitionRH = 0;
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = (int) settings.fromFrToGe().getCurrentValue();
				repartition1 = settings.fromFrToGeRepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue2() / (100.0);
				repartitionRH = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				//r.setFlow((int) (simulation.getSimSettingsState().fromFrToGe().getCurrentValue()*(100-simulation.getSimSettingsState().fromFrToGeRepartition().getCurrentValue2()) / (100.0)));
			}
			r.setFlow((int) (value*(repartition2-repartition1)*(1-repartitionRH)));
			r.setFlowRH((int) (value*(repartition2-repartition1)*repartitionRH));
			
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
	public static int getFromFrToGe() {
		return nFrGe;
	}
}
