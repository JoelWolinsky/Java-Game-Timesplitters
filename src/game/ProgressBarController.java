package game;

import game.entities.GameObject;
import game.entities.players.Player;
import game.graphics.LevelState;

import java.awt.*;
import java.util.ArrayList;

import static game.Level.*;

public class ProgressBarController extends GameObject {

	private int offset= 20;
	private int totalNrBlocks;
	private ArrayList<Player> listOfAddedBlips = new ArrayList<>();

	public ProgressBarController(float x, float y, int width, int height, Level currentLevel, ArrayList<MapPart> mps) {
		super(x, y, 3, width, height);

		for (MapPart st : mps) {
			totalNrBlocks = totalNrBlocks + st.getNrBlocks();

		}
		for (MapPart st : mps) {
			currentLevel.addEntity(new UIElement(this.x + offset, this.y + 10, (int)((((float)((st.getNrBlocks()*426)*100)/(totalNrBlocks*426))/100)*600), 57, st.getUrl()));
			offset = offset + (int)((((float)((st.getNrBlocks()*426)*100)/(totalNrBlocks*426))/100)*600);
		}

		this.width = width;

	}

	public void tick() {

		if (getLevelState()== LevelState.Waiting || getLevelState()== LevelState.Starting)
		{
			for (Player p: getPlayers())
			{
				if (!listOfAddedBlips.contains(p)) {
					addToAddQueue(new Blip(this.x, this.y + 20, 20, 20, p, totalNrBlocks, "./img/head1.png"));
					listOfAddedBlips.add(p);
				}
			}
		}

	}

	public void render(Graphics g, float f, float h) {

	}


}
