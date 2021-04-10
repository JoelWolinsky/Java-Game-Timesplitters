package game.entities.areas;
import game.graphics.AnimationStates;
import java.awt.*;

public class AnimArea extends Area{

	private boolean visibile=true;

	public AnimArea(float x, float y, int width, int height, String url) {
		super(x, y, width, height,url);

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

}
