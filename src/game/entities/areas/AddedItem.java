package game.entities.areas;
import game.Effect;
import game.entities.players.Player;

import static game.Level.getPlayers;

/**
 * Class for any objects that have been picked up by the player
 */
public class AddedItem extends AnimArea {

	private boolean visibile=true;
	private Player creator;
	private String effect;

	public AddedItem(float x, float y, int width, int height, Player creator, String url) {
		super(x, y, width, height,url);
		this.creator=creator;

		effect = url;
	}

	/**
	 * Called every frame, applies item effects to the player
	 */
	public void tick() {

		for (Player p: getPlayers())
		{
			if (this.isVisibile())
				if (this.getInteraction(p))
					if (p!=this.getCreator()){
						this.getEffect(p);
						this.setVisibile(false);
						p.addEffect(new Effect(this.getEffectName(),250));
					}
		}

	}

	public void getEffect(Player k){

		switch (effect)
		{
			case "banana":
				k.bounce(5);
				break;
			case "fart":
				k.setRunSpeed(2.0f);
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

	public String getEffectName() {
		return effect;
	}
}
