package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.Assets;
import graphics.Text;

public class UIImageButton extends UIObject {
	
	private BufferedImage imgIdle, imgActive;
	private ClickListener clicker;
	private boolean isActivable = true;
	private boolean isVisible = true;

	public UIImageButton(float x, float y, int width, int height, BufferedImage imgIdle, BufferedImage imgActive, ClickListener clicker) {
		super(x, y, width, height);
		this.imgIdle = imgIdle;
		this.imgActive = imgActive;
		this.clicker = clicker;
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		if (isVisible) {
			//g.setColor(Assets.vert4Col);
			//g.fillRect((int) this.x, (int) this.y, this.width, this.height);
			
			//g.setFont(this.font);
			if (hovering && isActivable) {
				g.setColor(Assets.textCol);
				g.drawRect((int) this.x, (int) this.y, this.width, this.height);
				g.drawImage(imgIdle, (int) x, (int) y, null);
				//g.setColor(activeColor);
			} else if (!hovering || !isActivable) {
				//if (!isActivable) {
					g.setColor(Assets.idleCol);
					g.drawImage(imgActive, (int) x, (int) y, null);
					
				//} /*else {
					//g.setColor(Assets.idleCol);
					//Text.drawString(g, text, Assets.idleCol, (int) (this.x + (width/2)), (int) (this.y + (height/2)), true, Assets.normalFont);
				//}*/
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
	public void switchActivable() {
		isActivable = !isActivable;
	}
	public void setActivable(boolean isActivable) {
		this.isActivable = isActivable;
	}
}
