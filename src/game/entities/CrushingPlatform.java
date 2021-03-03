package game.entities;

import game.attributes.CollidingObject;
import game.attributes.SolidCollider;

public class CrushingPlatform extends Platform implements SolidCollider{

	private float basePos, movingRange, velocity;
	private boolean xAxis;
	private boolean charging = false;
	private int startOffset = 0;
	private int i;

	public CrushingPlatform(float x, float y, int width, int height, int movingRange, boolean xAxis, float velocity, String url, int startOffset) {
		super(x, y, width, height, url);
		this.movingRange = movingRange;
		this.xAxis = xAxis;
		this.startOffset=startOffset;
		if(xAxis) {
			this.basePos = this.x;
		}else {
			this.basePos = this.y;
		}
		this.velocity = velocity;

		CollidingObject.addCollider(this);
		SolidCollider.addSolidCollider(this);
	}
	
	public boolean getXAxis() {
		return this.xAxis;
	}
	public float getVelocity() {
		return this.velocity;
	}
	
	public void tick() {
		if (i<startOffset)
			i++;
		else{
			if(this.xAxis) {
				if (this.velocity<0)
					this.x += this.velocity + 4;
				else
					this.x += this.velocity;
				if(Math.abs(this.x - this.basePos) >= this.movingRange) {
					this.velocity = -this.velocity;
					if (charging == false)
						charging= true;
					else
						charging=false;
				}
			}else{
				this.y += this.velocity;
				if(Math.abs(this.y - this.basePos) >= this.movingRange) {
					if (charging == false)
					{charging= true;
						this.velocity = this.velocity-7.5f;}
					else if (charging==true)
					{charging=false;
						this.velocity = this.velocity-7.5f;}
					this.velocity = -this.velocity;

				}
			}

		}

	}

}
