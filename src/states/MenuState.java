package states;
import java.awt.Graphics;

import data.DataManager;
import graphics.Assets;
import graphics.Text;
import main.Simulation;
import network.Network;
import ui.ClickListener;
import ui.UIManager;
import ui.UISlider;
import ui.UITextButton;

public class MenuState extends State {
	
	private UIManager uiManager;
	
	private int xStart = 180;
	private int yStart = 200;
	private int buttonYMargin = 20;
	private int buttonWidth = 140;
	private int buttonHeight = 30;
	private int descriptionMargin = 20;
	private int sliderHeight = 30;
	private int sliderWidth = 500;
	
	private UITextButton network1, network2, network3;
	private UISlider sizeOfNetwork;
	
	public MenuState(Simulation simulation) {
		super(simulation);
		this.uiManager = new UIManager(simulation);
		
		// Play button ==============================================================================================
		
		network1 = new UITextButton(xStart, yStart, buttonWidth, buttonHeight, Network.getTitle(0), new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				simulation.setSimState(new SimState(simulation));
				simulation.getSimSettingsState().enableUIManager();
				simulation.getSimState().setNetwork(new Network(simulation, 0, simulation.getMenuState().getSizeOfNetwork().getCurrentValue()));
				simulation.getSimState().init();
				State.setState(simulation.getSimSettingsState());
			}
		});
		this.uiManager.addObject(network1);
		
		network2 = new UITextButton(xStart,  yStart+(buttonHeight+buttonYMargin), buttonWidth, buttonHeight, Network.getTitle(1), new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				simulation.setSimState(new SimState(simulation));
				simulation.getSimSettingsState().enableUIManager();
				simulation.getSimState().setNetwork(new Network(simulation, 1, simulation.getMenuState().getSizeOfNetwork().getCurrentValue()));
				simulation.getSimState().init();
				State.setState(simulation.getSimSettingsState());
			}
		});
		this.uiManager.addObject(network2);
		
		network3 = new UITextButton(xStart, yStart+2*(buttonHeight+buttonYMargin), buttonWidth, buttonHeight, Network.getTitle(2), new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				simulation.setSimState(new SimState(simulation));
				simulation.getSimSettingsState().enableUIManager();
				simulation.getSimState().setNetwork(new Network(simulation, 2, simulation.getMenuState().getSizeOfNetwork().getCurrentValue()));
				simulation.getSimState().init();
				State.setState(simulation.getSimSettingsState());
			}
		});
		this.uiManager.addObject(network3);
		
		sizeOfNetwork = new UISlider(simulation, xStart, yStart+4*(sliderHeight+buttonYMargin), sliderWidth, "Size of network", 3, 1, 2, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(sizeOfNetwork);
		
		this.uiManager.addObject(new UITextButton(xStart, yStart+7*(buttonHeight+buttonYMargin), buttonWidth, buttonHeight, "Exit", new ClickListener(){
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
	public void applyNetworkSize() {
		simulation.getSimState().getNetwork().setNetworkSize();
	}
	public void render(Graphics g) {
		g.setColor(Assets.bgCol);
		g.fillRect(0, 0, simulation.getWidth(), simulation.getHeight());

		Text.drawString(g, simulation.getVersionID(), Assets.idleCol, 10, simulation.getHeight()-10, false, Assets.normalFont);
		
		Text.drawString(g, "Trafic simulation at CERN", Assets.idleCol, simulation.getWidth()/2, 100, true, Assets.largeFont);
		
		if (network1.isHovering()) {
			Text.drawString(g, Network.getDescription(0), Assets.textCol, xStart+buttonWidth+descriptionMargin, yStart+buttonHeight/2+5, false, Assets.normalFont);
		} else {
			Text.drawString(g, Network.getDescription(0), Assets.idleCol, xStart+buttonWidth+descriptionMargin, yStart+buttonHeight/2+5, false, Assets.normalFont);
		}
		
		if (network2.isHovering()) {
			Text.drawString(g, Network.getDescription(1), Assets.textCol, xStart+buttonWidth+descriptionMargin, yStart+1*(buttonHeight+buttonYMargin)+buttonHeight/2+5, false, Assets.normalFont);
		} else {
			Text.drawString(g, Network.getDescription(1), Assets.idleCol, xStart+buttonWidth+descriptionMargin, yStart+1*(buttonHeight+buttonYMargin)+buttonHeight/2+5, false, Assets.normalFont);
		}
		
		if (network3.isHovering()) {
			Text.drawString(g, Network.getDescription(2), Assets.textCol, xStart+buttonWidth+descriptionMargin, yStart+2*(buttonHeight+buttonYMargin)+buttonHeight/2+5, false, Assets.normalFont);
		} else {
			Text.drawString(g, Network.getDescription(2), Assets.idleCol, xStart+buttonWidth+descriptionMargin, yStart+2*(buttonHeight+buttonYMargin)+buttonHeight/2+5, false, Assets.normalFont);
		}
		
		this.uiManager.render(g);
	}
	
	// Getters & setters ====================================================================================
	public UISlider getSizeOfNetwork() {
		return this.sizeOfNetwork;
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
