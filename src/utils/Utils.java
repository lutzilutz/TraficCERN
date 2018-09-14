package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import data.DataManager;


public class Utils {

	public static PrintStream log;
	public static PrintStream dataCounters;
	public static PrintStream dataSegmentCounters;
	public static PrintStream dataChecking;
	public static PrintStream dataLeakyBuckets;
	public static String dataStrCounters = "";
	public static String dataStrLeakyBuckets = "";
	public static String dataDir = "data";
	private static Date date = new Date();
	private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
	public static String dataDirSim;
	public static long time = 0;
	
	public static void initLog() {
		try {
			log = new PrintStream(new FileOutputStream("log.txt", true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void initAllData() {
		date = new Date();
		dataDirSim = dataDir + "/" + dateFormat.format(date);
		
		File directory = new File(dataDir);
		File directorySim = new File(dataDirSim);
		
		if (! directory.exists()){
			directory.mkdir();
		}
		
		if (! directorySim.exists()){
			directorySim.mkdir();
		}
		
		initDataCounters();
		//initDataSegmentCounters();
		initCheckingValues();
		initDataLeakyBuckets();
		
	}
	public static void initDataLeakyBuckets() {
		try {
			dataLeakyBuckets = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_leakyBuckets.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataLeakyBuckets.print("Time Thoiry St-Genis Ferney Tun Geneva\n");
	}
	public static void initDataCounters() {
		try {
			dataCounters = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_counters.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataCounters.print("Time Counter1A Counter1B Counter2A Counter2B\n");
	}
	public static void initDataSegmentCounters() {
		try {
			dataSegmentCounters = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_segment_counters.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataSegmentCounters.print("Time Counter1A-speed=0 Counter1A-speed=1 Counter1A-speed=2 Counter1B-speed=0 Counter1B-speed=1 Counter1B-speed=2 Counter2A-speed=0 Counter2A-speed=1 Counter2A-speed=2 Counter2B-speed=0 Counter2B-speed=1 Counter2B-speed=2\n");
	}
	public static void initCheckingValues() {
		try {
			dataChecking = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_checking.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataChecking.print("Checking probabilities\n");
	}
	public static void saveCheckingValues() {
		float errorFrGe;
		float errorGeFr;
		float errorToA;
		float errorFromA;
		float errorToB;
		float errorFromB;
		float errorToE;
		float errorFromE;
		if (DataManager.nFrGeChosen > 0) {
			errorFrGe = Math.round((1000*Math.abs((DataManager.nFrGeChosen-DataManager.nFrGeEmpiric)/ (float)DataManager.nFrGeChosen))) / (float) 10;
		} else {
			errorFrGe = 0;
		}
		if (DataManager.nFrGeChosen > 0) {
			errorGeFr = Math.round((1000*Math.abs((DataManager.nGeFrChosen-DataManager.nGeFrEmpiric)/ (float)DataManager.nGeFrChosen))) / (float) 10;
		} else {
			errorGeFr = 0;
		}
		if (DataManager.nToAChosen > 0) {
			errorToA = Math.round((1000*Math.abs((DataManager.nToAChosen-DataManager.nToAEmpiric)/ (float)DataManager.nToAChosen))) / (float) 10;
		} else {
			errorToA = 0;
		}
		if (DataManager.nFromAChosen > 0) {
			errorFromA = Math.round((1000*Math.abs((DataManager.nFromAChosen-DataManager.nFromAEmpiric)/ (float)DataManager.nFromAChosen))) / (float) 10;
		} else {
			errorFromA = 0;
		}
		if (DataManager.nToBChosen > 0) {
			errorToB = Math.round((1000*Math.abs((DataManager.nToBChosen-DataManager.nToBEmpiric)/ (float)DataManager.nToBChosen))) / (float) 10;
		} else {
			errorToB = 0;
		}
		if (DataManager.nFromBChosen > 0) {
			errorFromB = Math.round((1000*Math.abs((DataManager.nFromBChosen-DataManager.nFromBEmpiric)/ (float)DataManager.nFromBChosen))) / (float) 10;
		} else {
			errorFromB = 0;
		}
		if (DataManager.nToEChosen > 0) {
			errorToE = Math.round((1000*Math.abs((DataManager.nToEChosen-DataManager.nToEEmpiric)/ (float)DataManager.nToEChosen))) / (float) 10;
		} else {
			errorToE = 0;
		}
		if (DataManager.nFromEChosen > 0) {
			errorFromE = Math.round((1000*Math.abs((DataManager.nFromEChosen-DataManager.nFromEEmpiric)/ (float)DataManager.nFromEChosen))) / (float) 10;
		} else {
			errorFromE = 0;
		}
		
		dataChecking.print("From France to Geneva (per day) :     expected " + DataManager.nFrGeChosen + ", got " + DataManager.nFrGeEmpiric + " (" + errorFrGe + "%)\n");
		dataChecking.print("From Geneva to France (per day) :     expected " + DataManager.nGeFrChosen + ", got " + DataManager.nGeFrEmpiric + " (" + errorGeFr + "%)\n");
		dataChecking.print("To entrance A (per day) :             expected " + DataManager.nToAChosen + ", got " + DataManager.nToAEmpiric + " (" + errorToA + "%)\n");
		dataChecking.print("From entrance A (per day) :           expected " + DataManager.nFromAChosen + ", got " + DataManager.nFromAEmpiric + " (" + errorFromA + "%)\n");
		dataChecking.print("To entrance B (per day) :             expected " + DataManager.nToBChosen + ", got " + DataManager.nToBEmpiric + " (" + errorToB + "%)\n");
		dataChecking.print("From entrance B (per day) :           expected " + DataManager.nFromBChosen + ", got " + DataManager.nFromBEmpiric + " (" + errorFromB + "%)\n");
		dataChecking.print("From France to entrance E (per day) : expected " + DataManager.nToEChosen + ", got " + DataManager.nToEEmpiric + " (" + errorToE + "%)\n");
		dataChecking.print("From entrance E to France (per day) : expected " + DataManager.nFromEChosen + ", got " + DataManager.nFromEEmpiric + " (" + errorFromE + "%)\n");
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
	public static void saveData() {
		saveDataCounters();
		saveDataLeakyBuckets();
	}
	public static void writeDataCounters(String text) {
		dataStrCounters = dataStrCounters + text;
	}
	public static void saveDataCounters() {
		dataCounters.print(dataStrCounters);
		dataStrCounters = "";
	}
	public static void writeDataLeakyBuckets(String text) {
		dataStrLeakyBuckets = dataStrLeakyBuckets + text;
	}
	public static void saveDataLeakyBuckets() {
		dataLeakyBuckets.print(dataStrLeakyBuckets);
		dataStrLeakyBuckets = "";
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
