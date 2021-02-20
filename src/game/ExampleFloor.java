package server;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class ExampleFloor extends GameObject implements GraphicalObject, SolidCollider{

	private static BufferedImage sprite;
	private static String url = "./img/Grass2.png";


	public ExampleFloor(float x, float y, int z) {
		super(x, y, z, 640, 32);
		sprite = this.loadImage(ExampleFloor.url);
		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
		Game.handler.addObject(this);
	}
	
	
	public void tick(double delta) {
		CollidingObject.getCollisions(this);
		
	}

	public void render(Graphics g) {
		this.drawSprite(g, sprite, (int)this.x, (int)this.y);
	}


	@Override
	public void handleCollisions(LinkedList<CollidingObject> collisions) {
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)this.x, (int)this.y, this.width, this.height);
	}

	

}
