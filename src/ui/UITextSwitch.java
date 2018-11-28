package ui;

import java.awt.Graphics;

import graphics.Assets;
import graphics.Text;

public class UITextSwitch extends UIObject {
	
	private String text1, text2, text3;
	private int nArgs = 2;
	private ClickListener clicker;
	private int chosenArg;

	public UITextSwitch(float x, float y, int width, int height, String text1, String text2, boolean chosen1, ClickListener clicker) {
		super(x, y, width, height);
		this.text1 = text1;
		this.text2 = text2;
		this.clicker = clicker;
		if (chosen1) {chosenArg = 0;}
		else {chosenArg = 1;}
		nArgs = 2;
	}
	public UITextSwitch(float x, float y, int width, int height, String text1, String text2, int chosenArg, ClickListener clicker) {
		super(x, y, width, height);
		this.text1 = text1;
		this.text2 = text2;
		this.clicker = clicker;
		this.chosenArg = chosenArg;
		nArgs = 2;
	}
	public UITextSwitch(float x, float y, int width, int height, String text1, String text2, String text3, int chosenArg, ClickListener clicker) {
		super(x, y, width, height);
		this.text1 = text1;
		this.text2 = text2;
		this.text3 = text3;
		this.clicker = clicker;
		this.chosenArg = chosenArg;
		nArgs = 3;
	}


	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		if (hovering) {
			g.setColor(Assets.textCol);
			g.drawRect((int) this.x, (int) this.y, this.width, this.height);
			if (chosenArg == 0) {
				Text.drawString(g, text1, Assets.textCol, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
			} else if (chosenArg == 1) {
				Text.drawString(g, text2, Assets.textCol, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
			} else if (chosenArg == 2) {
				Text.drawString(g, text3, Assets.textCol, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
			}
		} else {
			g.setColor(Assets.idleCol);
			g.drawRect((int) this.x, (int) this.y, this.width, this.height);
			if (chosenArg==0) {
				Text.drawString(g, text1, Assets.idleCol, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
			} else if (chosenArg == 1) {
				Text.drawString(g, text2, Assets.idleCol, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
			} else if (chosenArg == 2) {
				Text.drawString(g, text3, Assets.textCol, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
			}
		}
	}

	@Override
	public void onClick() {
		clicker.onClick();
	}

	public void switchIt() {
		chosenArg = (chosenArg+1)%nArgs;
	}
	public int getChosenArg() {
		return this.chosenArg;
	}
}
