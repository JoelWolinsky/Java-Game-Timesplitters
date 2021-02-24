package game.attributes;
/**
 * An interface to be extended by any object that is to be affected by gravity<br>
 * <br>
 * <b>In tick()</b><br>
 * <pre>fall(this);</pre><br>
 * 
 */
public interface GravityObject {
	
	public float gravityRate = 0.35f;

	
	default void fall(GravityObject o) {
		float currentVelY = o.getVelY();
		float terminalVel = o.getTerminalVel();
		float newVelY = currentVelY + gravityRate;
		if(newVelY > terminalVel) {
			o.setVelY(terminalVel);
		}else {
			o.setVelY(newVelY);
		}
	}
	
	void setVelY(float velY);
	float getVelY();
	float getTerminalVel();

}
