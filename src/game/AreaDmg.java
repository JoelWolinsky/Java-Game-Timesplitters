package game;
import game.attributes.AnimatedObject;
import game.graphics.Animation;
import game.graphics.AnimationStates;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AreaDmg extends GameObject implements AnimatedObject {
	private BufferedImage img;
	private int timer;
	private static int animationTimer = 0;
	private static AnimationStates defaultAnimationState = AnimationStates.IDLE;
	private static AnimationStates currentAnimationState = defaultAnimationState;
	private static HashMap<AnimationStates, Animation> animations = new HashMap<AnimationStates, Animation>();
	private boolean active = true;

	public AreaDmg(float x, float y, int width, int height, String...urls) {
		super(x, y, -2, width, height);
		try
		{
			//sets the width and height of the platform based on the provided image width and height
			img = ImageIO.read( new File(urls[0]));
			this.width = img.getWidth();
			this.height = img.getHeight();
		}
		catch ( IOException exc )
		{
			//TODO: Handle exception.
		}

		animations.put(AnimationStates.IDLE, new Animation(20, urls));


	}

	public void tick() {

		//sets the on/off timer for the area dmg
		if (timer==40)
		{
			if (this.active == true)
				this.active = false;
			else
				this.active = true;
			timer = 0;
		}

		timer++;

	}
	
	public void render(Graphics g, float f, float h) {

			g.setColor(Color.magenta);
			g.fillRect((int)(this.x + f),(int)(this.y + h),width,height);
			this.renderAnim(g, (int)(this.x+f), (int)(this.y+h));

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

	public boolean getActive(){
		return this.active;
	}

	public void setActive(boolean active){
		this.active = active;
	}
}
