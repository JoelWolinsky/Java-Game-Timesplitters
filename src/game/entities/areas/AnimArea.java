package game.entities.areas;
import game.attributes.AnimatedObject;
import game.graphics.Animation;
import game.graphics.AnimationStates;

import java.awt.*;
import java.util.HashMap;

public class AnimArea extends Area implements AnimatedObject {

	private static int animationTimer = 0;
	private static AnimationStates defaultAnimationState = AnimationStates.IDLE;
	private static AnimationStates currentAnimationState = defaultAnimationState;
	private static HashMap<AnimationStates, Animation> animations = new HashMap<AnimationStates, Animation>();
	private Animation anim;
	private boolean visibile=true;

	public AnimArea(float x, float y, int width, int height, String...urls) {
		super(x, y, width, height,urls[0]);
		anim = new Animation(10, urls);
	}

	public void tick() {
	}

	public void render(Graphics g, float f, float h) {

		//g.setColor(Color.magenta);
		//g.fillRect((int)(this.x + f),(int)(this.y + h),this.width,this.height);
		if (isVisibile())
			this.renderAnimAlt(g, (int)(this.x+f), (int)(this.y+h),anim);
	}

	public int getAnimationTimer() {
		return this.animationTimer;
	}

	public void setAnimationTimer(int animationTimer) {
		this.animationTimer = animationTimer;

	}

	public AnimationStates getCurrentAnimationState() {
		return this.currentAnimationState;
	}

	public AnimationStates getDefaultAnimationState() {
		return this.defaultAnimationState;
	}

	public Animation getAnimation(AnimationStates state) {
		return animations.get(state);
	}

	public boolean isVisibile() {
		return visibile;
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}
}
