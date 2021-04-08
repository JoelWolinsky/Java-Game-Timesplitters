package game.entities.areas;
import game.entities.AIPlayer;

import java.awt.*;

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
