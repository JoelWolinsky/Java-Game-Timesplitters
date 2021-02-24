package game.graphics;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Image {
	/**
	 * This will load an image given a url
	 * @param url The url of the image to be loaded
	 * @return The image loaded from the url
	 */
	public static BufferedImage loadImage(String url) {
		try {
			BufferedImage sprite = ImageIO.read(new File(url));;
			return sprite;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error in loading " + url);
			return null;
		}
	}
}
