package game.graphics;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	
	private BufferedImage sheet;
	
	public SpriteSheet(BufferedImage sheet) {
		this.sheet = sheet;
	}
	
	/**
	 * Gets a single image from the spritesheet
	 * @param x The x position of the image in the sprite sheet
	 * @param y The y position of the image in the sprite sheet
	 * @param width The width of the image in the sprite sheet
	 * @param height The height of the image in the sprite sheet
	 * @return The images that has been extracted from the sprite sheet
	 */
	public BufferedImage crop (int x, int y, int width, int height) {
		return sheet.getSubimage(x, y, width, height);
	}
}
