package ui;

import java.awt.Graphics;

import graphics.Assets;
import graphics.Text;

public class UITextSwitch extends UIObject {
	
	private String text1, text2;
	private ClickListener clicker;
	private boolean chosen1;

	public UITextSwitch(float x, float y, int width, int height, String text1, String text2, boolean chosen1, ClickListener clicker) {
		super(x, y, width, height);
		this.text1 = text1;
		this.text2 = text2;
		this.clicker = clicker;
		this.chosen1 = chosen1;
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		if (hovering) {
			g.setColor(Assets.textCol);
			g.drawRect((int) this.x, (int) this.y, this.width, this.height);
			if (chosen1) {
				Text.drawString(g, text1, Assets.textCol, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
			} else {
				Text.drawString(g, text2, Assets.textCol, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
			}
		} else {
			g.setColor(Assets.idleCol);
			g.drawRect((int) this.x, (int) this.y, this.width, this.height);
			if (chosen1) {
				Text.drawString(g, text1, Assets.idleCol, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
			} else {
				Text.drawString(g, text2, Assets.idleCol, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
			}
		}
	}

	@Override
	public void onClick() {
		clicker.onClick();
	}

	public void switchIt() {
		if (this.chosen1) {
			this.chosen1 = false;
		} else {
			this.chosen1 = true;
		}
	}
	public boolean isChosen() {
		return this.chosen1;
	}
}
