package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Text {
	
	public static void drawString(Graphics g, String text, Color c, int xPos, int yPos, boolean center, Font font) {
		
		g.setColor(c);
		g.setFont(font);
		int x = xPos;
		int y = yPos;
		
		if (center) {
			FontMetrics fm = g.getFontMetrics(font);
			x = xPos- fm.stringWidth(text) / 2;
			y = (int) ((yPos- fm.getHeight() / 2) + 1.2*fm.getAscent());		
		}
		
		g.drawString(text, x, y);
	}
}
