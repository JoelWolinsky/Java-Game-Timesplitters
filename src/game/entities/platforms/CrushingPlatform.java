package game.entities.platforms;

import game.attributes.CollidingObject;
import game.attributes.SolidCollider;
import game.entities.AIPlayer;
import game.entities.Player;

import static game.Level.getPlayers;

public class CrushingPlatform extends Platform implements SolidCollider{

	private float basePos, movingRange, velocity;
	private boolean charging = false;
	private int startOffset = 0;
	private int i;
	private float chargingSpeed;
	private String crushingSide;

	public CrushingPlatform(float x, float y, int width, int height, int movingRange, float velocity, String url, int startOffset, String crushingSide, float chargingSpeed) {
		super(x, y, width, height, url);
		this.movingRange = movingRange;
		this.startOffset=startOffset;
		this.crushingSide = crushingSide;
		this.chargingSpeed = chargingSpeed;

		if(crushingSide.equals("LEFT") || crushingSide.equals("RIGHT"))
			this.basePos = this.x;
		else if(crushingSide.equals("TOP") || crushingSide.equals("BOTTOM"))
			this.basePos = this.y;

		this.velocity = velocity;
		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
	}
	
	public void tick() {

		for (Player p: getPlayers()){

			if (this.getInteraction(p))
				p.respawn();
		}

		if (i<startOffset)
			i++;
		else{
			if(crushingSide.equals("LEFT") || crushingSide.equals("RIGHT")) {

				if (crushingSide.equals("RIGHT"))
					this.x += this.velocity;
				else if (crushingSide.equals("LEFT"))
					this.x -= this.velocity;
				if(Math.abs(this.x - this.basePos) >= this.movingRange) {
					if (charging == false)
					{charging= true;
						this.velocity = this.velocity-chargingSpeed;}
					else if (charging==true)
					{charging=false;
						this.velocity = this.velocity-chargingSpeed;}
					this.velocity = -this.velocity;

				}
			}else if(crushingSide.equals("TOP") || crushingSide.equals("BOTTOM")){
				if (crushingSide.equals("BOTTOM"))
					this.y += this.velocity;
				else if (crushingSide.equals("TOP"))
					this.y -= this.velocity;
				if(Math.abs(this.y - this.basePos) >= this.movingRange) {
					if (charging == false)
					{charging= true;
						this.velocity = this.velocity-chargingSpeed;}
					else if (charging==true)
					{charging=false;
						this.velocity = this.velocity-chargingSpeed;}
					this.velocity = -this.velocity;

				}
			}

		}

	}

	public boolean getInteraction(Player player){
		switch (crushingSide){
			case "BOTTOM":
				return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y+this.height<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);
			case "TOP":
				return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y);
			case "LEFT":
				return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x && (int)this.y<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);
			case "RIGHT":
				return ((int)this.x + this.width<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);

			default:
				return false;
		}

	}

	public boolean getInteraction(AIPlayer player){
		switch (crushingSide){
			case "BOTTOM":
				return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y+this.height<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);
			case "TOP":
				return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y);
			case "LEFT":
				return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x && (int)this.y<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);
			case "RIGHT":
				return ((int)this.x + this.width<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);

			default:
				return false;
		}
	}

	public float getVelocity() {
		return velocity;
	}

	public String getCrushingSide() {
		return crushingSide;
	}
}


