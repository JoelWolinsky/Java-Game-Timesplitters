package game.network.packets;

import java.util.Arrays;

import game.graphics.AnimationStates;
import network.GameClient;
import network.GameServer;

public class Packet03MoveWall extends Packet {

	private float x;
	
	public Packet03MoveWall(byte[] data) {
		super(03);
		String[] dataArray = readData(data).split(",");
		this.x = Float.parseFloat(dataArray[1]);

	}
	
	public Packet03MoveWall(float x2) {
		super(03);
		this.x = x2;

		
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
		return ("03,"+ this.x).getBytes();
	}

	public float getX() {
		return this.x;
	}
	
	
}
