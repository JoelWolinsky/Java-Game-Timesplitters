package game;

import game.attributes.CollidingObject;
import game.attributes.SolidCollider;
import game.entities.MovingPlatform;
import game.entities.Platform;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Chunk extends GameObject {
	private LinkedList<GameObject> entities = new LinkedList<>();
	//private LinkedList<Platform> platforms = new LinkedList<>();
	private String url;

	public Chunk(float x, float y, int width, int height,String url) {
		super(x, y, -2, width, height);
		this.url=url;
	}

	public void tick() {
		for(GameObject o : entities) {
			o.tick();
		}
		/*
		for(Platform p : platforms) {

				p.tick();

		}

		 */
	}
	
	public void render(Graphics g, float f, float h) {
		//Render the platforms first, so they are below the entities





		g.setColor(Color.magenta);
		g.fillRect((int)(this.x + f),(int)(this.y + h),width,height);
		BufferedImage img=null;
		try
		{
			img = ImageIO.read( new File(url));
		}
		catch ( IOException exc )
		{
			//TODO: Handle exception.
		}
		g.drawImage(img,(int)(this.x + f),(int)(this.y + h),null);

		//renderPlatforms(g,f,h);

	}
	
	public void renderEntities(Graphics g, float f, float h) {
		for(GameObject o : entities) {
			o.render(g, f, h);
		}
	}
	/*
	public void renderPlatforms(Graphics g, float f, float h) {
		for(Platform p : platforms) {
			p.render(g, f, h);
		}
	}

	 */
	public void addEntity(GameObject o) {
		entities.add(o);
	}
	public void removeEntity(GameObject o) {
		entities.remove(o);
	}
	/*
	public void addPlatform(Platform p) {
		platforms.add(p);
	}


	public void removePlatform(Platform p) {
		platforms.remove(p);
	}
	*/
}
