package game.entities.platforms;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import game.entities.GameObject;
import game.attributes.CollidingObject;
import game.attributes.SolidCollider;

import javax.imageio.ImageIO;

public class Platform extends GameObject implements SolidCollider{

	private BufferedImage img;

	public Platform(float x, float y, int width, int height, String url) {
		super(x, y, 1, width, height);
		try
		{
			//sets the width and height of the platform based on the provided image width and height
			img = ImageIO.read( new File("./img/".concat(url)));
			this.width = img.getWidth();
			this.height = img.getHeight();
		}
		catch ( IOException exc )
		{
		}

		if (url.equals("")) {
			this.width = width;
			this.height = height;
		}

		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
	}

	public void tick() {
	}

	public void render(Graphics g, float xOffset, float yOffset) {

		//ENABLE THIS TO DEBUG COLLISION BOX
		/*
		g.setColor(Color.magenta);
		g.fillRect((int)(this.x + xOffset),(int)(this.y + yOffset),width,height);
		*/
		g.drawImage(img,(int)(this.x + xOffset),(int)(this.y + yOffset),null);
	}

	public void handleCollisions(LinkedList<CollidingObject> collisions) {	
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, this.width, this.height);
	}
}
