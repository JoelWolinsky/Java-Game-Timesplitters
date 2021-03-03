package game.entities;

import game.attributes.CollidingObject;
import game.attributes.SolidCollider;

public class TimerPlatform extends Platform {

	private int timer=0;
	private boolean active = true;
	private int speed = 0;
	private int startOffset;
	private int i=0;
	public TimerPlatform(float x, float y, int width, int height,String url, int speed,int startOffset) {
		super(x, y, width, height, url);
		this.speed = speed;
		this.startOffset=startOffset;
	}
	
	public void tick() {

		if (i<startOffset)
			i++;
		else
		{
			if (timer==100)
			{
				if (this.active == true)
				{
					this.active = false;
					//CollidingObject.removeCollider(this);
					SolidCollider.removeSolidCollider(this);
				}
				else
				{
					this.active = true;
					//CollidingObject.addCollider(this);
					SolidCollider.addSolidCollider(this);
				}
				timer = 0;
			}

			timer+=speed;
		}

	}

	public boolean getActive(){
		return this.active;
	}

	public void setActive(boolean active){
		this.active = active;
	}

}
