package network;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import elements.CrossRoad;
import elements.Road;
import elements.RoundAbout;
import graphics.Assets;
import graphics.Text;

public class NetworkRendering {

	public static Rectangle bounds = new Rectangle(0,0,300,300);
	// One-time operations ##########################################################################################################################
	// ##############################################################################################################################################
	
	// Compute Background (one-time operation)
	public static BufferedImage[] renderAllBGs(Network n, BufferedImage[] backgrounds) {
		//computeBounds(n);
		BufferedImage[] tmp = new BufferedImage[8];
		for (int i=0 ; i<8 ; i++) {
			tmp[i] = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
		}
		Graphics2D g2d = tmp[0].createGraphics();
		g2d.translate(-bounds.x, -bounds.y);
		renderBG(n, g2d, false, false, false);
		g2d = tmp[1].createGraphics();
		g2d.translate(-bounds.x, -bounds.y);
		renderBG(n, g2d, false, false, true);
		g2d = tmp[2].createGraphics();
		g2d.translate(-bounds.x, -bounds.y);
		renderBG(n, g2d, false, true, false);
		g2d = tmp[3].createGraphics();
		g2d.translate(-bounds.x, -bounds.y);
		renderBG(n, g2d, false, true, true);
		g2d = tmp[4].createGraphics();
		g2d.translate(-bounds.x, -bounds.y);
		renderBG(n, g2d, true, false, false);
		g2d = tmp[5].createGraphics();
		g2d.translate(-bounds.x, -bounds.y);
		renderBG(n, g2d, true, false, true);
		g2d = tmp[6].createGraphics();
		g2d.translate(-bounds.x, -bounds.y);
		renderBG(n, g2d, true, true, false);
		g2d = tmp[7].createGraphics();
		g2d.translate(-bounds.x, -bounds.y);
		renderBG(n, g2d, true, true, true);
		
		return tmp;
	}
	public static void renderBG(Network n, Graphics g, boolean drawColors, boolean drawWire, boolean drawRoadID) {
		// CrossRoads =================================================================================================
		for (CrossRoad cr: n.getCrossRoads()) {
			Graphics2D gg = (Graphics2D) g.create();
			gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gg.rotate(((cr.getDirection())/360.0)*2*Math.PI- Math.PI/2, cr.getX(), cr.getY());
			
			// Background ---------------------------------------------------------------------------------------------
			if (drawColors) {
				gg.setColor(Color.cyan);
			} else {
				if (drawWire) {
					gg.setColor(Color.gray);
				} else {
					gg.setColor(Color.white);
				}
			}
			gg.fillRect((int) (cr.getX()-n.getCellWidth()), (int) (cr.getY()-n.getCellHeight()), n.getCellHeight()*2, n.getCellHeight()*2);
			gg.drawRect((int) (cr.getX()-n.getCellWidth()), (int) (cr.getY()-n.getCellHeight()), n.getCellHeight()*2, n.getCellHeight()*2);
			
			// Wires --------------------------------------------------------------------------------------------------
			if (drawWire) {
				gg.setColor(Color.white);
				gg.drawRect((int) (cr.getX()), (int) (cr.getY() - n.getCellHeight()), n.getCellHeight(), n.getCellHeight());
				gg.drawRect((int) (cr.getX() - n.getCellWidth()), (int) (cr.getY() -n.getCellHeight() ), n.getCellHeight(), n.getCellHeight());
				gg.drawRect((int) (cr.getX() - n.getCellWidth()), (int) (cr.getY()), n.getCellHeight(), n.getCellHeight());
				gg.drawRect((int) (cr.getX()), (int) (cr.getY()), n.getCellHeight(), n.getCellHeight());
			}
			gg.dispose();
		}
		// Roads ======================================================================================================
		for (Road r: n.getRoads()) {
			double firstAngle = (r.getDirection()/360.0)*2*Math.PI- Math.PI/2;
			Graphics2D gg = (Graphics2D) g.create();
			gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gg.rotate(firstAngle, r.getX(), r.getY());
			
			// Background ---------------------------------------------------------------------------------------------
			if (drawWire) {
				gg.setColor(Color.gray);
			} else {
				gg.setColor(Color.white);
			}
			
			if (r.getReorientations().size()==1) {
				gg.fillRect((int) (r.getX()), (int) (r.getY()-n.getCellHeight()/2.0), n.getCellWidth()*(r.getLength()), n.getCellHeight());
				if (drawWire) {
					gg.setColor(Color.white);
					gg.drawRect((int) (r.getX()), (int) (r.getY()-n.getCellHeight()/2.0), n.getCellWidth()*(r.getLength()), n.getCellHeight());
				}
				
				for (int i=0 ; i<r.getLength() ; i++) {
					if (drawWire) {
						gg.setColor(Color.white);
						gg.drawLine((int) (r.getX()+i*n.getCellWidth()), (int) (r.getY() - n.getCellHeight()/2.0), (int) (r.getX()+i*n.getCellWidth()), (int) (r.getY() + n.getCellHeight()/2.0));
					}
				}
			} else {				
				double x = r.getX();
				double y = r.getY()-n.getCellHeight()/2.0;
				double lastAngle = r.getDirection();
				double newAngle = r.getDirection();
				gg.fillRect((int) (r.getX()), (int) (r.getY()-n.getCellHeight()/2.0), (int) (n.getCellWidth()*(r.getReorientations().get(1).getX())), n.getCellHeight());
				gg.setColor(Color.white);
				gg.drawRect((int) (r.getX()), (int) (r.getY()-n.getCellHeight()/2.0), (int) (n.getCellWidth()*(r.getReorientations().get(1).getX())), n.getCellHeight());
				for (int i=1 ; i<r.getReorientations().get(1).getX() ; i++) {
					if (drawWire) {
						gg.setColor(Color.white);
						gg.drawLine((int) (r.getX()+i*n.getCellWidth()), (int) (r.getY() - n.getCellHeight()/2.0), (int) (r.getX()+i*n.getCellWidth()), (int) (r.getY() + n.getCellHeight()/2.0));
					}
				}
				for (int i=1 ; i<r.getReorientations().size() ; i++) {
					lastAngle = newAngle;
					newAngle = r.getReorientations().get(i).getY();
					x += n.getCellWidth()*(r.getReorientations().get(i).getX()-r.getReorientations().get(i-1).getX());
					gg.rotate(2*Math.PI*(newAngle-lastAngle)/360.0, x, y+n.getCellHeight()/2.0);
					
					if (drawWire) {
						gg.setColor(Color.gray);
					} else {
						gg.setColor(Color.white);
					}
					
					if (i==r.getReorientations().size()-1) {
						gg.fillRect((int) (x), (int) (y), (int) (n.getCellWidth()*(r.getLength()-r.getReorientations().get(i).getX())), n.getCellHeight());
						for (int j=1 ; j < r.getLength()-r.getReorientations().get(i).getX() ; j++) {
							if (drawWire) {
								gg.setColor(Color.white);
								gg.drawLine((int) (x+j*n.getCellWidth()), (int) (y - 0*n.getCellHeight()/2.0), (int) (x+j*n.getCellWidth()), (int) (y + 2*n.getCellHeight()/2.0));
							}
						}
					} else {
						gg.fillRect((int) (x), (int) (y), (int) (n.getCellWidth()*(r.getReorientations().get(i+1).getX()-r.getReorientations().get(i).getX())), n.getCellHeight());
						for (int j=1 ; j < r.getReorientations().get(i+1).getX()-r.getReorientations().get(i).getX() ; j++) {
							if (drawWire) {
								gg.setColor(Color.white);
								gg.drawLine((int) (x+j*n.getCellWidth()), (int) (y - 0*n.getCellHeight()/2.0), (int) (x+j*n.getCellWidth()), (int) (y + 2*n.getCellHeight()/2.0));
							}
						}
					}
					
					if (drawWire) {
						gg.setColor(Color.white);
						if (i==r.getReorientations().size()-1) {
							gg.drawRect((int) (x), (int) (y), (int) (n.getCellWidth()*(r.getLength()-r.getReorientations().get(i).getX())), n.getCellHeight());
						} else {
							gg.drawRect((int) (x), (int) (y), (int) (n.getCellWidth()*(r.getReorientations().get(i+1).getX()-r.getReorientations().get(i).getX())), n.getCellHeight());
						}
					}
					
					
				}
			}
			gg.dispose();
			// Colored cells ----------------------------------------------------------------------
			gg = (Graphics2D) g.create();
			gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gg.rotate(firstAngle, r.getX(), r.getY());
			double x = r.getX();
			double y = r.getY();
			double lastAngle = r.getDirection();
			double newAngle = r.getDirection();
			int tmp = 1;
			for (int i=0 ; i<r.getLength() ; i++) {
				if (drawColors) {
					lastAngle = newAngle;
					if (r.getReorientations().size()!=1) {
						if (i == r.getReorientations().get(tmp).getX()) {
							newAngle = r.getReorientations().get(tmp).getY();
							gg.rotate(2*Math.PI*(newAngle-lastAngle)/360.0, x, y);
							if (tmp<r.getReorientations().size()-1) {
								tmp++;
							}
						}
					}
					if (r.getRoadCells().get(i).getPreviousCell()==null) { // Start Cell
						gg.setColor(Color.green);
						gg.fillRect((int) (x), (int) (y - n.getCellHeight()/2.0), n.getCellWidth(), n.getCellHeight());
					} else if (r.getRoadCells().get(i).getNextCell()==null) { // End Cell
						gg.setColor(Color.red);
						gg.fillRect((int) (x), (int) (y - n.getCellHeight()/2.0), n.getCellWidth(), n.getCellHeight());
					} else if (r.getRoadCells().get(i).getOutCell()!=null){ // Out Cell
						gg.setColor(Color.blue);
						gg.fillRect((int) (x), (int) (y - n.getCellHeight()/2.0), n.getCellWidth(), n.getCellHeight());
					}
					x += n.getCellWidth();
				}
			}
			gg.dispose();
			// Wires --------------------------------------------------------------------------------------------------
			/*gg = (Graphics2D) g.create();
			gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gg.rotate(firstAngle, r.getX(), r.getY());
			if (drawWire) {
				gg.setColor(Color.white);
				if (r.getReorientations().size()==1) {
					gg.drawRect((int) (r.getX()), (int) (r.getY()-n.getCellHeight()/2.0), n.getCellWidth()*(r.getLength()), n.getCellHeight());
				} else {
					double x = r.getX();
					double y = r.getY()-n.getCellHeight()/2.0;
					double lastAngle = r.getDirection();
					double newAngle = r.getDirection();
					gg.drawRect((int) (r.getX()), (int) (r.getY()-n.getCellHeight()/2.0), (int) (n.getCellWidth()*(r.getReorientations().get(1).getX())), n.getCellHeight());
					
					for (int i=1 ; i<r.getReorientations().size() ; i++) {
						lastAngle = newAngle;
						newAngle = r.getReorientations().get(i).getY();
						x += n.getCellWidth()*(r.getReorientations().get(i).getX()-r.getReorientations().get(i-1).getX());
						gg.rotate(2*Math.PI*(newAngle-lastAngle)/360.0, x, y);
						
						
						
						
					}
				}
			}
			for (int i=0 ; i<r.getLength() ; i++) {
				if (drawWire) {
					gg.setColor(Color.white);
					//gg.drawLine((int) (r.getX()+i*n.getCellWidth()), (int) (r.getY() - n.getCellHeight()/2.0), (int) (r.getX()+i*n.getCellWidth()), (int) (r.getY() + n.getCellHeight()/2.0));
				}
			}
			gg.dispose();*/
		}
		// RoundAbouts ================================================================================================
		for (RoundAbout r: n.getRoundAbouts()) {
			Graphics2D gg = (Graphics2D) g.create();
			gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			double radius = (r.getLength()*n.getCellWidth())/(2*Math.PI);
			double outRadius = radius+n.getCellHeight()/2;
			double inRadius = radius-n.getCellHeight()/2;
			double innerSize = 2*outRadius - (2 * n.getCellHeight());
			
			Shape outer = new Ellipse2D.Double(-1, -1, 2*outRadius+2, 2*outRadius+2);
			Shape inner = new Ellipse2D.Double(n.getCellHeight(), n.getCellHeight(), innerSize, innerSize);
			Area circle = new Area(outer);
			circle.subtract(new Area(inner));
			
			gg.translate(r.getX()-outRadius, r.getY()-outRadius);
			
			// Background ---------------------------------------------------------------------------------------------
			if (drawWire) {
				gg.setColor(Color.gray);
			} else {
				gg.setColor(Color.white);
			}
			gg.fill(circle);
			
			// Wires --------------------------------------------------------------------------------------------------
			if (drawWire) {
				gg.setColor(Color.white);
			}
			gg.draw(circle);
			gg.dispose();
			gg = (Graphics2D) g.create();
			gg.setColor(Color.white);
			
			for (int i=0 ; i<r.getLength() ; i++) {
				double angle = 2*Math.PI*i/r.getLength() - 2*Math.PI*r.getDirection()/360;
				double delta = 2*Math.PI/(2*r.getLength());
				int x1 = (int) (r.getX()-inRadius*Math.sin(angle + delta));
				int y1 = (int) (r.getY()-inRadius*Math.cos(angle + delta));
				int x2 = (int) (r.getX()-outRadius*Math.sin(angle + delta));
				int y2 = (int) (r.getY()-outRadius*Math.cos(angle + delta));
				if (drawWire) {
					gg.drawLine(x1, y1, x2, y2);
				}
			}
			
			gg.dispose();
		}
		if (drawRoadID) {
			renderIDs(n, g);
		}
	}
	public static void renderIDs(Network n, Graphics g) {
		g.setColor(Color.blue);
		g.setFont(new Font("Arial",Font.BOLD,14));
		for (Road r: n.getRoads()) {
			g.drawString(Integer.toString(r.getId()), (int) (r.getX()+10), (int) (r.getY()-10));
		}
		for (RoundAbout r: n.getRoundAbouts()) {
			g.drawString(Integer.toString(r.getId()), (int) (r.getX()+10), (int) (r.getY()-10));
		}
	}
	public static void renderButtonsHeader(Network n, BufferedImage hud) {
		Graphics2D g = hud.createGraphics();
		g.setColor(Assets.idleCol);
		
		// "Controls"
		g.fillRect(Assets.buttonXStart, Assets.buttonYStart-15, 2, 12);
		g.fillRect(Assets.buttonXStart, Assets.buttonYStart-15, 100, 2);
		Text.drawString(g, "controls", Assets.idleCol, Assets.buttonXStart+(int) (Assets.buttonW*1.5)+Assets.buttonSpacing, Assets.buttonYStart-17, true, Assets.normalFont);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*3+Assets.buttonSpacing*2-100, Assets.buttonYStart-15, 100, 2);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*3+Assets.buttonSpacing*2-1, Assets.buttonYStart-15, 2, 12);
		
		// "Speed"
		g.fillRect(Assets.buttonXStart+Assets.buttonW*3+Assets.buttonSpacing*3, Assets.buttonYStart-15, 2, 12);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*3+Assets.buttonSpacing*3, Assets.buttonYStart-15, 200, 2);
		Text.drawString(g, "speed", Assets.idleCol, (int) (Assets.buttonXStart+Assets.buttonW*5.5+Assets.buttonSpacing*5), Assets.buttonYStart-17, true, Assets.normalFont);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*8+Assets.buttonSpacing*7-200, Assets.buttonYStart-15, 200, 2);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*8+Assets.buttonSpacing*7-1, Assets.buttonYStart-15, 2, 12);
		
		// "Visuals"
		g.fillRect(Assets.buttonXStart, n.getSimulation().getHeight()-Assets.buttonH-20-15, 2, 12);
		g.fillRect(Assets.buttonXStart, n.getSimulation().getHeight()-Assets.buttonH-20-15, 100, 2);
		Text.drawString(g, "visuals", Assets.idleCol, Assets.buttonXStart+(int) (Assets.buttonW*1.5)+Assets.buttonSpacing, n.getSimulation().getHeight()-Assets.buttonH-20-17, true, Assets.normalFont);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*3+Assets.buttonSpacing*2-100, n.getSimulation().getHeight()-Assets.buttonH-20-15, 100, 2);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*3+Assets.buttonSpacing*2-1, n.getSimulation().getHeight()-Assets.buttonH-20-15, 2, 12);
	}
	
	// Every-frame operations #######################################################################################################################
	// ##############################################################################################################################################
		
	public static void render(Network network, Graphics g) {
		renderVehicles(network, g);
		if (network.getDrawCenters()) {
			renderElementCenter(network, g);
		}
	}
	public static void renderVehicles(Network n, Graphics g) {
		Graphics2D gg = (Graphics2D) g.create();
		gg.translate(-bounds.x, -bounds.y);
		gg.setColor(Color.black);
		for (CrossRoad cr: n.getCrossRoads()) {
			for (int i=0; i<4; ++i) {
				if (cr.getMiddleCells()[i].isOccupied()) {
					gg.fillOval((int) (cr.getMiddleCells()[i].getX()), (int) (cr.getMiddleCells()[i].getY()), n.getCellWidth(), n.getCellHeight());
				}
			}
		}
		for (Road r: n.getRoads()) {
			for (int i=0 ; i<r.getLength() ; i++) {
				if (r.getRoadCells().get(i).isOccupied()) {
					gg.fillOval((int) (r.getRoadCells().get(i).getX()), (int) (r.getRoadCells().get(i).getY()), n.getCellWidth(), n.getCellHeight());
				}
			}
		}
		for (RoundAbout r: n.getRoundAbouts()) {
			for (int i=0 ; i<r.getLength() ; i++) {
				if (r.getRoadCells().get(i).isOccupied()) {
					gg.fillOval((int) (r.getRoadCells().get(i).getX()-n.getCellWidth()/2), (int) (r.getRoadCells().get(i).getY()-n.getCellHeight()/2), n.getCellWidth(), n.getCellHeight());
				}
			}
		}
		gg.dispose();
	}
	public static void renderElementCenter(Network n, Graphics g) {
		Graphics2D gg = (Graphics2D) g.create();
		gg.translate(-bounds.x, -bounds.y);
		gg.setColor(Color.red);
		for (CrossRoad cr: n.getCrossRoads()) {
			gg.fillRect((int) (cr.getX()-1), (int) (cr.getY()-5), 2, 10);
			gg.fillRect((int) (cr.getX()-5), (int) (cr.getY()-1), 10, 2);
			Graphics2D ggg = (Graphics2D) gg.create();
			ggg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			ggg.rotate((cr.getDirection()/360.0)*2*Math.PI- Math.PI/2, cr.getX(), cr.getY());
			ggg.fillRect((int) (cr.getX()), (int) cr.getY()-1, 30, 2);
		}
		for (Road r: n.getRoads()) {
			gg.fillRect((int) (r.getX()-1), (int) (r.getY()-5), 2, 10);
			gg.fillRect((int) (r.getX()-5), (int) (r.getY()-1), 10, 2);
			Graphics2D ggg = (Graphics2D) gg.create();
			ggg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			ggg.rotate((r.getDirection()/360.0)*2*Math.PI- Math.PI/2, r.getX(), r.getY());
			ggg.fillRect((int) r.getX(), (int) r.getY()-1, 30, 2);
		}
		for (RoundAbout r: n.getRoundAbouts()) {
			gg.fillRect((int) (r.getX()-1), (int) (r.getY()-5), 2, 10);
			gg.fillRect((int) (r.getX()-5), (int) (r.getY()-1), 10, 2);
			Graphics2D ggg = (Graphics2D) gg.create();
			ggg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			ggg.rotate((r.getDirection()/360.0)*2*Math.PI- Math.PI/2, r.getX(), r.getY());
			ggg.fillRect((int) r.getX(), (int) r.getY()-1, 30, 2);
		}
	}
	public static void renderInformations(Network n, Graphics g) {
		g.setColor(Color.white);
		g.drawString("Steps :    " + Integer.toString(n.getSimulation().getSimState().getStep()), n.getSimulation().getWidth()-170, 40);
		g.drawString("Time :    " + n.getSimulation().getSimState().getTime(), n.getSimulation().getWidth()-170, 60);
		g.drawString("Speed : " + ((int) (10*3.6*7.5/n.getSimulation().getSimState().getStepSize())/10.0) + " km/h", n.getSimulation().getWidth()-170, 80);
	}
	// Render Vehicles according to Cells
	public static void display(Network n) {
		for (Road r: n.getRoads()) {
			r.display();
		}
		System.out.print("\n");
		for (RoundAbout r: n.getRoundAbouts()) {
			r.display();
		}
		System.out.print("\n");
	}
	
}
