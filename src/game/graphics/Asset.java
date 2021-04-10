package game.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Asset {

	private String name;
	private HashMap<AnimationStates,Animation> animations;

	public Asset(String name, HashMap<AnimationStates,Animation> animations){

		this.name=name;
		this.animations=animations;
	}

	public String getName() {
		return name;
	}

	public HashMap<AnimationStates, Animation> getAnimations() {
		return animations;
	}
}

