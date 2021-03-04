package game;
import game.attributes.AnimatedObject;
import game.entities.Player;
import game.graphics.Animation;
import game.graphics.AnimationStates;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Area extends GameObject implements AnimatedObject {
	private BufferedImage img;
	private int timer=0;
	private static int animationTimer = 0;
	private static AnimationStates defaultAnimationState = AnimationStates.IDLE;
	private static AnimationStates currentAnimationState = defaultAnimationState;
	private static HashMap<AnimationStates, Animation> animations = new HashMap<AnimationStates, Animation>();
	private Animation anim;
	private int increment = 0;
	private boolean active = true;
	private String areaScope;

	public Area(float x, float y, int width, int height, int increment, String areaScope, String...urls) {
		super(x, y, -2, width, height);
		this.increment=increment;
		this.areaScope=areaScope;
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
		anim = new Animation(30000, urls);

	}

	public void tick() {

		if (increment!=0)
		{
			//sets the on/off timer for the area dmg
			if (timer==100)
			{
				if (this.active == true)
					this.active = false;
				else
					this.active = true;
				timer = 0;
			}

			timer+=increment;
		}

	}
	
	public void render(Graphics g, float f, float h) {

			//g.setColor(Color.magenta);
			//g.fillRect((int)(this.x + f),(int)(this.y + h),width,height);

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

	public boolean getInteraction(Player player){
		return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);
	}


	public boolean getActive(){
		return this.active;
	}

	public void setActive(boolean active){
		this.active = active;
	}

	public String getAreaScope() {
		return areaScope;
	}
}
