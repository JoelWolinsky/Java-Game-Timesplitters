package game.entities.areas;
import game.entities.GameObject;
import game.entities.players.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Area extends GameObject {
	private BufferedImage img;

	public Area(float x, float y, int width, int height, BufferedImage bi) {
		super(x, y, 0, width, height);

		img = bi;
		if (img!=null)
		{
			this.width = img.getWidth();
			this.height = img.getHeight();
		}
	}

	public Area(float x, float y, int width, int height) {
		super(x, y, 0, width, height);
	}

	public void tick() {

	}

	public void render(Graphics g, float xOffset, float yOffset) {

			//g.setColor(Color.magenta);
			//g.fillRect((int)(this.x + f),(int)(this.y + h),this.width,this.height);
			g.drawImage(img,(int)(this.x + xOffset),(int)(this.y + yOffset),null);
	}

	public boolean getInteraction(GameObject player){
		if (player instanceof Player)
			if (((Player) player).isGhostMode())
				return false;

		return ((int)this.x < (int)player.getX()+player.getWidth() &&
				(int)player.getX() < this.x+this.width && 
				(int)this.y < (int)player.getY()+player.getHeight() && 
				(int)player.getY() < (int)this.y+this.height);
	}


}
