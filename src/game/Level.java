package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import game.entities.*;
import game.entities.areas.GameEndingObject;
import game.entities.areas.WallOfDeath;
import game.entities.players.AIPlayer;
import game.entities.players.Player;
import game.entities.players.PlayerMP;
import game.graphics.LevelState;

import static game.Game.getGameMode;

public class Level extends Canvas {

	private static boolean gameStarted=true;
	private static boolean gameEnded=false;
	private static LevelState levelState = LevelState.Loading;

	private static LinkedList<GameObject> entities = new LinkedList<>();
	public static ArrayList<GameObject> toBeAdded = new ArrayList<>();
	private static ArrayList<GameObject> toBeRemoved = new ArrayList<>();

	public static synchronized LinkedList<GameObject> getGameObjects(){
		return entities;
	}
	public static synchronized ArrayList<GameObject> getToBeAdded() {return  toBeAdded;}
	public static synchronized void addToAddQueue(GameObject o) {toBeAdded.add(o);}
	public static synchronized void addToRemoveQueue(GameObject o) {toBeRemoved.add(o);}
	public static synchronized ArrayList<GameObject> getToBeRemoved() {return  toBeRemoved;}
	public static LevelState getLevelState () {return levelState;}
	public static void setLevelState (LevelState levelStatee) {levelState = levelStatee;}
	private double time1=0,time2;

	public void tick() {


		//Add the elements sitting in the queue to be added
		if (!toBeAdded.isEmpty()) {
			for (GameObject o : toBeAdded) {
				entities.add(o);
			}
			toBeAdded.removeAll(toBeAdded);
		}

		//Remove the elements sitting in the queue to be removed
		if (!toBeRemoved.isEmpty()) {
			for (GameObject o : toBeRemoved) {
				entities.remove(o);
			}
			toBeRemoved.removeAll(toBeRemoved);
		}

		if (levelState!=LevelState.Loading)
		for(GameObject o : entities) { o.tick(); }

		if (levelState==LevelState.Waiting)
			checkAllPlayersConnected();

	}

	public void render(Graphics g, float f, float h) {

		if (!entities.isEmpty() && levelState!=LevelState.Loading)
		for(GameObject o : entities) {
			o.render(g, f, h);
		}
	}

	public void addEntity(GameObject o) {entities.add(o); }

	public static void removeEntity(GameObject o) { entities.remove(o); }

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

	public static LinkedList<AIPlayer> getAIPlayers()
	{
		LinkedList<AIPlayer> AIplayers = new LinkedList<AIPlayer>();
		for (GameObject o : entities)
		{
			if (o instanceof AIPlayer)
				AIplayers.add((AIPlayer) o);
		}

		return AIplayers;
	}


	public static void setGameStarted(boolean gmeStarted) {
		gameStarted = gmeStarted;
	}

	public static void setGameEnded(boolean gmeEnded) {
		gameEnded = gmeEnded;
	}


	//MULTIPLAYER STUFF

	public void removePlayerMP(String username) {
		try {
			for(Player e : getPlayers())
			{
				if(e instanceof PlayerMP && e.getUsername().equals(username)) {
					toBeRemoved.add(e);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in removePlayerMP. Player " + username);
			e.printStackTrace();
		}
	}


	public Player getSpecificPlayerMP(String username){

		for (Player p : getPlayers())
		{
			if (p.getUsername().equals(username))
			{
				return p;
			}
		}

		return null;

	}

	public void movePlayer(String username, float x, float y) {
		try {

			if (getSpecificPlayerMP(username)!=null) {

				getSpecificPlayerMP(username).setX(x);
				getSpecificPlayerMP(username).setY(y);
				
				
			}

		} catch (Exception e) {
			System.out.println("Exception in movePlayer when moving player " + username);
			e.printStackTrace();
		}
	}

	public void checkAllPlayersConnected(){

		switch (getGameMode()){
			case SINGLEPLAYER:
				if (getPlayers().size()==1)
				{
					if(time1==0)
						time();
					if(levelState!=LevelState.Starting)
					if(timetime()>=1000)
						levelState=LevelState.Starting;
				}
				break;
			case vsAI:
				if (getPlayers().size()>=2)
					levelState=LevelState.Starting;
				break;
			case MULTIPLAYER:
				if (getPlayers().size()==2)
				{
					if(time1==0)
						time();
					if(levelState!=LevelState.Starting)
						if(timetime()>=5000)
							levelState=LevelState.Starting;

				}
				break;
		}
	}

	public static GameEndingObject getFinish()
	{
		for (GameObject o : getGameObjects())
		{
			if (o instanceof GameEndingObject)
				return (GameEndingObject)o;
		}

		return null;
	}

	public static ArrayList<Blip> getBlips(){
		ArrayList<Blip> blips = new ArrayList<Blip>();
		for (GameObject o : entities)
		{
			if (o instanceof Blip)
				blips.add((Blip) o);
		}

		return blips;
	}

	public static WallOfDeath getWallOfDeath()
	{
		for (GameObject o : entities)
		{
			if (o instanceof WallOfDeath)
				return (WallOfDeath) o;
		}
		return null;
	}

	public void time()
	{
		time1 = System.currentTimeMillis();
	}

	public double timetime()
	{
		time2 = System.currentTimeMillis();

		return time2-time1;
	}

}
