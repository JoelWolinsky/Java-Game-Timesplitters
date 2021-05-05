package game.entities.players;

import java.net.InetAddress;

import game.input.KeyInput;

public class PlayerMP extends Player {
	
	public InetAddress ipAddress;
	public int port;
	
	
	public PlayerMP(float x, float y, KeyInput input, InetAddress ipAddress, int port,String url) {
		super(x, y, input, 32, 64,url);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public PlayerMP(float x, float y, InetAddress ipAddress, int port,String url) {
		super(x, y, null, 32,64,url);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	@Override
	public void tick() {
		super.tick();
	}
	
}
