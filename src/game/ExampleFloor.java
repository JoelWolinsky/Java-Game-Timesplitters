package server;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class ExampleFloor extends GameObject implements GraphicalObject, SolidCollider{

	static BufferedImage sprite;
	static String url = "./img/Cursor.png";
	
	public ExampleMouseListener() {
		super(0, 100, 10, 32, 32);
		sprite = this.loadImage(ExampleMouseListener.url);
		CollidingObject.addCollider(this);	
		SolidCollider.addCollider(this); 
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
