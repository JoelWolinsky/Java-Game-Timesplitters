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

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = -772676358550096683L;
	private Thread thread;
	private boolean running = false;

	public static KeyInput keyInput = new KeyInput();
	public static MouseInput mouseInput = new MouseInput();

	private int horizontalIndex = 0;
	private int verticalIndex = 0;
	private int newOffsetX = 0;
	private int newOffsetY = 0;
	private int t = 0;
	
	public static GameState state = GameState.MainMenu;

	//	private LinkedList<Level> levels = new LinkedList<>();
	private Level currentLevel = new Level();

	private Player player;

	private Camera camera;

	public Game() {
		player = new Player(0, 0);
		currentLevel.addEntity(player);

		//make this as a player choice in the menu either MAP 1 or Randomly Generated
		String mapMode = "default";
		String mapUrl = "./src/game/map.txt";

		//keep default for now until we sort randomly generated
		if (mapMode.equals("default"))
			mapParser(0,0, currentLevel,mapUrl);

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
				mapParser(0,0,currentLevel,mapUrl);
			}
		}

		camera = new Camera();
		camera.addTarget(player);
		new Window(this);
		this.addKeyListener(keyInput);
		this.addMouseListener(mouseInput);
		this.addMouseMotionListener(mouseInput);
	}

	private void tick() {
		if(this.state == GameState.Playing) {
			currentLevel.tick();
			camera.tick();
		}
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		

		if(this.state == GameState.Playing) {
			g.setColor(new Color(100,100,100));
			g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
			currentLevel.render(g, camera.getXOffset(), camera.getYOffset()+120);
		}else {
			g.setColor(new Color(255,255,255));
			g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		}

		g.dispose();
		bs.show();
	}

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

	public void mapParser(int masterOffsetX, int masterOffsetY, Level currentLevel, String url)
	{

		Platform p;
		Chunk c = new Chunk();

		try {
			File myObj = new File(url); //
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {

				String data = myReader.nextLine();
				String[] splited = data.split("\\s+");

				switch (splited[0]){
					case "Theme":
						newOffsetX = Integer.parseInt(splited[2]);
						newOffsetY = Integer.parseInt(splited[3]);
						t = 1;
						break;
					case "Chunk":
						currentLevel.addChunk(c);
						c = new Chunk();
						if (t==0) {
							masterOffsetX = newOffsetX;
							masterOffsetY = newOffsetY;
						}
						switch (splited[2]){
							case "E":
								horizontalIndex= horizontalIndex + masterOffsetX;
								break;
							case "W":
								horizontalIndex= horizontalIndex - masterOffsetX;
								break;
							case "N":
								verticalIndex = verticalIndex - masterOffsetY;
								break;
							case "S":
								verticalIndex = verticalIndex + masterOffsetY;
								break;
						}
						t--;
						break;
					case "Platform":
						System.out.println(masterOffsetX);
						System.out.println(newOffsetX);
						System.out.println(horizontalIndex);
						p = new Platform(horizontalIndex + Integer.parseInt(splited[1]), verticalIndex + Integer.parseInt(splited[2]) , Integer.parseInt(splited[3]), Integer.parseInt(splited[4]));
						c.addPlatform(p);
						break;
				}
				System.out.println(data);

			}
			currentLevel.addChunk(c);
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
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
