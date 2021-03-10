package game.graphics;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Assets {
	
	private static final int PLAYER_WIDTH = 50;
	private static final int PLAYER_HEIGHT = 37;
	
	///Keep in mind that all animations lists should be same length if not it will lead to out of bound problems
	public static LinkedList <BufferedImage> player_idle;
	public static LinkedList <BufferedImage> player_right;
	public static LinkedList <BufferedImage> player_left;
	public static LinkedList <BufferedImage> player_jump;
	public static LinkedList <BufferedImage> player_fall;
	
	public static void init () {
		SpriteSheet sheet = new SpriteSheet(Image.loadImage("./img/adventurer-Sheet.png"));
		player_idle = new LinkedList<BufferedImage>();
		
		player_idle.add(sheet.crop(0, 0, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(PLAYER_WIDTH, 0, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(PLAYER_WIDTH*2, 0, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(PLAYER_WIDTH*3, 0, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(0, 0, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(PLAYER_WIDTH, 0, PLAYER_WIDTH, PLAYER_HEIGHT));
		
		player_right = new LinkedList<BufferedImage>();
		
		player_right.add(sheet.crop(PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(PLAYER_WIDTH*2, PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(PLAYER_WIDTH*3, PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(PLAYER_WIDTH*4, PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(PLAYER_WIDTH*5, PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(PLAYER_WIDTH*6, PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT));
		
		SpriteSheet sheet2 = new SpriteSheet(Image.loadImage("./img/adventurer-reverse-Sheet.png"));
		
		player_left = new LinkedList<BufferedImage>();
		
		player_left.add(sheet2.crop(PLAYER_WIDTH*5, PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet2.crop(PLAYER_WIDTH*4, PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet2.crop(PLAYER_WIDTH*3, PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet2.crop(PLAYER_WIDTH*2, PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet2.crop(PLAYER_WIDTH*1, PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet2.crop(0, PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT));
	}

}
