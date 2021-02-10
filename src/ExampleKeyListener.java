import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class ExampleKeyListener extends GameObject implements KeyListener, GraphicalObject, CollidingObject{
	
	private int velX, velY;
	private static BufferedImage sprite;
	private static String url = "./img/player1.png";
	private int jmp = 10;
	private boolean cd=false;
	private boolean wON=false;

	
	
	public ExampleKeyListener(float x, float y, int z) {
		super(x, y, z, 64, 32);
		this.velX = 0;
		this.velY = 3;
		sprite = this.loadImage(ExampleKeyListener.url);
		CollidingObject.addCollider(this);
		Game.keyInput.addListener(this);
		Game.handler.addObject(this);
	}

	public void tick(double delta) {
		move(delta);
		CollidingObject.getCollisions(this);
		
	}
	
	private void move(double delta) {
		if(!SolidCollider.willCauseSolidCollision(this, delta * this.velX, true)) {
			this.x += (delta * this.velX);
		}
		if(!SolidCollider.willCauseSolidCollision(this, delta * this.velY, false)) {
			this.y += (delta * this.velY);
		}
		else
			cd = false;
	}

	public void render(Graphics g) {
		this.drawSprite(g, sprite, (int)this.x, (int)this.y);
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_D) {
			this.velX = 3;
		} else if (key == KeyEvent.VK_A) {
			this.velX = -3;
		} else if (key == KeyEvent.VK_W) {


			if (cd==false)
			{
				this.y = y - 50;
				cd = true;
			}

		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_D || key == KeyEvent.VK_A) {
			this.velX = 0;
			this.velY = 3;
		}
		
		if(key == KeyEvent.VK_W) {
			this.velY = 3;
		}
	}

	@Override
	public void handleCollisions(LinkedList<CollidingObject> collisions) {
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)this.x, (int)this.y, this.width, this.height);
	}

}
