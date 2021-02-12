package client;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import server.Listeners;

/**
 * A keyboard listener class.<br>
 * <br>
 * <b>This should be public and static in the game class, and registered as a key listener in the Game constructor</b><br>
 * <pre>this.addKeyListener(keyInput);</pre>
 */
public class KeyInput extends KeyAdapter{
	
	/**
	 * Broadcasts keyPressed events to all KeyListener objects in the listener list
	 */
	public void keyPressed(KeyEvent e) {
		for(int i = 0; i < Listeners.keyListeners.size(); i ++) {
			Listeners.keyListeners.get(i).keyPressed(e);
		}
	}
	
	/**
	 * Broadcasts keyReleased events to all KeyListener objects in the listener list
	 */
	public void keyReleased(KeyEvent e) {
		for(int i = 0; i < Listeners.keyListeners.size(); i ++) {
			Listeners.keyListeners.get(i).keyReleased(e);
		}
	}
}
