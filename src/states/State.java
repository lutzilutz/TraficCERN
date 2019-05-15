package states;

import java.awt.Graphics;

import main.Simulator;
import utils.Utils;

public abstract class State {

	protected Simulator simulator;
	
	// State Manager
	private static State currentState = null;
	
	public State(Simulator simulator) {
		this.simulator = simulator;
	}
	
	public static void setState(State state) {
		Utils.logln("  " + state.getClass().getSimpleName() + " --------------");
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
