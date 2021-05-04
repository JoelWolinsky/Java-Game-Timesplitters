package game.entities.areas;

import game.entities.players.Player;

import java.util.LinkedList;

public class TrackedScriptedDamageZone extends EventScriptedDamageZone{

	Player mytarget;

	public TrackedScriptedDamageZone(float x, float y, int width, int height, float speed, LinkedList<Point> points, int startOffset, Player player, String url) {
		super(x, y, width, height, speed,points,startOffset, url);
		this.mytarget=player;
		this.setActivated(false);
	}

	/**
	 * Checks if it's target is within its activation area, and moves towards the target if so
	 */
	public void tick() {


		for (Area a: this.getEventArea())
			if (a.getInteraction(mytarget))
				this.setActivated(true);

		if (this.getActivated())
		{

			this.getPoints().get(1).setX((int)mytarget.getX());
			this.getPoints().get(1).setY((int)mytarget.getY());
			super.tick();
		}

	}


}
