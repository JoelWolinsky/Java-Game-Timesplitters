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
import game.network.packets.Packet01Disconnect;
import game.network.packets.Packet02Move;
import game.network.packets.Packet03MoveWall;
import game.network.packets.Packet04StartGame;

public class GameServer extends Thread {
	private DatagramSocket socket;
	private Game game;
	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
	private List<String> playerIDs= new ArrayList<String>();
	
	/** 
	 * Initialises the socket connection 
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
	 * Initialises the server. This will run continuously and will forward and packets received onto parsePacket.
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
				System.out.println("Server handle login");
				packet = new Packet00Login(data);
				System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet00Login) packet).getUsername() + " has connected...");
				System.out.println(connectedPlayers.size());
				//System.out.println("Add con1");

				PlayerMP player = new PlayerMP (100, 100, address, port,"player1");
				System.out.println("username before: " + player.getUsername());
				player.setUsername(((Packet00Login) packet).getUsername());
				System.out.println("username after: " + player.getUsername());
				//System.out.println("Add con2");
				this.addConnection(player, (Packet00Login) packet);
				System.out.println("Add con3");

				break;
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
				System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect) packet).getUsername() + " has left...");
				this.removeConnection((Packet01Disconnect) packet);
				break;
			case MOVE:
				packet = new Packet02Move(data);
				//System.out.println(((Packet02Move)packet).getUsername() + " has moved to " + ((Packet02Move)packet).getX() + ", " + ((Packet02Move)packet).getY());
				this.handleMove((Packet02Move)packet);
				break;
			case MOVEWALL:
				//System.out.println("send wall packet");

				packet = new Packet03MoveWall(data);
				this.handleMoveWall((Packet03MoveWall)packet);
				break;
			case STARTGAME:
				packet = new Packet04StartGame(data);
				this.handleStartGame((Packet04StartGame)packet);
			}
		}catch (Exception e) {
				System.out.println("Exception at GameServer.parsePacket");
				e.printStackTrace();
			}
		}

	

	
	private void handleStartGame(Packet04StartGame packet) {
		try {
			packet.writeData(this);
		} catch (Exception e) {
			System.out.println("Exception in GameServer.handleStartGame");
			e.printStackTrace();
		}		
	}

	/** 
	 * Adds new players to the game.
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
	
	public void removeConnection(Packet01Disconnect packet) {
		try {
			//PlayerMP player = getPlayerMP(packet.getUsername());
			//this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
			//this.playerIDs.remove(getPlayerMPIndex(packet.getUsername()));
			//packet.writeData(this);
		} catch (Exception e) {
			System.out.println("Exception in GameServer.removeConnection");
			e.printStackTrace();
		}
	}
	
	
	public String getPlayerMP(String username) {
		try {
			for(String player : this.playerIDs) {
				if(player.equals(username)) {
					return player;
				}
			}
			return null;
		} catch (Exception e) {
			System.out.println("Exception in GameServer.getPlayerMP");
			e.printStackTrace();
			return null;
		}
	}
	
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
	 * Sends packets from the server to a client. 
	 */
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		//System.out.println("server send packet");
		if(port != -1) {
			DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);

			try {
				this.socket.send(packet);
				//System.out.println("server packet sent to " + ipAddress +":"+port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 * Sends data to every user logged in by calling sendData for each user.
	 */
	public void sendDataToAllClients(byte[] data) {
		//System.out.println("Server Send to all");
		try {
			for (PlayerMP p : connectedPlayers) {
				if (p.port != -1) {
					//System.out.println("Sent to " + p.getUsername());
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
	 * @param packet
	 */
	private void handleMove(Packet02Move packet) {
		try {
			//System.out.println("server handle move");
			//System.out.println(packet.getUsername());
			//if(getPlayerMP(packet.getUsername()) != null) {
				//System.out.println("packet name not null");
				int index = getPlayerMPIndex(packet.getUsername());
				if(index >= this.connectedPlayers.size()) {
					index--;
				}
				this.connectedPlayers.get(index).setX(packet.getX());
				this.connectedPlayers.get(index).setY(packet.getY());
				packet.writeData(this);
//			} else {
//				System.out.println("packet name  null");
//			}
		} catch (Exception e) {
			System.out.println("Exception in GameServer.handleMove");
			e.printStackTrace();
		}
	}
	
	private void handleMoveWall(Packet03MoveWall packet) {
		try {
				packet.writeData(this);
//			} else {
//				System.out.println("packet name  null");
//			}
		} catch (Exception e) {
			System.out.println("Exception in GameServer.handleMove");
			e.printStackTrace();
		}
	}

}