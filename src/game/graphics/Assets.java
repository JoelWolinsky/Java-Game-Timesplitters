package game.graphics;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Assets {
	

	///Keep in mind that all animations lists should be same length if not it will lead to out of bound problems
	public static ArrayList <BufferedImage> player_idle;
	public static ArrayList <BufferedImage> player_right;
	public static ArrayList <BufferedImage> player_left;
	public static ArrayList <BufferedImage> player_other;

	public static ArrayList<Asset> assets = new ArrayList<>();
	String[] splitted;
	private static int yOffset = 6;
	private static int xOffset = 13;
	private static int yDistance = 37;
	private static int xDistance = 50;
	private static int PLAYER_WIDTH=22;
	private static int PLAYER_HEIGHT=30;

	public Assets(){
		parseAssets();
	}


	public void init() {
		SpriteSheet sheet = new SpriteSheet(Image.loadImage("./img/adventurer-Sheet.png"));
		player_idle = new ArrayList<BufferedImage>();
		
		player_idle.add(sheet.crop(xOffset, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset+xDistance, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset+xDistance*2, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset+xDistance*3, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset+xDistance, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));

		player_other = new ArrayList<BufferedImage>();
		for (BufferedImage b : player_idle)
		{
			BufferedImage bb = b;
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-bb.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			bb = op.filter(bb, null);
			player_other.add(bb);
		}

		
		player_right = new ArrayList<BufferedImage>();
		
		player_right.add(sheet.crop(xOffset+xDistance, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*3, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*4, yOffset+yDistance, PLAYER_WIDTH	, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*5, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*6, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		
		player_left = new ArrayList<BufferedImage>();
		
		player_left.add(sheet.crop(xOffset+xDistance*5, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*4, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*3, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*1, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*0, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));

		Asset as = new Asset("player",new HashMap<AnimationStates, Animation>());

		as.getAnimations().put(AnimationStates.IDLE, new Animation(15,player_idle));
		as.getAnimations().put(AnimationStates.RIGHT, new Animation(15,player_right));
		as.getAnimations().put(AnimationStates.LEFT, new Animation(15,player_left));
		as.getAnimations().put(AnimationStates.OTHER, new Animation(15,player_other));
		assets.add(as);
	}


	public void parseAssets(){


		try
		{
			File myObj = new File("./src/game/assets.txt");
			Scanner myReader = new Scanner(myObj);
			Asset asset = null;

			while (myReader.hasNextLine()) {

				String data = myReader.nextLine();
				splitted = data.split("\\s+");

				if (splitted.length==1) {
					if (asset!=null)
						assets.add(asset);
					asset = new Asset(splitted[0], new HashMap<AnimationStates, Animation>());
				}
				else
				{
					switch (splitted[0]){
						case "IDLE":
							asset.getAnimations().put(AnimationStates.IDLE, new Animation(15,createURLS(1)));
							break;
						case "LEFT":
							asset.getAnimations().put(AnimationStates.LEFT, new Animation(15,createURLS(1)));
							break;
						case "RIGHT":
							asset.getAnimations().put(AnimationStates.RIGHT, new Animation(15,createURLS(1)));
							break;
						case "JUMP":
							asset.getAnimations().put(AnimationStates.JUMP, new Animation(15,createURLS(1)));
							break;
						case "OTHER":
							asset.getAnimations().put(AnimationStates.OTHER, new Animation(15,createURLS(1)));
							break;
					}

				}
			}

			assets.add(asset);

			myReader.close();


		} catch (FileNotFoundException e) {
		System.out.println("An error occurred.");
		e.printStackTrace();
		}

	}

	public static HashMap<AnimationStates,Animation> getAnimations(String name){

		for (Asset a : assets)
		{
			if (a.getName().equals(name))
				return a.getAnimations();
		}

		return null;
	}

	public String[] createURLS(int index)
	{
		//prepares a string array with the urls
		List<String> list = new ArrayList<String>();
		for (int l = index+1 ; l < index+1 + Integer.parseInt(splitted[index]); l++)
			list.add("./img/".concat(splitted[l]));

		String[] arr = list.toArray(new String[0]);

		return arr;
	}


}
