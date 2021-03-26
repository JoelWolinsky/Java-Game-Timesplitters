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
	private LinkedList<Waypoint> waypoints = new LinkedList<>();
	private int i =0;
	private int j=0;

	public synchronized LinkedList<GameObject> getGameObjects(){
		return this.entities;
	}

	public void tick() {

		for (GameObject k: getGameObjects())
		{
			if (k instanceof Player) {
				for (GameObject l:getGameObjects())
				{
					if (l instanceof Platform)
						if (l instanceof CrushingPlatform)
							if (((CrushingPlatform) l).getInteraction(((Player) k)))
								((Player) k).respawn();

					if (l instanceof Area)
					{

						if (l instanceof Portal) {
							if (((Portal) l).getInteraction(((Player) k))) {
								((Player) k).setRespawnX(((Portal) l).getCurrentX() + ((Portal) l).getDestinationX());
								((Player) k).setRespawnY((int) ((Portal) l).getCurrentY() + ((Portal) l).getDestinationLevel() + ((Portal) l).getDestinationY());
								((Player) k).setRespawnThreshold((int)((Portal) l).getCurrentY() + ((Portal) l).getDestinationLevel() + ((Portal) l).getDestinationY());
								k.setY(((Portal) l).getCurrentY() + ((Portal) l).getDestinationLevel() + ((Portal) l).getDestinationY());
								k.setX(k.getX());
								//for (RespawnPoint oo: respawnPoints)
								//	if (oo != o)
								//		oo.setCurrentActive(false);
							}

							if (((Portal) l).getInteractionEffect(((Player) k)))
							{
								switch (j){
									case 0:
										k.setX(k.getX()+15);
										j++;
										break;
									case 1:
										k.setX(k.getX()-15);
										j++;
										break;
									case 2:
										k.setX(k.getX()+15);
										j++;
										break;
									case 3:
										k.setX(k.getX()-15);
										j++;
										break;
									case 4:
										k.setX(k.getX()+15);
										j++;
										break;
									case 5:
										k.setX(k.getX()-15);
										j++;
										break;
									case 6:
										k.setX(k.getX()+15);
										j++;
										break;
									case 7:
										k.setX(k.getX()-15);
										j++;
										break;
								}

							}

						}

						if (l instanceof OnReachAnimArea)
							if (((OnReachAnimArea) l).getInteraction(((Player) k)))
								((OnReachAnimArea) l).setActive(true);
							else
								((OnReachAnimArea) l).setActive(false);

						if (l instanceof RespawnPoint)
							//if (o.getReached()==false) //comment this out if you want to allow players to activate previously reached respawn points
							//if (((RespawnPoint) l).getReached()==false)
							if (((RespawnPoint) l).getInteraction(((Player) k)))
							{
								((Player) k).setRespawnX((int) l.getX() + (int)((RespawnPoint) l).getExtraPointX());
								((Player) k).setRespawnY((int) l.getY()-40 + (int) ((RespawnPoint) l).getExtraPointY());
								((Player) k).setRespawnThreshold((int)l.getY());
								((RespawnPoint)l).setReached(true);
								((RespawnPoint)l).setCurrentActive(true);
								//for (RespawnPoint oo: respawnPoints)
								//	if (oo != o)
								//		oo.setCurrentActive(false);
							}

						if (l instanceof ExtendedRespawnPoint)
							//if (((ExtendedRespawnPoint) l).getReached()==false)
							if (((ExtendedRespawnPoint) l).getInteraction(((Player) k)))
							{
								((Player) k).setRespawnThreshold((int)l.getY());
								((ExtendedRespawnPoint)l).setReached(true);
								((ExtendedRespawnPoint)l).setCurrentActive(true);
								//for (RespawnPoint oo: respawnPoints)
								//	if (oo != o)
								//		oo.setCurrentActive(false);
							}

						//player interaction with damage zones
						if (l instanceof DamageZone)
							if (((DamageZone) l).getActive())
								if (((DamageZone) l).getInteraction(((Player) k)))
									((Player) k).respawn();

						if (l instanceof ScriptedDamageZone)
								if (((ScriptedDamageZone) l).getInteraction(((Player) k)))
									((Player) k).respawn();
						//player interaction with event damage zones
						if (l instanceof EventDamageZone) {

							for (Area xd: ((EventDamageZone) l).getEventArea() )
							if (xd.getInteraction(((Player) k))) {
								((EventDamageZone) l).setTriggered(true);
							}
							if (((EventDamageZone) l).getActive())
								if (((EventDamageZone) l).getInteraction(((Player) k)))
									((Player) k).respawn();
						}

						if (l instanceof DetectionDamageZone) {
								if (((DetectionDamageZone) l).getArea().getInteraction(((Player) k))) {
									//((DetectionDamageZone) l).setTriggered(true);
									((DetectionDamageZone) l).getArea().setVisibile(true);
									((Player) k).respawn();
									i=0;
								}
								else {
									if (i<50)
										i++;
									else
									((DetectionDamageZone) l).getArea().setVisibile(false);
								}
							if (((DetectionDamageZone) l).getActive())
								if (((DetectionDamageZone) l).getInteraction(((Player) k)))
									((Player) k).respawn();

						}

					}

				}
			}

			if (k instanceof AIPlayer) {

				for (GameObject l:entities)
				{

					if (l instanceof Waypoint) {
						//if (o.getReached()==false) //comment this out if you want to allow players to activate previously reached respawn points
						if (((Waypoint) l).getInteraction((AIPlayer) k))
						{

							((AIPlayer) k).setDirection(((Waypoint) l).getDirection());
							((AIPlayer) k).setJump(((Waypoint) l).getJump());

							// Means a waypoint only has an effect once until player touches another one
							if (!waypoints.contains((Waypoint) l)) {

								waypoints.clear();
								waypoints.add((Waypoint) l);
								((AIPlayer) k).setWait(((Waypoint) l).getWait());

							}
						}
					}

					if (l instanceof Platform)
						if (l instanceof CrushingPlatform)
							if (((CrushingPlatform) l).getInteraction(((AIPlayer) k)))
								((AIPlayer) k).respawn();

					if (l instanceof Area)
					{
						if (l instanceof RespawnPoint)
							//if (o.getReached()==false) //comment this out if you want to allow players to activate previously reached respawn points
							if (((RespawnPoint) l).getInteraction(((AIPlayer) k)))
							{
								((AIPlayer) k).setRespawnX((int) l.getX());
								((AIPlayer) k).setRespawnY((int) l.getY()-10);
								((RespawnPoint)l).setReached(true);
								((RespawnPoint)l).setCurrentActive(true);
								//for (RespawnPoint oo: respawnPoints)
								//	if (oo != o)
								//		oo.setCurrentActive(false);
							}

						//player interaction with damage zones
						if (l instanceof DamageZone)
							if (((DamageZone) l).getActive())
								if (((DamageZone) l).getInteraction(((AIPlayer) k)))
									((AIPlayer) k).respawn();

						if (l instanceof ScriptedDamageZone)
							if (((ScriptedDamageZone) l).getInteraction(((AIPlayer) k)))
								((AIPlayer) k).respawn();

						//player interaction with event damage zones
						if (l instanceof EventDamageZone) {

							for (Area xd: ((EventDamageZone) l).getEventArea() )
							if (xd.getInteraction(((AIPlayer) k))) {
								((EventDamageZone) l).setTriggered(true);
							}
							if (((EventDamageZone) l).getActive())
								if (((EventDamageZone) l).getInteraction(((AIPlayer) k)))
									((AIPlayer) k).respawn();
						}

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
			else if (o instanceof ExtendedRespawnPoint)
			{
				if (((ExtendedRespawnPoint) o).getCurrentActive())
					o.render(g, f, h);
			}
			else if (o instanceof Waypoint)
			{
					// COMMENT OUT TO HIDE WAYPOINTS
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
			else if (o instanceof OnReachAnimArea)
			{
				if (((OnReachAnimArea) o).isActive())
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

	public void removeEntity(GameObject o) {
		getGameObjects().remove(o);
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
			return (index);
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
