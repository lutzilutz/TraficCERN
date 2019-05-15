package states;
import java.awt.Graphics;

import data.DataManager;
import graphics.Assets;
import graphics.Text;
import main.Simulator;
import ui.ClickListener;
import ui.UIManager;
import ui.UISlider;
import ui.UISliderDouble;
import ui.UITextButton;
import utils.Defaults;
import utils.Utils;

public class SimSettingsState extends State {
	
	private UIManager uiManager;
	
	private int xStart = 320;
	private int yStart = 150;
	private int buttonYMargin = 5;
	private int buttonXMargin = 15;
	private boolean isLeftPressed = false;
	
	private UISlider timePerVhcEntrance;
	
	private UISliderDouble crEntreeB_phase1;
	private UISliderDouble crEntreeB_phase2;
	private UISliderDouble crEntreeB_phase3;
	private UISliderDouble crEntreeB_phase4;
	
	private UITextButton run, back;
	
	public SimSettingsState(Simulator simulator) {
		super(simulator);
		this.uiManager = new UIManager(simulator);
		
		// control duration button
		timePerVhcEntrance = new UISlider(simulator, xStart, yStart+1*(Assets.sliderHeight+buttonYMargin), Assets.sliderWidth, "Control duration of 1 vehicle at entrances", 30, Defaults.getControlDuration(), false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(timePerVhcEntrance);
		
		// Light phases duration ============================================================================
		
		crEntreeB_phase1 = new UISliderDouble(simulator, xStart, yStart+3*(Assets.sliderHeight+buttonYMargin), Assets.sliderWidth, "Duration phase 1", 60, Defaults.getLightPhaseDuration()[0], Defaults.getLightPhaseDuration()[1], false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(crEntreeB_phase1);
		
		crEntreeB_phase2 = new UISliderDouble(simulator, xStart, yStart+4*(Assets.sliderHeight+buttonYMargin), Assets.sliderWidth, "Duration phase 2", 60, Defaults.getLightPhaseDuration()[2], Defaults.getLightPhaseDuration()[3], false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(crEntreeB_phase2);
		
		crEntreeB_phase3 = new UISliderDouble(simulator, xStart, yStart+5*(Assets.sliderHeight+buttonYMargin), Assets.sliderWidth, "Duration phase 3", 60, Defaults.getLightPhaseDuration()[4], Defaults.getLightPhaseDuration()[5], false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(crEntreeB_phase3);
		
		crEntreeB_phase4 = new UISliderDouble(simulator, xStart, yStart+6*(Assets.sliderHeight+buttonYMargin), Assets.sliderWidth, "Duration phase 4", 60, Defaults.getLightPhaseDuration()[6], Defaults.getLightPhaseDuration()[7], false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(crEntreeB_phase4);
		
		// Global buttons ===================================================================================
		
		run = new UITextButton((simulator.getWidth()-0*Assets.menuButtonW)/2 + buttonXMargin/2, simulator.getHeight()-60, Assets.menuButtonW, Assets.menuButtonH, "Run", new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				Utils.initAllData(simulator.getSimState().getNumberOfSimulations());
				DataManager.applyDataProba(simulator);
				simulator.getSimState().enableUIManager();
				State.setState(simulator.getSimState());
			}
		});
		this.uiManager.addObject(run);
		back = new UITextButton((simulator.getWidth()-2*Assets.menuButtonW)/2 - buttonXMargin/2, simulator.getHeight()-60, Assets.menuButtonW, Assets.menuButtonH, "Back", new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				simulator.getMenuState().enableUIManager();
				State.setState(simulator.getMenuState());
			}
		});
		this.uiManager.addObject(back);
	}
	
	public void tick(int n) {
		
		// tick the UIManager
		this.uiManager.tick();
		
		// switch boolean field if left mouse button is pressed
		if (simulator.getMouseManager().isLeftPressed()) {
			isLeftPressed = true;
		} else {
			if (isLeftPressed) {
				isLeftPressed = false;
			}
		}
	}
	public void tick() {
		
	}
	public void render(Graphics g) {
		g.setColor(Assets.bgCol);
		g.fillRect(0, 0, simulator.getWidth(), simulator.getHeight());
		Text.drawString(g, simulator.getVersionID(), Assets.idleCol, 10, simulator.getHeight()-10, false, Assets.normalFont);
		Text.drawString(g, "Simulation settings", Assets.idleCol, simulator.getWidth()/2+150, 50, true, Assets.largeFont);
		Text.drawString(g, "general settings", Assets.idleCol, simulator.getWidth()/2+150, 85, true, Assets.largeFont);
		this.uiManager.render(g);
		
	}
	
	// Getters & setters ====================================================================================
	public UISliderDouble crEntreeB_phase1() {
		return crEntreeB_phase1;
	}
	public UISliderDouble crEntreeB_phase2() {
		return crEntreeB_phase2;
	}
	public UISliderDouble crEntreeB_phase3() {
		return crEntreeB_phase3;
	}
	public UISliderDouble crEntreeB_phase4() {
		return crEntreeB_phase4;
	}
	// Entrance flow ----------------------------------------------------------------------------------------
	public UISlider timePerVhcEntrance() {
		return timePerVhcEntrance;
	}
	public void disableUIManager() {
		simulator.getMouseManager().setUIManager(null);
	}
	public void enableUIManager() {
		simulator.getMouseManager().setUIManager(this.uiManager);
	}
	public void enableUIManager(UIManager uiManager) {
		simulator.getMouseManager().setUIManager(uiManager);
	}
}
