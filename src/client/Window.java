package client;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

import server.Game;

public class Window extends Canvas{

	private static final long serialVersionUID = 1877720651231192133L;
	
	public Window(int width, int height, String title, Game game) {
		JFrame frame = new JFrame(title);
		Dimension d = new Dimension(width, height);
		Container c = frame.getContentPane();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		c.setPreferredSize(d);
		c.setMaximumSize(d);
		c.setMinimumSize(d);
		

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(game);
		frame.setVisible(true);
		frame.pack();
		game.start();
	}

}
