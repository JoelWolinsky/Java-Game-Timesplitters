package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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
	public static Boolean againstComputer = false;
	public static Game game;
	boolean half = false;
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

		if(isMultiplayer == true) {
			
			System.out.println("mp");
			player = new PlayerMP(this.currentLevel, 300, 300, keyInput, null, -1);
			currentLevel.addEntity(player);
			Packet00Login loginPacket = new Packet00Login(player.getUsername(), 300, 300);
	        if (socketServer != null) {
	        	socketServer.addConnection((PlayerMP) player, loginPacket);
	        }
	        loginPacket.writeData(socketClient);

		} else if (againstComputer == true) {

			System.out.println("not mp");
			System.out.println("against computer");

			aiPlayer = new AIPlayer(50, 340, 0 ,0,"./img/adventurer-idle0.png","./img/adventurer-idle1.png","./img/adventurer-idle2.png");
			player = new Player(0, 340, keyInput, 0 ,0);
			
			currentLevel.addEntity(player);
			currentLevel.addEntity(aiPlayer);
			
		} else {
			
			System.out.println("not mp");
			System.out.println("single player");

			player = new Player(0, 340, keyInput, 0 ,0);

			currentLevel.addEntity(player);

		}

		game = this;

		//make this as a player choice in the menu either MAP 1 or Randomly Generated

		String mapMode = "default";
		Map m = new Map();
		//keep default for now untwil we sort randomly generated
		if (mapMode.equals("default")) {
			
			m.mapParser(currentLevel, "intersegmentA3");			
			m.mapParser(currentLevel, "intersegmentA1");			
			m.mapParser(currentLevel, "goNX");			
			m.mapParser(currentLevel, "segmentA1X");			

			
			// TODO: Fix segment A7
			
			/*

			LEGEND

			m.mapParser(currentLevel, "goN");				// Skeletons throwing objects down
			
			m.mapParser(currentLevel, "intro1");			// No go zone 						-- Don't think I need AI version
			m.mapParser(currentLevel, "intro2");			// Basic chandelier room
			m.mapParser(currentLevel, "introDimension");	// Pink portal

			m.mapParser(currentLevel, "segmentA1");			// basic segment
			m.mapParser(currentLevel, "segmentA2");			// electric one
			m.mapParser(currentLevel, "segmentA3");			// aesthetic hall 1
			m.mapParser(currentLevel, "segmentA4");			// aesthetic hall 2
			m.mapParser(currentLevel, "segmentA5");			// aesthetic hall 3
			m.mapParser(currentLevel, "segmentA6");			// ghosts
			m.mapParser(currentLevel, "segmentA7");			// platforms						-- CAUSES PROGRAM TO CRASH
			m.mapParser(currentLevel, "segmentA8");			// disappearing long and small platforms
			m.mapParser(currentLevel, "segmentA9");			// spinning fireball one 			-- NOT DOING AI VERSION
			m.mapParser(currentLevel, "segmentA10");		// long corridor
			m.mapParser(currentLevel, "segmentA11");		// disappearing platforms over acid
			m.mapParser(currentLevel, "segmentA12");		// bookshelf pyramid
			m.mapParser(currentLevel, "segmentA13");		// wizard and crushing bookshelves 	-- NOT DOING AI VERSION
			m.mapParser(currentLevel, "segmentA14");		// interstellar bookshelf columns 	-- WAIT UNTIL DEBUGGED
			
			m.mapParser(currentLevel, "intersegmentA1"); 	// skeletons throwing objects down
			m.mapParser(currentLevel, "intersegmentA2");	// falling rocks
			m.mapParser(currentLevel, "intersegmentA3");	// hands one
			



 */
		}
		else if (mapMode.equals("RNG")) {
			// ;
			ArrayList<String> segments1 = new ArrayList<String>(Arrays.asList("segmentA1","segmentA2","intersegmentA2","intersegmentA1","intersegmentA2up","intersegmentA2down","intersegmentA2up","intersegmentA2down"));
			//ArrayList<String> segments1 = new ArrayList<String>(Arrays.asList("segmentA1", "segmentA2"));
			ArrayList<String> segments2 = new ArrayList<String>(Arrays.asList("segmentA3", "segmentA4", "segmentA4"));
			ArrayList<String> intro2 = new ArrayList<String>(Arrays.asList("intro2"));
			ArrayList<String> throneRoom = new ArrayList<String>(Arrays.asList("segmentA5"));

			int index = 0;
			int rnd1;

			String level0="";
			String level1="";
			String level2="";
			String level3="";
			String oplevel0="";
			String oplevel1="";
			String oplevel2="";
			String oplevel3="";


			m.mapParser(currentLevel, "intro1");

			while (!segments1.isEmpty()) {


				if (half) {

					level0="ground2.png";
					level1="sky2.png";
					level2="sky4.png";
					level3="sky6.png";

					oplevel0="ground1.png";
					oplevel1="sky1.png";
					oplevel2="sky3.png";
					oplevel3="sky5.png";

				} else {

					level0="ground1.png";
					level1="sky1.png";
					level2="sky3.png";
					level3="sky5.png";

					oplevel0="ground2.png";
					oplevel1="sky2.png";
					oplevel2="sky4.png";
					oplevel3="sky6.png";

				}


				m.parseCommand(currentLevel, "Chunk 1 E asdgasdg.png");

				rnd1 = new Random().nextInt(segments1.size());

				if (index == 0)
					while (segments1.get(rnd1).equals("intersegmentA2down"))
						rnd1 = new Random().nextInt(segments1.size());


				if (segments1.get(rnd1).equals("intersegmentA2up")) {

					if (index == 0) {
						m.parseCommand(currentLevel, "Platform Custom -86 384 customFloor3.png");
						m.parseCommand(currentLevel, "Platform Custom 340 384 customFloor3.png");
					} else {
						m.parseCommand(currentLevel, "Platform Custom 54 384 edge1.png");
						m.parseCommand(currentLevel, "Platform Custom 340 384 customFloor3.png");
					}

					switch (index) {
						case 0:
							m.parseCommand(currentLevel, "Area 0 0 ".concat(level0));
							m.parseCommand(currentLevel, "Area 0 -384 ".concat(level1));
							m.parseCommand(currentLevel, "Area 0 -768 ".concat(level2));
							m.parseCommand(currentLevel, "Area -426 -768 ".concat(oplevel2));
							break;
						case 1:

							m.parseCommand(currentLevel, "Area 0 384 ".concat(level0));
							m.parseCommand(currentLevel, "Area 0 0 ".concat(level1));
							m.parseCommand(currentLevel, "Area 0 -384 ".concat(level2));
							m.parseCommand(currentLevel, "Area 0 -768 ".concat(level3));
							m.parseCommand(currentLevel, "Area -426 -768 ".concat(oplevel3));
							break;
						case 2:

							m.parseCommand(currentLevel, "Area 0 768 ".concat(level0));
							m.parseCommand(currentLevel, "Area 0 384 ".concat(level1));
							m.parseCommand(currentLevel, "Area 0 0 ".concat(level2));
							m.parseCommand(currentLevel, "Area 0 -384 ".concat(level3));
							m.parseCommand(currentLevel, "Area 0 -768 ".concat(oplevel3)); // ?
							m.parseCommand(currentLevel, "Area -426 -768 ".concat(level3));
							break;
					}

					if (index != 2)
						index++;
					if (!half)
						half = true;
					else
						half = false;

				} else if (segments1.get(rnd1).equals("intersegmentA2down")) {

					if (index == 1) {
						m.parseCommand(currentLevel, "Platform Custom -86 768 customFloor3.png");
						m.parseCommand(currentLevel, "Platform Custom 340 768 customFloor3.png");
					} else {
						m.parseCommand(currentLevel, "Platform Custom 338 768 edge1inv.png");
						m.parseCommand(currentLevel, "Platform Custom -86 768 customFloor3.png");
					}

					switch (index) {
						case 1:

							m.parseCommand(currentLevel, "Area 0 -384 ".concat(level2));
							m.parseCommand(currentLevel, "Area 426 -384 ".concat(oplevel2));
							m.parseCommand(currentLevel, "Area 0 0 ".concat(level1));
							m.parseCommand(currentLevel, "Area 0 384 ".concat(level0));
							break;
						case 2:

							m.parseCommand(currentLevel, "Area 0 -384 ".concat(level3));
							m.parseCommand(currentLevel, "Area 426 -384 ".concat(oplevel3));
							m.parseCommand(currentLevel, "Area 0 0 ".concat(level2));
							m.parseCommand(currentLevel, "Area 0 384 ".concat(level1));
							m.parseCommand(currentLevel, "Area 0 768 ".concat(level0));
							break;
					}

					if (index != 0)
						index--;
					if (!half)
						half = true;
					else
						half = false;

				} else if (segments1.get(rnd1).equals("intersegmentA2") || segments1.get(rnd1).equals("intersegmentA1")) {
					if (index == 0) {
						m.parseCommand(currentLevel, "Platform Custom -86 384 customFloor3.png");
						m.parseCommand(currentLevel, "Platform Custom 340 384 customFloor3.png");
					} else {
						m.parseCommand(currentLevel, "Platform Custom 54 384 edge1.png");
						m.parseCommand(currentLevel, "Platform Custom 338 384 edge1inv.png");

					}

					switch (index) {
						case 0:

							m.parseCommand(currentLevel, "Area 0 -384 ".concat(level1));
							m.parseCommand(currentLevel, "Area 0 0 ".concat(level0));
							break;
						case 1:

							m.parseCommand(currentLevel, "Area 0 -384 ".concat(level2));
							m.parseCommand(currentLevel, "Area 0 0 ".concat(level1));
							m.parseCommand(currentLevel, "Area 0 384 ".concat(level0));
							break;
						case 2:

							m.parseCommand(currentLevel, "Area 0 -384 ".concat(level3));
							m.parseCommand(currentLevel, "Area 0 0 ".concat(level2));
							m.parseCommand(currentLevel, "Area 0 384 ".concat(level1));
							m.parseCommand(currentLevel, "Area 0 768 ".concat(level0));
							break;
					}

					if (!half)
						half = true;
					else
						half = false;


				} else {
					switch (index) {
						case 0:

							m.parseCommand(currentLevel, "Area 0 0 ".concat(level0));
							m.parseCommand(currentLevel, "Area 0 -384 ".concat(level1));
							m.parseCommand(currentLevel, "Area 426 0 ".concat(oplevel0));
							m.parseCommand(currentLevel, "Area 426 -384 ".concat(oplevel1));
							break;
						case 1:

							m.parseCommand(currentLevel, "Area 0 -384 ".concat(level2));
							m.parseCommand(currentLevel, "Area 426 -384 ".concat(oplevel2));
							m.parseCommand(currentLevel, "Area 0 0 ".concat(level1));
							m.parseCommand(currentLevel, "Area 426 0 ".concat(oplevel1));
							m.parseCommand(currentLevel, "Area 0 384 ".concat(level0));
							m.parseCommand(currentLevel, "Area 426 384 ".concat(oplevel0));


							m.parseCommand(currentLevel, "Area 0 384 pillars.png");
							m.parseCommand(currentLevel, "Platform Custom 0 768 floorA.png");
							m.parseCommand(currentLevel, "Area 426 384 pillars.png");
							m.parseCommand(currentLevel, "Platform Custom 426 768 floorA.png");

							break;
						case 2:

							m.parseCommand(currentLevel, "Area 0 -384 ".concat(level3));
							m.parseCommand(currentLevel, "Area 426 -384 ".concat(oplevel3));
							m.parseCommand(currentLevel, "Area 0 0 ".concat(level2));
							m.parseCommand(currentLevel, "Area 426 0 ".concat(oplevel2));
							m.parseCommand(currentLevel, "Area 0 384 ".concat(level1));
							m.parseCommand(currentLevel, "Area 426 384 ".concat(oplevel1));

							m.parseCommand(currentLevel, "Area 0 768 ".concat(level0));
							m.parseCommand(currentLevel, "Area 0 768 pillars.png");
							m.parseCommand(currentLevel, "Area 426 768 ".concat(oplevel0));
							m.parseCommand(currentLevel, "Area 426 768 pillars.png");



							m.parseCommand(currentLevel, "Area 0 384 pillars.png");
							m.parseCommand(currentLevel, "Area 426 384 pillars.png");
							m.parseCommand(currentLevel, "Platform Custom 0 768 floorA.png");
							m.parseCommand(currentLevel, "Platform Custom 426 768 floorA.png");
							break;
					}
				}


				m.parseCommand(currentLevel, "Revert");


				m.mapParser(currentLevel, segments1.get(rnd1));
				segments1.remove(rnd1);


			}

			randomGenerate(m,intro2);
			randomGenerate(m,segments2);
			randomGenerate(m,throneRoom);


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

	public void randomGenerate(Map m,ArrayList<String> mapPool) {


		int rnd1;

		while (!mapPool.isEmpty()) {

			m.parseCommand(currentLevel, "Chunk 1 E asdgasdg.png");

			rnd1 = new Random().nextInt(mapPool.size());

			File myObj = new File("./src/game/segments/".concat(mapPool.get(rnd1)).concat(".txt"));
			Scanner myReader;
			try {
				myReader = new Scanner(myObj);
				String data = myReader.nextLine();
				String[] splix = data.split("\\s+");
				if (Integer.parseInt(splix[8]) == 2) {
					if (half) {
						m.parseCommand(currentLevel, "Area 0 0 ground2.png");
						m.parseCommand(currentLevel, "Area 426 0 ground1.png");

						m.parseCommand(currentLevel, "Area 0 -384 sky2.png");
						m.parseCommand(currentLevel, "Area 426 -384 sky1.png");
					} else {
						m.parseCommand(currentLevel, "Area 0 0 ground1.png");
						m.parseCommand(currentLevel, "Area 426 0 ground2.png");

						m.parseCommand(currentLevel, "Area 0 -384 sky1.png");
						m.parseCommand(currentLevel, "Area 426 -384 sky2.png");
					}


				} else if (Integer.parseInt(splix[8]) == 1) {
					if (half) {
						m.parseCommand(currentLevel, "Area 0 -384 sky2.png");
						m.parseCommand(currentLevel, "Area 0 0 ground2.png");

					} else {
						m.parseCommand(currentLevel, "Area 0 -384 sky1.png");
						m.parseCommand(currentLevel, "Area 0 0 ground1.png");
					}


					if (!half)
						half = true;
					else
						half = false;

				}


			} catch (FileNotFoundException e) {
			}


			m.parseCommand(currentLevel, "Revert");

			m.mapParser(currentLevel, mapPool.get(rnd1));
			mapPool.remove(rnd1);

		}
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
