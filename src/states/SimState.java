package states;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import data.ExpVarCalculator;
import graphics.Assets;
import graphics.Text;
import input.KeyManager;
import main.Simulator;
import network.Network;
import network.NetworkComputing;
import network.NetworkRendering;
import ui.ClickListener;
import ui.UIImageButton;
import ui.UIManager;
import ui.UITextButton;
import ui.UITextSwitch;
import utils.Defaults;
import utils.Utils;

public class SimState extends State {
	
	// UI and keyboard managers
	private UIManager uiManager;
	private KeyManager keyManager;
	
	private Network network; // network being used
	
	private int step = 1; // step counter
	private double stepSize = 1; // duration of one step in seconds
	private boolean paused = false; // if simulation should be paused
	private boolean askExit = false; // if user clicked on exit button
	private long lastTick; // time of the last tick in [nanoseconds]
	private int numberOfSimulations = 0; // number of simulation to compute
	private int simulationID = 1; // current simulation being computed
	private boolean restarting = false; // if user asked to restart the simulation
	private boolean finished = false; // if all simulations finished
	
	private boolean rushHours = false, rushHoursMorning = false; // if simulation time is a rush hour
	
	private boolean leftPressed = false; // if user have left mouse button pressed
	private double clickedX=0, clickedY=0; // position on-screen of the beginning of a click
	
	private double simSpeed = Defaults.getSimSpeed(); // simulation speed, set by default
	private double offsetSpeedDefault = 1; // defaut speed of the offset
	private double offsetSpeed = offsetSpeedDefault; // current speed of the offset
	private long offsetTime = 0; // start of the offset in [nanoseconds]
	private double rotationSpeed = 0.02; // speed of the rotation
	
	// Top screen buttons
	private UITextButton stepByStep, exitY, exitN;
	private UIImageButton playPause;
	
	// Bottom screen buttons
	private UITextSwitch colorOn, wireOn, idOn, ridesOn, namesOn, centersOn;
	
	private BufferedImage[] networkDisplays; // list of all network images depending on color, wire, ...
	private BufferedImage currentDisplay; // current network image
	private BufferedImage hud; // image of the head-up display (hud)
	private BufferedImage background; // background image
	private int currentBackgroundID = 0; // ID of the current background being displayed
	private int currentNetwork = -1; // ID of the current network, -1 to generate empty network
	
	// ExpVarCalculator for all leaky buckets
	private ExpVarCalculator leakyBucketsEVC_rD884NE;
	private ExpVarCalculator leakyBucketsEVC_rRueDeGeneveSE;
	private ExpVarCalculator leakyBucketsEVC_rRueGermaineTillionSW;
	private ExpVarCalculator leakyBucketsEVC_rC5SW;
	private ExpVarCalculator leakyBucketsEVC_rRouteDeMeyrinSouthNW;
	private ExpVarCalculator leakyBucketsEVC_rRoutePauliSouthNELeft;
	private ExpVarCalculator leakyBucketsEVC_rRoutePauliSouthNERight;
	
	// ExpVarCalculator for all mean times
	private ExpVarCalculator meanTimeSpentEVC_transit;
	private ExpVarCalculator meanTimeSpentEVC_cern;
	
	// ExpVarCalculator for all counters
	private ExpVarCalculator counter1AEVC;
	private ExpVarCalculator counter1BEVC;
	private ExpVarCalculator counter2AEVC;
	private ExpVarCalculator counter2BEVC;
	private ExpVarCalculator counterEntranceBLeftEVC;
	private ExpVarCalculator counterEntranceBRightEVC;
	private ExpVarCalculator counterEntranceELeftEVC;
	private ExpVarCalculator counterEntranceERightEVC;
	private ExpVarCalculator counterEntranceESumEVC;
	
	// state of the writing phase : 0 before, 1 when begins ... , -1 if finished
	private int finalDataWritingState = 0;
	
	public SimState(Simulator simulator) {
		super(simulator);
		
		// Initialize UI and keyboard manager, and create empty network
		this.uiManager = new UIManager(simulator);
		this.keyManager = new KeyManager();
		network = new Network(simulator, currentNetwork, 2);
		
		initButtons();
		
	}
	public void init() {
		
		// comput position of all cells of the network
		NetworkComputing.computeCellsPosition(network);
		
		// Rendering background -----------------------------------------------------------------------------
		background = new BufferedImage(simulator.getWidth(), simulator.getHeight(), BufferedImage.TYPE_INT_RGB);
		networkDisplays = new BufferedImage[8];
		hud = new BufferedImage(simulator.getWidth(), simulator.getHeight(), BufferedImage.TYPE_INT_ARGB);
		renderBG(network, networkDisplays);
		
		// Init output structures ---------------------------------------------------------------------------
		leakyBucketsEVC_rD884NE = new ExpVarCalculator(24*4-1);
		leakyBucketsEVC_rRueDeGeneveSE = new ExpVarCalculator(24*4-1);
		leakyBucketsEVC_rRueGermaineTillionSW = new ExpVarCalculator(24*4-1);
		leakyBucketsEVC_rC5SW = new ExpVarCalculator(24*4-1);
		leakyBucketsEVC_rRouteDeMeyrinSouthNW = new ExpVarCalculator(24*4-1);
		leakyBucketsEVC_rRoutePauliSouthNELeft = new ExpVarCalculator(24*4-1);
		leakyBucketsEVC_rRoutePauliSouthNERight = new ExpVarCalculator(24*4-1);
		meanTimeSpentEVC_transit = new ExpVarCalculator(24);
		meanTimeSpentEVC_cern = new ExpVarCalculator(24);
		counter1AEVC = new ExpVarCalculator(24*60-1);
		counter1BEVC = new ExpVarCalculator(24*60-1);
		counter2AEVC = new ExpVarCalculator(24*60-1);
		counter2BEVC = new ExpVarCalculator(24*60-1);
		counterEntranceBLeftEVC = new ExpVarCalculator(24*60-1);
		counterEntranceBRightEVC = new ExpVarCalculator(24*60-1);
		counterEntranceELeftEVC = new ExpVarCalculator(24*60-1);
		counterEntranceERightEVC = new ExpVarCalculator(24*60-1);
		counterEntranceESumEVC = new ExpVarCalculator(24*60-1);
		
		// save time of current tick
		lastTick = System.nanoTime();
		
	}
	public void initButtons() {
		
		initTopButtons();
		initBottomButtons();
		
	}
	public void initBottomButtons() {

		colorOn = new UITextSwitch(Assets.buttonXStart, simulator.getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Color ON", "Color OFF", Defaults.getDrawColors(), new ClickListener(){
			@Override
			public void onClick() {
				colorOn.switchIt();
				currentBackgroundID = (currentBackgroundID+4)%8;
				currentDisplay = networkDisplays[currentBackgroundID];
			}
		});
		wireOn = new UITextSwitch(Assets.buttonXStart+Assets.buttonW+Assets.buttonSpacing, simulator.getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Wire ON", "Wire OFF", Defaults.getDrawWire(), new ClickListener(){
			@Override
			public void onClick() {
				wireOn.switchIt();
				currentBackgroundID = (currentBackgroundID-currentBackgroundID%4)+(currentBackgroundID+2)%4;
				currentDisplay = networkDisplays[currentBackgroundID];
			}
		});
		idOn = new UITextSwitch(Assets.buttonXStart+(Assets.buttonW+Assets.buttonSpacing)*2, simulator.getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "IDs ON", "IDs OFF", Defaults.getDrawRoadID(), new ClickListener(){
			@Override
			public void onClick() {
				idOn.switchIt();
				currentBackgroundID = (currentBackgroundID-currentBackgroundID%2)+(currentBackgroundID+1)%2;
				currentDisplay = networkDisplays[currentBackgroundID];
			}
		});
		ridesOn = new UITextSwitch(Assets.buttonXStart+(Assets.buttonW+Assets.buttonSpacing)*3, simulator.getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Rides ON", "Rides OFF", Defaults.getDrawCenters(), new ClickListener(){
			@Override
			public void onClick() {
				ridesOn.switchIt();
				Defaults.switchDrawRides();
			}
		});
		namesOn = new UITextSwitch(Assets.buttonXStart+(Assets.buttonW+Assets.buttonSpacing)*4, simulator.getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Names ON", "Names OFF", Defaults.getDrawCenters(), new ClickListener(){
			@Override
			public void onClick() {
				namesOn.switchIt();
				Defaults.switchDrawNames();
			}
		});
		centersOn = new UITextSwitch(Assets.buttonXStart+(Assets.buttonW+Assets.buttonSpacing)*5, simulator.getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Centers ON", "Centers OFF", Defaults.getDrawCenters(), new ClickListener(){
			@Override
			public void onClick() {
				centersOn.switchIt();
				Defaults.switchDrawCenters();
			}
		});
		this.uiManager.addObject(colorOn);
		this.uiManager.addObject(wireOn);
		this.uiManager.addObject(idOn);
		this.uiManager.addObject(ridesOn);
		this.uiManager.addObject(namesOn);
		this.uiManager.addObject(centersOn);
	}
	public void initTopButtons() {
		
		// "Controls" Buttons ===============================================================================

		stepByStep = new UITextButton(Assets.buttonXStart+(Assets.buttonSpacing+Assets.buttonW)*1, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, "Next step", new ClickListener(){
			@Override
			public void onClick() {
				step++;
				NetworkComputing.computeEvolution(network);
				NetworkComputing.evolve(network);
			}
		});
		stepByStep.switchActivable();
		this.uiManager.addObject(stepByStep);
		
		playPause = new UIImageButton(Assets.buttonXStart, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, Assets.pauseIdle, Assets.pauseActive, Assets.playIdle, Assets.playActive, new ClickListener(){
			@Override
			public void onClick() {
				switchPause();
				stepByStep.switchActivable();
			}
		});
		this.uiManager.addObject(playPause);
		
		this.uiManager.addObject(new UIImageButton(Assets.buttonXStart+(Assets.buttonSpacing+Assets.buttonW)*2, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, Assets.restartIdle, Assets.restartActive, new ClickListener(){
			@Override
			public void onClick() {
				restartNetwork();
			}
		}));
		
		// "Speed" buttons ==================================================================================
		
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
		
		// Exit buttons =====================================================================================
		
		this.uiManager.addObject(new UITextButton(simulator.getWidth()-Assets.buttonW-Assets.buttonXStart, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, "Exit", new ClickListener(){
			@Override
			public void onClick() {
				askExit = !askExit;
				exitY.setVisible(askExit);
				exitN.setVisible(askExit);
			}
		}));
		
		exitY = new UITextButton(simulator.getWidth()-Assets.buttonW-Assets.buttonXStart, Assets.buttonYStart+Assets.buttonH+Assets.buttonSpacing+20, Assets.buttonW, Assets.buttonH, "Yes", new ClickListener(){
			@Override
			public void onClick() {
				Utils.logWarningln("User ends simulation prematurely at step " + step);
				Utils.initAllData(numberOfSimulations);
				disableUIManager();
				simulator.getMenuState().enableUIManager();
				State.setState(simulator.getMenuState());
			}
		});
		exitY.setVisible(false);
		this.uiManager.addObject(exitY);
		
		exitN = new UITextButton(simulator.getWidth()-Assets.buttonW-Assets.buttonXStart, Assets.buttonYStart+Assets.buttonH*2+2*Assets.buttonSpacing+20, Assets.buttonW, Assets.buttonH, "No", new ClickListener(){
			@Override
			public void onClick() {
				askExit = !askExit;
				exitY.setVisible(askExit);
				exitN.setVisible(askExit);
			}
		});
		exitN.setVisible(false);
		this.uiManager.addObject(exitN);
	}
	public void renderBG(Network network, BufferedImage[] backgrounds) {
		
		// create Graphics object for the background, and draw on it
		Graphics2D g = background.createGraphics();
		g.setColor(Assets.bgCol);
		g.fillRect(0, 0, simulator.getWidth(), simulator.getHeight());
		g.dispose();
		
		// render head-up display and all network images
		NetworkRendering.renderButtonsHeader(network, hud);
		networkDisplays = NetworkRendering.renderAllBGs(network, backgrounds);
		
		// compute ID of the current network image to use
		currentBackgroundID = 0;
		if (Defaults.getDrawRoadID()) {currentBackgroundID += 1;}
		if (Defaults.getDrawWire()) {currentBackgroundID += 2;}
		if (Defaults.getDrawColors()) {currentBackgroundID += 4;}
		currentDisplay = this.networkDisplays[currentBackgroundID];
	}
	public void networkTick() {
		// check for the end of a day in-sim and not a "finished" flag
		step++;
		if (step >= 86400 && !finished) {
			
			// if more simulations should be computed
			if (simulationID < numberOfSimulations) {
				step = 1;
				simulationID++;
				network.restart();
			}
			// else, it's the last simulation, log it, and switch "paused" and "finished" flags
			else {
				Utils.logInfoln(simulationID + " simulation(s) finished (" + network.artificiallyDestroyedVehicles + " vehicle(s) destroyed artificially)");
				switchPause();
				finished = true;
			}
		}
		
		// tick the two main evolution methods
		NetworkComputing.computeEvolution(network);
		NetworkComputing.evolve(network);
	}
	public void checkFirstTick() {
		
		// if it's the first tick of this SimState, log it
		if (network.getSimulation().getSimState().getStep()==1 && network.getSimulation().getSimState().getSimulationID()==1) {
			Utils.logInfoln("Launch of " + network.getSimulation().getSimState().getNumberOfSimulations() + " simulations");
			Utils.logInfoln("Simulating ...");
		}
	}
	public void keyboardTick() {
		// Offset keys ======================================================================================
		if (simulator.getKeyManager().right) {
			network.setxOffset(network.getxOffset()-offsetSpeed*Math.cos(network.getRotation()));
			network.setyOffset(network.getyOffset()+offsetSpeed*Math.sin(network.getRotation()));
			increaseOffset();
		} else if (simulator.getKeyManager().left) {
			network.setxOffset(network.getxOffset()+offsetSpeed*Math.cos(network.getRotation()));
			network.setyOffset(network.getyOffset()-offsetSpeed*Math.sin(network.getRotation()));
			increaseOffset();
		}
		if (simulator.getKeyManager().up) {
			network.setxOffset(network.getxOffset()+offsetSpeed*Math.sin(network.getRotation()));
			network.setyOffset(network.getyOffset()+offsetSpeed*Math.cos(network.getRotation()));
			increaseOffset();
		} else if (simulator.getKeyManager().down) {
			network.setxOffset(network.getxOffset()-offsetSpeed*Math.sin(network.getRotation()));
			network.setyOffset(network.getyOffset()-offsetSpeed*Math.cos(network.getRotation()));
			increaseOffset();
		}
		if (simulator.getKeyManager().space) {
			network.setxOffset(network.getxDefaultOffset());
			network.setyOffset(network.getyDefaultOffset());
		}
		
		// Rotation keys ====================================================================================
		if (simulator.getKeyManager().d) {
			network.setRotation(network.getRotation()+rotationSpeed);
		} else if (simulator.getKeyManager().a) {
			network.setRotation(network.getRotation()-rotationSpeed);
		}
		if (simulator.getKeyManager().s) {
			network.setRotation(0);
		}
		
		// Acceleration of movement
		if (!simulator.getKeyManager().up && !simulator.getKeyManager().down && !simulator.getKeyManager().right && !simulator.getKeyManager().left) {
			offsetTime = 0;
			offsetSpeed = offsetSpeedDefault;
		}
		// Left click to change offset
		if (simulator.getMouseManager().isLeftPressed() && simulator.getMouseManager().getMouseY()>60 && simulator.getMouseManager().getMouseY()<simulator.getHeight()-60) {
			if (!leftPressed) {
				clickedX = simulator.getMouseManager().getMouseX()-network.getxOffset();
				clickedY = simulator.getMouseManager().getMouseY()-network.getyOffset();
				leftPressed = true;
			} else {
				network.setxOffset(0*network.getxOffset() + simulator.getMouseManager().getMouseX()-clickedX);
				network.setyOffset(0*network.getyOffset() + simulator.getMouseManager().getMouseY()-clickedY);
			}
		} else {
			leftPressed = false;
		}
	}
	public void tick(int n) {
		
		
		if (!getPause() && !restarting && (System.nanoTime()-lastTick >= 1000000000/simSpeed)) {
			
			// if simulation speed is set to be maximum
			if (simSpeed >= 5000) {
				
				// tick this loop to fill a 60th of seconds with maximum number of ticks
				while((System.nanoTime()-lastTick) <= 1000000000/60 && !restarting) {
					
					checkFirstTick();
					networkTick();
				}
			}
			// else, simulation speed has a limit
			else {
				
				// tick until i reach the number of simulation tick that should happen
				for (int i=0 ; i<n ; i++) {
					
					checkFirstTick();
					networkTick();
				}
			}
			// update time to now
			lastTick = System.nanoTime();
		}
		
		// UI and keyboard managers tick
		uiManager.tick();
		keyManager.tick();
		keyboardTick();
		
	}
	public void increaseOffset() {
		if (offsetTime == 0) {
			offsetTime = System.nanoTime();
		}
		offsetSpeed = Math.max(offsetSpeedDefault, Math.min(5*offsetSpeedDefault, Math.log(100*(System.nanoTime()-offsetTime)/1000000000.0)));
	}
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		
		if (!restarting) {
			
			// draw background image
			g.drawImage(background, 0, 0, null);
			Graphics2D gg = (Graphics2D) g.create();
			
			// apply offset
			gg.translate(network.getxOffset(), network.getyOffset());
			gg.rotate(network.getRotation(), simulator.getWidth()/2-network.getxOffset(), simulator.getHeight()/2-network.getyOffset());
			
			// draw network image
			gg.drawImage(currentDisplay, 0, 0, null);
			
			// draw network elements
			NetworkRendering.render(network, gg);
			NetworkRendering.renderHeaderBG(network, g);
			
			// draw HUD
			g.drawImage(hud, 0, 0, null);
			
			// draw simulation infos (bottom-right corner)
			NetworkRendering.renderInformations(network, g);
			gg.dispose();
			
			// draw UI manager
			uiManager.render(g);
			
			// if user asked to exit simulation
			if (askExit) {Text.drawString(g, "Are you sure ?", Assets.idleCol, simulator.getWidth()-(int) (0.5*Assets.buttonW)-Assets.buttonXStart, Assets.buttonYStart+50, true, Assets.normalFont);}
			
			if (finalDataWritingState == 0 && getSimulationID() == getNumberOfSimulations()) {
				// if simulation speed is set to maximum
				if (simSpeed >= 5000) {
					if (getStep() > 86000) {
						incrementWritingState(); // moment at which display "wait" message
					}
				}
				// else if simulation speed is bigger than 2000
				else if (simSpeed >= 2000) {
					if (getStep() > 86300) {
						incrementWritingState(); // moment at which display "wait" message
					}
				}
				// else if simulation speed is bigger than 100
				else if (simSpeed >= 100) {
					if (getStep() > 86390) {
						incrementWritingState(); // moment at which display "wait" message
					}
				}
				// else simulation speed is below 100
				else {
					if (getStep() > 86399) {
						incrementWritingState(); // moment at which display "wait" message
					}
				}
			}
			
			// if data has been written, reset this field
			if (NetworkComputing.writtenFinalData) {
				finalDataWritingState = 0;
			}
			
			// if data writing has begun
			if (finalDataWritingState > 0) {
				g.setColor(Assets.bgAlphaCol);
				g.fillRect(0, 0, 1000, 700);
				g.setColor(Color.white);
				Text.drawString(g, "Please wait, writing output data into files ...", Color.white, network.getSimulation().getWidth()/2, network.getSimulation().getHeight()/2, true, Assets.largeFont);
			}
		}
	}
	public void restartNetwork() {
		step = 1;
		network.restart();
		Utils.initAllData(numberOfSimulations);
	}
	
	// Return time in format "hh:mm:ss"
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
		
		return hrStr + ":" + minStr + ":" + secStr;
	}
	public int getHours() {
		int time = (int) (step*stepSize);
		int sec = time % 60;
		int min = ((time - sec)/60) % 60;
		int hr = ((((time - sec)/60) - min)/60) % 24;
		
		return hr;
	}
	
	// Getters & setters ====================================================================================
	public ExpVarCalculator getLBrD884NE() {
		return this.leakyBucketsEVC_rD884NE;
	}
	public ExpVarCalculator getLBrRueDeGeneveSE() {
		return this.leakyBucketsEVC_rRueDeGeneveSE;
	}
	public ExpVarCalculator getLBrRueGermaineTillionSW() {
		return this.leakyBucketsEVC_rRueGermaineTillionSW;
	}
	public ExpVarCalculator getLBrC5SW() {
		return this.leakyBucketsEVC_rC5SW;
	}
	public ExpVarCalculator getLBrRouteDeMeyrinSouthNW() {
		return this.leakyBucketsEVC_rRouteDeMeyrinSouthNW;
	}
	public ExpVarCalculator getLBrRoutePauliSouthNELeft() {
		return this.leakyBucketsEVC_rRoutePauliSouthNELeft;
	}
	public ExpVarCalculator getLBrRoutePauliSouthNERight() {
		return this.leakyBucketsEVC_rRoutePauliSouthNERight;
	}
	public ExpVarCalculator getMeanTimeSpentTransit() {
		return this.meanTimeSpentEVC_transit;
	}
	public ExpVarCalculator getMeanTimeSpentCERN() {
		return this.meanTimeSpentEVC_cern;
	}
	public ExpVarCalculator getCounter1A() {
		return this.counter1AEVC;
	}
	public ExpVarCalculator getCounter1B() {
		return this.counter1BEVC;
	}
	public ExpVarCalculator getCounter2A() {
		return this.counter2AEVC;
	}
	public ExpVarCalculator getCounter2B() {
		return this.counter2BEVC;
	}
	public ExpVarCalculator getCounterEntranceBLeft() {
		return this.counterEntranceBLeftEVC;
	}
	public ExpVarCalculator getCounterEntranceBRight() {
		return this.counterEntranceBRightEVC;
	}
	public ExpVarCalculator getCounterEntranceELeft() {
		return this.counterEntranceELeftEVC;
	}
	public ExpVarCalculator getCounterEntranceERight() {
		return this.counterEntranceERightEVC;
	}
	public ExpVarCalculator getCounterEntranceESum() {
		return this.counterEntranceESumEVC;
	}
	public int getSimulationID() {
		return this.simulationID;
	}
	public int getNumberOfSimulations() {
		return this.numberOfSimulations;
	}
	public void setNumberOfSimulations(int numberOfSimulations) {
		this.numberOfSimulations = numberOfSimulations;
		this.simulationID = 1;
	}
	public boolean isRushHours() {
		return rushHours;
	}
	public boolean isRushHoursMorning() {
		return rushHoursMorning;
	}
	public void setCurrentNetwork(int currentNetwork) {
		network = null;
		this.currentNetwork = currentNetwork;
		network = new Network(simulator, currentNetwork, simulator.getMenuState().getSizeOfNetwork().getCurrentValue());
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
		simulator.getMouseManager().setUIManager(null);
	}
	public void enableUIManager() {
		simulator.getMouseManager().setUIManager(this.uiManager);
	}
	public int getFinalDataWritingState() {
		return this.finalDataWritingState;
	}
	public void setFinalDataWritingState(int finalDataWritingState) {
		this.finalDataWritingState = finalDataWritingState;
	}
	public void incrementWritingState() {
		this.finalDataWritingState++;
	}
}
