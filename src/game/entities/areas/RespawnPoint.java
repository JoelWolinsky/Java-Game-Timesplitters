package game.entities.areas;
import game.entities.AIPlayer;
import game.entities.Player;

import static game.Level.getPlayers;

public class RespawnPoint extends Area {

	private boolean reached = false;
	private boolean currentActive = false;
	private float pointX,pointY;

	public RespawnPoint(float x, float y, int width, int height, int pointX, int pointY, String url) {
		super(x, y, width, height, url);
		this.pointX=pointX;
		this.pointY=pointY;
	}

	public void tick() {

		for (Player p : getPlayers())
		{
			if (this.getInteraction(p))
			{
				p.setRespawnX((int) this.getX() + (int)this.getExtraPointX());
				p.setRespawnY((int) this.getY()-40 + (int)this.getExtraPointY());
				p.setRespawnThreshold((int)this.getY());
				this.setReached(true);
				//this.setCurrentActive(true);


				if (!p.getRespawnPoints().contains(this)) {
					p.addRespawnPoint(this);
				}

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

	public boolean getInteraction(Player player){
		return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y-200<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);
	}

	public float getExtraPointX() {
		return pointX;
	}

	public float getExtraPointY() {
		return pointY;
	}

}
