package game.entities;
import game.graphics.Animation;
import game.graphics.AnimationStates;
import game.graphics.Assets;

import java.awt.Graphics;
import java.util.HashMap;

/**
 * Superclass for all objects within the game.<br>
 * Contains basic important information, and enforces required functions.
 */
public abstract class GameObject {
	
	protected float x,y;
	protected int z,width,height;

	//animations related
	protected HashMap<AnimationStates, Animation> animations = new HashMap<AnimationStates, Animation>();
	protected int animationTimer = 0;
	protected int frame;
	protected AnimationStates currentAnimState;
	protected Animation currentAnimation;
	
	/**
	 * GameObject constructor.<br>
	 * This should be called in every subclass's constructor<br>
	 * <pre>super(x, y, z, width, height);</pre>
	 * @param x The starting x coordinate of the object
	 * @param y The starting y coordinate of the object
	 * @param z The z index of the coordinate. Higher indexes are rendered on top of later indexes.
	 * @param width The width of the object
	 * @param height The height of the object
	 */
	public GameObject(float x, float y, int z, int width, int height) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
	}

	
	/**
	 * Required function for all subclasses<br>
	 * Performs any operations that should be performed every frame
	 */
	public abstract void tick();
	
	/**
	 * Required function for all subclasses<br>
	 * Performs any rendering
	 * @param g The Graphics object onto which the object will be rendered
	 */
	public abstract void render(Graphics g, float xOffset, float yOffset);

	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getZ() {
		return z;
	}	
	public void setZ(int z) {
		this.z = z;
	}

	public void renderAnim(Graphics g, int x, int y) {

		if (currentAnimState!=null)
		{
			currentAnimation = animations.get(currentAnimState);

			frame = (animationTimer / currentAnimation.getTicksPerFrame());

			g.drawImage(currentAnimation.getFrame(frame), x, y, null);

			animationTimer ++;

			if (animationTimer >= currentAnimation.getTicksPerFrame() * currentAnimation.getNumberOfFrames()) {
				animationTimer = 0;
			}
		}
	}


}
