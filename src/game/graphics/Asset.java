package game.graphics;

import java.util.HashMap;

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

