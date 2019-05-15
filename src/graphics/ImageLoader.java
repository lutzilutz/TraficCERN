package graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import utils.Utils;


public class ImageLoader {

	public static BufferedImage loadImage(String path) {
		Utils.logInfo("Loading " + path + " ... ");
		try {
			BufferedImage bi = ImageIO.read(ImageLoader.class.getResourceAsStream(path));
			Utils.logln("success");
			return bi;
		} catch (IOException e) {
			Utils.logln("failed ! Couldn't read image " + path);
			Utils.log(e);
			System.exit(1);
		}
		return null;
	}
}
