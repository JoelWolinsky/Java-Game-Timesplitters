package game.entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import game.Game;
import game.attributes.AnimatedObject;
import game.attributes.CollidingObject;
import game.attributes.GravityObject;
import game.attributes.SolidCollider;
import game.entities.platforms.MovingPlatform;
import game.graphics.Animation;
import game.graphics.AnimationStates;
import game.graphics.Assets;
import game.input.KeyInput;
import game.network.packets.Packet02Move;

import javax.imageio.ImageIO;

public class AIPlayer extends GameObject implements AnimatedObject, SolidCollider, GravityObject{

	static BufferedImage sprite;

	private float velX = 0;
	private float velY = 0;
	private float terminalVelY = 15;

	private static final float DECELERATION = 0.4f; 	 	// Rate at which velX decreases when A/D key released (for sliding)
	private static final float JUMP_GRAVITY = -7.5f; 	// VelY changes to this number upon jump
	private static final float RUN_SPEED = 3.6f; 		// Default run speed
	private static final float DOWN_SPEED = 10; 		// Speed at which character falls when S pressed in mid-air

    public String direction = "N"; // or private?
    public String jump = "N";
    public float wait = 0;


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
	private int currentFrame;
	private Assets s = new Assets();
	
	private Point prevPos;
	
	private String username;

	private int respawnThreshold;

	public AIPlayer(float x, float y, int width,int height, String...urls) {
		super(x, y, 2, width, height);
		
		this.username = UUID.randomUUID().toString();;

		BufferedImage img;
		try
		{
			//sets the width and hight of the platform based on the provided image width and height
			img = ImageIO.read( new File(urls[0]));

		}
		catch ( IOException exc )
		{
			//TODO: Handle exception.
		}

		s.init();

		animations.put(AnimationStates.IDLE, new Animation(15, Assets.player_idle));
		animations.put(AnimationStates.RIGHT, new Animation(15, Assets.player_right));
		animations.put(AnimationStates.LEFT, new Animation(15, Assets.player_left));

		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
	}

	public void tick() {
		//Gather all collisions
		CollidingObject.getCollisions(this);
		if (i<100) {
			i++;
		}else {
			immunity=false;
		}

		this.width = getAnimation(currentAnimationState).getFrame(currentFrame).getWidth();
		this.height = getAnimation(currentAnimationState).getFrame(currentFrame).getHeight();


		this.prevPos = new Point((int)this.x, (int)this.y);

		if (this.wait > 0) {
			
			this.wait--;

			if (!SolidCollider.willCauseSolidCollision(this, this.velX, true)){
				if (this.velX >= -0.1f && this.velX <= 0.1f) {
					this.velX = 0;
					currentAnimationState = AnimationStates.IDLE;
				} else if (this.velX > 0.1f) {
					this.velX -= DECELERATION;
				} else {
					this.velX += DECELERATION;
				}
			} else {
				this.velX = 0;
				currentAnimationState = AnimationStates.IDLE;
			}

		}
		else {

			if(this.direction.equals("R")) {

			/* Beware: Java floating point representation makes it difficult to have perfect numbers
			( e.g. 3.6f - 0.2f = 3.3999999 instead of 3.4 ) so this code allows some leeway for values. */

					// Simulates acceleration when running right
					if (this.velX >= RUN_SPEED){
						this.velX = RUN_SPEED;
					} else {
						this.velX += RUN_SPEED/6;
					}
					currentAnimationState = AnimationStates.RIGHT;

			} else if(this.direction.equals("L")) { 

					// Simulates acceleration when running left
					if (this.velX <= -RUN_SPEED){
						this.velX = -RUN_SPEED;
					} else {
						this.velX -= RUN_SPEED/6;
					}
					currentAnimationState = AnimationStates.LEFT;

			} else { 
				// For deceleration effect
				if (!SolidCollider.willCauseSolidCollision(this, this.velX, true)){
					if (this.velX >= -0.1f && this.velX <= 0.1f) {
						this.velX = 0;
						currentAnimationState = AnimationStates.IDLE;
					} else if (this.velX > 0.1f) {
						this.velX -= DECELERATION;
					} else {
						this.velX += DECELERATION;
					}
				} else {
					this.velX = 0;
					currentAnimationState = AnimationStates.IDLE;
				}
			}

			// For moving downwards quickly
			  /* 	if(KeyInput.down.isPressed()) {
						this.velY = DOWN_SPEED; 	} */

			if(jump.equals("Y")) {
				if(isOnGround() && !hasCeilingAbove() && !isOnWall()) {
					this.velY = JUMP_GRAVITY;
				}
				this.jump = "N";
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
			if(o != null) {
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
		}
		
		if(Game.isMultiplayer) {
			if((int)this.x != this.prevPos.x || (int)this.y != this.prevPos.y) {
				Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y);
				packet.writeData(Game.socketClient);
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
		if(KeyInput.r.isPressed())
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


		//-- To visualise the boundary box, uncomment these and getBounds(float xOffset, float yOffset) as well.
		//g.setColor(Color.magenta);
		//g.fillRect((int)(this.x + xOffset),(int)(this.y + yOffset),this.width,this.height);
		if (immunity==true)
		{
			if (0<i && i <10 || 30<i && i <40 || 60<i && i<70)
			{


			}
			else
			{


				this.currentFrame= this.renderAnim(g, (int)(this.x+xOffset), (int)(this.y+yOffset));

			}
		}
		else
		{

			this.currentFrame= this.renderAnim(g, (int)(this.x+xOffset), (int)(this.y+yOffset));
		}

	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}


	public int getAnimationTimer() {
		return AIPlayer.animationTimer;
	}

	public void setAnimationTimer(int animationTimer) {
		AIPlayer.animationTimer = animationTimer;

	}

	public AnimationStates getCurrentAnimationState() {
		return AIPlayer.currentAnimationState;
	}

	public AnimationStates getDefaultAnimationState() {
		return AIPlayer.defaultAnimationState;
	}

	public Animation getAnimation(AnimationStates state) {
		return animations.get(state);
	}



    public void setDirection(String r) {
		this.direction = r;
	}

	public String getDirection() {
		return this.direction;
	}

    public void setJump(String j) {
		this.jump = j;
	}

    public void setWait(float w) {
		this.wait = w;
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

	public String getUsername() {
		return this.username;
	}

	public void setRespawnThreshold(int respawnThreshold) {
		this.respawnThreshold = respawnThreshold;
	}


}
