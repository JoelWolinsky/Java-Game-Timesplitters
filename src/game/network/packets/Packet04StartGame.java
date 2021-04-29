package game.network.packets;

import java.util.Arrays;

import game.graphics.AnimationStates;
import network.GameClient;
import network.GameServer;

public class Packet04StartGame extends Packet {

	
	public Packet04StartGame(byte[] data) {
		super(04);
	
	}
	
	@Override
	public void writeData(GameClient client) {
		//System.out.println("packet02 write to client");
		client.sendData(getData());
	}

	@Override
	public void writeData(GameServer server) {
		//System.out.println("packet02 write data send to all");
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("04").getBytes();
	}

	
}
