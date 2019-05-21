package graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import utils.Utils;

public class ImageLoader {

	// Load image from "path" to a BufferedImage object
	public static BufferedImage loadImage(String path) {
		
		// log the loading of the image
		Utils.logInfo("Loading " + path + " ... ");
		
		// try to read the image as a stream
		try {
			BufferedImage bi = ImageIO.read(ImageLoader.class.getResourceAsStream(path));
			Utils.logln("success");
			return bi;
		}
		// or log the exception and close the program
		catch (IOException e) {
			Utils.logln("failed ! Couldn't read image " + path);
			Utils.log(e);
			System.exit(1);
		}
		return null;
	}
}
