package game.graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

public class Animation {
	private ArrayList<BufferedImage> frames;
	private int ticksPerFrame;
	
	/**
	 * @param ticksPerFrame The number of ticks before the next frame is shown, allows you to control the speed of animation
	 * @param urls A sequence of urls of the frames of animation, in order
	 */
	public Animation(int ticksPerFrame, ArrayList<BufferedImage> animList) {
		frames = new ArrayList<BufferedImage>();
		this.ticksPerFrame = ticksPerFrame;
		this.frames = animList;

	}

	public Animation(int ticksPerFrame, String... urls) {

		frames = new ArrayList<BufferedImage>();
		this.ticksPerFrame = ticksPerFrame;
		for(String url : urls) {
			BufferedImage b = Image.loadImage(url);
			if(b!=null) {
				frames.add(b);
			}
		}

	}
	
	/**
	 * @return The number of ticks per frame
	 */
	public int getTicksPerFrame() {
		return this.ticksPerFrame;
	}
	
	/**
	 * @param index The index of the image in the frames list you would like to access
	 * @return The BufferedImage stored at index in the frames list
	 */
	public BufferedImage getFrame(int index) {
		return frames.get(index);
	}
	
	/**
	 * @return The number of frames of animation
	 */
	public int getNumberOfFrames() {
		return frames.size();
	}

}
		