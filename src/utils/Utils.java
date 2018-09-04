package utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;


public class Utils {

	public static PrintStream log;
	public static PrintStream data;
	public static String dataStr = "";
	
	public static void initLog() {
		try {
			log = new PrintStream(new FileOutputStream("log.txt", true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void initData() {
		try {
			data = new PrintStream(new FileOutputStream("data_test.txt", false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		data.print("Time Counter1 Counter2 Counter3 Counter4\n");
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
	public static void writeData(String text) {
		dataStr = dataStr + text;
	}
	public static void saveData() {
		data.print(dataStr);
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
