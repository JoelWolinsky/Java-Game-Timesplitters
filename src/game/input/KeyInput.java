package game.input;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.LinkedList;

public class KeyInput extends KeyAdapter{
	
	public static Key up = new Key();
	public static Key down = new Key();
	public static Key left = new Key();
	public static Key right = new Key();
    public static Key r = new Key();
    public static Key g = new Key();
    public static Key space = new Key();
    public static Key comma = new Key();
    public static Key period = new Key();
	/**
	 * Sets the pressed value of a given key based on a keyCode
	 * @param keyCode The keyCode representing the key pressed
	 * @param isPressed The value to which the pressed value should be set
	 */
    public void toggleKey(int keyCode, boolean isPressed) {
        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            up.setPressed(isPressed);
        }
        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            down.setPressed(isPressed);
        }
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            left.setPressed(isPressed);
        }
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            right.setPressed(isPressed);
        }
        if (keyCode == KeyEvent.VK_R) {
            r.setPressed(isPressed);
        }
        if (keyCode == KeyEvent.VK_G) {
            g.setPressed(isPressed);
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            space.setPressed(isPressed);
        }
        if (keyCode == KeyEvent.VK_COMMA) {
            comma.setPressed(isPressed);
        }
        if (keyCode == KeyEvent.VK_PERIOD) {
            period.setPressed(isPressed);
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
        	System.exit(0);
        }

    }
    
	public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(), false);
    }
}
