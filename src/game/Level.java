package game;

import java.awt.Graphics;
import java.util.LinkedList;

import game.entities.MovingPlatform;
import game.entities.Platform;

public class Level {
	private LinkedList<GameObject> entities = new LinkedList<>();
	private LinkedList<Chunk> chunks = new LinkedList<>();
	
	public void tick() {
		for(GameObject o : entities) {
			o.tick();
		}
		for(Chunk c : chunks) {
			c.tick();
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
		for(Chunk c : chunks) {
			c.render(g, f, h);
		}
	}
	
	public void addEntity(GameObject o) {
		entities.add(o);
	}
	public void removeEntity(GameObject o) {
		entities.remove(o);
	}
	public void addChunk(Chunk c) {
		chunks.add(c);
	}
	public void removeChunk(Chunk c) {
		chunks.remove(c);
	}
	
}
