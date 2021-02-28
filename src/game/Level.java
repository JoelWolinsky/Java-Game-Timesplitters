package game;

import java.awt.*;
import java.util.LinkedList;
import game.entities.Platform;
import game.entities.Player;

public class Level extends Canvas {
	private LinkedList<GameObject> entities = new LinkedList<>();
	private LinkedList<Chunk> chunks = new LinkedList<>();
	private LinkedList<Platform> platforms = new LinkedList<>();
	private LinkedList<RespawnPoint> respawnPoints = new LinkedList<>();
	private LinkedList<AreaDmg> areaDmgs = new LinkedList<>();

	public void tick() {
		for(Chunk c : chunks) {
			c.tick();
		}
		for(GameObject o : entities) {
			o.tick();
		}
		for(AreaDmg ad: areaDmgs) {
			ad.tick();
		}
		for (Platform p:platforms)
			p.tick();

	}
	
	public void render(Graphics g, float f, float h, Player player) {

		renderChunks(g,f,h,player);
		renderPlatforms(g,f,h,player);
		renderRespawnPoints(g,f,h,player);
		renderAreaDmgs(g,f,h,player);
		renderEntities(g, f, h);

	}

	public void renderEntities(Graphics g, float f, float h) {
		for(GameObject o : entities) {
			o.render(g, f, h);
		}
	}
	
	public void renderChunks(Graphics g, float f, float h,Player player) {
		for(Chunk c : chunks) {
			//CAMERA PROXIMITY RENDERING
			//if (c.getX()- 640<f*(-1) && f*(-1)<c.getX()+ 640 && c.getY()-380<h*(-1) && h*(-1)<c.getY()+ 740)
			//PLAYER POSITION PROXIMITY RENDERING
			if (c.getX()- 640<player.getX() && player.getX()<c.getX()+ 800 && c.getY()-480<player.getY() && player.getY()<c.getY()+ 740)
			{c.render(g, f, h);}

		}
	}
	public void renderPlatforms(Graphics g, float f, float h,Player player) {
		for(Platform p : platforms) {
			////CAMERA PROXIMITY RENDERING
			//if (p.getX()-650<f*(-1) && f*(-1)<p.getX()+800 && p.getY()-380<h*(-1) && h*(-1)<p.getY()+ 740)
			//PLAYER POSITION PROXIMITY RENDERING
			if (p.getX()-650<player.getX() && player.getX()<p.getX()+800 && p.getY()-480<player.getY() && player.getY()<p.getY()+ 740)
			{p.render(g, f, h);}
		}
	}

	public void renderRespawnPoints(Graphics g, float f, float h,Player player) {
		for(RespawnPoint o : respawnPoints) {

			if (o.getReached()==false) //delete this if you want to allow players to activate previously reached respawn points
			if ((int)o.getX()<(int)player.getX()+player.getWidth() && (int)player.getX()<o.getX()+o.getWidth() && (int)o.getY()-100<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)o.getY()+o.getHeight())
			{
				player.setRespawnX((int) o.getX());
				player.setRespawnY((int) o.getY());
				o.setReached(true);
				o.setCurrentActive(true);
				for (RespawnPoint oo: respawnPoints)
					if (oo != o)
						oo.setCurrentActive(false);
			}

			if (o.getCurrentActive()==true)
				o.render(g,f,h);
		}

	}

	public void renderAreaDmgs(Graphics g, float f, float h,Player player) {
		for(AreaDmg o : areaDmgs) {
			if(o.getActive()==true)
			{
				//only render if is active
				o.render(g, f, h);
				//if player comes in contact with the area he gets respawned
				if ((int)o.getX()<(int)player.getX()+player.getWidth() && (int)player.getX()<o.getX()+o.getWidth() && (int)o.getY()<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)o.getY()+o.getHeight())
					player.respawn();
			}



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

	public void addRespawnPoint(RespawnPoint rp) {
		respawnPoints.add(rp);
	}

	public void removeRespawnPoint(RespawnPoint rp) {
		respawnPoints.remove(rp);
	}

	public void addAreaDmg(AreaDmg ad) {
		areaDmgs.add(ad);
	}

	public void removeAreaDmg(AreaDmg ad) {
		areaDmgs.remove(ad);
	}
	
}
