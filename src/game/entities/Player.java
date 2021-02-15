package game.entities;

import game.Handler;

public class Player extends mob {
	 public Player(int x, int y, Handler input, String username) {
	        super("Player", x, y, 1);
	        this.input = input;
	        this.username = username;
	    }
}
