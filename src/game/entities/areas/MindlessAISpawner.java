package game.entities.areas;
import game.entities.GameObject;
import java.awt.*;
import java.util.LinkedList;
import static game.Level.*;

public class MindlessAISpawner extends GameObject {

	private int i = 0;
	private int dummyMinRange,dummyMaxRange;
	private int chickensSpawned=0;
	private int maxChickens;
	private String url;
	private LinkedList<MindlessAI> myChickens = new LinkedList<>();

	public MindlessAISpawner(float x, float y, int width, int height,int minRange, int maxRange,int maxChickens, String url) {
		super(x,y,0,width,height);
		this.url=url;
		this.dummyMinRange=minRange;
		this.dummyMaxRange=maxRange;
		this.maxChickens=maxChickens;
	}

	public void tick() {

		if (chickensSpawned<=maxChickens)
		{
			if (i < 100)
				i++;
			else {
				MindlessAI mindlessAI = new MindlessAI(this.x, this.y, this.width, this.height, this.getDummyMinRange(), this.getDummyMaxRange(), this.getUrl());
				getToBeAdded().add(mindlessAI);
				myChickens.add(mindlessAI);
				chickensSpawned++;
				i = 0;
			}

		}
		else
		{
			getToBeRemoved().add(myChickens.getFirst());
			myChickens.removeFirst();
			chickensSpawned--;
		}

	}

	@Override
	public void render(Graphics g, float xOffset, float yOffset) {

	}

	public int getDummyMinRange() {
		return dummyMinRange;
	}

	public int getDummyMaxRange() {
		return dummyMaxRange;
	}

	public String getUrl() {
		return url;
	}
}
