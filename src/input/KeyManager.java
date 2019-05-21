package input;
// Java
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {

	private boolean[] keys; // list of keys, true if being used, else false
	public boolean w,a,s,d,up,down,left,right,space,escape,enter; // fiels for keys being used in the simulator
	private int keysCounter; // counter of the number of keys being used

	// Constructor
	public KeyManager() {
		keys = new boolean[256];
		keysCounter = 0;
	}
	
	// Tick method, update the booleans of all used keys
	public void tick() {
		w = keys[KeyEvent.VK_W];
		a = keys[KeyEvent.VK_A];
		s = keys[KeyEvent.VK_S];
		d = keys[KeyEvent.VK_D];
		up = keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_DOWN];
		left = keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_RIGHT];
		space = keys[KeyEvent.VK_SPACE];
		escape = keys[KeyEvent.VK_ESCAPE];
		enter = keys[KeyEvent.VK_ENTER];
	}
	
	// If a key is pressed, register it inside "keys" and update counter
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()<256) {
			if (keys[e.getKeyCode()] == false) {
				keysCounter += 1;
			}
			keys[e.getKeyCode()] = true;
		}
	}
	
	// If a key is released, register it inside "keys" and update counter
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode()<256) {
			if (keys[e.getKeyCode()] == true) {
				keysCounter -= 1;
			}
			keys[e.getKeyCode()] = false;
		}
	}
	
	// If a key is typed ... do nothing
	public void keyTyped(KeyEvent e) {
		
	}
	
	// Getters & setters ====================================================================================
	public int getKeysCounter() {
		return this.keysCounter;
	}
}
