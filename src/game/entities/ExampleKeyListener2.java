package game.entities;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import game.Level;

public class ExampleKeyListener2 extends JumpingCharacter {
	
	private static BufferedImage sprite;
	private static String url = "./img/player1inv.png";

	public ExampleKeyListener2(Level level, float x, float y, int z) {
		super(level, x, y, z);
        sprite = this.loadImage(ExampleKeyListener2.url);
	}

    @Override
	public void render(Graphics g) {
		this.drawSprite(g, sprite, (int)this.x, (int)this.y);
	}

    // This example key listener uses arrow keys to move.
    // Controls can be adjusted.
    @Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_RIGHT) {
			this.velX = DEFAULT_X_VELOCITY;
		} else if (key == KeyEvent.VK_LEFT) {
			this.velX = -DEFAULT_X_VELOCITY;
		} else if (key == KeyEvent.VK_DOWN) {
			this.velY = 10;
		} else if (key == KeyEvent.VK_UP) {
			if (cd==false)
			{
				this.velY = JUMP_GRAVITY;
				cd = true;
			}
		}
	}

    @Override
	public void keyReleased(KeyEvent e) { 	/* TODO fix this so that when both A and D are held 
											and one is released the character doesn't stop */
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_LEFT) {
			this.velX = 0;
		}		
	}
}
