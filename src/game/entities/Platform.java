package game.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import game.GameObject;
import game.attributes.CollidingObject;
import game.attributes.SolidCollider;

import javax.imageio.ImageIO;

public class Platform extends GameObject implements SolidCollider{

	private String url;
	private int theme;

	public Platform(float x, float y, int width, int height, String url) {
		super(x, y, -1, width, height);
		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
		this.url = url;
		this.theme = theme;
	}

	public void tick() {
	}

	public void render(Graphics g, float xOffset, float yOffset) {

		//ENABLE THIS TO DEBUG PLATFORM TEXTURES WITH PLATFORM COLLISIONS

		g.setColor(Color.magenta);
		g.fillRect((int)(this.x + xOffset),(int)(this.y + yOffset),width,height);


		BufferedImage img=null;
		try
		{
			img = ImageIO.read( new File(url));
		}
		catch ( IOException exc )
		{
			//TODO: Handle exception.
		}
		g.drawImage(img,(int)(this.x + xOffset),(int)(this.y + yOffset),null);
	}

	public void handleCollisions(LinkedList<CollidingObject> collisions) {	
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, this.width, this.height);
	}
}
