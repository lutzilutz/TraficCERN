package main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.Utils;

public class Main {

	public static void main(String[] args) {
		
		Simulation simulation = new Simulation("Trafic simulation around CERN",1000,700);
		simulation.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				//Date date = new Date();
				
				//Utils.log(dateFormat.format(date) + "\n");
				
				Utils.log("Closed ===============================================================\n\n");
			}
		}, "Shutdown-thread"));
	}
	
}
