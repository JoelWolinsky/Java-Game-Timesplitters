package server;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.util.Timer;

import client.KeyInput;
import client.MouseInput;
import client.Window;

/**
 *The main class. All setup is done here and global constants are held here. 
 */
public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = -8921419424614180143L;

	

	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final String TITLE = "Engine";
	public static Rectangle windowRect = new Rectangle(0,0,Game.WIDTH, Game.HEIGHT);
		
	private Thread thread;
	private boolean running = false;
	
	public static Handler handler = new Handler();
	public static KeyInput keyInput = new KeyInput();
	public static MouseInput mouseInput = new MouseInput();
	
	public Player player;
	public FloorTile floor;
	
	/**
	 * For everything that you want performed every frame.<br>
	 * Try to abstract everything to class-level tick() functions, rather than here
	 * @param delta The delta time generated in the run function
	 */
	private void tick() {
		handler.tick();
	}
	
	/**
	 * Primary function is to clear the screen, and then call class-level render functions via the handler
	 */
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		handler.render(g);
		
		g.dispose();
		bs.show();
	}
	
	/**
	 * Game constructor.<br>
	 * Constructors should be called here
	 */
	public Game() {
		new Window(WIDTH, HEIGHT, TITLE, this);
		this.addKeyListener(keyInput);
		this.addMouseListener(mouseInput);
		this.addMouseMotionListener(mouseInput);
		player = new Player(100,100);
		floor = new FloorTile(0, Game.HEIGHT-32, Game.WIDTH, 32);
	}
	
	public static void main(String[] args){
		new Game();		
	}
	
	public synchronized void start() {
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
	
//	public void run() {
//		long t1 = System.currentTimeMillis();
//		long t2, delta;
//		int FPS = 30;
//		int desiredDelta = 1000/FPS;
//		int frames = 0;
//		long framesTimer = System.currentTimeMillis();
//		while(running) {
//			tick();
//			render();
//		}
//		stop();
//	}
	public void run() {
		int fps = 30;
		long frameTime = 1000000000 / fps;
		long currentTime = System.nanoTime();
		long nextFrame;
		while(running) {
			nextFrame = currentTime + frameTime;
			tick();
			render();
			while(System.nanoTime() <= nextFrame) {};
			currentTime = System.nanoTime();
		}
		stop();	   
	}
}
