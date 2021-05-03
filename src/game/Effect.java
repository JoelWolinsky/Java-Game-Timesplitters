package game;

public class Effect  {

	private int timer;
	private String name;

	public Effect(String name, int timer) {
		this.name=name;
		this.timer=timer;
	}


	public int getTimer() {
		return timer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	/**
	 * Decrements the timer value
	 */
	public void decrement()
	{
		timer--;
	}
}
