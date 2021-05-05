package game.entities.areas;

import game.entities.players.Player;
import game.graphics.AnimationStates;

import java.util.LinkedList;

import static game.Level.getPlayers;

public class TrackingAI extends EventScriptedDamageZone{

	private Player mytarget;
	private float speed;

	public TrackingAI(float x, float y, int width, int height, float speed, LinkedList<Point> points, int startOffset, Player player, String url) {
		super(x, y, width, height, speed,points,startOffset, url);
		this.mytarget=player;
		this.speed=speed;
		this.setActivated(false);
	}

	/**
	 * Checks if the target is within its activation area, and follows the target if it has been activated.
	 */
	public void tick() {

			for (Area a: this.getEventArea() )
				if (a.getInteraction(mytarget)) {
					this.setActivated(true);
					this.setVisibile(true);
				}
				else {
					this.setActivated(false);
					this.setVisibile(false);
				}


		for (Player p : getPlayers())
			if (this.getActivated())
				if (this.getInteraction(p))
					p.respawn();


		if (this.getActivated())
		{
			this.getPoints().get(1).setX((int)mytarget.getX());
			this.getPoints().get(1).setY((int)mytarget.getY());

			if (this.x < this.getPoints().get(1).getX()) {
				currentAnimState=AnimationStates.OTHER;
				this.x = this.x + speed;
			}
			else if (this.x > this.getPoints().get(1).getX()) {
				currentAnimState=AnimationStates.IDLE;
				this.x = this.x - speed;
			}

			if (this.y < this.getPoints().get(1).getY())
				this.y= this.y + speed;
			else if (this.y > this.getPoints().get(1).getY())
				this.y = this.y - speed;

		}
		else
		{
			this.x = this.getPoints().get(0).getX();
			this.y = this.getPoints().get(0).getY();
		}

	}



}
