package game;

import java.awt.*;
import java.util.LinkedList;

import game.entities.*;
import game.entities.areas.*;
import game.entities.platforms.CrushingPlatform;
import game.entities.platforms.Platform;
import game.entities.platforms.TimerPlatform;

public class Level extends Canvas {

	private LinkedList<GameObject> entities = new LinkedList<>();
	private LinkedList<Player> players = new LinkedList<>();
	
	public synchronized LinkedList<GameObject> getGameObjects(){
		return this.entities;
	}

	public void tick() {

		for (GameObject k: getGameObjects())
		{
			if (k instanceof Player)
				for (GameObject l:getGameObjects())
				{
					if (l instanceof Platform)
						if (l instanceof CrushingPlatform)
							if (((CrushingPlatform) l).getInteraction(((Player) k)))
								((Player) k).respawn();

					if (l instanceof Area)
					{
						if (l instanceof RespawnPoint)
							//if (o.getReached()==false) //comment this out if you want to allow players to activate previously reached respawn points
							if (((RespawnPoint) l).getInteraction(((Player) k)))
							{
								((Player) k).setRespawnX((int) l.getX());
								((Player) k).setRespawnY((int) l.getY()-10);
								((RespawnPoint)l).setReached(true);
								((RespawnPoint)l).setCurrentActive(true);
								//for (RespawnPoint oo: respawnPoints)
								//	if (oo != o)
								//		oo.setCurrentActive(false);
							}

						//player interaction with damage zones
						if (l instanceof DamageZone)
							if (((DamageZone) l).getActive())
								if (((DamageZone) l).getInteraction(((Player) k)))
									((Player) k).respawn();

						//player interaction with event damage zones
						if (l instanceof EventDamageZone) {
							if (((EventDamageZone) l).getEventArea().getInteraction(((Player) k))) {
								((EventDamageZone) l).setTriggered(true);
							}
							if (((EventDamageZone) l).getActive())
								if (((EventDamageZone) l).getInteraction(((Player) k)))
									((Player) k).respawn();
						}
					}

				}

		}

		for(GameObject o : getGameObjects()) {
			o.tick();
		}

	}
	
	public void render(Graphics g, float f, float h) {

		for(GameObject o : getGameObjects()) {

			if (o instanceof RespawnPoint)
			{
				if (((RespawnPoint) o).getCurrentActive())
					o.render(g, f, h);
			}
			else if (o instanceof DamageZone)
			{
					o.render(g, f, h);
			}
			else if (o instanceof TimerPlatform)
			{
				if (((TimerPlatform) o).getActive())
					o.render(g, f, h);
			}
			else
				o.render(g, f, h);
		}
	}

	////CAMERA PROXIMITY RENDERING
	//if (p.getX()-650<f*(-1) && f*(-1)<p.getX()+800 && p.getY()-380<h*(-1) && h*(-1)<p.getY()+ 740)
	//PLAYER POSITION PROXIMITY RENDERING
	//if (p.getX()-650<player.getX() && player.getX()<p.getX()+800 && p.getY()-480<player.getY() && player.getY()<p.getY()+ 740)


	
	public void addEntity(GameObject o) {
//		System.out.println("Adding entitiy to level");
		getGameObjects().add(o);
	}

	public void addPlayer(Player p) {
		getGameObjects().add(p);
	}
	public void removeEntity(GameObject o) {
		getGameObjects().remove(o);
	}

	public LinkedList<GameObject> getEntities() {
		return getGameObjects();
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
					System.out.println("username");
					System.out.println(((PlayerMP)e).getUsername());
					System.out.println(username);
					break;
				}
				index++;
			}
			System.out.println("index: " + index);
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
			System.out.println(getGameObjects().size());
			if(index == getGameObjects().size() && getGameObjects().get(index-1) instanceof PlayerMP) {
				System.out.println("too big");
				index--;
			}
			getGameObjects().get(index).setX(x);
			getGameObjects().get(index).setY(y);;
		} catch (Exception e) {
			System.out.println("Exception in movePlayer when moving player " + username);
			e.printStackTrace();
		}
	}

}
