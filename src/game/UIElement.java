package game;

import game.entities.GameObject;
import game.entities.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UIElement extends GameObject {
	private BufferedImage img;
	private boolean visible = true;
	private Player player;

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

			//g.setColor(Color.magenta);
			//g.fillRect((int)(this.x + f),(int)(this.y + h),this.width,this.height);
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
}
