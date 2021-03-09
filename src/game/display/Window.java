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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.ConfigHandler.ConfigOption;
import game.Game;
import game.GameState;
import game.Launcher;
import game.SoundHandler;

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
		c.setPreferredSize(d);
		c.setMaximumSize(d);
		c.setMinimumSize(d);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		Font buttonFont = null;
		try {
		InputStream is = new FileInputStream("./img/PressStart2P.ttf");
		buttonFont = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Font sizedFont = buttonFont.deriveFont(11f);
		
		int panelWidth = WIDTH / 2;
		int panelHeight = ((HEIGHT / 2) - (WIDTH / 10));
		
		// The panel that holds the 4 buttons on this page
		JPanel mainMenu = new JPanel(new FlowLayout(FlowLayout.LEFT));
		mainMenu.setBounds(WIDTH / 23, (HEIGHT / 2) + 50, panelWidth, panelHeight);
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
		backButtonPanel.setBounds(WIDTH / 23, HEIGHT - (HEIGHT / 7), panelWidth, panelHeight / 4 + 20);
		backButtonPanel.setOpaque(false);
		backButtonPanel.setVisible(false);
		
		// The panel that holds the multiplayer buttons
		JPanel multiplayerButtonPanel = new JPanel(new FlowLayout());
		multiplayerButtonPanel.setBounds(WIDTH / 4, HEIGHT / 10, panelWidth, panelHeight * 2);
		multiplayerButtonPanel.setOpaque(false);
		multiplayerButtonPanel.setVisible(false);
		((FlowLayout)multiplayerButtonPanel.getLayout()).setVgap(panelHeight / 6);
		
		// Handling the main screen background image

		ImageIcon background = new ImageIcon("./img/background.png");
	    Image img = background.getImage();
	    Image temp = img.getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);
	    background = new ImageIcon(temp);
	    JLabel back = new JLabel(background);
	    back.setLayout(null);
	    back.setBounds(0,0,WIDTH,HEIGHT);
	    back.setVisible(true);
	    
	    // Handling the options screen background
		ImageIcon optionsBackground = new ImageIcon("./img/backgroundOptions.jpg");
	    Image img2 = optionsBackground.getImage();
	    Image temp2 = img2.getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);
	    optionsBackground = new ImageIcon(temp2);
	    JLabel backOptions = new JLabel(optionsBackground);
	    backOptions.setLayout(null);
	    backOptions.setBounds(0,0,WIDTH,HEIGHT);
	    backOptions.setVisible(false);
	    
	    // Importing button graphics
		ImageIcon button1Icon = new ImageIcon("./img/button1.png");
		Image scaledButton1 = button1Icon.getImage().getScaledInstance(panelWidth - 50, (panelHeight / 4) - 10,Image.SCALE_SMOOTH);
		button1Icon = new ImageIcon(scaledButton1);
		final ImageIcon button1Inner = button1Icon;
		
		ImageIcon button1ClickedIcon = new ImageIcon("./img/button1Clicked.png");
		Image scaledbutton1ClickedIcon = button1ClickedIcon.getImage().getScaledInstance(panelWidth - 50, (panelHeight / 4) - 10,Image.SCALE_SMOOTH);
		button1ClickedIcon = new ImageIcon(scaledbutton1ClickedIcon);
		final ImageIcon button1ClickedInner = button1ClickedIcon;
		
		ImageIcon button1HoverIcon = new ImageIcon("./img/button1Hover.png");
		Image scaledButton1HoverIcon = button1HoverIcon.getImage().getScaledInstance(panelWidth - 50, (panelHeight / 4) - 10,Image.SCALE_SMOOTH);
		button1HoverIcon = new ImageIcon(scaledButton1HoverIcon);
		final ImageIcon button1HoverInner = button1HoverIcon;
		
		ImageIcon button2Icon = new ImageIcon("./img/button2.png");
		Image scaledButton2 = button2Icon.getImage().getScaledInstance(panelWidth / 2, (panelHeight / 4) , Image.SCALE_SMOOTH);
		button2Icon = new ImageIcon(scaledButton2);
		final ImageIcon button2Inner = button2Icon;
		
		ImageIcon button2HoverIcon = new ImageIcon("./img/button2Hover.png");
		Image scaledButton2Hover = button2HoverIcon.getImage().getScaledInstance(panelWidth / 2, (panelHeight / 4) , Image.SCALE_SMOOTH);
		button2HoverIcon = new ImageIcon(scaledButton2Hover);
		final ImageIcon button2HoverInner = button2HoverIcon;
		
		ImageIcon button2ClickedIcon = new ImageIcon("./img/button2Clicked.png");
		Image scaledButton2Clicked = button2HoverIcon.getImage().getScaledInstance(panelWidth / 2, (panelHeight / 4) , Image.SCALE_SMOOTH);
		button2ClickedIcon = new ImageIcon(scaledButton2Clicked);
		final ImageIcon button2ClickedInner = button2ClickedIcon;
		
		JLabel singleplayerButton = new JLabel(button1Icon);
		singleplayerButton.setFont(sizedFont);
		singleplayerButton.setText("SINGLEPLAYER");
		singleplayerButton.setHorizontalTextPosition(JLabel.CENTER);
		singleplayerButton.setForeground(Color.black);
		
		singleplayerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				singleplayerButton.setIcon(button1ClickedInner);
				SoundHandler.playSound("button1", 1f);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				mainMenu.setVisible(false);
				back.setVisible(false);
					    	
				Game.state = GameState.Playing;
			}
				  
			@Override
			public void mouseEntered(MouseEvent e) {
				singleplayerButton.setIcon(button1HoverInner);
			}
				  
			@Override
			public void mouseExited(MouseEvent e) {
				singleplayerButton.setIcon(button1Inner);
			}
		});
		
		JLabel multiplayerButton = new JLabel(button1Icon);
		multiplayerButton.setForeground(Color.black);
		multiplayerButton.setFont(sizedFont);
		multiplayerButton.setText("MULTIPLAYER");
		multiplayerButton.setHorizontalTextPosition(JLabel.CENTER);
		
		multiplayerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				multiplayerButton.setIcon(button1ClickedInner);
		    	SoundHandler.playSound("button1", 1f);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
		    	mainMenu.setVisible(false);
		    	back.setVisible(false);
		    	backOptions.setVisible(true);
		    	backButtonPanel.setVisible(true);
		    	multiplayerButtonPanel.setVisible(true);
			}
				  
			@Override
			public void mouseEntered(MouseEvent e) {
				multiplayerButton.setIcon(button1HoverInner);
			}
				  
			@Override
			public void mouseExited(MouseEvent e) {
				multiplayerButton.setIcon(button1Inner);
			}
		});
		
		JLabel optionsButton = new JLabel(button1Icon);
		optionsButton.setForeground(Color.black);
		optionsButton.setFont(sizedFont);
		optionsButton.setText("OPTIONS");
		optionsButton.setHorizontalTextPosition(JLabel.CENTER);
		
		optionsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				optionsButton.setIcon(button1ClickedInner);
		    	SoundHandler.playSound("button1", 1f);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
		    	mainMenu.setVisible(false);
		    	back.setVisible(false);
		    	backOptions.setVisible(true);
		    	optionButtonPanel.setVisible(true);
		    	backButtonPanel.setVisible(true);
			}
				  
			@Override
			public void mouseEntered(MouseEvent e) {
				optionsButton.setIcon(button1HoverInner);
			}
				  
			@Override
			public void mouseExited(MouseEvent e) {
				optionsButton.setIcon(button1Inner);
			}
		});
		
		JLabel quitButton = new JLabel(button1Icon);
		quitButton.setForeground(Color.black);
		quitButton.setFont(sizedFont);
		quitButton.setText("QUIT GAME");
		quitButton.setHorizontalTextPosition(JLabel.CENTER);
		
		quitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				quitButton.setIcon(button1ClickedInner);
		    	SoundHandler.playSound("button1", 1f);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}
				  
			@Override
			public void mouseEntered(MouseEvent e) {
				quitButton.setIcon(button1HoverInner);
			}
				  
			@Override
			public void mouseExited(MouseEvent e) {
				quitButton.setIcon(button1Inner);
			}
		});
		
		JLabel backButton = new JLabel(button2Icon);
		backButton.setForeground(Color.black);
		backButton.setFont(sizedFont);
		backButton.setText("BACK");
		backButton.setHorizontalTextPosition(JLabel.CENTER);
		
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				backButton.setIcon(button2ClickedInner);
				SoundHandler.playSound("button1", 1f);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				backButton.setIcon(button2Inner);
		    	backButtonPanel.setVisible(false);
		    	optionButtonPanel.setVisible(false);
		    	multiplayerButtonPanel.setVisible(false);
		    	mainMenu.setVisible(true);
		    	backOptions.setVisible(false);
		    	back.setVisible(true);
			}
				  
			@Override
			public void mouseEntered(MouseEvent e) {
				backButton.setIcon(button2HoverInner);
			}
				  
			@Override
			public void mouseExited(MouseEvent e) {
				backButton.setIcon(button2Inner);
			}
		});
		
		JButton toggleSoundEffectsButton = new JButton();
		toggleSoundEffectsButton.setBackground(Color.DARK_GRAY);
		toggleSoundEffectsButton.setForeground(Color.black);
		toggleSoundEffectsButton.setPreferredSize(new Dimension(panelWidth / 2, panelHeight / 3));
		toggleSoundEffectsButton.setFont(sizedFont);
		toggleSoundEffectsButton.setFocusPainted(false);
		
		if (Launcher.cHandler.getSoundEffectsToggle()) {
			toggleSoundEffectsButton.setText("<html><center>SOUND EFFECTS:<br>ON</center></html>");
		} else {
			toggleSoundEffectsButton.setText("<html><center>SOUND EFFECTS:<br>OFF</center></html>");
		}
		
		toggleSoundEffectsButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	SoundHandler.playSound("button1", 1f);
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
		toggleMusicButton.setFont(sizedFont);
		toggleMusicButton.setFocusPainted(false);
		
		if (Launcher.cHandler.getSoundEffectsToggle()) {
			toggleMusicButton.setText("<html><center>MUSIC:<br>ON</center></html>");
		} else {
			toggleMusicButton.setText("<html><center>MUSIC:<br>OFF</center></html>");
		}
		
		toggleMusicButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	SoundHandler.playSound("button1", 1f);
		    	if (Launcher.cHandler.getMusicToggle()) {
		    		toggleMusicButton.setText("<html><center>MUSIC:<br>OFF</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.MUSIC, "False");
		    	} else {
		    		toggleMusicButton.setText("<html><center>MUSIC:<br>ON</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.MUSIC, "True");
		    	}
		    }
		});
		
		JButton createGameButton = new JButton();
		createGameButton.setBackground(Color.DARK_GRAY);
		createGameButton.setForeground(Color.black);
		createGameButton.setPreferredSize(new Dimension(panelWidth, panelHeight / 3));
		createGameButton.setFont(sizedFont);
		createGameButton.setFocusPainted(false);
		createGameButton.setText("CREATE GAME");
		
		JButton joinGameButton = new JButton();
		joinGameButton.setBackground(Color.DARK_GRAY);
		joinGameButton.setForeground(Color.black);
		joinGameButton.setPreferredSize(new Dimension(panelWidth, panelHeight / 3));
		joinGameButton.setFont(sizedFont);
		joinGameButton.setFocusPainted(false);
		joinGameButton.setText("JOIN GAME");
		
		// Add all components then set the frame to visible
		mainMenu.add(singleplayerButton);
		mainMenu.add(multiplayerButton);
		mainMenu.add(optionsButton);
		mainMenu.add(quitButton);
		backButtonPanel.add(backButton);
		optionButtonPanel.add(toggleSoundEffectsButton);
		optionButtonPanel.add(toggleMusicButton);
		multiplayerButtonPanel.add(createGameButton);
		multiplayerButtonPanel.add(joinGameButton);
		c.add(mainMenu);
		c.add(optionButtonPanel);
		c.add(multiplayerButtonPanel);
		c.add(backButtonPanel);
		c.add(back);
		c.add(backOptions);
		
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		game.start();
	}
	
	

}
