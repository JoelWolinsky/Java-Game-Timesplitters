package game.entities.areas;
import game.graphics.Animation;
import game.graphics.AnimationStates;
import java.awt.*;
import java.util.HashMap;

import static game.graphics.Assets.getAnimations;
import static game.graphics.Assets.getImageForReference;

public class AnimArea extends Area{

	private boolean visibile=true;
	protected HashMap<AnimationStates, Animation> animations;
	protected int animationTimer = 0;
	protected int frame;
	protected AnimationStates currentAnimState;
	protected Animation currentAnimation;

	public AnimArea(float x, float y, int width, int height, String url) {
		super(x, y, width, height,getImageForReference(url));

		animations=getAnimations(url);
		this.currentAnimState = AnimationStates.IDLE;
	}

	public void tick() {
	}
	
	public void render(Graphics g, float xOffset, float yOffset) {
		if (isVisibile())
			this.renderAnim(g, (int)(this.x+xOffset), (int)(this.y+yOffset));
	}


	public boolean isVisibile() {
		return visibile;
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}

	/**
	 * Calculates and draws the correct frame of annimation based on the animation timer
	 * @param g The graphics object onto which the animation will be drawn
	 * @param x The x position of the image
	 * @param y The y position of the image
	 */
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
