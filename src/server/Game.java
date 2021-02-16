package server;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.util.Timer;

import javax.swing.JOptionPane;

import client.KeyInput;
import client.MouseInput;
import client.Window;
import network.GameClient;
import network.GameServer;

/**
 *The main class. All setup is done here and global constants are held here. 
 */
public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = -8921419424614180143L;

	
	public static final int WIDTH_BORDER = 14;
	public static final int HEIGHT_BORDER = 37;
	public static final int WIDTH = 640;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final String TITLE = "Engine";
	public static Rectangle windowRect = new Rectangle(0,0,Game.WIDTH, Game.HEIGHT);
		
	private Thread thread;
	private boolean running = false;
	
	public static Handler handler = new Handler();
	public static KeyInput keyInput = new KeyInput();
	public static MouseInput mouseInput = new MouseInput();
	
	private GameClient socketClient;
	private GameServer socketServer;
	
	ExampleKeyListener e1;
	ExampleMouseListener e2;
	ExampleFloor e3,e4,e5;


	
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
		new Window(WIDTH+WIDTH_BORDER, HEIGHT+HEIGHT_BORDER, TITLE, this);
		this.addKeyListener(keyInput);
		this.addMouseListener(mouseInput);
		this.addMouseMotionListener(mouseInput);
		e1 = new ExampleKeyListener(100, 200, 2);
		e2 = new ExampleMouseListener();
		e3 = new ExampleFloor(0, 450, 2);
		e4 = new ExampleFloor(400, 420, 2);
		e5 = new ExampleFloor(-480, 320, 2);
		
		socketClient.sendData("ping".getBytes());

	}
	
	public static void main(String[] args){
		new Game();		
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
		this.requestFocus();
		
		if(JOptionPane.showConfirmDialog(this, "Do you want to start the server?") == 0) {
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
	
//	public void run() {
//		long t1 = System.currentTimeMillis();
//		long t2, delta;
//		int FPS = 30;
//		int desiredDelta = 1000/FPS;
//		int frames = 0;
//		long framesTimer = System.currentTimeMillis();
//		while(running) {
//			tick(1);
//			render();
//			frames ++;
//			t2 = System.currentTimeMillis();
//			delta = t2-t1;
//			if(delta > desiredDelta) {
//				System.out.println("Frame took " + delta + " ms, sleeping for " + (delta-desiredDelta) + " ms");
//				try {
//					Thread.sleep(delta-desiredDelta);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			if(System.currentTimeMillis()-framesTimer > 1000) {
//				framesTimer += 1000;
//				System.out.println("FPS: "+frames);
//				frames = 0;
//			}
//		}
//		stop();
//	}
	public void run() {
		int fps = 30;
		long frameTime = 1000 / fps;
		long currentTime = System.currentTimeMillis();
		long nextFrame;
		while(running) {
			nextFrame = currentTime + frameTime;
			tick();
			render();
			while(System.currentTimeMillis() <= nextFrame) {};
			currentTime = System.currentTimeMillis();
		}
		stop();
	}
}
