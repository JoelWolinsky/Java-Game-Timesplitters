package game.entities.areas;

import game.Level;
import game.entities.GameObject;
import game.entities.players.Player;
import game.graphics.LevelState;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static game.Level.addToAddQueue;
import static game.Level.getPlayers;

public class TrackedScriptedDamageZoneSpawner extends GameObject {

	private float speed;
	private LinkedList<Point>pointz;
	private int startOffset;
	private String url;
	private ArrayList<Area>areasOfEffect;
	private boolean once=true;

	public TrackedScriptedDamageZoneSpawner(float x, float y, int width, int height, float speed, LinkedList<Point> points, ArrayList<Area> areasOfEffect, int startOffset, String url) {
		super(x, y,0, width, height);
		this.pointz=points;
		this.speed=speed;
		this.startOffset=startOffset;
		this.url=url;
		this.areasOfEffect=areasOfEffect;
	}

	public void tick() {

		if (Level.getLevelState()== LevelState.Starting && once)
		{
			for (Player p : getPlayers())
			{
				TrackedScriptedDamageZone trackedScriptedDamageZone = new TrackedScriptedDamageZone(x, y, 0, 0, speed, pointz, startOffset, p,url);
				for (Area a: areasOfEffect)
				{
					trackedScriptedDamageZone.addArea(a);
				}
				addToAddQueue(trackedScriptedDamageZone);
			}

			once=false;
		}

	}

	@Override
	public void render(Graphics g, float xOffset, float yOffset) {

	}
}
