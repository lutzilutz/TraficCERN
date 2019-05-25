package main;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import graphics.Assets;
import graphics.Display;
import input.KeyManager;
import input.MouseManager;
import states.MenuState;
import states.SettingsState;
import states.SimState;
import states.State;
import ui.UIManager;
import utils.Utils;

public class Simulator implements Runnable {

	// Display
	private Display display; // the window in itself
	private String title; // program title
	private String versionID; // version ID of the program
	private int width, height; // width and height of screen in [pixels]
	
	// Thread
	private boolean running = false; // true if thread should be alive
	private Thread thread; // the main thread of the program
	
	// Graphics
	private BufferStrategy bs; // current buffer strategy (see official documentation)
	private Graphics g; // main Graphics object on which to draw
	
	// Mouse, keyboard and UI managers
	private MouseManager mouseManager;
	private KeyManager keyManager;
	private UIManager uiManager;
	
	// States (in order of execution)
	private MenuState menuState; // state of the program when it's on the main menu
	private SettingsState settingsState; // state of the program when it's on the settings menu
	private SimState simState; // state of the program when it runs simulations
	
	// Constructor
	public Simulator(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.versionID = "v 1.0.1";
		
		// initialize mouse, keyboard and UI managers
		this.uiManager = new UIManager(this);
		this.mouseManager = new MouseManager();
		this.mouseManager.setUIManager(this.uiManager);
		this.keyManager = new KeyManager();
	}
	
	// Initialize the windows, the states, and the user interface managers
	private void init() {
		
		// init the log output and all the assets
		Utils.initLog();
		Assets.init();
		
		// instantiate the display and link it with mouse and keyboard listeners
		display = new Display(title,width,height);
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		
		// instantiate all states of the program
		simState = new SimState(this);
		settingsState = new SettingsState(this);
		menuState = new MenuState(this);
		
		// go to the menu state
		mouseManager.setUIManager(menuState.getUIManager());
		State.setState(menuState);
		
		// log the end of the initialization
		Utils.logln("Running --------------------------------------------------------------");
		
	}
	
	// Tick method, ticks 1 time no matter the value of "n"
	private void tick(int n) {
		
		// tick the current state it it exists
		if (State.getState() != null) {
			State.getState().tick(n);
		}
		
		// the the keyboard manager
		keyManager.tick();
		
	}
	
	// Render method, create the environment and render the actual state if exists
	private void render() {
		
		// prepare the buffer strategy with value 3 (see official documentation)
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		
		// retrieve the Graphics object
		g = bs.getDrawGraphics();
		
		// Clear screen
		g.clearRect(0, 0, this.width, this.height);
		
		// Start drawing =========================================
		
		if (State.getState() != null) {
			State.getState().render(g);
		}
		
		// End drawing ===========================================
		
		// display and dispose the Graphics object
		bs.show();
		g.dispose();
		
	}
	
	// Run method, called when the thread start()
	@Override
	public void run() {
		
		// call the initialization method
		init();
		
		int fps = 60; // number of frame per second
		double timePerTick = 1000000000 / fps; // time corresponding to 1 tick, in [nanoseconds/tick]
		double delta = 0; // delay since last tick, in [number of ticks]
		long now; // current clock in [nanoseconds]
		long lastTime = System.nanoTime(); // used to compute delay in [nanoseconds]
		
		while(running) {
			
			// compute actual delay since last tick
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			lastTime = System.nanoTime();
			
			// if one tick of delay has passed
			if (delta >= 1) {
				
				// compute number of ticks to do, and tick
				int tickNumber = (int) Math.max(1, simState.getSimSpeed()/fps);
				tick(tickNumber);
				
				render(); // call the render method
				Toolkit.getDefaultToolkit().sync(); // used to avoid graphics lag (see official documentation)
				delta--;
			}
			
			// try to sleep until next tick
			try {
				Thread.sleep(Math.max(0, (int) ((now - System.nanoTime() + timePerTick) / 1000000)));
			} catch (InterruptedException e) {
				Utils.logErrorln("Couldn't sleep in Simulator.run()");
				Utils.log(e);
			}
		}
		
		// stop the program
		stop();
	}
	
	// Start method, first one to be called for the current thread
	public synchronized void start() {
		
		// if already running, exit method
		if (running) {
			return;
		}
		
		// start a new thread
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	// Stop method, stop the current thread
	private synchronized void stop() {
		
		// thread has already been stopped
		if (!running) {
			return;
		}
		
		// stop the thread
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			Utils.logErrorln("Couldn't join() thread in Simulator.stop()");
			Utils.log(e);
		}
	}
	
	// Getters & setters ====================================================================================
	public void setSimState(SimState simState) {
		this.simState = simState;
	}
	public SimState getSimState() {
		return this.simState;
	}
	public SettingsState getSettingsState() {
		return this.settingsState;
	}
	public MenuState getMenuState() {
		return this.menuState;
	}
	public MouseManager getMouseManager() {
		return mouseManager;
	}
	public KeyManager getKeyManager() {
		return keyManager;
	}
	public Display getDisplay() {
		return this.display;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	public String getVersionID() {
		return this.versionID;
	}
}
