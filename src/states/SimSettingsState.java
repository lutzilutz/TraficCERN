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

public class SimSettingsState extends State {
	
	private UIManager uiManager;
	
	private int xStart = 300;
	private int yStart = 200;
	private int buttonYMargin = 20;
	private int buttonWidth = 140;
	private int buttonHeight = 30;
	private int sliderWidth = 140;
	private int sliderHeight = 30;
	private int descriptionMargin = 20;
	private boolean isLeftPressed = false;
	
	private UISlider fromFrToGe,fromFrToGeDuringRH;
	private UISliderDouble fromFrToGeRepartition, test2;
	private UITextButton run, back;
	
	public SimSettingsState(Simulation simulation) {
		super(simulation);
		this.uiManager = new UIManager(simulation);
		
		fromFrToGe = new UISlider(simulation, xStart, yStart, 550, "Flow from France to Geneva (vhc/hour)", 4000, (int) (DataManager.getFromFrToGe()/24.0), false, new ClickListener(){
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
		
		fromFrToGeDuringRH = new UISlider(simulation, xStart, yStart+3*(sliderHeight+buttonYMargin), 550, "[Not Working] Quantity during rush-hours", 100, 70, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(fromFrToGeDuringRH);
		
		test2 = new UISliderDouble(simulation, xStart, yStart+4*(sliderHeight+buttonYMargin), 550, "[Not Working] Repartition in 7-8-9h", 100, 30, 70, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(test2);
		
		run = new UITextButton((simulation.getWidth()-sliderWidth)/2, simulation.getHeight()-60-buttonHeight-buttonYMargin, buttonWidth, buttonHeight, "Run", new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				DataManager.applyData(simulation);
				simulation.getSimState().enableUIManager();
				State.setState(simulation.getSimState());
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
		
		Text.drawString(g, "Simulation settings", Assets.idleCol, simulation.getWidth()/2, 100, true, Assets.largeFont);
		
		this.uiManager.render(g);
	}
	
	// Getters & setters ====================================================================================
	public UISlider fromFrToGe() {
		return fromFrToGe;
	}
	public UISliderDouble fromFrToGeRepartition() {
		return fromFrToGeRepartition;
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
