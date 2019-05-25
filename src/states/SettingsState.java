package states;
import java.awt.Color;
import java.awt.Graphics;

import data.DataManager;
import graphics.Assets;
import graphics.Text;
import main.Simulator;
import network.Network;
import ui.ClickListener;
import ui.UIManager;
import ui.UISlider;
import ui.UISliderDouble;
import ui.UITextButton;
import utils.Defaults;
import utils.Utils;

public class SettingsState extends State {
	
	// UI manager
	private UIManager uiManager;
	private Network network;
	
	private int buttonYMargin = 5; // vertical margin between buttons
	private int buttonXMargin = 15; // horizontal margin between buttons
	private boolean isLeftPressed = false; // if user have left mouse button pressed
	
	// Buttons and slider of the setting
	private UISlider timePerVhcEntrance;
	private UISliderDouble crEntreeB_phase1;
	private UISliderDouble crEntreeB_phase2;
	private UISliderDouble crEntreeB_phase3;
	private UISliderDouble crEntreeB_phase4;
	private UITextButton run, back;
	
	private boolean loading = false;
	
	public SettingsState(Simulator simulator) {
		super(simulator);
		this.uiManager = new UIManager(simulator);
		this.network = simulator.getSimState().getNetwork();
		
		// control duration button
		timePerVhcEntrance = new UISlider(simulator, Assets.menuXStart, Assets.menuYStart+1*(Assets.sliderHeight+buttonYMargin), Assets.sliderWidth, "Control duration", 30, Defaults.getControlDuration(), false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(timePerVhcEntrance);
		
		// Light phases duration ============================================================================
		
		crEntreeB_phase1 = new UISliderDouble(simulator, Assets.menuXStart, Assets.menuYStart+3*(Assets.sliderHeight+buttonYMargin), Assets.sliderWidth, "Duration phase 1", 60, Defaults.getLightPhaseDuration()[0], Defaults.getLightPhaseDuration()[1], false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(crEntreeB_phase1);
		
		crEntreeB_phase2 = new UISliderDouble(simulator, Assets.menuXStart, Assets.menuYStart+4*(Assets.sliderHeight+buttonYMargin), Assets.sliderWidth, "Duration phase 2", 60, Defaults.getLightPhaseDuration()[2], Defaults.getLightPhaseDuration()[3], false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(crEntreeB_phase2);
		
		crEntreeB_phase3 = new UISliderDouble(simulator, Assets.menuXStart, Assets.menuYStart+5*(Assets.sliderHeight+buttonYMargin), Assets.sliderWidth, "Duration phase 3", 60, Defaults.getLightPhaseDuration()[4], Defaults.getLightPhaseDuration()[5], false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(crEntreeB_phase3);
		
		crEntreeB_phase4 = new UISliderDouble(simulator, Assets.menuXStart, Assets.menuYStart+6*(Assets.sliderHeight+buttonYMargin), Assets.sliderWidth, "Duration phase 4", 60, Defaults.getLightPhaseDuration()[6], Defaults.getLightPhaseDuration()[7], false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager.addObject(crEntreeB_phase4);
		
		// Global buttons ===================================================================================
		
		run = new UITextButton((simulator.getWidth()-0*Assets.menuButtonW)/2 + buttonXMargin/2, simulator.getHeight()-60, Assets.menuButtonW, Assets.menuButtonH, "Run", new ClickListener(){
			@Override
			public void onClick() {
				loading = true;
				// prevents user to continue clicking after state change
				disableUIManager();
				Utils.initAllData(simulator.getSimState().getNumberOfSimulations());
				DataManager.applyData(simulator);
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
		
		tick();
		
	}
	public void tick() {
		
		// menu aren't loading anymore
		simulator.getMenuState().setLoading(false);
		
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
	public void render(Graphics g) {
		g.setColor(Assets.bgCol);
		g.fillRect(0, 0, simulator.getWidth(), simulator.getHeight());
		Text.drawString(g, simulator.getVersionID(), Assets.idleCol, 10, simulator.getHeight()-10, false, Assets.normalFont);
		Text.drawString(g, "Simulation settings", Assets.idleCol, simulator.getWidth() / 2, 100, true, Assets.largeFont);
		this.uiManager.render(g);
		
		if (loading) {
			g.setColor(Assets.bgAlphaCol);
			g.fillRect(0, 0, 1000, 700);
			g.setColor(Color.white);
			Text.drawString(g, "Please wait, processing ...", Color.white, network.getSimulation().getWidth()/2, network.getSimulation().getHeight()/2, true, Assets.largeFont);
		}
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
	private void disableUIManager() {
		simulator.getMouseManager().setUIManager(null);
	}
	public void enableUIManager() {
		simulator.getMouseManager().setUIManager(this.uiManager);
	}
	public void setLoading(boolean loading) {
		this.loading = loading;
	}
}
