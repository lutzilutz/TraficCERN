package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class Assets {

	// Dimensions
	public static int buttonW = 110, buttonH = 34;
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
		pauseActive = sheet.crop(buttonW*1, 0, buttonW, buttonH);
		playIdle = sheet.crop(buttonW*2, 0, buttonW, buttonH);
		playActive = sheet.crop(buttonW*3, 0, buttonW, buttonH);
		fastIdle = sheet.crop(buttonW*0, buttonH*1, buttonW, buttonH);
		fastActive = sheet.crop(buttonW*1, buttonH*1, buttonW, buttonH);
		fastFastIdle = sheet.crop(buttonW*2, buttonH*1, buttonW, buttonH);
		fastFastActive = sheet.crop(buttonW*3, buttonH*1, buttonW, buttonH);
	}
	
}
