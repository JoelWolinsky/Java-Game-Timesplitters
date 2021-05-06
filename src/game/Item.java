package game;

import game.entities.GameObject;
import game.entities.players.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class represents an item in the player's inventory
 */

public class Item extends GameObject {
	private Player player;
	private String url;
	private boolean addItem = false;
	private String itemToAdd = "";

	/**
	 * @param player the players with which this object will be associated
	 * @param url identifies what the object will be eg. Shoes,Banana etc.
	 */
	public Item(float x, float y, int width, int height, Player player, String url) {
		super(x, y, 3, width, height);
		this.player=player;
		this.url=url;

	}

	public void tick() {
	}

	public void render(Graphics g, float xOffset, float yOffset) {
	}

	/**
	 * This method gets the effect of the item based on the items url/identity
	 */

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
