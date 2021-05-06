package game.entities.areas;
import game.Game;
import game.SoundHandler;
import game.entities.GameObject;
import game.entities.players.Player;
import game.graphics.Animation;
import game.graphics.AnimationStates;
import game.graphics.GameMode;
import game.graphics.LevelState;
import game.network.packets.Packet03MoveWall;

import javax.imageio.ImageIO;

import static game.Level.*;
import static game.graphics.Assets.getAnimations;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class WallOfDeath extends GameObject {

    protected int animationTimer = 0;
    protected int frame;
    protected AnimationStates currentAnimState;
    protected Animation currentAnimation;
    protected HashMap<AnimationStates, Animation> animations;
    private static BufferedImage gravestone;
    private float speed;

    public WallOfDeath(float speed) {
        super(-2000, -300, 3,1113, 819);

        this.animations = getAnimations("DETH");
        this.currentAnimState = AnimationStates.IDLE;
        try {
			WallOfDeath.gravestone = ImageIO.read(new File("./img/gravestone.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    this.speed = speed;
	}

    /**
	 * Called every frame, and is responsible for moving the Wall of Death and handling its interactions with players.
	 */
    public void tick() {

        for (Player p: getPlayers())
			if (this.getInteraction(p))
			    if (!p.isGhostMode())
                {
                    getCertainBlip(p).setImg(WallOfDeath.gravestone);
                    p.setGhostMode(true);
                    p.setCurrentAnimState(AnimationStates.LEFT);
                    p.setAnimations(getAnimations("GHOST_FORM"));
                    SoundHandler.playSound("death", 0.5f);

                }

        if (getLevelState()== LevelState.InProgress){
        	if(Game.socketServer != null) {
	            moving=true;
	            this.x += speed;

	            Packet03MoveWall packet = new Packet03MoveWall(this.x);
                packet.writeData(Game.socketClient);
        	} else if (Game.gameMode == GameMode.SINGLEPLAYER || Game.gameMode == GameMode.vsAI ) {
        		moving=true;
	            this.x += speed;
        	}
        }
	}

    /**
	 * Calls the renderAnim function that renders the animation of the Wall.
     * @param g The Graphics object onto which the object will be rendered
	 * @param xOffset The xOffset of the object
	 * @param yOffset The yOffset of the object
	 */
    @Override
    public void render(Graphics g, float xOffset, float yOffset) {
        this.renderAnim(g, (int) (this.x + xOffset), (int) (this.y + yOffset));
    }

    /**
	 * Returns whether a given object is in contact (i.e. interacting) with the Wall.
     * @param player The object that we check is in contact with the Wall.
	 */
	public boolean getInteraction(GameObject player){
		return ((int)this.x < (int)player.getX()+player.getWidth() &&
				(int)player.getX() < this.x+this.width - 110 &&
				(int)this.y < (int)player.getY()+player.getHeight() &&
				(int)player.getY() < (int)this.y+this.height);
	}

    /**
	 * Returns the 'areas' LinkedList.
	 */

    /**
	 * Renders an animated sprite by flicking through its different frames
	 * @param g The Graphics object onto which the object will be rendered
	 * @param x The x coordinate of the object
	 * @param y The y coordinate of the object
	 */
    public void renderAnim(Graphics g, int x, int y) {

        if (currentAnimState != null) {
            currentAnimation = animations.get(currentAnimState);

            frame = (animationTimer / currentAnimation.getTicksPerFrame());

            g.drawImage(currentAnimation.getFrame(frame), x, y, null);

            animationTimer++;

            if (animationTimer >= currentAnimation.getTicksPerFrame() * currentAnimation.getNumberOfFrames()) {
                animationTimer = 0;
            }
        }
    }

    /**
	 * Updates the state of animation.
     * @param currentAnimState The current state of animation.
	 */
    public void setCurrentAnimState(AnimationStates currentAnimState) {
        this.currentAnimState = currentAnimState;
    }

    /**
	 * Sets the variable 'moving' to true to facilitate networking.
	 */
    public void setMoving() {
    	this.moving = true;
    }

}
