package game.entities.areas;

public class TimerDamageZone extends AnimArea{
	private int timer=0;
	private int increment = 0;
	private boolean active = true;

	public TimerDamageZone(float x, float y, int width, int height,int increment, String...urls) {
		super(x, y, width, height,urls);
		this.increment=increment;

	}

	public void tick() {

		if (increment!=0)
		{
			if (timer>100)
			{

				if (this.active == true)
					this.active = false;
				else
					this.active = true;
				timer = 0;
			}

			timer+=increment;
			System.out.println(active);
		}

	}

	public boolean getActive(){
		return this.active;
	}
	public void setActive(boolean active){
		this.active = active;
	}

}
