package game.entities.areas;

import game.entities.Player;

import java.util.LinkedList;

public class TrackingAI extends EventScriptedDamageZone{

	private Player mytarget;
	private float speed;

	public TrackingAI(float x, float y, int width, int height, float speed, LinkedList<Point> points, int startOffset, Player player, String...urls) {
		super(x, y, width, height, speed,points,startOffset, urls);
		this.mytarget=player;
		this.speed=speed;
		this.setActivated(false);
	}

	public void tick() {

		if (this.getActivated())
		{
			this.getPoints().get(1).setX((int)mytarget.getX());
			this.getPoints().get(1).setY((int)mytarget.getY());

			if (this.x < this.getPoints().get(1).getX())
				this.x= this.x + speed;
			else if (this.x > this.getPoints().get(1).getX())
				this.x = this.x - speed;

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
