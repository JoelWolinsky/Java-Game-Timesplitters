package network;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.entities.players.PlayerMP;
import game.network.packets.Packet;
import game.network.packets.Packet.PacketTypes;
import game.network.packets.Packet00Login;
import game.network.packets.Packet01Item;
import game.network.packets.Packet02Move;
import game.network.packets.Packet03MoveWall;
import game.network.packets.Packet04StartGame;

public class GameServer extends Thread {
	private DatagramSocket socket;
	private Game game;
	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
	private List<String> playerIDs= new ArrayList<String>();
	
	/** 
	 * Opens the socket for client-server connections. 
	 * @param game The game that the server will run for.
	 */
	public GameServer(Game game) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(1331);
		} catch (SocketException e) {
			e.printStackTrace(); 
		}
	}
	
	/** 
	 * Initialises the server. 
	 * This will run continuously and will forward and packets received onto parsePacket.
	 */
	public void run() {
		while(true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("Exception receiving packet in server run");
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}
	
	/**
	 * Handles the different packets received from the client.
	 * @param data The data which contains the packet information.
	 * @param address The address the data is from.
	 * @param port The port the data is from.
	 */
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
				PlayerMP player = new PlayerMP (10, 0, address, port,"player1");
				player.setUsername(((Packet00Login) packet).getUsername());
				this.addConnection(player, (Packet00Login) packet);
				break;
			case ITEM:
				packet = new Packet01Item(data);
				this.handleItem((Packet01Item)packet);
				break;
			case MOVE:
				packet = new Packet02Move(data);
				this.handleMove((Packet02Move)packet);
				break;
			case MOVEWALL:
				packet = new Packet03MoveWall(data);
				this.handleMoveWall((Packet03MoveWall)packet);
				break;
			case STARTGAME:
				packet = new Packet04StartGame();
				this.handleStartGame((Packet04StartGame)packet);
			}
		}catch (Exception e) {
				System.out.println("Exception at GameServer.parsePacket");
				e.printStackTrace();
			}
		}

	

	private void handleItem(Packet01Item packet) {
		try {
			packet.writeData(this);
		} catch (Exception e) {
			System.out.println("Exception in GameServer.handleItem");
			e.printStackTrace();
		}		
	}

	/**
	 * Handles the StartGame packet.
	 * @param packet The StartGame packet.
	 */
	private void handleStartGame(Packet04StartGame packet) {
		try {
			packet.writeData(this);
		} catch (Exception e) {
			System.out.println("Exception in GameServer.handleStartGame");
			e.printStackTrace();
		}		
	}

	/**
	 * Handles adding new players joining on the server side.
	 * @param player The PlayerMP that is joining.
	 * @param packet The Login packet received.
	 */
	public void addConnection(PlayerMP player, Packet00Login packet) {
		System.out.println("Ser add con");
		System.out.println(packet.getUsername());
		try {
			boolean alreadyConnected = false;
			// For each player in the game
			for (String p : this.playerIDs) {
				System.out.println(this.connectedPlayers.size());
				// If player already in game
				if(packet.getUsername().equalsIgnoreCase(p)) {
					if(this.connectedPlayers.get(getPlayerMPIndex(p)).ipAddress == null) {
						this.connectedPlayers.get(getPlayerMPIndex(p)).ipAddress = player.ipAddress;
					}
					if(this.connectedPlayers.get(getPlayerMPIndex(p)).port == -1) {
						this.connectedPlayers.get(getPlayerMPIndex(p)).port = player.port;
					}
					alreadyConnected = true;
				} else {
					System.out.print("Adding and send \n");
					sendData(packet.getData(),this.connectedPlayers.get(getPlayerMPIndex(p)).ipAddress, this.connectedPlayers.get(getPlayerMPIndex(p)).port);
					
					Packet00Login packet1 = new Packet00Login(this.connectedPlayers.get(getPlayerMPIndex(p)).getUsername(), this.connectedPlayers.get(getPlayerMPIndex(p)).getX(), this.connectedPlayers.get(getPlayerMPIndex(p)).getY());
					sendData(packet1.getData(), player.ipAddress, player.port);
					

				}
			}
			System.out.println("already connected = " + alreadyConnected);
			if (!alreadyConnected) {
	
	            this.connectedPlayers.add(player);
	            this.playerIDs.add(packet.getUsername());
	            System.out.println("adding to ids " + packet.getUsername());
	            
	            System.out.println(this.connectedPlayers.size());
	        }
		} catch (Exception e) {
			System.out.println("Exception in GameServer.addConnection");
			e.printStackTrace();
		}

	}
	
	/** 
	 * Finds the index of a given player in the playerIDs list.
	 * @param username The username of the player.
	 * @return The index of the player.
	 */
	public int getPlayerMPIndex(String username) {
		try {
			int index = 0;
			for(String player : this.playerIDs) {
				if(player.equals(username)) {
					break;
				}
				index++;
			}
			return index;
		} catch (Exception e) {
			System.out.println("Exception in GameServer.getPlayerMPIndex");
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Sends data from the server to the client.
	 * @param data The data to be sent.
	 * @param ipAddress The address to send the data to.
	 * @param port The port to send the data to.
	 */
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		if(port != -1) {
			DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);

			try {
				this.socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sends the data to all connected players.
	 * @param data The data to be sent.
	 */
	public void sendDataToAllClients(byte[] data) {
		try {
			for (PlayerMP p : connectedPlayers) {
				if (p.port != -1) {
					sendData(data, p.ipAddress, p.port);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in GameServer.sendDataToAllClients");
			e.printStackTrace();
		}
	}
	
	/** 
	 * Handles a move packet received from the client.
	 * @param packet The packet received.
	 */
	private void handleMove(Packet02Move packet) {
		try {
				int index = getPlayerMPIndex(packet.getUsername());
				if(index >= this.connectedPlayers.size()) {
					index--;
				}
				this.connectedPlayers.get(index).setX(packet.getX());
				this.connectedPlayers.get(index).setY(packet.getY());
				packet.writeData(this);
		} catch (Exception e) {
			System.out.println("Exception in GameServer.handleMove");
			e.printStackTrace();
		}
	}
	/** 
	 * Handles a wall move packet received from the client.
	 * @param packet The packet received.
	 */
	private void handleMoveWall(Packet03MoveWall packet) {
		try {
			packet.writeData(this);
		} catch (Exception e) {
			System.out.println("Exception in GameServer.handleMove");
			e.printStackTrace();
		}
	}

}