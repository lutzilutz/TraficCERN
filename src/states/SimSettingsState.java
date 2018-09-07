package states;
import java.awt.Graphics;

import data.DataManager;
import graphics.Assets;
import graphics.Text;
import main.Simulation;
import ui.ClickListener;
import ui.UIManager;
import ui.UISlider;
import ui.UISliderDouble;
import ui.UITextButton;
import utils.Utils;

public class SimSettingsState extends State {
	
	private UIManager uiManager;
	
	private int xStart = 300;
	private int yStart = 100;
	private int buttonYMargin = 5;
	private int buttonWidth = 140;
	private int buttonHeight = 30;
	private int sliderWidth = 140;
	private int sliderHeight = 30;
	//private int descriptionMargin = 20;
	private boolean isLeftPressed = false;
	
	private UISlider fromFrToGe,fromFrToGeDuringRH;
	private UISliderDouble fromFrToGeRepartition;
	
	private UISlider toEntranceE;
	private UISliderDouble toEntranceERepartition, toEntranceERepartitionRH;
	private UITextButton run, back;
	
	public SimSettingsState(Simulation simulation) {
		super(simulation);
		this.uiManager = new UIManager(simulation);
		
		fromFrToGe = new UISlider(simulation, xStart, yStart, 550, "Flow from France to Geneva (vhc/day)", 50000, (int) (DataManager.getFromFrToGe()), false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(fromFrToGe);
		fromFrToGeRepartition = new UISliderDouble(simulation, xStart, yStart+1*(sliderHeight+buttonYMargin), 550, "Coming from Thoiry, St-Genis and Ferney", 100, DataManager.nFrGe_fromSW, DataManager.nFrGe_fromSW+DataManager.nFrGe_fromNW, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(fromFrToGeRepartition);
		
		fromFrToGeDuringRH = new UISlider(simulation, xStart, yStart+2*(sliderHeight+buttonYMargin), 550, "Quantity during rush-hours", 100, 70, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(fromFrToGeDuringRH);
		
		// ==================================================================================================
		
		toEntranceE = new UISlider(simulation, xStart, yStart+4*(sliderHeight+buttonYMargin), 550, "Flow to Entrance E during rush-hours", 4000, DataManager.nToE, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(toEntranceE);
		
		toEntranceERepartition = new UISliderDouble(simulation, xStart, yStart+5*(sliderHeight+buttonYMargin), 550, "Coming from Thoiry, St-Genis and Ferney", 100, DataManager.nToE_fromSW, DataManager.nToE_fromSW+DataManager.nToE_fromNW, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(toEntranceERepartition);
		
		toEntranceERepartitionRH = new UISliderDouble(simulation, xStart, yStart+6*(sliderHeight+buttonYMargin), 550, "Distribution between 7am, 8am, 9am", 100, DataManager.nToE_7, DataManager.nToE_7+DataManager.nToE_8, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(toEntranceERepartitionRH);
		
		// ==================================================================================================
		
		run = new UITextButton((simulation.getWidth()-sliderWidth)/2, simulation.getHeight()-60-buttonHeight-buttonYMargin, buttonWidth, buttonHeight, "Run", new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				DataManager.applyData(simulation);
				simulation.getSimState().enableUIManager();
				State.setState(simulation.getSimState());
				Utils.log("Simulation starts\n");
			}
		});
		this.uiManager.addObject(run);
		back = new UITextButton((simulation.getWidth()-sliderWidth)/2, simulation.getHeight()-60, buttonWidth, buttonHeight, "Back", new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				simulation.getMenuState().enableUIManager();
				State.setState(simulation.getMenuState());
			}
		});
		this.uiManager.addObject(back);
	}
	
	public void tick(int n) {
		this.uiManager.tick();
		
		if (simulation.getMouseManager().isLeftPressed()) {
			isLeftPressed = true;
			
		} else {
			if (isLeftPressed) {
				
				//simulation.setEntranceERate(test.getCurrentValue());
				isLeftPressed = false;
			}
		}
	}
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Assets.bgCol);
		g.fillRect(0, 0, simulation.getWidth(), simulation.getHeight());
		
		Text.drawString(g, "Simulation settings", Assets.idleCol, simulation.getWidth()/2, 50, true, Assets.largeFont);
		
		this.uiManager.render(g);
	}
	
	// Getters & setters ====================================================================================
	public UISlider toEntranceE() {
		return toEntranceE;
	}
	public UISliderDouble toEntranceERepartition() {
		return toEntranceERepartition;
	}
	public UISliderDouble toEntranceERepartitionRH() {
		return toEntranceERepartitionRH;
	}
	public UISlider fromFrToGe() {
		return fromFrToGe;
	}
	public UISliderDouble fromFrToGeRepartition() {
		return fromFrToGeRepartition;
	}
	public UISlider fromFrToGeDuringRH() {
		return fromFrToGeDuringRH;
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
