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

	private int horizontalIndex = 0;
	private int verticalIndex = 0;
	private int setX = 426;
	private int setY = 384;
	private String lastDirection="";
	private String currentTheme="A";

	//	private LinkedList<Level> levels = new LinkedList<>();
	private Level currentLevel = new Level();

	private Player player;

	private Camera camera;

	public Game() {
		player = new Player(0, 340,0,0,"./img/adventurer-idle-00.png", "./img/adventurer-idle-01.png", "./img/adventurer-idle-02.png");
		currentLevel.addEntity(player);

		//make this as a player choice in the menu either MAP 1 or Randomly Generated
		String mapMode = "default";
		String mapUrl = "./src/game/intersegmentA2.txt";

		//keep default for now until we sort randomly generated
		if (mapMode.equals("default")) {

			mapParser(currentLevel, "./src/game/intersegmentA2.txt");


		}	//WORK IN PROGRESS
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


		//System.out.println("X:" + player.getX()+ "Y:"+ player.getY());
		//System.out.println("X:" + camera.getXOffset()+ "Y:"+ camera.getYOffset());
		currentLevel.render(g, camera.getXOffset(), camera.getYOffset()+100,player.getX(),player.getY());

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

	public void mapParser(Level currentLevel, String url)
	{
		Chunk c;
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
						if (!splited[1].equals(currentTheme))
						{
							setX = Integer.parseInt(splited[2]);
							setY = Integer.parseInt(splited[3]);
							currentTheme = splited[1];
						}
						texturePlatformDefault = splited[4];
						texturePlatformInverted = splited[5];
						textureFloor = splited[6];
						textureBackground = splited[7];
						break;

					case "Chunk":
						lastDirection= splited[2];
						switch (splited[2]){
							case "E":
								horizontalIndex= horizontalIndex + setX;
								break;
							case "W":
								horizontalIndex= horizontalIndex - setX;
								break;
							case "N":
								verticalIndex = verticalIndex - setY;
								break;
							case "S":
								verticalIndex = verticalIndex + setY;
								break;
						}
						c = new Chunk(horizontalIndex-setX,verticalIndex,setX,setY,splited[3]);
						currentLevel.addChunk(c);

						break;

					case "Platform":
						Platform p;

						switch (splited[1]){
							case "Default":
								goUrl = texturePlatformDefault;
								break;
							case "Inverted":
								goUrl = texturePlatformInverted;
								break;
							case "Custom":
								goUrl = splited[4];
								break;
						}

						p = new Platform(horizontalIndex-setX+ Integer.parseInt(splited[2]), verticalIndex + Integer.parseInt(splited[3]) , 0, 0, goUrl);
						currentLevel.addPlatform(p);
						break;

					case "Floor":
						goUrl=textureFloor;
						p = new Platform(horizontalIndex-setX , verticalIndex +setY , setX, 0, goUrl);
						currentLevel.addPlatform(p);
						break;
					/*
					case "Moving":
						MovingPlatform xd = new MovingPlatform(horizontalIndex + Integer.parseInt(splited[2]), verticalIndex + Integer.parseInt(splited[3]) , 32,16,20,true,1	,"./img/platformA.png");
						currentLevel.addPlatform(xd);
					*/
					case "Revert":
						switch (lastDirection)
						{
							case "E":
								horizontalIndex= horizontalIndex - setX;
								break;
							case "W":
								horizontalIndex= horizontalIndex + setX;
								break;
							case "N":
								verticalIndex = verticalIndex + setY;
								break;
							case "S":
								verticalIndex = verticalIndex - setY;
								break;

						}
						break;
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
