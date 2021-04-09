package game;

import game.entities.GameObject;
import game.entities.players.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Blip extends GameObject {

	private BufferedImage img;
	private Player player;
	private int totalNrBlocks=0;

	public Blip(float x, float y, int width, int height, Player player, int totalNrBlocks, String url) {
		super(x, y, 3, width, height);
		this.player=player;
		this.totalNrBlocks=totalNrBlocks;
		try
		{
			img = ImageIO.read( new File(url));
			this.width = img.getWidth();
			this.height = img.getHeight();
		}
		catch ( IOException exc )
		{
			//TODO: Handle exception.
		}

	}

	public void tick() {

		if (player.moving())
			this.x=player.getX()/(0.71f*totalNrBlocks);

	}

	public void render(Graphics g, float f, float h) {

			g.drawImage(img,(int)(this.x+20),(int)(this.y),null);
	}


}
