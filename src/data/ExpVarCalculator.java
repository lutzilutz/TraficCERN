package data;

import java.util.ArrayList;

import utils.Utils;

public class ExpVarCalculator { 
	private ArrayList<Long> values;
	private ArrayList<Long> valuesSquared;
	private ArrayList<Integer> tempValues;
	private int n = 0;
	private int size;
	
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
	
	public void println() {
		for(long xi : values) {
			System.out.print(xi + "\t");
		}
		System.out.println("");
		for(long xi : valuesSquared) {
			System.out.print(xi + "\t");
		}
		System.out.println("");
	}
	public void addTemp(Integer i) {
		if (tempValues.size() < this.size) {
			tempValues.add(i);
		} else {
			Utils.logErrorln("Can't add another number to EVC (size mismatch in addTemp), expected " + size + ", got " + tempValues.size());
		}
	}
	public void saveTemp() {
		add(tempValues);
		tempValues = new ArrayList<Integer>();
	}
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
	public ArrayList<Float> getEsperance() {
		ArrayList<Float> esp = new ArrayList<Float>();
		for (int i=0; i<size; ++i) {
			esp.add((float) (this.values.get(i)/(float) n));
		}
		return esp;
	}
	
	public ArrayList<Float> getVariance() {
		ArrayList<Float> var = new ArrayList<Float>();
		for (int i=0; i<size; ++i) {
			var.add((float) (this.valuesSquared.get(i)/(float) n - Math.pow(this.values.get(i)/(float) n, 2)));
		}
		return var;
	}
	
	public ArrayList<Float> getEcartType() {
		ArrayList<Float> var = getVariance();
		ArrayList<Float> ecartType = new ArrayList<Float>();
		for (int i=0; i<size; ++i) {
			ecartType.add((float) Math.sqrt(var.get(i)));
		}
		return ecartType;
	}
}
