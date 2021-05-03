package game.network.packets;

import game.entities.players.Player;
import network.GameClient;
import network.GameServer;

public class Packet01Item extends Packet {

	private String effect;
	private float x,y;
	private String username;
	
	/**
	 * Creates a login packet when given data.
	 * @param data The data for the packet.
	 */
	public Packet01Item(byte[] data) {
		super(01);
		String[] dataArray = readData(data).split(",");
		
		System.out.println(readData(data));
		this.x = Float.parseFloat(dataArray[0]);
		this.y = Float.parseFloat(dataArray[1]);
		this.username = dataArray[2];
		this.effect = dataArray[3];

	}
	/**
	 * Creates a login packet when given the parameters.
	 * @param username The username of the player joining.
	 * @param x The x coordinate of the joining player.
	 * @param y The y coordinate of the joining player.
	 */
	public Packet01Item(float x, float y, String username, String effect) {
		super(01);
		this.x = x;
		this.y = y;
		this.username = username;
		this.effect = effect;
		
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
		System.out.println("packet01 writedata");
		server.sendDataToAllClients(getData());
	}
	
	/** 
	 * Formats the data of the packet into byte form.
	 * @return The data in byte form.
	 */
	@Override
	public byte[] getData() {
		//System.out.println(("00" + this.username+","+getX()+","+getY()+getURLs()));
		return ("01" + getX() + "," + getY() + "," + this.username + "," + this.effect).getBytes();
	}

	public String getEffect() {
		return effect;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public String getUsername() {
		return username;
	}
}
