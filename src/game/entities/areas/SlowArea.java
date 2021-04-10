package game.entities.areas;
import game.entities.players.Player;
import static game.Level.getPlayers;

public class SlowArea extends AnimArea{


	public SlowArea(float x, float y, int width, int height, String url) {
		super(x, y, width, height,url);
	}

	public void tick() {

		for (Player p: getPlayers())
		{
			if (this.getInteraction(p))
			{
				p.slow();
				p.setLocker(this);
			}
			else
			{
				p.normal(this);
			}
		}

	}


}
