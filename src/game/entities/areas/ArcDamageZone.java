package game.entities.areas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ArcDamageZone extends AnimArea{
	private int timer=0;
	private boolean active = true;
	private int i = 0;
	private int startOffset;
	private int onDuration;
	private int offDuration;
	private BufferedImage img;
	private float originalY;
	private float originalX;
	private float ySpeed,xSpeed;
	private int goalY,goalX;


	public ArcDamageZone(float x, float y, int width, int height,int goalX, int goalY,float speedX,float speedY, int startOffset, String...urls) {
		super(x, y, width, height, urls);
		this.startOffset = startOffset;
		this.onDuration=onDuration;
		this.offDuration=offDuration;
		this.originalX=x;
		this.originalY=y;
		this.goalX=goalX;
		this.goalY=goalY;
		this.xSpeed=speedX;
		this.ySpeed=speedY;


	}

	public void tick() {

		if (i<startOffset)
			i++;
		else
		{
				System.out.println(y);

			this.x = this.x + xSpeed;
			this.y = this.y - ySpeed;

			if (this.y<=goalY || this.y>=originalY)
				ySpeed=ySpeed*(-1);

			if (this.x>=goalX || this.x<=originalX)
				xSpeed=xSpeed*(-1);
		}
	}

	@Override
	public void render(Graphics g, float f, float h) {
		super.render(g, f, h);
	}

	public boolean getActive(){
		return this.active;
	}
	public void setActive(boolean active){
		this.active = active;
	}
}
