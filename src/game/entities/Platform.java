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

	public Platform(float x, float y, int width, int height, String url) {
		super(x, y, -1, width, height);
		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
		this.url = url;
	}

	public void tick() {
	}

	public void render(Graphics g, float xOffset, float yOffset) {

		/* ENABLE THIS TO DEBUG COLLISION BOX
		g.setColor(Color.magenta);
		g.fillRect((int)(this.x + xOffset),(int)(this.y + yOffset),width,height);
		*/

		BufferedImage img=null;
		try
		{
			//sets the width and hight of the platform based on the provided image width and height
			img = ImageIO.read( new File("./img/".concat(url)));
			width = img.getWidth();
			height = img.getHeight();
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
