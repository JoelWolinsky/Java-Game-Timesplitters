package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.entities.ExampleKeyListener2;
import game.entities.Platform;
import game.entities.Player;
import game.entities.PlayerMP;
import game.network.packets.Packet00Login;
import network.GameClient;
import network.GameServer;

public class Game extends Canvas implements Runnable{
	
	private Thread thread;
	private boolean running = false;
	
	public static KeyInput keyInput = new KeyInput();
	public static MouseInput mouseInput = new MouseInput();
	public static Game game;
	
	private int xOffset = 0, yOffset = 0;
	
	private LinkedList<Level> levels = new LinkedList<>();
	public Level currentLevel = new Level();
	
	public Player player;
	
	public GameClient socketClient;
	public GameServer socketServer;
	
	
	//public JFrame frame;
	
	//public WindowHandler windowHandler;
	
	

	
	public Game() {
		game = this;
		new Window(this);
		this.addKeyListener(keyInput);
		this.addMouseListener(mouseInput);
		this.addMouseMotionListener(mouseInput);
		
		
		player = new PlayerMP(this.currentLevel, 300, 300, keyInput, JOptionPane.showInputDialog(this, "Please enter a username"), null, -1);
		currentLevel.addEntity(player);
		
		
		Platform p = new Platform(currentLevel, 0, Window.HEIGHT-32, Window.WIDTH, 32);
		currentLevel.addPlatform(p);
		p = new Platform(currentLevel, 100, Window.HEIGHT-96, 150, 32);
		currentLevel.addPlatform(p);
		
		Packet00Login loginPacket = new Packet00Login(player.getUsername());
		
		if(socketServer != null) {
			socketServer.addConnection((PlayerMP) player, loginPacket);
		}
		//socketClient.sendData("ping".getBytes());
		
		loginPacket.writeData(socketClient);
		//windowHandler = new WindowHandler(this);
	}
	
	private void tick() {
		currentLevel.tick();
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(new Color(100,100,100));
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		
		currentLevel.render(g, xOffset, yOffset);
		
		g.dispose();
		bs.show();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
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
				frames ++;
				
			
				if(System.currentTimeMillis() - timer >1000) {
					timer += 1000;
//					System.out.println(frames);
					frames = 0;
				}
			}
		}
		stop();		
	}

	
//	public void run() {
//		int fps = 30;
//		long frameTime = 1000000000 / fps;
//		long currentTime = System.nanoTime();
//		long nextFrame;
//		while(running) {
//			nextFrame = currentTime + frameTime;
//			tick();
//			render();
//			while(System.nanoTime() <= nextFrame) {};
//			currentTime = System.nanoTime();
//		}
//		stop();	   
//	}
	
	public synchronized void start() {
		//The server is started in this function
		thread = new Thread(this);
		thread.start();
		running = true;
		this.requestFocus();
		
		if (JOptionPane.showConfirmDialog(this,"Do you want to run the server") == 0) {
			socketServer = new GameServer(this);
			socketServer.start();
		}
		
		socketClient = new GameClient(this, "localhost");
		socketClient.start();
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
