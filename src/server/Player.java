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
	private float maxVelX = 10;
	private float maxVelY = 10;
	private float jumpVel = -100;
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
		if(this.arrowKeysPressed[1]) {
			this.velX = this.maxVelX;
		}else if(this.arrowKeysPressed[3]) {
			this.velX = -this.maxVelX;
		}else {
			this.velX = 0;
		}
		if(this.arrowKeysPressed[0]) {
			if(SolidCollider.willCauseSolidCollision(this, 1, false)) {
				this.velY = this.jumpVel;
			}
		}
		if(!SolidCollider.willCauseSolidCollision(this, this.velX, true)) {
			this.x += this.velX;
		}else {
			Rectangle s = SolidCollider.nextCollision(this, this.velX, true).getBounds();
			if(this.velX > 0) {
				this.x = s.x - this.width;
			}else {
				this.x = s.x + s.width;
			}
		}
		if(!SolidCollider.willCauseSolidCollision(this, this.velY, false)) {
			this.y += this.velY;
		}else {
			Rectangle s = SolidCollider.nextCollision(this, this.velY, false).getBounds();
			if(this.velY > 0) {
				this.y = s.y - this.height;
			}else {
				this.y = s.y + s.height;
			}
		}
		fall(this);
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
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			this.arrowKeysPressed[3] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			this.arrowKeysPressed[0] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			this.arrowKeysPressed[1] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			this.arrowKeysPressed[3] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			this.arrowKeysPressed[0] = false;
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