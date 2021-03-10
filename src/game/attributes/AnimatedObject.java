package game.attributes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.graphics.Animation;
import game.graphics.AnimationStates;

/**
 * 
 * <b>Required class variables</b><br>
 * <pre>private static int animationTimer = 0;</pre><br>
 * <pre>private static AnimationStates defaultAnimationState = ...;</pre>
 * <pre>private static AnimationStates currentAnimationState = defaultAnimationState;</pre>
 * <pre>private static HashMap<AnimationStates, Animation> animations = new HashMap<AnimationStates, Animation>();</pre>
 * 
 * <b>In constructor</b>
 * <pre>Populate animations map</pre>
 * 
 * <b>In render function</b>
 * <pre>this.renderAnim(g, (int)this.x+xOffset, (int)this.y+yOffset);</pre>
 * @author bthar
 *
 */
public interface AnimatedObject {
	
	default int renderAnim(Graphics g, int x, int y) {
		AnimationStates currentState = getCurrentAnimationState();
		Animation currentAnimation = getAnimation(currentState);
		if(currentAnimation == null) {
			currentAnimation = getAnimation(getDefaultAnimationState());
		}
		int animationTimer = getAnimationTimer();
		int frame = (animationTimer / currentAnimation.getTicksPerFrame());
		BufferedImage currentImage = currentAnimation.getFrame(frame);
		g.drawImage(currentImage, (int)x, (int)y, null);
		animationTimer ++;
		if (animationTimer >= currentAnimation.getTicksPerFrame() * currentAnimation.getNumberOfFrames()) {
			animationTimer = 0;
		}
		setAnimationTimer(animationTimer);
		return frame;
	}

	default void renderAnimAlt(Graphics g, int x, int y, Animation anim) {
		Animation currentAnimation = anim;
		int animationTimer = getAnimationTimer();
		int frame = (animationTimer / currentAnimation.getTicksPerFrame());
		BufferedImage currentImage = currentAnimation.getFrame(frame);
		g.drawImage(currentImage, (int)x, (int)y, null);
		animationTimer ++;
		if (animationTimer >= currentAnimation.getTicksPerFrame() * currentAnimation.getNumberOfFrames()) {
			animationTimer = 0;
		}
		setAnimationTimer(animationTimer);
	}
	
	int getAnimationTimer();
	void setAnimationTimer(int animationTimer);
	AnimationStates getCurrentAnimationState();
	AnimationStates getDefaultAnimationState();
	Animation getAnimation(AnimationStates state);
}
