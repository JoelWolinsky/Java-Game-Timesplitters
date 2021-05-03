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
import game.graphics.AnimationStates;
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
	public static void setLevelState (LevelState levelStatee) {levelState = levelStatee; levelStateChanged=true;}
	private double time1=0,time2;
	private AnimationStates currentAnimState;
	private static boolean levelStateChanged=false;

	public static boolean getLevelStateChanged() {
		return levelStateChanged;
	}

	public static void setLevelStateChanged(boolean levelStateChanged) {
		Level.levelStateChanged = levelStateChanged;
	}

	/**
	 * Called every frame, this adds and removes all awaiting elements, and calls the tick function of all objects within the level
	 */
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
	
	/**
	 * Renders all objects within the level
	 * @param g The graphics object onto which the objects will be rendered
	 * @param xOffset The camera xOffset
	 * @param yOffset The camera yOffset
	 */
	public void render(Graphics g, float xOffset, float yOffset) {

		if (!entities.isEmpty() && levelState!=LevelState.Loading)
		for(GameObject o : entities) {

			o.render(g, xOffset, yOffset);
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


	/**
	 * Removes a given multiplayer character
	 * @param username The username of the multiplayer character to be removed
	 */
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

	/**
	 * Returns a single multiplayer player
	 * @param username The username of the multiplayer player to be returned
	 * @return The specified multiplayer player
	 */
	public static Player getSpecificPlayerMP(String username){

		for (Player p : getPlayers())
		{
			if (p.getUsername().equals(username))
			{
				return p;
			}
		}

		return null;

	}

	/**
	 * Moves a given player in a given direction - used for multiplayer
	 * @param username The username of the player
	 * @param x The number of units in the x axis to move
	 * @param y The number of units in the y axis to move
	 * @param direction The direciton to move the player
	 */
	public void movePlayer(String username, float x, float y, String direction) {
		try {

			if (getSpecificPlayerMP(username)!=null) {

				getSpecificPlayerMP(username).setX(x);
				getSpecificPlayerMP(username).setY(y);



				if(direction.equals("right")) {
					currentAnimState = AnimationStates.RIGHT;
				} else if (direction.equals("left")) {
					currentAnimState = AnimationStates.LEFT;
				} else if (direction.equals("idle")){
					currentAnimState = AnimationStates.IDLE;
				} else {

					currentAnimState = AnimationStates.OTHER;
				}
				//System.out.println(currentAnimState);
				getSpecificPlayerMP(username).setCurrentAnimState(currentAnimState);
			}

		} catch (Exception e) {
			System.out.println("Exception in movePlayer when moving player " + username);
			e.printStackTrace();
		}
	}

	/**
	 * Checks that the correct numbver of players have connected before starting the game
	 */
	public void checkAllPlayersConnected(){

		switch (getGameMode()){
			case SINGLEPLAYER:
				if (getPlayers().size()==1)
				{
					if(time1==0)
						automaticallySetTime1();
					if(getElapsedTime()>=1000) {
						setLevelState(LevelState.Starting);
					}
				}
				break;
			case vsAI:
				if (getPlayers().size()>=2)
					setLevelState(LevelState.Starting);
				break;
			case MULTIPLAYER:
				if (getPlayers().size()==2)
				{
					if(time1==0)
						automaticallySetTime1();
					if(levelState!=LevelState.Starting)
						if(getElapsedTime()>=5000)
							setLevelState(LevelState.Starting);

				}
				break;
		}
	}


	/**
	 * @return The game ending object
	 */
	public static GameEndingObject getFinish()
	{
		for (GameObject o : getGameObjects())
		{
			if (o instanceof GameEndingObject)
				return (GameEndingObject)o;
		}

		return null;
	}
	
	/**
	 * @return All blips in the level
	 */
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

	/**
	 * Moves the wall of death
	 * @param x The number of units in the x-axis to increment the wall of death by
	 */
	public void moveWall(float x) {
		try {
			//System.out.println("movewall");
			if (getWallOfDeath()!=null) {

				getWallOfDeath().setX(x);
				getWallOfDeath().setMoving();
				//System.out.println("movedwall to " + x);

			}

		} catch (Exception e) {
			System.out.println("Exception in movePlayer when moving player " );
			e.printStackTrace();
		}
	}

	/**
	 * Sets the time1 object to the current time
	 */
	public void automaticallySetTime1()
	{
		time1 = System.currentTimeMillis();
	}

	/**
	 * @return The time passed since time1 has been set
	 */
	public double getElapsedTime()
	{
		time2 = System.currentTimeMillis();

		return time2-time1;
	}
	
	/**
	 * @param player A given player
	 * @return Blips targeted on a given player
	 */
	public static Blip getCertainBlip(Player player)
	{
		for (Blip blip: getBlips())
			if (blip.getTarget()==player)
				return blip;

		return null;
	}


}
