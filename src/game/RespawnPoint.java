package game;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RespawnPoint extends GameObject {
	private BufferedImage img;
	private boolean reached = false;
	private boolean currentActive = false;

	public RespawnPoint(float x, float y, int width, int height, String url) {
		super(x, y, -2, width, height);

		try
		{
			//sets the width and height of the platform based on the provided image width and height
			img = ImageIO.read( new File("./img/".concat(url)));
			this.width = img.getWidth();
			this.height = img.getHeight();
		}
		catch ( IOException exc )
		{
			//TODO: Handle exception.
		}


	}

	public void tick() {
	}
	
	public void render(Graphics g, float f, float h) {

			g.setColor(Color.magenta);
			g.fillRect((int)(this.x + f),(int)(this.y + h),width,height);
			g.drawImage(img,(int)(this.x + f),(int)(this.y + h),null);

	}

	public void setCurrentActive(boolean currentActive)
	{
		this.currentActive = currentActive;
	}

	public boolean getCurrentActive()
	{
		return this.currentActive;
	}

	public void setReached(boolean reached)
	{
		this.reached = reached;
	}

	public boolean getReached()
	{
		return this.reached;
	}

}
