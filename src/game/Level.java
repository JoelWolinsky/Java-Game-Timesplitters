package game;

import java.awt.Graphics;
import java.util.LinkedList;

import game.entities.MovingPlatform;
import game.entities.Platform;

public class Level {
	/**
	 * A list of all chunks in a given level
	 */
	private LinkedList<Chunk> chunks = new LinkedList<>();
	
	/**
	 * Called every frame. Calls the tick function of all chunks in a level
	 */
	public void tick() {
		for(Chunk c : chunks) {
			c.tick();
		}
	}
	
	/**
	 * Renders all chunks
	 * @param g The graphics object onto which everything will be rendered
	 * @param xOffset The xOffset of the camera
	 * @param yOffset The yOffset of the camera
	 */
	public void render(Graphics g, float xOffset, float yOffset) {
		for(Chunk c : chunks) {
			c.render(g, xOffset, yOffset);
		}
	}
	
	/**
	 * Adds a chunk to the chunks list
	 * @param c The chunk which is to be added to the chunks list
	 */
	public void addChunk(Chunk c) {
		chunks.add(c);
	}
	/**
	 * Removes a given chunk from the chunks list
	 * @param c The chunk which is to be removed from the chunks list
	 */
	public void removeChunk(Chunk c) {
		chunks.remove(c);
	}
	
	public Chunk firstChunk() {
		return chunks.getFirst();
	}
	
}
