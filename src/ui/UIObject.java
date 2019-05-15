package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public abstract class UIObject {

	protected float x,y; // x and y position of the top left corner of the object
	protected int width,height; // size of the object
	protected Rectangle bounds; // bounding box of the object
	protected boolean hovering = false; // if object is hovered by the user cursor
	
	public UIObject(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		bounds = new Rectangle((int) x, (int) y, width, height);
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
	public abstract void onClick();
	
	public void onMouseMove(MouseEvent e) {
		
		// check for the mouse over the object
		if (bounds.contains(e.getX(), e.getY())) {
			hovering = true;
		} else {
			hovering = false;
		}
		
	}
	public void onMouseRelease(MouseEvent e) {
		
		// if user click over a button, disabled hovering flag (avoid graphical bugs)
		if (hovering) {
			onClick();
			hovering = false;
		}
	}
	
	// Getters & setters ====================================================================================	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isHovering() {
		return hovering;
	}
	public void setHovering(boolean hovering) {
		this.hovering = hovering;
	}
}
