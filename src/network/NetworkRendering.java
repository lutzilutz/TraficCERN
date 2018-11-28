package network;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import elements.CrossRoad;
import elements.Road;
import elements.RoundAbout;
import elements.Vehicle;
import graphics.Assets;
import graphics.Text;
import utils.Utils;

public class NetworkRendering {

	public static Rectangle bounds = new Rectangle(0,0,300,300);
	public static ArrayList<Polygon> zone = new ArrayList<Polygon>();
	
	// One-time operations ##########################################################################################################################
	// ##############################################################################################################################################
	
	// Render (compute) all background images
	public static BufferedImage[] renderAllBGs(Network n, BufferedImage[] backgrounds) {
		Utils.log("generating Network images ... ");
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
		
		Utils.log("done");
		Utils.logTime();
		
		return tmp;
	}
	
	// Render one background image
	public static void renderBG(Network n, Graphics g, boolean drawColors, boolean drawWire, boolean drawRoadID) {
		// CERN zone ==================================================================================================
		g.setColor(Assets.zoneCERNCol);
		for (Polygon zone: n.getZones()) {
			g.fillPolygon(zone);
		}
		Text.drawString(g, "CERN", Assets.zoneCERNtextCol, 115*n.getCellWidth(), 80*n.getCellWidth(), true, Assets.hugeFont);
		Text.drawString(g, "Entrance E", Assets.zoneCERNtextCol, 12*n.getCellWidth(), 27*n.getCellWidth(), true, Assets.largeFont);
		Text.drawString(g, "Entrance B", Assets.zoneCERNtextCol, 187*n.getCellWidth(), 103*n.getCellWidth(), true, Assets.largeFont);
		Text.drawString(g, "Entrance A", Assets.zoneCERNtextCol, 210*n.getCellWidth(), 119*n.getCellWidth(), true, Assets.largeFont);
		Text.drawString(g, "Inter-site", Assets.zoneCERNtextCol, 126*n.getCellWidth(), 71*n.getCellWidth(), true, Assets.largeFont);
		
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
						if (r.getRoadCells().get(i).getOutCell()==null){ // Out Cell
							gg.setColor(Color.red);
							gg.fillRect((int) (x), (int) (y - n.getCellHeight()/2.0), n.getCellWidth(), n.getCellHeight());
						} else {
							gg.setColor(Color.pink);
							gg.fillRect((int) (x), (int) (y - n.getCellHeight()/2.0), n.getCellWidth(), n.getCellHeight());
						}
					}
					
					x += n.getCellWidth();
				}
			}
			gg.dispose();
		}
		// RoundAbouts ================================================================================================
		for (RoundAbout r: n.getRoundAbouts()) {
			Graphics2D gg = (Graphics2D) g.create();
			gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			
			double radius = (r.getLength()*n.getCellWidth())/(2*Math.PI) - r.getInnerLane()*n.getCellWidth();
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
		g.fillRect(Assets.buttonXStart, n.getSimulation().getHeight()-Assets.buttonH-20-15, 250, 2);
		Text.drawString(g, "visuals", Assets.idleCol, Assets.buttonXStart+(int) (Assets.buttonW*3 + Assets.buttonSpacing*2.5), n.getSimulation().getHeight()-Assets.buttonH-20-17, true, Assets.normalFont);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*6+Assets.buttonSpacing*5-250, n.getSimulation().getHeight()-Assets.buttonH-20-15, 250, 2);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*6+Assets.buttonSpacing*5-1, n.getSimulation().getHeight()-Assets.buttonH-20-15, 2, 12);
	}
	
	// Every-frame operations #######################################################################################################################
	// ##############################################################################################################################################
		
	public static void render(Network network, Graphics g) {
		renderVehicles(network, g);
		if (network.getDrawNames()) {
			renderElementName(network, g);
		}
		if (network.getDrawCenters()) {
			renderElementCenter(network, g);
		}
		renderCounters(network, g);
		renderLeakyBuckets(network, g);
	}
	public static void renderVehicles(Network n, Graphics g) {
		Graphics2D gg = (Graphics2D) g.create();
		gg.translate(-bounds.x, -bounds.y);
		gg.setColor(Color.black);
		for (CrossRoad cr: n.getCrossRoads()) {
			for (int i=0; i<4; ++i) {
				if (cr.getMiddleCells()[i].getVehicle() != null) {
					Vehicle v = cr.getMiddleCells()[i].getVehicle();
					if (n.getDrawVehicleColor()) {
						int newSize = (int) (n.getCellWidth()*1.5);
						int newOffset = (int) ((newSize-n.getCellWidth())/2.0);
						gg.setColor(v.getDstColor());
						gg.fillOval((int) (cr.getMiddleCells()[i].getX()-newOffset), (int) (cr.getMiddleCells()[i].getY()-newOffset), newSize, newSize);
						int smallOffset = newSize/4;
						gg.setColor(v.getSrcColor());
						gg.fillOval((int) (cr.getMiddleCells()[i].getX()-newOffset+smallOffset), (int) (cr.getMiddleCells()[i].getY()-newOffset+smallOffset), newSize/2, newSize/2);
					} else {
						gg.setColor(Color.black);
						gg.fillOval((int) (cr.getMiddleCells()[i].getX()), (int) (cr.getMiddleCells()[i].getY()), n.getCellWidth(), n.getCellHeight());
					}
				}
			}
		}
		//System.out.println(n.getVehicles().size());
		for (Road r: n.getRoads()) {
			for (int i=0 ; i<r.getLength() ; i++) {
				if (r.getRoadCells().get(i).getVehicle() != null) {
					if (!r.getRoadCells().get(i).isUnderground()) {
						Vehicle v = r.getRoadCells().get(i).getVehicle();
						if (n.getDrawVehicleColor()) {
							int newSize = (int) (n.getCellWidth()*1.5);
							int newOffset = (int) ((newSize-n.getCellWidth())/2.0);
							gg.setColor(v.getDstColor());
							gg.fillOval((int) (r.getRoadCells().get(i).getX()-newOffset), (int) (r.getRoadCells().get(i).getY()-newOffset), newSize, newSize);
							int smallOffset = newSize/4;
							gg.setColor(v.getSrcColor());
							gg.fillOval((int) (r.getRoadCells().get(i).getX()-newOffset+smallOffset), (int) (r.getRoadCells().get(i).getY()-newOffset+smallOffset), newSize/2, newSize/2);
						} else {
							gg.setColor(Color.black);
							gg.fillOval((int) (r.getRoadCells().get(i).getX()), (int) (r.getRoadCells().get(i).getY()), n.getCellWidth(), n.getCellHeight());
						}
					}
				}
			}
		}
		for (RoundAbout r: n.getRoundAbouts()) {
			for (int i=0 ; i<r.getLength() ; i++) {
				if (r.getRoadCells().get(i).getVehicle() != null) {
					Vehicle v = r.getRoadCells().get(i).getVehicle();
					if (n.getDrawVehicleColor()) {
						int newSize = (int) (n.getCellWidth()*1.5);
						int newOffset = (int) ((newSize-n.getCellWidth())/2.0);
						gg.setColor(v.getDstColor());
						gg.fillOval((int) (r.getRoadCells().get(i).getX()-n.getCellWidth()/2-newOffset), (int) (r.getRoadCells().get(i).getY()-n.getCellWidth()/2-newOffset), newSize, newSize);
						int smallOffset = newSize/4;
						gg.setColor(v.getSrcColor());
						gg.fillOval((int) (r.getRoadCells().get(i).getX()-n.getCellWidth()/2-newOffset+smallOffset), (int) (r.getRoadCells().get(i).getY()-n.getCellWidth()/2-newOffset+smallOffset), newSize/2, newSize/2);
					} else {
						gg.setColor(Color.black);
						gg.fillOval((int) (r.getRoadCells().get(i).getX()-n.getCellWidth()/2), (int) (r.getRoadCells().get(i).getY()-n.getCellHeight()/2), n.getCellWidth(), n.getCellHeight());
					}
				}
			}
		}
		gg.dispose();
	}
	public static void renderElementName(Network n, Graphics g) {
		Graphics2D gg = (Graphics2D) g.create();
		gg.translate(-bounds.x, -bounds.y);
		gg.setColor(Color.red);
		for (CrossRoad cr: n.getCrossRoads()) {
			Text.drawString(gg, cr.getName(), Color.red, (int) cr.getX(), (int) cr.getY(), false, Assets.normalBoldFont);
		}
		for (Road r: n.getRoads()) {
			Text.drawString(gg, r.getName(), Color.red, (int) r.getX(), (int) r.getY(), false, Assets.normalBoldFont);
		}
		for (RoundAbout r: n.getRoundAbouts()) {
			Text.drawString(gg, r.getName(), Color.red, (int) r.getX(), (int) r.getY(), false, Assets.normalBoldFont);
		}
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
		g.drawString("Vehicles :    " + Integer.toString(n.getNumberOfVehicles()), n.getSimulation().getWidth()-170, n.getSimulation().getHeight()-90);
		g.drawString("Steps :    " + Integer.toString(n.getSimulation().getSimState().getStep()), n.getSimulation().getWidth()-170, n.getSimulation().getHeight()-70);
		g.drawString("Time :    " + n.getSimulation().getSimState().getTime(), n.getSimulation().getWidth()-170, n.getSimulation().getHeight()-50);
		g.drawString("Speed : " + ((int) (10*3.6*7.5/n.getSimulation().getSimState().getStepSize())/10.0) + " km/h", n.getSimulation().getWidth()-170, n.getSimulation().getHeight()-30);
		g.drawString("Rush hours : " + n.getSimulation().getSimState().isRushHours(), n.getSimulation().getWidth()-170, n.getSimulation().getHeight()-10);
		
		if (n.getDrawVehicleColor()) {
			int x = 660;
			int y = n.getSimulation().getHeight()-134;
			int yTextOffset = 10;
			int xMargin = 20;
			int yMargin = 17;
			int radius = 10;
			
			g.setColor(Assets.vhcFranceCol1);
			g.fillOval(x, y, radius, radius);
			g.setColor(Color.white);
			g.drawString("Thoiry", x+xMargin, y + yTextOffset);
			
			g.setColor(Assets.vhcFranceCol2);
			g.fillOval(x, y+yMargin, radius, radius);
			g.setColor(Color.white);
			g.drawString("St-Genis", x+xMargin, y + yMargin + yTextOffset);
			
			g.setColor(Assets.vhcFranceCol3);
			g.fillOval(x, y+2*yMargin, radius, radius);
			g.setColor(Color.white);
			g.drawString("Ferney", x+xMargin, y + 2*yMargin + yTextOffset);
			
			g.setColor(Assets.vhcFranceCol4);
			g.fillOval(x, y+3*yMargin, radius, radius);
			g.setColor(Color.white);
			g.drawString("Europe", x+xMargin, y + 3*yMargin + yTextOffset);
			
			g.setColor(Assets.vhcSuisseCol);
			g.fillOval(x, y+4*yMargin, radius, radius);
			g.setColor(Color.white);
			g.drawString("Genève", x+xMargin, y + 4*yMargin + yTextOffset);
			
			g.setColor(Assets.vhcCERNCol1);
			g.fillOval(x, y+5*yMargin, radius, radius);
			g.setColor(Color.white);
			g.drawString("Entree E", x+xMargin, y + 5*yMargin + yTextOffset);
			
			g.setColor(Assets.vhcCERNCol2);
			g.fillOval(x, y+6*yMargin, radius, radius);
			g.setColor(Color.white);
			g.drawString("Entree B", x+xMargin, y + 6*yMargin + yTextOffset);
			
			g.setColor(Assets.vhcCERNCol3);
			g.fillOval(x, y+7*yMargin, radius, radius);
			g.setColor(Color.white);
			g.drawString("Entree A", x+xMargin, y + 7*yMargin + yTextOffset);
			
		}
	}
	public static void renderCounters(Network n, Graphics g) {
		for (Road r: n.getRoads()) {
			if (r.getVehicleCounter() != null) {
				r.getVehicleCounter().render(g);
			}
		}
	}
	public static void renderLeakyBuckets(Network n, Graphics g) {
		Graphics2D gg = (Graphics2D) g.create();
		gg.translate(-bounds.x, -bounds.y);
		gg.setColor(Color.pink);
		for (Road r: n.getRoads()) {
			boolean renderBucket = false;
			for (Float i: r.getFlow()) {
				if (i>0) {
					renderBucket = true;
				}
			}
			if (renderBucket) {
				Text.drawString(gg, Integer.toString(r.getLeakyBucket().size()), Color.pink, (int) (r.getX()-6*n.getCellWidth()*Math.sin(2*Math.PI*r.getDirection()/360.0)), (int) (r.getY()+2*n.getCellWidth()*Math.cos(2*Math.PI*r.getDirection()/360.0)), true, Assets.normalBoldFont);
			}
		}
	}
	public static void renderHeaderBG(Network n, Graphics g) {
		// Left upper corner
		g.setColor(Assets.bgAlphaCol);
		g.fillRoundRect(-10, -10, Assets.buttonXStart+Assets.buttonW*8+Assets.buttonSpacing*7+6+10, Assets.buttonYStart+Assets.buttonH+5+10, 10, 10);
		
		// Rigth upper corner
		g.setColor(Assets.bgAlphaCol);
		g.fillRoundRect(n.getSimulation().getWidth()-Assets.buttonW-Assets.buttonXStart-5, -10, Assets.buttonW+Assets.buttonXStart+15, Assets.buttonYStart+Assets.buttonH+5+10, 10, 10);
		
		// Left bottom corner
		g.setColor(Assets.bgAlphaCol);
		g.fillRoundRect(-10, n.getSimulation().getHeight()-Assets.buttonH-20-17-6, Assets.buttonXStart+Assets.buttonW*6+Assets.buttonSpacing*5+6+10, Assets.buttonH+20+17+6+10, 10, 10);
		
		// Right bottom corner
		g.setColor(Assets.bgAlphaCol);
		g.fillRoundRect(n.getSimulation().getWidth()-175, n.getSimulation().getHeight()-105, 185, 115, 10, 10);
		
		// Middle bottom corner
		if (n.getDrawVehicleColor()) {
			g.setColor(Assets.bgAlphaCol);
			g.fillRoundRect(650, n.getSimulation().getHeight()-139, 130, 149, 10, 10);
		}
	}
}
