package states;

import java.awt.Graphics;

import main.Simulator;

public abstract class State {

	protected Simulator simulator; // link to the simulator
	
	// State Manager
	private static State currentState = null;
	
	public State(Simulator simulator) {
		this.simulator = simulator;
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
