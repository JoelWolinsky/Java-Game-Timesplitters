package game.entities.areas;

public class DamageZone extends AnimArea{
	private int timer=0;
	private int increment = 0;
	private boolean active = true;
	private int i = 0;
	private int startOffset;
	private int onDuration;
	private int offDuration;

	public DamageZone(float x, float y, int width, int height, int increment,int onDuration, int offDuration, int startOffset, String...urls) {
		super(x, y, width, height,urls);
		this.increment=increment;
		this.startOffset = startOffset;
		this.onDuration=onDuration;
		this.offDuration=offDuration;
	}

	public void tick() {

		if (i<startOffset)
			i++;
		else
		{
			if (increment!=0)
			{
				if (this.active == false)
					if (timer>offDuration) {
						this.active = true;
						timer = 0;
					}
				if (this.active ==true)
					if (timer>onDuration) {
						this.active = false;
						timer = 0;
					}

				timer+=increment;
			}
		}
	}

	public boolean getActive(){
		return this.active;
	}
	public void setActive(boolean active){
		this.active = active;
	}

}
