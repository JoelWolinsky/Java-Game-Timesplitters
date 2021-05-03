package game.entities.areas;
import game.Blip;
import game.UIElement;
import game.entities.players.Player;
import game.graphics.AnimationStates;
import game.graphics.LevelState;


import static game.Level.*;
import static game.ProgressBarController.getAllBlips;
import static game.ProgressBarController.getProgressBarElements;

public class GameEndingObject extends AnimArea {
	


	public GameEndingObject(float x, float y, int width, int height,String url) {
		super(x, y, width, height, url);

	}

	public void tick() {

		for (Player p : getPlayers())
		{
			if (this.getInteraction(p))
			{
				for (Player pp : getPlayers()) {
					pp.setVelX(0);
					pp.setVelY(0);
					pp.setCanMove(false);
					pp.setCurrentAnimState(AnimationStates.IDLE);
				}

				for (Blip b: getAllBlips())
					b.setVisible(false);
				for (UIElement uiElement: getProgressBarElements())
					uiElement.setVisible(false);

				setLevelState(LevelState.Finished);
				p.setCurrentAnimState(AnimationStates.SWAG);
				p.winner = true;
				

			}

		}

	}

}
