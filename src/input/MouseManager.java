package input;
// Java
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

// Own
import ui.UIManager;

public class MouseManager implements MouseListener, MouseMotionListener {

	private UIManager uiManager; // link to the UI manager
	private boolean leftPressed, rightPressed; // if left and right mouse button are being pressed
	private int mouseX, mouseY; // coordinates of the cursor on the screen
	
	// Constructor
	public MouseManager() {
		
	}
	
	// If a mouse button is pressed, register it
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			leftPressed = true;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			rightPressed = true;
		}
	}
	
	// If a mouse button is released, register it
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			leftPressed = false;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			rightPressed = false;
		}
		if (this.uiManager != null) {
			this.uiManager.onMouseRelease(e);
		}
		if (this.uiManager != null) {
			this.uiManager.onMouseMove(e);
		}
	}
	
	// If the mouse moves, register the new location
	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		if (this.uiManager != null) {
			this.uiManager.onMouseMove(e);
		}
	}
	
	// If the user drags the mouse, register the new location
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	// Getters & setters ====================================================================================
	public void setUIManager(UIManager uiManager) {
		this.uiManager = uiManager;
	}
	public boolean isLeftPressed() {
		return leftPressed;
	}
	public boolean isRightPressed() {
		return rightPressed;
	}
	public int getMouseX() {
		return mouseX;
	}
	public int getMouseY() {
		return mouseY;
	}	
}
