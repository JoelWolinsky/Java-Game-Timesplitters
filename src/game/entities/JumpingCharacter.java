package game.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import game.GameObject;
import game.Level;
import game.attributes.CollidingObject;
import game.attributes.GraphicalObject;
import game.attributes.SolidCollider;

public class JumpingCharacter extends GameObject implements GraphicalObject, CollidingObject{
	
	protected float velX, velY;
	
	public static final float DEFAULT_X_VELOCITY = 3.5f; // Running speed
    public static final float DEFAULT_Y_VELOCITY = 4.0f; // Constant gravity when on ground
	public static final float GRAVITY = 0.35f; // Rate at which falling speed increases
	public static final float TERMINAL_VEL = 15.0f; // Max falling speed
	public static final float JUMP_GRAVITY = -7.5f; // Height of jump

	protected boolean cd = false; // Is the character in mid-air

	public JumpingCharacter(Level level, float x, float y, int z) {
		super(level, x, y, z, 64, 32);
		this.velX = 0.0f;
		this.velY = DEFAULT_Y_VELOCITY;
		CollidingObject.addCollider(this);
	}

	public void tick(double delta) {
		move(delta);
		CollidingObject.getCollisions(this);		
	}
	
	private void move(double delta) {

		if(SolidCollider.willCauseSolidCollision(this, delta * this.velY, false)) {
			/*  velY needs to be above a certain number for a floor to be recognised by
			    willCauseSolidCollision() such that the character doesn't get stuck in the floor. */
            velY = DEFAULT_Y_VELOCITY;
			cd = false;	// TODO Adjust this because allows jumping after hitting a ceiling
		}
		else{
			velY += GRAVITY; // While character mid-air increase rate of falling

			if (velY > TERMINAL_VEL){
				velY = TERMINAL_VEL;
			}
			this.y += (delta * this.velY);
		}
		if(!SolidCollider.willCauseSolidCollision(this, delta * this.velX, true)) {
			this.x += (delta * this.velX);
		}	
	}

	public void render(Graphics g) {}
    public void keyPressed(KeyEvent e) {} 
	public void keyReleased(KeyEvent e) {}

	@Override
	public void handleCollisions(LinkedList<CollidingObject> collisions) {}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)this.x, (int)this.y, this.width, this.height);
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g, int xOffset, int yOffset) {
		// TODO Auto-generated method stub
		
	}
}
