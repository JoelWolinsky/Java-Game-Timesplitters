package game.entities.areas;
import game.entities.AIPlayer;
import game.entities.Player;

import java.awt.*;

import static game.Level.getPlayers;
import static game.Level.getWaypoints;

public class Waypoint extends Area {

	private boolean reached = false;
	private boolean currentActive = false;

	private String direction;
	private String jump;
	private float wait;

	public Waypoint(float x, float y, int width, int height, 
		String url, String direction, String jump, int wait) { 		// 'Wait' is the number of ticks the AIPlayer is to wait at each Waypoint
			
			super(x, y, width, height, url);
			this.direction = direction;
			this.jump = jump;
			this.wait = wait;

	}

    public void tick() {

		/*

		for (Player p : getPlayers())
		{
			if (p instanceof AIPlayer)
			if (this.getInteraction((AIPlayer) object1))
			{

				p.setDirection(this.getDirection());
				((AIPlayer) object1).setJump(((Waypoint) l).getJump());

				// Means a waypoint only has an effect once until player touches another one
				if (!getWaypoints().contains(this)) {

					getWaypoints().clear();
					getWaypoints().add(this);
					((AIPlayer) object1).setWait(((Waypoint) l).getWait());

				}
			}
		}

		 */

	}

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

	public boolean getInteraction(AIPlayer aiPlayer){
		return ((int)this.x 		 < 	(int)aiPlayer.getX()+aiPlayer.getWidth() && 
				(int)aiPlayer.getX() < 	this.x+this.width && 
				(int)this.y-20 	 < 	(int)aiPlayer.getY()+aiPlayer.getHeight() && 
				(int)aiPlayer.getY() < 	(int)this.y+this.height);
	}

}
