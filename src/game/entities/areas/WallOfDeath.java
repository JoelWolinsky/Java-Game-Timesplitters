package game.entities.areas;

import game.entities.GameObject;
import game.entities.players.Player;
import game.graphics.LevelState;

import static game.Level.getPlayers;
import static game.Level.getLevelState;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class WallOfDeath extends GameObject {

    private BufferedImage img;
	private boolean visible = true;
    private LinkedList<Area> areas = new LinkedList<>();

    public WallOfDeath() {
		
        super(-3000, -500, 100, 2000, 4600);

	}

    public void tick() {

        for (Player p: getPlayers())
		{
			if (this.getInteraction(p))
				p.respawn();
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
    








	

	

	




