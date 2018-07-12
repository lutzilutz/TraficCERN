package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.Assets;

public class UIImageButton extends UIObject {
	
	private BufferedImage imgIdle1, imgActive1, imgIdle2, imgActive2;
	private ClickListener clicker;
	private boolean isActivable = true;
	private boolean isVisible = true;
	private boolean oneMode = true;
	private boolean isMode1 = true;

	public UIImageButton(float x, float y, int width, int height, BufferedImage imgIdle, BufferedImage imgActive, ClickListener clicker) {
		super(x, y, width, height);
		this.imgIdle1 = imgIdle;
		this.imgActive1 = imgActive;
		this.clicker = clicker;
	}
	public UIImageButton(float x, float y, int width, int height, BufferedImage imgIdle1, BufferedImage imgActive1, BufferedImage imgIdle2, BufferedImage imgActive2, ClickListener clicker) {
		super(x, y, width, height);
		this.imgIdle1 = imgIdle1;
		this.imgActive1 = imgActive1;
		this.imgIdle2 = imgIdle2;
		this.imgActive2 = imgActive2;
		this.clicker = clicker;
		this.oneMode = false;
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
				if (isMode1) {
					g.drawImage(imgActive1, (int) x, (int) y, null);
				} else {
					g.drawImage(imgActive2, (int) x, (int) y, null);
				}
			} else if (!hovering || !isActivable) {
				g.setColor(Assets.idleCol);
				
				if (isMode1) {
					g.drawImage(imgIdle1, (int) x, (int) y, null);
				} else {
					g.drawImage(imgIdle2, (int) x, (int) y, null);
				}
				g.drawRect((int) this.x, (int) this.y, this.width, this.height);
			}
		}
	}

	@Override
	public void onClick() {
		if (isActivable) {
			if (!oneMode) {
				isMode1 = !isMode1;
			}
			clicker.onClick();
		}
	}
	public void switchMode() {
		isMode1 = !isMode1;
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
