package game;

import game.entities.GameObject;
import game.entities.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Effect  {

	private int timer;
	private String name;

	public Effect(String name, int timer) {

		this.name=name;
		this.timer=timer;
	}


	public int getTimer() {
		return timer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public void decrement()
	{
		timer--;
	}
}
