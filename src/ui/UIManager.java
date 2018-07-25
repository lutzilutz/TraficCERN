package ui;
// Java
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import main.Simulation;

public class UIManager {

	private Simulation simulation;
	
	private ArrayList<UIObject> objects;
	
	public UIManager(Simulation simulation) {
		this.simulation = simulation;
		this.objects = new ArrayList<UIObject>();
	}
	
	public void tick() {
		for (UIObject o : this.objects) {
			o.tick();
		}
	}
	public void render(Graphics g) {
		for (UIObject o : this.objects) {
			o.render(g);
		}
	}
	public void onMouseMove(MouseEvent e) {
		for (UIObject o : this.objects) {
			o.onMouseMove(e);
		}
	}
	public void onMouseRelease(MouseEvent e) {
		for (UIObject o : this.objects) {
			o.onMouseRelease(e);
		}
	}
	public void addObject(UIObject o) {
		this.objects.add(o);
	}
	public void removeObject(UIObject o) {
		this.objects.remove(o);
	}

	// Getters and setters ===============================================
	public Simulation getSimulation() {
		return this.simulation;
	}
	public ArrayList<UIObject> getObjects() {
		return objects;
	}
	public void setObjects(ArrayList<UIObject> objects) {
		this.objects = objects;
	}
	// ===================================================================
}
