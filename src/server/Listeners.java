package server;

import java.util.LinkedList;


public class Listeners {
	
	/**
	 * A LinkedList maintaining all classes implementing KeyListener
	 * @see KeyListener
	 */
	public static LinkedList<KeyListener> keyListeners = new LinkedList<KeyListener>();
	
	/**
	 * Adds a KeyListener object to the listeners list
	 * @param k The KeyListener object to be added to the listeners list
	 */
	public static void addKeyListener(KeyListener k) {
		keyListeners.add(k);
	}
	
	/**
	 * Removes a KeyListener object from the listeners list
	 * @param k The object to be removed from the listeners list
	 */
	public static void removeKeyListener(KeyListener k) {
		keyListeners.remove(keyListeners.indexOf(k));
	}
	
	/**
	 * A list of objects implementing mouseClickListener. All mouse click events are broadcast to this list
	 */
	public static LinkedList<MouseClickListener> mouseClickListeners = new LinkedList<MouseClickListener>();
	
	/**
	 * Adds a MouseClickListener to the mouselickListeners list
	 * @param m The MouseClickListener to be added
	 */
	public static void addMouseClickListener(MouseClickListener m) {
		mouseClickListeners.add(m);
	}
	
	/**
	 * Removes a MouseClickListener from the clickListeners list
	 * @param m The MouseClickListener to be removed
	 */
	public static void removeMouseClickListener(MouseClickListener m) {
		mouseClickListeners.remove(m);
	}
	

}
