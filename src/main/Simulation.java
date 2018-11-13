package main;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import graphics.Assets;
import graphics.Display;
import input.KeyManager;
import input.MouseManager;
import states.MenuState;
import states.SimSettingsState;
import states.SimState;
import states.State;
import ui.UIManager;
import utils.Utils;

public class Simulation implements Runnable {

	private Display display;
	private String title;
	private String versionID;
	private int width, height;
	
	// Thread
	private boolean running = false;
	private Thread thread;
	
	// Graphics
	private BufferStrategy bs;
	private Graphics g;
	
	// UI
	private MouseManager mouseManager;
	private KeyManager keyManager;
	private UIManager uiManager;
	
	// States
	private SimState simState;
	private MenuState menuState;
	private SimSettingsState simSettingsState;
	
	// User-chosen values
	private int entranceERate;
	
	public Simulation(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.versionID = "v 0.9.2";
		this.uiManager = new UIManager(this);
		this.mouseManager = new MouseManager();
		this.mouseManager.setUIManager(this.uiManager);
		this.keyManager = new KeyManager();
	}
	
	private void init() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		Utils.initLog();
		Utils.log("Started at " + dateFormat.format(date) + " =======================================\n");
		Utils.log("Initialization ---------------------------------------------\n");
		Assets.init();
		
		display = new Display(title,width,height);
		
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		
		simState = new SimState(this);
		simSettingsState = new SimSettingsState(this);
		menuState = new MenuState(this);
		mouseManager.setUIManager(menuState.getUIManager());
		
		Utils.log("Running ----------------------------------------------------\n");
		State.setState(menuState);
	}
	private void tick(int n) {
		
		if (State.getState() != null) {
			State.getState().tick(n);
		}
		keyManager.tick();
		
	}
	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		
		g = bs.getDrawGraphics();
		
		// Clear screen
		g.clearRect(0, 0, this.width, this.height);
		
		// start drawing =========================================
		
		if (State.getState() != null) {
			State.getState().render(g);
		}
		
		// end drawing ===========================================
		
		bs.show();
		g.dispose();
		
	}
	@Override
	public void run() {
		
		init();
		
		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		
		while(running) {
			
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			
			lastTime = System.nanoTime();
			
			if (delta >= 1) {
				int tickNumber = (int) Math.max(1, simState.getSimSpeed()/fps);
				tick(tickNumber);
				
				render();
				Toolkit.getDefaultToolkit().sync();
				delta--;
			}
			
			try {
				Thread.sleep(Math.max(0, (int) ((now - System.nanoTime() + timePerTick) / 1000000)));
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
		
		stop();
	}
	public synchronized void start() {
		if (running) {
			return;
		}
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	public synchronized void stop() {
		if (!running) {
			return;
		}
		
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Getters & setters ====================================================================================
	public int getEntranceERate() {
		return entranceERate;
	}
	public void setEntranceERate(int entranceERate) {
		this.entranceERate = entranceERate;
	}
	public void setSimState(SimState simState) {
		this.simState = simState;
	}
	public SimState getSimState() {
		return this.simState;
	}
	public SimSettingsState getSimSettingsState() {
		return this.simSettingsState;
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
	public void disableUIManager() {
		mouseManager.setUIManager(null);
	}
	public void enableUIManager() {
		mouseManager.setUIManager(this.uiManager);
	}
}
