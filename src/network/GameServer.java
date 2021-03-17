package network;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.entities.PlayerMP;
import game.network.packets.Packet;
import game.network.packets.Packet.PacketTypes;
import game.network.packets.Packet00Login;
import game.network.packets.Packet01Disconnect;
import game.network.packets.Packet02Move;

public class GameServer extends Thread {
	private DatagramSocket socket;
	private Game game;
	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
	private List<String> playerIDs= new ArrayList<String>();
	
	public GameServer(Game game) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(1331);
		} catch (SocketException e) {
			e.printStackTrace(); 
		}
	}
	
	public void run() {
		while(true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
				System.out.println("Server recieve");
			} catch (IOException e) {
				System.out.println("Exception receiving packet in server run");
				e.printStackTrace();
			}
			//System.out.println("packet");
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
//			String message = new String(packet.getData());
//			if(message.trim().equalsIgnoreCase("ping")) {
//				System.out.println("Client ["+packet.getAddress().getHostAddress()+":"+packet.getPort()+"]-> " + message);
//				sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
//				
//
//			}
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		try {
			//System.out.println("server parse");
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
				PlayerMP player = new PlayerMP (game.currentLevel, 100, 100, address, port);
				this.addConnection(player, (Packet00Login) packet);
				break;
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
				System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect) packet).getUsername() + " has left...");
				this.removeConnection((Packet01Disconnect) packet);
				break;
			case MOVE:
				packet = new Packet02Move(data);
				System.out.println(((Packet02Move)packet).getUsername() + " has moved to " + ((Packet02Move)packet).getX() + ", " + ((Packet02Move)packet).getY());
				this.handleMove((Packet02Move)packet);
			}
		}catch (Exception e) {
				System.out.println("Exception at GameServer.parsePacket");
				e.printStackTrace();
			}
		}

	

	

	public void addConnection(PlayerMP player, Packet00Login packet) {
		System.out.println("Ser add con");
		try {
			boolean alreadyConnected = false;
			for (String p : this.playerIDs) {
				System.out.println(this.connectedPlayers.size());
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
					
					packet = new Packet00Login(this.connectedPlayers.get(getPlayerMPIndex(p)).getUsername(), this.connectedPlayers.get(getPlayerMPIndex(p)).getX(), this.connectedPlayers.get(getPlayerMPIndex(p)).getY());
	                sendData(packet.getData(), player.ipAddress, player.port);
				}
			}
			System.out.println("already connected = " + alreadyConnected);
			if (!alreadyConnected) {
	
	            this.connectedPlayers.add(player);
	            this.playerIDs.add(packet.getUsername());
	            System.out.println("adding to ids" + packet.getUsername());
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

	public void sendDataToAllClients(byte[] data) {
		System.out.println("Server Send to all");
		try {
			for (PlayerMP p : connectedPlayers) {
				if (p.port != -1) {
					System.out.println("Sent to " + p.getUsername());
					sendData(data, p.ipAddress, p.port);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in GameServer.sendDataToAllClients");
			e.printStackTrace();
		}
	}
	
	private void handleMove(Packet02Move packet) {
		try {
			System.out.println("server handle move");
			System.out.println(packet.getUsername());
			//if(getPlayerMP(packet.getUsername()) != null) {
				System.out.println("packet name not null");
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

}