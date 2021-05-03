package game;

import java.util.ArrayList;

public class BackgroundStates {

	private static ArrayList<String> states = new ArrayList<String>();

	/**
	 * @param urls A list of background images to be loaded
	 */
	public BackgroundStates(String...urls) {

		//load up the images inside of the private arraylist
		for (String x : urls)
			states.add(x);

	}

	/**
	 * @param index The index representing the current level
	 * @return The required state for the given level index
	 */
	public String getState(int index) {

		if (index<0)
			return states.get(getStateSize()-1);
		else if (index==getStateSize())
				return states.get(0);
			else
				return states.get(index);
	}

	public int getStateSize(){
		return states.size();
	}
}
