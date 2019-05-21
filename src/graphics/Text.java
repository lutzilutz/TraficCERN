package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Text {
	
	// Render method to draw the string
	public static void drawString(Graphics g, String text, Color c, int xPos, int yPos, boolean center, Font font) {
		
		// set color and font to use
		g.setColor(c);
		g.setFont(font);
		
		// copy x and y position
		int x = xPos;
		int y = yPos;
		
		// if text should be centered
		if (center) {
			FontMetrics fm = g.getFontMetrics(font); // get the metrics to compute size of the text
			x = xPos- fm.stringWidth(text) / 2; // offset x position with the width of the text
			y = (int) ((yPos- fm.getHeight() / 2) + 1.2*fm.getAscent()); // offset y position with the height of the text
		}
		
		// draw the string at x and y
		g.drawString(text, x, y);
	}
}
