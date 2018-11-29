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
import ui.UITextSwitch;
import utils.Utils;

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
	
	private UITextButton network2, network3;
	private UISlider sizeOfNetwork, globalMultiplier;
	private UITextSwitch numOrProba, minMaxTransfer;
	
	public MenuState(Simulation simulation) {
		super(simulation);
		this.uiManager = new UIManager(simulation);
		
		// Play button ==============================================================================================
		
		network2 = new UITextButton(xStart,  yStart+0*(buttonHeight+buttonYMargin), buttonWidth, buttonHeight, Network.getTitle(1), new ClickListener(){
			@Override
			public void onClick() {
				launchSimulation(1);
			}
		});
		this.uiManager.addObject(network2);
		
		network3 = new UITextButton(xStart, yStart+1*(buttonHeight+buttonYMargin), buttonWidth, buttonHeight, Network.getTitle(2), new ClickListener(){
			@Override
			public void onClick() {
				launchSimulation(2);
			}
		});
		this.uiManager.addObject(network3);
		
		sizeOfNetwork = new UISlider(simulation, xStart, yStart+4*(sliderHeight+buttonYMargin), sliderWidth, "Size of network", 3, 1, 2, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(sizeOfNetwork);
		
		numOrProba = new UITextSwitch(xStart, yStart+5*(sliderHeight+buttonYMargin), buttonWidth, buttonHeight, "Use numerical", "Use probabilities", 1, new ClickListener(){
			@Override
			public void onClick() {
				numOrProba.switchIt();
				DataManager.switchNumProba();
			}
		});
		this.uiManager.addObject(numOrProba);
		
		globalMultiplier = new UISlider(simulation, xStart, yStart+6*(sliderHeight+buttonYMargin), sliderWidth, "Additionnal flow", 100, 0, (int) (DataManager.globalFlowMultiplier*100 - 100), true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		minMaxTransfer = new UITextSwitch(xStart, yStart+7*(sliderHeight+buttonYMargin), buttonWidth, buttonHeight, "No transfer", "Min transfer", "Max transfer", 0, new ClickListener(){
			@Override
			public void onClick() {
				minMaxTransfer.switchIt();
				DataManager.transfers = minMaxTransfer.getChosenArg();
			}
		});
		this.uiManager.addObject(new UITextButton(xStart, yStart+9*(buttonHeight+buttonYMargin), buttonWidth, buttonHeight, "Exit", new ClickListener(){
			@Override
			public void onClick() {
				System.exit(0);
			}
		}));
	}
	// launch the simulation #nSimulation, depending on probabilities/numerical values
	public void launchSimulation(int nSimulation) {
		
		// prevents user to continue clicking after state change
		disableUIManager();
		
		// create a new SimState
		simulation.setSimState(new SimState(simulation));
		simulation.getSimState().setNetwork(new Network(simulation, nSimulation, simulation.getMenuState().getSizeOfNetwork().getCurrentValue()));
		simulation.getSimState().init();
		
		if (DataManager.useProbabilities) {
			Utils.initAllData();
			DataManager.applyDataProba(simulation);
			simulation.getSimState().enableUIManager();
			State.setState(simulation.getSimState());
		} else {
			simulation.getSimSettingsState().enableUIManager();
			State.setState(simulation.getSimSettingsState());
		}
	}
	public void tick(int n) {
		if (DataManager.useProbabilities && !this.uiManager.getObjects().contains(globalMultiplier)) {
			this.uiManager.addObject(globalMultiplier);
		} else if (!DataManager.useProbabilities && this.uiManager.getObjects().contains(globalMultiplier)) {
			this.uiManager.removeObject(globalMultiplier);
		}
		if (DataManager.useProbabilities && !this.uiManager.getObjects().contains(minMaxTransfer)) {
			this.uiManager.addObject(minMaxTransfer);
		} else if (!DataManager.useProbabilities && this.uiManager.getObjects().contains(minMaxTransfer)) {
			this.uiManager.removeObject(minMaxTransfer);
		}
		this.uiManager.tick();
		
		DataManager.globalFlowMultiplier = (float) (globalMultiplier.getCurrentValue()+100) / 100.0;
		//System.out.println(DataManager.globalFlowMultiplier);
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
		
		if (network2.isHovering()) {
			Text.drawString(g, Network.getDescription(1), Assets.textCol, xStart+buttonWidth+descriptionMargin, yStart+0*(buttonHeight+buttonYMargin)+buttonHeight/2+5, false, Assets.normalFont);
		} else {
			Text.drawString(g, Network.getDescription(1), Assets.idleCol, xStart+buttonWidth+descriptionMargin, yStart+0*(buttonHeight+buttonYMargin)+buttonHeight/2+5, false, Assets.normalFont);
		}
		
		if (network3.isHovering()) {
			Text.drawString(g, Network.getDescription(2), Assets.textCol, xStart+buttonWidth+descriptionMargin, yStart+1*(buttonHeight+buttonYMargin)+buttonHeight/2+5, false, Assets.normalFont);
		} else {
			Text.drawString(g, Network.getDescription(2), Assets.idleCol, xStart+buttonWidth+descriptionMargin, yStart+1*(buttonHeight+buttonYMargin)+buttonHeight/2+5, false, Assets.normalFont);
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
