package game;

import game.entities.GameObject;
import game.entities.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class UIController extends GameObject {
	private BufferedImage img;
	private boolean visible = true;
	private LinkedList<String> s;
	private int index=0;
	private Level level;
	private int timer=0;
	private boolean startCountdown=true;
	UIElement uie;

	public UIController(float x, float y, int width, int height, Level level, LinkedList<String> b,String url) {
		super(x, y, 3, width, height);
		this.s = (LinkedList)b.clone();
		this.level=level;


		System.out.println(width);

		uie = new UIElement(x, y, 0, 0, "./img/5.png");
		level.addEntity(uie);
		uie.centerHorizontally();
		uie.centerVertically();

	}

	public void tick() {

		if (startCountdown) {

			if (timer < 50)
				timer++;
			else {

				if (startCountdown)
				{
					try {
						uie.setImg(ImageIO.read(new File("./img/".concat(s.get(index)))));
					} catch (IOException exc) {
						//TODO: Handle exception.
					}
				}

				index++;
				if (index == s.size()-1) {
					level.setGameStarted(true);
				}

				if (index == s.size()) {
					startCountdown = false;
					uie.setVisible(false);
				}

				timer = 0;
			}
		}



	}

	public void render(Graphics g, float f, float h) {

			//g.setColor(Color.magenta);
			//g.fillRect((int)(this.x),(int)(this.y),this.width,this.height);
	}


	public void centerHorizontally()
	{
		this.x=(640/2)-(this.width/2);
	}

	public void centerVertically()
	{
		this.y=(480/2)-(this.height/2);
	}

}
