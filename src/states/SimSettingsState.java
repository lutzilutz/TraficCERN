package states;
import java.awt.Graphics;

import data.DataManager;
import elements.Road;
import graphics.Assets;
import graphics.Text;
import main.Simulation;
import ui.ClickListener;
import ui.UIImageButton;
import ui.UIManager;
import ui.UISlider;
import ui.UISliderDouble;
import ui.UISliderTriple;
import ui.UITextButton;
import utils.Utils;

public class SimSettingsState extends State {
	
	private UIManager uiManager1;
	private UIManager uiManager2;
	private UIManager uiManager3;
	private int activePage = 1;
	private int nPages = 3;
	
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
	
	private UISlider fromFrToGe,fromFrToGeDuringRH;
	private UISliderDouble fromFrToGeRepartitionRH;
	private UISliderTriple fromFrToGeRepartition;
	
	private UISlider fromGeToFr,fromGeToFrDuringRH2;
	private UISliderDouble fromGeToFrRepartitionRH2;
	private UISliderTriple fromGeToFrRepartition;
	
	private UISlider toEntranceE;
	private UISliderDouble toEntranceERepartition, toEntranceERepartitionRH;
	
	private UISlider fromEntranceE;
	private UISliderDouble fromEntranceERepartition, fromEntranceERepartitionRH2;
	
	private UISlider toEntranceA, toEntranceARepartition;
	private UISliderDouble toEntranceARepartitionRH;
	
	private UIImageButton previous, next;
	private UITextButton run, back;
	
	public SimSettingsState(Simulation simulation) {
		super(simulation);
		this.uiManager1 = new UIManager(simulation);
		this.uiManager2 = new UIManager(simulation);
		this.uiManager3 = new UIManager(simulation);

		// ##################################################################################################
		// PAGE 1 ###########################################################################################
		// ##################################################################################################
		
		// From France to Geneva
		fromFrToGe = new UISlider(simulation, xStart, yStart, sliderWidth, "Flow from France to Geneva (vhc/day)", 25000, DataManager.nFrGe, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(fromFrToGe);
		fromFrToGeRepartition = new UISliderTriple(simulation, xStart, yStart+1*(sliderHeight+buttonYMargin), sliderWidth, "Coming from Thoiry, St-Genis, Ferney and Europe", 100, DataManager.nFrGe_fromSW, DataManager.nFrGe_fromSW+DataManager.nFrGe_fromNW, DataManager.nFrGe_fromSW+DataManager.nFrGe_fromNW+DataManager.nFrGe_fromTun, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(fromFrToGeRepartition);
		
		fromFrToGeDuringRH = new UISlider(simulation, xStart, yStart+2*(sliderHeight+buttonYMargin), sliderWidth, "Quantity during rush-hours (morning)", 100, 70, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(fromFrToGeDuringRH);
		
		fromFrToGeRepartitionRH = new UISliderDouble(simulation, xStart, yStart+3*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 7am, 8am, 9am", 100, DataManager.nFrGe_7, DataManager.nFrGe_7+DataManager.nFrGe_8, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(fromFrToGeRepartitionRH);
		
		// From Geneva to France ============================================================================
		
		fromGeToFr = new UISlider(simulation, xStart, yStart+5*(sliderHeight+buttonYMargin), sliderWidth, "Flow from Geneva to France (vhc/day)", 25000, DataManager.nGeFr, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(fromGeToFr);
		fromGeToFrRepartition = new UISliderTriple(simulation, xStart, yStart+6*(sliderHeight+buttonYMargin), sliderWidth, "Going to Thoiry, St-Genis, Ferney and Europe", 100, DataManager.nGeFr_toSW, DataManager.nGeFr_toSW+DataManager.nGeFr_toNW, DataManager.nGeFr_toSW+DataManager.nGeFr_toNW+DataManager.nGeFr_toTun, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(fromGeToFrRepartition);
		
		fromGeToFrDuringRH2 = new UISlider(simulation, xStart, yStart+7*(sliderHeight+buttonYMargin), sliderWidth, "Quantity during rush-hours (evening)", 100, 70, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(fromGeToFrDuringRH2);
		
		fromGeToFrRepartitionRH2 = new UISliderDouble(simulation, xStart, yStart+8*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 5pm, 6pm, 7pm", 100, DataManager.nGeFr_17, DataManager.nGeFr_17+DataManager.nGeFr_18, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(fromGeToFrRepartitionRH2);
		
		// ##################################################################################################
		// PAGE 2 ###########################################################################################
		// ##################################################################################################
		
		timePerVhcEntrance = new UISlider(simulation, xStart, yStart+0*(sliderHeight+buttonYMargin), sliderWidth, "Control duration of 1 vehicle at entrances", 30, 8, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager2.addObject(timePerVhcEntrance);
		
		// To entrance E ====================================================================================
		
		toEntranceE = new UISlider(simulation, xStart, yStart+2*(sliderHeight+buttonYMargin), sliderWidth, "Flow to Entrance E during rush-hours", 4000, DataManager.nToE, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager2.addObject(toEntranceE);
		
		toEntranceERepartition = new UISliderDouble(simulation, xStart, yStart+3*(sliderHeight+buttonYMargin), sliderWidth, "Coming from Thoiry, St-Genis and Ferney", 100, DataManager.nToE_fromSW, DataManager.nToE_fromSW+DataManager.nToE_fromNW, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager2.addObject(toEntranceERepartition);
		
		toEntranceERepartitionRH = new UISliderDouble(simulation, xStart, yStart+4*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 7am, 8am, 9am", 100, DataManager.nToE_7, DataManager.nToE_7+DataManager.nToE_8, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager2.addObject(toEntranceERepartitionRH);
		
		// From entrance E ====================================================================================
		
		fromEntranceE = new UISlider(simulation, xStart, yStart+6*(sliderHeight+buttonYMargin), sliderWidth, "Flow from Entrance E during rush-hours", 4000, DataManager.nFromE, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager2.addObject(fromEntranceE);
		
		fromEntranceERepartition = new UISliderDouble(simulation, xStart, yStart+7*(sliderHeight+buttonYMargin), sliderWidth, "Going to Thoiry, St-Genis and Ferney", 100, DataManager.nFromE_toSW, DataManager.nFromE_toSW+DataManager.nFromE_toNW, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager2.addObject(fromEntranceERepartition);
		
		fromEntranceERepartitionRH2 = new UISliderDouble(simulation, xStart, yStart+8*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 5pm, 6pm, 7pm", 100, DataManager.nFromE_17, DataManager.nFromE_17+DataManager.nFromE_18, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager2.addObject(fromEntranceERepartitionRH2);
		
		// ##################################################################################################
		// PAGE 3 ###########################################################################################
		// ##################################################################################################
		
		// To entrance A ====================================================================================
		
		toEntranceA = new UISlider(simulation, xStart, yStart+2*(sliderHeight+buttonYMargin), sliderWidth, "Flow to Entrance A during rush-hours", 4000, DataManager.nToA, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager3.addObject(toEntranceA);
		
		toEntranceARepartition = new UISlider(simulation, xStart, yStart+3*(sliderHeight+buttonYMargin), sliderWidth, "Coming from Geneva, France", 100, DataManager.nToA_fromGe, true, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager3.addObject(toEntranceARepartition);
		
		toEntranceARepartitionRH = new UISliderDouble(simulation, xStart, yStart+4*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 7am, 8am, 9am", 100, DataManager.nToA_7, DataManager.nToA_7+DataManager.nToA_8, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager3.addObject(toEntranceARepartitionRH);
		
		// ##################################################################################################
		// BUTTONS ##########################################################################################
		// ##################################################################################################
		
		previous = new UIImageButton(325, 593, 50, buttonHeight, Assets.previousIdle, Assets.previousActive, new ClickListener(){
			@Override
			public void onClick() {
				previousPage();
			}
		});
		this.uiManager1.addObject(previous);
		this.uiManager2.addObject(previous);
		this.uiManager3.addObject(previous);
		next = new UIImageButton(625, 593, 50, buttonHeight, Assets.nextIdle, Assets.nextActive, new ClickListener(){
			@Override
			public void onClick() {
				nextPage();
			}
		});
		this.uiManager1.addObject(next);
		this.uiManager2.addObject(next);
		this.uiManager3.addObject(next);
		
		run = new UITextButton((simulation.getWidth()-0*buttonWidth)/2 + buttonXMargin/2, simulation.getHeight()-60, buttonWidth, buttonHeight, "Run", new ClickListener(){
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
		this.uiManager1.addObject(run);
		this.uiManager2.addObject(run);
		this.uiManager3.addObject(run);
		back = new UITextButton((simulation.getWidth()-2*buttonWidth)/2 - buttonXMargin/2, simulation.getHeight()-60, buttonWidth, buttonHeight, "Back", new ClickListener(){
			@Override
			public void onClick() {
				// prevents user to continue clicking after state change
				disableUIManager();
				simulation.getMenuState().enableUIManager();
				State.setState(simulation.getMenuState());
			}
		});
		this.uiManager1.addObject(back);
		this.uiManager2.addObject(back);
		this.uiManager3.addObject(back);
	}
	
	public void tick(int n) {
		
		if (activePage == 1) {
			this.uiManager1.tick();
		} else if (activePage == 2) {
			this.uiManager2.tick();
		} else if (activePage == 3) {
			this.uiManager3.tick();
		}
		
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
	public void nextPage() {
		
		if (activePage+1 > nPages) {
			activePage = 1;
		} else {
			activePage++;
		}
		
		if (activePage == 1) {
			enableUIManager(uiManager1);
		} else if (activePage == 2) {
			enableUIManager(uiManager2);
		} else if (activePage == 3) {
			enableUIManager(uiManager3);
		}
	}
	public void previousPage() {
		
		if (activePage-1 < 1) {
			activePage = nPages;
		} else {
			activePage--;
		}
		
		if (activePage == 1) {
			enableUIManager(uiManager1);
		} else if (activePage == 2) {
			enableUIManager(uiManager2);
		} else if (activePage == 3) {
			enableUIManager(uiManager3);
		}
	}
	public void renderPageIndication(Graphics g) {
		int smallR = 4;
		int bigR = 6;
		int spacing = 30;
		double x = (simulation.getWidth() - 2*(nPages)*smallR - Math.max(0, nPages-1)*spacing ) /2.0;
		int y = 600;
		int r = 0;
		for (int i=1; i<=nPages ; i++) {
			if (activePage == i) {
				g.setColor(Assets.textCol);
			} else {
				g.setColor(Assets.idleCol);
			}
			if (activePage == i) {
				x -= (bigR-smallR);
				y -= (bigR-smallR);
				r = bigR;
			} else {
				r = smallR;
			}
			g.fillRect((int) (x), y, 2*r, 2*r);
			
			if (activePage == i) {
				x += (bigR-smallR);
				y += (bigR-smallR);
				r = smallR;
			}
			if (activePage == i) {
				g.setColor(Assets.idleCol);
			}
			
			x += 2*r + spacing;
		}
		
	}
	public void render(Graphics g) {
		g.setColor(Assets.bgCol);
		g.fillRect(0, 0, simulation.getWidth(), simulation.getHeight());
		
		Text.drawString(g, "Simulation settings", Assets.idleCol, simulation.getWidth()/2, 50, true, Assets.largeFont);
		
		if (activePage == 1) {
			Text.drawString(g, "transit between France and Geneva", Assets.idleCol, simulation.getWidth()/2, 85, true, Assets.largeFont);
			this.uiManager1.render(g);
		} else if (activePage == 2) {
			Text.drawString(g, "entrance E", Assets.idleCol, simulation.getWidth()/2, 85, true, Assets.largeFont);
			this.uiManager2.render(g);
		} else if (activePage == 3) {
			Text.drawString(g, "entrance A", Assets.idleCol, simulation.getWidth()/2, 85, true, Assets.largeFont);
			this.uiManager3.render(g);
		}
		
		renderPageIndication(g);
	}
	
	// Getters & setters ====================================================================================
	// FR to GE ---------------------------------------------------------------------------------------------
	public UISlider fromFrToGe() {
		return fromFrToGe;
	}
	public UISliderTriple fromFrToGeRepartition() {
		return fromFrToGeRepartition;
	}
	public UISliderDouble fromFrToGeRepartitionRH() {
		return fromFrToGeRepartitionRH;
	}
	public UISlider fromFrToGeDuringRH() {
		return fromFrToGeDuringRH;
	}
	// GE to FR ---------------------------------------------------------------------------------------------
	public UISlider fromGeToFr() {
		return fromGeToFr;
	}
	public UISliderTriple fromGeToFrRepartition() {
		return fromGeToFrRepartition;
	}
	public UISlider fromGeToFrDuringRH2() {
		return fromGeToFrDuringRH2;
	}
	public UISliderDouble fromGeToFrRepartitionRH2() {
		return fromGeToFrRepartitionRH2;
	}
	// Entrance flow ----------------------------------------------------------------------------------------
	public UISlider timePerVhcEntrance() {
		return timePerVhcEntrance;
	}
	// To entrance E ----------------------------------------------------------------------------------------
	public UISlider toEntranceE() {
		return toEntranceE;
	}
	public UISliderDouble toEntranceERepartition() {
		return toEntranceERepartition;
	}
	public UISliderDouble toEntranceERepartitionRH() {
		return toEntranceERepartitionRH;
	}
	// From entrance E --------------------------------------------------------------------------------------
	public UISlider fromEntranceE() {
		return fromEntranceE;
	}
	public UISliderDouble fromEntranceERepartition() {
		return fromEntranceERepartition;
	}
	public UISliderDouble fromEntranceERepartitionRH2() {
		return fromEntranceERepartitionRH2;
	}
	// To entrance A ----------------------------------------------------------------------------------------
	public UISlider toEntranceA() {
		return toEntranceA;
	}
	public UISlider toEntranceARepartition() {
		return toEntranceARepartition;
	}
	public UISliderDouble toEntranceARepartitionRH() {
		return toEntranceARepartitionRH;
	}
	// ------------------------------------------------------------------------------------------------------
	public UIManager getUIManager() {
		return this.uiManager1;
	}
	public void disableUIManager() {
		simulation.getMouseManager().setUIManager(null);
	}
	public void enableUIManager() {
		simulation.getMouseManager().setUIManager(this.uiManager1);
	}
	public void enableUIManager(UIManager uiManager) {
		simulation.getMouseManager().setUIManager(uiManager);
	}
}
