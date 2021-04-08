package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import game.entities.*;
import game.entities.areas.*;

public class Level extends Canvas {

	private static LinkedList<GameObject> entities = new LinkedList<>();
	private static ArrayList<GameObject> toBeAdded = new ArrayList<>();
	private static ArrayList<GameObject> toBeRemoved = new ArrayList<>();
	private static LinkedList<Waypoint> waypoints = new LinkedList<>();
	private boolean gameStarted=true;

	public static synchronized LinkedList<Waypoint> getWaypoints(){
		return waypoints;
	}
	public static synchronized LinkedList<GameObject> getGameObjects(){
		return entities;
	}
	public static synchronized ArrayList<GameObject> getToBeAdded() {return  toBeAdded;}
	public static synchronized ArrayList<GameObject> getToBeRemoved() {return  toBeRemoved;}

	public void tick() {


		if (!toBeAdded.isEmpty()) {
			for (GameObject o : toBeAdded) {
				entities.add(o);
			}
			toBeAdded.removeAll(toBeAdded);
		}

		if (!toBeRemoved.isEmpty()) {
			for (GameObject o : toBeRemoved) {
				entities.remove(o);
			}
			toBeRemoved.removeAll(toBeRemoved);
		}


		for (GameObject object1: entities)
		{

			if (object1 instanceof Player)
				{
					if (gameStarted)
						((Player) object1).setCanMove(true);
				}

		}

		for(GameObject o : entities) {
			o.tick();
		}


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

	public static LinkedList<MindlessAI> getChickens()
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
