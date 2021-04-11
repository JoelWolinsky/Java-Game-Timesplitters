package game.entities.areas;
import game.entities.players.Player;
import game.graphics.Image;

import static game.Level.getPlayers;

public class ExtendedRespawnPoint extends Area {

	private boolean reached = false;
	private boolean currentActive = true;

	public ExtendedRespawnPoint(float x, float y, int width, int height, String url) {
		super(x, y, width, height, Image.loadImage(url));

		this.width=width;
		this.height=height;

	}

	public void tick() {

		for (Player p : getPlayers())
		{
			if (this.getInteraction(p))
			{
				p.setRespawnThreshold((int)this.getY());
				this.setReached(true);
				this.setCurrentActive(true);
			}
		}


	}

	public void setCurrentActive(boolean currentActive)
	{
		this.currentActive = currentActive;
	}

	public boolean getCurrentActive()
	{
		return this.currentActive;
	}

	public void setReached(boolean reached)
	{
		this.reached = reached;
	}

	public boolean getReached()
	{
		return this.reached;
	}


}
