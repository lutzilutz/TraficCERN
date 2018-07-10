package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class Assets {

	// Dimensions
	public static int buttonW = 115, buttonH = 35;
	public static int buttonXStart = 20, buttonYStart = 20;
	public static int buttonSpacing = 10;
	
	// Colors
	public static Color bgCol = new Color(50,50,50);
	public static Color textCol = new Color(220,220,220);
	public static Color idleCol = new Color(120,120,120);
	
	// Fonts
	public static Font normalFont = new Font("Arial", Font.PLAIN, 12);
	
	// Images
	public static BufferedImage pauseIdle, pauseActive, playIdle, playActive, fastIdle, fastActive, fastFastIdle, fastFastActive;
	
	public static void init() {
		
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/resources/img/buttons_spritesheet.png"));
		
		pauseIdle = sheet.crop(0, 0, buttonW, buttonH);
	}
	
}
