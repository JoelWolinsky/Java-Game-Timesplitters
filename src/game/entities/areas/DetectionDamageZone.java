package game.entities.areas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class DetectionDamageZone extends ScriptedDamageZone{
	private boolean active = true;
	private AnimArea a;
	private int index=0,coord=1;

	int k=0;

	public DetectionDamageZone(float x, float y, int width, int height, float speed, LinkedList<Point> points, int startOffset, String...urls) {
		super(x, y, width, height, speed, points, startOffset, urls);


	}

	public void tick() {

		super.tick();
		index += coord;
		if (index>10 || index<-10)
			coord = coord *(-1);

		a.setX(index);

	}

	public boolean getActive(){
		return this.active;
	}
	public void setActive(boolean active){
		this.active = active;
	}
	public void setArea(AnimArea area){a=area;}
	public AnimArea getArea(){
		return this.a;
	}
}
