package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import game.entities.MovingPlatform;
import game.entities.Platform;
import game.entities.Player;

import javax.imageio.ImageIO;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = -772676358550096683L;
	private Thread thread;
	private boolean running = false;

	public static KeyInput keyInput = new KeyInput();
	public static MouseInput mouseInput = new MouseInput();

	private int xOffset = 0, yOffset = 0;
	private int horizontalIndex = 0;
	private int verticalIndex = 0;
	private int newOffsetX = 0;
	private int newOffsetY = 0;
	private int t = 0;
	private String currentTheme="";

	//	private LinkedList<Level> levels = new LinkedList<>();
	private Level currentLevel = new Level();

	private Player player;

	private Camera camera;

	public Game() {
		player = new Player(0, 340);
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
				mapParser(426,384,currentLevel,mapUrl);
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
		currentLevel.tick();
		camera.tick();
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(4);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);




		currentLevel.render(g, camera.getXOffset(), camera.getYOffset()+140);

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
//				frames ++;


				if(System.currentTimeMillis() - timer >1000) {
					timer += 1000;
//					System.out.println(frames);
//					frames = 0;
				}
			}
		}
		stop();
	}

	public void mapParser(int masterOffsetX, int masterOffsetY, Level currentLevel, String url)
	{
		Chunk c = null;
		String goUrl ="";
		String texturePlatformDefault="";
		String texturePlatformInverted="";
		String textureFloor ="";
		String textureBackground="";

		try {
			File myObj = new File(url);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {

				String data = myReader.nextLine();
				String[] splited = data.split("\\s+");

				switch (splited[0]){

					case "Theme":
						if (Integer.parseInt(splited[2])!=masterOffsetX && Integer.parseInt(splited[3]) != masterOffsetY)
						{
							newOffsetX = Integer.parseInt(splited[2]);
							newOffsetY = Integer.parseInt(splited[3]);
							t = 1;
						}
						currentTheme = splited[1];
						texturePlatformDefault = splited[4];
						texturePlatformInverted = splited[5];
						textureFloor = splited[6];
						textureBackground = splited[7];
						break;

					case "Chunk":
						c = new Chunk(horizontalIndex,verticalIndex,newOffsetX,newOffsetY,splited[3]);
						currentLevel.addChunk(c);
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
						int width=0;
						int height=0;

						Platform p;

						switch (splited[1]){
							case "Default":
								width=32;
								height=16;
								goUrl=texturePlatformDefault;
								break;
							case "Inverted":
								width=16;
								height=32;
								goUrl = texturePlatformInverted;
								break;
							case "Custom":
								width= Integer.parseInt(splited[4]);
								height = Integer.parseInt(splited[5]);
								goUrl = splited[6];
								break;
						}

						p = new Platform(horizontalIndex + Integer.parseInt(splited[2]), verticalIndex + Integer.parseInt(splited[3]) , width	, height, goUrl);
						currentLevel.addPlatform(p);
						break;

					case "Floor":
						goUrl=textureFloor;
						p = new Platform(horizontalIndex , verticalIndex + newOffsetY , newOffsetX, 32, goUrl);
						currentLevel.addPlatform(p);
						break;
					/*
					case "Moving":
						MovingPlatform xd = new MovingPlatform(horizontalIndex + Integer.parseInt(splited[2]), verticalIndex + Integer.parseInt(splited[3]) , 32,16,20,true,1	,"./img/platformA.png");
						currentLevel.addPlatform(xd);
					*/
				}
				System.out.println(data);
			}
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
