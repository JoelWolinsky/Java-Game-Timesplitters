package game;

import game.entities.AIPlayer;
import game.entities.GameObject;
import game.entities.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BackgroundStates {

	private ArrayList<String> states = new ArrayList<String>();

	public BackgroundStates(String...urls) {

		//load up the images inside of the private arraylist
		for (String x : urls)
			states.add(x);

	}

	//get the required state specified by the controller
	//is also able to wrap around
	public String getState(int index) {

		if (index<0)
			return states.get(getStateSize()-1);
		else if (index==getStateSize())
				return states.get(0);
			else
				return states.get(index);
	}

	public int getStateSize(){
		return states.size();
	}
}
