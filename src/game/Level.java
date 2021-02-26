package game;

import java.awt.*;
import java.util.LinkedList;
import game.entities.Platform;

public class Level extends Canvas {
	private LinkedList<GameObject> entities = new LinkedList<>();
	private LinkedList<Chunk> chunks = new LinkedList<>();
	private LinkedList<Platform> platforms = new LinkedList<>();

	public void tick() {
		for(Chunk c : chunks) {
			c.tick();
		}
		for(GameObject o : entities) {
			o.tick();
		}
	}
	
	public void render(Graphics g, float f, float h,float jeh, float buh) {
		//rendering order chunks->platforms->entities
		renderChunks(g,f,h,jeh,buh);
		renderPlatforms(g,f,h,jeh,buh);
		renderEntities(g, f, h);

	}

	public void renderEntities(Graphics g, float f, float h) {
		for(GameObject o : entities) {
			o.render(g, f, h);
		}
	}
	
	public void renderChunks(Graphics g, float f, float h,float jeh,float buh) {
		for(Chunk c : chunks) {
			//CAMERA RENDERING
			//if (c.getX()- 640<f*(-1) && f*(-1)<c.getX()+ 640 && c.getY()-380<h*(-1) && h*(-1)<c.getY()+ 740)
			//PLAYER POSITION RENDERING
			if (c.getX()- 640<jeh && jeh<c.getX()+ 800 && c.getY()-480<buh && buh<c.getY()+ 740)
			{c.render(g, f, h);}
		}
	}
	public void renderPlatforms(Graphics g, float f, float h,float jeh,float buh) {
		for(Platform p : platforms) {
			////CAMERA RENDERING
			//if (p.getX()-650<f*(-1) && f*(-1)<p.getX()+800 && p.getY()-380<h*(-1) && h*(-1)<p.getY()+ 740)
			//PLAYER POSITION RENDERING
			if (p.getX()-650<jeh && jeh<p.getX()+800 && p.getY()-480<buh && buh<p.getY()+ 740)
			{p.render(g, f, h);}
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

	public void addPlatform(Platform p) {
		platforms.add(p);
	}

	public void removePlatform(Platform p) {
		platforms.remove(p);
	}
	
}
