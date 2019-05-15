package graphics;

import java.awt.image.BufferedImage;

public class SpriteSheet {

	// image of the sprite sheet
	private BufferedImage sheet;
	
	public SpriteSheet(BufferedImage sheet) {
		this.sheet = sheet;
	}
	
	public BufferedImage crop(int x, int y, int width, int height) {
		
		// return the sprite (a cropped image of the sprite sheet, i.e. an element
		return sheet.getSubimage(x, y, width, height);
	}
}
