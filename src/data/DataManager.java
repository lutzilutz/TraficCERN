package data;

import main.Simulation;

public class DataManager {

	public static void loadData(Simulation simulation) {
	
		simulation.getSimSettingsState().getTest();
		
	}
	public static void applyData(Simulation simulation) {
		
		simulation.setEntranceERate(simulation.getSimSettingsState().getTest().getCurrentValue());
		
	}
}
