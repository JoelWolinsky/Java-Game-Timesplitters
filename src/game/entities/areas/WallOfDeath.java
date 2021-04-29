package game.entities.areas;
import game.Game;
import game.SoundHandler;
import game.entities.GameObject;
import game.entities.players.AIPlayer;
import game.entities.players.Player;
import game.graphics.Animation;
import game.graphics.AnimationStates;
import game.graphics.GameMode;
import game.graphics.LevelState;
import game.network.packets.Packet02Move;
import game.network.packets.Packet03MoveWall;

import static game.Level.getPlayers;
import static game.Level.getLevelState;
import static game.graphics.Assets.getAnimations;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class WallOfDeath extends GameObject {

    private LinkedList<Area> areas = new LinkedList<>();
    protected int animationTimer = 0;
    protected int frame;
    protected AnimationStates currentAnimState;
    protected Animation currentAnimation;
    protected HashMap<AnimationStates, Animation> animations;

    public WallOfDeath() {
        super(-2000, -300, 3,1113, 819);
        // super(-3000, -1000, 100, 2000, 5500);

        this.animations = getAnimations("DETH");
        this.currentAnimState = AnimationStates.IDLE;

	}

    public void tick() {

        for (Player p: getPlayers())
		{
			if (this.getInteraction(p)){
			    if(!(p instanceof AIPlayer))
			    if (!p.isGhostMode())
                {
                    p.setGhostMode(true);
                    p.setCurrentAnimState(AnimationStates.LEFT);
                    p.setAnimations(getAnimations("GHOST_FORM"));
                    SoundHandler.playSound("death", 0.5f);

                }
            }
                // permadeath so don't respawn. turn into ghost and fly
		}

        if (getLevelState()== LevelState.InProgress){
        	//System.out.println(Game.socketServer);
        	if(Game.socketServer != null) {
	            moving=true;
	            this.x += 1;
	            //this.width++;

	            Packet03MoveWall packet = new Packet03MoveWall(this.x);
                packet.writeData(Game.socketClient);
        	} else if (Game.gameMode == GameMode.SINGLEPLAYER) {
        		moving=true;
	            this.x += 1;
        	}
        }

	}

    @Override
    public void render(Graphics g, float f, float h) {

       // g.setColor(Color.magenta);
       /// g.fillRect((int)(this.x + f),(int)(this.y + h),this.width,this.height);
        // g.drawImage(img,(int)(this.x + f),(int)(this.y + h),null);
       //super.render(g, f, h);
        this.renderAnim(g, (int) (this.x + f), (int) (this.y + h));
    }


	public boolean getInteraction(GameObject player){
		return ((int)this.x < (int)player.getX()+player.getWidth() &&
				(int)player.getX() < this.x+this.width - 110 &&
				(int)this.y < (int)player.getY()+player.getHeight() &&
				(int)player.getY() < (int)this.y+this.height);
	}

    public LinkedList<Area> getEventArea(){return this.areas;}


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

    public void setCurrentAnimState(AnimationStates currentAnimState) {
        this.currentAnimState = currentAnimState;
    }

    public void setMoving() {
    	this.moving = true;
    }

}
