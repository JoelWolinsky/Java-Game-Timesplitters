package game.network.packets;

import network.GameClient;
import network.GameServer;

public class Packet00Login extends Packet {

	private String username;
	private float x,y;
	
	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Float.parseFloat(dataArray[1]);
		this.y = Float.parseFloat(dataArray[2]);
		// TODO Auto-generated constructor stub
	}
	
	public Packet00Login(String username, float x, float y) {
		super(00);
		this.username = username;
		this.x = x;
		this.y = y;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("00" + this.username+","+getX()+","+getY()).getBytes();
	}

	public String getUsername() {
		return username;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
}
