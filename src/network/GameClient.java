package network;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import game.Game;
import game.Level;
import game.UIController;
import game.entities.areas.AddedItem;
import game.entities.players.PlayerMP;
import game.graphics.LevelState;
import game.network.packets.Packet;
import game.network.packets.Packet.PacketTypes;
import game.network.packets.Packet00Login;
import game.network.packets.Packet01Item;
import game.network.packets.Packet02Move;
import game.network.packets.Packet03MoveWall;
import game.network.packets.Packet04StartGame;
import game.network.packets.Packet05Capacity;

import static game.Level.*;

public class GameClient extends Thread {
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Game game;

	/**
	 * Opens the socket for client-server connections.
	 * @param game The game that the server will run for.
	 * @param ipAddress The ipAddress to connect to.
	 */
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

	/**
	 * Initialises the client side to receive packets from the server.
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
	 * @param data The data which contains the packet information.
	 * @param address The address the data is from.
	 * @param port The port the data is from.
	 */
	private void parsePacket(byte[] data, InetAddress address, int port) {
		try {
			String message = new String(data).trim();
			PacketTypes type = Packet.lookupPacket(message.substring(0,2));
			Packet packet = null;
			if (getLevelState() != LevelState.Finished) {
				switch (type) {
				default:
				case INVALID:
					break;
				case LOGIN:
					System.out.println("Client handle login");
					packet = new Packet00Login(data);
					handleLogin((Packet00Login) packet, address, port);
					break;
				case ITEM:
					packet = new Packet01Item(data);
					this.handleItem((Packet01Item)packet);
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
					break;
				case STARTGAME:
					packet = new Packet04StartGame();
					handleStartGame((Packet04StartGame)packet);
					break;
				case CAPACITY:
					packet = new Packet05Capacity(data);
					handleCapacity((Packet05Capacity) packet);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in parsePacket on client. Data " + data + "\nAddress " + address + "\nPort " + port);
			e.printStackTrace();
		}
	}

	private void handleCapacity(Packet05Capacity packet) {
		this.game.m.currentLevel.setMultiplayerCapacity(packet.getCapacity());
		
	}
	
	/**
	 * Handles the handleItem packet.
	 * @param packet The handleItem packet.
	 */
	private void handleItem(Packet01Item packet) {
		System.out.println("client add item");
		System.out.println(packet.getX()+","+packet.getY());
		Level.getToBeAdded().add(new AddedItem(packet.getX(),packet.getY(), 0, 0, Level.getSpecificPlayerMP(packet.getUsername()), packet.getEffect()));
	}

	/**
	 * Handles the StartGame packet.
	 * @param packet The StartGame packet.
	 */
	private void handleStartGame(Packet04StartGame packet) {
		System.out.println("handleStartGame");
		setLevelState(LevelState.InProgress);

	}

	/*
	 * Sends data to the server.
	 * @param data The data to be sent.
	 */
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles a move packet received from the server.
	 * @param packet The packet received.
	 */
	private void handleMove(Packet02Move packet) {
		if (!Game.player.getUsername().equals(packet.getUsername())) {
			try {
				this.game.m.currentLevel.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getDirection());
			} catch (Exception e) {
				System.out.println("Exception in handleMove. Packet " + packet);
				e.printStackTrace();
			}
		}

	}

	/**
	 * Handles a wall move packet received from the server.
	 * @param packet The packet received.
	 */
	private void handleMove(Packet03MoveWall packet) {
		if (Game.socketServer == null) {
			try {
				this.game.m.currentLevel.moveWall(packet.getX());
			} catch (Exception e) {
				System.out.println("Exception in handleMove. Packet " + packet);
				e.printStackTrace();
			}
		} else {
		}

	}

	/**
	 * Handles adding new players to the clients game.
	 * @param packet The Login packet received.
	 * @param address The address of the joining player.
	 * @param port The port of the joining player.
	 */
	private void handleLogin(Packet00Login packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + (packet).getUsername() + " has joined the game...");
		PlayerMP player = new PlayerMP (0, 350, address, port,"player2");
		player.setUsername((packet).getUsername());

		Level.addToAddQueue(player);
	}


}
