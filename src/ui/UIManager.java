package ui;
// Java
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import main.Simulator;

public class UIManager {

	private Simulator simulator;
	
	private ArrayList<UIObject> objects;
	
	public UIManager(Simulator simulator) {
		this.simulator = simulator;
		this.objects = new ArrayList<UIObject>();
	}
	
	// tick all objects of the manager
	public void tick() {
		for (UIObject o : this.objects) {
			o.tick();
		}
	}
	
	// render all objects of the manager
	public void render(Graphics g) {
		for (UIObject o : this.objects) {
			o.render(g);
		}
	}
	
	// check mouse move for all objects of the manager
	public void onMouseMove(MouseEvent e) {
		for (UIObject o : this.objects) {
			o.onMouseMove(e);
		}
	}
	
	// check mouse releas on all objects of the manager
	public void onMouseRelease(MouseEvent e) {
		for (UIObject o : this.objects) {
			o.onMouseRelease(e);
		}
	}
	
	// add a new object to the manager
	public void addObject(UIObject o) {
		this.objects.add(o);
	}
	
	// remove the given object of the manager
	public void removeObject(UIObject o) {
		this.objects.remove(o);
	}

	// Getters & setters ====================================================================================
	public Simulator getSimulation() {
		return this.simulator;
	}
	public ArrayList<UIObject> getObjects() {
		return objects;
	}
	public void setObjects(ArrayList<UIObject> objects) {
		this.objects = objects;
	}
}
