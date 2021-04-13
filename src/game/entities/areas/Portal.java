package game.entities.areas;
import game.entities.players.Player;
import game.entities.players.AIPlayer;

import java.awt.*;

import static game.Level.getPlayers;

public class Portal extends AnimArea{

	private int destinationLevel;
	private int destinationX;
	private int destinationY;
	private int currentX,currentY;
	private int stutterValue = 15;
	private int j=0;

	public Portal(float x, float y, int width, int height, int destinationLevel,int destinationX,int destinationY,int currentX,int currentY, String url) {
		super(x, y, width, height,url);
		this.destinationLevel=destinationLevel;
		this.destinationX=destinationX;
		this.destinationY=destinationY;
		this.currentX=currentX;
		this.currentY=currentY;
	}

	public void tick() {


		for (Player p: getPlayers())
		{
			if (this.getInteraction(p)) {
				p.setRespawnX(this.getCurrentX() + this.getDestinationX());
				p.setRespawnY(this.getCurrentY() + this.getDestinationLevel() + this.getDestinationY());
				p.setRespawnThreshold(this.getCurrentY() + this.getDestinationLevel() + this.getDestinationY());
				p.setY(this.getCurrentY() + this.getDestinationLevel() + this.getDestinationY());
				p.setX(p.getX());

			}


			if (this.getInteractionEffect(p))
			{
				p.setX(p.getX()+stutterValue);
				stutterValue=stutterValue*(-1);
			}

		}



	}

	@Override
	public void render(Graphics g, float f, float h) {
		super.render(g, f, h);
	}

	public boolean getInteractionEffect(Player player){
		return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y-200<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);
	}

	public boolean getInteractionEffect(AIPlayer player){
		return ((int)this.x<(int)player.getX()+player.getWidth() && (int)player.getX()<this.x+this.width && (int)this.y-200<(int)player.getY()+player.getHeight() && (int)player.getY() <(int)this.y+this.height);
	}

	public int getDestinationLevel() {
		return destinationLevel;
	}

	public int getDestinationX() {
		return destinationX;
	}

	public int getDestinationY() {
		return destinationY;
	}

	public int getCurrentX() {
		return currentX;
	}

	public int getCurrentY() {
		return currentY;
	}
}
