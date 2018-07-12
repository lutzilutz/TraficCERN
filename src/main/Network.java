package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import elements.CrossRoad;
import elements.Road;
import elements.RoundAbout;
import graphics.Assets;

public class Network {

	private Simulation sim;
	private ArrayList<Road> roads = new ArrayList<Road>();
	private ArrayList<RoundAbout> roundAbouts = new ArrayList<RoundAbout>();
	private ArrayList<CrossRoad> crossRoads = new ArrayList<CrossRoad>();
	private int cellWidth=10, cellHeight=cellWidth;
	
	private boolean drawWire = true; // true for rendering the border of the cells
	private boolean drawColors = true; // true for rendering color codes (end of road, out cells, ...)
	private boolean drawRoadID = false; // true for rendering roads ID
	
	public Network(Simulation sim) {
		this.setCellHeight(12);
		this.setCellWidth(12);
		
		this.sim = sim;
		
		/*
		Road r1 = new Road(this, 15);
		r1.setX(100);
		r1.setY(200);
		r1.setDirection(113);
		roads.add(r1);
		
		RoundAbout ra1 = new RoundAbout(this, 48);
		ra1.setPositionFrom(r1);
		ra1.setDirection(180+r1.getDirection());
		roundAbouts.add(ra1);
		
		Road r1Out = new Road(this, 15);
		r1Out.setStartPositionFrom(ra1, ra1.getLength()-1);
		r1Out.setDirection(r1.getDirection()+180);
		roads.add(r1Out);
		
		// N-E road
		Road r2Out = new Road(this, 15);
		r2Out.setStartPositionFrom(ra1, ra1.getLength()-14);
		roads.add(r2Out);
		Road r2In = new Road(this, 15);
		r2In.setEndPositionFrom(ra1, ra1.getLength()-13);
		roads.add(r2In);
		r2Out.setDirection((r2In.getDirection()+180)%360);
		
		// S-E road
		Road r3Out = new Road(this, 15);
		r3Out.setStartPositionFrom(ra1, 24);
		roads.add(r3Out);
		Road r3In = new Road(this, 15);
		r3In.setEndPositionFrom(ra1, 25);
		roads.add(r3In);
		r3Out.setDirection((r3In.getDirection()+180)%360);

		// S-W road
		Road r4Out = new Road(this, 15);
		r4Out.setStartPositionFrom(ra1, 10);
		roads.add(r4Out);
		Road r4In = new Road(this, 15);
		r4In.setEndPositionFrom(ra1, 11);
		roads.add(r4In);
		r4Out.setDirection((r4In.getDirection()+180)%360);

		
		r1.connectTo(ra1, 0);
		r2In.connectTo(ra1, ra1.getLength()-13);
		r3In.connectTo(ra1, 25);
		r4In.connectTo(ra1, 11);
		
		ra1.connectTo(r4Out, 10);
		ra1.connectTo(r3Out, 24);
		ra1.connectTo(r2Out, ra1.getLength()-14);
		ra1.connectTo(r1Out, ra1.getLength()-1);
		
		r1.setGenerateVehicules(true);
		r2In.setGenerateVehicules(true);
		r3In.setGenerateVehicules(true);
		r4In.setGenerateVehicules(true);
		*/
		CrossRoad CR = new CrossRoad(this);
		CR.setX(300);
		CR.setY(300);
		CR.setDirection(110);
		crossRoads.add(CR);

		Road ri1 = new Road(this, 8);
		ri1.setX(250);
		ri1.setY(250);
		ri1.setDirection(135);
		roads.add(ri1);
		
		ri1.connectTo(CR, 1);
		ri1.setPositionInFrom(CR, 1);
		
		Road ri2 = new Road(this, 17);
		roads.add(ri2);
		ri2.connectTo(CR, 2);
		ri2.setPositionInFrom(CR, 2);
		
		Road ri3 = new Road(this, 20);
		roads.add(ri3);
		ri3.connectTo(CR, 3);
		ri3.setPositionInFrom(CR, 3);
		
		Road ri4 = new Road(this, 8);
		roads.add(ri4);
		ri4.connectTo(CR, 4);
		ri4.setPositionInFrom(CR, 4);
		
		//ri1.setGenerateVehicules(true);
		ri2.setGenerateVehicules(true);
		ri3.setGenerateVehicules(true);
		//ri4.setGenerateVehicules(true);
		
		
		Road ro1 = new Road(this, 17);
		roads.add(ro1);
		CR.connectTo(ro1, 1);
		ro1.setPositionOutFrom(CR, 1);
		
		Road ro2 = new Road(this, 20);
		roads.add(ro2);
		CR.connectTo(ro2, 2);
		ro2.setPositionOutFrom(CR, 2);
		
		Road ro3 = new Road(this, 8);
		roads.add(ro3);
		CR.connectTo(ro3, 3);
		ro3.setPositionOutFrom(CR, 3);
		
		Road ro4 = new Road(this, 8);
		roads.add(ro4);
		CR.connectTo(ro4, 4);
		ro4.setPositionOutFrom(CR, 4);
		
		RoundAbout RA1 = new RoundAbout(this, 30);
		RA1.setPositionFrom(ro4);
		RA1.setDirection(180+ro4.getDirection());
		this.roundAbouts.add(RA1);
		
		Road rlol = new Road(this, 10);
		rlol.setEndPositionFrom(RA1, 4);
		this.roads.add(rlol);
		
		RoundAbout RA2 = new RoundAbout(this, 30);
		RA2.setPositionFrom(ro3);
		RA2.setDirection(180+ro3.getDirection());
		this.roundAbouts.add(RA2);
		
		ro4.connectTo(RA1, 0);
		RA1.connectTo(ri1, 29);
		rlol.connectTo(RA1, 4);
		ro3.connectTo(RA2, 0);
		RA2.connectTo(ri4, 29);
		RA2.connectTo(rlol, 25);
		
		Road R = new Road(this, 17);
		R.setStartPositionFrom(RA1, 25);
		RA1.connectTo(R, 25);
		this.roads.add(R);
		//CR.setTimeTrafficLight(20);
		
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
		g.setColor(Assets.bgCol);
		g.fillRect(0, 0, sim.getWidth(), sim.getHeight());
		
		// Print cells
		for (CrossRoad cr: crossRoads) {
			Graphics2D gg = (Graphics2D) g.create();
			gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if (drawColors) {
				gg.setColor(Color.cyan);
			} else {
				gg.setColor(Color.white);
			}
			
			gg.rotate(((cr.getDirection())/360.0)*2*Math.PI- Math.PI/2, cr.getX(), cr.getY());
			
			if (drawWire) {
				if (drawColors) {
					gg.setColor(Color.cyan);
				} else {
					gg.setColor(Color.gray);
				}
				gg.fillRect((int) (cr.getX()-cellWidth), (int) (cr.getY()-cellHeight), cellHeight*2, cellHeight*2);
				gg.setColor(Color.white);
				gg.drawRect((int) (cr.getX()), (int) (cr.getY() - cellHeight), cellHeight, cellHeight);
				gg.drawRect((int) (cr.getX() - cellWidth), (int) (cr.getY() -cellHeight ), cellHeight, cellHeight);
				gg.drawRect((int) (cr.getX() - cellWidth), (int) (cr.getY()), cellHeight, cellHeight);
				gg.drawRect((int) (cr.getX()), (int) (cr.getY()), cellHeight, cellHeight);
			} else {
				/*gg.fillRect((int) (cr.getX()), (int) (cr.getY() - cellHeight), cellHeight, cellHeight);
				gg.fillRect((int) (cr.getX() - cellWidth), (int) (cr.getY() -cellHeight ), cellHeight, cellHeight);
				gg.fillRect((int) (cr.getX() - cellWidth), (int) (cr.getY()), cellHeight, cellHeight);
				gg.fillRect((int) (cr.getX()), (int) (cr.getY()), cellHeight, cellHeight);
				gg.drawRect((int) (cr.getX()), (int) (cr.getY() - cellHeight), cellHeight, cellHeight);
				gg.drawRect((int) (cr.getX() - cellWidth), (int) (cr.getY() -cellHeight ), cellHeight, cellHeight);
				gg.drawRect((int) (cr.getX() - cellWidth), (int) (cr.getY()), cellHeight, cellHeight);
				gg.drawRect((int) (cr.getX()), (int) (cr.getY()), cellHeight, cellHeight);*/
				gg.fillRect((int) (cr.getX()-cellWidth), (int) (cr.getY()-cellHeight), cellHeight*2, cellHeight*2);
				gg.drawRect((int) (cr.getX()-cellWidth), (int) (cr.getY()-cellHeight), cellHeight*2, cellHeight*2);
			}
			for (int i=0 ; i<4 ; i++) {
				cr.getMiddleCells()[i].setX((int) (cr.getX() - cellWidth/2 - cellWidth/2*Math.sqrt(2.0)*Math.sin(2*Math.PI*(-cr.getDirection() - 90 + 45 + i*90)/360)));
				cr.getMiddleCells()[i].setY((int) (cr.getY() - cellHeight/2 - cellWidth/2*Math.sqrt(2.0)*Math.cos(2*Math.PI*(-cr.getDirection() - 90 + 45 + i*90)/360)));
			}
			
			gg.dispose();
		}
		for (Road r: roads) {
			Graphics2D gg = (Graphics2D) g.create();
			gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gg.rotate((r.getDirection()/360.0)*2*Math.PI- Math.PI/2, r.getX(), r.getY());
			
			if (drawWire) {
				gg.setColor(Color.gray);
				gg.fillRect((int) (r.getX()), (int) (r.getY()-cellHeight/2.0), cellWidth*(r.getLength()), cellHeight);
			}
			
			for (int i=0 ; i<r.getLength() ; i++) {
				if (drawWire) {
					gg.setColor(Color.white);
					gg.drawRect((int) (r.getX()+i*cellWidth), (int) (r.getY() - cellHeight/2.0), cellWidth, cellHeight);
				}
				
				if (r.getRoadCells().get(i).getPreviousCell()==null) { // Start Cell
					gg.setColor(Color.green);
				} else if (r.getRoadCells().get(i).getNextCell()==null) { // End Cell
					gg.setColor(Color.red);
				} else if (r.getRoadCells().get(i).getOutCell()!=null){ // Out Cell
					gg.setColor(Color.blue);
				} else { // Standard Cell
					gg.setColor(Color.white);
				}
				
				r.getRoadCells().get(i).setX((int) (r.getX()-cellWidth/2+(i*cellWidth + cellWidth/2)*Math.sin(2*Math.PI*r.getDirection()/360)));
				r.getRoadCells().get(i).setY((int) (r.getY()-cellHeight/2-(i*cellWidth + cellWidth/2)*Math.cos(2*Math.PI*r.getDirection()/360)));
			}
			if (!drawWire) {
				gg.setColor(Color.white);
				gg.fillRect((int) (r.getX()), (int) (r.getY()-cellHeight/2.0), cellWidth*(r.getLength()), cellHeight);
				gg.drawRect((int) (r.getX()), (int) (r.getY()-cellHeight/2.0), cellWidth*(r.getLength()), cellHeight);
			}
			
			gg.dispose();
			// Render ID
			if (drawRoadID) {
				g.setColor(Color.black);
				g.drawString(Integer.toString(r.getId()), (int) (r.getX()+10), (int) (r.getY()-10));
			}
			// Render x,y position of Road
			/*g.setColor(Color.red);
			g.fillRect(r.getX()-1, r.getY()-5, 2, 10);
			g.fillRect(r.getX()-5, r.getY()-1, 10, 2);*/
		}
		for (RoundAbout r: roundAbouts) {
			Graphics2D gg = (Graphics2D) g.create();
			gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			double radius = (r.getLength()*cellWidth)/(2*Math.PI);
			double outRadius = radius+cellHeight/2;
			double inRadius = radius-cellHeight/2;
			
			double innerSize = 2*outRadius - (2 * cellHeight);
			
			Shape outer = new Ellipse2D.Double(0, 0, 2*outRadius, 2*outRadius);
			Shape inner = new Ellipse2D.Double(cellHeight, cellHeight, innerSize, innerSize);
			
			Area circle = new Area( outer );
			circle.subtract( new Area(inner) );
			
			gg.translate(r.getX()-outRadius, r.getY()-outRadius);
			
			if (!drawWire) {
				gg.setColor(Color.white);
				gg.fill(circle);
				
			} else {
				gg.setColor(Color.gray);
				gg.fill(circle);
				gg.setColor(Color.white);
				gg.draw(circle);
			}
			
			gg.dispose();
			
			gg = (Graphics2D) g.create();
			
			if (drawWire) {
				//gg.setColor(Color.white);
				//gg.drawOval((int) (r.getX()-outRadius), (int) (r.getY()-outRadius), (int) (outRadius*2), (int) (outRadius*2));
				//gg.drawOval((int) (r.getX()-inRadius), (int) (r.getY()-inRadius), (int) (inRadius*2), (int) (inRadius*2));
			}
			for (int i=0 ; i<r.getLength() ; i++) {
				gg.setColor(Color.white);
				//gg.fillRect((int) (r.getX()-outRadius), (int) (r.getY()-cellHeight/2-1), cellWidth, cellHeight+2);
				double angle = 2*Math.PI*i/r.getLength() - 2*Math.PI*r.getDirection()/360;
				double delta = 2*Math.PI/(2*r.getLength());
				int x1 = (int) (r.getX()-inRadius*Math.sin(angle + delta));
				int y1 = (int) (r.getY()-inRadius*Math.cos(angle + delta));
				int x2 = (int) (r.getX()-outRadius*Math.sin(angle + delta));
				int y2 = (int) (r.getY()-outRadius*Math.cos(angle + delta));
				if (drawWire) {
					gg.drawLine(x1, y1, x2, y2);
				}
				r.getRoadCells().get(i).setX((int) (r.getX()-radius*Math.sin(angle)));
				r.getRoadCells().get(i).setY((int) (r.getY()-radius*Math.cos(angle)));
			}
			
			//gg.setColor(Color.BLACK);
			//gg.draw(circle);
			
			gg.dispose();
			// Render ID
			if (drawRoadID) {
				g.setColor(Color.black);
				g.drawString(Integer.toString(r.getId()), (int) (r.getX()+10), (int) (r.getY()-10));
			}
			// Render x,y position of RoundAbout
			/*gg.setColor(Color.red);
			gg.fillRect(r.getX()-1, r.getY()-5, 2, 10);
			gg.fillRect(r.getX()-5, r.getY()-1, 10, 2);*/
			//gg.dispose();
		}
	}
	// Render Vehicles according to Cells
	public void render(Graphics g) {
		
		// Print cells
		g.setColor(Color.black);
		for (CrossRoad CR: crossRoads) {
			for (int i=0; i<4; ++i) {
				if (CR.getMiddleCells()[i].isOccupied()) {
					g.fillOval(CR.getMiddleCells()[i].getX(), CR.getMiddleCells()[i].getY(), cellWidth, cellHeight);
				}
			}
		}
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
		g.drawString("Step :    " + Integer.toString(sim.getStep()), 630, 75);
		g.drawString("Time :    " + sim.getTime(), 630, 95);
		g.drawString("Speed : " + ((int) (10*3.6*7.5/sim.getStepSize())/10.0) + " km/h", 630, 115);
	}
	// Update Cell of the Road according to the next state
	public void evolve() {
		for (CrossRoad CR: crossRoads) {
			for (int i=0; i < 4; ++i) {
				CR.getMiddleCells()[i].evolve();
			}
		}
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
		for (CrossRoad CR: crossRoads) {
			if (!CR.getRoadsIN()[(CR.getStateOfTrafficLight()+1)%4].isTrafficLightRed()) {
				CR.setTrafficLightState(CR.getStateOfTrafficLight()%4);
			}
			for (int i=0; i < 4; ++i) {
				if (CR.getMiddleCells()[i].getOutCell() == null) {
					if (CR.getMiddleCells()[i].isOccupied()) {
						
						if (CR.getMiddleCells()[i].getNextCell().isOccupied()) {
							
							CR.getMiddleCells()[i].setIsOccupiedNext(1);
							
						} else {
							CR.getMiddleCells()[i].setIsOccupiedNext(0);
							CR.getMiddleCells()[i].getNextCell().setIsOccupiedNext(1);
							
						}
					} else {
						
						if (CR.getMiddleCells()[i].getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							CR.getMiddleCells()[i].setIsOccupiedNext(0);
						}
					}
				} else {
					if (CR.getMiddleCells()[i].isOccupied()) {
						if ((int)(Math.random()*2)==1) {
							if (CR.getMiddleCells()[i].getOutCell().isOccupied()) {
								
								CR.getMiddleCells()[i].setIsOccupiedNext(1);
								
							} else {
								CR.getMiddleCells()[i].setIsOccupiedNext(0);
								CR.getMiddleCells()[i].getOutCell().setIsOccupiedNext(1);
								
							}
						} else {
							if (CR.getMiddleCells()[i].getNextCell().isOccupied()) {
								
								CR.getMiddleCells()[i].setIsOccupiedNext(1);
								
							} else {
								CR.getMiddleCells()[i].setIsOccupiedNext(0);
								CR.getMiddleCells()[i].getNextCell().setIsOccupiedNext(1);
								
							}
						}
						
					} else {
						
						if (CR.getMiddleCells()[i].getIsOccupiedNext() == -1) {
							// Cell stay inoccupied
							CR.getMiddleCells()[i].setIsOccupiedNext(0);
						}
					}
				}
			}
			CR.setCounter(CR.getCounter()+1);
			if (CR.getCounter()>=CR.getTimeTrafficLight()) {
				if (CR.getCounter()>=CR.getTimeTrafficLight()+5) {
					CR.setStateOfTrafficLight((CR.getStateOfTrafficLight()+1)%4);
					CR.setTrafficLightState(CR.getStateOfTrafficLight());
					CR.setCounter(0);
				} else if (CR.getCounter()==CR.getTimeTrafficLight()) {
					CR.setAllTrafficLightsRed();
				}
				
				
			}
		}
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
							if (r.getRoadCells().get(i).getNextCell().isOccupied() || r.getRoadCells().get(i).getNextCell().getPreviousCell().isOccupied() || r.isTrafficLightRed()) {
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
				if (Math.random()<0.1) {
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
	
	public void addRoadtoRoads(Road r) {
		this.roads.add(r);
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
