package game;

public class Camera {
	private float xOffset, yOffset;
	private GameObject target = null;
	
	public Camera() {
		this.xOffset = 0;
		this.yOffset = 0;
	}
	
	public void tick() {
		if(target != null) {
			this.xOffset = -(this.target.getX() + (this.target.getWidth() /2) - (Window.WIDTH/2));
			this.yOffset = -(this.target.getY() + (this.target.getHeight() /2) - (Window.HEIGHT/2));
			//this.yOffset = 150-Window.HEIGHT/2;
		}
	}
	
	public void addTarget(GameObject o ) {
		this.target = o;
	}
	
	public void removeCenter() {
		this.target = null;
	}
	
	public void moveCameraX(int offset) {
		this.xOffset += offset;
	}
	
	public void moveCameraY(int offset) {
		this.yOffset += offset;
	}
	
	public float getXOffset() {
		return this.xOffset;
	}
	
	public float getYOffset() {
		return this.yOffset;
	}

}
