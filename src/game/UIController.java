package game;

import game.entities.GameObject;
import game.graphics.LevelState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static game.Level.*;

public class UIController extends GameObject {

	private ArrayList<String> countdownUrls = new ArrayList<String>(Arrays.asList("5.png", "4.png","3.png","2.png","1.png","finish2.png","asdgasdg.png"));
	private int index=0;
	private int timer=0;
	private boolean startCountdown=false;
	private boolean endGameCelebration=false;
	UIElement announcer;

	public UIController(float x, float y, int width, int height) {
		super(x, y, 3, width, height);

		//Initialize center announcer UI Element
		announcer = new UIElement(x, y, 0, 0, "./img/5.png");
		announcer.setVisible(false);
		announcer.centerHorizontally();
		announcer.centerVertically();
		getGameObjects().add(announcer);

	}

	public void tick() {

		//START OF THE GAME COUNTDOWN SLIDESHOW
		if (getLevelState()==LevelState.Starting)
			startCountdown=true;

		if (getLevelState()==LevelState.Finished)
			endGameCelebration=true;

		if (startCountdown) {

			if (!announcer.isVisible())
				announcer.setVisible(true);

			if (timer < 50)
				timer++;

			else {

				try {
					announcer.setImg(ImageIO.read(new File("./img/".concat(countdownUrls.get(index)))));
				}
				catch (IOException exc) {
					//TODO: Handle exception.
				}

				index++;
				if (index == countdownUrls.size()-1) {
					setLevelState(LevelState.InProgress);
				}

				if (index == countdownUrls.size()) {
					startCountdown = false;
					//announcer.setVisible(false);
				}

				timer = 0;
			}
		}

		if (endGameCelebration)
		{
			try {
				announcer.setImg(ImageIO.read(new File("./img/winner.png")));
			}
			catch (IOException exc) {
				//TODO: Handle exception.
			}
			announcer.setY(y);
			announcer.setX(x+49);
		}



	}

	public void render(Graphics g, float f, float h) {

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
