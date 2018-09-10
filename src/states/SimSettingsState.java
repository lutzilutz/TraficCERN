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
	
	private UIManager uiManagerTransit;
	private UIManager uiManagerE;
	private UIManager uiManagerA;
	private UIManager uiManagerB;
	private UIManager uiManagerGeneral;
	private int activePage = 1;
	private int nPages = 5;
	
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
	
	private UISlider sizeOfNetwork;
	
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
	
	private UISlider fromEntranceA, fromEntranceARepartition;
	private UISliderDouble fromEntranceARepartitionRH2;
	
	private UISlider toEntranceB, toEntranceBRepartition;
	private UISliderDouble toEntranceBRepartitionRH;
	
	private UISlider fromEntranceB, fromEntranceBRepartition;
	private UISliderDouble fromEntranceBRepartitionRH2;
	
	private UIImageButton previous, next;
	private UITextButton run, back;
	
	public SimSettingsState(Simulation simulation) {
		super(simulation);
		this.uiManagerTransit = new UIManager(simulation);
		this.uiManagerE = new UIManager(simulation);
		this.uiManagerA = new UIManager(simulation);
		this.uiManagerB = new UIManager(simulation);
		this.uiManagerGeneral = new UIManager(simulation);

		// ##################################################################################################
		// PAGE 1 ###########################################################################################
		// ##################################################################################################
		
		// From France to Geneva
		fromFrToGe = new UISlider(simulation, xStart, yStart, sliderWidth, "Flow from France to Geneva (vhc/day)", 25000, DataManager.nFrGe, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerTransit.addObject(fromFrToGe);
		fromFrToGeRepartition = new UISliderTriple(simulation, xStart, yStart+1*(sliderHeight+buttonYMargin), sliderWidth, "Coming from Thoiry, St-Genis, Ferney and Europe", 100, DataManager.nFrGe_fromSW, DataManager.nFrGe_fromSW+DataManager.nFrGe_fromNW, DataManager.nFrGe_fromSW+DataManager.nFrGe_fromNW+DataManager.nFrGe_fromTun, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerTransit.addObject(fromFrToGeRepartition);
		
		fromFrToGeDuringRH = new UISlider(simulation, xStart, yStart+2*(sliderHeight+buttonYMargin), sliderWidth, "Quantity during rush-hours (morning)", 100, 70, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerTransit.addObject(fromFrToGeDuringRH);
		
		fromFrToGeRepartitionRH = new UISliderDouble(simulation, xStart, yStart+3*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 7am, 8am, 9am", 100, DataManager.nFrGe_7, DataManager.nFrGe_7+DataManager.nFrGe_8, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerTransit.addObject(fromFrToGeRepartitionRH);
		
		// From Geneva to France ============================================================================
		
		fromGeToFr = new UISlider(simulation, xStart, yStart+5*(sliderHeight+buttonYMargin), sliderWidth, "Flow from Geneva to France (vhc/day)", 25000, DataManager.nGeFr, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerTransit.addObject(fromGeToFr);
		fromGeToFrRepartition = new UISliderTriple(simulation, xStart, yStart+6*(sliderHeight+buttonYMargin), sliderWidth, "Going to Thoiry, St-Genis, Ferney and Europe", 100, DataManager.nGeFr_toSW, DataManager.nGeFr_toSW+DataManager.nGeFr_toNW, DataManager.nGeFr_toSW+DataManager.nGeFr_toNW+DataManager.nGeFr_toTun, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerTransit.addObject(fromGeToFrRepartition);
		
		fromGeToFrDuringRH2 = new UISlider(simulation, xStart, yStart+7*(sliderHeight+buttonYMargin), sliderWidth, "Quantity during rush-hours (evening)", 100, 70, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerTransit.addObject(fromGeToFrDuringRH2);
		
		fromGeToFrRepartitionRH2 = new UISliderDouble(simulation, xStart, yStart+8*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 5pm, 6pm, 7pm", 100, DataManager.nGeFr_17, DataManager.nGeFr_17+DataManager.nGeFr_18, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerTransit.addObject(fromGeToFrRepartitionRH2);
		
		// ##################################################################################################
		// PAGE 2 ###########################################################################################
		// ##################################################################################################
		
		// To entrance E ====================================================================================
		
		toEntranceE = new UISlider(simulation, xStart, yStart+2*(sliderHeight+buttonYMargin), sliderWidth, "Flow to Entrance E during rush-hours", 4000, DataManager.nToE, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerE.addObject(toEntranceE);
		
		toEntranceERepartition = new UISliderDouble(simulation, xStart, yStart+3*(sliderHeight+buttonYMargin), sliderWidth, "Coming from Thoiry, St-Genis and Ferney", 100, DataManager.nToE_fromSW, DataManager.nToE_fromSW+DataManager.nToE_fromNW, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerE.addObject(toEntranceERepartition);
		
		toEntranceERepartitionRH = new UISliderDouble(simulation, xStart, yStart+4*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 7am, 8am, 9am", 100, DataManager.nToE_7, DataManager.nToE_7+DataManager.nToE_8, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerE.addObject(toEntranceERepartitionRH);
		
		// From entrance E ====================================================================================
		
		fromEntranceE = new UISlider(simulation, xStart, yStart+6*(sliderHeight+buttonYMargin), sliderWidth, "Flow from Entrance E during rush-hours", 4000, DataManager.nFromE, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerE.addObject(fromEntranceE);
		
		fromEntranceERepartition = new UISliderDouble(simulation, xStart, yStart+7*(sliderHeight+buttonYMargin), sliderWidth, "Going to Thoiry, St-Genis and Ferney", 100, DataManager.nFromE_toSW, DataManager.nFromE_toSW+DataManager.nFromE_toNW, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerE.addObject(fromEntranceERepartition);
		
		fromEntranceERepartitionRH2 = new UISliderDouble(simulation, xStart, yStart+8*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 5pm, 6pm, 7pm", 100, DataManager.nFromE_17, DataManager.nFromE_17+DataManager.nFromE_18, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerE.addObject(fromEntranceERepartitionRH2);
		
		// ##################################################################################################
		// PAGE 3 ###########################################################################################
		// ##################################################################################################
		
		// To entrance A ====================================================================================
		
		toEntranceA = new UISlider(simulation, xStart, yStart+1*(sliderHeight+buttonYMargin), sliderWidth, "Flow to Entrance A during rush-hours", 4000, DataManager.nToA, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerA.addObject(toEntranceA);
		
		toEntranceARepartition = new UISlider(simulation, xStart, yStart+2*(sliderHeight+buttonYMargin), sliderWidth, "Coming from Geneva, France", 100, DataManager.nToA_fromGe, true, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerA.addObject(toEntranceARepartition);
		
		toEntranceARepartitionRH = new UISliderDouble(simulation, xStart, yStart+3*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 7am, 8am, 9am", 100, DataManager.nToA_7, DataManager.nToA_7+DataManager.nToA_8, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerA.addObject(toEntranceARepartitionRH);
		
		// From entrance A ====================================================================================
		
		fromEntranceA = new UISlider(simulation, xStart, yStart+5*(sliderHeight+buttonYMargin), sliderWidth, "Flow from Entrance A during rush-hours", 4000, DataManager.nFromA, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerA.addObject(fromEntranceA);
		
		fromEntranceARepartition = new UISlider(simulation, xStart, yStart+6*(sliderHeight+buttonYMargin), sliderWidth, "Going to Geneva, France", 100, DataManager.nFromA_toGe, true, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerA.addObject(fromEntranceARepartition);
		
		fromEntranceARepartitionRH2 = new UISliderDouble(simulation, xStart, yStart+7*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 5pm, 6pm, 7pm", 100, DataManager.nFromA_17, DataManager.nFromA_17+DataManager.nFromA_18, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerA.addObject(fromEntranceARepartitionRH2);
		
		// ##################################################################################################
		// PAGE 4 ###########################################################################################
		// ##################################################################################################
		
		// To entrance B ====================================================================================
		
		toEntranceB = new UISlider(simulation, xStart, yStart+1*(sliderHeight+buttonYMargin), sliderWidth, "Flow to Entrance B during rush-hours", 4000, DataManager.nToB, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerB.addObject(toEntranceB);
		
		toEntranceBRepartition = new UISlider(simulation, xStart, yStart+2*(sliderHeight+buttonYMargin), sliderWidth, "Coming from Geneva, France", 100, DataManager.nToB_fromGe, true, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerB.addObject(toEntranceBRepartition);
		
		toEntranceBRepartitionRH = new UISliderDouble(simulation, xStart, yStart+3*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 7am, 8am, 9am", 100, DataManager.nToB_7, DataManager.nToB_7+DataManager.nToB_8, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerB.addObject(toEntranceBRepartitionRH);
		
		// From entrance B ====================================================================================
		
		fromEntranceB = new UISlider(simulation, xStart, yStart+5*(sliderHeight+buttonYMargin), sliderWidth, "Flow from Entrance B during rush-hours", 4000, DataManager.nFromB, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerB.addObject(fromEntranceB);
		
		fromEntranceBRepartition = new UISlider(simulation, xStart, yStart+6*(sliderHeight+buttonYMargin), sliderWidth, "Going to Geneva, France", 100, DataManager.nFromB_toGe, true, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerB.addObject(fromEntranceBRepartition);
		
		fromEntranceBRepartitionRH2 = new UISliderDouble(simulation, xStart, yStart+7*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 5pm, 6pm, 7pm", 100, DataManager.nFromB_17, DataManager.nFromB_17+DataManager.nFromB_18, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerB.addObject(fromEntranceBRepartitionRH2);
		
		// ##################################################################################################
		// PAGE 5 ###########################################################################################
		// ##################################################################################################
		
		timePerVhcEntrance = new UISlider(simulation, xStart, yStart+1*(sliderHeight+buttonYMargin), sliderWidth, "Control duration of 1 vehicle at entrances", 30, 8, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManagerGeneral.addObject(timePerVhcEntrance);
		
		// ##################################################################################################
		// BUTTONS ##########################################################################################
		// ##################################################################################################
		
		previous = new UIImageButton(325, 593, 50, buttonHeight, Assets.previousIdle, Assets.previousActive, new ClickListener(){
			@Override
			public void onClick() {
				previousPage();
			}
		});
		this.uiManagerTransit.addObject(previous);
		this.uiManagerE.addObject(previous);
		this.uiManagerA.addObject(previous);
		this.uiManagerB.addObject(previous);
		this.uiManagerGeneral.addObject(previous);
		next = new UIImageButton(625, 593, 50, buttonHeight, Assets.nextIdle, Assets.nextActive, new ClickListener(){
			@Override
			public void onClick() {
				nextPage();
			}
		});
		this.uiManagerTransit.addObject(next);
		this.uiManagerE.addObject(next);
		this.uiManagerA.addObject(next);
		this.uiManagerB.addObject(next);
		this.uiManagerGeneral.addObject(next);
		
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
		this.uiManagerTransit.addObject(run);
		this.uiManagerE.addObject(run);
		this.uiManagerA.addObject(run);
		this.uiManagerB.addObject(run);
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
		this.uiManagerTransit.addObject(back);
		this.uiManagerE.addObject(back);
		this.uiManagerA.addObject(back);
		this.uiManagerB.addObject(back);
		this.uiManagerGeneral.addObject(back);
	}
	
	public void tick(int n) {
		
		if (activePage == 1) {
			this.uiManagerTransit.tick();
		} else if (activePage == 2) {
			this.uiManagerE.tick();
		} else if (activePage == 3) {
			this.uiManagerA.tick();
		} else if (activePage == 4) {
			this.uiManagerB.tick();
		} else if (activePage == 5) {
			this.uiManagerGeneral.tick();
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
			enableUIManager(uiManagerTransit);
		} else if (activePage == 2) {
			enableUIManager(uiManagerE);
		} else if (activePage == 3) {
			enableUIManager(uiManagerA);
		} else if (activePage == 4) {
			enableUIManager(uiManagerB);
		} else if (activePage == 5) {
			enableUIManager(uiManagerGeneral);
		}
	}
	public void previousPage() {
		
		if (activePage-1 < 1) {
			activePage = nPages;
		} else {
			activePage--;
		}
		
		if (activePage == 1) {
			enableUIManager(uiManagerTransit);
		} else if (activePage == 2) {
			enableUIManager(uiManagerE);
		} else if (activePage == 3) {
			enableUIManager(uiManagerA);
		} else if (activePage == 4) {
			enableUIManager(uiManagerB);
		} else if (activePage == 5) {
			enableUIManager(uiManagerGeneral);
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
		
		Text.drawString(g, simulation.getVersionID(), Assets.idleCol, 10, simulation.getHeight()-10, false, Assets.normalFont);
		
		Text.drawString(g, "Simulation settings", Assets.idleCol, simulation.getWidth()/2, 50, true, Assets.largeFont);
		
		if (activePage == 1) {
			Text.drawString(g, "transit between France and Geneva", Assets.idleCol, simulation.getWidth()/2, 85, true, Assets.largeFont);
			this.uiManagerTransit.render(g);
		} else if (activePage == 2) {
			Text.drawString(g, "entrance E", Assets.idleCol, simulation.getWidth()/2, 85, true, Assets.largeFont);
			this.uiManagerE.render(g);
		} else if (activePage == 3) {
			Text.drawString(g, "entrance A", Assets.idleCol, simulation.getWidth()/2, 85, true, Assets.largeFont);
			this.uiManagerA.render(g);
		} else if (activePage == 4) {
			Text.drawString(g, "entrance B", Assets.idleCol, simulation.getWidth()/2, 85, true, Assets.largeFont);
			this.uiManagerB.render(g);
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
	// From entrance A ----------------------------------------------------------------------------------------
	public UISlider fromEntranceA() {
		return fromEntranceA;
	}
	public UISlider fromEntranceARepartition() {
		return fromEntranceARepartition;
	}
	public UISliderDouble fromEntranceARepartitionRH2() {
		return fromEntranceARepartitionRH2;
	}
	// To entrance B ----------------------------------------------------------------------------------------
	public UISlider toEntranceB() {
		return toEntranceB;
	}
	public UISlider toEntranceBRepartition() {
		return toEntranceBRepartition;
	}
	public UISliderDouble toEntranceBRepartitionRH() {
		return toEntranceBRepartitionRH;
	}
	// From entrance A ----------------------------------------------------------------------------------------
	public UISlider fromEntranceB() {
		return fromEntranceB;
	}
	public UISlider fromEntranceBRepartition() {
		return fromEntranceBRepartition;
	}
	public UISliderDouble fromEntranceBRepartitionRH2() {
		return fromEntranceBRepartitionRH2;
	}
	// ------------------------------------------------------------------------------------------------------
	public UIManager getUIManager() {
		return this.uiManagerTransit;
	}
	public void disableUIManager() {
		simulation.getMouseManager().setUIManager(null);
	}
	public void enableUIManager() {
		simulation.getMouseManager().setUIManager(this.uiManagerTransit);
	}
	public void enableUIManager(UIManager uiManager) {
		simulation.getMouseManager().setUIManager(uiManager);
	}
}
