package states;
import java.awt.Graphics;

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
	
	private UISlider test,fromFrToGe,fromFrToGeDuringRH;
	private UISliderDouble test2;
	private UITextButton run, back;
	
	public SimSettingsState(Simulation simulation) {
		super(simulation);
		this.uiManager = new UIManager(simulation);
		
		test = new UISlider(simulation, xStart, yStart, 550, "Entrance E generation rate (vhc/hour)", 3600, 50, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(test);
		
		fromFrToGe = new UISlider(simulation, xStart, yStart+2*(sliderHeight+buttonYMargin), 550, "[Not Working] France to Geneva (vhc/day)", 20000, 12500, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(fromFrToGe);
		
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
				
				simulation.setEntranceERate(test.getCurrentValue());
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
		Text.drawString(g, "Examples of the 3 types of sliders :", Assets.idleCol, simulation.getWidth()/2, 250, true, Assets.normalBoldFont);
		
		this.uiManager.render(g);
	}
	
	// Getters & setters ====================================================================================
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
