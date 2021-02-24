package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import game.display.Window;
import game.entities.Platform;
import game.entities.Player;
import game.input.KeyInput;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = -772676358550096683L;
	private Thread thread;
	private boolean running = false;

	public static KeyInput keyInput = new KeyInput();
	public static MouseInput mouseInput = new MouseInput();

	
	public static GameState state = GameState.MainMenu;

	//	private LinkedList<Level> levels = new LinkedList<>();
	private Level currentLevel = new Level();

	private Player player;

	private Camera camera;

	/**
	 * Initialises game entities and objects that must appear at the start of the game
	 */
	public Game() {
		//make this as a player choice in the menu either MAP 1 or Randomly Generated
		String mapMode = "default";
		String mapUrl = "./src/game/map.txt";

		//keep default for now until we sort randomly generated
		if (mapMode.equals("default"))
			Map.mapParser(0,0, currentLevel,mapUrl);

			//WORK IN PROGRESS
		else if (mapMode.equals("randomlyGenerated"))
		{
			//set to 3 just for example
			for (int i =0 ;i<3;i++)
			{
				//pool of predefined segments
				String[] bruh = {"Mage","Sewer"};

				//pick random predefined segment
				Random r=new Random();
				int randomNumber=r.nextInt(bruh.length);
				System.out.println(bruh[randomNumber]);
				mapUrl = "./src/game/".concat(bruh[randomNumber]).concat(".txt");

				//generate the predefined segment
				Map.mapParser(0,0,currentLevel,mapUrl);
			}
		}
		
		//Initialise player character at 0,0 in the first chunk
		player = new Player(0, 0);
		Chunk c = currentLevel.firstChunk();
		c.addEntity(player);

		camera = new Camera();
		camera.addTarget(player);
		
		//This section should always be last
		new Window(this);
		this.addKeyListener(keyInput);
		this.addMouseListener(mouseInput);
		this.addMouseMotionListener(mouseInput);
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
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		//Rendering code happens here

		if(this.state == GameState.Playing) {
			g.setColor(new Color(100,100,100));
			g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
			currentLevel.render(g, camera.getXOffset(), camera.getYOffset()+120);
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
				delta--;
			}
			if(running) {
				render();


				if(System.currentTimeMillis() - timer >1000) {
					timer += 1000;
				}
			}
		}
		stop();
	}


	public synchronized void start() {
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
