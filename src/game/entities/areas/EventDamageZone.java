package game.entities.areas;
import game.entities.players.Player;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static game.Level.getPlayers;
import static game.Utility.getRandomIntInRangeSeeded;

/*
This class extends AnimArea instead of DamageZone to avoid some rendering issues due to the fact that we use this class to
render the notice and then we use super to render the animation in the parent. Extending DamageZone will make the
invisible enemy anim sit on top of the notice even if it isn't rendering at all and make the notice never show.
Plus we don't even need the extra customization from DamageZone since this is different mechanically speaking.
 */
public class EventDamageZone extends AnimArea{
	private int timer=0;
	private boolean active = false;
	private Area a;
	private boolean triggered;
	private BufferedImage img;
	private int noticeDelay,onDuration;
	private LinkedList<Area> areas = new LinkedList<>();
	private int noticeX,noticeY;
	private int cooldown = 0;
	private boolean go = false;
	private boolean random = false;

	public EventDamageZone(float x, float y, int width, int height, int noticeDelay, int onDuration ,String notice, int noticeX, int noticeY,boolean random, String...urls) {
		super(x, y, width, height,urls);

		this.random=random;
		this.noticeDelay=noticeDelay;
		this.onDuration=onDuration;
		this.noticeX=noticeX;
		this.noticeY=noticeY;

		try
		{
			img = ImageIO.read( new File("./img/".concat(notice)));
		}
		catch ( IOException exc )
		{
			//TODO: Handle exception.
		}

	}

	public void tick() {


		for (Player p: getPlayers())
		{
			for (Area a: this.getEventArea())
				if (a.getInteraction(p))
					this.setTriggered(true);


			if (this.getActive())
				if (this.getInteraction(p))
					p.respawn();
		}



		if (triggered)
		{

			if (timer==1 && random)
				if (getRandomIntInRangeSeeded(0,3)>0)
					go=false;
				else
					go=true;

			if (timer>noticeDelay)
			{
				if (random) {
					if (go)
						this.active = true;
				}
				else
					this.active=true;
			}

			if (timer >onDuration) {
				triggered = false;
				this.active = false;
				timer=0;
			}
			timer++;
		}
	}

	public void render(Graphics g, float f, float h) {
		
		if (random) {
			if (go) {
				if (triggered)
					if (1 < timer && timer < noticeDelay)
						g.drawImage(img, (int) (this.x + noticeX + f), (int) (this.y + noticeY + h), null);
				if (active)
					super.render(g, f, h);
			}
		}
		else
			{
				if (triggered)
					if (1 < timer && timer < noticeDelay)
						g.drawImage(img, (int) (this.x+ noticeX + f), (int) (this.y+ noticeY + h), null);
				if (active)
					super.render(g,f,h);
			}
	}

	public boolean getActive(){
		return this.active;
	}
	public void setActive(boolean active){
		this.active = active;
	}
	public LinkedList<Area> getEventArea(){return this.areas;}
	public void setTriggered(boolean triggered){this.triggered=triggered;}
	public void addArea(Area area){ areas.add(area);}

	public boolean isTriggered() {
		return triggered;
	}

}
