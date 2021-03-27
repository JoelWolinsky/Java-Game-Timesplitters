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

	//increment the current panoramic view, if we reach the end wrap around
	public void incrementStateIndex(){

		stateIndex++;

		if (stateIndex>=levels.get(0).getStateSize())
			stateIndex=0;

	}

	//get the current panoramic view for the specified (index) level
	public String getCurrent(int index)
	{
		return levels.get(index).getState(stateIndex);
	}


	public String getPrevious(int index)
	{
		if (stateIndex-1<0)
			return levels.get(index).getState(levels.get(index).getStateSize()-1);
		else
			return levels.get(index).getState(stateIndex-1);
	}

	public String getNext(int index)
	{
		if (stateIndex>=levels.get(index).getStateSize())
			return levels.get(index).getState(0);
		else
			return levels.get(index).getState(stateIndex+1);
	}



}
