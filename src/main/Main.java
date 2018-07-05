package main;

public class Main {

	public static void main(String[] args) {
		
		//Display display = new Display("Trafic simulation around CERN",800,600);
		
		Simulation simulation = new Simulation("Trafic simulation around CERN",800,600);
		simulation.start();
		
		Network n1 = new Network(simulation);
		n1.display();
		
		/*for (int i=0 ; i<10 ; i++) {
			n1.computeEvolution();
			n1.evolve();
			n1.display();
		}*/
	}
	
}
