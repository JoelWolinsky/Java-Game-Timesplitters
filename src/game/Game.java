package game;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.*;

import game.display.Window;
import game.entities.GameObject;
import game.entities.players.Player;
import game.entities.players.AIPlayer;
import game.graphics.Assets;
import game.graphics.GameMode;
import game.graphics.LevelState;
import game.graphics.MapMode;
import game.input.KeyInput;
import game.network.packets.Packet00Login;
import network.GameClient;
import network.GameServer;
import game.entities.players.PlayerMP;

import static game.Level.getFinish;
import static game.Level.getLevelState;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = -772676358550096683L;
	private Thread thread;
	private static boolean running = false;
	public static KeyInput keyInput = new KeyInput();
	public static MouseInput mouseInput = new MouseInput();
	public static GameState gameState = GameState.MainMenu;

	public static GameClient socketClient;
	public static GameServer socketServer;

	public float a=0,b=0,c=0,d=0;
	public static Game game;
	public static Player player;
	public static Camera camera = new Camera();
	private Assets s = new Assets();
	public Map m;
	public static GameMode gameMode = GameMode.SINGLEPLAYER;
	public static boolean bruh=false;

	/**
	 * Initialises game entities and objects that must appear at the start of the game
	 */
	public Game() {
		new Window(this);
	}

	/**
	 * Called every frame, this tells certain lists, objects, or entities to call their own tick function.
	 */
	private void tick() {

		if(this.gameState == GameState.Playing) {
			m.getCurrentLevel().tick();
			camera.tick();
		}
	}

	/**
	 * Responsible for all rendering. This generates a Graphics object, refreshes the screen, and renders the correct objects based on the play state.
	 */
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(4);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		//Rendering code happens here


		if(this.gameState == GameState.Playing) {
			g.setColor(Color.black);
			g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);

			//END GAME VICTORY
			if (getLevelState()== LevelState.Finished) {
				Graphics2D zoomed = (Graphics2D) g;


				//Zooming Effect on scaling the camera
				if (a<6)
				{
					a=a+0.25f;
				}

				if (b<6)
				{
					b=b+0.25f;
				}

				//Zooming Effect on positioning the camera
				if (c<213)
				{
					c=c+71;
				}

				if (d<192)
				{
					d=d+24;
				}


				camera.addTarget(getFinish());
				zoomed.scale(a, b);
				m.getCurrentLevel().render(zoomed, camera.getXOffset() - c - ((Window.WIDTH/6)/2), camera.getYOffset()-d);
			}
			else
			{
				m.getCurrentLevel().render(g, camera.getXOffset(), camera.getYOffset() + 100);
			}

		}else {
			g.setColor(new Color(255,255,255));
			g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		}

		//Rendering code stops here

		g.dispose();
		bs.show();
	}

	/**
	 * The game loop, calls tick and render regularly
	 */
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
//		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now-lastTime)/ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				render();
				delta--;
			}
			if(running) {
				if(System.currentTimeMillis() - timer >1000) {
					timer += 1000;
				}
			}
		}
		stop();
	}



	public synchronized void start() {

		m = new Map(MapMode.RNG, gameMode);

		switch (gameMode)
		{
			case MULTIPLAYER:
				player = new PlayerMP(0, 350, keyInput, null, -1,"player1");

				Packet00Login loginPacket = new Packet00Login(player.getUsername(), 200, 340);
				if (socketServer != null) {
					//socketServer.addConnection((PlayerMP) player, loginPacket);
					loginPacket.writeData(socketServer);
				}
				loginPacket.writeData(socketClient);
				break;
			case vsAI:
				player = new Player(0, 340, keyInput, 0 ,0,"player1");
				m.getCurrentLevel().addToAddQueue(new AIPlayer(30, 350, 0 ,0, player,"player2"));
				m.getCurrentLevel().addToAddQueue(new AIPlayer(60, 350, 0 ,0, player,"player3"));
				m.getCurrentLevel().addToAddQueue(new AIPlayer(90, 350, 0 ,0, player,"player4"));
				break;
			case SINGLEPLAYER:
				player = new Player(0, 350, keyInput, 0 ,0,"player1");
				break;
		}

		m.getCurrentLevel().addToAddQueue(player);
		System.out.println(gameMode);

		game = this;
		camera.addTarget(player);


		//GameProgress UI
		m.currentLevel.addEntity(new ProgressBarController(Game.camera.getXOffset(), Game.camera.getYOffset()+10, 0,0,m.currentLevel,m.mps));
		//Inventorry UI
		m.currentLevel.addEntity(new InventoryBarController(Game.camera.getXOffset(), Game.camera.getYOffset()+400, 0,0,Game.player,m.currentLevel));
		//Main UI Announcer eg. Countdown, End Game Celebration etc.
		m.currentLevel.addEntity(new UIController(Game.camera.getXOffset(), Game.camera.getYOffset()+10,0,0));

		//Sorts all of the entities based on their z index
		Collections.sort(m.getCurrentLevel().getGameObjects(), Comparator.comparingInt(GameObject::getZ));

		//windowHandler = new WindowHandler(this);
		this.addKeyListener(keyInput);
		this.addMouseListener(mouseInput);
		this.addMouseMotionListener(mouseInput);

		//The server is started in this function
		thread = new Thread(this);
		thread.start();
		running = true;
		this.requestFocus();
	}



	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public static GameMode getGameMode() {
		return gameMode;
	}

	public static void setGameState(GameState gameStatee) {
		gameState = gameStatee;
	}

	public GameState getGameState() {
		return gameState;
	}

	public static void setRunning(boolean runningg) {
		running = runningg;
	}

	public static void setBruh(boolean bruh) {
		Game.bruh = bruh;
	}
}
