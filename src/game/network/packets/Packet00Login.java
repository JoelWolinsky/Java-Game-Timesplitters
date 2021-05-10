package game.network.packets;

import network.GameClient;
import network.GameServer;

public class Packet00Login extends Packet {

	private String username;
	private float x,y;
	private String url;
	
	/**
	 * Creates a login packet when given data.
	 * @param data The data for the packet.
	 */
	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Float.parseFloat(dataArray[1]);
		this.y = Float.parseFloat(dataArray[2]);
		this.url = dataArray[3];
		

	}
	/**
	 * Creates a login packet when given the parameters.
	 * @param username The username of the player joining.
	 * @param x The x coordinate of the joining player.
	 * @param y The y coordinate of the joining player.
	 */
	public Packet00Login(String username, float x, float y, String url) {
		super(00);
		this.username = username;
		this.x = x;
		this.y = y;
		this.url = url;
		
	}

	/**
	 * Sends data to the client which will then be passed on to the server.
	 * @param client The client to send the data to.
	 */
	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}
	
	/**
	 * Sends data to all clients through the server.
	 * @param server The server to send the data to.
	 */
	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}
	
	/** 
	 * Formats the data of the packet into byte form.
	 * @return The data in byte form.
	 */
	@Override
	public byte[] getData() {
		return ("00" + this.username+","+getX()+","+getY()+","+this.url).getBytes();
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
	public String getUrl() {
		return this.url;
	}
}
