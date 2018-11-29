package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import data.DataManager;


public class Utils {

	public static PrintStream log;
	public static PrintStream dataCounters;
	public static PrintStream dataSegmentCounters;
	public static PrintStream dataChecking;
	public static PrintStream dataLeakyBuckets;
	public static PrintStream dataEnterExit;
	public static PrintStream dataMeanTimeSpent;
	public static String dataStrCounters = "";
	public static String dataStrSegmentCounters = "";
	public static String dataStrLeakyBuckets = "";
	public static String dataStrEnterExit = "";
	public static String dataStrMeanTimeSpent = "";
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
	// initialize all data output streams (folder /data/XXXXXXXX_XXXXXX.txt
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
		initDataSegmentCounters();
		initCheckingValues();
		initDataLeakyBuckets();
		initDataEnterExit();
		initDataMeanTimeSpent();
	}
	public static void initDataLeakyBuckets() {
		try {
			dataLeakyBuckets = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_leakyBuckets.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataLeakyBuckets.print("Thoiry St-Genis Ferney Tun Geneva\n");
	}
	public static void initDataCounters() {
		try {
			dataCounters = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_counters.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataCounters.print("Number of vehicles per minute passing through counters ---\n");
		dataCounters.print("Time Counter1A Counter1B Counter2A Counter2B\n");
	}
	public static void initDataSegmentCounters() {
		try {
			dataSegmentCounters = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_segment_counters.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataSegmentCounters.print("Number of vehicles at a given time, syntax is [counter ID, speed] ---\n");
		dataSegmentCounters.print("Time 1A,0 1A,1 1A,2 1B,0 1B,1 1B,2 2A,0 2A,1 2A,2 2B,0 2B,1 2B,2\n");
	}
	public static void initCheckingValues() {
		try {
			dataChecking = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_checking.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataChecking.print("Checking probabilities\n");
	}
	public static void initDataEnterExit() {
		try {
			dataEnterExit = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_enter_exit.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataEnterExit.print("Checking enters and exits (per hour)\n");
	}
	public static void initDataMeanTimeSpent() {
		try {
			dataMeanTimeSpent = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_mean_time.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dataMeanTimeSpent.print("Checking mean time spent on network in seconds (per hour)\n");
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
		saveDataSegmentCounters();
		saveDataLeakyBuckets();
		saveDataEnterExit();
		saveMeanTimeSpent();
	}
	public static void writeDataCounters(String text) {
		dataStrCounters = dataStrCounters + text;
	}
	public static void saveDataCounters() {
		dataCounters.print(dataStrCounters);
		dataStrCounters = "";
	}
	public static void writeDataSegmentCounters(String text) {
		dataStrSegmentCounters = dataStrSegmentCounters + text;
	}
	public static void saveDataSegmentCounters() {
		dataSegmentCounters.print(dataStrSegmentCounters);
		dataStrSegmentCounters = "";
	}
	public static void writeDataLeakyBuckets(String text) {
		dataStrLeakyBuckets = dataStrLeakyBuckets + text;
	}
	public static void saveDataLeakyBuckets() {
		dataLeakyBuckets.print(dataStrLeakyBuckets);
		dataStrLeakyBuckets = "";
	}
	public static void saveDataEnterExit() {
		
		for (int i=0; i<16; i++) {
			dataStrEnterExit += DataManager.flowPerExitEmpiric[i] + "\t";
			DataManager.flowPerExitEmpiric[i] = 0;
		}
		dataStrEnterExit += "\n";
		dataEnterExit.print(dataStrEnterExit);
		dataStrEnterExit = "";
	}
	public static void saveMeanTimeSpent() {
		double meanTimeLastHour = 0;
		for (Integer i: DataManager.timeSpent) {
			meanTimeLastHour += i;
		}
		meanTimeLastHour = meanTimeLastHour / (double) (DataManager.timeSpent.size());
		DataManager.timeSpent = new ArrayList<Integer>();
		dataStrMeanTimeSpent = String.format("%.2f", meanTimeLastHour) + "\n";
		dataMeanTimeSpent.print(dataStrMeanTimeSpent);
		dataStrMeanTimeSpent = "";
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
