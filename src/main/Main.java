package main;

import utils.Utils;

public class Main {

	public static void main(String[] args) {
		
		Simulation simulation = new Simulation("Trafic simulation around CERN",1000,700);
		simulation.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				Utils.log("Closed ===============================================================\n");
			}
		}, "Shutdown-thread"));
	}
	
}
