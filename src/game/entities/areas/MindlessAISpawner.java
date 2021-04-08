package game.entities.areas;

import game.Level;
import game.attributes.CollidingObject;
import game.attributes.GravityObject;
import game.attributes.SolidCollider;
import game.entities.GameObject;
import game.entities.platforms.MovingPlatform;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

import static game.Level.*;

public class MindlessAISpawner extends GameObject {

	private Level currentLevel;
	private int i = 0;
	private float dummyX,dummyY;
	private int dummyWidth,dummyHeight,dummyMinRange,dummyMaxRange;
	private boolean addChicken = false;
	private boolean removeChicken = false;
	private int chickensSpawned=0;
	private int maxChickens;
	private String[] urls;

	public MindlessAISpawner(float x, float y, int width, int height,int minRange, int maxRange,int maxChickens,Level currentLevel, String...urls) {
		super(0,0,0,0,0);
		this.urls=urls;
		this.dummyX=x;
		this.dummyY=y;
		this.dummyWidth=width;
		this.dummyHeight=height;
		this.dummyMinRange=minRange;
		this.dummyMaxRange=maxRange;
		this.currentLevel=currentLevel;
		this.maxChickens=maxChickens;
	}

	public void tick() {


		if (this.getAddChicken())
		{
			getToBeAdded().add(new MindlessAI(this.getDummyX(), this.getDummyY(), this.getDummyWidth(), this.getDummyHeight(), this.getDummyMinRange(), this.getDummyMaxRange(), this.getUrls()));
			this.setAddChicken(false);
		}
		else if (this.getRemoveChicken())
		{
			getToBeRemoved().add(getChickens().getFirst());
			this.setRemoveChicken(false);

		}


		if (chickensSpawned<=maxChickens)
		{
			if (addChicken==false) {
				if (i < 100)
					i++;
				else {
					this.addChicken = true;
					chickensSpawned++;
					//currentLevel.addEntity(new MindlessAI(dummyX,dummyY,dummyWidth,dummyWidth,dummyMinRange,dummyMaxRange,urls));
					i = 0;
				}
			}
		}
		else
		{
			removeChicken=true;
			chickensSpawned--;
			//currentLevel.getChickens().remove(currentLevel.getChickens().getFirst());
		}

	}

	@Override
	public void render(Graphics g, float xOffset, float yOffset) {

	}

	public float getDummyX() {
		return dummyX;
	}

	public float getDummyY() {
		return dummyY;
	}

	public int getDummyHeight() {
		return dummyHeight;
	}

	public int getDummyWidth() {
		return dummyWidth;
	}

	public int getDummyMinRange() {
		return dummyMinRange;
	}

	public int getDummyMaxRange() {
		return dummyMaxRange;
	}

	public boolean getAddChicken() {
		return addChicken;
	}

	public String[] getUrls() {
		return urls;
	}

	public void setAddChicken(boolean addChicken) {
		this.addChicken = addChicken;
	}

	public void setRemoveChicken(boolean removeChicken) {
		this.removeChicken = removeChicken;
	}

	public boolean getRemoveChicken() {
		return removeChicken;
	}
}
