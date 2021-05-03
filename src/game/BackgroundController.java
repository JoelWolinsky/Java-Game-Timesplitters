package game;

import java.util.ArrayList;
import java.util.Arrays;

public class BackgroundController {

	private int stateIndex=0;


	private ArrayList<BackgroundStates> levels = new ArrayList<BackgroundStates>();

	public BackgroundController(BackgroundStates...bs) {

		for (BackgroundStates x : bs)
			levels.add(x);

	}

	/**
	 * This function increments the current panoramic view, if we reach the end wrap around
	 */
	public void incrementStateIndex(){

		stateIndex++;

		if (stateIndex>=levels.get(0).getStateSize())
			stateIndex=0;

	}

	/**
	 * @param index An integer representing the level for which the panoramic view should be returned.
	 * @return The current panoramic view for the specified level
	 */
	public String getCurrent(int index)
	{
		return levels.get(index).getState(stateIndex);
	}

	
	/**
	 * @param index An integer representing the current level
	 * @return The panoramic view for the previous level
	 */
	public String getPrevious(int index)
	{
		if (stateIndex-1<0)
			return levels.get(index).getState(levels.get(index).getStateSize()-1);
		else
			return levels.get(index).getState(stateIndex-1);
	}
	
	/**
	 * @param index An integer representing the current level
	 * @return The panoramic view for the next level
	 */
	public String getNext(int index)
	{
		if (stateIndex>=levels.get(index).getStateSize())
			return levels.get(index).getState(0);
		else
			return levels.get(index).getState(stateIndex+1);
	}



}
