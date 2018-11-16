package data;

import java.util.ArrayList;

import elements.Ride;
import elements.Road;
import main.Simulation;
import network.AllNetworkRides;
import network.Network;
import states.SimSettingsState;
import utils.Utils;

public class DataManager {
	
	public static boolean useProbabilities = true;
	
	// ============================================================================================
	// Numerical values ===========================================================================
	// ============================================================================================
	
	private static float value = 0;
	private static double repartition1 = 0;
	private static double repartition2 = 0;
	private static double repartitionRHMorning = 0;
	private static double repartitionRHEvening = 0;
	private static double repartitionRH7 = 0;
	private static double repartitionRH8 = 0;
	private static double repartitionRH9 = 0;
	private static double repartitionRH17 = 0;
	private static double repartitionRH18 = 0;
	private static double repartitionRH19 = 0;
	private static int numberOfSameRide = 0;
	private static double repartitionFrToFr = 0;
	private static boolean useFrToFr = false;
	
	// Theoritical default values -------------------------
	
	public static int nFrGe = 12500;
	public static int nFrGe_fromSW = 88;
	public static int nFrGe_fromNW = 1;
	public static int nFrGe_fromNE = 2;
	public static int nFrGe_fromTun = 9;
	public static int nFrGe_7 = 30;
	public static int nFrGe_8 = 40;
	public static int nFrGe_9 = 30;
	
	public static int nGeFr = 12500;
	public static int nGeFr_toSW = 88;
	public static int nGeFr_toNW = 1;
	public static int nGeFr_toNE = 2;
	public static int nGeFr_toTun = 9;
	public static int nGeFr_17 = 30;
	public static int nGeFr_18 = 40;
	public static int nGeFr_19 = 30;
	
	public static int nFrFr = 6750;
	
	public static int nToE = 1605;
	public static int nToE_fromSW = 30;
	public static int nToE_fromNW = 65;
	public static int nToE_fromNE = 5;
	public static int nToE_7 = 33;
	public static int nToE_8 = 47;
	public static int nToE_9 = 20;
	
	public static int nFromE = 1500;
	public static int nFromE_toSW = 30;
	public static int nFromE_toNW = 65;
	public static int nFromE_toNE = 5;
	public static int nFromE_17 = 33;
	public static int nFromE_18 = 40;
	public static int nFromE_19 = 27;
	
	public static int nToA = 554;
	public static int nToA_fromFr = 30;
	public static int nToA_fromGe = 70;
	public static int nToA_7 = 20;
	public static int nToA_8 = 47;
	public static int nToA_9 = 33;
	
	public static int nFromA = 116;
	public static int nFromA_toFr = 30;
	public static int nFromA_toGe = 70;
	public static int nFromA_17 = 62;
	public static int nFromA_18 = 34;
	public static int nFromA_19 = 4;
	
	public static int nToB = 970;
	public static int nToB_fromFr = 70;
	public static int nToB_fromGe = 30;
	public static int nToB_7 = 23;
	public static int nToB_8 = 47;
	public static int nToB_9 = 40;
	
	public static int nFromB = 351;
	public static int nFromB_toFr = 70;
	public static int nFromB_toGe = 30;
	public static int nFromB_17 = 33;
	public static int nFromB_18 = 32;
	public static int nFromB_19 = 35;
	
	// Theoritical chosen values --------------------------
	public static int nFrGeChosen = 0;
	public static int nGeFrChosen = 0;
	public static int nToEChosen = 0;
	public static int nFromEChosen = 0;
	public static int nToAChosen = 0;
	public static int nFromAChosen = 0;
	public static int nToBChosen = 0;
	public static int nFromBChosen = 0;
	
	// Empirical values -----------------------------------
	public static int nFrGeEmpiric = 0;
	public static int nGeFrEmpiric = 0;
	public static int nToEEmpiric = 0;
	public static int nFromEEmpiric = 0;
	public static int nToAEmpiric = 0;
	public static int nFromAEmpiric = 0;
	public static int nToBEmpiric = 0;
	public static int nFromBEmpiric = 0;
	
	// ============================================================================================
	// Statistical values =========================================================================
	// ============================================================================================
	
	public static double[][][] probas = new double[][][]{
		
		{{0.000, 0.741, 0.827, 0.724, 0.740, 0.812, 0.740, 0.832},
		{0.080, 0.000, 0.025, 0.022, 0.023, 0.025, 0.023, 0.025},
		{0.452, 0.127, 0.000, 0.125, 0.127, 0.140, 0.127, 0.143},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.071, 0.020, 0.022, 0.020, 0.000, 0.022, 0.000, 0.000},
		{0.391, 0.110, 0.123, 0.108, 0.110, 0.000, 0.110, 0.000},
		{0.006, 0.002, 0.002, 0.002, 0.000, 0.002, 0.000, 0.000}},
		
		{{0.000, 0.711, 0.788, 0.678, 0.698, 0.761, 0.698, 0.786},
		{0.143, 0.000, 0.053, 0.046, 0.047, 0.052, 0.047, 0.053},
		{0.433, 0.146, 0.000, 0.139, 0.143, 0.156, 0.143, 0.161},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.075, 0.025, 0.028, 0.024, 0.000, 0.027, 0.000, 0.000},
		{0.338, 0.114, 0.126, 0.109, 0.112, 0.000, 0.112, 0.000},
		{0.011, 0.004, 0.004, 0.003, 0.000, 0.004, 0.000, 0.000}},

		{{0.000, 0.703, 0.762, 0.658, 0.668, 0.753, 0.668, 0.766},
		{0.187, 0.000, 0.074, 0.064, 0.065, 0.073, 0.065, 0.074},
		{0.400, 0.146, 0.000, 0.137, 0.139, 0.157, 0.139, 0.159},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.029, 0.011, 0.012, 0.010, 0.000, 0.011, 0.000, 0.000},
		{0.369, 0.135, 0.146, 0.126, 0.128, 0.000, 0.128, 0.000},
		{0.015, 0.005, 0.006, 0.005, 0.000, 0.006, 0.000, 0.000}},

		{{0.000, 0.573, 0.622, 0.510, 0.519, 0.623, 0.519, 0.636},
		{0.226, 0.000, 0.135, 0.111, 0.113, 0.135, 0.113, 0.138},
		{0.370, 0.204, 0.000, 0.181, 0.184, 0.221, 0.184, 0.226},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.035, 0.019, 0.021, 0.017, 0.000, 0.021, 0.000, 0.000},
		{0.370, 0.204, 0.221, 0.181, 0.185, 0.000, 0.185, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000}},

		{{0.000, 0.456, 0.575, 0.439, 0.445, 0.603, 0.445, 0.615},
		{0.068, 0.000, 0.050, 0.038, 0.039, 0.052, 0.039, 0.053},
		{0.422, 0.246, 0.000, 0.237, 0.240, 0.325, 0.240, 0.332},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.025, 0.015, 0.019, 0.014, 0.000, 0.019, 0.000, 0.000},
		{0.485, 0.283, 0.356, 0.272, 0.276, 0.000, 0.276, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000}},

		{{0.000, 0.282, 0.351, 0.256, 0.266, 0.391, 0.266, 0.414},
		{0.123, 0.000, 0.125, 0.091, 0.095, 0.139, 0.095, 0.148},
		{0.364, 0.298, 0.000, 0.271, 0.281, 0.413, 0.281, 0.438},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.048, 0.039, 0.049, 0.036, 0.000, 0.055, 0.000, 0.000},
		{0.463, 0.379, 0.472, 0.344, 0.358, 0.000, 0.358, 0.000},
		{0.002, 0.002, 0.002, 0.002, 0.000, 0.002, 0.000, 0.000}},

		{{0.000, 0.362, 0.466, 0.342, 0.360, 0.481, 0.360, 0.517},
		{0.082, 0.000, 0.074, 0.054, 0.057, 0.076, 0.057, 0.082},
		{0.404, 0.281, 0.000, 0.266, 0.279, 0.374, 0.279, 0.401},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.074, 0.052, 0.067, 0.049, 0.000, 0.069, 0.000, 0.000},
		{0.440, 0.306, 0.394, 0.289, 0.304, 0.000, 0.304, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000}},

		{{0.000, 0.327, 0.368, 0.325, 0.405, 0.531, 0.405, 0.604},
		{0.049, 0.000, 0.039, 0.035, 0.043, 0.057, 0.043, 0.065},
		{0.210, 0.148, 0.000, 0.148, 0.184, 0.242, 0.184, 0.274},
		{0.219, 0.155, 0.175, 0.155, 0.000, 0.000, 0.000, 0.000},
		{0.044, 0.031, 0.035, 0.000, 0.039, 0.051, 0.039, 0.058},
		{0.070, 0.049, 0.056, 0.049, 0.000, 0.080, 0.000, 0.000},
		{0.374, 0.265, 0.299, 0.264, 0.329, 0.000, 0.329, 0.000},
		{0.034, 0.024, 0.027, 0.024, 0.000, 0.039, 0.000, 0.000}},

		{{0.000, 0.299, 0.349, 0.294, 0.418, 0.401, 0.418, 0.501},
		{0.076, 0.000, 0.068, 0.057, 0.081, 0.078, 0.081, 0.097},
		{0.265, 0.201, 0.000, 0.197, 0.281, 0.270, 0.281, 0.337},
		{0.255, 0.193, 0.226, 0.190, 0.000, 0.000, 0.000, 0.000},
		{0.051, 0.039, 0.045, 0.000, 0.054, 0.052, 0.054, 0.065},
		{0.112, 0.085, 0.099, 0.083, 0.000, 0.114, 0.000, 0.000},
		{0.157, 0.119, 0.139, 0.117, 0.166, 0.000, 0.166, 0.000},
		{0.083, 0.063, 0.074, 0.062, 0.000, 0.085, 0.000, 0.000}},

		{{0.000, 0.430, 0.525, 0.423, 0.539, 0.463, 0.539, 0.567},
		{0.089, 0.000, 0.068, 0.055, 0.070, 0.060, 0.070, 0.073},
		{0.377, 0.235, 0.000, 0.231, 0.295, 0.253, 0.295, 0.310},
		{0.140, 0.087, 0.107, 0.086, 0.000, 0.000, 0.000, 0.000},
		{0.061, 0.038, 0.046, 0.000, 0.048, 0.041, 0.048, 0.050},
		{0.186, 0.116, 0.142, 0.114, 0.000, 0.125, 0.000, 0.000},
		{0.062, 0.039, 0.047, 0.038, 0.049, 0.000, 0.049, 0.000},
		{0.086, 0.054, 0.065, 0.053, 0.000, 0.058, 0.000, 0.000}},

		{{0.000, 0.714, 0.784, 0.691, 0.758, 0.744, 0.758, 0.822},
		{0.103, 0.000, 0.036, 0.032, 0.035, 0.034, 0.035, 0.038},
		{0.383, 0.122, 0.000, 0.118, 0.130, 0.127, 0.130, 0.140},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.210, 0.067, 0.074, 0.065, 0.000, 0.070, 0.000, 0.000},
		{0.229, 0.073, 0.080, 0.071, 0.077, 0.000, 0.077, 0.000},
		{0.076, 0.024, 0.026, 0.023, 0.000, 0.025, 0.000, 0.000}},

		{{0.000, 0.790, 0.844, 0.770, 0.822, 0.813, 0.822, 0.872},
		{0.109, 0.000, 0.028, 0.025, 0.027, 0.027, 0.027, 0.028},
		{0.383, 0.090, 0.000, 0.088, 0.094, 0.093, 0.094, 0.100},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.198, 0.047, 0.050, 0.046, 0.000, 0.048, 0.000, 0.000},
		{0.232, 0.055, 0.058, 0.053, 0.057, 0.000, 0.057, 0.000},
		{0.078, 0.018, 0.020, 0.018, 0.000, 0.019, 0.000, 0.000}},

		{{0.000, 0.810, 0.859, 0.791, 0.850, 0.821, 0.850, 0.885},
		{0.112, 0.000, 0.025, 0.023, 0.025, 0.024, 0.025, 0.026},
		{0.380, 0.081, 0.000, 0.080, 0.085, 0.083, 0.085, 0.089},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.224, 0.048, 0.051, 0.047, 0.000, 0.049, 0.000, 0.000},
		{0.177, 0.038, 0.040, 0.037, 0.040, 0.000, 0.040, 0.000},
		{0.108, 0.023, 0.025, 0.023, 0.000, 0.024, 0.000, 0.000}},

		{{0.000, 0.771, 0.834, 0.754, 0.854, 0.762, 0.854, 0.864},
		{0.092, 0.000, 0.025, 0.023, 0.026, 0.023, 0.026, 0.026},
		{0.389, 0.098, 0.000, 0.096, 0.108, 0.097, 0.108, 0.110},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.317, 0.080, 0.086, 0.078, 0.000, 0.079, 0.000, 0.000},
		{0.044, 0.011, 0.012, 0.011, 0.012, 0.000, 0.012, 0.000},
		{0.158, 0.040, 0.043, 0.039, 0.000, 0.039, 0.000, 0.000}},

		{{0.000, 0.811, 0.861, 0.792, 0.850, 0.823, 0.850, 0.885},
		{0.108, 0.000, 0.024, 0.023, 0.024, 0.023, 0.024, 0.025},
		{0.385, 0.082, 0.000, 0.080, 0.086, 0.083, 0.086, 0.089},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.234, 0.050, 0.053, 0.049, 0.000, 0.050, 0.000, 0.000},
		{0.178, 0.038, 0.040, 0.037, 0.040, 0.000, 0.040, 0.000},
		{0.094, 0.020, 0.021, 0.020, 0.000, 0.020, 0.000, 0.000}},

		{{0.000, 0.818, 0.866, 0.800, 0.833, 0.854, 0.833, 0.891},
		{0.108, 0.000, 0.023, 0.022, 0.023, 0.023, 0.023, 0.024},
		{0.382, 0.078, 0.000, 0.076, 0.079, 0.081, 0.079, 0.085},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.145, 0.030, 0.031, 0.029, 0.000, 0.031, 0.000, 0.000},
		{0.313, 0.064, 0.068, 0.062, 0.065, 0.000, 0.065, 0.000},
		{0.052, 0.011, 0.011, 0.010, 0.000, 0.011, 0.000, 0.000}},

		{{0.000, 0.852, 0.893, 0.837, 0.861, 0.887, 0.861, 0.913},
		{0.107, 0.000, 0.019, 0.017, 0.018, 0.018, 0.018, 0.019},
		{0.382, 0.063, 0.000, 0.062, 0.064, 0.066, 0.064, 0.068},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.121, 0.020, 0.021, 0.020, 0.000, 0.021, 0.000, 0.000},
		{0.342, 0.057, 0.059, 0.056, 0.057, 0.000, 0.057, 0.000},
		{0.049, 0.008, 0.008, 0.008, 0.000, 0.008, 0.000, 0.000}},

		{{0.000, 0.687, 0.699, 0.640, 0.646, 0.691, 0.646, 0.722},
		{0.255, 0.000, 0.109, 0.100, 0.101, 0.108, 0.101, 0.113},
		{0.294, 0.123, 0.000, 0.115, 0.116, 0.124, 0.116, 0.130},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.081, 0.034, 0.034, 0.000, 0.032, 0.034, 0.032, 0.036},
		{0.064, 0.027, 0.027, 0.025, 0.000, 0.027, 0.000, 0.000},
		{0.266, 0.112, 0.114, 0.104, 0.105, 0.000, 0.105, 0.000},
		{0.040, 0.017, 0.017, 0.016, 0.000, 0.017, 0.000, 0.000}},

		{{0.000, 0.573, 0.612, 0.528, 0.527, 0.587, 0.527, 0.613},
		{0.229, 0.000, 0.136, 0.117, 0.117, 0.130, 0.117, 0.136},
		{0.345, 0.191, 0.000, 0.176, 0.176, 0.196, 0.176, 0.204},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.078, 0.044, 0.046, 0.000, 0.040, 0.045, 0.040, 0.047},
		{0.055, 0.030, 0.032, 0.028, 0.000, 0.031, 0.000, 0.000},
		{0.274, 0.152, 0.162, 0.140, 0.140, 0.000, 0.140, 0.000},
		{0.019, 0.011, 0.011, 0.010, 0.000, 0.011, 0.000, 0.000}},

		{{0.000, 0.539, 0.584, 0.498, 0.496, 0.573, 0.496, 0.596},
		{0.212, 0.000, 0.134, 0.114, 0.114, 0.132, 0.114, 0.137},
		{0.342, 0.200, 0.000, 0.185, 0.184, 0.213, 0.184, 0.221},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.070, 0.041, 0.044, 0.000, 0.038, 0.043, 0.038, 0.045},
		{0.060, 0.035, 0.038, 0.032, 0.000, 0.037, 0.000, 0.000},
		{0.313, 0.183, 0.198, 0.169, 0.168, 0.000, 0.168, 0.000},
		{0.002, 0.001, 0.001, 0.001, 0.000, 0.001, 0.000, 0.000}},

		{{0.000, 0.669, 0.709, 0.615, 0.630, 0.721, 0.630, 0.742},
		{0.210, 0.000, 0.093, 0.081, 0.083, 0.095, 0.083, 0.098},
		{0.344, 0.144, 0.000, 0.133, 0.136, 0.156, 0.136, 0.160},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.051, 0.021, 0.023, 0.020, 0.000, 0.023, 0.000, 0.000},
		{0.383, 0.160, 0.170, 0.147, 0.151, 0.000, 0.151, 0.000},
		{0.012, 0.005, 0.005, 0.005, 0.000, 0.006, 0.000, 0.000}},

		{{0.000, 0.686, 0.657, 0.580, 0.594, 0.662, 0.594, 0.680},
		{0.368, 0.000, 0.175, 0.155, 0.158, 0.176, 0.158, 0.181},
		{0.281, 0.140, 0.000, 0.118, 0.121, 0.135, 0.121, 0.138},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.052, 0.026, 0.025, 0.022, 0.000, 0.025, 0.000, 0.000},
		{0.294, 0.146, 0.140, 0.124, 0.127, 0.000, 0.127, 0.000},
		{0.005, 0.003, 0.002, 0.002, 0.000, 0.002, 0.000, 0.000}},

		{{0.000, 0.675, 0.627, 0.553, 0.564, 0.637, 0.564, 0.650},
		{0.404, 0.000, 0.205, 0.181, 0.184, 0.208, 0.184, 0.212},
		{0.263, 0.143, 0.000, 0.117, 0.119, 0.135, 0.119, 0.138},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.041, 0.022, 0.020, 0.018, 0.000, 0.021, 0.000, 0.000},
		{0.293, 0.159, 0.148, 0.131, 0.133, 0.000, 0.133, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000}},

		{{0.000, 0.699, 0.599, 0.535, 0.543, 0.601, 0.543, 0.611},
		{0.505, 0.000, 0.262, 0.234, 0.238, 0.263, 0.238, 0.268},
		{0.230, 0.139, 0.000, 0.107, 0.108, 0.120, 0.108, 0.122},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000, 0.000},
		{0.027, 0.017, 0.014, 0.013, 0.000, 0.014, 0.000, 0.000},
		{0.234, 0.142, 0.122, 0.109, 0.110, 0.000, 0.110, 0.000},
		{0.005, 0.003, 0.002, 0.002, 0.000, 0.002, 0.000, 0.000}}
		
	};
	
	public static int[][] flowPerExit = new int[][] {
		{77, 10, 191, 0, 29, 94, 1, 0, 264, 14, 53, 0, 0, 12, 58, 1},
		{43, 10, 93, 0, 25, 35, 1, 0, 126, 14, 29, 0, 0, 7, 30, 1},
		{28, 10, 65, 0, 13, 28, 1, 0, 87, 13, 19, 0, 0, 2, 23, 1},
		{52, 20, 56, 0, 6, 29, 1, 0, 68, 24, 31, 0, 0, 4, 37, 0},
		{209, 20, 83, 0, 8, 45, 1, 0, 88, 21, 110, 0, 0, 8, 139, 0},
		{558, 100, 133, 0, 10, 177, 1, 0, 147, 111, 309, 0, 0, 47, 363, 2},
		{1315, 150, 349, 0, 19, 293, 1, 0, 365, 157, 688, 0, 0, 149, 768, 0},
		{1828, 671, 717, 213, 75, 195, 43, 0, 704, 142, 583, 663, 141, 226, 1172, 111},
		{1651, 623, 829, 216, 144, 128, 103, 0, 686, 225, 712, 772, 167, 354, 516, 263},
		{1191, 463, 628, 155, 174, 66, 73, 0, 758, 178, 683, 287, 134, 390, 140, 180},
		{1074, 150, 312, 0, 156, 519, 75, 0, 914, 147, 525, 0, 0, 295, 299, 106},
		{1006, 150, 370, 0, 258, 302, 162, 0, 1026, 139, 465, 0, 0, 239, 286, 94},
		{963, 150, 435, 0, 306, 258, 283, 0, 1212, 139, 448, 0, 0, 256, 216, 124},
		{1216, 150, 357, 0, 214, 337, 145, 0, 981, 138, 557, 0, 0, 453, 64, 226},
		{1011, 150, 423, 0, 178, 545, 102, 0, 1177, 139, 469, 0, 0, 293, 213, 118},
		{1001, 150, 470, 0, 198, 606, 112, 0, 1311, 140, 466, 0, 0, 183, 373, 65},
		{1000, 150, 642, 0, 243, 860, 150, 0, 1810, 141, 470, 0, 0, 154, 409, 62},
		{1109, 115, 471, 114, 239, 977, 210, 500, 1807, 553, 591, 0, 175, 116, 421, 72},
		{1078, 218, 691, 162, 204, 730, 203, 600, 1644, 585, 779, 0, 203, 115, 520, 40},
		{999, 199, 762, 150, 292, 509, 4, 400, 1304, 487, 661, 0, 163, 120, 576, 4},
		{609, 200, 342, 0, 119, 307, 18, 0, 684, 200, 305, 0, 0, 50, 344, 12},
		{298, 200, 242, 0, 139, 172, 4, 0, 495, 205, 152, 0, 0, 31, 169, 3},
		{260, 200, 216, 0, 105, 174, 1, 0, 441, 205, 133, 0, 0, 23, 154, 0},
		{192, 200, 189, 0, 196, 48, 1, 0, 389, 206, 99, 0, 0, 12, 118, 2}
	};
	
	public static int[] flowPerExitEmpiric = new int[16];
	
	public static double globalFlowMultiplier = 1.14;
	
	// ============================================================================================
	// Light system ===============================================================================
	// ============================================================================================
	
	public static int cycle1LTSmin = 0;
	public static int cycle1LTSmax = 15;
	public static int cycle2LTSmin = 0;
	public static int cycle2LTSmax = 35;
	public static int cycle3LTSmin = 0;
	public static int cycle3LTSmax = 15;
	public static int cycle4LTSmin = 0;
	public static int cycle4LTSmax = 15;
	
	public static int randomValue = 0;
	
	// ============================================================================================
	// Time spent on network ======================================================================
	// ============================================================================================
	
	public static ArrayList<Integer> timeSpent = new ArrayList<Integer>();
	public static double meanTime = 0;
	
	public static boolean lastRoadIs(Ride r, String roadName) {
		if (r.getNextConnections().size()>0) {
			if (r.getNextConnections().get(r.getNextConnections().size()-1).getName().equals(roadName)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public static void applyDataProba(Simulation simulation) {
		
		System.out.println("Trying to apply ...");
		
		if (useProbabilities) {
			
			Utils.log("applying data (proba) to Network ... ");
			applyDataToRidesProba(simulation);
			applyDataToRoadsProba(simulation);
			Utils.log("done\n");
			
		}
	}
	public static void applyDataToRidesProba(Simulation simulation) {
		
		Network n = simulation.getSimState().getNetwork();
		
		for (AllNetworkRides anr: n.getAllNetworkRides()) {
			for (Ride r: anr.getNetworkRides()) {
				r.setNumberOfSameRide(numberOfSameRides(simulation, r.getRoadName(), r.getNextConnections().get(r.getNextConnections().size()-1).getName()));
			}
		}
		
		/*System.out.println("Printing all rides -----");
		for (AllNetworkRides anr: n.getAllNetworkRides()) {
			for (Ride ride: anr.getNetworkRides()) {
				System.out.println(ride);
			}
		}
		System.out.println("-----");*/
		// From A ===========================================================================================
		for (Ride r: n.getAllRides("rD884NE").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 0, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 0, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 0, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rD884CERN")) {saveFlowIntoRide(r, 0, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 0, 4);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 0, 5);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 0, 6);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 0, 7);}
			
			else {r.setFlow(0);}
			
		}
		// From B ===========================================================================================
		for (Ride r: n.getAllRides("rRueDeGeneveSE").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 1, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 1, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 1, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 1, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 1, 4);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 1, 5);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 1, 6);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 1, 7);}
			
			else {r.setFlow(0);}
			
		}
		// From C ===========================================================================================
		for (Ride r: n.getAllRides("rRueGermaineTillionSW").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 2, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 2, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 2, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 2, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 2, 4);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 2, 5);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 2, 6);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 2, 7);}
			
			else {r.setFlow(0);}
		}
		// From D ===========================================================================================
		for (Ride r: n.getAllRides("rC5SW").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 3, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 3, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 3, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 3, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 3, 4);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 3, 5);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 3, 6);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 3, 7);}
			
			else {r.setFlow(0);}
		}
		// From E1 (left) ===================================================================================
		for (Ride r: n.getAllRides("rRoutePauliSouthNELeft").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 4, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 4, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 4, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 4, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 4, 4);}
			
			else {r.setFlow(0);}
		}
		// From E1 (right) ==================================================================================
		for (Ride r: n.getAllRides("rRoutePauliSouthNERight").getNetworkRides()) {
			// To L1 -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 4, 5);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 4, 6);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 4, 7);}
			
			else {r.setFlow(0);}
		}
		// From E3 ===========================================================================================
		for (Ride r: n.getAllRides("rRouteDeMeyrinSouthNW").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 5, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 5, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 5, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 5, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 5, 4);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 5, 5);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 5, 6);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 5, 7);}
			
			else {r.setFlow(0);}
		}
		// From E4 ===========================================================================================
		for (Ride r: n.getAllRides("rRouteBellNE").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 6, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 6, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 6, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 6, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 6, 4);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 6, 5);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 6, 6);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 6, 7);}
			
			else {r.setFlow(0);}
		}
		// From F ===========================================================================================
		for (Ride r: n.getAllRides("rSortieCERNNW").getNetworkRides()) {
			// To G -----------------------------------------------------------------------------------------
			if (lastRoadIs(r, "rD884SW")) {saveFlowIntoRide(r, 7, 0);}
			// To H -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueDeGeneveNW")) {saveFlowIntoRide(r, 7, 1);}
			// To I -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRueGermaineTillionNE")) {saveFlowIntoRide(r, 7, 2);}
			// To J -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rSortieCERNSE")) {saveFlowIntoRide(r, 7, 3);}
			// To K -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rC5NE")) {saveFlowIntoRide(r, 7, 4);}
			// To L1 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRoutePauliSouthSW")) {saveFlowIntoRide(r, 7, 5);}
			// To L3 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {saveFlowIntoRide(r, 7, 6);}
			// To L4 -----------------------------------------------------------------------------------------
			else if (lastRoadIs(r, "rRouteBellSW")) {saveFlowIntoRide(r, 7, 7);}
			
			else {r.setFlow(0);}
		}
		
	}
	// in : entering road index in probas ; out : exiting road index in probas
	public static void saveFlowIntoRide(Ride r, int in, int out) {		
		for (int i=0; i<24; i++) {
			r.setFlow(i, i+1, (float) (globalFlowMultiplier*probas[i][out][in])*flowPerExit[i][in] / (float) (r.getNumberOfSameRide()));
		}
	}
	public static void saveFlowIntoRoad(Road road, int index, int specialCase) {
		for (int i=0; i<24; i++) {
			//road.setGenerateVehicules(i, i+1, flowPerExit[i][index]);
			// All roads
			if (specialCase == 0) {
				road.setGenerateVehicules(i, i+1, (float) (globalFlowMultiplier*flowPerExit[i][index]));
			}
			// RoutePauliSouthNELeft
			else if (specialCase == 1) {
				road.setGenerateVehicules(i, i+1, (float) (globalFlowMultiplier*(probas[i][0][3]+probas[i][1][3]+probas[i][2][3]+probas[i][3][3]+probas[i][4][3]) * flowPerExit[i][index]));
			}
			// RoutePauliSouthNERight
			else if (specialCase == 2) {
				road.setGenerateVehicules(i, i+1, (float) (globalFlowMultiplier*(probas[i][5][3]+probas[i][6][3]+probas[i][7][3]) * flowPerExit[i][index]));
			}
		}
	}
	public static void applyDataToRoadsProba(Simulation simulation) {
		
		System.out.println("Applying ...");
		
		Network n = simulation.getSimState().getNetwork();
		for (Road road: n.getRoads()) {
			if (n.getAllRides(road.getName()) != null) {
				if (road.getName().equals("rD884NE")) {saveFlowIntoRoad(road, 0, 0);}
				else if (road.getName().equals("rRueDeGeneveSE")) {saveFlowIntoRoad(road, 1, 0);}
				else if (road.getName().equals("rRueGermaineTillionSW")) {saveFlowIntoRoad(road, 2, 0);}
				else if (road.getName().equals("rC5SW")) {saveFlowIntoRoad(road, 3, 0);}
				else if (road.getName().equals("rRoutePauliSouthNELeft")) {saveFlowIntoRoad(road, 4, 1);}
				else if (road.getName().equals("rRoutePauliSouthNERight")) {saveFlowIntoRoad(road, 4, 2);}
				else if (road.getName().equals("rRouteDeMeyrinSouthNW")) {saveFlowIntoRoad(road, 5, 0);}
				else if (road.getName().equals("rRouteBellNE")) {saveFlowIntoRoad(road, 6, 0);}
				else if (road.getName().equals("rSortieCERNNW")) {saveFlowIntoRoad(road, 7, 0);}
				
				else {
					road.setGenerateVehicules(0);
				}
			}
		}
		
		/*for (Ride ride: n.getAllRides("rD884NE").getNetworkRides()) {
			System.out.print(ride.getNextConnections().get(ride.getNextConnections().size()-1).getName() + " ");
			System.out.println(ride.getFlow());
		}*/
		
		if (n.getN() == 1) {
			n.getTrafficLightsSystems().get(0).getPhases().get(0).setMin(simulation.getSimSettingsState().crEntreeB_phase1().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(0).setMax(simulation.getSimSettingsState().crEntreeB_phase1().getCurrentValue2());
			
			n.getTrafficLightsSystems().get(0).getPhases().get(1).setMin(simulation.getSimSettingsState().crEntreeB_phase2().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(1).setMax(simulation.getSimSettingsState().crEntreeB_phase2().getCurrentValue2());
			
			n.getTrafficLightsSystems().get(0).getPhases().get(2).setMin(simulation.getSimSettingsState().crEntreeB_phase3().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(2).setMax(simulation.getSimSettingsState().crEntreeB_phase3().getCurrentValue2());
			
			n.getTrafficLightsSystems().get(0).getPhases().get(3).setMin(simulation.getSimSettingsState().crEntreeB_phase4().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(3).setMax(simulation.getSimSettingsState().crEntreeB_phase4().getCurrentValue2());
		}
	}
	public static void applyDataNumerical(Simulation simulation) {
		
		if (!useProbabilities) {
			nFrGeChosen = simulation.getSimSettingsState().fromFrToGe().getCurrentValue();
			nGeFrChosen = simulation.getSimSettingsState().fromGeToFr().getCurrentValue();
			nToEChosen = simulation.getSimSettingsState().toEntranceE().getCurrentValue();
			nFromEChosen = simulation.getSimSettingsState().fromEntranceE().getCurrentValue();
			nToAChosen = simulation.getSimSettingsState().toEntranceA().getCurrentValue();
			nFromAChosen = simulation.getSimSettingsState().fromEntranceA().getCurrentValue();
			nToBChosen = simulation.getSimSettingsState().toEntranceB().getCurrentValue();
			nFromBChosen = simulation.getSimSettingsState().fromEntranceB().getCurrentValue();
			
			if (!simulation.getSimState().getNetwork().isRandomGeneration()) {
				//Utils.log("applying data to Network ... ");
				applyDataToRidesNumerical(simulation);
				applyRidesToRoads(simulation);
				//Utils.log("done\n");
			} else {
				//Utils.log("applying random gen to Network ... done\n");
			}	
		}
	}
	public static void applyFlowFromVariablesNumerical(Simulation sim, Ride r) {
		
		/*if (r.getNextConnections().size() > 0) {
			numberOfSameRide = numberOfSameRides(sim, r.getRoadName(), r.getNextConnections().get(r.getNextConnections().size()-1).getName());
		} else {
			numberOfSameRide = 1;
		}*/
//		int tmp = randomValue;
//		if (!useFrToFr) {
//			randomValue = 0;
//		}
		
		if (!useProbabilities) {
			numberOfSameRide = r.getNumberOfSameRide();
			
			r.setFlow((float) ((value)*(repartition2-repartition1)*(1-repartitionRHMorning-repartitionRHEvening)/(21.0*numberOfSameRide)));
			
			if (repartitionRH7 > 0 && repartitionRH8 > 0 && repartitionRH9 > 0) {
				r.setFlow(7, 8, (float) ((value)*(repartition2-repartition1)*repartitionRHMorning*repartitionRH7/((float) numberOfSameRide)));
				r.setFlow(8, 9, (float) ((value)*(repartition2-repartition1)*repartitionRHMorning*repartitionRH8/((float) numberOfSameRide)));
				r.setFlow(9, 10, (float) ((value)*(repartition2-repartition1)*repartitionRHMorning*repartitionRH9/((float) numberOfSameRide)));
			} else {
				if (repartitionRHMorning > 0) {
					r.setFlow(7, 10, (float) ((value-randomValue)*(repartition2-repartition1)*repartitionRHMorning/((float) numberOfSameRide*3.0)));
				}
			}
			
			if (repartitionRH17 > 0 && repartitionRH18 > 0 && repartitionRH19 > 0) {
				r.setFlow(17, 18, (float) ((value)*(repartition2-repartition1)*repartitionRHEvening*repartitionRH17/((float) numberOfSameRide)));
				r.setFlow(18, 19, (float) ((value)*(repartition2-repartition1)*repartitionRHEvening*repartitionRH18/((float) numberOfSameRide)));
				r.setFlow(19, 20, (float) ((value)*(repartition2-repartition1)*repartitionRHEvening*repartitionRH19/((float) numberOfSameRide)));
			} else {
				if (repartitionRHEvening > 0) {
					r.setFlow(17, 20, (float) ((value)*(repartition2-repartition1)*repartitionRHEvening/((float) numberOfSameRide*3.0)));
				}
			}
		}
		
		//randomValue = tmp;
	}
	public static void applyDataToRidesNumerical(Simulation simulation) {
		
		Network n = simulation.getSimState().getNetwork();
		SimSettingsState settings = simulation.getSimSettingsState();
		
		for (AllNetworkRides anr: n.getAllNetworkRides()) {
			for (Ride r: anr.getNetworkRides()) {
				r.setNumberOfSameRide(numberOfSameRides(simulation, r.getRoadName(), r.getNextConnections().get(r.getNextConnections().size()-1).getName()));
			}
		}
		
		// France to Geneva =================================================================================
		
		for (Ride r: n.getAllRides("rD884NE").getNetworkRides()) {
			resetValues();
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				//repartitionFrToFr = settings.fromFrToFr().getCurrentValue()/100.0;
				//System.out.print(settings.fromFrToGe().getCurrentValue() + " - ");
				value = (int) (settings.fromFrToGe().getCurrentValue());
				//value = (int) (settings.fromFrToGe().getCurrentValue()*(1-repartitionFrToFr));
				//System.out.println(value);
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue1() / (100.0);
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
				//randomValue = (int) (settings.fromFrToGe().getCurrentValue()*(repartitionFrToFr));
			} else if (lastRoadIs(r, "rD884CERN")) {
				value = settings.toEntranceE().getCurrentValue();
				repartition2 = settings.toEntranceERepartition().getCurrentValue1() / (100.0);
				repartitionRH7 = settings.toEntranceERepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceERepartitionRH().getCurrentValue2() - settings.toEntranceERepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100-settings.toEntranceERepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRHMorning = 1;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = settings.toEntranceA().getCurrentValue();
				repartition2 = (100 - settings.toEntranceARepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = settings.toEntranceB().getCurrentValue();
				repartition2 = (100 - settings.toEntranceBRepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRueDeGeneveNW")) {
				value = settings.fromFrToFr().getCurrentValue()/4;
				repartition2 = 1;
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRueGermaineTillionNE")) {
				value = settings.fromFrToFr().getCurrentValue()/4;
				repartition2 = 1;
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			}/* else  if (lastRoadIs(r, "rRueGermaineTillionNE")) {
				value = (int) (randomValue/2.0);
				repartition2 = 1;
				useFrToFr = true;
			}*/
			applyFlowFromVariablesNumerical(simulation, r);
		}
		
		for (Ride r: n.getAllRides("rRueDeGeneveSE").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = settings.fromFrToGe().getCurrentValue();
				repartition1 = settings.fromFrToGeRepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue2() / (100.0);
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			} else if (lastRoadIs(r, "rSortieCERNSE")) {
				value = settings.toEntranceE().getCurrentValue();
				repartition1 = settings.toEntranceERepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.toEntranceERepartition().getCurrentValue2() / (100.0);
				repartitionRH7 = settings.toEntranceERepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceERepartitionRH().getCurrentValue2() - settings.toEntranceERepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100-settings.toEntranceERepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRHMorning = 1;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = settings.toEntranceA().getCurrentValue();
				repartition2 = (100 - settings.toEntranceARepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = settings.toEntranceB().getCurrentValue();
				repartition2 = (100 - settings.toEntranceBRepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rD884SW")) {
				value = settings.fromFrToFr().getCurrentValue()/4;
				repartition2 = 1;
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			}
			applyFlowFromVariablesNumerical(simulation, r);
		}
		
		for (Ride r: n.getAllRides("rRueGermaineTillionSW").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = settings.fromFrToGe().getCurrentValue();
				repartition1 = settings.fromFrToGeRepartition().getCurrentValue2() / (100.0);
				repartition2 = settings.fromFrToGeRepartition().getCurrentValue3() / (100.0);
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			} else if (lastRoadIs(r, "rSortieCERNSE")) {
				value = settings.toEntranceE().getCurrentValue();
				repartition1 = settings.toEntranceERepartition().getCurrentValue2() / (100.0);
				repartition2 = 1;
				repartitionRH7 = settings.toEntranceERepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceERepartitionRH().getCurrentValue2() - settings.toEntranceERepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100-settings.toEntranceERepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRHMorning = 1;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = settings.toEntranceA().getCurrentValue();
				repartition2 = (100 - settings.toEntranceARepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = settings.toEntranceB().getCurrentValue();
				repartition2 = (100 - settings.toEntranceBRepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rD884SW")) {
				value = settings.fromFrToFr().getCurrentValue()/4;
				repartition2 = 1;
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			}
			applyFlowFromVariablesNumerical(simulation, r);
		}
		for (Ride r: n.getAllRides("rC5SW").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = settings.fromFrToGe().getCurrentValue();
				repartition1 = settings.fromFrToGeRepartition().getCurrentValue3() / (100.0);
				repartition2 = 1;
				repartitionRHMorning = settings.fromFrToGeDuringRH().getCurrentValue() / 100.0;
				repartitionRH7 = settings.fromFrToGeRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.fromFrToGeRepartitionRH().getCurrentValue2() - settings.fromFrToGeRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.fromFrToGeRepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = settings.toEntranceA().getCurrentValue();
				repartition2 = (100 - settings.toEntranceARepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
			} else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = settings.toEntranceB().getCurrentValue();
				repartition2 = (100 - settings.toEntranceBRepartition().getCurrentValue()) / (100.0*4);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
			}
			applyFlowFromVariablesNumerical(simulation, r);
		}
		
		// Geneva to France =================================================================================
		
		for (Ride r: n.getAllRides("rRouteDeMeyrinSouthNW").getNetworkRides()) {
			
			resetValues();
			
			repartitionRH17 = settings.fromGeToFrRepartitionRH2().getCurrentValue1() / 100.0;
			repartitionRH18 = (settings.fromGeToFrRepartitionRH2().getCurrentValue2() - settings.fromGeToFrRepartitionRH2().getCurrentValue1()) / 100.0;
			repartitionRH19 = (100 - settings.fromGeToFrRepartitionRH2().getCurrentValue2()) / 100.0;
			
			if (lastRoadIs(r, "rD884SW")) {
				value = settings.fromGeToFr().getCurrentValue();
				repartition1 = 0;
				repartition2 = settings.fromGeToFrRepartition().getCurrentValue1() / (100.0);
				repartitionRHEvening = settings.fromGeToFrDuringRH2().getCurrentValue() / 100.0;
			} else if (lastRoadIs(r, "rRueDeGeneveNW")) {
				value = settings.fromGeToFr().getCurrentValue();
				repartition1 = settings.fromGeToFrRepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.fromGeToFrRepartition().getCurrentValue2() / (100.0);
				repartitionRHEvening = settings.fromGeToFrDuringRH2().getCurrentValue() / 100.0;
			} else if (lastRoadIs(r, "rRueGermaineTillionNE")) {
				value = settings.fromGeToFr().getCurrentValue();
				repartition1 = settings.fromGeToFrRepartition().getCurrentValue2() / (100.0);
				repartition2 = settings.fromGeToFrRepartition().getCurrentValue3() / (100.0);
				repartitionRHEvening = settings.fromGeToFrDuringRH2().getCurrentValue() / 100.0;
			} else if (lastRoadIs(r, "rC5NE")) {
				value = settings.fromGeToFr().getCurrentValue();
				repartition1 = settings.fromGeToFrRepartition().getCurrentValue3() / (100.0);
				repartition2 = 1;
				repartitionRHEvening = settings.fromGeToFrDuringRH2().getCurrentValue() / 100.0;
			} else  if (lastRoadIs(r, "rRouteBellSW")) {
				value = settings.toEntranceA().getCurrentValue();
				repartition2 = (settings.toEntranceARepartition().getCurrentValue()) / (100.0);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceARepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceARepartitionRH().getCurrentValue2() - settings.toEntranceARepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceARepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRH17 = 0;
				repartitionRH18 = 0;
				repartitionRH19 = 0;
			}  else  if (lastRoadIs(r, "rRoutePauliSouthSW")) {
				value = settings.toEntranceB().getCurrentValue();
				repartition2 = (settings.toEntranceBRepartition().getCurrentValue()) / (100.0);
				repartitionRHMorning = 1;
				repartitionRH7 = settings.toEntranceBRepartitionRH().getCurrentValue1() / 100.0;
				repartitionRH8 = (settings.toEntranceBRepartitionRH().getCurrentValue2() - settings.toEntranceBRepartitionRH().getCurrentValue1()) / 100.0;
				repartitionRH9 = (100 - settings.toEntranceBRepartitionRH().getCurrentValue2()) / 100.0;
				repartitionRH17 = 0;
				repartitionRH18 = 0;
				repartitionRH19 = 0;
			}
			applyFlowFromVariablesNumerical(simulation, r);
		}
		
		// From entrance E to France ========================================================================
		
		for (Ride r: n.getAllRides("rSortieCERNNW").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rD884SW")) {
				value = settings.fromEntranceE().getCurrentValue();
				repartition1 = 0;
				repartition2 = settings.fromEntranceERepartition().getCurrentValue1() / (100.0);
			} else if (lastRoadIs(r, "rRueDeGeneveNW")) {
				value = settings.fromEntranceE().getCurrentValue();
				repartition1 = settings.fromEntranceERepartition().getCurrentValue1() / (100.0);
				repartition2 = settings.fromEntranceERepartition().getCurrentValue2() / (100.0);
			} else if (lastRoadIs(r, "rRueGermaineTillionNE")) {
				value = settings.fromEntranceE().getCurrentValue();
				repartition1 = settings.fromEntranceERepartition().getCurrentValue2() / (100.0);
				repartition2 = 1;
			}
			repartitionRH17 = settings.fromEntranceERepartitionRH2().getCurrentValue1() / 100.0;
			repartitionRH18 = (settings.fromEntranceERepartitionRH2().getCurrentValue2() - settings.fromEntranceERepartitionRH2().getCurrentValue1()) / 100.0;
			repartitionRH19 = (100 - settings.fromEntranceERepartitionRH2().getCurrentValue2()) / 100.0;
			repartitionRHEvening = 1;
			applyFlowFromVariablesNumerical(simulation, r);
		}
		
		// From entrance A ==================================================================================
		
		for (Ride r: n.getAllRides("rRouteBellNE").getNetworkRides()) {
			
			resetValues();
			
			if (lastRoadIs(r, "rD884SW") || lastRoadIs(r, "rRueDeGeneveNW") || lastRoadIs(r, "rRueGermaineTillionNE") || lastRoadIs(r, "rC5NE")) {
				value = settings.fromEntranceA().getCurrentValue();
				repartition2 = (100-settings.fromEntranceARepartition().getCurrentValue()) / (100.0*4);
			} else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
				value = settings.fromEntranceA().getCurrentValue();
				repartition2 = (settings.fromEntranceARepartition().getCurrentValue()) / (100.0);
			}
			repartitionRH17 = settings.fromEntranceARepartitionRH2().getCurrentValue1() / 100.0;
			repartitionRH18 = (settings.fromEntranceARepartitionRH2().getCurrentValue2() - settings.fromEntranceARepartitionRH2().getCurrentValue1()) / 100.0;
			repartitionRH19 = (100 - settings.fromEntranceARepartitionRH2().getCurrentValue2()) / 100.0;
			repartitionRHEvening = 1;
			applyFlowFromVariablesNumerical(simulation, r);
		}
		
		// From entrance B ==================================================================================
		
		ArrayList<AllNetworkRides> anrEntreeB = new ArrayList<AllNetworkRides>();
		
		// if one road out of entrance B
		if (n.getAllRides("rRoutePauliSouthNE") != null) {
			anrEntreeB.add(n.getAllRides("rRoutePauliSouthNE"));
		} else {
			if (n.getAllRides("rRoutePauliSouthNELeft") != null) {
				anrEntreeB.add(n.getAllRides("rRoutePauliSouthNELeft"));
			}
			if (n.getAllRides("rRoutePauliSouthNERight") != null) {
				anrEntreeB.add(n.getAllRides("rRoutePauliSouthNERight"));
			}
			
		}
		for (AllNetworkRides anr: anrEntreeB) {
			for (Ride r: anr.getNetworkRides()) {
				
				resetValues();
				
				if (lastRoadIs(r, "rD884SW") || lastRoadIs(r, "rRueDeGeneveNW") || lastRoadIs(r, "rRueGermaineTillionNE") || lastRoadIs(r, "rC5NE")) {
					value = settings.fromEntranceB().getCurrentValue();
					repartition2 = (100-settings.fromEntranceBRepartition().getCurrentValue()) / (100.0*4);
				} else if (lastRoadIs(r, "rRouteDeMeyrinSouthSE")) {
					value = settings.fromEntranceB().getCurrentValue();
					repartition2 = (settings.fromEntranceBRepartition().getCurrentValue()) / (100.0);
				}
				repartitionRH17 = settings.fromEntranceBRepartitionRH2().getCurrentValue1() / 100.0;
				repartitionRH18 = (settings.fromEntranceBRepartitionRH2().getCurrentValue2() - settings.fromEntranceBRepartitionRH2().getCurrentValue1()) / 100.0;
				repartitionRH19 = (100 - settings.fromEntranceBRepartitionRH2().getCurrentValue2()) / 100.0;
				repartitionRHEvening = 1;
				applyFlowFromVariablesNumerical(simulation, r);
			}
		}
		
		for (Road road: n.getRoads()) {
			if (road.getName().equals("rSortieCERNSE") || road.getName().equals("rD884CERN") || road.getName().equals("rRoutePauliSouthSW") || road.getName().equals("rRouteBellSW")) {
				road.setMaxOutflow(settings.timePerVhcEntrance().getCurrentValue());
			}
		}
		
		/*for (AllNetworkRides anr: n.getAllNetworkRides()) {
			
			for (Ride ride: anr.getNetworkRides()) {
				//if ((ride.getRoadName().equals("rRueDeGeneveSE") || ride.getRoadName().equals("rD884NE") || ride.getRoadName().equals("rRueGermaineTillionSW") || ride.getRoadName().equals("rC5SW")) && lastRoadIs(ride,"rRoutePauliSouthSW")) {
				if ((ride.getRoadName().equals("rD884NE")) && lastRoadIs(ride,"rRoutePauliSouthSW")) {
					ride.print();
					System.out.println();
				}
			}
		}*/
	}
	public static void applyRidesToRoads(Simulation simulation) {
		
		Network n = simulation.getSimState().getNetwork();
		
		for (Road road: n.getRoads()) {
			/*if (n.getAllRides(road.getName()) != null) {
				if (road.getName().equals("rSortieCERNNW")) {//rRueDeGeneveSE//rSortieCERNNW
					System.out.println(road.getName() + " : ");
					for (Ride ride: n.getAllRides(road.getName()).getNetworkRides()) {
						ride.print();
						System.out.println();
						int tmp = 0;
						for (Integer i: ride.getFlow()) {
							System.out.print(i + " ");
							tmp += i;
						}
						System.out.println();
						System.out.println(tmp);
					}
				}
			}*/
			int tmp = 0;
			
			for (int h=0 ; h<24 ; h++) {
				Utils.saveCheckingValues(Integer.toString(h) + " ");
				float sum = 0;
				if (n.getAllRides(road.getName()) != null) {
					
					for (Ride ride: n.getAllRides(road.getName()).getNetworkRides()) {
						//Utils.saveCheckingValues("(" + Integer.toString(ride.getFlow().get(h)) + ") - ");
						sum += ride.getFlow().get(h);
						
					}
				}
				Utils.saveCheckingValues("(" + Float.toString(sum) + ") ");
				road.setGenerateVehicules(h, h+1, sum);
				tmp += sum;
			}
			Utils.saveCheckingValues("\n");
			Utils.saveCheckingValues("Total : " + tmp + "\n");
		}
		/*
		for (AllNetworkRides anr: n.getAllNetworkRides()) {
			for (Ride ride: anr.getNetworkRides()) {
				System.out.print(ride.getRoadName() + " to " + ride.getNextConnections().get(ride.getNextConnections().size()-1).getName() + " : ");
				for (Float flow: ride.getFlow()) {
					System.out.print(flow + " ");
				}
				System.out.println();
			}
		}
		for (Road road: n.getRoads()) {
			System.out.println(road.getName() + " : ");
			for (int i=0; i<road.getFlow().size() ; i++) {
				System.out.print(road.getFlow().get(i) + " ");
			}
			System.out.println();
		}*/
		
		if (n.getN() == 1) {
			n.getTrafficLightsSystems().get(0).getPhases().get(0).setMin(simulation.getSimSettingsState().crEntreeB_phase1().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(0).setMax(simulation.getSimSettingsState().crEntreeB_phase1().getCurrentValue2());
			
			n.getTrafficLightsSystems().get(0).getPhases().get(1).setMin(simulation.getSimSettingsState().crEntreeB_phase2().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(1).setMax(simulation.getSimSettingsState().crEntreeB_phase2().getCurrentValue2());
			
			n.getTrafficLightsSystems().get(0).getPhases().get(2).setMin(simulation.getSimSettingsState().crEntreeB_phase3().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(2).setMax(simulation.getSimSettingsState().crEntreeB_phase3().getCurrentValue2());
			
			n.getTrafficLightsSystems().get(0).getPhases().get(3).setMin(simulation.getSimSettingsState().crEntreeB_phase4().getCurrentValue1());
			n.getTrafficLightsSystems().get(0).getPhases().get(3).setMax(simulation.getSimSettingsState().crEntreeB_phase4().getCurrentValue2());
		}
	}
	public static int numberOfSameRides(Simulation sim, String start, String end) {
		Network n = sim.getSimState().getNetwork();
		int count = 1;
		for (AllNetworkRides anr: n.getAllNetworkRides()) {
			
			for (Ride ride: anr.getNetworkRides()) {
				if (ride.getRoadName().equals(start) && ride.getNextConnections().size()>0) {
					if (ride.getNextConnections().get(ride.getNextConnections().size()-1).getName().equals(end)) {
						count++;
					}
				}
			}
		}
		count--;
		return count;
	}
	public static void resetValues() {
		value = 0;
		repartition1 = 0;
		repartition2 = 0;
		repartitionRHMorning = 0;
		repartitionRHEvening = 0;
		repartitionRH7 = 0;
		repartitionRH8 = 0;
		repartitionRH9 = 0;
		repartitionRH17 = 0;
		repartitionRH18 = 0;
		repartitionRH19 = 0;
		numberOfSameRide = 0;
		useFrToFr = false;
		repartitionFrToFr = 0;
	}
	public static void switchNumProba() {
		useProbabilities = !useProbabilities;
	}
}
