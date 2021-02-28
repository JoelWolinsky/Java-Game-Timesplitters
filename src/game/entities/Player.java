package game.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import game.Game;
import game.GameObject;
import game.attributes.AnimatedObject;
import game.attributes.CollidingObject;
import game.attributes.GravityObject;
import game.attributes.SolidCollider;
import game.graphics.Animation;
import game.graphics.AnimationStates;

import javax.imageio.ImageIO;

public class Player extends GameObject implements AnimatedObject, SolidCollider, GravityObject{
	
	static BufferedImage sprite;
	
	private float velX = 0;
	private float velY = 0;
	private float terminalVelY = 15;

	private static final float SLIDING_VEL = 0.2f; 	 	// Rate at which velX decreases when A/D key released (for sliding)
	private static final float JUMP_GRAVITY = -7.5f; 	// VelY changes to this number upon jump
	private static final float RUN_SPEED = 3.6f; 		// Default run speed
	private static final float DOWN_SPEED = 10; 		// Speed at which character falls when S pressed in mid-air

	private int respawnX=0;
	private int respawnY=340;
	private int deathFromFallThreshold;

	private static int animationTimer = 0;
	private static AnimationStates defaultAnimationState = AnimationStates.IDLE;
	private static AnimationStates currentAnimationState = defaultAnimationState;
	private static HashMap<AnimationStates, Animation> animations = new HashMap<AnimationStates, Animation>();

	public Player(float x, float y, int width,int height,String...urls) {
		super(x, y, 1, width, height);

		BufferedImage img;
		try
		{
			//sets the width and hight of the platform based on the provided image width and height
			img = ImageIO.read( new File(urls[0]));
			this.width = img.getWidth();
			this.height = img.getHeight();
			System.out.println(this.width);
		}
		catch ( IOException exc )
		{
			//TODO: Handle exception.
		}


		animations.put(AnimationStates.IDLE, new Animation(20, urls));
		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
	}
	
	//TODO: Fix sticking into wall when moving in the air
	//TODO: Fix moving through the up-and-down moving platform when you jump underneath it
	public void tick() {
		//Gather all collisions
		CollidingObject.getCollisions(this);
		
		//Check for keyboard input along the x-axis
		if(Game.keyInput.right.isPressed()) {
			this.velX = RUN_SPEED;
		}else if(Game.keyInput.left.isPressed()) {
			this.velX = -RUN_SPEED;
		}else {
			/* Beware: Java floating point representation makes it difficult to have perfect numbers 
			( e.g. 3.6f - 0.2f = 3.3999999 instead of 3.4 ) so this code allows some leeway for values. */
			if (this.velX >= -0.1f && this.velX <= 0.1f) {
				this.velX = 0;	 
			}
			else if (this.velX > 0.1f) {
				this.velX -= SLIDING_VEL;
			}
			else {
				this.velX += SLIDING_VEL;
			}
		}
		
		//Check for keyboard input along the y-axis
		if(Game.keyInput.down.isPressed()) {
			this.velY = DOWN_SPEED;
		}else if(Game.keyInput.up.isPressed()) {
			if(isOnGround() && !hasCeilingAbove()) {
				this.velY = JUMP_GRAVITY;
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
		if(!SolidCollider.willCauseSolidCollision(this, this.velX+1, true)) {
			this.x += velX;
		}
		if(!SolidCollider.willCauseSolidCollision(this, this.velY+1, false)) {
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
				this.y = s.y + s.height;
				this.velY = 0;
			}
		}

		//quick little respawn when falling below level
		if (this.y >respawnY+300)
		{
			this.x = respawnX;
			this.y = respawnY;
		}

		// press r to respawn -- used for debugging
		if(Game.keyInput.r.isPressed())
		{
			this.x = respawnX;
			this.y = respawnY;
		}

	}
	
	private boolean isOnGround() {
		return SolidCollider.willCauseSolidCollision(this, 5, false);
	}

	private boolean hasCeilingAbove() {
		return SolidCollider.willCauseSolidCollision(this, -10, false);
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

		g.setColor(Color.magenta);
		g.fillRect((int)(this.x + xOffset),(int)(this.y + yOffset),width,height);

		this.renderAnim(g, (int)(this.x+xOffset), (int)(this.y+yOffset));
		
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

	public Rectangle getBounds(float xOffset, float yOffset) {
		return new Rectangle((int)(this.x+xOffset), (int)(this.y + yOffset), width, height);
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

	public void setRespawnX(int x) {
		this.respawnX = x;
	}

	public void setRespawnY(int y) {
		this.respawnY = y;
	}

	public int getRespawnX() {
		return this.respawnX;
	}

	public int getRespawnY() {
		return this.respawnY;
	}

}
