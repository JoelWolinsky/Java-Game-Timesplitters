package game.network.packets;

import network.GameClient;
import network.GameServer;

public class Packet02Move extends Packet {

	private String username;
	private float x, y;
	
	public Packet02Move(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Float.parseFloat(dataArray[1]);
		this.y = Float.parseFloat(dataArray[2]);

	}
	
	public Packet02Move(String username, float x2, float y2) {
		super(02);
		this.username = username;
		this.x = x2;
		this.y = y2;
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
		return ("02" + this.username + "," + this.x + "," + this.y).getBytes();
	}

	public String getUsername() {
		return username;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
}
