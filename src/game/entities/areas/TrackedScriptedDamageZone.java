package game.entities.areas;

import game.entities.players.Player;

import java.util.LinkedList;

public class TrackedScriptedDamageZone extends EventScriptedDamageZone{

	Player mytarget;

	public TrackedScriptedDamageZone(float x, float y, int width, int height, float speed, LinkedList<Point> points, int startOffset, Player player, String...urls) {
		super(x, y, width, height, speed,points,startOffset, urls);
		this.mytarget=player;
		this.setActivated(false);
	}

	public void tick() {

		if (this.getActivated())
		{
			this.getPoints().get(1).setX((int)mytarget.getX());
			this.getPoints().get(1).setY((int)mytarget.getY());
			super.tick();
		}

	}


}
