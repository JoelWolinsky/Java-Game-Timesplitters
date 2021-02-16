package server;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Player extends GameObject implements GraphicalObject, SolidCollider, KeyListener, GravityObject{

	static BufferedImage sprite;
	static String url = "./img/Anglerfish.png";
	
	private float velX = 0;
	private float velY = 0;
	private float maxVelX = 6;
	private float maxVelY = 10;
	
	//{Up, right, down, left}
	private boolean[] arrowKeysPressed = {false, false, false, false};
	
	public Player(float x, float y) {
		super(x, y, 1, 64, 32);
		sprite = this.loadImage(this.url);
		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
		Game.handler.addObject(this);
		Listeners.addKeyListener(this);
	}

	@Override
	public void tick() {
//		CollidingObject.getCollisions(this);
		fall(this);
		if(this.arrowKeysPressed[1]) {
			this.velX = this.maxVelX;
		}else if(this.arrowKeysPressed[3]) {
			this.velX = -this.maxVelX;
		}else {
			this.velX = 0;
		}
		if(!SolidCollider.willCauseSolidCollision(this, this.velX, true)) {
			this.x += this.velX;
		}
		if(!SolidCollider.willCauseSolidCollision(this, this.velY, false)) {
			this.y += this.velY;
		}
	}

	@Override
	public void render(Graphics g) {
		this.drawSprite(g, sprite, (int)this.x, (int)this.y);
		
	}

	@Override
	public void handleCollisions(LinkedList<CollidingObject> collisions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) this.x, (int) this.y, this.width, this.height);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			this.arrowKeysPressed[1] = true;
		}else if(e.getKeyCode() == KeyEvent.VK_A) {
			this.arrowKeysPressed[3] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			this.arrowKeysPressed[1] = false;
		}else if(e.getKeyCode() == KeyEvent.VK_A) {
			this.arrowKeysPressed[3] = false;
		}
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

	public float getVelY() {
		return this.velY;
	}

	@Override
	public float getMaxVelY() {
		// TODO Auto-generated method stub
		return this.maxVelY;
	}
}