package states;
import java.awt.Graphics;

import data.DataManager;
import elements.Ride;
import elements.Road;
import graphics.Assets;
import graphics.Text;
import main.Simulation;
import network.AllNetworkRides;
import network.Network;
import ui.ClickListener;
import ui.UIImageButton;
import ui.UIManager;
import ui.UISlider;
import ui.UISliderDouble;
import ui.UISliderTriple;
import ui.UITextButton;
import utils.Utils;

public class SimSettingsStateProba extends State {
	
	private UIManager uiManagerGeneral;
	private long counter = 0;
	
	private int xStart = 320;
	private int yStart = 150;
	private int buttonYMargin = 5;
	private int buttonXMargin = 15;
	private int buttonWidth = 140;
	private int buttonHeight = 30;
	private int sliderWidth = 500;
	private int sliderHeight = 30;
	//private int descriptionMargin = 20;
	private boolean isLeftPressed = false;
	
	private UISlider timePerVhcEntrance;
	
	private UISliderDouble crEntreeB_phase1;
	private UISliderDouble crEntreeB_phase2;
	private UISliderDouble crEntreeB_phase3;
	private UISliderDouble crEntreeB_phase4;
	
	private UITextButton run, back;
	
	public SimSettingsStateProba(Simulation simulation) {
		super(simulation);
		this.uiManagerGeneral = new UIManager(simulation);
		
		timePerVhcEntrance = new UISlider(simulation, xStart, yStart+1*(sliderHeight+buttonYMargin), sliderWidth, "Control duration of 1 vehicle at entrances", 30, 8, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerGeneral.addObject(timePerVhcEntrance);
		
		crEntreeB_phase1 = new UISliderDouble(simulation, xStart, yStart+3*(sliderHeight+buttonYMargin), sliderWidth, "Duration phase 1", 60, DataManager.cycle1LTSmin, DataManager.cycle1LTSmax, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerGeneral.addObject(crEntreeB_phase1);
		
		crEntreeB_phase2 = new UISliderDouble(simulation, xStart, yStart+4*(sliderHeight+buttonYMargin), sliderWidth, "Duration phase 2", 60, DataManager.cycle2LTSmin, DataManager.cycle2LTSmax, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerGeneral.addObject(crEntreeB_phase2);
		
		crEntreeB_phase3 = new UISliderDouble(simulation, xStart, yStart+5*(sliderHeight+buttonYMargin), sliderWidth, "Duration phase 3", 60, DataManager.cycle3LTSmin, DataManager.cycle3LTSmax, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerGeneral.addObject(crEntreeB_phase3);
		
		crEntreeB_phase4 = new UISliderDouble(simulation, xStart, yStart+6*(sliderHeight+buttonYMargin), sliderWidth, "Duration phase 4", 60, DataManager.cycle4LTSmin, DataManager.cycle4LTSmax, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerGeneral.addObject(crEntreeB_phase4);
		
		// ##################################################################################################
		// BUTTONS ##########################################################################################
		// ##################################################################################################
		
		run = new UITextButton((simulation.getWidth()-0*buttonWidth)/2 + buttonXMargin/2, simulation.getHeight()-60, buttonWidth, buttonHeight, "Run", new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				Utils.initAllData(simulation.getSimState().getNumberOfSimulations());
				if (DataManager.useProbabilities) {
					DataManager.applyDataProba(simulation);
				} else {
					DataManager.applyDataNumerical(simulation);
				}
				simulation.getSimState().enableUIManager();
				State.setState(simulation.getSimState());
				//Utils.log("Simulation starts\n");
				
			}
		});
		this.uiManagerGeneral.addObject(run);
		back = new UITextButton((simulation.getWidth()-2*buttonWidth)/2 - buttonXMargin/2, simulation.getHeight()-60, buttonWidth, buttonHeight, "Back", new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				simulation.getMenuState().enableUIManager();
				State.setState(simulation.getMenuState());
			}
		});
		this.uiManagerGeneral.addObject(back);
	}
	
	public void tick(int n) {
		
		this.uiManagerGeneral.tick();
		
		if (simulation.getMouseManager().isLeftPressed()) {
			isLeftPressed = true;
		} else {
			if (isLeftPressed) {
				isLeftPressed = false;
			}
		}
		
		if (counter % 10 == 0) {
			if (!DataManager.useProbabilities) {
				DataManager.applyDataNumerical(simulation);
			}
		}
		counter++;
	}
	public void tick() {
		
	}
	public int computeTotalFlow(Road r) {
		int totalFlow = -1;
		for (int i=0 ; i<r.getFlow().size() ; i++) {
			totalFlow += r.getFlow().get(i);
		}
		if (totalFlow != -1) {
			totalFlow++;
		}
		return totalFlow;
	}
	public int computeOutFlow(Network n, String roadName) {
		int totalFlow = -1;
		for (AllNetworkRides anr : n.getAllNetworkRides()) {
			
			for (Ride ride: anr.getNetworkRides()) {
				
				if (ride.getNextConnections().size() > 0 && ride.getNextConnections().get(ride.getNextConnections().size()-1).getName().equals(roadName)) {
					for (int i=0 ; i<ride.getFlow().size() ; i++) {
						totalFlow += ride.getFlow().get(i);
					}
				}
				
			}
		}
		
		if (totalFlow != -1) {
			totalFlow++;
		}
		return totalFlow;
		
	}
	public void render(Graphics g) {
		g.setColor(Assets.bgCol);
		g.fillRect(0, 0, simulation.getWidth(), simulation.getHeight());
		
		Text.drawString(g, simulation.getVersionID(), Assets.idleCol, 10, simulation.getHeight()-10, false, Assets.normalFont);
		
		Text.drawString(g, "Simulation settings", Assets.idleCol, simulation.getWidth()/2+150, 50, true, Assets.largeFont);
		
		Text.drawString(g, "general settings", Assets.idleCol, simulation.getWidth()/2+150, 85, true, Assets.largeFont);
		this.uiManagerGeneral.render(g);
		
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
		simulation.getMouseManager().setUIManager(null);
	}
	public void enableUIManager() {
		simulation.getMouseManager().setUIManager(this.uiManagerGeneral);
	}
	public void enableUIManager(UIManager uiManager) {
		simulation.getMouseManager().setUIManager(uiManager);
	}
}
