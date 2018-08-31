package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class Assets {

	// Dimensions
	public static int buttonW = 90, buttonH = 34;
	public static int buttonXStart = 20, buttonYStart = 30;
	public static int buttonSpacing = 10;
	
	// Colors
	public static Color bgCol = new Color(50,50,50);
	public static Color textCol = new Color(250,250,250);
	public static Color idleCol = new Color(150,150,150);
	public static Color zoneCERNCol = new Color(0,100,0);
	public static Color zoneCERNtextCol = new Color(120,255,120);
	
	// Fonts
	public static Font normalFont = new Font("Arial", Font.PLAIN, 12);
	public static Font normalBoldFont = new Font("Arial", Font.BOLD, 12);
	public static Font largeFont = new Font("Arial", Font.PLAIN, 20);
	public static Font hugeFont = new Font("Arial", Font.PLAIN, 50);
	
	// Images
	public static BufferedImage pauseIdle, pauseActive, playIdle, playActive, restartIdle, restartActive, fastIdle, fastActive, fastFastIdle, fastFastActive, fastFastFastIdle, fastFastFastActive;
	
	public static void init() {
		
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/resources/img/buttons_spritesheet.png"));
		
		pauseIdle = sheet.crop(0, 0, buttonW, buttonH);
		pauseActive = sheet.crop(buttonW*1, 0, buttonW, buttonH);
		playIdle = sheet.crop(buttonW*2, 0, buttonW, buttonH);
		playActive = sheet.crop(buttonW*3, 0, buttonW, buttonH);
		restartIdle = sheet.crop(buttonW*0, buttonH*1, buttonW, buttonH);
		restartActive = sheet.crop(buttonW*1, buttonH*1, buttonW, buttonH);
		fastIdle = sheet.crop(buttonW*2, buttonH*1, buttonW, buttonH);
		fastActive = sheet.crop(buttonW*3, buttonH*1, buttonW, buttonH);
		fastFastIdle = sheet.crop(buttonW*0, buttonH*2, buttonW, buttonH);
		fastFastActive = sheet.crop(buttonW*1, buttonH*2, buttonW, buttonH);
		fastFastFastIdle = sheet.crop(buttonW*2, buttonH*2, buttonW, buttonH);
		fastFastFastActive = sheet.crop(buttonW*3, buttonH*2, buttonW, buttonH);
	}
	
}
