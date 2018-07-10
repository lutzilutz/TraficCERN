package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import graphics.Assets;
import graphics.Display;
import graphics.Text;
import ui.ClickListener;
import ui.MouseManager;
import ui.UIImageButton;
import ui.UIManager;
import ui.UITextButton;

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
	private double simSpeed = 10;
	
	private Network network;
	
	// Thread
	private boolean running = false;
	private Thread thread;
	
	// Graphics
	private BufferStrategy bs;
	private Graphics g;
	private BufferedImage background;
	
	// UI
	private MouseManager mouseManager;
	private UIManager uiManager;
	private UITextButton stepByStep;
	private UIImageButton playPause;
	
	public Simulation(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.versionID = "v 0.1";
		this.uiManager = new UIManager();
		this.mouseManager = new MouseManager();
		this.mouseManager.setUIManager(this.uiManager);
	}
	
	private void init() {
		
		Assets.init();
		
		network = new Network(this);
		
		// Rendering background ---------------------------------------------------
		background = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = background.createGraphics();
		network.renderBG(g2d);
		g2d.dispose();
		// ------------------------------------------------------------------------
		
		display = new Display(title,width,height);
		
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		
		stepByStep = new UITextButton(Assets.buttonXStart+(Assets.buttonSpacing+Assets.buttonW)*1, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, "Next step", new ClickListener(){
			@Override
			public void onClick() {
				step++;
				network.computeEvolution();
				network.evolve();
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
		this.uiManager.addObject(new UITextButton(Assets.buttonXStart+(Assets.buttonSpacing+Assets.buttonW)*2, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, "Real-time", new ClickListener(){
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
		this.uiManager.addObject(new UIImageButton(Assets.buttonXStart+(Assets.buttonSpacing+Assets.buttonW)*3, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, Assets.fastIdle, Assets.fastActive, new ClickListener(){
			@Override
			public void onClick() {
				simSpeed = 10;
				if (paused) {
					stepByStep.switchActivable();
					switchPause();
					playPause.switchMode();
				}
			}
		}));
		this.uiManager.addObject(new UIImageButton(Assets.buttonXStart+(Assets.buttonSpacing+Assets.buttonW)*4, Assets.buttonYStart, Assets.buttonW, Assets.buttonH, Assets.fastFastIdle, Assets.fastFastActive, new ClickListener(){
			@Override
			public void onClick() {
				simSpeed = 6000;
				if (paused) {
					stepByStep.switchActivable();
					switchPause();
					playPause.switchMode();
				}
			}
		}));
		
	}
	private void tick(int n) {
		
		if (System.nanoTime()-lastTick >= 1000000000/simSpeed) {
			for (int i=0 ; i<n ; i++) {
				step++;
				network.computeEvolution();
				network.evolve();
			}
			//network.display();
			lastTick = System.nanoTime();
		}
		uiManager.tick();
		
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
		
		// start drawing =========
		
		g.drawImage(background, 0, 0, null);
		network.render(g);
		uiManager.render(g);
		renderButtonsHeader(g);
		
		// end drawing ===========
		
		bs.show();
		g.dispose();
		
	}
	public void renderButtonsHeader(Graphics g) {
		g.setColor(Assets.idleCol);
		
		// "Controls"
		g.fillRect(Assets.buttonXStart, Assets.buttonYStart-15, 2, 12);
		g.fillRect(Assets.buttonXStart, Assets.buttonYStart-15, 80, 2);
		Text.drawString(g, "controls", Assets.idleCol, Assets.buttonXStart+Assets.buttonW+Assets.buttonSpacing/2, Assets.buttonYStart-17, true, Assets.normalFont);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*2+Assets.buttonSpacing-80, Assets.buttonYStart-15, 80, 2);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*2+Assets.buttonSpacing-1, Assets.buttonYStart-15, 2, 12);
		
		// "Speed"
		g.fillRect(Assets.buttonXStart+Assets.buttonW*2+Assets.buttonSpacing*2, Assets.buttonYStart-15, 2, 12);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*2+Assets.buttonSpacing*2, Assets.buttonYStart-15, 140, 2);
		Text.drawString(g, "speed", Assets.idleCol, (int) (Assets.buttonXStart+Assets.buttonW*3.5+Assets.buttonSpacing*3), Assets.buttonYStart-17, true, Assets.normalFont);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*5+Assets.buttonSpacing*4-140, Assets.buttonYStart-15, 140, 2);
		g.fillRect(Assets.buttonXStart+Assets.buttonW*5+Assets.buttonSpacing*4-1, Assets.buttonYStart-15, 2, 12);
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
		long timer = 0;
		//int ticks = 0;
		
		while(running) {
			
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			
			timer += now - lastTime; 
			lastTime = System.nanoTime();
			
			if (delta >= 1) {
				if (!paused) {
					int tickNumber = (int) Math.max(1, simSpeed/fps);
					tick(tickNumber);
				}
				render();
				Toolkit.getDefaultToolkit().sync();
				delta--;
				//ticks++;
			}
			
			if (timer >= 1000000000) {
				//System.out.println("FPS : " + ticks);
				//realTicks = ticks;
				//ticks=0;
				timer=0;
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
	// Getters and setters ============================================
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
