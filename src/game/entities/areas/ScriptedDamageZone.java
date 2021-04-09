package game.entities.areas;

import game.entities.players.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import static game.Level.getPlayers;

public class ScriptedDamageZone extends AnimArea{
	private int timer=0;
	private boolean active = true;
	private int i = 0;
	private int startOffset;
	private BufferedImage img;
	private float originalY;
	private float originalX;
	private float speed,speed2;
	private boolean lockX=false, lockY=false;
	private LinkedList<Point> points;

	int k=0;

	public ScriptedDamageZone(float x, float y, int width, int height, float speed, LinkedList<Point> points, int startOffset, String...urls) {
		super(x, y, width, height, urls);
		this.startOffset = startOffset;
		this.originalX=x;
		this.originalY=y;
		this.speed=speed;
		this.speed2=speed;
		this.points = (LinkedList) points.clone();

	}

	public void tick() {


			for (Player p : getPlayers())
			{
				if (this.getInteraction(p))
					p.respawn();
			}

			if (i<startOffset)
				i++;
			else
			{

				if (speed>0)
				{
					if (this.x >= points.get(k).getX())

						lockX=true;
				}
				else if (speed<0)
				{
					if (this.x <= points.get(k).getX())

						lockX=true;
				}


				if (speed2>0)
				{
					if (this.y >= points.get(k).getY())
						lockY= true;
				}
				else if (speed2<0)
				{
					if (this.y <= points.get(k).getY())

						lockY=true;
				}


				if (!lockX)
					this.x = this.x + (speed * points.get(k).getSpeed());
				if (!lockY)
					this.y = this.y + (speed2* points.get(k).getSpeed());

				if (lockY && lockX)
				{
					k++;

					if (k==points.size()) {
						this.x=originalX;
						this.y=originalY;
						speed=1;
						speed2=1;
						k = 1;
					}

					if (points.get(k).getX() - points.get(k - 1).getX() == 0)
					{
						lockX = true;
					}
					else
					{
						lockX = false;
						if (points.get(k).getX() - points.get(k - 1).getX() < 0) {
							if (speed > 0)
								speed = speed * (-1);
						} else if (points.get(k).getX() - points.get(k - 1).getX() > 0) {
							if (speed < 0)
								speed = speed * (-1);
						}
					}

					if (points.get(k).getY()-points.get(k-1).getY()==0)
					{
						lockY=true;
					}
					else
					{
						lockY=false;
						if (points.get(k).getY()-points.get(k-1).getY() < 0) {
							if (speed2 > 0)
								speed2 = speed2 * (-1);
						}
						else if (points.get(k).getY()-points.get(k-1).getY() > 0) {
							if (speed2 < 0)
								speed2 = speed2 * (-1);
						}
					}

				}

			}



	}

	@Override
	public void render(Graphics g, float f, float h) {
		super.render(g, f, h);
	}

	public boolean getActive(){
		return this.active;
	}
	public void setActive(boolean active){
		this.active = active;
	}


	public void addPoint(Point p)
	{
		points.add(p);
	}

	public Point getPoint(int index) {
		return points.get(index);
	}
}
