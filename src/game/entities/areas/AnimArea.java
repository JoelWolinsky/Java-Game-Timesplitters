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

	public void render(Graphics g, float f, float h) {

		//g.setColor(Color.magenta);
		//g.fillRect((int)(this.x + f),(int)(this.y + h),this.width,this.height);
		if (isVisibile())
			this.renderAnim(g, (int)(this.x+f), (int)(this.y+h));
	}


	public boolean isVisibile() {
		return visibile;
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
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
