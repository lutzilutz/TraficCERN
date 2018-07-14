package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import graphics.Assets;
import graphics.Display;
import input.KeyManager;
import input.MouseManager;
import network.Network;
import network.NetworkComputing;
import network.NetworkRendering;
import ui.ClickListener;
import ui.UIImageButton;
import ui.UIManager;
import ui.UITextButton;
import ui.UITextSwitch;

public class Simulation implements Runnable {

	private Display display;
	private String title;
	private String versionID;
	private int width, height;
	private int step = 0; // step counter
	private double stepSize = 1; // duration of one step in seconds-
	private String[] daysOfWeek = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
	private boolean paused = false;
	private long lastTick;
	private double simSpeed = 20;
	private double offsetSpeed = 0.8;
	
	private Network network;
	
	// Thread
	private boolean running = false;
	private Thread thread;
	
	// Graphics
	private BufferStrategy bs;
	private Graphics g;
	private BufferedImage[] networkDisplays;
	private BufferedImage currentDisplay;
	private BufferedImage hud;
	private BufferedImage background;
	private int currentBackgroundID = 0;
	
	// UI
	private MouseManager mouseManager;
	private KeyManager keyManager;
	private UIManager uiManager;
	private UITextButton stepByStep;
	private UIImageButton playPause;
	private UITextSwitch colorOn, wireOn, idOn, centersOn;
	
	public Simulation(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.versionID = "v 0.1";
		this.uiManager = new UIManager();
		this.mouseManager = new MouseManager();
		this.mouseManager.setUIManager(this.uiManager);
		this.keyManager = new KeyManager();
	}
	
	private void init() {
		
		Assets.init();
		
		network = new Network(this);
		NetworkComputing.computeCellsPosition(network);
		
		// Rendering background ---------------------------------------------------
		background = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		networkDisplays = new BufferedImage[8];
		hud = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		renderBG(network, networkDisplays);
		// ------------------------------------------------------------------------
		
		display = new Display(title,width,height);
		
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		
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
		colorOn = new UITextSwitch(Assets.buttonXStart, getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Color ON", "Color OFF", network.getDrawColors(), new ClickListener(){
			@Override
			public void onClick() {
				colorOn.switchIt();
				currentBackgroundID = (currentBackgroundID+4)%8;
				currentDisplay = networkDisplays[currentBackgroundID];
			}
		});
		wireOn = new UITextSwitch(Assets.buttonXStart+Assets.buttonW+Assets.buttonSpacing, getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Wire ON", "Wire OFF", network.getDrawWire(), new ClickListener(){
			@Override
			public void onClick() {
				wireOn.switchIt();
				currentBackgroundID = (currentBackgroundID-currentBackgroundID%4)+(currentBackgroundID+2)%4;
				currentDisplay = networkDisplays[currentBackgroundID];
			}
		});
		idOn = new UITextSwitch(Assets.buttonXStart+(Assets.buttonW+Assets.buttonSpacing)*2, getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "IDs ON", "IDs OFF", network.getDrawRoadID(), new ClickListener(){
			@Override
			public void onClick() {
				idOn.switchIt();
				currentBackgroundID = (currentBackgroundID-currentBackgroundID%2)+(currentBackgroundID+1)%2;
				currentDisplay = networkDisplays[currentBackgroundID];
			}
		});
		centersOn = new UITextSwitch(Assets.buttonXStart+(Assets.buttonW+Assets.buttonSpacing)*3, getHeight()-Assets.buttonH-20, Assets.buttonW, Assets.buttonH, "Centers ON", "Centers OFF", network.getDrawCenters(), new ClickListener(){
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
	private void tick(int n) {
		
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
		
		if (keyManager.right) {
			network.setxOffset(network.getxOffset()+offsetSpeed);
		} else if (keyManager.left) {
			network.setxOffset(network.getxOffset()-offsetSpeed);
		}
		if (keyManager.up) {
			network.setyOffset(network.getyOffset()-offsetSpeed);
		} else if (keyManager.down) {
			network.setyOffset(network.getyOffset()+offsetSpeed);
		}
		if (keyManager.space) {
			network.setxOffset(network.getxDefaultOffset());
			network.setyOffset(network.getyDefaultOffset());
		}
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
		g.drawImage(background, 0, 0, null);
		g.drawImage(currentDisplay, (int) (network.getxOffset()), (int) (network.getyOffset()), null);
		g.drawImage(hud, 0, 0, null);
		NetworkRendering.render(network, g);
		uiManager.render(g);
		// end drawing ===========================================
		
		bs.show();
		g.dispose();
		
	}
	public void renderBG(Network network, BufferedImage[] backgrounds) {
		Graphics2D g = background.createGraphics();
		g.setColor(Assets.bgCol);
		g.fillRect(0, 0, width, height);
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
	@Override
	public void run() {
		
		init();
		
		lastTick = System.nanoTime();
	
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
				if (!paused) {
					int tickNumber = (int) Math.max(1, simSpeed/fps);
					tick(tickNumber);
				}
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
	public void restartNetwork() {
		step = 0;
		network = new Network(this);
		NetworkComputing.computeCellsPosition(network);
	}
	// Getters and setters ============================================
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
	public void disableUIManager() {
		mouseManager.setUIManager(null);
	}
	public void enableUIManager() {
		mouseManager.setUIManager(this.uiManager);
	}
}
