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
	public static Game game;
	boolean half = false;
	public static Player player;
	public static AIPlayer aiPlayer;
	public static Camera camera;
	int index = 0;
	int stateIndex=0;
	int bottom;
	int top;
	BackgroundController ctrlr;
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
		String mapMode = "RNG";
		Map m = new Map();
		//keep default for now untwil we sort randomly generated
		if (mapMode.equals("default")) {

/*

			m.mapParser(currentLevel, "segmentA1");			// basic segment
			m.mapParser(currentLevel, "intersegmentA1"); 	// falling objects - hard for AI
			m.mapParser(currentLevel, "segmentA2");			// electric one
			m.mapParser(currentLevel, "intersegmentA2");	// falling rocks
			m.mapParser(currentLevel, "segmentA3");			// aesthetic hall 1
			m.mapParser(currentLevel, "segmentA4");			// aesthetic hall 2
			m.mapParser(currentLevel, "segmentA5");			// aesthetic hall 3

			m.mapParser(currentLevel, "intersegmentA3");	// hands one
			m.mapParser(currentLevel, "segmentA6");			// ghosts
			m.mapParser(currentLevel, "segmentA7");			// platforms

			m.mapParser(currentLevel, "intersegmentA3");	// hands one
			m.mapParser(currentLevel, "segmentA8");			// disappearing long and small platforms
			m.mapParser(currentLevel, "intersegmentA3");	// hands one
			m.mapParser(currentLevel, "segmentA10");		// long corridor
			m.mapParser(currentLevel, "segmentA11");		// disappearing platforms over acid
			m.mapParser(currentLevel, "segmentA12");		// bookshelf pyramid
			m.mapParser(currentLevel, "segmentA13");		// crushing bookshelves
			m.mapParser(currentLevel, "intersegmentA3");
			m.mapParser(currentLevel, "introDimension");
			m.mapParser(currentLevel, "segmentA14");



 */
		}
		else if (mapMode.equals("RNG")) {

			//create different segment pools for the different parts of the game
			//we want them separated in order to keep a specific order in our game
			ArrayList<String> segments1 = new ArrayList<String>(Arrays.asList("segmentA1","segmentA2","intersegmentA2","intersegmentA1","intersegmentA2up","intersegmentA2down","intersegmentA2up","intersegmentA2down"));
			ArrayList<String> segments2 = new ArrayList<String>(Arrays.asList("segmentA3", "segmentA4", "segmentA4"));
			ArrayList<String> intro2 = new ArrayList<String>(Arrays.asList("intro2"));
			ArrayList<String> throneRoom = new ArrayList<String>(Arrays.asList("segmentA5"));

			//load up the images that are going to be used for dynamic background generation onto individual levels
			//**it is important that all levels have the same amount of images
			BackgroundStates level0 = new BackgroundStates("ground1.png","ground2.png");
			BackgroundStates level1 = new BackgroundStates("sky1.png","sky2.png");
			BackgroundStates level2 = new BackgroundStates("sky3.png","sky4.png");
			BackgroundStates level3 = new BackgroundStates("sky5.png","sky6.png");

			//choose the limits of your map this should also be related to how many levels you have defined
			//eg. you don't want to have 20 levels but a top limit of only 2 since 17 of the leves will never be used
			bottom=0;
			top=2;

			//add levels to the background controller which manages the current position within the horizontal panorama
			ctrlr = new BackgroundController(level0,level1,level2,level3);

			//generate the segment pools
			m.mapParser(currentLevel, "intro1");
			randomGenerate(m,segments1);
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

	public void randomGenerate(Map m,ArrayList<String> segmentPool) {


		int rnd1;

		while (!segmentPool.isEmpty()) {

			//advance by 1 block
			m.parseCommand(currentLevel, "Chunk 1 E asdgasdg.png");
			//choose segment at random from segment pool
			rnd1 = new Random().nextInt(segmentPool.size());

			//if picked segment is not allowed when on top level or bottom level reroll till it's something valid
			//when the map is on level 0 we don't want to go further down
			if (index == bottom)
				while (segmentPool.get(rnd1).equals("intersegmentA2down")) {
					rnd1 = new Random().nextInt(segmentPool.size());
				}

			//when the map is on the top most level chosen we don't want to go further up
			if (index == top)
				while (segmentPool.get(rnd1).equals("intersegmentA2up")) {
					rnd1 = new Random().nextInt(segmentPool.size());
				}

			try {

				//initialize object to read first line of the picked segment
				File myObj = new File("./src/game/segments/".concat(segmentPool.get(rnd1)).concat(".txt"));
				Scanner myReader;
				myReader = new Scanner(myObj);
				String data = myReader.nextLine();
				String[] splix = data.split("\\s+");

				//check if segment is of size 1 or 2 or custom and generate background accordingly
				switch (splix[8]){

					//default 1 block size segment -- no level change
					case "1":

						if (index == 0) {
							m.parseCommand(currentLevel, "Platform Custom -86 384 customFloor3.png");
							m.parseCommand(currentLevel, "Platform Custom 340 384 customFloor3.png");
						}
						else {
							m.parseCommand(currentLevel, "Platform Custom 54 384 edge1.png");
							m.parseCommand(currentLevel, "Platform Custom 338 384 edge1inv.png");
						}

						m.parseCommand(currentLevel, "Area 0 -384 ".concat(ctrlr.getCurrent(index+1)));
						m.parseCommand(currentLevel, "Area 0 0 ".concat(ctrlr.getCurrent(index)));

						if (index>0)
						{
							m.parseCommand(currentLevel, "Area 0 384 ".concat(ctrlr.getCurrent(index-1)));
							if (index>1)
								m.parseCommand(currentLevel, "Area 0 768 ".concat(ctrlr.getCurrent(index-2)));
						}

						ctrlr.incrementStateIndex();
						break;

					//default 2 block size segment -- no level change
					case "2":

						m.parseCommand(currentLevel, "Area 0 -384 ".concat(ctrlr.getCurrent(index+1)));
						m.parseCommand(currentLevel, "Area 426 -384 ".concat(ctrlr.getNext(index+1)));

						m.parseCommand(currentLevel, "Area 0 0 ".concat(ctrlr.getCurrent(index)));
						m.parseCommand(currentLevel, "Area 426 0 ".concat(ctrlr.getNext(index)));

						if (index>0)
						{
							m.parseCommand(currentLevel, "Area 0 384 ".concat(ctrlr.getCurrent(index-1)));
							m.parseCommand(currentLevel, "Area 426 384 ".concat(ctrlr.getNext(index-1)));

							m.parseCommand(currentLevel, "Area 0 384 pillars.png");
							m.parseCommand(currentLevel, "Area 426 384 pillars.png");
							m.parseCommand(currentLevel, "Platform Custom 0 768 floorA.png");
							m.parseCommand(currentLevel, "Platform Custom 426 768 floorA.png");

							if (index>1)
							{
								m.parseCommand(currentLevel, "Area 0 768 ".concat(ctrlr.getCurrent(index-2)));
								m.parseCommand(currentLevel, "Area 426 768 ".concat(ctrlr.getNext(index-2)));

								m.parseCommand(currentLevel, "Area 0 768 pillars.png");
								m.parseCommand(currentLevel, "Area 426 768 pillars.png");

							}
						}

						ctrlr.incrementStateIndex();
						ctrlr.incrementStateIndex();
						break;

					case "Custom":

						//here you can define special behaviour for certain segments
						switch (splix[9]){

							//this segment elevates the level
							case "intersegmentA2up":
								if (index == 0) {
									m.parseCommand(currentLevel, "Platform Custom -86 384 customFloor3.png");
									m.parseCommand(currentLevel, "Platform Custom 340 384 customFloor3.png");
								} else {
									m.parseCommand(currentLevel, "Platform Custom 54 384 edge1.png");
									m.parseCommand(currentLevel, "Platform Custom 340 384 customFloor3.png");
								}

								if (index>0)
								{
									m.parseCommand(currentLevel, "Area 0 384 ".concat(ctrlr.getCurrent(index-1)));
									if (index>1)
										m.parseCommand(currentLevel, "Area 0 768 ".concat(ctrlr.getCurrent(index-2)));
								}

								m.parseCommand(currentLevel, "Area 0 0 ".concat(ctrlr.getCurrent(index)));
								m.parseCommand(currentLevel, "Area 0 -384 ".concat(ctrlr.getCurrent(index+1)));
								m.parseCommand(currentLevel, "Area 0 -768 ".concat(ctrlr.getCurrent(index+2)));
								m.parseCommand(currentLevel, "Area -426 -768 ".concat(ctrlr.getPrevious(index+2)));


								index++;

								ctrlr.incrementStateIndex();
								break;

							//this segment decreses elevation of the level
							case "intersegmentA2down":
								if (index == 1) {
									m.parseCommand(currentLevel, "Platform Custom -86 768 customFloor3.png");
									m.parseCommand(currentLevel, "Platform Custom 340 768 customFloor3.png");
								} else {
									m.parseCommand(currentLevel, "Platform Custom 338 768 edge1inv.png");
									m.parseCommand(currentLevel, "Platform Custom -86 768 customFloor3.png");
								}

								m.parseCommand(currentLevel, "Area 0 -384 ".concat(ctrlr.getCurrent(index+1)));
								m.parseCommand(currentLevel, "Area 426 -384 ".concat(ctrlr.getNext(index+1)));
								m.parseCommand(currentLevel, "Area 0 0 ".concat(ctrlr.getCurrent(index)));
								if (index>0)
								{
									m.parseCommand(currentLevel, "Area 0 384 ".concat(ctrlr.getCurrent(index-1)));
									if(index>1)
										m.parseCommand(currentLevel, "Area 0 768 ".concat(ctrlr.getCurrent(index-2)));
								}

								index--;

								ctrlr.incrementStateIndex();
								break;
						}
						break;
				}


			}
			catch (FileNotFoundException e)
			{
			}


			//revert the 1 block advancement at the start of the while loop
			m.parseCommand(currentLevel, "Revert");
			//draw the contents of the segment
			m.mapParser(currentLevel, segmentPool.get(rnd1));
			//rremove the segment from the segment pool
			segmentPool.remove(rnd1);

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
