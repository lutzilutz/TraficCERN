package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import data.DataManager;


public class Utils {

	public static PrintStream log;
	public static PrintStream dataCounters;
	public static PrintStream dataChecking;
	public static String dataStr = "";
	public static String dataDir = "data";
	public static long time = 0;
	
	public static void initLog() {
		try {
			log = new PrintStream(new FileOutputStream("log.txt", true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void initAllData() {
		File directory = new File(dataDir);
		if (! directory.exists()){
			directory.mkdir();
		}
		
		initDataCounters();
		initCheckingValues();
		
	}
	public static void initDataCounters() {
		try {
			dataCounters = new PrintStream(new FileOutputStream(dataDir + "/" + "data_counters.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataCounters.print("Time Counter1 Counter2 Counter3 Counter4\n");
	}
	public static void initCheckingValues() {
		try {
			dataChecking = new PrintStream(new FileOutputStream(dataDir + "/" + "data_checking.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataChecking.print("Checking probabilities\n");
	}
	public static void saveCheckingValues() {
		dataChecking.print("From France to Geneva (per day) :     expected " + DataManager.nFrGeChosen + ", got " + DataManager.nFrGeEmpiric + "\n");
		dataChecking.print("From France to Entrance E (per day) : expected " + DataManager.nToEChosen + ", got " + DataManager.nToEEmpiric + "\n");
		
	}
	public static void saveCheckingValues(String text) {
		//dataChecking.print(text);	
	}
	public static void log(String text) {
		log.print(text);
		System.out.print(text);
	}
	public static void log(Exception e) {
		for (int i=0; i<e.getStackTrace().length;i++) {
			log.print("   " + e.getStackTrace()[i] + "\n");
			System.out.print("   " + e.getStackTrace()[i] + "\n");
		}
	}
	public static void tick() {
		time = System.nanoTime();
	}
	public static void logTime() {
		long delta = (System.nanoTime()-time) / 100000;
		double deltaMilli = delta / 10.0;
		String text = " in ";
		text = text + deltaMilli + " ms\n";
		log.print(text);
		System.out.print(text);
	}
	public static void writeData(String text) {
		dataStr = dataStr + text;
	}
	public static void saveData() {
		dataCounters.print(dataStr);
		dataStr = "";
	}
	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
