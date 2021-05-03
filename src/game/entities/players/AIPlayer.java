package game.entities.players;

import java.util.UUID;

import game.Effect;
import game.attributes.SolidCollider;
import game.entities.areas.RespawnPoint;
import game.entities.areas.Waypoint;
import game.graphics.AnimationStates;
import game.graphics.LevelState;

import static game.Level.getAIPlayers;

public class AIPlayer extends Player {

	/**
	 * The direction in which the AIPlayer is running
	 */
	private String direction = "N";
	
	/**
	 * Whether the AIPlayer will jump or not (when 'wait' equals zero)
	 */
	private String jump = "N";
	
	/**
	 * The amount of ticks for which the AIPlayer will halt before continuing its movement
	 */
	private float wait = 0;

	/**
	 * The human player associated with the AIPlayer (i.e. the user)
	 */
	private Player humanPlayer;

	/**
	 * The distance between the AIPlayer and its human player
	 */
	private float dist_from_player;

	/**
	 * The penultimate RespawnPoint that the human player visited
	 */
	private RespawnPoint penultimateRespawnPoint;

	/**
	 * The maximum distance behind the human player than an AIPlayer can get before measures are taken
	 * to move closer to the human player
	 */
	private int MAX_DISTANCE_BEHIND = -400;
	
	/**
	 * The last Waypoint that the AIPlayer visited
	 */
	public Waypoint currentWaypoint;
	
	/**
	 * The ID of the AIPlayer, used for ordering to decide which player halts to not get stuck to another AIPlayer
	 */
	private int id;

	/**
	 * The time elapsed since the AIPlayer picked up an item from a Chest
	 */
	private int inventoryTimer = 0;

	/**
	 * Used to commence the inventoryTimer
	 */
	private boolean inventoryTimerOn = false;
	
	/**
	 * The time elapsed since the AIPlayer was in contact with another AIPlayer
	 */
	private int interactionTimer = 0;

	/**
	 * The time the AIPlayer must halt for after being in contact with another AIPlayer for too long
	 */
	private int interactionWait = 0;
	
	// private String username;

	public AIPlayer(float x, float y, int width,int height, Player humanPlayer,String url) {
		super(x, y, null, width, height,url);

		this.humanPlayer = humanPlayer;
		this.id = Character.getNumericValue(url.charAt(6));
	}

	/**
	 * Called every frame, and is responsible for the AIPlayer's movement and 
	 * interaction with chests, items, and the Wall of Death.
	 */
	public void tick() {

		System.out.println(this.id + "Wait" + this.wait);
		System.out.println(this.id + "Jump" + this.jump);
		System.out.println(this.id + "Direction" + this.direction);

		super.tick();

		this.dist_from_player = this.x - this.humanPlayer.getX();

		if (this.isGhostMode() == false) {

			if (this.humanPlayer.isGhostMode() == false) {

				// sends AI Player to the penultimate RespawnPoint that the player has reached 
				if (this.dist_from_player < MAX_DISTANCE_BEHIND && humanPlayer.getRespawnPoints().size() > 2) {

					this.invincible = true;

					// Get the penultimate RespawnPoint that humanPlayer visited
					penultimateRespawnPoint = humanPlayer.getRespawnPoints().get(humanPlayer.getRespawnPoints().size()-2);

					if (this.humanPlayer.getX() - penultimateRespawnPoint.getX() > 350) {
						this.invincibleMove = true;
					}
				}

				if (this.invincibleMove == true) {

					// to make smooth transition on minimap
					if (this.x < penultimateRespawnPoint.getX()) {
						this.x += 3;
					} else { 
						this.invincible = false; 
						this.invincibleMove = false; 
					}

					// so that it only teleports once, and doesn't keep getting sent back
					if (!this.getRespawnPoints().contains(penultimateRespawnPoint)) {
						this.y = penultimateRespawnPoint.getY()-20;
					} 	
				}
			}	

			if (getLevelState() == LevelState.InProgress){
				for (AIPlayer p: getAIPlayers())	
					if (this.getInteraction(p)){

						this.interactionTimer += 3;

						if (this.interactionTimer >= 200) {

							if (this.id < p.id) {
								this.interactionWait += 20;
							} else {
								p.interactionWait += 20;
							}

							this.interactionTimer -= 200;
							p.interactionTimer -= 200; 

						}
					}
					else {
						this.interactionTimer -= 1;
					}
			}
				

			if (this.canMove == true && this.invincibleMove == false) {

				if (this.interactionWait > 0) {
					this.interactionWait--;
				} 
				else {

					if (this.wait > 0) {

						this.wait--;

						if (!SolidCollider.willCauseSolidCollision(this, this.velX, true)){
							if (this.velX >= -0.1f && this.velX <= 0.1f) {
								this.velX = 0;
								currentAnimState = AnimationStates.IDLE;
							} else if (this.velX > 0.1f) {
								this.velX -= DECELERATION;
							} else {
								this.velX += DECELERATION;
							}
						} else {
							this.velX = 0;
							currentAnimState = AnimationStates.IDLE;
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
								currentAnimState = AnimationStates.RIGHT;

							} else if(this.direction.equals("L")) { 

									// Simulates acceleration when running left
									if (this.velX <= -RUN_SPEED){
										this.velX = -RUN_SPEED;
									} else {
										this.velX -= RUN_SPEED/6;
									}
								currentAnimState = AnimationStates.LEFT;

							} else { 
								// For deceleration effect
								if (!SolidCollider.willCauseSolidCollision(this, this.velX, true)){
									if (this.velX >= -0.1f && this.velX <= 0.1f) {
										this.velX = 0;
										currentAnimState = AnimationStates.IDLE;
									} else if (this.velX > 0.1f) {
										this.velX -= DECELERATION;
									} else {
										this.velX += DECELERATION;
									}
								} else {
									this.velX = 0;
									currentAnimState = AnimationStates.IDLE;
								}
							}

						
							if(jump.equals("Y")) {
								if(isOnGround() && !hasCeilingAbove() && !isOnWall()) {
									
									this.velY = JUMP_GRAVITY;
								}
								this.jump = "N";
							}
						}
					}

					if (this.inventoryTimerOn == true) {

						this.inventoryTimer++;

						if (this.inventoryTimer >= 80) {
							
							INVENTORY.get(0).getEffect();
							if (!(INVENTORY.get(0).getUrl().equals("./img/jump.png")) && !(INVENTORY.get(0).getUrl().equals("./img/banana.png")))
								CURRENT_EFFECTS.add(new Effect(INVENTORY.get(0).getUrl(), 500));
							INVENTORY.get(0).setUrl("./img/empty.png");
							this.setInventoryChanged(true);

							this.inventoryTimer = 0;
							this.inventoryTimerOn = false;
						}
					}
			}
			
		} else {	// If AIPlayer isGhostMode

			if (this.y < this.humanPlayer.getY()+600) {

				if (this.velY <= -6f) {
					this.velY = -6f;
				} else {
					this.velY -= 0.5;
				}

				this.velX = 0;

			}
		}
	}

	/**
	 * Commences the inventory timer; called when the AIPlayer touches a Chest.
	 */
	public void startInventoryTimer(){
		this.inventoryTimerOn = true;
	}

	/**
	 * Returns whether a given object is in contact (i.e. interacting) with the AIPlayer.
     * @param aiPlayer The object that we check is in contact with the AIPlayer.
	 */
	public boolean getInteraction(Player player){
		return ((int)this.x 		 < 	(int)player.getX()+player.getWidth() && 
				(int)player.getX() < 	this.x+this.width && 
				(int)this.y-20 	 < 	(int)player.getY()+player.getHeight() && 
				(int)player.getY() < 	(int)this.y+this.height);
	}


	//GETTERS AND SETTERS

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

	public Waypoint getCurrentWaypoint() {
		return currentWaypoint;
	}

	public void setCurrentWaypoint(Waypoint currentWaypoint) {
		this.currentWaypoint = currentWaypoint;
	}

	private LevelState getLevelState() {
		return null;
	}

}
