package game;

import game.entities.GameObject;
import game.entities.players.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UIElement extends GameObject {

	private BufferedImage img;
	private boolean visible = true;

	public UIElement(float x, float y, int width, int height, String url) {
		super(x, y, 3, width, height);

		try
		{
			img = ImageIO.read( new File(url));
			this.height = img.getHeight();
			if (width!=0 && height!=0)
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

			if (visible)
			g.drawImage(img,(int)(this.x),(int)(this.y),null);
	}


	public void centerHorizontally()
	{
		this.x=(640/2)-(this.width/2);
	}

	public void centerVertically()
	{
		this.y=(480/2)-(this.height/2);
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}
}
