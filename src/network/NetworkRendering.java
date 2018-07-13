package network;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

	// One-time operations ##########################################################################################################################
	// ##############################################################################################################################################
	
	// Compute Background (one-time operation)
	public static void renderAllBGs(Network n, BufferedImage[] backgrounds) {
		Graphics2D g2d = backgrounds[0].createGraphics();
		renderBG(n, g2d, false, false, false);
		g2d = backgrounds[1].createGraphics();
		renderBG(n, g2d, false, false, true);
		g2d = backgrounds[2].createGraphics();
		renderBG(n, g2d, false, true, false);
		g2d = backgrounds[3].createGraphics();
		renderBG(n, g2d, false, true, true);
		g2d = backgrounds[4].createGraphics();
		renderBG(n, g2d, true, false, false);
		g2d = backgrounds[5].createGraphics();
		renderBG(n, g2d, true, false, true);
		g2d = backgrounds[6].createGraphics();
		renderBG(n, g2d, true, true, false);
		g2d = backgrounds[7].createGraphics();
		renderBG(n, g2d, true, true, true);
	}
	public static void renderBG(Network n, Graphics g, boolean drawColors, boolean drawWire, boolean drawRoadID) {
		g.setColor(Assets.bgCol);
		g.fillRect(0, 0, n.getSimulation().getWidth(), n.getSimulation().getHeight());
		
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
			Graphics2D gg = (Graphics2D) g.create();
			gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gg.rotate((r.getDirection()/360.0)*2*Math.PI- Math.PI/2, r.getX(), r.getY());
			
			// Background ---------------------------------------------------------------------------------------------
			if (drawWire) {
				gg.setColor(Color.gray);
			} else {
				gg.setColor(Color.white);
			}
			gg.fillRect((int) (r.getX()), (int) (r.getY()-n.getCellHeight()/2.0), n.getCellWidth()*(r.getLength()), n.getCellHeight());
			
			// Colored cells ----------------------------------------------------------------------
			for (int i=0 ; i<r.getLength() ; i++) {
				if (drawColors) {
					if (r.getRoadCells().get(i).getPreviousCell()==null) { // Start Cell
						gg.setColor(Color.green);
						gg.fillRect((int) (r.getX()+i*n.getCellWidth()), (int) (r.getY() - n.getCellHeight()/2.0), n.getCellWidth(), n.getCellHeight());
					} else if (r.getRoadCells().get(i).getNextCell()==null) { // End Cell
						gg.setColor(Color.red);
						gg.fillRect((int) (r.getX()+i*n.getCellWidth()), (int) (r.getY() - n.getCellHeight()/2.0), n.getCellWidth(), n.getCellHeight());
					} else if (r.getRoadCells().get(i).getOutCell()!=null){ // Out Cell
						gg.setColor(Color.blue);
						gg.fillRect((int) (r.getX()+i*n.getCellWidth()), (int) (r.getY() - n.getCellHeight()/2.0), n.getCellWidth(), n.getCellHeight());
					}
				}
			}
			
			// Wires --------------------------------------------------------------------------------------------------
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
			gg.dispose();
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
		renderButtonsHeader(n, g);
	}
	public static void renderIDs(Network n, Graphics g) {
		g.setColor(Color.blue);
		for (Road r: n.getRoads()) {
			g.drawString(Integer.toString(r.getId()), (int) (r.getX()+10), (int) (r.getY()-10));
		}
		for (RoundAbout r: n.getRoundAbouts()) {
			g.drawString(Integer.toString(r.getId()), (int) (r.getX()+10), (int) (r.getY()-10));
		}
	}
	public static void renderButtonsHeader(Network n, Graphics g) {
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
		renderInformations(network, g);
		//renderElementCenter(Network network, g);
	}
	public static void renderVehicles(Network n, Graphics g) {
		g.setColor(Color.black);
		for (CrossRoad cr: n.getCrossRoads()) {
			for (int i=0; i<4; ++i) {
				if (cr.getMiddleCells()[i].isOccupied()) {
					g.fillOval((int) (cr.getMiddleCells()[i].getX()), (int) (cr.getMiddleCells()[i].getY()), n.getCellWidth(), n.getCellHeight());
				}
			}
		}
		for (Road r: n.getRoads()) {
			for (int i=0 ; i<r.getLength() ; i++) {
				if (r.getRoadCells().get(i).isOccupied()) {
					g.fillOval((int) (r.getRoadCells().get(i).getX()), (int) (r.getRoadCells().get(i).getY()), n.getCellWidth(), n.getCellHeight());
				}
			}
		}
		for (RoundAbout r: n.getRoundAbouts()) {
			for (int i=0 ; i<r.getLength() ; i++) {
				if (r.getRoadCells().get(i).isOccupied()) {
					g.fillOval((int) (r.getRoadCells().get(i).getX()-n.getCellWidth()/2), (int) (r.getRoadCells().get(i).getY()-n.getCellHeight()/2), n.getCellWidth(), n.getCellHeight());
				}
			}
		}
	}
	public static void renderElementCenter(Network n, Graphics g) {
		g.setColor(Color.red);
		for (CrossRoad cr: n.getCrossRoads()) {
			g.fillRect((int) (cr.getX()-1), (int) (cr.getY()-5), 2, 10);
			g.fillRect((int) (cr.getX()-5), (int) (cr.getY()-1), 10, 2);
		}
		for (Road r: n.getRoads()) {
			g.fillRect((int) (r.getX()-1), (int) (r.getY()-5), 2, 10);
			g.fillRect((int) (r.getX()-5), (int) (r.getY()-1), 10, 2);
		}
		for (RoundAbout r: n.getRoundAbouts()) {
			g.fillRect((int) (r.getX()-1), (int) (r.getY()-5), 2, 10);
			g.fillRect((int) (r.getX()-5), (int) (r.getY()-1), 10, 2);
		}
	}
	public static void renderInformations(Network n, Graphics g) {
		g.setColor(Color.white);
		g.drawString("Step :    " + Integer.toString(n.getSimulation().getStep()), n.getSimulation().getWidth()-170, 40);
		g.drawString("Time :    " + n.getSimulation().getTime(), n.getSimulation().getWidth()-170, 60);
		g.drawString("Speed : " + ((int) (10*3.6*7.5/n.getSimulation().getStepSize())/10.0) + " km/h", n.getSimulation().getWidth()-170, 80);
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
