package game;

import java.awt.Graphics;
import java.util.LinkedList;

import game.entities.Platform;

public class Level {
	private LinkedList<GameObject> entities = new LinkedList<>();
	private LinkedList<Platform> platforms = new LinkedList<>();
	
	public void tick() {
		for(GameObject o : entities) {
			o.tick();
		}
	}
	
	public void render(Graphics g, float f, float h) {
		//Render the platforms first, so they are below the entities
		renderPlatforms(g, f, h);
		renderEntities(g, f, h);
	}
	
	public void renderEntities(Graphics g, float f, float h) {
		for(GameObject o : entities) {
			o.render(g, f, h);
		}
	}
	
	public void renderPlatforms(Graphics g, float f, float h) {
		for(Platform p : platforms) {
			p.render(g, f, h);
		}
	}
	
	public void addEntity(GameObject o) {
		entities.add(o);
	}
	public void removeEntity(GameObject o) {
		entities.remove(o);
	}
	public void addPlatform(Platform p) {
		platforms.add(p);
	}
	public void removePlatform(Platform p) {
		platforms.remove(p);
	}
	
}
