package graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageLoader {

	public static BufferedImage loadImage(String path) {
		try {
			BufferedImage bi = ImageIO.read(ImageLoader.class.getResourceAsStream(path));
			return bi;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
}
