package game.network.packets;

import network.GameClient;
import network.GameServer;

public abstract class Packet {
	
	/**
	 * The types of packets that are sent over the server.
	 */
	public static enum PacketTypes {
		INVALID(-1), LOGIN(00), ITEM(01), MOVE(02), MOVEWALL(03), STARTGAME(04), CAPACITY(05);
		
		private int packetId;
		
		private PacketTypes(int packetId) {
			this.packetId = packetId;
		}
		
		public int getId() {
			return packetId;
		}
		
	}
	
	public byte packetId;
	/**
	 * Initialises a packet.
	 * @param packetId The id of the packet.
	 */
	public Packet(int packetId) {
		this.packetId = (byte) packetId;
	}
	
	public abstract void writeData(GameClient client);
	
	public abstract void writeData(GameServer server);
	
	/**
	 * Cuts of the first two character of the data which define the packet type.
	 * @param data The data to edit.
	 * @return The edited data.
	 */
	public String readData(byte[] data) {
		String message = new String(data).trim();
		return message.substring(2);
	}
	
	public abstract byte[] getData();
	/**
	 * Finds the PacketType for a given id.
	 * @param id The PacketId.
	 * @return The PacketType.
	 */
	public static PacketTypes lookupPacket(int id) {
		for (PacketTypes p : PacketTypes.values()) {
			if (p.getId() == id) {
				return p;
			}
		}
		return PacketTypes.INVALID;
		
	}
	/**
	 * Finds the PacketType for a given id.
	 * @param packetId The PacketId.
	 * @return The PacketType.
	 */
	public static PacketTypes lookupPacket(String packetId) {
		try {
			return lookupPacket(Integer.parseInt(packetId));
		} catch (NumberFormatException e) {
			return PacketTypes.INVALID;
		}
	}
	
}
