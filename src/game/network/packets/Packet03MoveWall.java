package game.network.packets;

import java.util.Arrays;

import game.graphics.AnimationStates;
import network.GameClient;
import network.GameServer;

public class Packet03MoveWall extends Packet {

	private float x;
	
	/**
	 * Creates a move wall packet when given data.
	 * @param data The data for the packet.
	 */
	public Packet03MoveWall(byte[] data) {
		super(03);
		String[] dataArray = readData(data).split(",");
		this.x = Float.parseFloat(dataArray[1]);

	}
	/**
	 * Creates a move wall packet when given the x coordinate.
	 * @param x2 The x coordinate the wall has moved to.
	 */
	public Packet03MoveWall(float x2) {
		super(03);
		this.x = x2;

		
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
		return ("03,"+ this.x).getBytes();
	}

	public float getX() {
		return this.x;
	}
	
	
}
