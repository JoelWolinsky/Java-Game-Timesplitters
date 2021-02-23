package game.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import game.Game;
import game.GameObject;
import game.KeyInput;
import game.Level;
import game.attributes.CollidingObject;
import game.attributes.GraphicalObject;
import game.attributes.GravityObject;
import game.attributes.SolidCollider;
import game.network.packets.Packet02Move;

public class Player extends GameObject implements GraphicalObject, SolidCollider, GravityObject{
	
	static BufferedImage sprite;
	static String url = "./img/Anglerfish.png";
	
	private float velX = 0;
	private float velY = 0;
	private float terminalVelY = 15;
	
	private KeyInput keyInput;
	private String username;
	
	public Player(Level level, float x, float y, KeyInput input, String username) {
		super(level, x, y, 1, 64, 32);
		this.keyInput = input;
		this.username = username;
		
		sprite = this.loadImage(this.url);
		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
	}
	
	//TODO: Fix sticking into wall when moving in the air
	public void tick() {
		CollidingObject.getCollisions(this);
		if(this.keyInput != null) {
			if(this.keyInput.right.isPressed()) {
				this.velX = 3.5f;
			}else if(this.keyInput.left.isPressed()) {
				this.velX = -3.5f;
			}else {
				this.velX = 0;
			}
			if(this.keyInput.down.isPressed()) {
				this.velY = 10;
			}else if(this.keyInput.up.isPressed()) {
				if(isOnGround()) {
					this.velY = -7.5f;
				}
			}
		}

		
		if(!isOnGround()) {
			fall(this);
		}
		if(!SolidCollider.willCauseSolidCollision(this, velX, true)) {
			this.x += velX;
//		}else {
//			Rectangle s = SolidCollider.nextCollision(this, this.velX, true).getBounds();
//			if(this.velX > 0) {
//				this.x = s.x - this.width;
//				this.velX = 0;
//			}else {
//				this.x = s.x + s.width;
//				this.velX = 0;
//			}
		}
		if(!SolidCollider.willCauseSolidCollision(this, this.velY, false)) {
			this.y += this.velY;
		}else {
			CollidingObject o = SolidCollider.nextCollision(this,  this.velY, false);
			if(o == null) {
				return;
			}
			Rectangle s = o.getBounds();
			if(this.velY > 0) {
				this.y = s.y - this.height;
				this.velY = 0;
			}else if(this.velY < 0 && !isOnGround()){
				this.y = s.y - s.height;
				this.terminalVelY = 0;
				System.out.println("This is my fault");
			}
		}
		
		if(this.velX != 0 || this.velY != 0) {
			Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y);
			packet.writeData(Game.game.socketClient);
		}
	}
	
	private boolean isOnGround() {
		return SolidCollider.willCauseSolidCollision(this, 5, false);
	}

	public void handleCollisions(LinkedList<CollidingObject> collisions) {
	}
	
	public void setVelY(float velY) {
		this.velY = velY;
	}

	public float getVelY() {
		return this.velY;
	}

	public float getTerminalVel() {
		return this.terminalVelY;
	}

	public void render(Graphics g, int xOffset, int yOffset) {
		this.drawSprite(g, sprite, (int)this.x+xOffset, (int)this.y+yOffset);
		
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

	public String getUsername() {
		return this.username;
	}

}
