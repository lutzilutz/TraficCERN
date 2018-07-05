package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import elements.Road;
import elements.RoundAbout;

public class Network {

	private Simulation sim;
	private ArrayList<Road> roads = new ArrayList<Road>();
	private ArrayList<RoundAbout> roundAbouts = new ArrayList<RoundAbout>();
	
	private int cellWidth=10, cellHeight=10;
	
	public Network(Simulation sim) {
		this.sim = sim;
		Road r1 = new Road(10);
		r1.setX(200);
		r1.setY(200);
		r1.setDirection(110);
		r1.setGenerateVehicules(true);
		roads.add(r1);
		RoundAbout ra1 = new RoundAbout(30);
		ra1.setX((int) (cellWidth/2 + r1.getX()+(r1.getLength()*cellWidth + ra1.getLength()*cellWidth/(2*Math.PI) )*Math.sin(2*Math.PI*r1.getDirection()/360)));
		ra1.setY((int) (r1.getY()-(r1.getLength()*cellWidth + ra1.getLength()*cellWidth/(2*Math.PI))*Math.cos(2*Math.PI*r1.getDirection()/360) + cellHeight/2));
		ra1.setDirection(180-r1.getDirection());
		roundAbouts.add(ra1);
		Road r2 = new Road(20);
		r2.setX(200);
		r2.setY(250);
		r2.setDirection(290);
		roads.add(r2);
		
		r1.getRoadCells().get(r1.getLength()-1).setNextCell(ra1.getRoadCells().get(0));
		ra1.getRoadCells().get(ra1.getLength()-5).setOutCell(r2.getRoadCells().get(0));
		
	}
	public void display() {
		for (Road r: roads) {
			//r.display();
		}
		//System.out.print("\n");
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
				r.getRoadCells().get(i).setX((int) (r.getX()+i*cellWidth*Math.sin(2*Math.PI*r.getDirection()/360)));
				r.getRoadCells().get(i).setY((int) (r.getY()-i*cellWidth*Math.cos(2*Math.PI*r.getDirection()/360)));
			}
			gg.dispose();
		}
		for (RoundAbout r: roundAbouts) {
			Graphics2D gg = (Graphics2D) g.create();
			
			int radius = (int) ((r.getLength()*cellWidth)/(2*Math.PI));
			int outRadius = radius+cellWidth/2;
			int inRadius = radius-cellWidth/2;
			gg.drawOval(r.getX()-outRadius, r.getY()-outRadius, outRadius*2, outRadius*2);
			gg.drawOval(r.getX()-inRadius, r.getY()-inRadius, inRadius*2, inRadius*2);
			
			for (int i=0 ; i<r.getLength() ; i++) {
				int x1 = (int) (r.getX()-inRadius*Math.sin(2*Math.PI*i/r.getLength() + 2*Math.PI*r.getDirection()/360 + 2*Math.PI/(2*r.getLength())));
				int y1 = (int) (r.getY()-inRadius*Math.cos(2*Math.PI*i/r.getLength() + 2*Math.PI*r.getDirection()/360 + 2*Math.PI/(2*r.getLength())));
				int x2 = (int) (r.getX()-outRadius*Math.sin(2*Math.PI*i/r.getLength() + 2*Math.PI*r.getDirection()/360 + 2*Math.PI/(2*r.getLength())));
				int y2 = (int) (r.getY()-outRadius*Math.cos(2*Math.PI*i/r.getLength() + 2*Math.PI*r.getDirection()/360 + 2*Math.PI/(2*r.getLength())));
				gg.drawLine(x1, y1, x2, y2);
				r.getRoadCells().get(i).setX((int) (r.getX()-radius*Math.sin(2*Math.PI*r.getDirection()/360 + 2*Math.PI*i/r.getLength())));
				r.getRoadCells().get(i).setY((int) (r.getY()-radius*Math.cos(2*Math.PI*r.getDirection()/360 + 2*Math.PI*i/r.getLength())));
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
					g.fillOval(r.getRoadCells().get(i).getX(), r.getRoadCells().get(i).getY(), 10, 10);
				}
			}
		}
		
		for (RoundAbout r: roundAbouts) {
			Random random = new Random();
			for (int i=0 ; i<r.getLength() ; i++) {
				if (r.getRoadCells().get(i).isOccupied()) {
					g.fillOval(r.getRoadCells().get(i).getX()-cellWidth/2, r.getRoadCells().get(i).getY()-cellHeight/2, cellWidth, cellHeight);
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
						
						if (r.getRoadCells().get(i).getNextCell().isOccupied()) {
							
							r.getRoadCells().get(i).setIsOccupiedNext(1);
							
						} else {
							r.getRoadCells().get(i).setIsOccupiedNext(0);
							r.getRoadCells().get(i).getNextCell().setIsOccupiedNext(1);
							
						}
					} else {
						
						if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							r.getRoadCells().get(i).setIsOccupiedNext(0);
						}
					}
				}
				// If Cell is at the end of the road
				else {
					if (r.getRoadCells().get(i).getNextCell() != null) {
						if (r.getRoadCells().get(i).isOccupied()) {
							if (r.getRoadCells().get(i).getNextCell().isOccupied() || r.getRoadCells().get(i).getNextCell().getPreviousCell().isOccupied()) {
								r.getRoadCells().get(i).setIsOccupiedNext(1);
							} else {
								r.getRoadCells().get(i).setIsOccupiedNext(0);
								r.getRoadCells().get(i).getNextCell().setIsOccupiedNext(1);
							}
						}
					}
					// If Cell has not been visited
					if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
						// Cell stay inoccupied
						r.getRoadCells().get(i).setIsOccupiedNext(0);;
					}
				}
			}
			// Random generation
			if (r.getRoadCells().get(0).getIsOccupiedNext() != 1 && r.getGenerateVehicules()) {
				if (Math.random()<0.4) {
					r.getRoadCells().get(0).setIsOccupiedNext(1);
				}
			}
		}
		for (RoundAbout r: roundAbouts) {
			for (int i=0; i < r.getLength(); ++i) {
				
				if (r.getRoadCells().get(i).getOutCell() == null) {
					if (r.getRoadCells().get(i).isOccupied()) {
						
						if (r.getRoadCells().get(i).getNextCell().isOccupied()) {
							
							r.getRoadCells().get(i).setIsOccupiedNext(1);
							
						} else {
							r.getRoadCells().get(i).setIsOccupiedNext(0);
							r.getRoadCells().get(i).getNextCell().setIsOccupiedNext(1);
							
						}
					} else {
						
						if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							r.getRoadCells().get(i).setIsOccupiedNext(0);
						}
					}
				} else {
					if (r.getRoadCells().get(i).isOccupied()) {
						
						if (r.getRoadCells().get(i).getOutCell().isOccupied()) {
							
							r.getRoadCells().get(i).setIsOccupiedNext(1);
							
						} else {
							r.getRoadCells().get(i).setIsOccupiedNext(0);
							r.getRoadCells().get(i).getOutCell().setIsOccupiedNext(1);
							
						}
					} else {
						
						if (r.getRoadCells().get(i).getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							r.getRoadCells().get(i).setIsOccupiedNext(0);
						}
					}
				}
			}
			// Random generation
			/*if (r.getRoadCells().get(0).getIsOccupiedNext() != 1) {
				if (Math.random()<0.1) {
					r.getRoadCells().get(0).setIsOccupiedNext(1);
				}
			}*/
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
