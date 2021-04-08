package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import game.entities.*;
import game.entities.areas.*;

public class Level extends Canvas {

	private boolean gameStarted=false;
	private boolean gameEnded=false;

	private static LinkedList<GameObject> entities = new LinkedList<>();
	private static ArrayList<GameObject> toBeAdded = new ArrayList<>();
	private static ArrayList<GameObject> toBeRemoved = new ArrayList<>();

	public static synchronized LinkedList<GameObject> getGameObjects(){
		return entities;
	}
	public static synchronized ArrayList<GameObject> getToBeAdded() {return  toBeAdded;}
	public static synchronized ArrayList<GameObject> getToBeRemoved() {return  toBeRemoved;}

	public void tick() {

		//Unique Event - GAME STARTING
		if (gameStarted)
		{
			for (Player p : getPlayers())
				p.setCanMove(true);
			gameStarted=false;
		}

		//Unique Event - GAME ENDING
		if (gameEnded)
		{
			for (Player p : getPlayers())
				p.setCanMove(false);
			gameEnded=false;
		}

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

		for(GameObject o : entities) { o.tick(); }

	}

	public void render(Graphics g, float f, float h) {

		for(GameObject o : entities) {
			o.render(g, f, h);
		}
	}

	public static void addEntity(GameObject o) { entities.add(o); }

	public static void removeEntity(GameObject o) { entities.remove(o); }

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


	public PlayerMP getSpecificPlayerMP(String username){

		for (Player p : getPlayers())
		{
			if (p.getUsername().equals(username))
			{
				return (PlayerMP) p;
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


	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	public void setGameEnded(boolean gameEnded) {
		this.gameEnded = gameEnded;
	}
}
