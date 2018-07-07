package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import graphics.Display;

public class Simulation implements Runnable {

	private Display display;
	private String title;
	private String versionID;
	private int width, height;
	private int step = 0; // step counter
	private double stepSize = 1; // duration of one step in seconds
	private String[] daysOfWeek = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
	
	private Network network;
	
	// Thread
	private boolean running = false;
	private Thread thread;
	
	// Graphics
	private BufferStrategy bs;
	private Graphics g;
	private BufferedImage background;
	
	public Simulation(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.versionID = "v 0.1";
	}
	
	private void init() {
		
		network = new Network(this);
		
		// Rendering background ---------------------------------------------------
		background = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = background.createGraphics();
		network.renderBG(g2d);
		g2d.dispose();
		// ------------------------------------------------------------------------
		
		display = new Display(title,width,height);
		
	}
	private void tick() {
		
		step++;
		network.computeEvolution();
		network.evolve();
		//network.display();
		
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
		
		// end drawing ===========
		
		bs.show();
		g.dispose();
		
	}
	@Override
	public void run() {
		
		init();
	
		int fps = 10;
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
				tick();
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
}
