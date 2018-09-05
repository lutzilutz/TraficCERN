package data;

import main.Simulation;

public class DataManager {

	public static void loadData(Simulation simulation) {
	
		// From France to Geneva ============================================================================
		// D884 to Geneva
		
		simulation.getSimState().getNetwork().selectARide("rD884NE", "rRouteDeMeyrinSouthSE").print();
		simulation.getSimState().getNetwork().selectARide("rD884NE").print();
		simulation.getSimState().getNetwork().getAllRides("rD884NE").print();
		
	}
	public static void applyData(Simulation simulation) {
		
		simulation.setEntranceERate(simulation.getSimSettingsState().getTest().getCurrentValue());
		
	}
}
