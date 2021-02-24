package game.display;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.ConfigHandler.ConfigOption;
import game.Game;
import game.GameState;
import game.Launcher;

public class Window extends Canvas{
	
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final String TITLE = "Engine";
	public static Rectangle windowRect = new Rectangle(0,0,Game.WIDTH, Game.HEIGHT);

	private static final long serialVersionUID = 1877720651231192133L;
	
	public JFrame frame;
	public static Dimension d = new Dimension(WIDTH,HEIGHT);
	
	public Window(Game game) {
		frame = new JFrame(TITLE);
		Container c = frame.getContentPane();
		
		String buttonFont = "Bauhaus 93";
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		c.setPreferredSize(d);
		c.setMaximumSize(d);
		c.setMinimumSize(d);
		

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		int panelWidth = WIDTH / 2;
		int panelHeight = ((HEIGHT / 2) - (WIDTH / 10));
		
		// The panel that holds the 4 buttons on this page
		JPanel mainMenu = new JPanel(new FlowLayout(FlowLayout.LEFT));
		mainMenu.setBounds(WIDTH / 20, (HEIGHT / 2) + 15, panelWidth, panelHeight);
		mainMenu.setOpaque(false);
		((FlowLayout)mainMenu.getLayout()).setVgap(panelHeight / 19);
		
		// The panel that holds the options buttons
		JPanel optionButtonPanel = new JPanel(new FlowLayout());
		optionButtonPanel.setBounds(WIDTH / 4, HEIGHT / 15, panelWidth, panelHeight);
		optionButtonPanel.setOpaque(false);
		optionButtonPanel.setVisible(false);
		((FlowLayout)optionButtonPanel.getLayout()).setVgap(panelHeight / 12);
		
		// The panel that holds the "back" button
		JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		backButtonPanel.setBounds(WIDTH / 23, HEIGHT - (HEIGHT / 5), panelWidth, panelHeight / 5);
		backButtonPanel.setOpaque(false);
		backButtonPanel.setVisible(false);
		
		// Handling the main screen background image
		ImageIcon background = new ImageIcon("img/background.jpg");
	    Image img = background.getImage();
	    Image temp = img.getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);
	    background = new ImageIcon(temp);
	    JLabel back = new JLabel(background);
	    back.setLayout(null);
	    back.setBounds(0,0,WIDTH,HEIGHT);
	    
	    // Handling the options screen background
		ImageIcon optionsBackground = new ImageIcon("img/backgroundOptions.jpg");
	    Image img2 = optionsBackground.getImage();
	    Image temp2 = img2.getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);
	    optionsBackground = new ImageIcon(temp2);
	    JLabel backOptions = new JLabel(optionsBackground);
	    backOptions.setLayout(null);
	    backOptions.setBounds(0,0,WIDTH,HEIGHT);
	    backOptions.setVisible(false);
	    
	    // Declaring buttons and their respective behaviour
		
		JButton singleplayerButton = new JButton();
		singleplayerButton.setBackground(Color.DARK_GRAY);
		singleplayerButton.setForeground(Color.black);
		singleplayerButton.setPreferredSize(new Dimension(panelWidth - 50, (panelHeight / 4) - 15));
		singleplayerButton.setFont(new Font(buttonFont, Font.BOLD, 12));
		singleplayerButton.setText("SINGLEPLAYER");
		singleplayerButton.setFocusPainted(false);
		
		singleplayerButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	mainMenu.setVisible(false);
		    	back.setVisible(false);
		    	
		    	Game.state = GameState.Playing;
		    }
		});
		
		JButton multiplayerButton = new JButton();
		multiplayerButton.setBackground(Color.DARK_GRAY);
		multiplayerButton.setForeground(Color.black);
		multiplayerButton.setPreferredSize(new Dimension(panelWidth - 50, (panelHeight / 4) - 15));
		multiplayerButton.setFont(new Font(buttonFont, Font.BOLD, 12));
		multiplayerButton.setText("MULTIPLAYER");
		multiplayerButton.setFocusPainted(false);
		
		JButton optionsButton = new JButton();
		optionsButton.setBackground(Color.DARK_GRAY);
		optionsButton.setForeground(Color.black);
		optionsButton.setPreferredSize(new Dimension(panelWidth - 50, (panelHeight / 4) - 15));
		optionsButton.setFont(new Font(buttonFont, Font.BOLD, 12));
		optionsButton.setText("OPTIONS");
		optionsButton.setFocusPainted(false);
		
		optionsButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	mainMenu.setVisible(false);
		    	back.setVisible(false);
		    	backOptions.setVisible(true);
		    	optionButtonPanel.setVisible(true);
		    	backButtonPanel.setVisible(true);
		    	// TODO: DO OPTION BUTTONS
		    }
		});
		
		JButton quitButton = new JButton();
		quitButton.setBackground(Color.DARK_GRAY);
		quitButton.setForeground(Color.black);
		quitButton.setPreferredSize(new Dimension(panelWidth - 50, (panelHeight / 4) - 15));
		quitButton.setFont(new Font(buttonFont, Font.BOLD, 12));
		quitButton.setText("QUIT GAME");
		quitButton.setFocusPainted(false);
		
		quitButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	System.exit(0);
		    }
		});
		
		JButton backButton = new JButton();
		backButton.setBackground(Color.DARK_GRAY);
		backButton.setForeground(Color.black);
		backButton.setPreferredSize(new Dimension(panelWidth / 2, (panelHeight / 5) - 5));
		backButton.setFont(new Font(buttonFont, Font.BOLD, 12));
		backButton.setText("BACK");
		backButton.setFocusPainted(false);
		
		backButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	backButtonPanel.setVisible(false);
		    	optionButtonPanel.setVisible(false);
		    	mainMenu.setVisible(true);
		    	backOptions.setVisible(false);
		    	back.setVisible(true);
		    }
		});
		
		JButton toggleSoundEffectsButton = new JButton();
		toggleSoundEffectsButton.setBackground(Color.DARK_GRAY);
		toggleSoundEffectsButton.setForeground(Color.black);
		toggleSoundEffectsButton.setPreferredSize(new Dimension(panelWidth / 2, panelHeight / 3));
		toggleSoundEffectsButton.setFont(new Font(buttonFont, Font.BOLD, 12));
		toggleSoundEffectsButton.setFocusPainted(false);
		
		if (Launcher.cHandler.getSoundEffectsToggle()) {
			toggleSoundEffectsButton.setText("<html><center>SOUND EFFECTS:<br>ON</center></html>");
		} else {
			toggleSoundEffectsButton.setText("<html><center>SOUND EFFECTS:<br>OFF</center></html>");
		}
		
		toggleSoundEffectsButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	if (Launcher.cHandler.getSoundEffectsToggle()) {
		    		toggleSoundEffectsButton.setText("<html><center>SOUND EFFECTS:<br>OFF</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.SOUNDEFFECTS, "False");
		    	} else {
		    		toggleSoundEffectsButton.setText("<html><center>SOUND EFFECTS:<br>ON</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.SOUNDEFFECTS, "True");
		    	}
		    }
		});
		
		JButton toggleMusicButton = new JButton();
		toggleMusicButton.setBackground(Color.DARK_GRAY);
		toggleMusicButton.setForeground(Color.black);
		toggleMusicButton.setPreferredSize(new Dimension(panelWidth / 2, panelHeight / 3));
		toggleMusicButton.setFont(new Font(buttonFont, Font.BOLD, 12));
		toggleMusicButton.setFocusPainted(false);
		
		if (Launcher.cHandler.getSoundEffectsToggle()) {
			toggleMusicButton.setText("<html><center>MUSIC:<br>ON</center></html>");
		} else {
			toggleMusicButton.setText("<html><center>MUSIC:<br>OFF</center></html>");
		}
		
		toggleMusicButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	if (Launcher.cHandler.getMusicToggle()) {
		    		toggleMusicButton.setText("<html><center>MUSIC:<br>OFF</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.MUSIC, "False");
		    	} else {
		    		toggleMusicButton.setText("<html><center>MUSIC:<br>ON</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.MUSIC, "True");
		    	}
		    }
		});
		
		// Add all components then set the frame to visible
		mainMenu.add(singleplayerButton);
		mainMenu.add(multiplayerButton);
		mainMenu.add(optionsButton);
		mainMenu.add(quitButton);
		backButtonPanel.add(backButton);
		optionButtonPanel.add(toggleSoundEffectsButton);
		optionButtonPanel.add(toggleMusicButton);
		c.add(mainMenu);
		c.add(optionButtonPanel);
		c.add(backButtonPanel);
		
		frame.add(game);
		frame.setVisible(true);
		frame.pack();
		game.start();
	}

}
