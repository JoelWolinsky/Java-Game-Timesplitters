package game;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RespawnPoint extends GameObject {
	private String url;
	private BufferedImage img;

	public RespawnPoint(float x, float y, int width, int height, String url) {
		super(x, y, -2, width, height);
		this.url=url;
	}

	public void tick() {
	}
	
	public void render(Graphics g, float f, float h) {

			g.setColor(Color.magenta);
			g.fillRect((int)(this.x + f),(int)(this.y + h),width,height);

			g.drawImage(img,(int)(this.x + f),(int)(this.y + h),null);

	}

	public void activate()
	{
		try
		{
			img = ImageIO.read( new File("./img/".concat(url)));
		}
		catch ( IOException exc )
		{
			//TODO: Handle exception.
		}

	}
}
