package states;
import java.awt.Graphics;

import data.DataManager;
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
	private int yStart = 100;
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
	private UISliderTriple fromFrToGeRepartition;
	
	private UISlider fromGeToFr,fromGeToFrDuringRH2;
	private UISliderTriple fromGeToFrRepartition;
	
	private UISlider toEntranceE;
	private UISliderDouble toEntranceERepartition, toEntranceERepartitionRH;
	private UIImageButton previous, next;
	private UITextButton run, back;
	
	public SimSettingsState(Simulation simulation) {
		super(simulation);
		this.uiManager1 = new UIManager(simulation);
		this.uiManager2 = new UIManager(simulation);
		this.uiManager3 = new UIManager(simulation);
		
		// From France to Geneva
		
		fromFrToGe = new UISlider(simulation, xStart, yStart, sliderWidth, "Flow from France to Geneva (vhc/day)", 50000, DataManager.nFrGe, false, new ClickListener(){
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
		
		// From Geneva to France ============================================================================
		
		fromGeToFr = new UISlider(simulation, xStart, yStart+4*(sliderHeight+buttonYMargin), sliderWidth, "Flow from Geneva to France (vhc/day)", 50000, DataManager.nGeFr, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(fromGeToFr);
		fromGeToFrRepartition = new UISliderTriple(simulation, xStart, yStart+5*(sliderHeight+buttonYMargin), sliderWidth, "Going to Thoiry, St-Genis, Ferney and Europe", 100, DataManager.nFrGe_fromSW, DataManager.nFrGe_fromSW+DataManager.nFrGe_fromNW, DataManager.nFrGe_fromSW+DataManager.nFrGe_fromNW+DataManager.nFrGe_fromTun, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(fromGeToFrRepartition);
		
		fromGeToFrDuringRH2 = new UISlider(simulation, xStart, yStart+6*(sliderHeight+buttonYMargin), sliderWidth, "Quantity during rush-hours (evening)", 100, 70, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(fromGeToFrDuringRH2);
		
		// To entrance E ====================================================================================
		
		toEntranceE = new UISlider(simulation, xStart, yStart+8*(sliderHeight+buttonYMargin), sliderWidth, "Flow to Entrance E during rush-hours", 4000, DataManager.nToE, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(toEntranceE);
		
		toEntranceERepartition = new UISliderDouble(simulation, xStart, yStart+9*(sliderHeight+buttonYMargin), sliderWidth, "Coming from Thoiry, St-Genis and Ferney", 100, DataManager.nToE_fromSW, DataManager.nToE_fromSW+DataManager.nToE_fromNW, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(toEntranceERepartition);
		
		toEntranceERepartitionRH = new UISliderDouble(simulation, xStart, yStart+10*(sliderHeight+buttonYMargin), sliderWidth, "Distribution between 7am, 8am, 9am", 100, DataManager.nToE_7, DataManager.nToE_7+DataManager.nToE_8, true, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(toEntranceERepartitionRH);
		
		// ==================================================================================================
		
		timePerVhcEntrance = new UISlider(simulation, xStart, yStart+12*(sliderHeight+buttonYMargin), sliderWidth, "Control duration of 1 vehicle at entrances", 30, 8, false, new ClickListener(){
			@Override
			public void onClick() {
				
			}
		});
		this.uiManager1.addObject(timePerVhcEntrance);
		
		// ==================================================================================================
		
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
		int bigR = 8;
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
			this.uiManager1.render(g);
		} else if (activePage == 2) {
			this.uiManager2.render(g);
		} else if (activePage == 3) {
			this.uiManager3.render(g);
		}
		
		renderPageIndication(g);
	}
	
	// Getters & setters ====================================================================================
	public UISlider timePerVhcEntrance() {
		return timePerVhcEntrance;
	}
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
	public UISliderTriple fromFrToGeRepartition() {
		return fromFrToGeRepartition;
	}
	public UISlider fromFrToGeDuringRH() {
		return fromFrToGeDuringRH;
	}
	public UISlider fromGeToFr() {
		return fromGeToFr;
	}
	public UISliderTriple fromGeToFrRepartition() {
		return fromGeToFrRepartition;
	}
	public UISlider fromGeToFrDuringRH2() {
		return fromGeToFrDuringRH2;
	}
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
