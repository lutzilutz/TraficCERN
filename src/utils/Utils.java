package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Utils {

	private static PrintStream log;
	private static PrintStream dataCounters;
	private static PrintStream dataLeakyBuckets;
	private static PrintStream dataMeanTimeSpent_transit;
	private static PrintStream dataMeanTimeSpent_cern;
	private static String dataStrCounters = "";
	private static String dataStrLeakyBuckets = "";
	private static String dataStrMeanTimeSpent_transit = "";
	private static String dataStrMeanTimeSpent_cern = "";
	private static String dataDir = "data";
	private static Date date = new Date();
	private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
	private static String dataDirSim;
	private static long time = 0;
	
	public static void initLog() {
		try {
			log = new PrintStream(new FileOutputStream("log.txt", true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		Utils.logln("Started at " + dateFormat.format(date) + " =======================================");
		Utils.logln("Initialization -------------------------------------------------------");
	}
	// initialize all data output streams for N simulations (folder /data/XXXXXXXX_XXXXXX_NN.txt
	public static void initAllData(int numberOfSimulations) {
		date = new Date();
		dataDirSim = dataDir + "/" + dateFormat.format(date) + "_" + Integer.toString(numberOfSimulations);
		
		File directory = new File(dataDir);
		File directorySim = new File(dataDirSim);
		
		if (!directory.exists()) {directory.mkdir();}
		if (!directorySim.exists()) {directorySim.mkdir();}
		
		initDataCounters();
		initDataLeakyBuckets();
		initDataMeanTimeSpent();
		
		Utils.logInfoln("Initialized simulation folder " + dateFormat.format(date) + "_" + Integer.toString(numberOfSimulations));
	}
	private static void initDataLeakyBuckets() {
		try {
			dataLeakyBuckets = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_leakyBuckets.txt", false));
		} catch (FileNotFoundException e) {
			Utils.logErrorln("Couldn't init print stream of data_leakyBuckets.txt");
			Utils.log(e);
		}
		dataLeakyBuckets.print("Thoiry-Exp Thoiry-StdDev St-Genis-Exp St-Genis-StdDev Ferney-Exp Ferney-StdDev Tun-Exp Tun-StdDev Geneva-Exp Geneva-StdDev EntranceB-L-Exp EntranceB-L-StdDev EntranceB-R-Exp EntranceB-R-StdDev\n");
	}
	private static void initDataCounters() {
		try {
			dataCounters = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_counters.txt", false));
		} catch (FileNotFoundException e) {
			Utils.logErrorln("Couldn't init print stream of data_counters.txt");
			Utils.log(e);
		}
		dataCounters.print("Number of vehicles per minute passing through counters ---\n");
		dataCounters.print("Counter1A Counter1B Counter2A Counter2B EntranceBLeft EntranceBRight EntranceELeft EntranceERight EntranceESum\n");
	}
	private static void initDataMeanTimeSpent() {
		try {
			dataMeanTimeSpent_transit = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_mean_time_transit.txt", false));
		} catch (FileNotFoundException e) {
			Utils.logErrorln("Couldn't init print stream of data_mean_time_transit.txt");
			Utils.log(e);
		}
		dataMeanTimeSpent_transit.print("Checking mean time spent on network in seconds (per hour)\n");
		dataMeanTimeSpent_transit.print("mean-time mean | mean-time standard dev | distance mean | distance standard dev\n");
		
		try {
			dataMeanTimeSpent_cern = new PrintStream(new FileOutputStream(dataDirSim + "/" + "data_mean_time_cern.txt", false));
		} catch (FileNotFoundException e) {
			Utils.logErrorln("Couldn't init print stream of data_mean_time_cern.txt");
			Utils.log(e);
		}
		dataMeanTimeSpent_cern.print("Checking mean time spent on network in seconds (per hour)\n");
		dataMeanTimeSpent_transit.print("mean-time mean | mean-time standard dev | distance mean | distance standard dev\n");
	}
	public static void logErrorln(String text) {
		logln("  ERROR    : " + text);
	}
	public static void logError(String text) {
		log("  ERROR    : " + text);
	}
	public static void logWarningln(String text) {
		logln("  WARNING  : " + text);
	}
	public static void logWarning(String text) {
		log("  WARNING  : " + text);
	}
	public static void logInfoln(String text) {
		logln("  INFO     : " + text);
	}
	public static void logInfo(String text) {
		log("  INFO     : " + text);
	}
	public static void logTabln(String text) {
		logln("             " + text);
	}
	public static void logTab(String text) {
		log("             " + text);
	}
	public static void logln(String text) {
		log.print(text + "\n");
		System.out.println(text);
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
	public static void writeDataCountersAll(String text) {
		dataStrCounters = dataStrCounters + text;
	}
	public static void saveDataCountersAll() {
		dataCounters.print(dataStrCounters);
		dataStrCounters = "";
	}
	public static void writeDataLeakyBucketsAll(String text) {
		dataStrLeakyBuckets = dataStrLeakyBuckets + text;
	}
	public static void saveDataLeakyBucketsAll() {
		dataLeakyBuckets.print(dataStrLeakyBuckets);
		dataStrLeakyBuckets = "";
	}
	public static void writeDataMeanTimeSpentAllTransit(String text) {
		dataStrMeanTimeSpent_transit = dataStrMeanTimeSpent_transit + text;
	}
	public static void writeDataMeanTimeSpentAllCERN(String text) {
		dataStrMeanTimeSpent_cern = dataStrMeanTimeSpent_cern + text;
	}
	public static void saveDataMeanTimeSpentAll() {
		dataMeanTimeSpent_transit.print(dataStrMeanTimeSpent_transit);
		dataStrMeanTimeSpent_transit = "";
		dataMeanTimeSpent_cern.print(dataStrMeanTimeSpent_cern);
		dataStrMeanTimeSpent_cern = "";
	}
	public static ArrayList<String> loadFileOutsideJarAsString(String path) {
		Utils.logInfo("Loading " + path + " ... ");
		
		ArrayList<String> text = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String line;
			while ((line = br.readLine()) != null) {
				text.add(line);
			}
			br.close();
			Utils.logln("success");
		} catch (IOException e) {
			Utils.logln("failed");
			Utils.logErrorln("Couldn't read " + path + " and save it into a string");
			Utils.log(e);
		}
		return text;
	}
	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			Utils.logErrorln("Couldn't parse integer in Utils.parseInt(), string was \"" + number + "\"");
			Utils.log(e);
			return 0;
		}
	}
}
