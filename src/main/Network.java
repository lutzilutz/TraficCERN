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
	
	private int cellWidth=10, cellHeight=cellWidth;
	
	public Network(Simulation sim) {
		this.sim = sim;
		Road r1 = new Road(this, 15);
		r1.setX(100);
		r1.setY(150);
		r1.setDirection(113);
		r1.setGenerateVehicules(true);
		roads.add(r1);
		
		RoundAbout ra1 = new RoundAbout(this, 48);
		ra1.setPositionFrom(r1);
		ra1.setDirection(180+r1.getDirection());
		roundAbouts.add(ra1);
		
		Road r2 = new Road(this, 15);
		r2.setPositionFrom(ra1, ra1.getLength()-12);
		roads.add(r2);
		
		Road r3 = new Road(this, 15);
		r3.setPositionFrom(ra1, ra1.getLength()-1);
		roads.add(r3);
		
		
		r1.connectTo(ra1, 0);
		ra1.connectTo(r2, ra1.getLength()-12);
		ra1.connectTo(r3, ra1.getLength()-1);
		r3.setDirection(r1.getDirection()+180);
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
	// Compute Background (one-time operation)
	public void renderBG(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, sim.getWidth(), sim.getHeight());
		
		// Print cells
		for (Road r: roads) {
			Graphics2D gg = (Graphics2D) g.create();
			gg.rotate((r.getDirection()/360.0)*2*Math.PI- Math.PI/2, r.getX(), r.getY());
			for (int i=0 ; i<r.getLength() ; i++) {
				
				if (r.getRoadCells().get(i).getPreviousCell()==null) { // Start Cell
					gg.setColor(Color.green);
				} else if (r.getRoadCells().get(i).getNextCell()==null) { // End Cell
					gg.setColor(Color.red);
				} else if (r.getRoadCells().get(i).getOutCell()!=null){ // Out Cell
					gg.setColor(Color.blue);
				} else { // Standard Cell
					gg.setColor(Color.white);
				}
				gg.drawRect(r.getX()+i*cellWidth, r.getY() - cellHeight/2, cellWidth, cellHeight);
				r.getRoadCells().get(i).setX((int) (r.getX()-cellWidth/2+(i*cellWidth + cellWidth/2)*Math.sin(2*Math.PI*r.getDirection()/360)));
				r.getRoadCells().get(i).setY((int) (r.getY()-cellHeight/2-(i*cellWidth + cellWidth/2)*Math.cos(2*Math.PI*r.getDirection()/360)));
			}
			gg.dispose();
			// Render ID
			g.setColor(Color.black);
			g.drawString(Integer.toString(r.getId()), r.getX()+10, r.getY()-10);
			// Render x,y position of Road
			/*g.setColor(Color.red);
			g.fillRect(r.getX()-1, r.getY()-5, 2, 10);
			g.fillRect(r.getX()-5, r.getY()-1, 10, 2);*/
		}
		for (RoundAbout r: roundAbouts) {
			Graphics2D gg = (Graphics2D) g.create();
			gg.setColor(Color.white);
			
			double radius = (r.getLength()*cellWidth)/(2*Math.PI);
			double outRadius = radius+cellWidth/2;
			double inRadius = radius-cellWidth/2;
			
			gg.drawOval((int) (r.getX()-outRadius), (int) (r.getY()-outRadius), (int) (outRadius*2), (int) (outRadius*2));
			gg.drawOval((int) (r.getX()-inRadius), (int) (r.getY()-inRadius), (int) (inRadius*2), (int) (inRadius*2));
			
			for (int i=0 ; i<r.getLength() ; i++) {				
				double angle = 2*Math.PI*i/r.getLength() - 2*Math.PI*r.getDirection()/360;
				double delta = 2*Math.PI/(2*r.getLength());
				int x1 = (int) (r.getX()-inRadius*Math.sin(angle + delta));
				int y1 = (int) (r.getY()-inRadius*Math.cos(angle + delta));
				int x2 = (int) (r.getX()-outRadius*Math.sin(angle + delta));
				int y2 = (int) (r.getY()-outRadius*Math.cos(angle + delta));
				gg.drawLine(x1, y1, x2, y2);
				r.getRoadCells().get(i).setX((int) (r.getX()-radius*Math.sin(angle)));
				r.getRoadCells().get(i).setY((int) (r.getY()-radius*Math.cos(angle)));
			}
			// Render ID
			g.setColor(Color.black);
			g.drawString(Integer.toString(r.getId()), r.getX()+10, r.getY()-10);
			
			// Render x,y position of RoundAbout
			/*gg.setColor(Color.red);
			gg.fillRect(r.getX()-1, r.getY()-5, 2, 10);
			gg.fillRect(r.getX()-5, r.getY()-1, 10, 2);*/
			gg.dispose();
		}
	}
	// Render Vehicles according to Cells
	public void render(Graphics g) {
		
		// Print cells
		g.setColor(Color.white);
		for (Road r: roads) {
			for (int i=0 ; i<r.getLength() ; i++) {
				if (r.getRoadCells().get(i).isOccupied()) {
					g.fillOval(r.getRoadCells().get(i).getX(), r.getRoadCells().get(i).getY(), cellWidth, cellHeight);
				}
			}
		}
		
		for (RoundAbout r: roundAbouts) {
			for (int i=0 ; i<r.getLength() ; i++) {
				if (r.getRoadCells().get(i).isOccupied()) {
					g.fillOval(r.getRoadCells().get(i).getX()-cellWidth/2, r.getRoadCells().get(i).getY()-cellHeight/2, cellWidth, cellHeight);
				}
			}
		}
		// Print actual informations (upper-left corner)
		g.setColor(Color.white);
		g.drawString("Step :    " + Integer.toString(sim.getStep()), 15, 20);
		g.drawString("Time :    " + sim.getTime(), 15, 40);
		g.drawString("Speed : " + ((int) (10*3.6*7.5/sim.getStepSize())/10.0) + " km/h", 15, 60);
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
						if ((int)(Math.random()*2)==1) {
							if (r.getRoadCells().get(i).getOutCell().isOccupied()) {
								
								r.getRoadCells().get(i).setIsOccupiedNext(1);
								
							} else {
								r.getRoadCells().get(i).setIsOccupiedNext(0);
								r.getRoadCells().get(i).getOutCell().setIsOccupiedNext(1);
								
							}
						} else {
							if (r.getRoadCells().get(i).getNextCell().isOccupied()) {
								
								r.getRoadCells().get(i).setIsOccupiedNext(1);
								
							} else {
								r.getRoadCells().get(i).setIsOccupiedNext(0);
								r.getRoadCells().get(i).getNextCell().setIsOccupiedNext(1);
								
							}
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
	public int getCellWidth() {
		return cellWidth;
	}
	public void setCellWidth(int cellWidth) {
		this.cellWidth = cellWidth;
	}
	public int getCellHeight() {
		return cellHeight;
	}
	public void setCellHeight(int cellHeight) {
		this.cellHeight = cellHeight;
	}
}
