package graphics;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display {

	private JFrame frame; // the windows in itself
	private Canvas canvas; // canvas on which the program will draw Graphics object (Canvas->BufferStrategy->Graphics)
	private String title; // title of the window
	private int width, height; // size of the window in pixels
	
	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		
		createDisplay();
	}
	
	private void createDisplay() {
		
		// initialize the JFrame object
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocation(0, 0);
		frame.setVisible(true);
		
		// initialize the Canvas object
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width,height));
		canvas.setMaximumSize(new Dimension(width,height));
		canvas.setMinimumSize(new Dimension(width,height));
		canvas.setFocusable(false);
		
		// add the canvas to the frame
		frame.add(canvas);
		frame.pack();
	}
	
	// Getters & setters ====================================================================================
	public Canvas getCanvas() {
		return this.canvas;
	}
	public JFrame getFrame() {
		return this.frame;
	}
}
