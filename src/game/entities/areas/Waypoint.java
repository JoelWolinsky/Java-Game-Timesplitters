package game.entities.areas;
import game.entities.players.AIPlayer;
import game.graphics.Image;

import java.awt.*;
import static game.Level.*;

public class Waypoint extends Area {

	/**
	 * The direction in which the Waypoint will send the AIPlayer upon contact: Left, Right, or Stationary
	 */
	private String direction;
	
	/**
	 * Indicates whether the AIPlayer will jump upon contact with the Waypoint (when 'wait' equals 0)
	 */
	private String jump;
	
	/**
	 * The amount of ticks for which the AIPlayer will halt before continuing its movement upon contact with the Waypoint
	 */
	private float wait;

	public Waypoint(float x, float y, int width, int height, 
		String url, String direction, String jump, int wait) {
			
			super(x, y, width, height, Image.loadImage(url));
			this.direction = direction;
			this.jump = jump;
			this.wait = wait;

	}

	/**
	 * Called every frame, and is responsible for updating an AIPlayer's motion variables upon contact with a Waypoint
	 */
    public void tick() {

		for (AIPlayer ap : getAIPlayers())
			if (this.getInteraction(ap))
			{
				if (ap.immunity == true && ap.i < 2) {

					ap.setWait(this.getWait());
					ap.setCurrentWaypoint(this);
					ap.setDirection(this.getDirection());
					ap.setJump(this.getJump());
					
				} else {

					if (ap.getCurrentWaypoint()!=this) {
						ap.setWait(this.getWait());
						ap.setCurrentWaypoint(this);
						ap.setDirection(this.getDirection());
						ap.setJump(this.getJump());
					}

				}
			}
	}

	/**
	 * Renders the Waypoint flag, used for debugging.
     * @param g The Graphics object onto which the object will be rendered
	 * @param f The xOffset of the object
	 * @param h The yOffset of the object
	 */
	@Override
	public void render(Graphics g, float f, float h) {
		//super.render(g, f, h);
	}

	public String getDirection()
	{
		return this.direction;
	}

    public String getJump()
	{
		return this.jump;
	}

    public float getWait()
	{
		return this.wait;
	}

	/**
	 * Returns whether a given object is in contact (i.e. interacting) with the Waypoint.
     * @param aiPlayer The object that we check is in contact with the Waypoint.
	 */
	public boolean getInteraction(AIPlayer aiPlayer){
		return ((int)this.x 		 < 	(int)aiPlayer.getX()+aiPlayer.getWidth() && 
				(int)aiPlayer.getX() < 	this.x+this.width && 
				(int)this.y-20 	 < 	(int)aiPlayer.getY()+aiPlayer.getHeight() && 
				(int)aiPlayer.getY() < 	(int)this.y+this.height);
	}

}
