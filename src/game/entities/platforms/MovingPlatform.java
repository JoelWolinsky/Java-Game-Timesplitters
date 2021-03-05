package game.entities.platforms;

import game.attributes.CollidingObject;
import game.attributes.SolidCollider;

public class MovingPlatform extends Platform implements SolidCollider{
	
	private float basePos, movingRange, velocity;
	private boolean xAxis;
	private boolean charging = false;

	public MovingPlatform(float x, float y, int width, int height, int movingRange, boolean xAxis, float velocity, String url) {
		super(x, y, width, height, url);
		this.movingRange = movingRange;
		this.xAxis = xAxis;
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
		if(this.xAxis) {
				this.x += this.velocity;
			if(Math.abs(this.x - this.basePos) >= this.movingRange) {
				this.velocity = -this.velocity;
			}
		}else{
			this.y += this.velocity;
			if(Math.abs(this.y - this.basePos) >= this.movingRange) {
				this.velocity = -this.velocity;

			}
		}
	}

}
