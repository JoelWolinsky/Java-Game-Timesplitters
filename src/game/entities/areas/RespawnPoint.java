package game.entities.areas;
import game.SoundHandler;
import game.entities.players.AIPlayer;
import game.entities.players.Player;
import game.graphics.Image;

import java.awt.*;

import static game.Level.getPlayers;

public class RespawnPoint extends Area {

	private float pointX,pointY;
	private boolean reached = false;

	public RespawnPoint(float x, float y, int width, int height, int pointX, int pointY, String url) {
		super(x, y, width, height, Image.loadImage(url));
		this.pointX=pointX;
		this.pointY=pointY;
	}

	public void tick() {

		for (Player p : getPlayers())
		{
			if (this.getInteraction(p))
			{
				if (!p.getRespawnPoints().contains(this)) {
					p.setRespawnX((int) this.getX() + (int)this.getExtraPointX());
					p.setRespawnY((int) this.getY()-40 + (int)this.getExtraPointY());
					p.setRespawnThreshold((int)this.getY());
					p.getRespawnPoints().add(this);
					
					if (! (p instanceof AIPlayer))
						SoundHandler.playSound("waypoint", 0.5f);
						this.reached=true;
				}
			}

		}

	}

	@Override
	public void render(Graphics g, float f, float h) {
		if (this.reached)
			super.render(g, f, h);
	}

	public boolean getInteraction(Player player){
		return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y-200<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);
	}

	public float getExtraPointX() {
		return pointX;
	}

	public float getExtraPointY() {
		return pointY;
	}

}
