package game.entities.areas;
import game.attributes.AnimatedObject;
import game.graphics.Animation;
import game.graphics.AnimationStates;

import java.awt.*;
import java.util.HashMap;

public class SlowArea extends AnimArea implements AnimatedObject {


	public SlowArea(float x, float y, int width, int height, String...urls) {
		super(x, y, width, height,urls);
	}

	public void tick() {
	}

	public void render(Graphics g, float f, float h) {

		super.render(g,f,h);
		//g.setColor(Color.magenta);
		//g.fillRect((int)(this.x + f),(int)(this.y + h),this.width,this.height);
	}

}
