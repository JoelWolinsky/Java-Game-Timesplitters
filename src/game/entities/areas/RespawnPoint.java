package game.entities.areas;
import game.entities.AIPlayer;
import game.entities.Player;

public class RespawnPoint extends Area {

	private boolean reached = false;
	private boolean currentActive = false;

	public RespawnPoint(float x, float y, int width, int height, String url) {
		super(x, y, width, height, url);
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

	public boolean getInteraction(Player player){
		return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y-100<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);
	}

	public boolean getInteraction(AIPlayer player){
		return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y-100<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);
	}

}
