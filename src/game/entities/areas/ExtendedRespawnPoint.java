package game.entities.areas;
import game.entities.players.Player;
import game.graphics.Image;

import static game.Level.getPlayers;

/**
 * This class is used to further extend the y range of the respawn point when players have for example to move downwards
 * This is due to the fact that any time a player will fall aproximately 300 blocks below the respawn point it will kill
 * the player and this is to attenuate that.
 */

public class ExtendedRespawnPoint extends Area {

	private boolean reached = false;
	private boolean currentActive = true;

	public ExtendedRespawnPoint(float x, float y, int width, int height, String url) {
		super(x, y, width, height, Image.loadImage(url));

		this.width=width;
		this.height=height;

	}

	/**
	 * Called every frame, this checks if a player has reached the respawn point, and updates its own display as well as the players' respawn threshold.
	 */
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
