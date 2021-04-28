package game.entities.areas;
import game.GameState;
import game.entities.GameObject;
import game.entities.players.AIPlayer;
import game.entities.players.Player;
import game.graphics.AnimationStates;
import game.graphics.Image;
import game.graphics.LevelState;

import java.awt.*;
import java.util.ArrayList;

import static game.Level.*;
import static game.Game.*;
import static game.display.Window.*;

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
					pp.setCurrentAnimState(AnimationStates.IDLE);
				}

				setLevelState(LevelState.Finished);
				p.setCurrentAnimState(AnimationStates.SWAG);

			}

		}

	}

}
