package game.entities.areas;

import game.SoundHandler;
import game.entities.GameObject;
import game.entities.players.AIPlayer;
import game.entities.players.Player;
import game.graphics.AnimationStates;
import game.graphics.LevelState;

import static game.Level.getPlayers;
import static game.Level.getLevelState;
import static game.graphics.Assets.getAnimations;

import java.awt.*;
import java.util.LinkedList;

public class WallOfDeath extends GameObject {

    private LinkedList<Area> areas = new LinkedList<>();

    public WallOfDeath() {
        super(-3000, -500, 100, 2000, 4600);
        
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
            this.width += 1;
        }

	}

    @Override
    public void render(Graphics g, float f, float h) {

        g.setColor(Color.magenta);
        g.fillRect((int)(this.x + f),(int)(this.y + h),this.width,this.height);
        // g.drawImage(img,(int)(this.x + f),(int)(this.y + h),null);
        
        // super.render(g, f, h);
    }


	public boolean getInteraction(GameObject player){
		return ((int)this.x < (int)player.getX()+player.getWidth() && 
				(int)player.getX() < this.x+this.width && 
				(int)this.y < (int)player.getY()+player.getHeight() && 
				(int)player.getY() < (int)this.y+this.height);
	}

    public LinkedList<Area> getEventArea(){return this.areas;}


}
    








	

	

	




