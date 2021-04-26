package game.entities.players;

import java.awt.*;
import java.util.*;

import game.Effect;
import game.Game;
import game.Item;
import game.attributes.CollidingObject;
import game.attributes.GravityObject;
import game.attributes.SolidCollider;
import game.entities.GameObject;
import game.entities.areas.AddedItem;
import game.entities.platforms.CrushingPlatform;
import game.entities.platforms.MovingPlatform;
import game.entities.areas.RespawnPoint;
import game.graphics.Animation;
import game.graphics.AnimationStates;
import game.graphics.GameMode;
import game.graphics.LevelState;
import game.input.KeyInput;
import game.network.packets.Packet02Move;

import static game.Level.getLevelState;
import static game.graphics.Assets.getAnimations;
import static game.Level.getToBeAdded;

public class Player extends GameObject implements SolidCollider, GravityObject {

    protected float velX = 0;
    protected float velY = 0;
    private final float terminalVelY = 15;

    protected float DECELERATION = 0.5f;        // Rate at which velX decreases when A/D key released (for sliding)
    protected float JUMP_GRAVITY = -7.5f;
    protected float JUMP_GRAVITY_DOUBLE = -6.5f;// VelY changes to this number upon jump
    protected float RUN_SPEED = 3.5f;        // Default run speed
    protected float DOWN_SPEED = 10;        // Speed at which character falls when S pressed in mid-air

    public LinkedList<RespawnPoint> previousRespawnPoints = new LinkedList<>();

    private boolean ghostMode = false;
    private int respawnX = 0;
    private int respawnThreshold = 340;
    private int respawnY = 340;
    public boolean immunity = false;
    public int i = 0;
    private int bi = 0;
    private final boolean cc = false;
    private boolean moving;
    protected boolean canMove = false;
    private boolean locked = false;
    private GameObject locker;
    private final ArrayList<Item> inventory = new ArrayList<Item>(Arrays.asList(new Item(0, 0, 0, 0, this, "./img/empty.png"), new Item(0, 0, 0, 0, this, "./img/empty.png"), new Item(0, 0, 0, 0, this, "./img/empty.png")));
    private final int inventorySize = 3;
    private int inventoryIndex = 2;
    private boolean inventoryChanged = false;
    private int itemUseCooldown = 0;
    private int slotSelectionCooldown = 0;
    private final int effectDuration = 500;
    private final ArrayList<Effect> currentEffects = new ArrayList<Effect>();
    private boolean canDoubleJump = false;
    private int jumpCooldown = 0;
    private boolean bouncing = false;
    private int bouncingSpeed = 0;
    private int bouncingTimer = 0;
    private boolean bounceImmunity = false;

    protected int animationTimer = 0;
    protected int frame;
    protected AnimationStates currentAnimState;
    protected Animation currentAnimation;
    protected HashMap<AnimationStates, Animation> animations;

    private Point prevPos;
    private final String username;
    private final KeyInput input;
    private String objectModel;


    public Player(float x, float y, KeyInput input, int width, int height,String url) {
        super(x, y, 2, width, height);

        this.input = input;
        this.username = UUID.randomUUID().toString();

        objectModel=url;
		//animations
        this.animations = getAnimations(url);
        this.currentAnimState = AnimationStates.IDLE;

        this.width = animations.get(currentAnimState).getFrame(frame).getWidth();
        this.height = animations.get(currentAnimState).getFrame(frame).getHeight();

        CollidingObject.addCollider(this);
        SolidCollider.addSolidCollider(this);
    }

    public void tick() {
        //Gather all collisions
        CollidingObject.getCollisions(this);


        if (getLevelState()== LevelState.InProgress)
            this.setCanMove(true);
        else
            this.setCanMove(false);

        moving = false;

        //disable immunity after 100
        // if (!(this instanceof AIPlayer))
        if (getLevelState()== LevelState.InProgress)
        if (i < 100) {
            i++;
            if (i > 20) {
                canMove = true; // makes Players pause for 20 ticks upon respawn
            }
        } else {
            immunity = false;
        }

        if (bi < 15) {
            bi++;
        } else {
            bounceImmunity = false;
        }


        for (Item i : this.getInventory()) {
            if (i.getAddItem()) {
                getToBeAdded().add(new AddedItem(this.getX(), this.getY(), 0, 0, this, i.getItemToAdd()));
                i.setAddItem(false);
            }
        }

        //always have the player collision box set to respective size of its animationstate
        //this.width = animations.get(currentAnimState).getFrame(frame).getWidth();
        //this.height = animations.get(currentAnimState).getFrame(frame).getHeight();


        this.prevPos = new Point((int) this.x, (int) this.y);


        //Check for keyboard input along the x-axis

        if (canMove)
            if (this.input != null) {
                if (KeyInput.right.isPressed() && !SolidCollider.willCauseSolidCollision(this, 2, true)) {

                    // Simulates acceleration when you run right
                    if (this.velX >= RUN_SPEED) {
                        this.velX = RUN_SPEED;
                    } else {
                        this.velX += 0.5;
                    }

                    currentAnimState = AnimationStates.RIGHT;

                } else if (KeyInput.left.isPressed() && !SolidCollider.willCauseSolidCollision(this, -2, true)) {

                    // Simulates acceleration when you run left
                    if (this.velX <= -RUN_SPEED) {
                        this.velX = -RUN_SPEED;
                    } else {
                        this.velX -= 0.5;
                    }

                    currentAnimState = AnimationStates.LEFT;

                } else {

                    // For deceleration effect
                    if (!SolidCollider.willCauseSolidCollision(this, this.velX, true)) {
                        if (this.velX >= -0.1f && this.velX <= 0.1f) {
                            this.velX = 0;
                        } else if (this.velX > 0.1f) {
                            this.velX -= DECELERATION;
                            if (!ghostMode)
                            currentAnimState = AnimationStates.IDLE;
                        } else {
                            this.velX += DECELERATION;
                            if (!ghostMode)
                            currentAnimState = AnimationStates.OTHER;
                        }
                    } else {
                        this.velX = 0;
                    }

                }


                //Check for keyboard input along the y-axis
                if (KeyInput.down.isPressed()) {
                    if (ghostMode)
                    {
                        if (this.velY >= RUN_SPEED) {
                            this.velY = RUN_SPEED;
                        } else {
                            this.velY += 0.5;
                        }
                    }
                    else
                        this.velY = DOWN_SPEED;
                } else if (KeyInput.up.isPressed()) {

                    if (ghostMode)
                    {
                        if (this.velY <= -RUN_SPEED) {
                            this.velY = -RUN_SPEED;
                        } else {
                            this.velY -= 0.5;
                        }
                    }
                    else
                    {
                        if (jumpCooldown >= 10 && canDoubleJump && !isOnGround() && !hasCeilingAbove() && !isOnWall()) {
                            this.velY = JUMP_GRAVITY_DOUBLE;
                            jumpCooldown = 0;
                            canDoubleJump = false;
                        } else if (jumpCooldown >= 10 && isOnGround() && !hasCeilingAbove() && !isOnWall()) {
                            this.velY = JUMP_GRAVITY;
                            jumpCooldown = 0;
                        }
                    }
                }
                else if(ghostMode)
                {
                    if (this.velY >= -0.1f && this.velY <= 0.1f) {
                        this.velY = 0;
                    } else if (this.velY > 0.1f) {
                        this.velY -= DECELERATION;
                    } else {
                        this.velY += DECELERATION;
                    }
                }
            }

        if (jumpCooldown < 10)
            jumpCooldown++;


        //If you're not on ground, you should fall
        if (!isOnGround() && !ghostMode) {
            fall(this);
        } else {
            CollidingObject o = SolidCollider.nextCollision(this, 5, false);
            if (o instanceof MovingPlatform) {
                if (((MovingPlatform) o).getXAxis()) {
                    this.x += ((MovingPlatform) o).getVelocity();
                } else {
                    this.y += ((MovingPlatform) o).getVelocity();
                }
            }

            if (o instanceof CrushingPlatform) {
                if (((CrushingPlatform) o).getCrushingSide().equals("BOTTOM")) {
                    if (((CrushingPlatform) o).getVelocity() < 0)
                        this.y += ((CrushingPlatform) o).getVelocity();
                    else fall(this);
                } else if (((CrushingPlatform) o).getCrushingSide().equals("LEFT") || ((CrushingPlatform) o).getCrushingSide().equals("RIGHT")) {
                    this.x += ((CrushingPlatform) o).getVelocity();

                }
            }
        }


        if (!SolidCollider.willCauseSolidCollision(this, this.velX + 1, true)) {

            if(!ghostMode)
            moving = true;

            this.x += this.velX;
        }

        if (!SolidCollider.willCauseSolidCollision(this, this.velY + 1, false)) {
            if(!ghostMode)
            moving = true;
            this.y += this.velY;
        } else {
            // Stop player falling through the floor
            CollidingObject o = SolidCollider.nextCollision(this, this.velY, false);
            if (o != null) {
                Rectangle s = o.getBounds();

                if (this.velY > 0 && !isOnWall()) {
                    this.y = s.y - this.height;
                    this.velY = 0;
                } else if (this.velY < 0 && !isOnWall()) {
                    this.velY = 0;
                } else {    // When velY == 0 and velX == 0 the sticking to the wall bug occurs.
                    // Rebounds the player off the wall to avoid sticking.
                    if (SolidCollider.willCauseSolidCollision(this, 5, true)) {
                        this.velX = -2.0f;
                    } else if (SolidCollider.willCauseSolidCollision(this, -5, true)) {
                        this.velX = 2.0f;
                    }
                }
            }
        }

        if (Game.game.getGameMode()== GameMode.MULTIPLAYER) {
            if (this == Game.player) {
                if ((int) this.x != this.prevPos.x || (int) this.y != this.prevPos.y) {
                    Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y);
                    //System.out.println("Player move usr " + this.getUsername());
                    packet.writeData(Game.socketClient);
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
        if (this.y > respawnThreshold + 300) {

            this.x = respawnX;
            this.y = respawnY;
            this.velX = 0;
            this.velY = 5;
            immunity = true;
            canMove = false;
            i = 0;
            currentAnimState = AnimationStates.IDLE;
        }

        // press r to respawn -- used for debugging
        if (KeyInput.r.isPressed()) {
            respawn();
        }


        if (KeyInput.g.isPressed()) {
            if (ghostMode) {
                ghostMode = false;
                RUN_SPEED = 3.5f;
                JUMP_GRAVITY = -7.5f;
            } else {
                ghostMode = true;
                RUN_SPEED = 7.6f;
                JUMP_GRAVITY = -12.0f;
            }
        }

        if (!(this instanceof AIPlayer))
            if (KeyInput.space.isPressed()) {
                //OPTION 2
                if (!(inventory.get(inventoryIndex).getUrl().equals("./img/empty.png"))) {
                    inventory.get(inventoryIndex).getEffect();
                    if (!(inventory.get(inventoryIndex).getUrl().equals("./img/jump.png")) && !(inventory.get(inventoryIndex).getUrl().equals("./img/banana.png")))
                        currentEffects.add(new Effect(inventory.get(inventoryIndex).getUrl(), 500));
                    inventory.get(inventoryIndex).setUrl("./img/empty.png");
                    this.setInventoryChanged(true);
                }
            }


        if (!currentEffects.isEmpty()) {
            for (Effect e : currentEffects) {
                e.decrement();

                if (e.getTimer() <= 0) {
                    removeEffect(e.getName());
                    currentEffects.remove(e);
                    break;
                }
            }

        }


        if (itemUseCooldown < 50)
            itemUseCooldown++;


        if (slotSelectionCooldown < 5)
            slotSelectionCooldown++;

        if (KeyInput.comma.isPressed()) {
            if (slotSelectionCooldown >= 5)
                if (inventoryIndex > 0) {
                    inventoryIndex--;
                    slotSelectionCooldown = 0;
                }
        }

        if (KeyInput.period.isPressed()) {
            if (slotSelectionCooldown >= 5)
                if (inventoryIndex < 2) {
                    inventoryIndex++;
                    slotSelectionCooldown = 0;
                }
        }


        if (bouncing) {
            this.x = this.x + (bouncingSpeed * 2);
            if (bouncingSpeed > 0)
                this.y = this.y - bouncingSpeed;
            else
                this.y = this.y - bouncingSpeed * (-1);
            bouncingTimer++;
            if (bouncingTimer >= 10) {
                bouncing = false;
                bouncingTimer = 0;
            }

        }


    }

    public boolean isMoving() {
        return moving;
    }

    public void setRunSpeed(float runSpeed) {
        RUN_SPEED = runSpeed;
    }

    public void setJumpGravity(float jumpGravity) {
        JUMP_GRAVITY = jumpGravity;
    }

    public void normal(GameObject z) {
        if (this.locker == z) {
            RUN_SPEED = 3.5f;
            JUMP_GRAVITY = -7.5f;
        }
    }

    public void removeEffect(String code) {

        switch (code) {
            case "./img/shoes.png":
                RUN_SPEED = 3.5f;
                JUMP_GRAVITY = -7.5f;
                break;

            case "banana":
                RUN_SPEED = 3.5f;
                JUMP_GRAVITY = -7.5f;
                break;

        }
    }

    public void respawn() {
        if (ghostMode == false && immunity == false) {
            this.x = respawnX;
            this.y = respawnY;
            this.velX = 0;
            this.velY = 5;
            immunity = true;
            canMove = false;
            i = 0;
            currentAnimState = AnimationStates.IDLE;
        }
    }

    public void bounce(int speed) {
        this.bouncing = true;
        bouncingSpeed = speed;
        bounceImmunity = true;
        bi = 0;
    }


    public boolean isOnGround() {
        return SolidCollider.willCauseSolidCollision(this, 5, false);
    }

    public boolean isOnWall() {
		return (SolidCollider.willCauseSolidCollision(this, this.velX, true) || SolidCollider.willCauseSolidCollision(this, -this.velX, true))
				&& !isOnGround();
    }

    public boolean hasCeilingAbove() {
        return SolidCollider.willCauseSolidCollision(this, -5, false);
    }

    public void handleCollisions(LinkedList<CollidingObject> collisions) {
    }

    public void setVelX(float velX) {
        this.velX = velX;
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
        if (immunity == true) {
            if (!(0 < i && i < 10 || 30 < i && i < 40 || 60 < i && i < 70)) {
                this.renderAnim(g, (int) (this.x + xOffset), (int) (this.y + yOffset));
            }
        } else {
            this.renderAnim(g, (int) (this.x + xOffset), (int) (this.y + yOffset));
        }

    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
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

    public ArrayList<Item> getInventory() {
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

    public int firstFreeSpace() {

        for (int i = 0; i < inventorySize; i++) {
            if (inventory.get(i).getUrl().equals("./img/empty.png"))
                return i;
        }

        return -1;
    }

    public void setCanDoubleJump(boolean canDoubleJump) {
        this.canDoubleJump = canDoubleJump;
    }

    public void addEffect(Effect e) {
        currentEffects.add(e);
    }

    public boolean isBounceImmune() {
        return bounceImmunity;
    }

    public LinkedList<RespawnPoint> getRespawnPoints() {
        return previousRespawnPoints;
    }

    public GameObject getLocker() {
        return locker;
    }

    public void removeLocker() {
        locker = null;
    }

    public void renderAnim(Graphics g, int x, int y) {

        if (currentAnimState != null) {
            currentAnimation = animations.get(currentAnimState);

            frame = (animationTimer / currentAnimation.getTicksPerFrame());

            g.drawImage(currentAnimation.getFrame(frame), x, y, null);

            animationTimer++;

            if (animationTimer >= currentAnimation.getTicksPerFrame() * currentAnimation.getNumberOfFrames()) {
                animationTimer = 0;
            }
        }
    }

    public void setCurrentAnimState(AnimationStates currentAnimState) {
        this.currentAnimState = currentAnimState;
    }

    public void setGhostMode(boolean ghostMode) {
        this.ghostMode = ghostMode;
    }

    public void setAnimations(HashMap<AnimationStates, Animation> animations) {
        animationTimer=0;
        this.animations = animations;
    }

    public boolean isGhostMode() {
        return ghostMode;
    }

    public String getObjectModel() {
        return objectModel;
    }
}
