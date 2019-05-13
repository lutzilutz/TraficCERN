package utils;

public class Defaults {

	private static boolean drawWire = false; // true for rendering the border of the cells
	private static boolean drawColors = false; // true for rendering color codes (end of road, out cells, ...)
	private static boolean drawRoadID = false; // true for rendering roads ID
	private static boolean drawNames = false; // true for rendering names of road
	private static boolean drawCenters = false; // true for rendering centers (x,y position)
	private static boolean drawVehicleColor = false; // true for rendering rides color on vehicles
	
	private static int simSpeed = 100000; // 1 - RT ; 20 - > ; 100 - >> ; 2000 - >>> ; 100000 - max
	
	private static int sizeOfNetwork = 2; // visual size of network ; 1 - zoomed out ; 3 - zoomed in
	private static int numberOfSimulations = 1; // number of simulations to compute
	private static double globalFlowMultiplier = 1.00; // multiplier for the global flow ; 1.00 for 100% ; 1.50 for 150% ; ...
	private static int transferScenario = 0; // 0 - no transfer ; 1 - min transfer ; 2 - max transfer
	private static int repartitionETunnel = 50; // percentage of vehicle being transfered to entrance E (100-repartitionETunnel is transfer to tunnel)
	
	private static int controlDuration = 8; // time in seconds to control 1 vehicle at 1 entrance
	private static int[] lightPhaseDuration = {0, 15, 0, 35, 0, 15, 0, 15}; // pairs of min/max light phase duration (4 phases)
	
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
	
	// Getters & setters ====================================================================================
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
	public static int getSimSpeed() {
		return simSpeed;
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
	public static int getTransferScenario() {
		return transferScenario;
	}
	public static int getRepartitionETunnel() {
		return repartitionETunnel;
	}
	public static int getControlDuration() {
		return controlDuration;
	}
	public static int[] getLightPhaseDuration() {
		return lightPhaseDuration;
	}
	public static void setGlobalFlowMultiplier(double newMultiplier) {
		globalFlowMultiplier = newMultiplier;
	}
	public static void setTransferScenario(int newScenario) {
		transferScenario = newScenario;
	}
}
