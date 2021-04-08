package game.entities.areas;

import game.attributes.CollidingObject;
import game.entities.Player;

import static game.Level.getPlayers;

public class Projectile extends DamageZone {
	private float baseposX,baseposY;
	private float velX,velY;
	private float distance;
	private float randomRangeX;
	private float randomRangeY;
	private int startOffset;
	private int i=0;

	public Projectile(float x, float y, int width, int height, float velocityX, float velocityY, float distance, float randomRangeX,float randomRangeY, int startOffset, String notice, String...urls) {
		super(x, y, width, height,0,0,0,startOffset, notice, urls);
		this.randomRangeX=randomRangeX;
		this.randomRangeY=randomRangeY;
		this.startOffset=startOffset;
		this.baseposX=x;
		this.baseposY=y;
		this.distance = distance;
		this.velX = velocityX;
		this.velY =velocityY;
	}

	public void tick() {

		for (Player p : getPlayers())
		{
			if (this.getInteraction(p))
				p.respawn();
		}

		if (i<startOffset)
			i++;
		else{
			if (!super.getActive())
				super.setActive(true);
			if (velY<0)
			{
				if (this.x < (this.baseposX + this.distance) && this.y > (this.baseposY -this.distance)) {
					this.x += this.velX;
					this.y += this.velY;
				}
				else
				{
					if (randomRangeX==0)
						this.x=this.baseposX;
					else
						this.x = (int)(Math.random() * (this.baseposX - randomRangeX + 1)) + randomRangeX;

					if (randomRangeY==0)
						this.y=this.baseposY;
					else
						this.y = (int)(Math.random() * (this.baseposY - randomRangeY + 1)) + randomRangeY;

					if (!super.getNotice().equals("nopicture.png"))
					{
						i=0;
						super.setActive(false);
					}
				}
			}

			else if (velY>0){
				if (this.x < (this.baseposX + this.distance) && this.y < (this.baseposY +this.distance)) {
					this.x += this.velX;
					this.y += this.velY;
				}
				else
				{
					if (randomRangeX==0)
						this.x=this.baseposX;
					else
						this.x = (int)(Math.random() * (this.baseposX - randomRangeX + 1)) + randomRangeX;

					if (randomRangeY==0)
						this.y=this.baseposY;
					else
						this.y = (int)(Math.random() * (this.baseposY - randomRangeY + 1)) + randomRangeY;

					if (!super.getNotice().equals("nopicture.png"))
					{
						i=0;
						super.setActive(false);
					}
				}
			}
			else if (velY==0)
			{
				if (velX<0)

				if (this.x > (this.baseposX - this.distance)) {

					this.x += this.velX;
				}
				else
				{

					if (randomRangeX==0)
						this.x=this.baseposX;
					else
						this.x = (int)(Math.random() * (this.baseposX - randomRangeX + 1)) + randomRangeX;

					if (randomRangeY==0)
						this.y=this.baseposY;
					else
						this.y = (int)(Math.random() * (this.baseposY - randomRangeY + 1)) + randomRangeY;

					if (!super.getNotice().equals("nopicture.png"))
					{
						i=0;
						super.setActive(false);
					}
				}

				else

				if (this.x < (this.baseposX + this.distance)) {
					this.x += this.velX;
				}
				else
				{

					if (randomRangeX==0)
						this.x=this.baseposX;
					else
						this.x = (int)(Math.random() * (this.baseposX - randomRangeX + 1)) + randomRangeX;

					if (randomRangeY==0)
						this.y=this.baseposY;
					else
						this.y = (int)(Math.random() * (this.baseposY - randomRangeY + 1)) + randomRangeY;

					if (!super.getNotice().equals("nopicture.png"))
					{
						i=0;
						super.setActive(false);
					}
				}
			}
		}

	}

	public void setBaseposX(float baseposX) {
		this.baseposX = baseposX;
	}

	public void setBaseposY(float baseposY) {
		this.baseposY = baseposY;
	}



}
