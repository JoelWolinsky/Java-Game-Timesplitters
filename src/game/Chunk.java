package game;

import game.entities.MovingPlatform;
import game.entities.Platform;

import java.awt.*;
import java.util.LinkedList;

public class Chunk {
	/**
	 * A list of all non-platform objects loaded in a given chunk
	 */
	private LinkedList<GameObject> entities = new LinkedList<>();
	/**
	 * A list of all platforms loaded in a given chunk
	 */
	private LinkedList<Platform> platforms = new LinkedList<>();
	/**
	 * A list of all objects loaded in a given chunk. Both platforms and entities are a subset of this list
	 */
	private LinkedList<GameObject> objects = new LinkedList<>();
	
	/**
	 * Called every frame. Calls the tick functions of any moving platforms or entities in a given chunk.
	 */
	public void tick() {
		for(GameObject o : entities) {
			o.tick();
		}
		for(Platform p : platforms) {
			if(p instanceof MovingPlatform) {
				p.tick();
			}
		}
	}
	
	/**
	 * Renders all objects, starting with platforms and followed by entities
	 * @param g The graphics object onto which everything will be rendered
	 * @param xOffset The xOffset of the camera
	 * @param yOffset The yOffset of the camera
	 */
	public void render(Graphics g, float xOffset, float yOffset) {
		//Render the platforms first, so they are below the entities
		renderPlatforms(g, xOffset, yOffset);
		renderEntities(g, xOffset, yOffset);
	}
	
	/**
	 * Renders the GameObjects in the entities list
	 * @param g The graphics object onto which everything will be rendered
	 * @param xOffset The xOffset of the camera
	 * @param yOffset the yOffset of the camera
	 */
	public void renderEntities(Graphics g, float xOffset, float yOffset) {
		for(GameObject o : entities) {
			o.render(g, xOffset, yOffset);
		}
	}
	
	/**
	 * Renders the Platforms in the entities list
	 * @param g The graphics object onto which everything will be rendered
	 * @param xOffset The xOffset of the camera
	 * @param yOffset the yOffset of the camera
	 */
	public void renderPlatforms(Graphics g, float xOffset, float yOffset) {
		for(Platform p : platforms) {
			p.render(g, xOffset, yOffset);
		}
	}
	
	/**
	 * Adds an entitiy to the entities list
	 * @param o The object which is to be added to the entities list
	 */
	public void addEntity(GameObject o) {
		entities.add(o);
	}
	/**
	 * Removes a given entitiy from the entities list
	 * @param o The object which is to be removed from the entities list
	 */
	public void removeEntity(GameObject o) {
		entities.remove(o);
	}
	
	/**
	 * Adds a platform to the platforms list
	 * @param p The platform which is to be added to the platforms list
	 */
	public void addPlatform(Platform p) {
		platforms.add(p);
	}
	/**
	 * Removes a given platform from the platforms list
	 * @param p The platform which is to be removed from the platforms list
	 */
	public void removePlatform(Platform p) {
		platforms.remove(p);
	}
	
}
