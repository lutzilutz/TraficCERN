package main;

import network.NetworkComputing;
import states.SimState;
import states.State;
import utils.Utils;

public class Main {

	public static void main(String[] args) {
		
		// instantiate the Simulator object (the software in itself)
		Simulator simulator = new Simulator("Trafic simulation around CERN",1000,700);
		simulator.start();
		
		// add a shutdown-hook to be able to print a last entry in log output before exiting
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				if (!NetworkComputing.writtenFinalData) {
					if (State.getState() instanceof SimState) {
						Utils.logWarningln("User ends simulation prematurely at step " + simulator.getSimState().getStep());
					}
				}
				Utils.logln("Closed ===============================================================");
			}
		}, "Shutdown-thread"));
	}
	
}
