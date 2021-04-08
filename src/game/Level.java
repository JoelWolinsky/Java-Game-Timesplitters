package game;

import java.awt.*;
import java.util.LinkedList;
import game.entities.*;
import game.entities.areas.*;
import game.entities.platforms.CrushingPlatform;
import game.entities.platforms.Platform;

public class Level extends Canvas {

	private static LinkedList<GameObject> entities = new LinkedList<>();
	private LinkedList<Waypoint> waypoints = new LinkedList<>();
	private int i =0;
	private int j = 0;
	private int gameTimer=0;
	private boolean added=false;
	private boolean gameStarted=true;

	public static synchronized LinkedList<GameObject> getGameObjects(){
		return entities;
	}

	public void tick() {


		for (MindlessAISpawner mss: getChickenSpawners()) {
			if (mss.getAddChicken()) {
				addEntity(new MindlessAI(mss.getDummyX(), mss.getDummyY(), mss.getDummyWidth(), mss.getDummyHeight(), mss.getDummyMinRange(), mss.getDummyMaxRange(), mss.getUrls()));
				mss.setAddChicken(false);
				break;
			}
			else if (mss.getRemoveChicken())
			{
				removeEntity(getChickens().getFirst());
				mss.setRemoveChicken(false);
				break;
			}
		}


		for (GameObject object1: getGameObjects())
		{


			if (object1 instanceof Player)
				{
					if (gameStarted)
						((Player) object1).setCanMove(true);

					for (Item i : ((Player) object1).getInventory()) {
						if (i.getAddItem()) {
							this.addEntity(new AddedItem(object1.getX(), object1.getY(), 0, 0, (Player) object1, i.getItemToAdd() , i.getItemToAdd(),i.getItemToAdd(),i.getItemToAdd()));
							i.setAddItem(false);
							added=true;
							break;
						}
					}

					if (added)
					{
						added=false;
						break;
					}


				}




			if (object1 instanceof AIPlayer) {

					for (GameObject l:getGameObjects())
					{
						
						if (l instanceof Waypoint) {
							//if (o.getReached()==false) //comment this out if you want to allow players to activate previously reached respawn points
							if (((Waypoint) l).getInteraction((AIPlayer) object1))
							{
	
								((AIPlayer) object1).setDirection(((Waypoint) l).getDirection());
								((AIPlayer) object1).setJump(((Waypoint) l).getJump());
	
								// Means a waypoint only has an effect once until player touches another one
								if (!waypoints.contains((Waypoint) l)) {
	
									waypoints.clear();
									waypoints.add((Waypoint) l);
									((AIPlayer) object1).setWait(((Waypoint) l).getWait());
	
								}
							}
						}
						



						if (l instanceof Platform)
							if (l instanceof CrushingPlatform)
								if (((CrushingPlatform) l).getInteraction(((AIPlayer) object1)))
									((AIPlayer) object1).respawn();
	
						if (l instanceof Area)
						{
	
							if (l instanceof Portal) {
								if (((Portal) l).getInteraction(((AIPlayer) object1))) {
									((AIPlayer) object1).setRespawnX(((Portal) l).getCurrentX() + ((Portal) l).getDestinationX());
									((AIPlayer) object1).setRespawnY((int) ((Portal) l).getCurrentY() + ((Portal) l).getDestinationLevel() + ((Portal) l).getDestinationY());
									((AIPlayer) object1).setRespawnThreshold((int)((Portal) l).getCurrentY() + ((Portal) l).getDestinationLevel() + ((Portal) l).getDestinationY());
									object1.setY(((Portal) l).getCurrentY() + ((Portal) l).getDestinationLevel() + ((Portal) l).getDestinationY());
									object1.setX(object1.getX());
									//for (RespawnPoint oo: respawnPoints)
									//	if (oo != o)
									//		oo.setCurrentActive(false);
								}
	
								if (((Portal) l).getInteractionEffect(((AIPlayer) object1)))
								{
									switch (j){
										case 0:
											object1.setX(object1.getX()+15);
											j++;
											break;
										case 1:
											object1.setX(object1.getX()-15);
											j++;
											break;
										case 2:
											object1.setX(object1.getX()+15);
											j++;
											break;
										case 3:
											object1.setX(object1.getX()-15);
											j++;
											break;
										case 4:
											object1.setX(object1.getX()+15);
											j++;
											break;
										case 5:
											object1.setX(object1.getX()-15);
											j++;
											break;
										case 6:
											object1.setX(object1.getX()+15);
											j++;
											break;
										case 7:
											object1.setX(object1.getX()-15);
											j++;
											break;
									}
	
								}
	
							}
	
							if (l instanceof OnReachAnimArea)
								if (((OnReachAnimArea) l).getInteraction(((AIPlayer) object1)))
									((OnReachAnimArea) l).setVisibile(true);
								else
									((OnReachAnimArea) l).setVisibile(false);
	
							if (l instanceof RespawnPoint)
								//if (o.getReached()==false) //comment this out if you want to allow players to activate previously reached respawn points
								//if (((RespawnPoint) l).getReached()==false)
								if (((RespawnPoint) l).getInteraction(((AIPlayer) object1)))
								{
									((AIPlayer) object1).setRespawnX((int) l.getX() + (int)((RespawnPoint) l).getExtraPointX());
									((AIPlayer) object1).setRespawnY((int) l.getY()-40 + (int) ((RespawnPoint) l).getExtraPointY());
									((AIPlayer) object1).setRespawnThreshold((int)l.getY());
									((RespawnPoint)l).setReached(true);
									((RespawnPoint)l).setCurrentActive(true);
									//for (RespawnPoint oo: respawnPoints)
									//	if (oo != o)
									//		oo.setCurrentActive(false);


									// Means a waypoint only has an effect once until player touches another one
									if (!((AIPlayer) object1).getRespawnPoints().contains((RespawnPoint) l)) {
		
										((AIPlayer) object1).addRespawnPoint((RespawnPoint) l);
												
									}

								}
	
							if (l instanceof ExtendedRespawnPoint)
								//if (((ExtendedRespawnPoint) l).getReached()==false)
								if (((ExtendedRespawnPoint) l).getInteraction(((AIPlayer) object1)))
								{
									((AIPlayer) object1).setRespawnThreshold((int)l.getY());
									((ExtendedRespawnPoint)l).setReached(true);
									((ExtendedRespawnPoint)l).setCurrentActive(true);
									//for (RespawnPoint oo: respawnPoints)
									//	if (oo != o)
									//		oo.setCurrentActive(false);
								}
	
							//player interaction with damage zones
							if (l instanceof DamageZone)
								if (((DamageZone) l).getActive())
									if (((DamageZone) l).getInteraction(((AIPlayer) object1)))
										((AIPlayer) object1).respawn();
	
							if (l instanceof ScriptedDamageZone)
									if (((ScriptedDamageZone) l).getInteraction(((AIPlayer) object1)))
										((AIPlayer) object1).respawn();
							//player interaction with event damage zones
							if (l instanceof EventDamageZone) {
	
								for (Area xd: ((EventDamageZone) l).getEventArea() )
								if (xd.getInteraction(((AIPlayer) object1))) {
									((EventDamageZone) l).setTriggered(true);
								}
								if (((EventDamageZone) l).getActive())
									if (((EventDamageZone) l).getInteraction(((AIPlayer) object1)))
										((AIPlayer) object1).respawn();
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
			o.render(g, f, h);
		}
	}

	public void addEntity(GameObject o) { getGameObjects().add(o); }

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

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}


	public static LinkedList<Player> getPlayers()
	{
		LinkedList<Player> players = new LinkedList<Player>();
		for (GameObject o : entities)
		{
			if (o instanceof Player)
				players.add((Player)o);
		}

		return players;
	}

	public LinkedList<MindlessAI> getChickens()
	{
		LinkedList<MindlessAI> chickens = new LinkedList<MindlessAI>();
		for (GameObject o : getGameObjects())
		{
			if (o instanceof MindlessAI)
				chickens.add((MindlessAI) o);
		}


		return chickens;
	}

	public LinkedList<MindlessAISpawner> getChickenSpawners()
	{
		LinkedList<MindlessAISpawner> chickenSpawners = new LinkedList<MindlessAISpawner>();
		for (GameObject o : getGameObjects())
		{
			if (o instanceof MindlessAISpawner)
				chickenSpawners.add((MindlessAISpawner) o);
		}


		return chickenSpawners;
	}
}
