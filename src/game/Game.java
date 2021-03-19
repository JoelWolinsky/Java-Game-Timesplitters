package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JOptionPane;

import game.display.Window;
import game.entities.GameObject;
import game.entities.Player;
import game.entities.AIPlayer;
import game.entities.platforms.Platform;
import game.graphics.Assets;
import game.input.KeyInput;
import game.network.packets.Packet00Login;
import network.GameClient;
import network.GameServer;
import game.entities.PlayerMP;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = -772676358550096683L;
	private Thread thread;
	private boolean running = false;
	public static KeyInput keyInput = new KeyInput();
	public static MouseInput mouseInput = new MouseInput();
	public static GameState state = GameState.MainMenu;
	public Level currentLevel = new Level();

	public static GameClient socketClient;
	public static GameServer socketServer;

	public static Boolean isMultiplayer = false;
	public static Game game;

	public static Player player;
	public static AIPlayer aiPlayer;
	public static Camera camera;

	/**
	 * Initialises game entities and objects that must appear at the start of the game
	 */
	public Game() {
		new Window(this);

		System.out.println("window open");
//		this.start();
	}

	/**
	 * Called every frame, this tells certain lists, objects, or entities to call their own tick function.
	 */
	private void tick() {
		if(this.state == GameState.Playing) {
			currentLevel.tick();
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

		if(this.state == GameState.Playing) {
			g.setColor(Color.black);
			g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
			currentLevel.render(g, camera.getXOffset(), camera.getYOffset()+100);
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
				//Can we change the render to here?? Animations are a lot more relative to speed by doing this --Marek
				render();
				delta--;
			}
			if(running) {
				///render(); ***ORIGINAL PLACE --Marek


				if(System.currentTimeMillis() - timer >1000) {
					timer += 1000;
				}
			}
		}
		stop();
	}



	public synchronized void start() {

		System.out.println("start()");

		if(isMultiplayer == false) {
			System.out.println("not mp");

//			player = new Player(0, 340, 0 ,0,"./img/adventurer-idle0.png","./img/adventurer-idle1.png","./img/adventurer-idle2.png");
			aiPlayer = new AIPlayer(50, 340, 0 ,0,"./img/adventurer-idle0.png","./img/adventurer-idle1.png","./img/adventurer-idle2.png");

			player = new Player(0, 340, keyInput, 0 ,0);

			currentLevel.addEntity(player);
			currentLevel.addEntity(aiPlayer);

		}else {
			System.out.println("mp");
			player = new PlayerMP(this.currentLevel, 300, 300, keyInput, null, -1);
			currentLevel.addEntity(player);
			Packet00Login loginPacket = new Packet00Login(player.getUsername(), 300, 300);
	        if (socketServer != null) {
	        	socketServer.addConnection((PlayerMP) player, loginPacket);
	        }
	        loginPacket.writeData(socketClient);


		}

		game = this;

		//make this as a player choice in the menu either MAP 1 or Randomly Generated

		String mapMode = "default";
		Map m = new Map();
		//keep default for now until we sort randomly generated
		if (mapMode.equals("default")) {


			// FOR DEMO PURPOSES

			//m.mapParser(currentLevel, "./src/game/segments/intersegmentA3.txt");
			//m.mapParser(currentLevel, "./src/game/segments/segmentA13.txt");
			//m.mapParser(currentLevel, "./src/game/segments/segmentA14.txt");
			//m.mapParser(currentLevel, "./src/game/segments/segmentA12.txt");
			//m.mapParser(currentLevel, "./src/game/segments/segmentA13.txt");
			//m.mapParser(currentLevel, "./src/game/segments/intersegmentA3.txt");

		//	m.mapParser(currentLevel, "./src/game/segments/segmentA1X.txt"); 		// 1 - basic first one (numbers for demo)
			// TODO: Fix glitch on falling rocks where you teleport into wall
			//m.mapParser(currentLevel, "./src/game/segments/intersegmentA2X.txt");	// 2 - falling rocks
			//m.mapParser(currentLevel, "./src/game/segments/segmentA2X.txt");		// 3 - electric one
			//m.mapParser(currentLevel, "./src/game/segments/segmentA7X.txt"); 		// 4 - platforms
			//m.mapParser(currentLevel, "./src/game/segments/intersegmentA3X.txt"); 	// 5 - hands intersegment
			//m.mapParser(currentLevel, "./src/game/segments/segmentA13X.txt"); 		// 6 - crushing bookshelves
			//m.mapParser(currentLevel, "./src/game/segments/segmentA6X.txt"); 		// 8 - ghosts - easier - add segment before
		//	m.mapParser(currentLevel, "./src/game/segments/segmentEND.txt"); 		// 7 - END

			// m.mapParser(currentLevel, "./src/game/segments/segmentA11.txt"); 		// 9 - disappearing floors



			m.mapParser(currentLevel, "./src/game/segments/segmentA1.txt");			// basic segment
			m.mapParser(currentLevel, "./src/game/segments/intersegmentA1.txt"); 	// falling objects - hard for AI
			m.mapParser(currentLevel, "./src/game/segments/segmentA2.txt");			// electric one
			m.mapParser(currentLevel, "./src/game/segments/intersegmentA2.txt");	// falling rocks
			m.mapParser(currentLevel, "./src/game/segments/segmentA3.txt");			// aesthetic hall 1
			m.mapParser(currentLevel, "./src/game/segments/segmentA4.txt");			// aesthetic hall 2
			m.mapParser(currentLevel, "./src/game/segments/segmentA5.txt");			// aesthetic hall 3

			m.mapParser(currentLevel, "./src/game/segments/intersegmentA3.txt");	// hands one
			m.mapParser(currentLevel, "./src/game/segments/segmentA6.txt");			// ghosts
			m.mapParser(currentLevel, "./src/game/segments/segmentA7.txt");			// platforms

			m.mapParser(currentLevel, "./src/game/segments/intersegmentA3.txt");	// hands one
			m.mapParser(currentLevel, "./src/game/segments/segmentA8.txt");			// disappearing long and small platforms
			m.mapParser(currentLevel, "./src/game/segments/intersegmentA3.txt");	// hands one
			m.mapParser(currentLevel, "./src/game/segments/segmentA10.txt");		// long corridor
			m.mapParser(currentLevel, "./src/game/segments/segmentA11.txt");		// disappearing platforms over acid
			m.mapParser(currentLevel, "./src/game/segments/segmentA12.txt");		// bookshelf pyramid
			m.mapParser(currentLevel, "./src/game/segments/segmentA13.txt");		// crushing bookshelves
			m.mapParser(currentLevel, "./src/game/segments/segmentA14.txt");


		}
		else if (mapMode.equals("randomlyGenerated"))
		{
			//WORK IN PROGRESS
		}

		Collections.sort(currentLevel.getGameObjects(), Comparator.comparingInt(GameObject::getZ));


		camera = new Camera();
		camera.addTarget(player);


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

}
