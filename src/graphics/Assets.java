package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utils.Utils;

public class Assets {

	// Dimensions
	public static int buttonW = 90, buttonH = 34; // default simulation buttons dimensions
	public static int menuButtonW = 140, menuButtonH = 30; // default menu buttons dimensions
	private static int smallButtonW = 50; // default small buttons width
	public static int menuXStart = 180, menuYStart = 200;
	public static int buttonXStart = 20, buttonYStart = 30; // x and y starts of the button inside SimState
	public static int buttonSpacing = 10; // spacing between buttons
	public static int sliderWidth = 670, sliderHeight = 30; // default sliders dimensions
	
	// Colors
	public static Color bgCol = new Color(50,50,50); // background of the simulator
	public static Color bgAlphaCol = new Color(50, 50, 50, 180); // same background with transparency
	public static Color textCol = new Color(250,250,250); // default text color
	public static Color idleCol = new Color(170,170,170); // color of inactive elements
	public static Color zoneCERNCol = new Color(0,100,0); // color of the CERN area
	public static Color zoneCERNtextCol = new Color(120,255,120); // color of the text in this area
	
	// Vehicles colors depending on location
	public static Color vhcFranceCol1 = new Color(0,0,150); // color for Thoiry
	public static Color vhcFranceCol2 = new Color(0,100,255); // color for St-Genis
	public static Color vhcFranceCol3 = new Color(0,200,255); // color for Ferney
	public static Color vhcFranceCol4 = new Color(150,150,255); // color for C5 road
	public static Color vhcSuisseCol = new Color(0,200,0); // color for Geneva
	public static Color vhcCERNCol1 = new Color(200,0,0); // color for CERN entrance E
	public static Color vhcCERNCol2 = new Color(255,100,0); // color for CERN entrance B
	public static Color vhcCERNCol3 = new Color(255,200,0); // color for CERN entrance A
	public static Color vhcCERNCol4 = new Color(155,0,255); // color for CERN tunnel
	
	// Default faults from normal to huge
	public static Font normalFont = new Font("Arial", Font.PLAIN, 12);
	public static Font normalBoldFont = new Font("Arial", Font.BOLD, 12);
	public static Font largeFont = new Font("Arial", Font.PLAIN, 20);
	public static Font hugeFont = new Font("Arial", Font.PLAIN, 50);
	
	// Images of all buttons, active and inactive
	public static BufferedImage pauseIdle, pauseActive, playIdle, playActive, restartIdle, restartActive, fastIdle, fastActive, fastFastIdle, fastFastActive, fastFastFastIdle, fastFastFastActive, previousIdle, previousActive, nextIdle, nextActive;
	
	// Text files input data
	public static ArrayList<String> inputDataEntrance;
	public static ArrayList<String> inputDataExit;
	public static ArrayList<String> inputDataReports;
	
	// Initialize all assets (load from files into matrices and images)
	public static void init() {
		
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/resources/img/buttons_spritesheet.png"));
		
		// crops all parts of the sprite sheet into individual images
		pauseIdle = sheet.crop(0, 0, buttonW, buttonH);
		pauseActive = sheet.crop(buttonW*1, 0, buttonW, buttonH);
		playIdle = sheet.crop(buttonW*2, 0, buttonW, buttonH);
		playActive = sheet.crop(buttonW*3, 0, buttonW, buttonH);
		restartIdle = sheet.crop(buttonW*0, buttonH*1, buttonW, buttonH);
		restartActive = sheet.crop(buttonW*1, buttonH*1, buttonW, buttonH);
		fastIdle = sheet.crop(buttonW*2, buttonH*1, buttonW, buttonH);
		fastActive = sheet.crop(buttonW*3, buttonH*1, buttonW, buttonH);
		fastFastIdle = sheet.crop(buttonW*0, buttonH*2, buttonW, buttonH);
		fastFastActive = sheet.crop(buttonW*1, buttonH*2, buttonW, buttonH);
		fastFastFastIdle = sheet.crop(buttonW*2, buttonH*2, buttonW, buttonH);
		fastFastFastActive = sheet.crop(buttonW*3, buttonH*2, buttonW, buttonH);
		
		previousIdle = sheet.crop(smallButtonW*0, buttonH*3, smallButtonW, buttonH);
		previousActive = sheet.crop(smallButtonW*1, buttonH*3, smallButtonW, buttonH);
		nextIdle = sheet.crop(smallButtonW*2, buttonH*3, smallButtonW, buttonH);
		nextActive = sheet.crop(smallButtonW*3, buttonH*3, smallButtonW, buttonH);
		
		// load input data into Strings and check number of lines in each one
		inputDataEntrance = Utils.loadFileOutsideJarAsString("inputData_entrance.txt");
		if (inputDataEntrance.size() != 24) {
			Utils.logWarningln("File inputData_entrance.txt has " + inputDataEntrance.size() + " lines, should have 24 lines (id est 24 hours)");
		}
		inputDataExit = Utils.loadFileOutsideJarAsString("inputData_exit.txt");
		if (inputDataExit.size() != 24) {
			Utils.logWarningln("File inputData_exit.txt has " + inputDataExit.size() + " lines, should have 24 lines (id est 24 hours)");
		}
		inputDataReports = Utils.loadFileOutsideJarAsString("inputData_reports.txt");
		if (inputDataReports.size() != 2) {
			Utils.logWarningln("File inputData_reports.txt has " + inputDataReports.size() + " lines, should have 2 lines (id est for entrances B and A)");
		}
	}
	
}
