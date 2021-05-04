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

	public EventDamageZone(float x, float y, int width, int height, int noticeDelay, int onDuration ,String notice, int noticeX, int noticeY,boolean random, String url) {
		super(x, y, width, height,url);

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
		}

	}

	/**
	 * Called every frame, this function checks for players within its event area, and kills the player if the object is active.
	 * It also controls the timing of the activity.
	 */
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

	public void render(Graphics g, float xOffset, float yOffset) {
		
		if (random) {
			if (go) {
				if (triggered)
					if (1 < timer && timer < noticeDelay)
						g.drawImage(img, (int) (this.x + noticeX + xOffset), (int) (this.y + noticeY + yOffset), null);
				if (active)
					super.render(g, xOffset, yOffset);
			}
		}
		else
			{
				if (triggered)
					if (1 < timer && timer < noticeDelay)
						g.drawImage(img, (int) (this.x+ noticeX + xOffset), (int) (this.y+ noticeY + yOffset), null);
				if (active)
					super.render(g,xOffset,yOffset);
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
