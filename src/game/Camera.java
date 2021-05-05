package game;

import game.display.Window;
import game.entities.GameObject;


public class Camera {
	/**
	 * xOffset and yOffset are used to offset the actual x and y positions to give the illusion of a moveable camera.
	 */
	private static float xOffset, yOffset;
	/**
	 * The game object on which the camera will be centered.
	 */
	private static GameObject target = null;
	
	public Camera() {
		Camera.xOffset = 0;
		Camera.yOffset = 0;
	}

	
	/**
	 * Called every frame, and is responsible for keeping the target centered.
	 */
	public void tick() {
		if(target != null) {
			Camera.xOffset = -(this.target.getX() + (this.target.getWidth() /2) - (Window.WIDTH/2));
			Camera.yOffset = -(this.target.getY() + (this.target.getHeight() /2) - (Window.HEIGHT/2));
		}
	}
	
	/**
	 * Adds a GameObject as the target of a camera.
	 * @param o The object on which the camera will center.
	 */
	public void addTarget(GameObject o ) {
		this.target = o;
	}
	
	/**
	 * Removes the current target.
	 */
	public void removeTarget() {
		this.target = null;
	}
	
	/**
	 * Moves the camera x by a given offset.
	 * @param offset The amount by which the xOffset should be incremented.
	 */
	public void moveCameraX(int offset) {
		Camera.xOffset += offset;
	}
	
	/**
	 * Moves the camera y by a given offset.
	 * @param offset The amount by which the yOffset should be incremented.
	 */
	public void moveCameraY(int offset) {
		Camera.yOffset += offset;
	}
	
	public static float getXOffset() {
		return xOffset;
	}
	
	public static float getYOffset() {
		return yOffset;
	}

	public static GameObject getTarget() {
		return target;
	}
}