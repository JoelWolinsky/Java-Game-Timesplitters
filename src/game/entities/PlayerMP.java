package game.entities;

import java.net.InetAddress;

import game.Game;
import game.Level;
import game.input.KeyInput;
import game.network.packets.Packet00Login;
public class PlayerMP extends Player {
	
	public InetAddress ipAddress;
	public int port;
	
	
	public PlayerMP(Level level, float x, float y, InetAddress ipAddress, int port) {
		super(x, y, 32, 64);
		this.ipAddress = ipAddress;
		this.port = port;
		//Packet00Login loginPacket = new Packet00Login(this.getUsername(), x, y);
		//]]System.out.println("new mp");
		//if(Game.socketServer != null) {
		//	Game.socketServer.addConnection(this, loginPacket);
		//}
		
		//loginPacket.writeData(Game.socketClient);
	}
	@Override
	public void tick() {
		super.tick();
	}
	
}
