package ui;

import java.awt.Color;
import java.awt.Graphics;

import graphics.Assets;
import graphics.Text;

public class UITextButton extends UIObject {
	
	private String text;
	private ClickListener clicker;
	private boolean isActivable = true;
	private boolean isVisible = true;

	public UITextButton(float x, float y, int width, int height, String text, ClickListener clicker) {
		super(x, y, width, height);
		this.text = text;
		this.clicker = clicker;
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		if (isVisible) {
			if (hovering && isActivable) {
				g.setColor(Assets.textCol);
				g.drawRect((int) this.x, (int) this.y, this.width, this.height);
				Text.drawString(g, text, Assets.textCol, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
				
			} else if (!hovering || !isActivable) {
				if (!isActivable) {
					g.setColor(Color.black);
					Text.drawString(g, text, Color.black, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
				} else {
					g.setColor(Assets.idleCol);
					Text.drawString(g, text, Assets.idleCol, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
				}
				g.drawRect((int) this.x, (int) this.y, this.width, this.height);
			}
		}
	}

	@Override
	public void onClick() {
		if (isActivable) {
			clicker.onClick();
		}
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		this.isActivable = isVisible;
	}
	public boolean isVisible() {
		return this.isVisible;
	}
	public void switchActivable() {
		isActivable = !isActivable;
	}
	public void setActivable(boolean isActivable) {
		this.isActivable = isActivable;
	}
}
