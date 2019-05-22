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
import ui.UITextButton;
import ui.UITextSwitch;
import utils.Defaults;

public class MenuState extends State {

	private UIManager uiManager;
	private Network network;

	private int buttonYMargin = 20; // margin between buttons
	private int descriptionMargin = 20; // margin between buttons and descriptions

	// Buttons and slider of the menu
	private UITextButton network0, network1;
	private UISlider sizeOfNetwork, globalMultiplier, nOfSimulations, repartition_E_tunnel;
	private UITextSwitch minMaxTransfer;

	private boolean loading = false;

	public MenuState(Simulator simulator) {
		super(simulator);
		this.uiManager = new UIManager(simulator); // initialize the user interface manager
		this.network = simulator.getSimState().getNetwork();

		// Network selection buttons
		// ========================================================================

		network0 = new UITextButton(Assets.menuXStart, Assets.menuYStart + 0 * (Assets.menuButtonH + buttonYMargin), Assets.menuButtonW,
				Assets.menuButtonH, Network.getTitle(0), new ClickListener() {
					@Override
					public void onClick() {
						launchSimulation(0, simulator.getSimState().getNumberOfSimulations());
					}
				});
		this.uiManager.addObject(network0);

		network1 = new UITextButton(Assets.menuXStart, Assets.menuYStart + 1 * (Assets.menuButtonH + buttonYMargin), Assets.menuButtonW,
				Assets.menuButtonH, Network.getTitle(1), new ClickListener() {
					@Override
					public void onClick() {
						launchSimulation(1, simulator.getSimState().getNumberOfSimulations());
					}
				});
		this.uiManager.addObject(network1);

		// Settings buttons
		// =================================================================================

		sizeOfNetwork = new UISlider(simulator, Assets.menuXStart, Assets.menuYStart + 3 * (Assets.sliderHeight + buttonYMargin),
				Assets.sliderWidth, "Size of network", 3, 1, Defaults.getSizeOfNetwork(), false, new ClickListener() {
					@Override
					public void onClick() {

					}
				});
		this.uiManager.addObject(sizeOfNetwork);

		globalMultiplier = new UISlider(simulator, Assets.menuXStart, Assets.menuYStart + 4 * (Assets.sliderHeight + buttonYMargin),
				Assets.sliderWidth, "Additionnal flow", 100, 0, (int) (Defaults.getGlobalFlowMultiplier() * 100 - 100),
				true, new ClickListener() {
					@Override
					public void onClick() {

					}
				});
		this.uiManager.addObject(globalMultiplier);
		nOfSimulations = new UISlider(simulator, Assets.menuXStart, Assets.menuYStart + 5 * (Assets.sliderHeight + buttonYMargin),
				Assets.sliderWidth, "Number of simulations", 100, 1, Defaults.getNumberOfSimulations(), false,
				new ClickListener() {
					@Override
					public void onClick() {

					}
				});
		this.uiManager.addObject(nOfSimulations);
		minMaxTransfer = new UITextSwitch(Assets.menuXStart, Assets.menuYStart + 6 * (Assets.sliderHeight + buttonYMargin),
				Assets.menuButtonW, Assets.menuButtonH, "No transfer", "Min transfer", "Max transfer",
				Defaults.getTransferScenario(), new ClickListener() {
					@Override
					public void onClick() {
						minMaxTransfer.switchIt(); // switch state of the button
						Defaults.setTransferScenario(minMaxTransfer.getChosenArg()); // change the type of transfer
																						// scenario according to the
																						// button
					}
				});
		this.uiManager.addObject(minMaxTransfer);
		repartition_E_tunnel = new UISlider(simulator, Assets.menuXStart, Assets.menuYStart + 7 * (Assets.sliderHeight + buttonYMargin),
				Assets.sliderWidth, "Transfer E-tunnel", 100, 0, Defaults.getRepartitionETunnel(), true,
				new ClickListener() {
					@Override
					public void onClick() {

					}
				});

		// Exit button
		// ======================================================================================

		this.uiManager.addObject(new UITextButton(Assets.menuXStart, Assets.menuYStart + 9 * (Assets.menuButtonH + buttonYMargin),
				Assets.menuButtonW, Assets.menuButtonH, "Exit", new ClickListener() {
					@Override
					public void onClick() {
						System.exit(0); // kill the programm
					}
				}));
	}

	public void tick(int n) {

		// sim settings aren't loading anymore
		simulator.getSimSettingsState().setLoading(false);

		// tick the UIManager
		this.uiManager.tick();

		// if a transfer scenario is selected, show the corresponding slider
		if (Defaults.getTransferScenario() != 0 && !this.uiManager.getObjects().contains(repartition_E_tunnel)) {
			this.uiManager.addObject(repartition_E_tunnel);
		} else if (Defaults.getTransferScenario() == 0 && this.uiManager.getObjects().contains(repartition_E_tunnel)) {
			this.uiManager.removeObject(repartition_E_tunnel);
		}

		// update the global flow multiplier to the button
		Defaults.setGlobalFlowMultiplier((float) (globalMultiplier.getCurrentValue() + 100) / 100.0);

		// update the repartition of transfers between entrance E and tunnel
		Defaults.setRepartitionETunnel(repartition_E_tunnel.getCurrentValue());
	}

	public void tick() {

	}

	public void render(Graphics g) {
		// draw the background
		g.setColor(Assets.bgCol);
		g.fillRect(0, 0, simulator.getWidth(), simulator.getHeight());

		// draw title and subtitle
		Text.drawString(g, simulator.getVersionID(), Assets.idleCol, 10, simulator.getHeight() - 10, false,
				Assets.normalFont);
		Text.drawString(g, "Trafic simulation at CERN", Assets.idleCol, simulator.getWidth() / 2, 100, true,
				Assets.largeFont);

		// if the button network0 is hovered by the UI cursor, change color
		if (network0.isHovering()) {
			Text.drawString(g, Network.getDescription(0), Assets.textCol,
					Assets.menuXStart + Assets.menuButtonW + descriptionMargin,
					Assets.menuYStart + 0 * (Assets.menuButtonH + buttonYMargin) + Assets.menuButtonH / 2 + 5, false,
					Assets.normalFont);
		} else {
			Text.drawString(g, Network.getDescription(0), Assets.idleCol,
					Assets.menuXStart + Assets.menuButtonW + descriptionMargin,
					Assets.menuYStart + 0 * (Assets.menuButtonH + buttonYMargin) + Assets.menuButtonH / 2 + 5, false,
					Assets.normalFont);
		}

		// if the button network1 is hovered by the UI cursor, change color
		if (network1.isHovering()) {
			Text.drawString(g, Network.getDescription(1), Assets.textCol,
					Assets.menuXStart + Assets.menuButtonW + descriptionMargin,
					Assets.menuYStart + 1 * (Assets.menuButtonH + buttonYMargin) + Assets.menuButtonH / 2 + 5, false,
					Assets.normalFont);
		} else {
			Text.drawString(g, Network.getDescription(1), Assets.idleCol,
					Assets.menuXStart + Assets.menuButtonW + descriptionMargin,
					Assets.menuYStart + 1 * (Assets.menuButtonH + buttonYMargin) + Assets.menuButtonH / 2 + 5, false,
					Assets.normalFont);
		}

		// render the UIManager with the current graphical element
		this.uiManager.render(g);

		if (loading) {
			g.setColor(Assets.bgAlphaCol);
			g.fillRect(0, 0, 1000, 700);
			g.setColor(Color.white);
			Text.drawString(g, "Please wait, processing ...", Color.white, network.getSimulation().getWidth() / 2,
					network.getSimulation().getHeight() / 2, true, Assets.largeFont);
		}
	}

	// launch the simulation #nSimulation, with the number of simulations to compute
	// together
	public void launchSimulation(int scenarioID, int numberOfSimulations) {

		if (scenarioID != 2) {
			loading = true;
		}

		// prevents user to continue clicking after state change
		disableUIManager();

		// create a new SimState
		simulator.setSimState(new SimState(simulator));
		simulator.getSimState().setNetwork(
				new Network(simulator, scenarioID, simulator.getMenuState().getSizeOfNetwork().getCurrentValue()));
		simulator.getSimState().init();
		simulator.getSimState().setNumberOfSimulations(nOfSimulations.getCurrentValue());

		// apply the input data to the simulation
		DataManager.applyData(simulator);

		// enable UIManager for the next state, switch state
		simulator.getSimSettingsState().enableUIManager();
		State.setState(simulator.getSimSettingsState());
	}

	// Getters & setters
	// ====================================================================================
	public UISlider getSizeOfNetwork() {
		return this.sizeOfNetwork;
	}

	public UIManager getUIManager() {
		return this.uiManager;
	}

	public void disableUIManager() {
		simulator.getMouseManager().setUIManager(null);
	}

	public void enableUIManager() {
		simulator.getMouseManager().setUIManager(this.uiManager);
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}
}
