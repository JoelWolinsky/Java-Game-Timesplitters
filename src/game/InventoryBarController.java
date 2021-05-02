package game;

import game.entities.GameObject;
import game.entities.players.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class InventoryBarController extends GameObject {

	private Player player;
	static LinkedList<UIElement> frames = new LinkedList<UIElement>();
	private LinkedList<UIElement> slots = new LinkedList<UIElement>();
	private int inventorySize=3;

	public InventoryBarController(float x, float y, int width, int height, Player player, Level currentLevel) {
		super(x, y, 3, width, height);
		this.player=player;

		for (int i =0;i < inventorySize;i++)
		{
			frames.add(new UIElement(this.x + 20 + (i*60) , this.y + 10, 50 , 50, "./img/frameNotSelected.png"));
			slots.add(new UIElement(this.x + 20 + (i*60) + 12 , this.y + 10 + 12, 25 , 25, player.getInventory().get(i).getUrl()));

			currentLevel.addToAddQueue(frames.get(i));
			currentLevel.addToAddQueue(slots.get(i));
		}


	}

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
				//TODO: Handle exception.
			}

			player.setInventoryChanged(false);
		}

	}

	public void render(Graphics g, float f, float h) {

	}

	public static void selectFrame(int frame, int previousFrame)
	{
		try {
			frames.get(frame).setImg(ImageIO.read(new File("./img/frame.png")));
			frames.get(previousFrame).setImg(ImageIO.read(new File("./img/frameNotSelected.png")));
		} catch (IOException exc) {
			//TODO: Handle exception.
		}
	}


}
