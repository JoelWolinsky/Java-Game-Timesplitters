package game;

import game.entities.GameObject;
import game.entities.players.Player;

import java.awt.*;

public class BarController extends GameObject {
	private boolean visible = true;
	private Player player;
	private int offset= 20;
	private int totalNrBlocks;

	public BarController(float x, float y, int width, int height, Player player,Level currentLevel, MapPart...urls) {
		super(x, y, 3, width, height);
		this.player=player;

		for (MapPart st : urls) {
			totalNrBlocks = totalNrBlocks + st.getNrBlocks();

		}
		for (MapPart st : urls) {
			currentLevel.addEntity(new UIElement(this.x + offset, this.y + 10, (int)((((float)((st.getNrBlocks()*426)*100)/(totalNrBlocks*426))/100)*600), 57, st.getUrl()));
			offset = offset + (int)((((float)((st.getNrBlocks()*426)*100)/(totalNrBlocks*426))/100)*600);
		}

		currentLevel.addEntity(new Blip(this.x, this.y+20, 20,20,player,totalNrBlocks,"./img/head1.png"));
		this.width = width;

	}

	public void tick() {

	}

	public void render(Graphics g, float f, float h) {

	}


}
