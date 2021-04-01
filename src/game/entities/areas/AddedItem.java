package game.entities.areas;
import game.attributes.AnimatedObject;
import game.entities.Player;
import game.graphics.Animation;
import game.graphics.AnimationStates;

import java.awt.*;
import java.util.HashMap;

public class AddedItem extends AnimArea implements AnimatedObject {

	private boolean visibile=true;
	private Player creator;
	private String effect;

	public AddedItem(float x, float y, int width, int height, Player creator, String...urls) {
		super(x, y, width, height,urls);
		this.creator=creator;

		effect = urls[0];
		switch (urls[0]){

		}

	}

	public void tick() {
	}

	public void getEffect(Player k){

		switch (effect)
		{
			case "./img/banana.png":
				k.setRunSpeed(1.6f);
				k.setJumpGravity(-1.0f);
				break;
		}

	}


	public boolean isVisibile() {
		return visibile;
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}

	public Player getCreator() {
		return creator;
	}

	public String getEffect() {
		return effect;
	}
}
