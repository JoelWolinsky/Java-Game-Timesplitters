package game;

import java.awt.*;
import java.util.LinkedList;

import game.entities.*;
import game.entities.areas.Area;
import game.entities.areas.RespawnPoint;
import game.entities.areas.TimerDamageZone;
import game.entities.platforms.CrushingPlatform;
import game.entities.platforms.MovingPlatform;
import game.entities.platforms.Platform;
import game.entities.platforms.TimerPlatform;

public class Level extends Canvas {
	private LinkedList<GameObject> entities = new LinkedList<>();
	private LinkedList<Player> players = new LinkedList<>();
	private LinkedList<Platform> platforms = new LinkedList<>();
	private LinkedList<Area> areas = new LinkedList<>();

	public void tick() {


		for (Player k: players)
		{

			for (Platform p : platforms)
			{
				if (p instanceof CrushingPlatform)
					if (((CrushingPlatform) p).getInteraction(k))
						k.respawn();
			}

			for(Area o : areas) {

				if (o instanceof RespawnPoint)
					//if (o.getReached()==false) //delete this if you want to allow players to activate previously reached respawn points
					if (o.getInteraction(k))
					{
						k.setRespawnX((int) o.getX());
						k.setRespawnY((int) o.getY()-10);
						((RespawnPoint)o).setReached(true);
						((RespawnPoint)o).setCurrentActive(true);
						//for (RespawnPoint oo: respawnPoints)
						//	if (oo != o)
						//		oo.setCurrentActive(false);
					}

				if (o instanceof TimerDamageZone)
					if (((TimerDamageZone) o).getActive())
						if (o.getInteraction(k))
							k.respawn();
			}
		}

		for(Player p : players) {
			p.tick();
		}
		for(GameObject o : entities) {
			o.tick();
		}
		for(Area ad: areas) {
			ad.tick();
		}
		for (Platform p:platforms) {
			p.tick();
		}

	}
	
	public void render(Graphics g, float f, float h) {

		renderAreas(g,f,h);
		renderPlatforms(g,f,h);
		renderEntities(g, f, h);
		renderPlayers(g,f,h);

	}

	public void renderPlayers(Graphics g, float f, float h) {
		for(Player o : players) {
			o.render(g, f, h);
		}
	}

	public void renderEntities(Graphics g, float f, float h) {
		for(GameObject o : entities) {
			o.render(g, f, h);
		}
	}
	


	public void renderPlatforms(Graphics g, float f, float h) {
		for(Platform p : platforms) {
			////CAMERA PROXIMITY RENDERING
			//if (p.getX()-650<f*(-1) && f*(-1)<p.getX()+800 && p.getY()-380<h*(-1) && h*(-1)<p.getY()+ 740)
			//PLAYER POSITION PROXIMITY RENDERING
			//if (p.getX()-650<player.getX() && player.getX()<p.getX()+800 && p.getY()-480<player.getY() && player.getY()<p.getY()+ 740)
			if (p instanceof TimerPlatform)
			{
				if (((TimerPlatform) p).getActive() == true)
					p.render(g, f, h);
			}
			else
				p.render(g, f, h);
		}
	}


	public void renderAreas(Graphics g, float f, float h) {
		for(Area o : areas) {

			if (o instanceof RespawnPoint) {
				if (((RespawnPoint) o).getCurrentActive())
					o.render(g, f, h);
			}
			else if (o instanceof TimerDamageZone) {
				if (((TimerDamageZone) o).getActive())
					o.render(g, f, h);
			}
			else
				o.render(g,f,h);
		}
	}

	
	public void addEntity(GameObject o) {
		entities.add(o);
	}
	public void removeEntity(GameObject o) {
		entities.remove(o);
	}

	public void addPlayer(Player p) {
		players.add(p);
	}
	public void removePlayer(Player p) {
		players.remove(p);
	}


	public void addPlatform(Platform p) {
		platforms.add(p);
	}

	public void removePlatform(Platform p) {
		platforms.remove(p);
	}


	public void addArea(Area ad) {
		areas.add(ad);
	}

	public void removeArea(Area ad) {
		areas.remove(ad);
	}

}
