package states;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphics.Assets;
import input.KeyManager;
import main.Simulation;
import network.Network;
import network.NetworkComputing;
import network.NetworkRendering;
import ui.ClickListener;
import ui.UIImageButton;
import ui.UIManager;
import ui.UITextButton;
import ui.UITextSwitch;

public class SimState extends State {
	
	private UIManager uiManager;
	private KeyManager keyManager;
	
	private Network network;
	
	private int step = 0; // step counter
	private double stepSize = 1; // duration of one step in seconds-
	private String[] daysOfWeek = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
	private boolean paused = false;
	private long lastTick;
	
	private double simSpeed = 20;
	private double offsetSpeed = 1;
	private double rotationSpeed = 0.02;
	
	private UITextButton stepByStep;
	private UIImageButton playPause;
	private UITextSwitch colorOn, wireOn, idOn, centersOn;
	
	private BufferedImage[] networkDisplays;
	private BufferedImage currentDisplay;
	private BufferedImage hud;
	private BufferedImage background;
	private int currentBackgroundID = 0;
	
	
	public SimState(Simulation simulation) {
		super(simulation);
		this.uiManager = new UIManager(simulation);
		//this.handler.getMouseManager().setUIManager(this.uiManager);
		this.keyManager = new KeyManager();
		network = new Network(simulation);
		NetworkComputing.computeCellsPosition(network);
		
		// Buttons ==============================================================================================
		
		stepByStep = new UITextButton(Assets.buttonXStart+(Assets.buttonSpacing+Assets.buttonW)*1, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, "Next step", new ClickListener(){
			@Override
			public void onClick() {
				step++;
				NetworkComputing.computeEvolution(network);
				NetworkComputing.evolve(network);
			}
		});
		stepByStep.switchActivable();
		playPause = new UIImageButton(Assets.buttonXStart, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, Assets.pauseIdle, Assets.pauseActive, Assets.playIdle, Assets.playActive, new ClickListener(){
			@Override
			public void onClick() {
				switchPause();
				stepByStep.switchActivable();
			}
		});
		this.uiManager.addObject(playPause);
		this.uiManager.addObject(stepByStep);
		this.uiManager.addObject(new UIImageButton(Assets.buttonXStart+(Assets.buttonSpacing+Assets.buttonW)*2, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, Assets.restartIdle, Assets.restartActive, new ClickListener(){
			@Override
			public void onClick() {
				restartNetwork();
			}
		}));
		this.uiManager.addObject(new UITextButton(Assets.buttonXStart+(Assets.buttonSpacing+Assets.buttonW)*3, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, "Real-time", new ClickListener(){
			@Override
			public void onClick() {
				simSpeed = 1;
				if (paused) {
					stepByStep.switchActivable();
					switchPause();
					playPause.switchMode();
				}
			}
		}));
		this.uiManager.addObject(new UIImageButton(Assets.buttonXStart+(Assets.buttonSpacing+Assets.buttonW)*4, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, Assets.fastIdle, Assets.fastActive, new ClickListener(){
			@Override
			public void onClick() {
				simSpeed = 20;
				if (paused) {
					stepByStep.switchActivable();
					switchPause();
					playPause.switchMode();
				}
			}
		}));
		this.uiManager.addObject(new UIImageButton(Assets.buttonXStart+(Assets.buttonSpacing+Assets.buttonW)*5, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, Assets.fastFastIdle, Assets.fastFastActive, new ClickListener(){
			@Override
			public void onClick() {
				simSpeed = 100;
				if (paused) {
					stepByStep.switchActivable();
					switchPause();
					playPause.switchMode();
				}
			}
		}));
		this.uiManager.addObject(new UIImageButton(Assets.buttonXStart+(Assets.buttonSpacing+Assets.buttonW)*6, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, Assets.fastFastFastIdle, Assets.fastFastFastActive, new ClickListener(){
			@Override
			public void onClick() {
				simSpeed = 2000;
				if (paused) {
					stepByStep.switchActivable();
					switchPause();
					playPause.switchMode();
				}
			}
		}));
		this.uiManager.addObject(new UITextButton(Assets.buttonXStart+(Assets.buttonSpacing+Assets.buttonW)*7, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, "Max", new ClickListener(){
			@Override
			public void onClick() {
				simSpeed = 100000;
				if (paused) {
					stepByStep.switchActivable();
					switchPause();
					playPause.switchMode();
				}
			}
		}));
		
		// Bottom buttons ============================================================================================
		colorOn = new UITextSwitch(Assets.buttonXStart, simulation.getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Color ON", "Color OFF", network.getDrawColors(), new ClickListener(){
			@Override
			public void onClick() {
				colorOn.switchIt();
				currentBackgroundID = (currentBackgroundID+4)%8;
				currentDisplay = networkDisplays[currentBackgroundID];
			}
		});
		wireOn = new UITextSwitch(Assets.buttonXStart+Assets.buttonW+Assets.buttonSpacing, simulation.getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Wire ON", "Wire OFF", network.getDrawWire(), new ClickListener(){
			@Override
			public void onClick() {
				wireOn.switchIt();
				currentBackgroundID = (currentBackgroundID-currentBackgroundID%4)+(currentBackgroundID+2)%4;
				currentDisplay = networkDisplays[currentBackgroundID];
			}
		});
		idOn = new UITextSwitch(Assets.buttonXStart+(Assets.buttonW+Assets.buttonSpacing)*2, simulation.getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "IDs ON", "IDs OFF", network.getDrawRoadID(), new ClickListener(){
			@Override
			public void onClick() {
				idOn.switchIt();
				currentBackgroundID = (currentBackgroundID-currentBackgroundID%2)+(currentBackgroundID+1)%2;
				currentDisplay = networkDisplays[currentBackgroundID];
			}
		});
		centersOn = new UITextSwitch(Assets.buttonXStart+(Assets.buttonW+Assets.buttonSpacing)*3, simulation.getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Centers ON", "Centers OFF", network.getDrawCenters(), new ClickListener(){
			@Override
			public void onClick() {
				centersOn.switchIt();
				network.switchDrawCenters();
			}
		});
		this.uiManager.addObject(colorOn);
		this.uiManager.addObject(wireOn);
		this.uiManager.addObject(idOn);
		this.uiManager.addObject(centersOn);
	}
	public void init() {
		// Rendering background ---------------------------------------------------
		background = new BufferedImage(simulation.getWidth(), simulation.getHeight(), BufferedImage.TYPE_INT_RGB);
		networkDisplays = new BufferedImage[8];
		hud = new BufferedImage(simulation.getWidth(), simulation.getHeight(), BufferedImage.TYPE_INT_ARGB);
		renderBG(network, networkDisplays);
		// ------------------------------------------------------------------------
		
		lastTick = System.nanoTime();
		
	}
	public void renderBG(Network network, BufferedImage[] backgrounds) {
		Graphics2D g = background.createGraphics();
		g.setColor(Assets.bgCol);
		g.fillRect(0, 0, simulation.getWidth(), simulation.getHeight());
		g.dispose();
		NetworkRendering.renderButtonsHeader(network, hud);
		networkDisplays = NetworkRendering.renderAllBGs(network, backgrounds);
		currentBackgroundID = 0;
		if (network.getDrawRoadID()) {
			currentBackgroundID += 1;
		}
		if (network.getDrawWire()) {
			currentBackgroundID += 2;
		}
		if (network.getDrawColors()) {
			currentBackgroundID += 4;
		}
		currentDisplay = this.networkDisplays[currentBackgroundID];
	}
	
	public void tick(int n) {
		
		if (System.nanoTime()-lastTick >= 1000000000/simSpeed) {
			
			if (simSpeed >= 5000) {
				while((System.nanoTime()-lastTick) <= 1000000000/60) {
					step++;
					NetworkComputing.computeEvolution(network);
					NetworkComputing.evolve(network);
				}
			} else {
				for (int i=0 ; i<n ; i++) {
					step++;
					NetworkComputing.computeEvolution(network);
					NetworkComputing.evolve(network);
				}
			}
			lastTick = System.nanoTime();
		}
		uiManager.tick();
		keyManager.tick();
		
		// Offset keys ============================================================================
		if (simulation.getKeyManager().right) {
			network.setxOffset(network.getxOffset()-offsetSpeed*Math.cos(network.getRotation()));
			network.setyOffset(network.getyOffset()+offsetSpeed*Math.sin(network.getRotation()));
		} else if (simulation.getKeyManager().left) {
			network.setxOffset(network.getxOffset()+offsetSpeed*Math.cos(network.getRotation()));
			network.setyOffset(network.getyOffset()-offsetSpeed*Math.sin(network.getRotation()));
		}
		if (simulation.getKeyManager().up) {
			network.setxOffset(network.getxOffset()+offsetSpeed*Math.sin(network.getRotation()));
			network.setyOffset(network.getyOffset()+offsetSpeed*Math.cos(network.getRotation()));
		} else if (simulation.getKeyManager().down) {
			network.setxOffset(network.getxOffset()-offsetSpeed*Math.sin(network.getRotation()));
			network.setyOffset(network.getyOffset()-offsetSpeed*Math.cos(network.getRotation()));
		}
		if (simulation.getKeyManager().space) {
			network.setxOffset(network.getxDefaultOffset());
			network.setyOffset(network.getyDefaultOffset());
		}
		
		// Rotation keys ==========================================================================
		if (simulation.getKeyManager().d) {
			network.setRotation(network.getRotation()+rotationSpeed);
		} else if (simulation.getKeyManager().a) {
			network.setRotation(network.getRotation()-rotationSpeed);
		}
		if (simulation.getKeyManager().s) {
			network.setRotation(0);
		}
		//System.out.println("x:" + network.getxOffset() + ", y:" + network.getyOffset() + ", a:" + network.getRotation());
	}
	public void tick() {
		this.uiManager.tick();
	}
	
	public void render(Graphics g) {
		
		g.drawImage(background, 0, 0, null);
		Graphics2D gg = (Graphics2D) g.create();
		gg.translate(network.getxOffset(), network.getyOffset());
		gg.rotate(network.getRotation(), simulation.getWidth()/2-network.getxOffset(), simulation.getHeight()/2-network.getyOffset());
		gg.drawImage(currentDisplay, 0, 0, null);
		g.drawImage(hud, 0, 0, null);
		NetworkRendering.render(network, gg);
		NetworkRendering.renderInformations(network, g);
		gg.dispose();
		uiManager.render(g);
		
		
		/*g.setColor(Assets.worldCol);
		g.fillRect(0, 0, this.handler.getGame().getWidth(), this.handler.getGame().getHeight());
		this.uiManager.render(g);
		Text.drawString(g, "Dona the Sworm", Assets.vert1Col, handler.getWidth()/2, handler.getGame().getTitleY(), true, Assets.fontLarge);
		Text.drawString(g, handler.getGame().getVersionID(), Color.gray, 10, handler.getHeight()-10, false, Assets.fontNormal);
		Text.drawString(g, "by LutziLutz", Color.gray, handler.getWidth() - 120, handler.getHeight()-10, false, Assets.fontNormal);*/
	}
	public void restartNetwork() {
		step = 0;
		network = new Network(simulation);
		NetworkComputing.computeCellsPosition(network);
	}
	
	// Return time in format "DDD hh:mm:ss"
	public String getTime() {
		int time = (int) (step*stepSize);
		int sec = time % 60;
		int min = ((time - sec)/60) % 60;
		int hr = ((((time - sec)/60) - min)/60) % 24;
		
		String secStr = Integer.toString(sec);
		if (sec < 10) {
			secStr = "0" + secStr;
		}
		
		String minStr = Integer.toString(min);
		if (min < 10) {
			minStr = "0" + minStr;
		}
		
		String hrStr = Integer.toString(hr);
		if (hr < 10) {
			hrStr = "0" + hrStr;
		}
		
		String dayStr = daysOfWeek[(time/(60*60*24)) % 7];
		
		return dayStr + " " + hrStr + ":" + minStr + ":" + secStr;
	}
	public UIManager getUIManager() {
		return this.uiManager;
	}
	public double getStepSize() {
		return stepSize;
	}
	public int getStep() {
		return step;
	}
	public boolean getPause() {
		return paused;
	}
	public void switchPause() {
		paused = !paused;
	}
	public double getSimSpeed() {
		return this.simSpeed;
	}
	public void disableUIManager() {
		simulation.getMouseManager().setUIManager(null);
	}
	public void enableUIManager() {
		simulation.getMouseManager().setUIManager(this.uiManager);
	}
}
