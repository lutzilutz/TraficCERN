package utils;

public class OriginDestinationCalculator {
	private double[][] entries;					// k x n
	private double[][] exits;					// k x m
	private boolean[][] links;					// n x m
	private double[][] betas;					// k x n 
	private double[][][] probas;				// n x m x k
	
	public OriginDestinationCalculator(double[][] I, double[][] O, boolean[][] L) {
		entries=I;
		exits=O;
		links=L;
		betas=new double[exits.length][exits[0].length];
		for (int i=0; i<betas.length; ++i) {
			for (int j=0; j<betas[0].length; ++j) {
				betas[i][j]=1.0;
			}
		}
		probas = new double[entries[0].length][exits[0].length][exits.length];
		computeCoeff();
	}

	private void computeCoeff() {
		for (int k=0; k<probas[0][0].length; ++k) {
			for (int i=0; i<probas.length; ++i) {
				double betaSum=0;
				for (int j=0; j<probas[i].length; ++j) {
					if (links[i][j]) {
						betaSum += betas[k][j];
					}
				}
				for (int j=0; j<probas[i].length; ++j) {
					if (links[i][j]) {
						probas[i][j][k]=betas[k][j]/betaSum;
					} else {
						probas[i][j][k]=0;
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
	
	public void printl(int k) {
		for (int i=0; i<probas.length; ++i) {
			for (int j=0; j<probas[i].length; ++j) {
				System.out.print(Math.round(10000*probas[i][j][k])/10000.0 + "\t");
			}
			System.out.println("");
		}
	}
	
	// Getters & setters ====================================================================================
	public double[][][] getProbas() {
		return probas;
	}
	public void setP(double[][][] p) {
		probas = p;
	}
}
