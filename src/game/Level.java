package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import game.entities.*;
import game.entities.players.AIPlayer;
import game.entities.players.Player;
import game.entities.players.PlayerMP;

public class Level extends Canvas {

	private static boolean gameStarted=true;
	private static boolean gameEnded=false;

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

	public void addEntity(GameObject o) { entities.add(o); }

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


}
