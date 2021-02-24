package game.entities;

import game.attributes.CollidingObject;
import game.attributes.SolidCollider;

public class MovingPlatform extends Platform implements SolidCollider{
	
	private float basePos, movingRange, velocity;
	private boolean xAxis;
	
	/**
	 * @param x The default x position of the platform
	 * @param y The default y position of the platform
	 * @param width The width of the platform
	 * @param height The height of the platform
	 * @param movingRange The range of the platform - It will move this number of pixels in both directions
	 * @param xAxis Is the platform moving along the x-axis?
	 * @param velocity The velocity at which the platform moves
	 */
	public MovingPlatform(float x, float y, int width, int height, int movingRange, boolean xAxis, float velocity) {
		super(x, y, width, height);
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
	
	/**
	 * Called every frame. This moves a moving platform to the correct place based on their velocity, range, and axis
	 */
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
	
	public boolean getXAxis() {
		return this.xAxis;
	}
	public float getVelocity() {
		return this.velocity;
	}

}
