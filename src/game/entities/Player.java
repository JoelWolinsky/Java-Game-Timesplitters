package game.entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import game.Effect;
import game.Game;
import game.Item;
import game.attributes.AnimatedObject;
import game.attributes.CollidingObject;
import game.attributes.GravityObject;
import game.attributes.SolidCollider;
import game.entities.areas.AddedItem;
import game.entities.platforms.CrushingPlatform;
import game.entities.platforms.MovingPlatform;
import game.entities.areas.RespawnPoint;
import game.graphics.Animation;
import game.graphics.AnimationStates;
import game.graphics.Assets;
import game.input.KeyInput;
import game.network.packets.Packet02Move;


import static game.Level.getToBeAdded;

public class Player extends GameObject implements AnimatedObject, SolidCollider, GravityObject{

	static BufferedImage sprite;

	protected float velX = 0;
	protected float velY = 0;
	private float terminalVelY = 15;

	private static final float DECELERATION = 0.4f; 	 	// Rate at which velX decreases when A/D key released (for sliding)
	private static float JUMP_GRAVITY = -7.5f;
	private static float JUMP_GRAVITY_DOUBLE = -10.5f;// VelY changes to this number upon jump
	private static float RUN_SPEED = 3.6f; 		// Default run speed
	private static float DOWN_SPEED = 10; 		// Speed at which character falls when S pressed in mid-air

	public LinkedList<RespawnPoint> visitedRespawnPoints = new LinkedList<>();

	private boolean godMode=false;
	private int respawnX=0;
	private int respawnThreshold=340;
	private int respawnY=340;
	private int deathFromFallThreshold;
	private boolean immunity=false;
	private int i=0,bi=0;
	private boolean cc=false;
	private boolean moving;
	private boolean canMove=false;
	private boolean locked=false;
	private GameObject locker;
	private LinkedList<Item> inventory = new LinkedList<Item>(Arrays.asList(new Item(0,0,0,0,this,"./img/empty.png"),new Item(0,0,0,0,this,"./img/shoes.png"),new Item(0,0,0,0,this,"./img/empty.png")));
	private int inventorySize=3;
	private int inventoryIndex=2;
	private boolean inventoryChanged=false;
	private int itemUseCooldown = 0;
	private int slotSelectionCooldown=0;
	private int effectDuration=500;
	private ArrayList<Effect> currentEffects = new ArrayList<Effect>();
	private boolean canDoubleJump=false;
	private int jumpCooldown=0;
	private boolean bouncing=false;
	private int bouncingSpeed = 0;
	private int bouncingTimer=0;
	private boolean bounceImmunity=false;

	private static int animationTimer = 0;
	private static AnimationStates defaultAnimationState = AnimationStates.IDLE;
	private static AnimationStates currentAnimationState = defaultAnimationState;
	private static HashMap<AnimationStates, Animation> animations = new HashMap<AnimationStates, Animation>();
	private int currentFrame;
	private Assets s = new Assets();

	private Point prevPos;

	private String username;
	private KeyInput input;

	public Player(float x, float y, KeyInput input, int width,int height) {
		super(x, y, 2, width, height);

		this.input = input;
		this.username = UUID.randomUUID().toString();;


		s.init();

		animations.put(AnimationStates.IDLE, new Animation(15, Assets.player_idle));
		animations.put(AnimationStates.RIGHT, new Animation(15, Assets.player_right));
		animations.put(AnimationStates.LEFT, new Animation(15, Assets.player_left));

		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
	}


	//TODO: Fix moving through the up-and-down moving platform when you jump underneath it
	public void tick() {
		//Gather all collisions
		CollidingObject.getCollisions(this);


		moving = false;

		//disable immunity after 100
		if (!(this instanceof AIPlayer))
		if (i<100) {
			i++;
		}else {
			immunity=false;
		}

		if (bi<15) {
			bi++;
		}else {
			bounceImmunity=false;
		}


		for (Item i : this.getInventory())
		{
			if (i.getAddItem())
			{
				getToBeAdded().add(new AddedItem(this.getX(), this.getY(), 0, 0, this, i.getItemToAdd() , i.getItemToAdd(),i.getItemToAdd(),i.getItemToAdd()));
				i.setAddItem(false);
			}
		}

		//always have the player collision box set to respective size of its animationstate
		this.width = getAnimation(currentAnimationState).getFrame(currentFrame).getWidth();
		this.height = getAnimation(currentAnimationState).getFrame(currentFrame).getHeight();


		this.prevPos = new Point((int)this.x, (int)this.y);


		//Check for keyboard input along the x-axis

		if (canMove)
		if (!(this instanceof AIPlayer))
		if (this.input != null) {
			if(KeyInput.right.isPressed() && !SolidCollider.willCauseSolidCollision(this, 2, true)) {


				moving=true;

			/* Beware: Java floating point representation makes it difficult to have perfect numbers
			( e.g. 3.6f - 0.2f = 3.3999999 instead of 3.4 ) so this code allows some leeway for values. */

					// Simulates acceleration when you run right
					if (this.velX >= RUN_SPEED){
						this.velX = RUN_SPEED;
					} else {
						this.velX += RUN_SPEED/6;
					}
					currentAnimationState = AnimationStates.RIGHT;

			} else if(KeyInput.left.isPressed() && !SolidCollider.willCauseSolidCollision(this, -2, true)) {


				moving=true;

					// Simulates acceleration when you run left
					if (this.velX <= -RUN_SPEED){
						this.velX = -RUN_SPEED;
					} else {
						this.velX -= RUN_SPEED/6;
					}
					currentAnimationState = AnimationStates.LEFT;

			} else {
				currentAnimationState = AnimationStates.IDLE;
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
			if(KeyInput.down.isPressed()) {
				this.velY = DOWN_SPEED;
			}else if(KeyInput.up.isPressed()) {

				if (jumpCooldown>=10 && canDoubleJump && !isOnGround() && !hasCeilingAbove() && !isOnWall())
				{
					this.velY = JUMP_GRAVITY_DOUBLE;
					jumpCooldown=0;
					canDoubleJump=false;
				}
				else if(jumpCooldown>=10 && isOnGround() && !hasCeilingAbove() && !isOnWall()) {
					this.velY = JUMP_GRAVITY;
					jumpCooldown=0;
				}
			}
		}

		if (jumpCooldown<10)
			jumpCooldown++;


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

			if (o instanceof CrushingPlatform)
			{
				if (((CrushingPlatform) o).getCrushingSide().equals("BOTTOM"))
				{
					if (((CrushingPlatform) o).getVelocity()<0)
						this.y += ((CrushingPlatform) o).getVelocity();
					else fall(this);
				}
				else if (((CrushingPlatform) o).getCrushingSide().equals("LEFT") || ((CrushingPlatform) o).getCrushingSide().equals("RIGHT"))
				{
					this.x += ((CrushingPlatform) o).getVelocity();

				}
			}
		}


		if(!SolidCollider.willCauseSolidCollision(this, this.velX+1, true)) {
			this.x += this.velX;
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
			if(this == Game.player) {
				if((int)this.x != this.prevPos.x || (int)this.y != this.prevPos.y) {
					Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y);
					System.out.println("Player move usr " + this.getUsername());
					packet.writeData(Game.game.socketClient);
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
		if (this.y >respawnThreshold+300) {
			this.x = respawnX;
			this.y = respawnY;
			i=0;
		}

		// press r to respawn -- used for debugging
		if(KeyInput.r.isPressed())
		{
			respawn();
		}

		// press g to enable godmode -- remember to disable this after finishing game
		if(KeyInput.g.isPressed())
		{
			if (godMode) {
				godMode = false;
				RUN_SPEED = 3.6f;
				JUMP_GRAVITY = -7.5f;
			}
			else {
				godMode = true;
				RUN_SPEED = 13.6f;
				JUMP_GRAVITY = - 12.0f;
			}
		}

		if(KeyInput.space.isPressed())
		{
			//OPTION 2
			if (!(inventory.get(inventoryIndex).getUrl().equals("./img/empty.png"))) {
				inventory.get(inventoryIndex).getEffect();
				if (!(inventory.get(inventoryIndex).getUrl().equals("./img/jump.png")) && !(inventory.get(inventoryIndex).getUrl().equals("./img/banana.png")))
				currentEffects.add(new Effect(inventory.get(inventoryIndex).getUrl(),500));
				inventory.get(inventoryIndex).setUrl("./img/empty.png");
				this.setInventoryChanged(true);
			}
		}


		if (!currentEffects.isEmpty())
		{
			for (Effect e : currentEffects)
			{
				e.decrement();

				if (e.getTimer()<=0) {
					removeEffect(e.getName());
					currentEffects.remove(e);
					break;
				}
			}

		}


		if (itemUseCooldown<50)
			itemUseCooldown++;


		if (slotSelectionCooldown<5)
			slotSelectionCooldown++;

		if(KeyInput.comma.isPressed())
		{
			if (slotSelectionCooldown>=5)
			if (inventoryIndex>0)
			{
				inventoryIndex--;
				slotSelectionCooldown=0;
			}
		}

		if(KeyInput.period.isPressed())
		{
			if (slotSelectionCooldown>=5)
			if (inventoryIndex<2)
			{
				inventoryIndex++;
				slotSelectionCooldown=0;
			}
		}



		if (bouncing)
		{
			this.x = this.x + (bouncingSpeed*2);
			if (bouncingSpeed>0)
				this.y = this.y - bouncingSpeed;
			else
				this.y = this.y - bouncingSpeed*(-1);
			bouncingTimer++;
			if(bouncingTimer>=10) {
				bouncing = false;
				bouncingTimer=0;
			}

			//sum = sum + speed;
		}



	}

	public boolean moving(){
		return moving;
	}

	public void slow(){
		RUN_SPEED = 2.0f;
		JUMP_GRAVITY = -5.5f;
	}


	public void setRunSpeed(float runSpeed) {
		RUN_SPEED = runSpeed;
	}

	public void setJumpGravity(float jumpGravity) {
		JUMP_GRAVITY = jumpGravity;
	}

	public void normal(GameObject z){
		if(this.locker == z)
		{
			RUN_SPEED = 3.6f;
			JUMP_GRAVITY = -7.5f;
		}
	}

	public void removeEffect(String code){

		switch (code){
			case "./img/shoes.png":
				RUN_SPEED = 3.6f;
				JUMP_GRAVITY = -7.5f;
				break;

			case "./img/banana.png":
				RUN_SPEED = 3.6f;
				JUMP_GRAVITY = -7.5f;
				break;

		}
	}

	public void respawn(){
		if (immunity==false && godMode==false)
		{
			this.x = respawnX;
			this.y = respawnY;
			immunity=true;
			i=0;
		}
	}

	public void bouncing(int speed){
		this.bouncing = true;
		bouncingSpeed= speed;
		bounceImmunity=true;
		bi=0;
	}




	public boolean isOnGround() {
		return SolidCollider.willCauseSolidCollision(this, 5, false);
	}

	public boolean isOnWall() {
		if ((SolidCollider.willCauseSolidCollision(this, this.velX, true) || SolidCollider.willCauseSolidCollision(this, -this.velX, true))
				&& !isOnGround()){
			return true;
		} else {
			return false;
		}
	}

	public boolean hasCeilingAbove() {
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

	public float getVelX() {
		return velX;
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
		return Player.animationTimer;
	}

	public void setAnimationTimer(int animationTimer) {
		Player.animationTimer = animationTimer;

	}

	public AnimationStates getCurrentAnimationState() {
		return currentAnimationState;
	}

	public AnimationStates getDefaultAnimationState() {
		return Player.defaultAnimationState;
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

	public String getUsername() {
		return this.username;
	}

	public void addRespawnThreshold(int respawnThreshold) {
		this.respawnThreshold = this.respawnThreshold + respawnThreshold;
	}

	public void setRespawnThreshold(int respawnThreshold) {
		this.respawnThreshold = respawnThreshold;
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public void setLocker(GameObject locker) {
		this.locker = locker;
	}

	public void addToInventory(Item item) {
		inventory.add(item);
	}

	public void removeFromInventory(GameObject item) {
		inventory.remove(item);
	}

	public LinkedList<Item> getInventory() {
		return inventory;
	}

	public boolean inventoryChanged() {
		return inventoryChanged;
	}

	public void setInventoryChanged(boolean inventoryChanged) {
		this.inventoryChanged = inventoryChanged;
	}

	public int getInventoryIndex() {
		return inventoryIndex;
	}

	public void incrementInventoryIndex() {
		inventoryIndex++;
	}

	public int firstFreeSpace(){

		for (int i =0 ; i<inventorySize;i++)
		{
			if (inventory.get(i).getUrl().equals("./img/empty.png"))
				return i;
		}

		return -1;
	}

	public void setCanDoubleJump(boolean canDoubleJump) {
		this.canDoubleJump = canDoubleJump;
	}

	public void addEffect(Effect e)
	{
		currentEffects.add(e);
	}

	public int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
	public static int getRandom(int[] array) {
		int rnd = new Random().nextInt(array.length);
		return array[rnd];
	}

	public boolean isBounceImmune() {
		return bounceImmunity;
	}

	public LinkedList<RespawnPoint> getRespawnPoints() {
		return visitedRespawnPoints;
	}

	public void addRespawnPoint(RespawnPoint r) {
		this.visitedRespawnPoints.add(r);
	}
}
