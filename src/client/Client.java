package client;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import shared.Buffer;

public class Client {
	
	public static Buffer<KeyEvent> keyEventBuffer = new Buffer<KeyEvent>();
	public static Buffer<MouseEvent> mouseEventBuffer = new Buffer<MouseEvent>();
	
	//Every packet, read() each buffer (this will clear the buffer) to a new temporary variable and send both buffers, and the mouse position
	

}
