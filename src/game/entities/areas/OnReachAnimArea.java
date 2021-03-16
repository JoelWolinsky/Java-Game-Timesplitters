package game.entities.areas;
import game.attributes.AnimatedObject;
import game.graphics.Animation;
import game.graphics.AnimationStates;

import java.awt.*;
import java.util.HashMap;

public class OnReachAnimArea extends AnimArea implements AnimatedObject {

	private boolean active = false;

	public OnReachAnimArea(float x, float y, int width, int height, String...urls) {
		super(x, y, width, height,urls);
	}

	public void setActive(boolean active) {
		this.active=active;
	}

	public boolean isActive() {
		return active;
	}
}
