package network;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import game.Game;
import game.entities.PlayerMP;
import game.network.packets.Packet;
import game.network.packets.Packet.PacketTypes;
import game.network.packets.Packet00Login;
import game.network.packets.Packet01Disconnect;
import game.network.packets.Packet02Move;

public class GameClient extends Thread {
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Game game;
	
	public GameClient(Game game, String ipAddress) {
		this.game = game;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
   public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {

                socket.receive(packet);

            } catch (IOException e) {
            	System.out.println("Exception receiving packet in client run");
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }
	   
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		try {
			String message = new String(data).trim();
			PacketTypes type = Packet.lookupPacket(message.substring(0,2));
			Packet packet = null;
			switch (type) {
			default:
			case INVALID:
				break;
			case LOGIN:
	
				packet = new Packet00Login(data);
				handleLogin((Packet00Login) packet, address, port);
				break;
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
				System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect) packet).getUsername() + " has left the world...");
				game.currentLevel.removePlayerMP(((Packet01Disconnect) packet).getUsername());
				break;
			case MOVE:
				packet = new Packet02Move(data);
				handleMove((Packet02Move)packet);
			}	
		} catch (Exception e) {
			System.out.println("Exception in parsePacket on client. Data " + data + "\nAddress " + address + "\nPort " + port);
			e.printStackTrace();
		}
	}
	
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleMove(Packet02Move packet) {
		try {
			this.game.currentLevel.movePlayer(packet.getUsername(), packet.getX(), packet.getY());
		} catch (Exception e) {
			System.out.println("Exception in handleMove. Packet " + packet);
			e.printStackTrace();
		}
	}

	
	private void handleLogin(Packet00Login packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + (packet).getUsername() + " has joined the game...");
		PlayerMP player = new PlayerMP (game.currentLevel, packet.getX(), packet.getY(), address, port);
		game.currentLevel.addEntity(player);
	}
	

}