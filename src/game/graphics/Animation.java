package game.graphics;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Animation {
	private LinkedList<BufferedImage> frames;
	private int ticksPerFrame;
	
	public Animation(int ticksPerFrame, String...urls) {
		frames = new LinkedList<BufferedImage>();
		this.ticksPerFrame = ticksPerFrame;
		for(String url : urls) {
			BufferedImage b = Image.loadImage(url);
			if(b!=null) {
				frames.add(b);
			}
		}
	}
	
	public int getTicksPerFrame() {
		return this.ticksPerFrame;
	}
	
	public BufferedImage getFrame(int index) {
		return frames.get(index);
	}
	
	public int getNumberOfFrames() {
		return frames.size();
	}

}
		