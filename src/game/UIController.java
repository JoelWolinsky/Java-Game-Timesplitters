package game;

import game.display.Window;
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
	UIElement announcer,announcerMessage;
	private double time1=0,time2;

	public UIController(float x, float y, int width, int height) {
		super(x, y, 4, width, height);

		//Initialize center announcer UI Element
		announcer = new UIElement(x, y, 0, 0, "./img/5.png");
		announcer.setVisible(false);
		announcer.centerHorizontally();
		announcer.centerVertically();

		announcerMessage = new UIElement(x, y-200, 0, 0, "./img/waiting.png");
		announcerMessage.centerHorizontally();
		announcerMessage.centerVertically();
		announcerMessage.setVisible(false);
		announcerMessage.setY(announcerMessage.getY()-165);

		getGameObjects().add(announcer);
		getGameObjects().add(announcerMessage);

	}

	public void tick() {


		if (announcer.isVisible())
		{
			announcer.centerHorizontally();
			announcer.centerVertically();
		}

		//START OF THE GAME COUNTDOWN SLIDESHOW
		if (getLevelState()==LevelState.Starting) {
			startCountdown = true;
		}

		if (getLevelState()==LevelState.Finished)
			endGameCelebration=true;

		if (getLevelState()==LevelState.Waiting)
		{

			if (!announcer.isVisible()) {
				announcer.setVisible(true);
				announcerMessage.setVisible(true);
			}

			try {
				announcer.setImg(ImageIO.read(new File("./img/loading.png")));
			}
			catch (IOException exc) {
				//TODO: Handle exception.
			}

		}


		if (startCountdown) {

			if (!announcer.isVisible()) {
				announcer.setVisible(true);
				announcerMessage.setVisible(true);
				time();
			}

			try {
				announcerMessage.setImg(ImageIO.read(new File("./img/starting.png")));
			}
			catch (IOException exc) {
				//TODO: Handle exception.
			}

			if (timetime() < 1000)
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
					announcerMessage.setVisible(false);
				}

				if (index == countdownUrls.size()) {
					startCountdown = false;
					announcer.setVisible(false);
				}

				time();
			}
		}

		if (endGameCelebration)
		{
			if (!announcer.isVisible())
				announcer.setVisible(true);

			try {
				announcer.setImg(ImageIO.read(new File("./img/trophy.png")));
			}
			catch (IOException exc) {
				//TODO: Handle exception.
			}
			announcer.setY(y);
			announcer.setX(x+((Window.WIDTH/6)/2) - (announcer.getImg().getWidth()/2));
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

	public void time()
	{
		time1 = System.currentTimeMillis();
	}

	public double timetime()
	{
		time2 = System.currentTimeMillis();

		return time2-time1;
	}

}
