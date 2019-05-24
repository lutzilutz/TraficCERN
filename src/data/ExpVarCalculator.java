package data;

import java.util.ArrayList;

import utils.Utils;

public class ExpVarCalculator { 
	private ArrayList<Long> values; // list of values to be computed
	private ArrayList<Long> valuesSquared; // list of values*values to be computed
	private ArrayList<Integer> tempValues; // temporary values before being added to "values" and "valuesSquared"
	private int n = 0; //
	private int size;
	
	// Constructor
	public ExpVarCalculator(int n) {
		size = n;
		values = new ArrayList<Long>();
		valuesSquared = new ArrayList<Long>();
		tempValues = new ArrayList<Integer>();
		for (int i = 0; i < n; ++i) {
			values.add((long) 0);
			valuesSquared.add((long) 0);
		}
	}
	
	// Add values "tempValue" to "tempValues"
	public void addTemp(Integer tempValue) {
		if (tempValues.size() < this.size) {
			tempValues.add(tempValue);
		} else {
			Utils.logErrorln("Can't add another number to EVC (size mismatch in addTemp), expected " + size + ", got " + tempValues.size());
		}
	}
	
	// Save the actual temporary values
	public void saveTemp() {
		add(tempValues);
		tempValues = new ArrayList<Integer>();
	}
	
	// Add the temporary values to the "values" and "valuesSquared"
	private void add(ArrayList<Integer> A) {
		if (A.size() == this.size) {
			n++;
			for (int i=0; i<this.size; ++i) {
				this.values.set(i, this.values.get(i) + A.get(i));
				this.valuesSquared.set(i, (long) (this.valuesSquared.get(i) + Math.pow(A.get(i), 2)));
			}
			tempValues = new ArrayList<Integer>();
		} else {
			Utils.logErrorln("Wrong size in EVC (expected " + size + ", got " + A.size() + ")");
		}
	}
	
	// Return expected value of "values"
	public ArrayList<Float> getEsperance() {
		ArrayList<Float> esp = new ArrayList<Float>();
		for (int i=0; i<size; ++i) {
			esp.add((float) (this.values.get(i)/(float) n));
		}
		return esp;
	}
	
	// Return variance of "values"
	public ArrayList<Float> getVariance() {
		ArrayList<Float> var = new ArrayList<Float>();
		for (int i=0; i<size; ++i) {
			var.add((float) (this.valuesSquared.get(i)/(float) n - Math.pow(this.values.get(i)/(float) n, 2)));
		}
		return var;
	}
	
	// Return standard deviation of "values"
	public ArrayList<Float> getEcartType() {
		ArrayList<Float> var = getVariance();
		ArrayList<Float> ecartType = new ArrayList<Float>();
		for (int i=0; i<size; ++i) {
			ecartType.add((float) Math.sqrt(var.get(i)));
		}
		return ecartType;
	}
}
