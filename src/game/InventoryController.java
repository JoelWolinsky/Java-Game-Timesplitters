package game;

import game.entities.GameObject;
import game.entities.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class InventoryController extends GameObject {

	private Player player;
	LinkedList<UIElement> slots = new LinkedList<UIElement>();
	private int inventorySize=3;
	int i =0;

	public InventoryController(float x, float y, int width, int height, Player player, Level currentLevel, MapPart...urls) {
		super(x, y, 3, width, height);
		this.player=player;

		for (int i =0;i < inventorySize;i++)
		{
			currentLevel.addEntity(new UIElement(this.x + 20 + (i*60) , this.y + 10, 50 , 50, "./img/frame.png"));
			slots.add(new UIElement(this.x + 20 + (i*60) + 12 , this.y + 10 + 12, 25 , 25, player.getInventory().get(i).getUrl()));
			currentLevel.addEntity(slots.get(i));
		}


	}

	public void tick() {

		if (player.inventoryChanged())
		{
			try
			{
				for (int i =0;i < inventorySize;i++)
				{
					slots.get(i).setImg(ImageIO.read(new File(player.getInventory().get(i).getUrl())));
					//slots.get(0).setImg(ImageIO.read(new File("./img/empty.png")));
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

			//g.setColor(Color.magenta);
			//g.fillRect((int)(this.x + f),(int)(this.y + h),this.width,this.height);
			//g.drawImage(img,(int)(this.x),(int)(this.y),null);
	}


}
