package game;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import game.entities.Platform;
import game.entities.Player;
import game.entities.PlayerMP;
import game.network.packets.Packet01Disconnect;

public class Level {
	private LinkedList<GameObject> entities = new LinkedList<GameObject>();
	private LinkedList<Platform> platforms = new LinkedList<Platform>();
	
	
	public synchronized LinkedList<GameObject> getGameObjects(){
		return this.entities;
	}
	
	public synchronized LinkedList<Platform> getPlatforms(){
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

	public void removePlayerMP(String username) {
		try {
			int index = 0;
			for(GameObject e : getGameObjects()) {
				if(e instanceof PlayerMP && ((PlayerMP)e).getUsername().equals(username)) {
					break;
				}
				index++;
			}
			this.getGameObjects().remove(index);
		} catch (Exception e) {
			System.out.println("Exception in removePlayerMP. Player " + username);
			e.printStackTrace();
		}
	}
	
	private int getPlayerMPIndex(String username) {
		try {
			int index = 0;
			for (GameObject e : getGameObjects()) {
				if(e instanceof PlayerMP && ((PlayerMP)e).getUsername().equals(username)) {
					break;
				}
				index++;
			}
			return index;
		} catch (Exception e) {
			System.out.println("Exception in getPlayerMPIndex. Player " + username);
			e.printStackTrace();
			return -1;
		}
	}
	
	public void movePlayer(String username, float x, float y) {
		try {
			int index = getPlayerMPIndex(username);
			this.getGameObjects().get(index).x = x;
			this.getGameObjects().get(index).y = y;
		} catch (Exception e) {
			System.out.println("Exception in movePlayer when moving player " + username);
			e.printStackTrace();
		}
	}
	
}
