package game;


import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import game.network.packets.Packet01Disconnect;

public class WindowHandler implements WindowListener {

	private final Game game;
	
	public WindowHandler(Game game) {
		this.game = game;
		//this.game.frame.addWindowListener(this);
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Packet01Disconnect packet = new Packet01Disconnect(this.game.player.getUsername());
		packet.writeData(this.game.socketClient);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
