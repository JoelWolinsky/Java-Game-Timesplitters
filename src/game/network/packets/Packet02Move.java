package game.network.packets;

import java.util.Arrays;

import game.graphics.AnimationStates;
import network.GameClient;
import network.GameServer;

public class Packet02Move extends Packet {

	private String username;
	private float x, y;
	private String direction;
	
	/**
	 * Creates a move packet when given data.
	 * @param data The data for the packet.
	 */
	public Packet02Move(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Float.parseFloat(dataArray[1]);
		this.y = Float.parseFloat(dataArray[2]);
		this.direction = dataArray[3];

	}
	/**
	 * Creates a move packet when given the parameters.
	 * @param username The username of the player moving.
	 * @param x The x coordinate of the moving player.
	 * @param y The y coordinate of the moving player.
	 */
	public Packet02Move(String username, float x2, float y2, String currentDirection) {
		super(02);
		this.username = username;
		this.x = x2;
		this.y = y2;
		this.direction = currentDirection;
		
	}
	
	/**
	 * Sends data to the client which will then be passed on to the server.
	 * @param client The client to send the data to.
	 */
	@Override
	public void writeData(GameClient client) {
		//System.out.println("packet02 write to client");
		client.sendData(getData());
	}
	
	/**
	 * Sends data to all clients through the server.
	 * @param server The server to send the data to.
	 */
	@Override
	public void writeData(GameServer server) {
		//System.out.println("packet02 write data send to all");
		server.sendDataToAllClients(getData());
	}
	
	/** 
	 * Formats the data of the packet into byte form.
	 * @return The data in byte form.
	 */
	@Override
	public byte[] getData() {
		return ("02" + this.username + "," + this.x + "," + this.y + "," + this.direction).getBytes();
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
	public String getDirection() {
		return this.direction;
	}
}
