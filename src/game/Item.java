package game;

import game.attributes.CollidingObject;
import game.attributes.GravityObject;
import game.attributes.SolidCollider;
import game.entities.GameObject;
import game.entities.players.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Item extends GameObject {
	private boolean visible = true;
	private Player player;
	private float velX = 5;
	private float velY = 5;
	private int offset= 20;
	private int totalNrBlocks;
	private BufferedImage img;
	private String url;
	private boolean addItem = false;
	private String itemToAdd = "";

	public Item(float x, float y, int width, int height, Player player, String url) {
		super(x, y, 3, width, height);

		this.player=player;
		this.url=url;

		try
		{
			img = ImageIO.read( new File(url));
		}
		catch ( IOException exc )
		{
			//TODO: Handle exception.
		}


	}

	public void tick() {


	}

	public void render(Graphics g, float xOffset, float yOffset) {

			//g.setColor(Color.magenta);
			//g.fillRect((int)(this.x + f),(int)(this.y + h),this.width,this.height);
			//g.drawImage(img,(int)(this.x),(int)(this.y),null);
	}

	public void getEffect(){

		switch (url){
			case "./img/shoes.png":
				player.setRunSpeed(5.0f);
				player.setJumpGravity(-7.5f);
				break;
			case "./img/jump.png":
				player.setCanDoubleJump(true);
				break;
			case "./img/banana.png":
				this.addItem=true;
				itemToAdd=url.substring(6,12);
				break;
			case "./img/fart1.png":
				this.addItem=true;
				itemToAdd=url.substring(6,10);
				break;
		}

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setAddItem(boolean addItem) {
		this.addItem = addItem;
	}

	public boolean getAddItem() {
		return addItem;
	}

	public String getItemToAdd() {
		return itemToAdd;
	}


}
