package utils;

public class Defaults {

	private static boolean drawWire = false; // true for rendering the border of the cells
	private static boolean drawColors = false; // true for rendering color codes (end of road, out cells, ...)
	private static boolean drawRoadID = false; // true for rendering roads ID
	private static boolean drawNames = false; // true for rendering names of road
	private static boolean drawCenters = false; // true for rendering centers (x,y position)
	private static boolean drawVehicleColor = false; // true for rendering rides color on vehicles
	
	private static int sizeOfNetwork = 2;
	private static int numberOfSimulations = 100;
	private static double globalFlowMultiplier = 1.00;
	
	// Switchers
	public static void switchDrawWire() {
		drawWire = !drawWire;
	}
	public static void switchDrawColors() {
		drawColors = !drawColors;
	}
	public static void switchDrawRoadID() {
		drawRoadID = !drawRoadID;
	}
	public static void switchDrawCenters() {
		drawCenters = !drawCenters;
	}
	public static void switchDrawNames() {
		drawNames = !drawNames;
	}
	public static void switchDrawRides() {
		drawVehicleColor = !drawVehicleColor;
	}
	// Getters
	public static boolean getDrawVehicleColor() {
		return drawVehicleColor;
	}
	public static boolean getDrawWire() {
		return drawWire;
	}
	public static boolean getDrawColors() {
		return drawColors;
	}
	public static boolean getDrawRoadID() {
		return drawRoadID;
	}
	public static boolean getDrawNames() {
		return drawNames;
	}
	public static boolean getDrawCenters() {
		return drawCenters;
	}
	public static int getSizeOfNetwork() {
		return sizeOfNetwork;
	}
	public static int getNumberOfSimulations() {
		return numberOfSimulations;
	}
	public static double getGlobalFlowMultiplier() {
		return globalFlowMultiplier;
	}
	// Setters
	public static void setGlobalFlowMultiplier(double newMultiplier) {
		globalFlowMultiplier = newMultiplier;
	}
}
