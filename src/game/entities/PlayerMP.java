package game.entities;

import java.net.InetAddress;

import game.Handler;

public class PlayerMP {
	
	public InetAddress ipAddress;
	public int port;
	
	public PlayerMP(int x, int y, Handler input, String username, InetAddress ipAddress, int port) {
		//construct
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public PlayerMP(int x, int y, String username, InetAddress ipAddress, int port) {
		//constuct
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public void tick() {
		//tick
	}
}
