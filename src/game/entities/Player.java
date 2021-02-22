package game.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import game.Game;
import game.GameObject;
import game.attributes.AnimatedObject;
import game.attributes.CollidingObject;
import game.attributes.GravityObject;
import game.attributes.SolidCollider;
import game.graphics.Animation;
import game.graphics.AnimationStates;

public class Player extends GameObject implements AnimatedObject, SolidCollider, GravityObject{
	
	static BufferedImage sprite;
	static String url = "./img/Anglerfish.png";
	
	private float velX = 0;
	private float velY = 0;
	private float terminalVelY = 15;
	
	private static int animationTimer = 0;
	private static AnimationStates defaultAnimationState = AnimationStates.IDLE;
	private static AnimationStates currentAnimationState = defaultAnimationState;
	private static HashMap<AnimationStates, Animation> animations = new HashMap<AnimationStates, Animation>();
	
	public Player(float x, float y) {
		super(x, y, 1, 64, 32);
		animations.put(AnimationStates.IDLE, new Animation(20, "./img/Anglerfish.png", "./img/Anglerfish2.png", "./img/Anglerfish3.png", "./img/Anglerfish4.png"));
		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
	}
	
	//TODO: Fix sticking into wall when moving in the air
	public void tick() {
		//Gather all collisions
		CollidingObject.getCollisions(this);
		
		//Check for keyboard input along the x-axis
		if(Game.keyInput.right.isPressed()) {
			this.velX = 3.5f;
		}else if(Game.keyInput.left.isPressed()) {
			this.velX = -3.5f;
		}else {
			this.velX = 0;
		}
		
		//Check for keyboard input along the y-axis
		if(Game.keyInput.down.isPressed()) {
			this.velY = 10;
		}else if(Game.keyInput.up.isPressed()) {
			//You can only jump if you're on ground
			if(isOnGround()) {
				this.velY = -7.5f;
			}
		}
		
		//If you're not on ground, you should fall
		if(!isOnGround()) {
			fall(this);
		}else {
			CollidingObject o = SolidCollider.nextCollision(this, 5, false);
			if(o instanceof MovingPlatform) {
				if(((MovingPlatform) o).getXAxis()) {
					this.x += ((MovingPlatform) o).getVelocity();
				}else {
					this.y += ((MovingPlatform) o).getVelocity();
				}
			}
		}
		
		//Move player if it will not cause a collision
		if(!SolidCollider.willCauseSolidCollision(this, velX, true)) {
			this.x += velX;
		}
		if(!SolidCollider.willCauseSolidCollision(this, this.velY, false)) {
			this.y += this.velY;
		}else {
			//Stop player falling through the floor
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
				this.velY = 0;
			}
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

	public void render(Graphics g, float xOffset, float yOffset) {
		this.renderAnim(g, (int)(this.x+xOffset), (int)(this.y+yOffset));
		
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

	public int getAnimationTimer() {
		return this.animationTimer;
	}

	public void setAnimationTimer(int animationTimer) {
		this.animationTimer = animationTimer;
		
	}

	public AnimationStates getCurrentAnimationState() {
		return this.currentAnimationState;
	}

	public AnimationStates getDefaultAnimationState() {
		return this.defaultAnimationState;
	}

	public Animation getAnimation(AnimationStates state) {
		return animations.get(state);
	}

}
