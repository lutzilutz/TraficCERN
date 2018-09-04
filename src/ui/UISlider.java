package ui;

import java.awt.Graphics;

import graphics.Assets;
import graphics.Text;
import main.Simulation;

public class UISlider extends UIObject {
	
	private ClickListener clicker;
	private Simulation simulation;
	private String text;
	private int nValues;
	private int defaultValue = 2;
	private int currentValue = defaultValue;
	private int width;
	private float radius = 6;

	public UISlider(Simulation simulation, float x, float y, int width, String text, int nValues, int defaultValue, ClickListener clicker) {
		super(x, y, width, 12);
		this.simulation = simulation;
		this.width = width;
		this.text = text;
		this.nValues = nValues;
		this.defaultValue = defaultValue;
		this.currentValue = defaultValue;
		this.clicker = clicker;
	}

	@Override
	public void tick() {
		
		if (hovering && simulation.getMouseManager().isLeftPressed()) {
			onClick();
		}
	}

	@Override
	public void render(Graphics g) {
		
		if (hovering) {
			g.setColor(Assets.textCol);
			float sliderX = this.x + (currentValue * this.width / ((float) nValues));
			g.fillOval((int) (sliderX-radius), (int) (this.y), (int) (2*radius), (int) (2*radius));
			g.fillRect((int) this.x, (int) this.y+5, this.width, 2);
			Text.drawString(g, text, Assets.textCol, (int) (x/2), (int) y+2, true, Assets.normalFont);
			Text.drawString(g, Integer.toString(currentValue), Assets.textCol, (int) (x+width+50), (int) y+9, false, Assets.normalFont);
		} else if (!hovering) {
			g.setColor(Assets.idleCol);
			float sliderX = this.x + (currentValue * this.width / ((float) nValues));
			g.fillOval((int) (sliderX-radius), (int) (this.y), (int) (2*radius), (int) (2*radius));
			g.fillRect((int) this.x, (int) this.y+5, this.width, 2);
			Text.drawString(g, text, Assets.idleCol, (int) (x/2), (int) y+2, true, Assets.normalFont);
			Text.drawString(g, Integer.toString(currentValue), Assets.idleCol, (int) (x+width+50), (int) y+9, false, Assets.normalFont);
		}
	}

	@Override
	public void onClick() {
		clicker.onClick();
		
		currentValue = Math.max(0, Math.min(nValues, Math.round((simulation.getMouseManager().getMouseX()-this.x) / ((float) (this.width/(float)nValues)))));
		
	}
	public int getCurrentValue() {
		return this.currentValue;
	}
}
