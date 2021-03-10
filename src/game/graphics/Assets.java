package game.graphics;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Assets {
	

	///Keep in mind that all animations lists should be same length if not it will lead to out of bound problems
	public static LinkedList <BufferedImage> player_idle;
	public static LinkedList <BufferedImage> player_right;
	public static LinkedList <BufferedImage> player_left;
	public static LinkedList <BufferedImage> player_jump;
	public static LinkedList <BufferedImage> player_fall;

	private static int yOffset = 6;
	private static int xOffset = 13;
	private static int yDistance = 37;
	private static int xDistance = 50;
	private static int PLAYER_WIDTH=22;
	private static int PLAYER_HEIGHT=30;


	public void init() {
		SpriteSheet sheet = new SpriteSheet(Image.loadImage("./img/adventurer-Sheet.png"));
		player_idle = new LinkedList<BufferedImage>();
		
		player_idle.add(sheet.crop(xOffset, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset+xDistance, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset+xDistance*2, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset+xDistance*3, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset+xDistance, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		
		player_right = new LinkedList<BufferedImage>();
		
		player_right.add(sheet.crop(xOffset+xDistance, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*3, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*4, yOffset+yDistance, PLAYER_WIDTH	, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*5, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*6, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		
		player_left = new LinkedList<BufferedImage>();
		
		player_left.add(sheet.crop(xOffset, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*1, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*3, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*4, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*5, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
	}



}
