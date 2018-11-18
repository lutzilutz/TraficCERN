package states;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphics.Assets;
import graphics.Text;
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
import utils.Utils;

public class SimState extends State {
	
	private UIManager uiManager;
	private KeyManager keyManager;
	
	private Network network;
	
	private int step = 1; // step counter
	private double stepSize = 1; // duration of one step in seconds-
	private int startHour = 0;
	private boolean paused = false;
	private boolean askExit = false;
	private long lastTick;
	private boolean restarting = false;
	private boolean finished = false;
	
	private boolean rushHours = false;
	
	private boolean leftPressed = false;
	private double clickedX=0, clickedY=0;
	
	private double defaultSimSpeed = 20;
	private double simSpeed = defaultSimSpeed;
	private double offsetSpeed = 1;
	private double offsetSpeedDefault = offsetSpeed;
	private long offsetTime = 0;
	private double rotationSpeed = 0.02;
	
	private UITextButton stepByStep, exitY, exitN;
	private UIImageButton playPause;
	private UITextSwitch colorOn, wireOn, idOn, ridesOn, namesOn, centersOn;
	
	private BufferedImage[] networkDisplays;
	private BufferedImage currentDisplay;
	private BufferedImage hud;
	private BufferedImage background;
	private int currentBackgroundID = 0;
	private int currentNetwork = -1;
	
	public SimState(Simulation simulation) {
		super(simulation);
		this.uiManager = new UIManager(simulation);
		this.keyManager = new KeyManager();
		network = new Network(simulation, currentNetwork, 2);
		
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
		this.uiManager.addObject(new UITextButton(simulation.getWidth()-Assets.buttonW-Assets.buttonXStart, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, "Exit", new ClickListener(){
			@Override
			public void onClick() {
				askExit = !askExit;
				exitY.setVisible(askExit);
				exitN.setVisible(askExit);
			}
		}));
		exitY = new UITextButton(simulation.getWidth()-Assets.buttonW-Assets.buttonXStart, Assets.buttonYStart+Assets.buttonH+Assets.buttonSpacing+20, Assets.buttonW, Assets.buttonH, "Yes", new ClickListener(){
			@Override
			public void onClick() {
				Utils.log("Simulation ends at step " + step + "\n");
				Utils.initAllData();
				disableUIManager();
				simulation.getMenuState().enableUIManager();
				State.setState(simulation.getMenuState());
			}
		});
		exitY.setVisible(false);
		this.uiManager.addObject(exitY);
		
		exitN = new UITextButton(simulation.getWidth()-Assets.buttonW-Assets.buttonXStart, Assets.buttonYStart+Assets.buttonH*2+2*Assets.buttonSpacing+20, Assets.buttonW, Assets.buttonH, "No", new ClickListener(){
			@Override
			public void onClick() {
				askExit = !askExit;
				exitY.setVisible(askExit);
				exitN.setVisible(askExit);
			}
		});
		exitN.setVisible(false);
		this.uiManager.addObject(exitN);
		
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
		ridesOn = new UITextSwitch(Assets.buttonXStart+(Assets.buttonW+Assets.buttonSpacing)*3, simulation.getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Rides ON", "Rides OFF", network.getDrawCenters(), new ClickListener(){
			@Override
			public void onClick() {
				ridesOn.switchIt();
				network.switchDrawRides();
			}
		});
		namesOn = new UITextSwitch(Assets.buttonXStart+(Assets.buttonW+Assets.buttonSpacing)*4, simulation.getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Names ON", "Names OFF", network.getDrawCenters(), new ClickListener(){
			@Override
			public void onClick() {
				namesOn.switchIt();
				network.switchDrawNames();
			}
		});
		centersOn = new UITextSwitch(Assets.buttonXStart+(Assets.buttonW+Assets.buttonSpacing)*5, simulation.getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Centers ON", "Centers OFF", network.getDrawCenters(), new ClickListener(){
			@Override
			public void onClick() {
				centersOn.switchIt();
				network.switchDrawCenters();
			}
		});
		this.uiManager.addObject(colorOn);
		this.uiManager.addObject(wireOn);
		this.uiManager.addObject(idOn);
		this.uiManager.addObject(ridesOn);
		this.uiManager.addObject(namesOn);
		this.uiManager.addObject(centersOn);
	}
	public void init() {
		
		NetworkComputing.computeCellsPosition(network);
		
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
		
		/*if (firstRun) {
			applyUserSettings();
			firstRun = false;
		}*/
		
		if (!getPause() && !restarting) {
			
			if (System.nanoTime()-lastTick >= 1000000000/simSpeed) {
				
				if (simSpeed >= 5000) {
					while((System.nanoTime()-lastTick) <= 1000000000/60 && !restarting) {
						step++;
						if (step >= 86400 && !finished) {
							Utils.saveCheckingValues();
							switchPause();
							finished = true;
							break;
						}
						updateRH();
						NetworkComputing.computeEvolution(network);
						NetworkComputing.evolve(network);
					}
				} else {
					for (int i=0 ; i<n ; i++) {
						step++;
						if (step >= 86400 && !finished) {
							Utils.saveCheckingValues();
							switchPause();
							finished = true;
						}
						updateRH();
						NetworkComputing.computeEvolution(network);
						NetworkComputing.evolve(network);
					}
				}
				lastTick = System.nanoTime();
			}
		}
		
		
		
		uiManager.tick();
		keyManager.tick();
		
		// Offset keys ============================================================================
		if (simulation.getKeyManager().right) {
			network.setxOffset(network.getxOffset()-offsetSpeed*Math.cos(network.getRotation()));
			network.setyOffset(network.getyOffset()+offsetSpeed*Math.sin(network.getRotation()));
			increaseOffset();
		} else if (simulation.getKeyManager().left) {
			network.setxOffset(network.getxOffset()+offsetSpeed*Math.cos(network.getRotation()));
			network.setyOffset(network.getyOffset()-offsetSpeed*Math.sin(network.getRotation()));
			increaseOffset();
		}
		if (simulation.getKeyManager().up) {
			network.setxOffset(network.getxOffset()+offsetSpeed*Math.sin(network.getRotation()));
			network.setyOffset(network.getyOffset()+offsetSpeed*Math.cos(network.getRotation()));
			increaseOffset();
		} else if (simulation.getKeyManager().down) {
			network.setxOffset(network.getxOffset()-offsetSpeed*Math.sin(network.getRotation()));
			network.setyOffset(network.getyOffset()-offsetSpeed*Math.cos(network.getRotation()));
			increaseOffset();
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
		
		// Acceleration of movement
		if (!simulation.getKeyManager().up && !simulation.getKeyManager().down && !simulation.getKeyManager().right && !simulation.getKeyManager().left) {
			offsetTime = 0;
			offsetSpeed = offsetSpeedDefault;
		}
		// Left click to change offset
		if (simulation.getMouseManager().isLeftPressed() && simulation.getMouseManager().getMouseY()>60 && simulation.getMouseManager().getMouseY()<simulation.getHeight()-60) {
			if (!leftPressed) {
				clickedX = simulation.getMouseManager().getMouseX()-network.getxOffset();
				clickedY = simulation.getMouseManager().getMouseY()-network.getyOffset();
				leftPressed = true;
			} else {
				network.setxOffset(0*network.getxOffset() + simulation.getMouseManager().getMouseX()-clickedX);
				network.setyOffset(0*network.getyOffset() + simulation.getMouseManager().getMouseY()-clickedY);
			}
		} else {
			leftPressed = false;
		}
	}
	public void increaseOffset() {
		if (offsetTime == 0) {
			offsetTime = System.nanoTime();
		}
		
		offsetSpeed = Math.max(offsetSpeedDefault, Math.min(5*offsetSpeedDefault, Math.log(100*(System.nanoTime()-offsetTime)/1000000000.0)));
	}
	public void tick() {
		//this.uiManager.tick();
	}
	
	public void render(Graphics g) {
		
		
		if (!restarting) {
			g.drawImage(background, 0, 0, null);
			Graphics2D gg = (Graphics2D) g.create();
			gg.translate(network.getxOffset(), network.getyOffset());
			gg.rotate(network.getRotation(), simulation.getWidth()/2-network.getxOffset(), simulation.getHeight()/2-network.getyOffset());
			gg.drawImage(currentDisplay, 0, 0, null);
			NetworkRendering.render(network, gg);
			NetworkRendering.renderHeaderBG(network, g);
			g.drawImage(hud, 0, 0, null);
			NetworkRendering.renderInformations(network, g);
			gg.dispose();
			uiManager.render(g);
			if (askExit) {
				Text.drawString(g, "Are you sure ?", Assets.idleCol, simulation.getWidth()-(int) (0.5*Assets.buttonW)-Assets.buttonXStart, Assets.buttonYStart+50, true, Assets.normalFont);
			}
		}
	}
	public void restartNetwork() {
		step = 1;
		network.restart();
		Utils.initAllData();
	}
	
	// Return time in format "hh:mm:ss"
	public String getTime() {
		int time = (int) (step*stepSize+startHour*60*60);
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
		
		return hrStr + ":" + minStr + ":" + secStr;
	}
	public int getHours() {
		int time = (int) (step*stepSize+startHour*60*60);
		int sec = time % 60;
		int min = ((time - sec)/60) % 60;
		int hr = ((((time - sec)/60) - min)/60) % 24;
		
		return hr;
	}
	public void updateRH() {
		if ((getHours() >= 7 && getHours() < 10) || (getHours() >= 17 && getHours() < 20)) {
			rushHours = true;
		} else {
			rushHours = false;
		}
	}
	// Getters & setters ====================================================================================
	public boolean isRushHours() {
		return rushHours;
	}
	public void setCurrentNetwork(int currentNetwork) {
		network = null;
		this.currentNetwork = currentNetwork;
		network = new Network(simulation, currentNetwork, simulation.getMenuState().getSizeOfNetwork().getCurrentValue());
		init();
		restartNetwork();
	}
	public void setNetwork(Network network) {
		this.network = network;
	}
	public Network getNetwork() {
		return network;
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
