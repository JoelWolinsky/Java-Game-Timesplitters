package game.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import game.GameObject;
import game.Level;
import game.attributes.CollidingObject;
import game.attributes.SolidCollider;

public class Platform extends GameObject implements SolidCollider{

	public Platform(Level level, float x, float y, int width, int height) {
		super(level, x, y, -1, width, height);
		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
	}

	public void tick() {
	}

	public void render(Graphics g, int xOffset, int yOffset) {
		g.setColor(Color.white);
		g.fillRect((int)this.x + xOffset, (int)this.y + yOffset, width, height);
	}

	public void handleCollisions(LinkedList<CollidingObject> collisions) {		
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, this.width, this.height);
	}
}
