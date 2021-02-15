package client;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import server.Listeners;

/**
 * A keyboard listener class.<br>
 * <br>
 * <b>This should be public and static in the game class, and registered as a key listener in the Game constructor</b><br>
 * <pre>this.addKeyListener(keyInput);</pre>
 */
public class KeyInput extends KeyAdapter{
	
	/**
	 * Broadcasts keyPressed events to all KeyListener objects in the listener list<br>
	 * This will be replaced with a network send function
	 */
	public void keyPressed(KeyEvent e) {
		for(int i = 0; i < Listeners.keyListeners.size(); i ++) {
			Listeners.keyListeners.get(i).keyPressed(e);
//			Client.keyEventBuffer.write(e);
		}
	}
	
	/**
	 * Broadcasts keyReleased events to all KeyListener objects in the listener list<br>
	 * This will be replaced with a network send function
	 */
	public void keyReleased(KeyEvent e) {
		for(int i = 0; i < Listeners.keyListeners.size(); i ++) {
			Listeners.keyListeners.get(i).keyReleased(e);
//			Client.keyEventBuffer.write(e);
		}
	}
}
