package game;

import game.entities.GameObject;
import game.entities.players.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Blip extends GameObject {

	private BufferedImage img;
	private GameObject target;
	private boolean visible = true;
	private int totalNrBlocks=0;

	public Blip(float x, float y, int width, int height, GameObject target, int totalNrBlocks, String url) {
		super(x, y, 3, width, height);
		this.target =target;
		this.totalNrBlocks=totalNrBlocks;
		try
		{
			img = ImageIO.read( new File(url));
			this.width = img.getWidth();
			this.height = img.getHeight();
		}
		catch ( IOException exc )
		{
		}

	}

	public void tick() {

		if (target.isMoving())
			this.x= target.getX()/(0.71f*totalNrBlocks);

	}

	public void render(Graphics g, float xOffset, float yOffset) {

		if(visible)
			g.drawImage(img,(int)(this.x+20),(int)(this.y),null);
	}


	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public GameObject getTarget() {
		return target;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}
}
