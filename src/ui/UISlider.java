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
	private int minValue = 0;
	private int defaultValue = 2;
	private int currentValue = defaultValue;
	private int width;
	private float radius = 6;
	private boolean percentage = false;
	private boolean printBothValues = false;

	public UISlider(Simulation simulation, float x, float y, int width, String text, int nValues, int defaultValue, boolean percentage, ClickListener clicker) {
		super(x, y, width, 12);
		this.simulation = simulation;
		this.width = width;
		this.text = text;
		this.nValues = nValues;
		this.defaultValue = defaultValue;
		this.currentValue = defaultValue;
		this.clicker = clicker;
		this.percentage = percentage;
	}
	public UISlider(Simulation simulation, float x, float y, int width, String text, int nValues, int minValue, int defaultValue, boolean percentage, ClickListener clicker) {
		super(x, y, width, 12);
		this.simulation = simulation;
		this.width = width;
		this.text = text;
		this.nValues = nValues;
		this.minValue = minValue;
		this.defaultValue = defaultValue;
		this.currentValue = defaultValue;
		this.clicker = clicker;
		this.percentage = percentage;
	}
	public UISlider(Simulation simulation, float x, float y, int width, String text, int nValues, int defaultValue, boolean percentage, boolean printBothValues, ClickListener clicker) {
		super(x, y, width, 12);
		this.simulation = simulation;
		this.width = width;
		this.text = text;
		this.nValues = nValues;
		this.defaultValue = defaultValue;
		this.currentValue = defaultValue;
		this.clicker = clicker;
		this.percentage = percentage;
		this.printBothValues = printBothValues;
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
			float sliderX = this.x + ((currentValue-minValue) * this.width / ((float) (nValues-minValue)));
			g.fillOval((int) (sliderX-radius), (int) (this.y), (int) (2*radius), (int) (2*radius));
			g.fillRect((int) this.x, (int) this.y+5, this.width, 2);
			Text.drawString(g, text, Assets.textCol, (int) (x/2), (int) y+2, true, Assets.normalFont);
			
			if (!percentage) {
				if (!printBothValues) {
					Text.drawString(g, Integer.toString(currentValue), Assets.textCol, (int) (x+width+50), (int) y+9, false, Assets.normalFont);
				} else {
					Text.drawString(g, Integer.toString(currentValue) + " - " + Integer.toString(nValues-currentValue), Assets.textCol, (int) (x+width+50), (int) y+9, false, Assets.normalFont);
				}
			} else {
				if (!printBothValues) {
					Text.drawString(g, Integer.toString(currentValue) + "%", Assets.textCol, (int) (x+width+50), (int) y+9, false, Assets.normalFont);
				} else {
					Text.drawString(g, Integer.toString(currentValue) + "% - " + Integer.toString(nValues-currentValue) + "%", Assets.textCol, (int) (x+width+50), (int) y+9, false, Assets.normalFont);
				}
			}
			
			Text.drawString(g, Integer.toString(minValue), Assets.textCol, (int) (x), (int) y+15, true, Assets.normalFont);
			Text.drawString(g, Integer.toString(nValues), Assets.textCol, (int) (x+width), (int) y+15, true, Assets.normalFont);
		} else if (!hovering) {
			g.setColor(Assets.idleCol);
			float sliderX = this.x + ((currentValue-minValue) * this.width / ((float) (nValues-minValue)));
			g.fillOval((int) (sliderX-radius), (int) (this.y), (int) (2*radius), (int) (2*radius));
			g.fillRect((int) this.x, (int) this.y+5, this.width, 2);
			Text.drawString(g, text, Assets.idleCol, (int) (x/2), (int) y+2, true, Assets.normalFont);
			
			if (!percentage) {
				if (!printBothValues) {
					Text.drawString(g, Integer.toString(currentValue), Assets.idleCol, (int) (x+width+50), (int) y+9, false, Assets.normalFont);
				} else {
					Text.drawString(g, Integer.toString(currentValue) + " - " + Integer.toString(nValues-currentValue), Assets.idleCol, (int) (x+width+50), (int) y+9, false, Assets.normalFont);
				}
			} else {
				if (!printBothValues) {
					Text.drawString(g, Integer.toString(currentValue) + "%", Assets.idleCol, (int) (x+width+50), (int) y+9, false, Assets.normalFont);
				} else {
					Text.drawString(g, Integer.toString(currentValue) + "% - " + Integer.toString(nValues-currentValue) + "%", Assets.idleCol, (int) (x+width+50), (int) y+9, false, Assets.normalFont);
				}
			}
			
			Text.drawString(g, Integer.toString(minValue), Assets.idleCol, (int) (x), (int) y+15, true, Assets.normalFont);
			Text.drawString(g, Integer.toString(nValues), Assets.idleCol, (int) (x+width), (int) y+15, true, Assets.normalFont);
		}
	}

	@Override
	public void onClick() {
		clicker.onClick();
		
		//currentValue = Math.max(minValue, Math.min(nValues, Math.round((simulation.getMouseManager().getMouseX()-this.x) / ((float) (this.width/(float)(nValues-0*minValue))))));
		currentValue = Math.max(1*minValue, minValue+Math.min(nValues-minValue, Math.round((simulation.getMouseManager().getMouseX()-this.x) / ((float) (this.width/(float)(nValues-1*minValue))))));
		
	}
	public int getCurrentValue() {
		return this.currentValue;
	}
	public void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
		this.currentValue = defaultValue;
	}
}
