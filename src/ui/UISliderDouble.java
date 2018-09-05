package ui;

import java.awt.Graphics;

import graphics.Assets;
import graphics.Text;
import main.Simulation;

public class UISliderDouble extends UIObject {
	
	private ClickListener clicker;
	private Simulation simulation;
	private String text;
	private int nValues;
	private int defaultValue1 = 2;
	private int currentValue1 = defaultValue1;
	private int defaultValue2 = 3;
	private int currentValue2 = defaultValue2;
	private int width;
	private float radius = 6;
	private boolean percentage = false;
	private boolean firstSelected = true;
	private boolean leftPressed = false;

	public UISliderDouble(Simulation simulation, float x, float y, int width, String text, int nValues, int defaultValue1, int defaultValue2, boolean percentage, ClickListener clicker) {
		super(x, y, width, 12);
		this.simulation = simulation;
		this.width = width;
		this.text = text;
		this.nValues = nValues;
		this.defaultValue1 = defaultValue1;
		this.defaultValue2 = defaultValue2;
		this.currentValue1 = defaultValue1;
		this.currentValue2 = defaultValue2;
		this.clicker = clicker;
		this.percentage = percentage;
	}

	@Override
	public void tick() {
		
		if (hovering && simulation.getMouseManager().isLeftPressed()) {
			if (!leftPressed) {
				if (Math.abs(simulation.getMouseManager().getMouseX()-x - (currentValue1*(width/(float)nValues))) < Math.abs(simulation.getMouseManager().getMouseX()-x - (currentValue2*(width/(float)nValues)))) {
					firstSelected = true;
				} else {
					firstSelected = false;
				}
				leftPressed = true;
			}
			onClick();
		} else {
			leftPressed = false;
		}
	}

	@Override
	public void render(Graphics g) {
		
		if (hovering) {
			g.setColor(Assets.textCol);
			float slider1X = this.x + (currentValue1 * this.width / ((float) nValues));
			g.fillOval((int) (slider1X-radius), (int) (this.y), (int) (2*radius), (int) (2*radius));
			float slider2X = this.x + (currentValue2 * this.width / ((float) nValues));
			g.fillOval((int) (slider2X-radius), (int) (this.y), (int) (2*radius), (int) (2*radius));
			g.fillRect((int) this.x, (int) this.y+5, this.width, 2);
			Text.drawString(g, text, Assets.textCol, (int) (x/2), (int) y+2, true, Assets.normalFont);
			if (!percentage) {
				Text.drawString(g, Integer.toString(currentValue1) + " - " + Integer.toString(currentValue2), Assets.textCol, (int) (x+width+50), (int) y+9, false, Assets.normalFont);
			} else {
				Text.drawString(g, Integer.toString(currentValue1) + "% - " + Integer.toString(currentValue2-currentValue1) + "% - " + Integer.toString(nValues-currentValue2) + "%", Assets.textCol, (int) (x+width+20), (int) y+9, false, Assets.normalFont);
			}
			Text.drawString(g, Integer.toString(0), Assets.textCol, (int) (x), (int) y+15, true, Assets.normalFont);
			Text.drawString(g, Integer.toString(nValues), Assets.textCol, (int) (x+width), (int) y+15, true, Assets.normalFont);
		} else if (!hovering) {
			g.setColor(Assets.idleCol);
			float slider1X = this.x + (currentValue1 * this.width / ((float) nValues));
			g.fillOval((int) (slider1X-radius), (int) (this.y), (int) (2*radius), (int) (2*radius));
			float slider2X = this.x + (currentValue2 * this.width / ((float) nValues));
			g.fillOval((int) (slider2X-radius), (int) (this.y), (int) (2*radius), (int) (2*radius));
			g.fillRect((int) this.x, (int) this.y+5, this.width, 2);
			Text.drawString(g, text, Assets.idleCol, (int) (x/2), (int) y+2, true, Assets.normalFont);
			if (!percentage) {
				Text.drawString(g, Integer.toString(currentValue1) + " - " + Integer.toString(currentValue2), Assets.idleCol, (int) (x+width+50), (int) y+9, false, Assets.normalFont);
			} else {
				Text.drawString(g, Integer.toString(currentValue1) + "% - " + Integer.toString(currentValue2-currentValue1) + "% - " + Integer.toString(nValues-currentValue2) + "%", Assets.idleCol, (int) (x+width+20), (int) y+9, false, Assets.normalFont);
			}
			Text.drawString(g, Integer.toString(0), Assets.idleCol, (int) (x), (int) y+15, true, Assets.normalFont);
			Text.drawString(g, Integer.toString(nValues), Assets.idleCol, (int) (x+width), (int) y+15, true, Assets.normalFont);
		}
	}

	@Override
	public void onClick() {
		clicker.onClick();
		
		if (firstSelected) {
			currentValue1 = Math.max(0, Math.min(nValues, Math.round((simulation.getMouseManager().getMouseX()-this.x) / ((float) (this.width/(float)nValues)))));
			if (currentValue1 >= currentValue2) {
				currentValue1 = currentValue2-1;
			}
		} else {
			currentValue2 = Math.max(0, Math.min(nValues, Math.round((simulation.getMouseManager().getMouseX()-this.x) / ((float) (this.width/(float)nValues)))));
			if (currentValue2 <= currentValue1) {
				currentValue2 = currentValue1+1;
			}
		}
	}
	public int getCurrentValue1() {
		return this.currentValue1;
	}
	public int getCurrentValue2() {
		return this.currentValue2;
	}
}
