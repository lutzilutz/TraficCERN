package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import elements.Road;
import elements.RoundAbout;

public class Network {

	private Simulation sim;
	private ArrayList<Road> roads = new ArrayList<Road>();
	private ArrayList<RoundAbout> roundAbouts = new ArrayList<RoundAbout>();
	
	private int cellWidth=10, cellHeight=10;
	
	public Network(Simulation sim) {
		this.sim = sim;
		Road r1 = new Road(50);
		r1.setX(50);
		r1.setY(50);
		r1.setDirection(90);
		roads.add(r1);
		Road r2 = new Road(20);
		r2.setX(50);
		r2.setY(100);
		r2.setDirection(105);
		roads.add(r2);
		Road r3 = new Road(30);
		r3.setX(50);
		r3.setY(150);
		r3.setDirection(180);
		roads.add(r3);
		Road r4 = new Road(50);
		r4.setX(700);
		r4.setY(450);
		r4.setDirection(315);
		roads.add(r4);
		RoundAbout ra1 = new RoundAbout(20);
		ra1.setX(400);
		ra1.setY(300);
		ra1.setDirection(r1.getDirection()+180);
		roundAbouts.add(ra1);
		RoundAbout ra2 = new RoundAbout(40);
		ra2.setX(400);
		ra2.setY(300);
		ra2.setDirection(r1.getDirection()+180);
		roundAbouts.add(ra2);
	}
	public void display() {
		for (Road r: roads) {
			r.display();
		}
		System.out.print("\n");
		for (RoundAbout r: roundAbouts) {
			r.display();
		}
		System.out.print("\n");
	}
	public void renderBG(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, sim.getWidth(), sim.getHeight());
		
		// Print cells
		g.setColor(Color.white);
		for (Road r: roads) {
			Graphics2D gg = (Graphics2D) g.create();
			gg.rotate((r.getDirection()/360.0)*2*Math.PI- Math.PI/2, r.getX()+cellWidth/2, r.getY()+cellHeight/2);
			for (int i=0 ; i<r.getLength() ; i++) {
				gg.drawRect(r.getX()+i*cellWidth, r.getY(), cellWidth, cellHeight);
			}
			gg.dispose();
		}
		for (RoundAbout r: roundAbouts) {
			Graphics2D gg = (Graphics2D) g.create();
			//gg.rotate((r.getDirection()/360.0)*2*Math.PI- Math.PI/2, r.getX()+cellWidth/2, r.getY()+cellHeight/2);
			//for (int i=0 ; i<r.getLength() ; i++) {
			//	gg.drawOval(r.getX()+i*cellWidth, r.getY(), cellWidth, cellHeight);
			//}
			int radius = (int) ((r.getLength()*cellWidth)/(2*Math.PI));
			int outRadius = radius+cellWidth/2;
			int inRadius = radius-cellWidth/2;
			gg.drawOval(r.getX()-outRadius, r.getY()-outRadius, outRadius*2, outRadius*2);
			gg.drawOval(r.getX()-inRadius, r.getY()-inRadius, inRadius*2, inRadius*2);
			
			for (int i=0 ; i<r.getLength() ; i++) {
				int x1 = (int) (r.getX()+inRadius*Math.sin(2*Math.PI*i/(r.getLength()-1)));
				int y1 = (int) (r.getY()+inRadius*Math.cos(2*Math.PI*i/(r.getLength()-1)));
				int x2 = (int) (r.getX()+outRadius*Math.sin(2*Math.PI*i/(r.getLength()-1)));
				int y2 = (int) (r.getY()+outRadius*Math.cos(2*Math.PI*i/(r.getLength()-1)));
				gg.drawLine(x1, y1, x2, y2);
			}
			gg.dispose();
		}
	}
	public void render(Graphics g) {
		
		// Print cells
		g.setColor(Color.white);
		for (Road r: roads) {
			for (int i=0 ; i<r.getLength() ; i++) {
				if (r.getRoadCells().get(i).isOccupied()) {
					g.fillOval((int) (r.getX()+i*cellWidth*Math.sin(2*Math.PI*r.getDirection()/360)), (int) (r.getY()-i*cellWidth*Math.cos(2*Math.PI*r.getDirection()/360)), 10, 10);
				}
			}
		}
		
		// Print actual step
		g.setColor(Color.white);
		g.drawString(Integer.toString(sim.getStep()), 20, 20);
	}
	// Update Cell of the Road according to the next state
	public void evolve() {
		for (Road r: roads) {
			for (int i=0; i < r.getLength(); ++i) {
				r.getRoadCells().get(i).evolve();
			}
		}
		for (RoundAbout r: roundAbouts) {
			for (int i=0; i < r.getLength(); ++i) {
				r.getRoadCells().get(i).evolve();
			}
		}
	}
	// Compute future state of the Cells of the Road
	public void computeEvolution() {
		for (Road r: roads) {
			for (int i=0; i < r.getLength(); ++i) {
				
				if (i < r.getLength()-1) {
					
					if (r.getRoadCells().get(i).isOccupied()) {
						
						if (r.getRoadCells().get(i+1).isOccupied()) {
							
							r.getRoadCells().get(i).setIsOccupiedNext(1);
							
						} else {
							r.getRoadCells().get(i).setIsOccupiedNext(0);
							r.getRoadCells().get(i+1).setIsOccupiedNext(1);
							
						}
					} else {
						
						if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							r.getRoadCells().get(i).setIsOccupiedNext(0);;
						}
					}
				}
				// If Cell is at the end of the road
				else {
					// If Cell has not been visited
					if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
						// Cell stay inoccupied
						r.getRoadCells().get(i).setIsOccupiedNext(0);;
					}
				}
			}
			// Random generation
			if (r.getRoadCells().get(0).getIsOccupiedNext() != 1) {
				if (Math.random()<0.4) {
					r.getRoadCells().get(0).setIsOccupiedNext(1);
				}
			}
		}
		for (RoundAbout r: roundAbouts) {
			for (int i=0; i < r.getLength(); ++i) {
				
				if (i < r.getLength()-1) {
					
					if (r.getRoadCells().get(i).isOccupied()) {
						
						if (r.getRoadCells().get(i+1).isOccupied()) {
							
							r.getRoadCells().get(i).setIsOccupiedNext(1);
							
						} else {
							r.getRoadCells().get(i).setIsOccupiedNext(0);
							r.getRoadCells().get(i+1).setIsOccupiedNext(1);
							
						}
					} else {
						
						if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							r.getRoadCells().get(i).setIsOccupiedNext(0);;
						}
					}
				}
				// If Cell is at the end of the road
				else {
					// If Cell has not been visited
					if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
						// Cell stay inoccupied
						r.getRoadCells().get(i).setIsOccupiedNext(0);;
					}
				}
			}
		}
	}
	// Compute future state of the Cells of the RoundAbout
	/*void Network::computeEvolutionRoundAbout() {
		for (unsigned int i(0); i < roundAbout->getRoadCells().size(); ++i) {
			// If Cell is not at the end of the road
			if (roundAbout->getRoadCells()[i]->getNextCell() != NULL) {
				// If Cell is occupied
				if (roundAbout->getRoadCells()[i]->getIsOccupied()) {
					// If next Cell is occupied
					if (roundAbout->getRoadCells()[i]->getNextCell()->getIsOccupied()) {
						// Cell stay occupied
						roundAbout->getRoadCells()[i]->setIsOccupiedNext(1);
					}
					// If next Cell is not occupied
					else {
						// Vehicule will move to next Cell
						roundAbout->getRoadCells()[i]->setIsOccupiedNext(0);
						roundAbout->getRoadCells()[i]->getNextCell()->setIsOccupiedNext(1);
					}	
				}
				// If Cell is not occupied
				else {
					// If Cell has not been visited
					if (roundAbout->getRoadCells()[i]->getIsOccupiedNext() == -1) {
						// Cell stay inoccupied
						roundAbout->getRoadCells()[i]->setIsOccupiedNext(0);
					}
				}
			}
			// If Cell is at the end of the road
			else {
				// If Cell has not been visited
				if (roundAbout->getRoadCells()[i]->getIsOccupiedNext() == -1) {
					// Cell stay inoccupied
					roundAbout->getRoadCells()[i]->setIsOccupiedNext(0);
				}
			}
			// Checking all Cells are updated
			if (roundAbout->getRoadCells()[i]->getIsOccupiedNext() == -1) {
				cout << "Update problem at Cell #" << i << endl;
			} else {
				//cout << "Cell #" << i << " updated" << endl;
			}
		}
	}*/
}
