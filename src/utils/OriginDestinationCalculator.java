package utils;

public class OriginDestinationCalculator {
	private double[][] entries;					// k x n
	private double[][] exits;					// k x m
	private boolean[][] links;					// n x m
	public double[][] betas;					// k x n 
	public double[][][] P;						// n x m x k
	
	OriginDestinationCalculator(double[][] I, double[][] O, boolean[][] L) {
		entries=I;
		exits=O;
		links=L;
		betas=new double[exits.length][exits[0].length];
		for (int i=0; i<betas.length; ++i) {
			for (int j=0; j<betas[0].length; ++j) {
				betas[i][j]=1.0;
			}
		}
		P = new double[entries[0].length][exits[0].length][exits.length];
		computeCoeff();

	}
	
	private void computeCoeff() {
		for (int k=0; k<P[0][0].length; ++k) {
			for (int i=0; i<P.length; ++i) {
				double betaSum=0;
				for (int j=0; j<P[i].length; ++j) {
					if (links[i][j]) {
						betaSum += betas[k][j];
					}
				}
				for (int j=0; j<P[i].length; ++j) {
					if (links[i][j]) {
						P[i][j][k]=betas[k][j]/betaSum;
					} else {
						P[i][j][k]=0;
					}
				}
			}
		}
	}
	
	private void nextBetaValues() {
		for (int k=0; k<betas.length; ++k) {
			double[] gamma = new double[entries[0].length];
			for (int i=0; i<entries[0].length; ++i) {
				gamma[i]=0;
				for (int j=0; j<exits[0].length; ++j) {
					if (links[i][j]) {
						gamma[i] += betas[k][j];
					}
				}
			}
			for (int j=0; j<exits[0].length; ++j) {
				double s=0;
				for (int i=0; i<entries[0].length; ++i) {
					if (links[i][j]) {
						s += entries[k][i]/gamma[i];
					}
				}
				betas[k][j] = exits[k][j]/s;
			}
		}
	}
	
	public void computation() {
		for (int i=0; i<10; ++i) {
			nextBetaValues();
		}
		computeCoeff();
	}
	
	public void println(int k) {
		for (int i=0; i<P.length; ++i) {
			for (int j=0; j<P[i].length; ++j) {
				System.out.print(Math.round(10000*P[i][j][k])/10000.0 + "\t");
			}
			System.out.println("");
		}
	}
	
	public static void main(String[] args) {
		double[][] entrees = {{30, 100}, {40, 60}};
		double[][] sorties = {{40, 50, 50}, {30, 30, 40}};
		boolean[][] liens = {{false, true, true},{true, true, true}};
		
		System.out.println(entrees.length + " " + entrees[0].length);
		System.out.println(sorties.length + " " + sorties[0].length);
		System.out.println(liens.length + " " + liens[0].length);
		
		OriginDestinationCalculator ODC = new OriginDestinationCalculator(entrees, sorties, liens);
		ODC.println(0);
		ODC.computation();
		System.out.println("");
		ODC.println(0);
		System.out.println("");
		ODC.println(1);
		System.out.println("");

		double[][] Matrice_N = 
			{
				{	77,		10,		191,	0,		0,		29,		94,		1,		0	},
				{	43,		10,		93,		0,		0,		25,		35,		1,		0	},
				{	28,		10,		65,		0,		0,		13,		28,		1,		0	},
				{	52,		20,		56,		0,		0,		6,		29,		1,		0	},
				{	209,	20,		56,		0,		0,		8,		45,		1,		0	},
				{	558,	100,	133,	0,		0,		10,		177,	1,		0	},
				{	1315,	150,	349,	0,		0,		19,		293,	1,		0	},
				{	1828,	671,	717,	240,	29,		75,		500,	43,		0	},
				{	1651,	623,	829,	330,	111,	144,	500,	103,	0	},
				{	1191,	463,	628,	254,	118,	174,	500,	73,		0	},
				{	1074,	150,	312,	0,		0,		156,	519,	75,		0	},
				{	1006,	150,	370,	0,		0,		258,	302,	162,	0	},
				{	963,	150,	435,	0,		0,		306,	200,	283,	0	},
				{	1216,	150,	357,	0,		0,		214,	300,	145,	0	},
				{	1011,	150,	423,	0,		0,		178,	500,	102,	0	},
				{	1001,	150,	470,	0,		0,		198,	606,	112,	0	},
				{	1000,	150,	642,	0,		0,		243,	860,	150,	0	},
				{	1109,	115,	471,	187,	76,		239,	1200,	210,	500	},
				{	1078,	218,	691,	163,	1,		204,	1000,	203,	600	},
				{	999,	199,	762,	150,	0,		292,	800,	4,		400	},
				{	609,	200,	342,	0,		0,		119,	307,	18,		0	},
				{	298,	200,	242,	0,		0,		139,	172,	4,		0	},
				{	260,	200,	216,	0,		0,		105,	174,	1,		0	},
				{	192,	200,	189,	0,		0,		196,	48,		1,		0	}
			};

		double[][] Matrice_M = 
			{
				{	264,	14,		53,		0+0,		0,		0,		12,		58,		1	},
				{	126,	14,		29,		0+0,		0,		0,		7,		30,		1	},
				{	87,		13,		19,		0+0,		0,		0,		2,		23,		1	},
				{	68,		24,		31,		0+0,		0,		0,		4,		37,		0	},
				{	88,		21,		110,	0+0,		0,		0,		8,		139,	0	},
				{	147,	111,	309,	0+0,		0,		0,		47,		363,	2	},
				{	365,	157,	688,	0+0,		0,		0,		149,	800,	0	},
				{	704,	142,	583,	332+331,	170,	27,		226,	1200,	111	},
				{	686,	225,	712,	386+386,	278,	114,	354,	1100,	263	},
				{	758,	178,	683,	143+144,	252,	99,		390,	900,	180	},
				{	914,	147,	525,	0+0,		0,		0,		295,	500,	106	},
				{	1026,	139,	465,	0+0,		0,		0,		239,	286,	94	},
				{	1212,	139,	448,	0+0,		0,		0,		256,	216,	124	},
				{	981,	138,	557,	0+0,		0,		0,		453,	64,		226	},
				{	1177,	139,	469,	0+0,		0,		0,		293,	213,	118	},
				{	1311,	140,	466,	0+0,		0,		0,		183,	373,	65	},
				{	1810,	141,	470,	0+0,		0,		0,		154,	409,	62	},
				{	1807,	553,	591,	0+0,		251,	73,		116,	500,	72	},
				{	1644,	585,	779,	0+0,		204,	1,		115,	550,	40	},
				{	1304,	487,	661,	0+0,		163,	0,		120,	600,	4	},
				{	684,	200,	305,	0+0,		0,		0,		50,		344,	12	},
				{	495,	205,	152,	0+0,		0,		0,		31,		169,	3	},
				{	441,	205,	152,	0+0,		0,		0,		23,		154,	0	},
				{	389,	206,	99,		0+0,		0,		0,		12,		118,	2	}
			};
		
		for (int i=0; i<Matrice_M.length; ++i) {
			for (int j=0; j<Matrice_M[i].length; ++j) {
				if (Matrice_M[i][j]==0) {
					Matrice_M[i][j]=1;
				} else {
					Matrice_M[i][j] *= 1000000;
				}
			}
		}
		
		for (int i=0; i<Matrice_N.length; ++i) {
			for (int j=0; j<Matrice_N[i].length; ++j) {
				if (Matrice_N[i][j]==0) {
					Matrice_N[i][j]=1;
				} else {
					Matrice_N[i][j] *= 1000000;
				}
			}
		}
		
		
		boolean[][] liensMN = 
			{		//	G 		H		I		J		K		D2		L1		L3		L4
					{	false,	true,	true,	true,	true,	false,	true,	true,	true	},	// A
					{	true, 	false, 	true,	true,	true,	false,	true, 	true,	true	},	// B
					{	true,	true,	false,	true,	true,	false,	true,	true,	true	},	// C
					{	true, 	true,	true,	true,	false,	true,	true,	true,	true	},	// D
					{	false,	false,	false,	false,	true,	false,	false,	false,	false	}, 	// K1
					{	true,	true,	true,	false,	true, 	false,	false,	true,	false	},	// E1
					{	true, 	true,	true,	false,	true,	false,	true,	false,	true	},	// E3
					{	true,	true,	true,	false,	true,	false,	false,	true,	false	},	// E4
					{	true, 	true,	true,	false,	true,	false,	false,	false,	false	}	// F
			};
		
		OriginDestinationCalculator ODC2 = new OriginDestinationCalculator(Matrice_N, Matrice_M, liensMN);
		ODC2.println(0);
		ODC2.computation();
		System.out.println("");
		for (int i=0; i<24; ++i) {
			ODC2.println(i);
			System.out.println("");
		}
	}
}
