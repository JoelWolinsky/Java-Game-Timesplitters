package game;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import game.entities.Platform;

public class Level {
	private LinkedList<GameObject> entities = new LinkedList<>();
	private LinkedList<Platform> platforms = new LinkedList<>();
	
	
	public synchronized List<GameObject>getGameObjects(){
		return this.entities;
	}
	
	public synchronized List<Platform>getPlatforms(){
		return this.platforms;
	}
	public void tick() {
		for(GameObject o : getGameObjects()) {
			o.tick();
		}
	}
	
	public void render(Graphics g, int xOffset, int yOffset) {
		//Render the platforms first, so they are below the getGameObjects()
		renderPlatforms(g, xOffset, yOffset);
		renderEntitites(g, xOffset, yOffset);
	}
	
	public void renderEntitites(Graphics g, int xOffset, int yOffset) {
		for(GameObject o : getGameObjects()) {
			o.render(g, xOffset, yOffset);
		}
	}
	
	public void renderPlatforms(Graphics g, int xOffset, int yOffset) {
		for(Platform p : getPlatforms()) {
			p.render(g, xOffset, yOffset);
		}
	}
	
	public void addEntity(GameObject o) {
		this.getGameObjects().add(o);
	}
	public void removeEntity(GameObject o) {
		this.getGameObjects().remove(o);
	}
	public void addPlatform(Platform p) {
		this.getPlatforms().add(p);
	}
	public void removePlatform(Platform p) {
		this.getPlatforms().remove(p);
	}
	
}
