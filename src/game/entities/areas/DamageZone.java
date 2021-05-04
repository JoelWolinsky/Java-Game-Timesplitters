package game.entities.areas;

import game.entities.players.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static game.Level.getPlayers;

public class DamageZone extends AnimArea{
	private int timer=0;
	private int increment = 0;
	private boolean active = false;
	private int i = 0;
	private int startOffset;
	private int onDuration;
	private int offDuration;
	private BufferedImage img;
	private String notice;

	public DamageZone(float x, float y, int width, int height, int increment,int onDuration, int offDuration, int startOffset, String notice, String url) {
		super(x, y, width, height,url);
		this.increment=increment;
		this.startOffset = startOffset;
		this.onDuration=onDuration;
		this.offDuration=offDuration;
		this.notice=notice;

		try
		{
			img = ImageIO.read( new File("./img/".concat(notice)));
		}
		catch ( IOException exc )
		{
		}

	}

	/**
	 * Called every frame, this checks for players within its' damage zone, tells any player objects that collide with it to respawn at the last respawn point
	 */
	public void tick() {


		for (Player p : getPlayers())
		{
			if (getInteraction(p))
				if (getActive()) {
					p.respawn();
				}

		}

		if (i<startOffset)
			i++;
		else
		{
			if (increment!=0)
			{
				if (this.active == false)
					if (timer>offDuration) {
						this.active = true;
						timer = 0;
					}
				if (this.active ==true)
					if (timer>onDuration) {
						this.active = false;
						timer = 0;
					}

				timer+=increment;
			}
			else
			{
				this.active=true;
			}
		}
	}

	public void render(Graphics g, float xOffset, float yOffset) {

		if (this.active==false && timer >= (offDuration/1.5))
			g.drawImage(img, (int) (this.x + xOffset), (int) (this.y + yOffset), null);
		else if (active)
			super.render(g,xOffset,yOffset);

	}

	public boolean getActive(){
		return this.active;
	}
	public void setActive(boolean active){
		this.active = active;
	}
	public String getNotice(){
		return this.notice;
	}
}
