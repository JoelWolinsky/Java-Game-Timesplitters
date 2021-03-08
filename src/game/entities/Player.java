package game.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import game.Game;
import game.attributes.AnimatedObject;
import game.attributes.CollidingObject;
import game.attributes.GravityObject;
import game.attributes.SolidCollider;
import game.entities.platforms.MovingPlatform;
import game.graphics.Animation;
import game.graphics.AnimationStates;

import javax.imageio.ImageIO;

public class Player extends GameObject implements AnimatedObject, SolidCollider, GravityObject{
	
	static BufferedImage sprite;
	
	private float velX = 0;
	private float velY = 0;
	private float terminalVelY = 15;

	private static final float DECELERATION = 0.4f; 	 	// Rate at which velX decreases when A/D key released (for sliding)
	private static final float JUMP_GRAVITY = -7.5f; 	// VelY changes to this number upon jump
	private static final float RUN_SPEED = 3.6f; 		// Default run speed
	private static final float DOWN_SPEED = 10; 		// Speed at which character falls when S pressed in mid-air

	private int respawnX=0;
	private int respawnY=340;
	private int deathFromFallThreshold;
	private boolean immunity=false;
	private int i=0;
	private boolean cc=false;

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
		}
		catch ( IOException exc )
		{
			//TODO: Handle exception.
		}


		animations.put(AnimationStates.IDLE, new Animation(1660, urls));
		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
	}
	
	//TODO: Fix moving through the up-and-down moving platform when you jump underneath it
	public void tick() {
		//Gather all collisions
		CollidingObject.getCollisions(this);
		if (i<100)
			i++;
		else
			immunity=false;

		//Check for keyboard input along the x-axis
		if(Game.keyInput.right.isPressed() && !SolidCollider.willCauseSolidCollision(this, 2, true)) {

		/* Beware: Java floating point representation makes it difficult to have perfect numbers 
		( e.g. 3.6f - 0.2f = 3.3999999 instead of 3.4 ) so this code allows some leeway for values. */

				// Simulates acceleration when you run right
				if (this.velX >= RUN_SPEED){
					this.velX = RUN_SPEED;
				} else {
					this.velX += RUN_SPEED/6;
				}

		} else if(Game.keyInput.left.isPressed() && !SolidCollider.willCauseSolidCollision(this, -2, true)) {
				
				// Simulates acceleration when you run left
				if (this.velX <= -RUN_SPEED){
					this.velX = -RUN_SPEED;
				} else {
					this.velX -= RUN_SPEED/6;
				}

		} else {	
			// For deceleration effect
			if (!SolidCollider.willCauseSolidCollision(this, this.velX, true)){
				if (this.velX >= -0.1f && this.velX <= 0.1f) {
					this.velX = 0;	 
				} else if (this.velX > 0.1f) {
					this.velX -= DECELERATION;
				} else {
					this.velX += DECELERATION;
				}
			} else {
				this.velX = 0;	 
			}	
		}
		
		//Check for keyboard input along the y-axis
		if(Game.keyInput.down.isPressed()) {
			this.velY = DOWN_SPEED;
		}else if(Game.keyInput.up.isPressed()) {
			if(isOnGround() && !hasCeilingAbove() && !isOnWall()) {
				this.velY = JUMP_GRAVITY;
			}
		}

		//If you're not on ground, you should fall
		if(!isOnGround()) {
			fall(this);
		} else {
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
		} else { 	
			// Stop player falling through the floor
			CollidingObject o = SolidCollider.nextCollision(this,  this.velY, false);
			if(o == null) {
				return;
			}
			Rectangle s = o.getBounds();
			
			if(this.velY > 0 && !isOnWall()) {
				this.y = s.y - this.height;
				this.velY = 0;
			} else if(this.velY < 0 && !isOnWall()) { 
				this.velY = 0;
			} else {	// When velY == 0 and velX == 0 the sticking to the wall bug occurs.
						// Rebounds the player off the wall to avoid sticking.
				if (SolidCollider.willCauseSolidCollision(this, 5, true)) { 
					this.velX = -2.0f;
				} else if (SolidCollider.willCauseSolidCollision(this, -5, true)) {
					this.velX = 2.0f;
				}
			}
		}

		/*
		Falling below the current height threshold kills the player
		We should only put checkpoints after successfully completing a segment not during segments since climbing
		segments can pose a problem respawning the player mid fall which is not what we want. The player should reach
		the bottom when failing.
		The main idea is that most respawn points will be at ground level on each specific floor so the +300 will
		work everytime. (Falling 300 blocks below the floor/respawnY will kill the player)
		 */
		if (this.y >respawnY+300) {
			this.x = respawnX;
			this.y = respawnY;
			i=0;
		}

		// press r to respawn -- used for debugging
		if(Game.keyInput.r.isPressed())
		{
			respawn();
		}


	}

	public void respawn(){
		if (immunity==false)
		{
			this.x = respawnX;
			this.y = respawnY;
			immunity=true;
			i=0;
		}
	}

	
	private boolean isOnGround() {
		return SolidCollider.willCauseSolidCollision(this, 5, false);
	}

	private boolean isOnWall() {
		if ((SolidCollider.willCauseSolidCollision(this, this.velX, true) || SolidCollider.willCauseSolidCollision(this, -this.velX, true))
				&& !isOnGround()){
			return true;
		} else {
			return false;
		}
	}

	private boolean hasCeilingAbove() {
		return SolidCollider.willCauseSolidCollision(this, -5, false);
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


		if (immunity==true)
		{
			if (0<i && i <10 || 30<i && i <40 || 60<i && i<70)
			{


			}
			else
			{


				this.renderAnim(g, (int)(this.x+xOffset), (int)(this.y+yOffset));

			}
		}
		else
		{

			this.renderAnim(g, (int)(this.x+xOffset), (int)(this.y+yOffset));
		}

		/* -- To visualise the boundary box, uncomment these and getBounds(float xOffset, float yOffset) as well.
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.RED);		
		g2d.draw(getBounds(xOffset, yOffset)); */
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
