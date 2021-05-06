package game.graphics;

import java.util.HashMap;

/**
 * Compound class that represents an asset in the game
 */

public class Asset {

	private String name;
	private HashMap<AnimationStates,Animation> animations;

	/**
	 * @param name the designated name of the asset eg. ghost
	 * @param animations a hasmap of animations corresponding to different animation states
	 */

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

