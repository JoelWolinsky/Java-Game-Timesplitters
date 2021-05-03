package game.network.packets;

import java.util.Arrays;

import game.graphics.AnimationStates;
import network.GameClient;
import network.GameServer;

public class Packet04StartGame extends Packet {

	/**
	 * Creates a login packet when given data.
	 */
	public Packet04StartGame() {
		super(04);
	
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
		return ("04").getBytes();
	}

	
}
