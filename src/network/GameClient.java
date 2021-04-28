package network;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import game.Game;
import game.Level;
import game.entities.players.PlayerMP;
import game.network.packets.Packet;
import game.network.packets.Packet.PacketTypes;
import game.network.packets.Packet00Login;
import game.network.packets.Packet01Disconnect;
import game.network.packets.Packet02Move;
import game.network.packets.Packet03MoveWall;

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
	
	/* 
	 * Open the socket to reveive packets from the server.
	 */
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
	   
	/* 
	 * Handles any packets received from the server.
	 */
	private void parsePacket(byte[] data, InetAddress address, int port) {
		try {
			//System.out.println("client parse");
			String message = new String(data).trim();
			PacketTypes type = Packet.lookupPacket(message.substring(0,2));
			Packet packet = null;
			switch (type) {
			default:
			case INVALID:
				break;
			case LOGIN:
				System.out.println("Client handle login");
				packet = new Packet00Login(data);
				handleLogin((Packet00Login) packet, address, port);
				break;
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
				System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect) packet).getUsername() + " has left the world...");
				game.m.currentLevel.removePlayerMP(((Packet01Disconnect) packet).getUsername());
				break;
			case MOVE:
				//System.out.println("client handle move");
				packet = new Packet02Move(data);
				handleMove((Packet02Move)packet);
				break;
			case MOVEWALL:
				//System.out.println("receive wall packet");
				packet = new Packet03MoveWall(data);
				handleMove((Packet03MoveWall)packet);
			}	
		} catch (Exception e) {
			System.out.println("Exception in parsePacket on client. Data " + data + "\nAddress " + address + "\nPort " + port);
			e.printStackTrace();
		}
	}
	
	/* 
	 * Sends data to the server.
	 */
	public void sendData(byte[] data) {
		//System.out.println("client send");
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* 
	 * Handles the move packet if the user moves.
	 */
	private void handleMove(Packet02Move packet) {
		if (Game.player.getUsername().equals(packet.getUsername())) {
			// 
		} else {
			//System.out.println(Game.player.getUsername() +" Client handle move " + packet.getUsername());

			try {
				this.game.m.currentLevel.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getDirection());
			} catch (Exception e) {
				System.out.println("Exception in handleMove. Packet " + packet);
				e.printStackTrace();
			}
		}
		
	}
	
	private void handleMove(Packet03MoveWall packet) {
		if (Game.socketServer == null) {
			try {
				this.game.m.currentLevel.moveWall(packet.getX());
			} catch (Exception e) {
				System.out.println("Exception in handleMove. Packet " + packet);
				e.printStackTrace();
			}
		} else {
			//System.out.println("Server != null");
		}
		
	}

	/*
	 * Handles the user login packet.
	 */
	private void handleLogin(Packet00Login packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + (packet).getUsername() + " has joined the game...");
		PlayerMP player = new PlayerMP (0, 340, address, port,"player2");
		player.setUsername((packet).getUsername());

		Level.addToAddQueue(player);
	}
	

}