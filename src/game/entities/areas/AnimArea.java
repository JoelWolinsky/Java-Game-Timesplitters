package game.entities.areas;
import game.graphics.Animation;
import game.graphics.AnimationStates;
import java.awt.*;

public class AnimArea extends Area{

	private boolean visibile=true;

	public AnimArea(float x, float y, int width, int height, String...urls) {
		super(x, y, width, height,urls[0]);

		this.currentAnimState = AnimationStates.IDLE;
		animations.put(AnimationStates.IDLE, new Animation(15, urls));
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

}
