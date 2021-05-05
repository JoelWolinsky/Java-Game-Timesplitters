

package game.network.packets;

import java.util.Arrays;

import game.graphics.AnimationStates;
import network.GameClient;
import network.GameServer;

public class Packet05Capacity extends Packet {

	/**
	 * Creates a capacity packet when given data.
	 * @param data The data for the packet.
	 */
	private int capacity;
	public Packet05Capacity(byte[] data) {
		super(05);
		
		String[] dataArray = readData(data).split(",");
		
		System.out.println(readData(data));
		this.capacity = Integer.parseInt(dataArray[0]);
	
	}
	/**
	 * Creates a login packet when given the parameters.
	 * @param capacity The player capacity of the game.
	 */
	public Packet05Capacity(int capacity) {
		super(05);
		this.capacity = capacity;

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
		return ("05" + this.capacity).getBytes();
	}
	
	public int getCapacity() {
		return this.capacity;
	}

	
}
