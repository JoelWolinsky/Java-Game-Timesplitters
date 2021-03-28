package game.display;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.ConfigHandler.ConfigOption;
import game.Game;
import game.GameState;
import game.Launcher;
import game.SoundHandler;
import network.GameClient;
import network.GameServer;

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
		c.setBackground(Color.BLACK);
		
		// Creating a new Font from the PressStart2P.ttf file that can be used in HTML JLabels
		Font buttonFont = null;
		try {
		InputStream is = new FileInputStream("./img/PressStart2P.ttf");
		buttonFont = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Font sizedFont = buttonFont.deriveFont(11f);
		GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(sizedFont);
		
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
		
		// The panel that holds the back button
		JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		backButtonPanel.setBounds(WIDTH / 23, HEIGHT - (HEIGHT / 7), panelWidth, panelHeight / 4 + 20);
		backButtonPanel.setOpaque(false);
		backButtonPanel.setVisible(false);
		
		// The panel that holds the multiplayer buttons
		JPanel multiplayerButtonPanel = new JPanel(new FlowLayout());
		multiplayerButtonPanel.setBounds(WIDTH / 4, HEIGHT / 10, panelWidth, panelHeight * 2);
		multiplayerButtonPanel.setOpaque(false);
		multiplayerButtonPanel.setVisible(false);
		((FlowLayout)multiplayerButtonPanel.getLayout()).setVgap(panelHeight / 4);
		
		// Handling the main screen background image
		ImageIcon backgroundMain = new ImageIcon("./img/backgroundMain.gif");
	    Image temp = backgroundMain.getImage().getScaledInstance(WIDTH,HEIGHT,Image.SCALE_DEFAULT);
	    backgroundMain = new ImageIcon(temp);
	    JLabel back = new JLabel(backgroundMain);
	    back.setLayout(null);
	    back.setBounds(0,0,WIDTH,HEIGHT);
	    back.setVisible(true);
	    
	    ImageIcon backgroundOptions = new ImageIcon("./img/backgroundOptions.gif");
	    Image temp2 = backgroundOptions.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
	    backgroundOptions = new ImageIcon(temp2);
	    JLabel backOptions = new JLabel(backgroundOptions);
	    backOptions.setLayout(null);
	    backOptions.setBounds(0,0,WIDTH,HEIGHT);
	    backOptions.setVisible(false);
	    
	    ImageIcon backgroundMultiplayer = new ImageIcon("./img/backgroundMultiplayer.gif");
	    Image temp3 = backgroundMultiplayer.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
	    backgroundMultiplayer = new ImageIcon(temp3);
	    JLabel backMultiplayer = new JLabel(backgroundMultiplayer);
	    backMultiplayer.setLayout(null);
	    backMultiplayer.setBounds(0, 0, WIDTH, HEIGHT);
	    backMultiplayer.setVisible(false);
	    
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
		Image scaledButton2Clicked = button2ClickedIcon.getImage().getScaledInstance(panelWidth / 2, (panelHeight / 4) , Image.SCALE_SMOOTH);
		button2ClickedIcon = new ImageIcon(scaledButton2Clicked);
		final ImageIcon button2ClickedInner = button2ClickedIcon;
		
		ImageIcon button3Icon = new ImageIcon("./img/button3.png");
		Image scaledButton3 = button3Icon.getImage().getScaledInstance(panelWidth - 100, panelHeight / 3, Image.SCALE_SMOOTH);
		button3Icon = new ImageIcon(scaledButton3);
		final ImageIcon button3Inner = button3Icon;
		
		ImageIcon button3HoverIcon = new ImageIcon("./img/button3Hover.png");
		Image scaledButton3Hover = button3HoverIcon.getImage().getScaledInstance(panelWidth - 100,  panelHeight / 3, Image.SCALE_SMOOTH);
		button3HoverIcon = new ImageIcon(scaledButton3Hover);
		final ImageIcon button3HoverInner = button3HoverIcon;
		
		ImageIcon button3ClickedIcon = new ImageIcon("./img/button3Clicked.png");
		Image scaledButton3Clicked = button3ClickedIcon.getImage().getScaledInstance(panelWidth - 100, panelHeight / 3, Image.SCALE_SMOOTH);
		button3ClickedIcon = new ImageIcon(scaledButton3Clicked);
		final ImageIcon button3ClickedInner = button3ClickedIcon;
		
		
		// The singleplayer Button on the main screen
		JLabel singleplayerButton = new JLabel(button1Icon);
		singleplayerButton.setFont(sizedFont);
		singleplayerButton.setText("SINGLEPLAYER");
		singleplayerButton.setHorizontalTextPosition(JLabel.CENTER);
		singleplayerButton.setForeground(Color.black);
		
		singleplayerButton.addMouseListener(new MouseAdapter() {
			
			// This handles when the JLabel is pressed by the mouse
			@Override
			public void mousePressed(MouseEvent e) {
				singleplayerButton.setIcon(button1ClickedInner);
				SoundHandler.playSound("button1", 1f);
			}
			
			// This handles when the JLabel is released by the mouse
			@Override
			public void mouseReleased(MouseEvent e) {
				mainMenu.setVisible(false);
				back.setVisible(false);
				backOptions.setVisible(false);
				backMultiplayer.setVisible(false);
				
				Game.state = GameState.Playing;
				game.start();
			}
			
			// This handles when the mouse enters the JLabel
			@Override
			public void mouseEntered(MouseEvent e) {
				singleplayerButton.setIcon(button1HoverInner);
			}
				
			// This handles when the mouse leaves the JLabel
			@Override
			public void mouseExited(MouseEvent e) {
				singleplayerButton.setIcon(button1Inner);
			}
		});
		
		
		// The rest of the button behaviours are handled in the same way
		
		// The multiplayer button on the main screen
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
		    	backMultiplayer.setVisible(true);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				backButtonPanel.setVisible(true);
		    	multiplayerButtonPanel.setVisible(true);
				mainMenu.setVisible(false);
				back.setVisible(false);
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
		
		// The options button on the main screen
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
		    	backOptions.setVisible(true);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				optionButtonPanel.setVisible(true);
		    	backButtonPanel.setVisible(true);
				mainMenu.setVisible(false);
				back.setVisible(false);
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
		
		// The quit button on the main screen
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
		
		// The back button on the options and multiplayer screens
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
				back.setVisible(true);
				backButton.setIcon(button2Inner);
				mainMenu.setVisible(true);
		    	backButtonPanel.setVisible(false);
		    	optionButtonPanel.setVisible(false);
		    	multiplayerButtonPanel.setVisible(false);
		    	backOptions.setVisible(false);
		    	backMultiplayer.setVisible(false);
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
		
		// The toggle sound effects button on the options screen
		JLabel toggleSoundEffectsButton = new JLabel(button3Icon);
		toggleSoundEffectsButton.setForeground(Color.black);
		toggleSoundEffectsButton.setFont(sizedFont);
		toggleSoundEffectsButton.setHorizontalTextPosition(JLabel.CENTER);
		
		if (Launcher.cHandler.soundEffectsToggle) {
			toggleSoundEffectsButton.setText("<html><center>SOUND EFFECTS:<br><p style='margin-top:8'>ON</center></html>");
		} else {
			toggleSoundEffectsButton.setText("<html><center>SOUND EFFECTS:<br><p style='margin-top:8'>OFF</center></html>");
		}
		
		toggleSoundEffectsButton.addMouseListener(new MouseAdapter() {
			@Override 
			public void mousePressed(MouseEvent e) {
				toggleSoundEffectsButton.setIcon(button3ClickedInner);
				SoundHandler.playSound("button1", 1f);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
		    	if (Launcher.cHandler.soundEffectsToggle) {
		    		toggleSoundEffectsButton.setText("<html><center>SOUND EFFECTS:<br><p style='margin-top:8'>OFF</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.SOUNDEFFECTS, "False");
		    		Launcher.cHandler.soundEffectsToggle = false;
		    	} else {
		    		toggleSoundEffectsButton.setText("<html><center>SOUND EFFECTS:<br><p style='margin-top:8'>ON</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.SOUNDEFFECTS, "True");
		    		Launcher.cHandler.soundEffectsToggle = true;
		    	}
	
		    	toggleSoundEffectsButton.setIcon(button3HoverInner);
			}
				  
			@Override
			public void mouseEntered(MouseEvent e) {
				toggleSoundEffectsButton.setIcon(button3HoverInner);
			}
				  
			@Override
			public void mouseExited(MouseEvent e) {
				toggleSoundEffectsButton.setIcon(button3Inner);
			}
		});
		
		// The toggle music button on the options screen
		JLabel toggleMusicButton = new JLabel(button3Icon);
		toggleMusicButton.setForeground(Color.black);
		toggleMusicButton.setFont(sizedFont);
		toggleMusicButton.setHorizontalTextPosition(JLabel.CENTER);
		
		if (Launcher.cHandler.musicToggle) {
			toggleMusicButton.setText("<html><center>MUSIC:<br><p style='margin-top:8'>ON</center></html>");
		} else {
			toggleMusicButton.setText("<html><center>MUSIC:<br><p style='margin-top:8'>OFF</center></html>");
		}
		
		toggleMusicButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				toggleMusicButton.setIcon(button3ClickedInner);
				SoundHandler.playSound("button1", 1f);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
		    	if (Launcher.cHandler.musicToggle) {
		    		toggleMusicButton.setText("<html><center>MUSIC:<br><p style='margin-top:8'>OFF</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.MUSIC, "False");
		    		Launcher.cHandler.musicToggle = false;
		    	} else {
		    		toggleMusicButton.setText("<html><center>MUSIC:<br><p style='margin-top:8'>ON</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.MUSIC, "True");
		    		Launcher.cHandler.musicToggle = true;
		    	}
	
		    	toggleMusicButton.setIcon(button3HoverInner);
			}
				  
			@Override
			public void mouseEntered(MouseEvent e) {
				toggleMusicButton.setIcon(button3HoverInner);
			}
				  
			@Override
			public void mouseExited(MouseEvent e) {
				toggleMusicButton.setIcon(button3Inner);
			}
		});
		
		// The play vs computer button on the multiplayer screen
		JLabel playVSComputerButton = new JLabel(button3Icon);
		playVSComputerButton.setForeground(Color.black);
		playVSComputerButton.setFont(sizedFont);
		playVSComputerButton.setHorizontalTextPosition(JLabel.CENTER);
		playVSComputerButton.setText("PLAY VS COMPUTER");
		
		playVSComputerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				playVSComputerButton.setIcon(button3ClickedInner);
				SoundHandler.playSound("button1", 1f);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				playVSComputerButton.setIcon(button3HoverInner);
				
				back.setVisible(false);
		    	backButtonPanel.setVisible(false);
		    	multiplayerButtonPanel.setVisible(false);
		    	backOptions.setVisible(false);
				backMultiplayer.setVisible(false);
			}
				  
			@Override
			public void mouseEntered(MouseEvent e) {
				playVSComputerButton.setIcon(button3HoverInner);
			}
				  
			@Override
			public void mouseExited(MouseEvent e) {
				playVSComputerButton.setIcon(button3Inner);
			}
		});
		
		// The create game button on the multiplayer screen
		JLabel createGameButton = new JLabel(button3Icon);
		createGameButton.setForeground(Color.black);
		createGameButton.setFont(sizedFont);
		createGameButton.setHorizontalTextPosition(JLabel.CENTER);
		createGameButton.setText("CREATE GAME");
		
		createGameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				createGameButton.setIcon(button3ClickedInner);
				SoundHandler.playSound("button1", 1f);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				createGameButton.setIcon(button3HoverInner);
				
				Game.socketServer = new GameServer(game);
				Game.socketServer.start();
				Game.socketClient = new GameClient(game, "localhost");
				Game.socketClient.start();
				
				back.setVisible(false);
		    	backButtonPanel.setVisible(false);
		    	multiplayerButtonPanel.setVisible(false);
		    	backOptions.setVisible(false);
				backMultiplayer.setVisible(false);
		    	
		    	Game.state = GameState.Playing;
		    	Game.isMultiplayer = true;
		    	
		    	game.start();
			}
				  
			@Override
			public void mouseEntered(MouseEvent e) {
				createGameButton.setIcon(button3HoverInner);
			}
				  
			@Override
			public void mouseExited(MouseEvent e) {
				createGameButton.setIcon(button3Inner);
			}
		});
		
		// The join game button on the multiplayer screen
		JLabel joinGameButton = new JLabel(button3Icon);
		joinGameButton.setForeground(Color.black);
		joinGameButton.setFont(sizedFont);
		joinGameButton.setHorizontalTextPosition(JLabel.CENTER);
		joinGameButton.setText("JOIN GAME");
		
		joinGameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				joinGameButton.setIcon(button3ClickedInner);
				SoundHandler.playSound("button1", 1f);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				joinGameButton.setIcon(button3HoverInner);
				
				Game.socketClient = new GameClient(game, "localhost");
				Game.socketClient.start();
				
				back.setVisible(false);
		    	backButtonPanel.setVisible(false);
		    	multiplayerButtonPanel.setVisible(false);
		    	backOptions.setVisible(false);
				backMultiplayer.setVisible(false);
		    	
		    	Game.state = GameState.Playing;
		    	Game.isMultiplayer = true;
		    	
		    	game.start();
			}
				  
			@Override
			public void mouseEntered(MouseEvent e) {
				joinGameButton.setIcon(button3HoverInner);
			}
				  
			@Override
			public void mouseExited(MouseEvent e) {
				joinGameButton.setIcon(button3Inner);
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
		multiplayerButtonPanel.add(playVSComputerButton);
		multiplayerButtonPanel.add(createGameButton);
		multiplayerButtonPanel.add(joinGameButton);
		c.add(mainMenu);
		c.add(optionButtonPanel);
		c.add(multiplayerButtonPanel);
		c.add(backButtonPanel);
		c.add(back);
		c.add(backOptions);
		c.add(backMultiplayer);
		
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}
