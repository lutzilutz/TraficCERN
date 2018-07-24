package states;
import java.awt.Graphics;

import graphics.Assets;
import main.Simulation;
import network.Network;
import ui.ClickListener;
import ui.UIManager;
import ui.UITextButton;

public class MenuState extends State {
	
	private UIManager uiManager;
	
	public MenuState(Simulation simulation) {
		super(simulation);
		this.uiManager = new UIManager(simulation);
		//this.handler.getMouseManager().setUIManager(this.uiManager);
		
		// Play button ==============================================================================================
		
		this.uiManager.addObject(new UITextButton(100, 100, 50, 20, "Test 1", new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				simulation.getSimState().enableUIManager();
				simulation.getSimState().setNetwork(new Network(simulation, 3));
				simulation.getSimState().init();
				State.setState(simulation.getSimState());
			}
		}));
		this.uiManager.addObject(new UITextButton(100, 150, 50, 20, "Test 2", new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				simulation.getSimState().enableUIManager();
				simulation.getSimState().setNetwork(new Network(simulation, 4));
				simulation.getSimState().init();
				State.setState(simulation.getSimState());
			}
		}));
		
		this.uiManager.addObject(new UITextButton(100, 200, 50, 20, "Exit", new ClickListener(){
			@Override
			public void onClick() {
				System.exit(0);
			}
		}));
	}
	
	public void tick(int n) {
		this.uiManager.tick();
	}
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Assets.bgCol);
		g.fillRect(0, 0, simulation.getWidth(), simulation.getHeight());
		this.uiManager.render(g);
	}
	public UIManager getUIManager() {
		return this.uiManager;
	}
	public void disableUIManager() {
		simulation.getMouseManager().setUIManager(null);
	}
	public void enableUIManager() {
		simulation.getMouseManager().setUIManager(this.uiManager);
	}
}
