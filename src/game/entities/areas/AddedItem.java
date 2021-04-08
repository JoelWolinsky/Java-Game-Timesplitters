package game.entities.areas;
import game.Effect;
import game.entities.Player;

import static game.Level.getPlayers;

public class AddedItem extends AnimArea {

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

		for (Player p: getPlayers())
		{
			if (this.isVisibile())
				if (this.getInteraction(p))
					if (p!=this.getCreator()) {
						this.getEffect(p);
						this.setVisibile(false);
						p.addEffect(new Effect(this.getEffect(),500));
					}
		}

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
