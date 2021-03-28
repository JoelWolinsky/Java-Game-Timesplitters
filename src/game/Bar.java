package game;

import game.entities.GameObject;
import game.entities.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bar extends GameObject {
	private BufferedImage img;
	private boolean visible = true;
	private Player player;

	public Bar(float x, float y, int width, int height, Player player, String url) {
		super(x, y, 3, width, height);
		this.player=player;

		System.out.println(width);
		try
		{
			img = ImageIO.read( new File(url));
			this.height = img.getHeight();
			img = img.getSubimage(0,0,width,this.height);
			this.width = img.getWidth();
		}
		catch ( IOException exc )
		{
			//TODO: Handle exception.
		}

	}

	public void tick() {



	}

	public void render(Graphics g, float f, float h) {

			//g.setColor(Color.magenta);
			//g.fillRect((int)(this.x + f),(int)(this.y + h),this.width,this.height);
			g.drawImage(img,(int)(this.x),(int)(this.y),null);
	}


}
