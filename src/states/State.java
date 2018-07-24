package states;

import java.awt.Graphics;

import main.Simulation;

public abstract class State {

	protected Simulation simulation;
	
	// State Manager
	private static State currentState = null;
	
	public State(Simulation simulation) {
		this.simulation = simulation;
	}
	
	public static void setState(State state) {
		currentState = state;
	}
	public static State getState() {
		return currentState;
	}
	
	// State
	public abstract void tick();
	
	public abstract void tick(int n);
	
	public abstract void render(Graphics g);
	
}
