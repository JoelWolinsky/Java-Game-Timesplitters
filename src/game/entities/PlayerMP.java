package game.entities;

import java.net.InetAddress;

import game.KeyInput;
import game.Level;

public class PlayerMP extends Player {
	
	public InetAddress ipAddress;
	public int port;
	
	public PlayerMP(Level level, float x, float y, KeyInput input, String username, InetAddress ipAddress, int port) {
		super(level, x, y, input, username);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	
	public PlayerMP(Level level, float x, float y, String username, InetAddress ipAddress, int port) {
		super(level, x, y, null, username);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	@Override
	public void tick() {
		super.tick();
	}
	
}
