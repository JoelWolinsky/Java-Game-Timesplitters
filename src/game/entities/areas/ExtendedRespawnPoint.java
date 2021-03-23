package game.entities.areas;
import game.entities.AIPlayer;
import game.entities.Player;

public class ExtendedRespawnPoint extends Area {

	private boolean reached = false;
	private boolean currentActive = true;
	private float pointX,pointY;

	public ExtendedRespawnPoint(float x, float y, int width, int height, String url) {
		super(x, y, width, height, url);

		this.width=width;
		this.height=height;

	}

	public void tick() {
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
