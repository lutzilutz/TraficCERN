package graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import utils.Utils;


public class ImageLoader {

	public static BufferedImage loadImage(String path) {
		Utils.log("loading " + path + " ... ");
		try {
			BufferedImage bi = ImageIO.read(ImageLoader.class.getResourceAsStream(path));
			Utils.log("success\n");
			return bi;
		} catch (IOException e) {
			Utils.log("failed !\n");
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
}
