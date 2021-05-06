package game;

import game.entities.GameObject;
import game.entities.players.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static game.Level.addToAddQueue;

/**
 * This class handles the inventory's visual representation on screen during game time
 */

public class InventoryBarController extends GameObject {

	private Player player;
	static LinkedList<UIElement> frames = new LinkedList<UIElement>();
	private LinkedList<UIElement> slots = new LinkedList<UIElement>();
	private int inventorySize=3;
	private static BufferedImage frame, frameNotSelected;

	/**
	 * Initializes the frames and slots of the inventory
	 */

	public InventoryBarController(float x, float y, int width, int height, Player player) {
		super(x, y, 3, width, height);
		this.player=player;
		try {
			frame = ImageIO.read(new File("./img/frame.png"));
			frameNotSelected = ImageIO.read(new File("./img/frameNotSelected.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i =0;i < inventorySize;i++)
		{
			frames.add(new UIElement(this.x + 20 + (i*60) , this.y + 10, 50 , 50, "./img/frameNotSelected.png"));
			slots.add(new UIElement(this.x + 20 + (i*60) + 12 , this.y + 10 + 12, 25 , 25, player.getInventory().get(i).getUrl()));

			addToAddQueue(frames.get(i));
			addToAddQueue(slots.get(i));
		}

		selectFrame(2,1);
	}

	/**
	 * Called every frame, this updates the inventory bar if the player's inventory has changed.
	 */
	public void tick() {


		//UPDATE INVENTORY
		if (player.inventoryChanged())
		{
			try
			{
				for (int i =0;i < inventorySize;i++)
				{
					slots.get(i).setImg(ImageIO.read(new File(player.getInventory().get(i).getUrl())));
				}
			}
			catch ( IOException exc )
			{
			}

			player.setInventoryChanged(false);
		}

	}

	public void render(Graphics g, float xOffset, float yOffset) {

	}

	/**
	 * Changes the graphics of the inventory bar to show the selected slot
	 * @param frame The index of the frame to be selected
	 * @param previousFrame The index of the previously selected frame
	 */
	public static void selectFrame(int frame, int previousFrame)
	{
		frames.get(frame).setImg(InventoryBarController.frame);
		frames.get(previousFrame).setImg(InventoryBarController.frameNotSelected);
	}


}
