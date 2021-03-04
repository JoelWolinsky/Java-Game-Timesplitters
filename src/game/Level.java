package game;

import java.awt.*;
import java.util.LinkedList;

import game.entities.*;

public class Level extends Canvas {
	private LinkedList<GameObject> entities = new LinkedList<>();
	private LinkedList<Chunk> chunks = new LinkedList<>();
	private LinkedList<Platform> platforms = new LinkedList<>();
	private LinkedList<RespawnPoint> respawnPoints = new LinkedList<>();
	private LinkedList<Area> areaDmgs = new LinkedList<>();
	private LinkedList<CollisionlessAnimObject> collisionlessAnimObjects = new LinkedList<>();

	public void tick() {
		for(Chunk c : chunks) {
			c.tick();
		}
		for(GameObject o : entities) {
			o.tick();
		}
		for(Area ad: areaDmgs) {
			ad.tick();
			if (ad instanceof Projectile)
				ad.tick();
		}
		for (Platform p:platforms)
		{

			if (p instanceof TimerPlatform)
				p.tick();
			if (p instanceof CrushingPlatform)
				p.tick();
			if (p instanceof MovingPlatform)
				p.tick();

		}

	}
	
	public void render(Graphics g, float f, float h, Player player) {

		for (GameObject k: entities)
		{
			if (k instanceof Player)
			{
				renderChunks(g,f,h,(Player)k);
				renderPlatforms(g,f,h,(Player)k);
				renderRespawnPoints(g,f,h,(Player)k);
				renderCollisionlessAnimObject(g,f,h,(Player)k);
				renderEntities(g, f, h);
			}

			for(Area o : areaDmgs) {

				if (o.getActive()==true)
				{

					if (o.getInteraction((Player)k)) {
						if (o.getAreaScope().equals("Dmg"))
							((Player)k).respawn();
						//if (o.getAreaScope().equals("Slow"))

					}
				}
			}

		}

		renderAreaDmgs(g,f,h);

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
			//if (c.getX()- 640<player.getX() && player.getX()<c.getX()+ 800 && c.getY()-480<player.getY() && player.getY()<c.getY()+ 740)
			{c.render(g, f, h);}

		}
	}
	public void renderCollisionlessAnimObject(Graphics g, float f, float h,Player player) {
		for(CollisionlessAnimObject c : collisionlessAnimObjects) {
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
			//if (p.getX()-650<player.getX() && player.getX()<p.getX()+800 && p.getY()-480<player.getY() && player.getY()<p.getY()+ 740)
			{
				if (p instanceof TimerPlatform) {
					if (((TimerPlatform) p).getActive() == true) {
						p.render(g, f, h);
					}
				}
				else if (p instanceof CrushingPlatform)
				{
					if (((CrushingPlatform) p).getInteraction(player))
						player.respawn();
					p.render(g,f,h);
				}else

				p.render(g, f, h);
			}

		}
	}

	public void renderRespawnPoints(Graphics g, float f, float h,Player player) {
		for(RespawnPoint o : respawnPoints) {

			if (o.getReached()==false) //delete this if you want to allow players to activate previously reached respawn points
			if ((int)o.getX()<(int)player.getX()+player.getWidth() && (int)player.getX()<o.getX()+o.getWidth() && (int)o.getY()-100<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)o.getY()+o.getHeight())
			{
				player.setRespawnX((int) o.getX());
				player.setRespawnY((int) o.getY()-10);
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

	public void renderAreaDmgs(Graphics g, float f, float h) {
		for(Area o : areaDmgs) {

				if (o.getActive()==true)
				{

					//only render if is active
					o.render(g, f, h);


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

	public void addAreaDmg(Area ad) {
		areaDmgs.add(ad);
	}

	public void removeAreaDmg(Area ad) {
		areaDmgs.remove(ad);
	}

	public void addCollisionlessAnimObject(CollisionlessAnimObject cao) {
		collisionlessAnimObjects.add(cao);
	}

	public void removeCollisionlessAnimObject(CollisionlessAnimObject cao) {
		collisionlessAnimObjects.remove(cao);
	}
	
}
