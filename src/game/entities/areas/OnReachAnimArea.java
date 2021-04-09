package game.entities.areas;
import game.entities.players.Player;
import static  game.Level.*;

public class OnReachAnimArea extends AnimArea{

	public OnReachAnimArea(float x, float y, int width, int height, String...urls) {
		super(x, y, width, height,urls);
	}

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
