package game;

import game.display.Window;
import game.entities.GameObject;


public class Camera {
	/**
	 * xOffset and yOffset are used to offset the actual x and y positions to give the illusion of a moveable camera.
	 */
	private float xOffset, yOffset;
	/**
	 * The game object on which the camera will be centered.
	 */
	private GameObject target = null;
	
	public Camera() {
		this.xOffset = 0;
		this.yOffset = 0;
	}

	
	/**
	 * Called every frame, and is responsible for keeping the target centered.
	 */
	public void tick() {
		if(target != null) {
			this.xOffset = -(this.target.getX() + (this.target.getWidth() /2) - (Window.WIDTH/2));
			this.yOffset = -(this.target.getY() + (this.target.getHeight() /2) - (Window.HEIGHT/2));
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
		this.xOffset += offset;
	}
	
	/**
	 * Moves the camera y by a given offset.
	 * @param offset The amount by which the yOffset should be incremented.
	 */
	public void moveCameraY(int offset) {
		this.yOffset += offset;
	}
	
	/**
	 * @return The current camera xOffset.
	 */
	public float getXOffset() {
		return this.xOffset;
	}
	
	/**
	 * @return The current camera yOffset.
	 */
	public float getYOffset() {
		return this.yOffset;
	}

	public GameObject getTarget() {
		return target;
	}
}