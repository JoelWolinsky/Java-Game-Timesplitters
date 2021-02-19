import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;

import javax.swing.*;

public class Window extends Canvas{
	
	JFrame frame;
	Container container;

	private static final long serialVersionUID = 1877720651231192133L;
	
	public Window(int width, int height, String title, Game game) {
		
		// Create the JFrame and set its values
		frame = new JFrame(title);
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		//frame.getContentPane().setBackground(Color.black);
		
		container = frame.getContentPane();
		int panelWidth = width / 2;
		int panelHeight = ((height / 2) - (width / 10));
		
		// The panel that holds the 4 buttons on this page
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.setBounds(width / 20, (height / 2) + 15, panelWidth, panelHeight);
		buttonPanel.setOpaque(false);
		
		((FlowLayout)buttonPanel.getLayout()).setVgap(panelHeight / 19);
		
		// Handling the background image
		
		ImageIcon background=new ImageIcon("img/background.jpg");
	    Image img=background.getImage();
	    Image temp=img.getScaledInstance(width,height,Image.SCALE_SMOOTH);
	    background=new ImageIcon(temp);
	    JLabel back=new JLabel(background);
	    back.setLayout(null);
	    back.setBounds(0,0,width,height);
		
		JButton singleplayerButton = new JButton();
		singleplayerButton.setBackground(Color.DARK_GRAY);
		singleplayerButton.setForeground(Color.black);
		singleplayerButton.setPreferredSize(new Dimension(panelWidth - 50, (panelHeight / 4) - 15));
		singleplayerButton.setFont(new Font("Bauhaus 93", Font.BOLD, 12));
		singleplayerButton.setText("SINGLEPLAYER");
		singleplayerButton.setFocusPainted(false);
		
		singleplayerButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	buttonPanel.setVisible(false);
		    	back.setVisible(false);
		    	
		    	// TODO: START THE GAME
		    }
		});
		
		JButton multiplayerButton = new JButton();
		multiplayerButton.setBackground(Color.DARK_GRAY);
		multiplayerButton.setForeground(Color.black);
		multiplayerButton.setPreferredSize(new Dimension(panelWidth - 50, (panelHeight / 4) - 15));
		multiplayerButton.setFont(new Font("Bauhaus 93", Font.BOLD, 12));
		multiplayerButton.setText("MULTIPLAYER");
		multiplayerButton.setFocusPainted(false);
		
		JButton optionsButton = new JButton();
		optionsButton.setBackground(Color.DARK_GRAY);
		optionsButton.setForeground(Color.black);
		optionsButton.setPreferredSize(new Dimension(panelWidth - 50, (panelHeight / 4) - 15));
		optionsButton.setFont(new Font("\"Bauhaus 93", Font.BOLD, 12));
		optionsButton.setText("OPTIONS");
		optionsButton.setFocusPainted(false);
		
		JButton quitButton = new JButton();
		quitButton.setBackground(Color.DARK_GRAY);
		quitButton.setForeground(Color.black);
		quitButton.setPreferredSize(new Dimension(panelWidth - 50, (panelHeight / 4) - 15));
		quitButton.setFont(new Font("Bauhaus 93", Font.BOLD, 12));
		quitButton.setText("QUIT");
		quitButton.setFocusPainted(false);
		
		quitButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	System.exit(0);
		    }
		});
		
		// Add all components then set the frame to visible
		buttonPanel.add(singleplayerButton);
		buttonPanel.add(multiplayerButton);
		buttonPanel.add(optionsButton);
		buttonPanel.add(quitButton);
		container.add(buttonPanel);
		
	    
	    frame.add(back);
		
		frame.setVisible(true);
		
	}

}
