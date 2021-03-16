package game.network.packets;

import network.GameClient;
import network.GameServer;

public class Packet00Login extends Packet {

	private String username;
	private float x,y;
	private String[] urls;
	
	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = readData(data).split(",");
		
		System.out.println(readData(data));
		this.username = dataArray[0];
		this.x = Float.parseFloat(dataArray[1]);
		this.y = Float.parseFloat(dataArray[2]);
		this.urls = new String[]{dataArray[3], dataArray[4], dataArray[5]};
		// TODO Auto-generated constructor stub
	}
	
	public Packet00Login(String username, float x, float y, String...urls) {
		super(00);
		this.username = username;
		this.x = x;
		this.y = y;
		this.urls = urls;
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
		//System.out.println(("00" + this.username+","+getX()+","+getY()+getURLs()));
		return ("00" + this.username+","+getX()+","+getY()+getURLs()).getBytes();
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
	
	public String[] getURLs() {
		return urls;
	}
}
