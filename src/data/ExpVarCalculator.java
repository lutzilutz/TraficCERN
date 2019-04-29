package data;

import java.util.ArrayList;

import utils.Utils;

public class ExpVarCalculator { 
	private ArrayList<Long> X;
	private ArrayList<Long> X2;
	private ArrayList<Integer> tempX;
	private int n = 0;
	private int size;
	
	public ExpVarCalculator(int n) {
		size = n;
		X = new ArrayList<Long>();
		X2 = new ArrayList<Long>();
		tempX = new ArrayList<Integer>();
		for (int i = 0; i < n; ++i) {
			X.add((long) 0);
			X2.add((long) 0);
		}
	}
	
	public void println() {
		for(long xi : X) {
			System.out.print(xi + "\t");
		}
		System.out.println("");
		for(long xi : X2) {
			System.out.print(xi + "\t");
		}
		System.out.println("");
	}
	public void addTemp(Integer i) {
		if (tempX.size() < this.size) {
			tempX.add(i);
			//System.out.println("INF : Add another number to EVC, expected " + tempX.size() + ", got " + this.size + ")");
		} else {
			//System.out.println("ERR : Can't add another number to EVC (size mismatch in addTemp) expected " + size + ", got " + tempX.size() + ")");
		}
	}
	public void saveTemp() {
		//System.out.println(tempX);
		//System.out.print("Saved temp data (size = " + tempX.size() + ")");
		add(tempX);
		tempX = new ArrayList<Integer>();
		//System.out.println(", new size = " + tempX.size());
	}
	public void add(ArrayList<Integer> A) {
		if (A.size() == this.size) {
			//System.out.println("Good size in EVC");
			n++;
			for (int i=0; i<this.size; ++i) {
				this.X.set(i, this.X.get(i) + A.get(i));
				this.X2.set(i, (long) (this.X2.get(i) + Math.pow(A.get(i), 2)));
			}
			tempX = new ArrayList<Integer>();
		} else {
			Utils.log("    ERROR : Wrong size in EVC (expected " + size + ", got " + A.size() + ")\n");
		}
	}
	public ArrayList<Float> getEsperance() {
		ArrayList<Float> esp = new ArrayList<Float>();
		for (int i=0; i<size; ++i) {
			esp.add((float) (this.X.get(i)/(float) n));
		}
		return esp;
	}
	
	public ArrayList<Float> getVariance() {
		ArrayList<Float> var = new ArrayList<Float>();
		for (int i=0; i<size; ++i) {
			var.add((float) (this.X2.get(i)/(float) n - Math.pow(this.X.get(i)/(float) n, 2)));
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

	/*public static void main(String[] args) {
		ExpVarCalculator C = new ExpVarCalculator(24);
		C.println();
		for (int i = 0; i < 10000; ++i) {
			ArrayList<Integer> A = new ArrayList<Integer>();
			for (int hours = 0; hours < 24; ++hours) {
				int val = (int) ((1+0.2*Math.random()-0.1)*100*Math.sqrt(hours+1));
				A.add(val);
			}
			
			C.add(A);
		}
		System.out.println("");
		System.out.println("N = " + C.N);
		System.out.println("");
		C.println();
		System.out.println("");
		System.out.println("Esperance: ");
		for (float esp_i: C.getEsperance()) {
			System.out.print(Math.round(100*esp_i)/100.0 + "\t");
		}
		System.out.println("");
		
		System.out.println("Variance: ");
		for (float var_i: C.getVariance()) {
			System.out.print(Math.round(100*var_i)/100.0 + "\t");
		}
		System.out.println("");
		
		System.out.println("Ecart type: ");
		for (float et_i: C.getEcartType()) {
			System.out.print(Math.round(100*et_i)/100.0 + "\t");
		}
		System.out.println("");
	}*/
}