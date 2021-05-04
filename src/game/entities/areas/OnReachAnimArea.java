package game.entities.areas;
import game.entities.players.Player;
import static  game.Level.*;

public class OnReachAnimArea extends AnimArea{

	public OnReachAnimArea(float x, float y, int width, int height, String url) {
		super(x, y, width, height,url);
	}

	/**
	 * Checks for player objects within the interactable area, and sets the object to visible if so.
	 */
	public void tick() {

		for (Player p: getPlayers())
		{
			if (this.getInteraction(p))
			{
				this.setVisibile(true);
			}
			else
			{
				this.setVisibile(false);
			}
		}
	}

}
