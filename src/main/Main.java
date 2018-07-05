package main;

public class Main {

	public static void main(String[] args) {
		
		System.out.print("\n");
		
		//Road r1 = new Road(10);
		//r1.display();

		Network n1 = new Network();
		n1.display();
		
		for (int i=0 ; i<10 ; i++) {
			n1.computeEvolution();
			n1.evolve();
			n1.display();
		}
	}

}
