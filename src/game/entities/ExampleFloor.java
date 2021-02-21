package game.entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import game.GameObject;
import game.attributes.CollidingObject;
import game.attributes.GraphicalObject;
import game.attributes.SolidCollider;

public class ExampleFloor extends GameObject implements GraphicalObject, SolidCollider{
private static BufferedImage sprite;
private static String url = "./img/Grass2.png";
public ExampleFloor(float x, float y, int z) {
    super(x, y, z, 640, 32);
    sprite = this.loadImage(ExampleFloor.url);
    CollidingObject.addCollider(this);
    SolidCollider.addSolidCollider(this);
}

public void tick(double delta) {
    CollidingObject.getCollisions(this);
    
}


@Override
public void handleCollisions(LinkedList<CollidingObject> collisions) {
}

@Override
public Rectangle getBounds() {
    return new Rectangle((int)this.x, (int)this.y, this.width, this.height);
}

@Override
public void tick() {
	// TODO Auto-generated method stub
	
}

@Override
public void render(Graphics g, float xOffset, float yOffset) {
	this.drawSprite(g, sprite, (int)(this.x+xOffset), (int)(this.y+yOffset));
	
}
}