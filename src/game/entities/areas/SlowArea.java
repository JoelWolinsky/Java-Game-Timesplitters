package game.entities.areas;
import game.entities.players.Player;
import static game.Level.getPlayers;

public class SlowArea extends AnimArea{


	public SlowArea(float x, float y, int width, int height, String url) {
		super(x, y, width, height,url);
	}

	/**
	 * Checks for players intersecting with th eobject and slows them down.
	 */
	public void tick() {

		for (Player p: getPlayers())
		{
			if (this.getInteraction(p))
			{
				p.setRunSpeed(2.0f);
				p.setJumpGravity(-5.5f);
				p.setLocker(this);
			}
			else
			{
				if (this == p.getLocker()) {
					p.normal(this);
					p.removeLocker();
				}
			}
		}

	}


}
