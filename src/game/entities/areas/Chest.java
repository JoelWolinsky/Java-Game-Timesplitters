package game.entities.areas;
import game.attributes.AnimatedObject;
import game.graphics.Animation;
import game.graphics.AnimationStates;

import java.awt.*;
import java.util.HashMap;

public class Chest extends AnimArea{

	private boolean visibile=true;

	public Chest(float x, float y, int width, int height, String...urls) {
		super(x, y, width, height,urls);
		System.out.println("bruh");
	}



}
