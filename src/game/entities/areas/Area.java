package game.entities.areas;
import game.entities.GameObject;
import game.entities.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Area extends GameObject {
	private BufferedImage img;
	private boolean active = true;

	public Area(float x, float y, int width, int height, String url) {
		super(x, y, -2, width, height);

		try
		{
			//sets the width and height of the platform based on the provided image width and height
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

	}

	public void render(Graphics g, float f, float h) {

			//g.setColor(Color.magenta);
			//g.fillRect((int)(this.x + f),(int)(this.y + h),this.width,this.height);
			g.drawImage(img,(int)(this.x + f),(int)(this.y + h),null);
	}

	public boolean getInteraction(Player player){
		return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);
	}

}
