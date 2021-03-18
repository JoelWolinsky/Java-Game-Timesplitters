package game.entities.areas;
import game.attributes.AnimatedObject;
import game.graphics.Animation;
import game.graphics.AnimationStates;

import java.awt.*;
import java.util.HashMap;

public class Portal extends AnimArea{

	private int destination;

	public Portal(float x, float y, int width, int height, int destination, String...urls) {
		super(x, y, width, height,urls);
		this.destination=destination;
	}

	public void tick() {
	}


	public int getDestination() {
		return destination;
	}
}
