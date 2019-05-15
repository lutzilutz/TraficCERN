package main;

import network.NetworkComputing;
import utils.Utils;

public class Main {

	public static void main(String[] args) {
		
		Simulation simulation = new Simulation("Trafic simulation around CERN",1000,700);
		simulation.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				if (!NetworkComputing.writtenFinalData) {
					Utils.log("    WARNING : User ends simulation prematurely at step " + simulation.getSimState().getStep() + "\n");
				}
				Utils.log("Closed ===============================================================\n");
			}
		}, "Shutdown-thread"));
	}
	
}
