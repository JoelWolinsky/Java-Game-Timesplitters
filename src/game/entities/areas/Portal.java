package game.entities.areas;
import game.attributes.AnimatedObject;
import game.entities.Player;
import game.graphics.Animation;
import game.graphics.AnimationStates;

import java.awt.*;
import java.util.HashMap;

public class Portal extends AnimArea{

	private int destinationLevel;
	private int destinationX;
	private int destinationY;
	private int currentX,currentY;

	public Portal(float x, float y, int width, int height, int destinationLevel,int destinationX,int destinationY,int currentX,int currentY, String...urls) {
		super(x, y, width, height,urls);
		this.destinationLevel=destinationLevel;
		this.destinationX=destinationX;
		this.destinationY=destinationY;
		this.currentX=currentX;
		this.currentY=currentY;
	}

	public void tick() {
	}


	public boolean getInteractionEffect(Player player){
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
