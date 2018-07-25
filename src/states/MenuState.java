package states;
import java.awt.Graphics;

import graphics.Assets;
import graphics.Text;
import main.Simulation;
import network.Network;
import ui.ClickListener;
import ui.UIManager;
import ui.UITextButton;

public class MenuState extends State {
	
	private UIManager uiManager;
	
	private int xStart = 200;
	private int yStart = 200;
	private int buttonYMargin = 20;
	private int buttonWidth = 100;
	private int buttonHeight = 30;
	private int descriptionMargin = 20;
	
	private UITextButton network1, network2;
	
	private Network tmp;
	
	public MenuState(Simulation simulation) {
		super(simulation);
		this.uiManager = new UIManager(simulation);
		
		// Play button ==============================================================================================
		
		tmp = new Network(simulation, 3);
		network1 = new UITextButton(xStart, yStart, buttonWidth, buttonHeight, tmp.getTitle(), new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				simulation.setSimState(new SimState(simulation));
				simulation.getSimState().enableUIManager();
				simulation.getSimState().setNetwork(new Network(simulation, 3));
				simulation.getSimState().init();
				State.setState(simulation.getSimState());
			}
		});
		this.uiManager.addObject(network1);
		
		tmp = new Network(simulation, 4);
		network2 = new UITextButton(xStart, yStart+(buttonHeight+buttonYMargin), buttonWidth, buttonHeight, tmp.getTitle(), new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				simulation.getSimState().enableUIManager();
				simulation.getSimState().setCurrentNetwork(4);
				simulation.getSimState().setNetwork(new Network(simulation, 4));
				simulation.getSimState().init();
				State.setState(simulation.getSimState());
			}
		});
		this.uiManager.addObject(network2);
		
		this.uiManager.addObject(new UITextButton(xStart, yStart+2*(buttonHeight+buttonYMargin), buttonWidth, buttonHeight, "Exit", new ClickListener(){
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
		
		Text.drawString(g, "Trafic simulation at CERN", Assets.idleCol, simulation.getWidth()/2, 100, true, Assets.largeFont);
		
		tmp = new Network(simulation, 3);
		if (network1.isHovering()) {
			Text.drawString(g, tmp.getDescription(), Assets.textCol, xStart+buttonWidth+descriptionMargin, yStart+buttonHeight/2+5, false, Assets.normalFont);
		} else {
			Text.drawString(g, tmp.getDescription(), Assets.idleCol, xStart+buttonWidth+descriptionMargin, yStart+buttonHeight/2+5, false, Assets.normalFont);
		}
		
		tmp = new Network(simulation, 4);
		if (network2.isHovering()) {
			Text.drawString(g, tmp.getDescription(), Assets.textCol, xStart+buttonWidth+descriptionMargin, yStart+buttonHeight+buttonYMargin+buttonHeight/2+5, false, Assets.normalFont);
		} else {
			Text.drawString(g, tmp.getDescription(), Assets.idleCol, xStart+buttonWidth+descriptionMargin, yStart+buttonHeight+buttonYMargin+buttonHeight/2+5, false, Assets.normalFont);
		}
		
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
