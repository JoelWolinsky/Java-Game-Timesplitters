package game.entities.players;

import java.util.LinkedList;
import java.util.UUID;

import game.attributes.SolidCollider;
import game.entities.areas.RespawnPoint;
import game.entities.areas.Waypoint;
import game.graphics.AnimationStates;

public class AIPlayer extends Player {

    public String direction = "N"; // or private?
    public String jump = "N";
    public float wait = 0;
	public Player humanPlayer;
	public float dist_from_player;
	public RespawnPoint penultimateRespawnPoint;
	private int max_distance_ahead = 650;
	private int max_distance_behind = -550;
	private String username;
	private Waypoint currentWaypoint;


	public AIPlayer(float x, float y, int width,int height, Player humanPlayer,String url) {
		super(x, y, null, width, height,url);

		
		this.username = UUID.randomUUID().toString();;
		this.humanPlayer = humanPlayer;
	}

	public void tick() {


		super.tick();

		dist_from_player = this.x - this.humanPlayer.getX();

		if (dist_from_player < max_distance_ahead) {

			if (humanPlayer.isGhostMode() == false) {

				// teleports AI Player to the penultimate RespawnPoint that the player has reached 
				if (dist_from_player < max_distance_behind) {

					if (humanPlayer.getRespawnPoints().size() > 2){

						// first get the penultimate one
						penultimateRespawnPoint = humanPlayer.getRespawnPoints().get(humanPlayer.getRespawnPoints().size()-2);

						// so that it only teleports once, and doesn't keep getting sent back
						if (!this.getRespawnPoints().contains(penultimateRespawnPoint)) {

							if (this.humanPlayer.getX() - penultimateRespawnPoint.getX() > 350) {

								// then set AIPlayer x to the value of that RP
								this.x = penultimateRespawnPoint.getX();
								this.y = penultimateRespawnPoint.getY()-40;

							}							
						}
					}
				}
			}
		
			if (this.canMove == true ) {

					if (this.wait > 0) {

						// System.out.println(this.wait);
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
				}
			}
		

		


		/*
		
		if(Game.isMultiplayer) {
			if((int)this.x != this.prevPos.x || (int)this.y != this.prevPos.y) {
				Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y);
				packet.writeData(Game.socketClient);
			}
		}
	 */
	


	//AI COMMANDS

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

	public void setCurrentWaypoint(Waypoint currentWaypoint) {
		this.currentWaypoint = currentWaypoint;
	}

	public Waypoint getCurrentWaypoint() {
		return currentWaypoint;
	}
}
