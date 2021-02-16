package server;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

public class FloorTile extends GameObject implements SolidCollider{

	public FloorTile(float x, float y, int width, int height) {
		super(x, y, 0, width, height);
		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
		Game.handler.addObject(this);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect((int)x, (int)y, width, height);
	}
	
	public void tick() {
	}
	public void handleCollisions(LinkedList<CollidingObject> collisions) {
	}

}
