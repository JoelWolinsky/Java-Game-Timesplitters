package game.display;
import java.awt.Canvas;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.ConfigHandler.ConfigOption;
import game.DifficultySettings;
import game.Game;
import game.GameState;
import game.Launcher;
import game.SoundHandler;
import game.entities.GameObject;
import game.graphics.GameMode;
import network.GameClient;
import network.GameServer;

import static game.Level.getGameObjects;

public class Window extends Canvas{

	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final String TITLE = "Engine";
	public static Rectangle windowRect = new Rectangle(0,0,Game.WIDTH, Game.HEIGHT);

	private static final long serialVersionUID = 1877720651231192133L;

	public JFrame frame;
	public static JPanel mainMenu;
	public static JLabel back,backOptions,backMultiplayer;
	public static Dimension d = new Dimension(WIDTH,HEIGHT);

	/**
	 * Sets up the window and UI elements and starts the game
	 * @param game
	 */
	public Window(Game game) {
		frame = new JFrame(TITLE);
		Container c = frame.getContentPane();
		c.setPreferredSize(d);
		c.setMaximumSize(d);
		c.setMinimumSize(d);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		c.setBackground(Color.BLACK);

		SoundHandler.playMusic("mainMenuMusic", 0.1f);

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

	    // Importing required image graphics
		ImageIcon button1Icon = new ImageIcon("./img/button1.png");
		Image scaledButton1 = button1Icon.getImage().getScaledInstance(panelWidth - 50, (panelHeight / 4) - 10,Image.SCALE_SMOOTH);
		button1Icon = new ImageIcon(scaledButton1);
		final ImageIcon BUTTON_1_INNER = button1Icon;

		ImageIcon button1ClickedIcon = new ImageIcon("./img/button1Clicked.png");
		Image scaledbutton1ClickedIcon = button1ClickedIcon.getImage().getScaledInstance(panelWidth - 50, (panelHeight / 4) - 10,Image.SCALE_SMOOTH);
		button1ClickedIcon = new ImageIcon(scaledbutton1ClickedIcon);
		final ImageIcon BUTTON_1_CLICKED_INNER = button1ClickedIcon;

		ImageIcon button1HoverIcon = new ImageIcon("./img/button1Hover.png");
		Image scaledButton1HoverIcon = button1HoverIcon.getImage().getScaledInstance(panelWidth - 50, (panelHeight / 4) - 10,Image.SCALE_SMOOTH);
		button1HoverIcon = new ImageIcon(scaledButton1HoverIcon);
		final ImageIcon BUTTON_1_HOVER_INNER = button1HoverIcon;

		ImageIcon button2Icon = new ImageIcon("./img/button2.png");
		Image scaledButton2 = button2Icon.getImage().getScaledInstance(panelWidth / 2, (panelHeight / 4) , Image.SCALE_SMOOTH);
		button2Icon = new ImageIcon(scaledButton2);
		final ImageIcon BUTTON_2_INNER = button2Icon;

		ImageIcon button2HoverIcon = new ImageIcon("./img/button2Hover.png");
		Image scaledButton2Hover = button2HoverIcon.getImage().getScaledInstance(panelWidth / 2, (panelHeight / 4) , Image.SCALE_SMOOTH);
		button2HoverIcon = new ImageIcon(scaledButton2Hover);
		final ImageIcon BUTTON_2_HOVER_INNER = button2HoverIcon;

		ImageIcon button2ClickedIcon = new ImageIcon("./img/button2Clicked.png");
		Image scaledButton2Clicked = button2ClickedIcon.getImage().getScaledInstance(panelWidth / 2, (panelHeight / 4) , Image.SCALE_SMOOTH);
		button2ClickedIcon = new ImageIcon(scaledButton2Clicked);
		final ImageIcon BUTTON_2_CLICKED_INNER = button2ClickedIcon;

		ImageIcon button3Icon = new ImageIcon("./img/button3.png");
		Image scaledButton3 = button3Icon.getImage().getScaledInstance(panelWidth - 100, panelHeight / 3, Image.SCALE_SMOOTH);
		button3Icon = new ImageIcon(scaledButton3);
		final ImageIcon BUTTON_3_INNER = button3Icon;

		ImageIcon button3HoverIcon = new ImageIcon("./img/button3Hover.png");
		Image scaledButton3Hover = button3HoverIcon.getImage().getScaledInstance(panelWidth - 100,  panelHeight / 3, Image.SCALE_SMOOTH);
		button3HoverIcon = new ImageIcon(scaledButton3Hover);
		final ImageIcon BUTTON_3_HOVER_INNER = button3HoverIcon;

		ImageIcon button3ClickedIcon = new ImageIcon("./img/button3Clicked.png");
		Image scaledButton3Clicked = button3ClickedIcon.getImage().getScaledInstance(panelWidth - 100, panelHeight / 3, Image.SCALE_SMOOTH);
		button3ClickedIcon = new ImageIcon(scaledButton3Clicked);
		final ImageIcon BUTTON_3_CLICKED_INNER = button3ClickedIcon;

		ImageIcon errorPanelIcon = new ImageIcon("./img/errorPanel.png");
		Image scaledErrorPanel = errorPanelIcon.getImage().getScaledInstance(panelWidth - 80, panelHeight + 100, Image.SCALE_SMOOTH);

		mainMenu = new JPanel(new FlowLayout(FlowLayout.LEFT));
		mainMenu.setBounds(WIDTH / 23, (HEIGHT / 2) + 50, panelWidth, panelHeight);
		mainMenu.setOpaque(false);
		((FlowLayout)mainMenu.getLayout()).setVgap(panelHeight / 19);

		JPanel optionButtonPanel = new JPanel(new FlowLayout());
		optionButtonPanel.setBounds(WIDTH / 4, HEIGHT / 15, panelWidth, panelHeight * 2);
		optionButtonPanel.setOpaque(false);
		optionButtonPanel.setVisible(false);
		((FlowLayout)optionButtonPanel.getLayout()).setVgap(panelHeight / 8);

		JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		backButtonPanel.setBounds(WIDTH / 23, HEIGHT - (HEIGHT / 7), panelWidth, panelHeight / 4 + 20);
		backButtonPanel.setOpaque(false);
		backButtonPanel.setVisible(false);

		JPanel multiplayerButtonPanel = new JPanel(new FlowLayout());
		multiplayerButtonPanel.setBounds(WIDTH / 4, HEIGHT / 10, panelWidth, panelHeight * 2);
		multiplayerButtonPanel.setOpaque(false);
		multiplayerButtonPanel.setVisible(false);
		((FlowLayout)multiplayerButtonPanel.getLayout()).setVgap(panelHeight / 4);

	    // Overriding JPanel's paintComponent method to enable an Image to be used as a background
		JPanel errorPanel = (new JPanel(new FlowLayout()) {
			@Override
			  protected void paintComponent(Graphics g) {

			    super.paintComponent(g);
			        g.drawImage(scaledErrorPanel, 0, 0, null);
			}
		});

		errorPanel.setBounds((WIDTH / 3) - 13, HEIGHT / 7, panelWidth - 80, panelHeight + 100);
		errorPanel.setOpaque(false);
		errorPanel.setVisible(false);
		((FlowLayout)errorPanel.getLayout()).setVgap((panelHeight / 4) - 15);

		JLabel errorPanelText = new JLabel();
		errorPanelText.setFont(sizedFont);
		errorPanelText.setHorizontalTextPosition(JLabel.CENTER);
		errorPanelText.setForeground(Color.black);

		// Handling the main screen background images
		ImageIcon backgroundMain = new ImageIcon("./img/backgroundMain.gif");
	    Image temp = backgroundMain.getImage().getScaledInstance(WIDTH,HEIGHT,Image.SCALE_DEFAULT);
	    backgroundMain = new ImageIcon(temp);
	    back = new JLabel(backgroundMain);
	    back.setLayout(null);
	    back.setBounds(0,0,WIDTH,HEIGHT);
	    back.setVisible(true);

	    ImageIcon backgroundOptions = new ImageIcon("./img/backgroundOptions.gif");
	    Image temp2 = backgroundOptions.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
	    backgroundOptions = new ImageIcon(temp2);
	    backOptions = new JLabel(backgroundOptions);
	    backOptions.setLayout(null);
	    backOptions.setBounds(0,0,WIDTH,HEIGHT);
	    backOptions.setVisible(false);

	    ImageIcon backgroundMultiplayer = new ImageIcon("./img/backgroundMultiplayer.gif");
	    Image temp3 = backgroundMultiplayer.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
	    backgroundMultiplayer = new ImageIcon(temp3);
	    backMultiplayer = new JLabel(backgroundMultiplayer);
	    backMultiplayer.setLayout(null);
	    backMultiplayer.setBounds(0, 0, WIDTH, HEIGHT);
	    backMultiplayer.setVisible(false);

		// Declaring buttons
		JLabel singleplayerButton = new JLabel(button1Icon);
		singleplayerButton.setFont(sizedFont);
		singleplayerButton.setText("SINGLEPLAYER");
		singleplayerButton.setHorizontalTextPosition(JLabel.CENTER);
		singleplayerButton.setForeground(Color.black);

		singleplayerButton.addMouseListener(new MouseAdapter() {

			// This handles when the JLabel is pressed by the mouse
			@Override
			public void mousePressed(MouseEvent e) {
				singleplayerButton.setIcon(BUTTON_1_CLICKED_INNER);
				SoundHandler.playSound("button1", 1f);
			}

			// This handles when the JLabel is released by the mouse
			@Override
			public void mouseReleased(MouseEvent e) {
				mainMenu.setVisible(false);
				back.setVisible(false);
				backOptions.setVisible(false);
				backMultiplayer.setVisible(false);

				if (Launcher.cHandler.ambienceToggle == true) {
					SoundHandler.playSound("ambience", 0.2f);
				}

				game.setGameState(GameState.Playing);

				game.start();
			}

			// This handles when the mouse enters the JLabel
			@Override
			public void mouseEntered(MouseEvent e) {
				singleplayerButton.setIcon(BUTTON_1_HOVER_INNER);
			}

			// This handles when the mouse leaves the JLabel
			@Override
			public void mouseExited(MouseEvent e) {
				singleplayerButton.setIcon(BUTTON_1_INNER);
			}
		});


		// The rest of the button behaviours are handled in the same way

		JLabel multiplayerButton = new JLabel(button1Icon);
		multiplayerButton.setForeground(Color.black);
		multiplayerButton.setFont(sizedFont);
		multiplayerButton.setText("MULTIPLAYER");
		multiplayerButton.setHorizontalTextPosition(JLabel.CENTER);

		multiplayerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				multiplayerButton.setIcon(BUTTON_1_CLICKED_INNER);
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
				multiplayerButton.setIcon(BUTTON_1_HOVER_INNER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				multiplayerButton.setIcon(BUTTON_1_INNER);
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
				optionsButton.setIcon(BUTTON_1_CLICKED_INNER);
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
				optionsButton.setIcon(BUTTON_1_HOVER_INNER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				optionsButton.setIcon(BUTTON_1_INNER);
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
				quitButton.setIcon(BUTTON_1_CLICKED_INNER);
		    	SoundHandler.playSound("button1", 1f);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				quitButton.setIcon(BUTTON_1_HOVER_INNER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				quitButton.setIcon(BUTTON_1_INNER);
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
				backButton.setIcon(BUTTON_2_CLICKED_INNER);
				SoundHandler.playSound("button1", 1f);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				back.setVisible(true);
				backButton.setIcon(BUTTON_2_INNER);
				mainMenu.setVisible(true);
		    	backButtonPanel.setVisible(false);
		    	optionButtonPanel.setVisible(false);
		    	multiplayerButtonPanel.setVisible(false);
		    	backOptions.setVisible(false);
		    	backMultiplayer.setVisible(false);
		    	errorPanel.setVisible(false);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				backButton.setIcon(BUTTON_2_HOVER_INNER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				backButton.setIcon(BUTTON_2_INNER);
			}
		});

		JLabel backErrorButton = new JLabel(button2Icon);
		backErrorButton.setForeground(Color.black);
		backErrorButton.setFont(sizedFont);
		backErrorButton.setText("OK");
		backErrorButton.setHorizontalTextPosition(JLabel.CENTER);

		backErrorButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				backErrorButton.setIcon(BUTTON_2_CLICKED_INNER);
				SoundHandler.playSound("button1", 1f);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				backButton.setIcon(BUTTON_2_INNER);
				errorPanel.setVisible(false);
				multiplayerButtonPanel.setVisible(true);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				backErrorButton.setIcon(BUTTON_2_HOVER_INNER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				backErrorButton.setIcon(BUTTON_2_INNER);
			}
		});

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
				toggleSoundEffectsButton.setIcon(BUTTON_3_CLICKED_INNER);
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

		    	toggleSoundEffectsButton.setIcon(BUTTON_3_HOVER_INNER);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				toggleSoundEffectsButton.setIcon(BUTTON_3_HOVER_INNER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				toggleSoundEffectsButton.setIcon(BUTTON_3_INNER);
			}
		});

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
				toggleMusicButton.setIcon(BUTTON_3_CLICKED_INNER);
				SoundHandler.playSound("button1", 1f);
			}

			@Override
			public void mouseReleased(MouseEvent e) {

		    	if (Launcher.cHandler.musicToggle) {
		    		toggleMusicButton.setText("<html><center>MUSIC:<br><p style='margin-top:8'>OFF</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.MUSIC, "False");
		    		Launcher.cHandler.musicToggle = false;
		    		SoundHandler.stopMusic();
		    	} else {
		    		toggleMusicButton.setText("<html><center>MUSIC:<br><p style='margin-top:8'>ON</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.MUSIC, "True");
		    		Launcher.cHandler.musicToggle = true;
		    		SoundHandler.resumeMusic();
		    	}

		    	toggleMusicButton.setIcon(BUTTON_3_HOVER_INNER);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				toggleMusicButton.setIcon(BUTTON_3_HOVER_INNER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				toggleMusicButton.setIcon(BUTTON_3_INNER);
			}
		});

		JLabel toggleAmbienceButton = new JLabel(button3Icon);
		toggleAmbienceButton.setForeground(Color.black);
		toggleAmbienceButton.setFont(sizedFont);
		toggleAmbienceButton.setHorizontalTextPosition(JLabel.CENTER);

		if (Launcher.cHandler.ambienceToggle) {
			toggleAmbienceButton.setText("<html><center>AMBIENCE:<br><p style='margin-top:8'>ON</center></html>");
		} else {
			toggleAmbienceButton.setText("<html><center>AMBIENCE:<br><p style='margin-top:8'>OFF</center></html>");
		}

		toggleAmbienceButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				toggleAmbienceButton.setIcon(BUTTON_3_CLICKED_INNER);
				SoundHandler.playSound("button1", 1f);
			}

			@Override
			public void mouseReleased(MouseEvent e) {

		    	if (Launcher.cHandler.ambienceToggle) {
		    		toggleAmbienceButton.setText("<html><center>AMBIENCE:<br><p style='margin-top:8'>OFF</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.AMBIENCE, "False");
		    		Launcher.cHandler.ambienceToggle = false;
		    	} else {
		    		toggleAmbienceButton.setText("<html><center>AMBIENCE:<br><p style='margin-top:8'>ON</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.AMBIENCE, "True");
		    		Launcher.cHandler.ambienceToggle = true;
		    	}

		    	toggleAmbienceButton.setIcon(BUTTON_3_HOVER_INNER);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				toggleAmbienceButton.setIcon(BUTTON_3_HOVER_INNER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				toggleAmbienceButton.setIcon(BUTTON_3_INNER);
			}
		});

		JLabel difficultyButton = new JLabel(button3Icon);
		difficultyButton.setForeground(Color.black);
		difficultyButton.setFont(sizedFont);
		difficultyButton.setHorizontalTextPosition(JLabel.CENTER);

		if (Launcher.cHandler.difficulty.equals("Easy")) {
			difficultyButton.setText("<html><center>DIFFICULTY:<br><p style='margin-top:8'>EASY</center></html>");
		} else if (Launcher.cHandler.difficulty.equals("Medium")){
			difficultyButton.setText("<html><center>DIFFICULTY:<br><p style='margin-top:8'>MEDIUM</center></html>");
		} else if (Launcher.cHandler.difficulty.equals("Hard")){
			difficultyButton.setText("<html><center>DIFFICULTY:<br><p style='margin-top:8'>HARD</center></html>");
		}

		difficultyButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				difficultyButton.setIcon(BUTTON_3_CLICKED_INNER);
				SoundHandler.playSound("button1", 1f);
			}

			@Override
			public void mouseReleased(MouseEvent e) {

		    	if (Launcher.cHandler.difficulty.equals("Easy")) {
		    		difficultyButton.setText("<html><center>DIFFICULTY:<br><p style='margin-top:8'>MEDIUM</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.DIFFICULTY, "Medium");
		    		Launcher.cHandler.difficulty = "Medium";

		    	} else if (Launcher.cHandler.difficulty.equals("Medium")) {
		    		difficultyButton.setText("<html><center>DIFFICULTY:<br><p style='margin-top:8'>HARD</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.DIFFICULTY, "Hard");
		    		Launcher.cHandler.difficulty = "Hard";
		    	}else if (Launcher.cHandler.difficulty.equals("Hard")) {
		    		difficultyButton.setText("<html><center>DIFFICULTY:<br><p style='margin-top:8'>EASY</center></html>");
		    		Launcher.cHandler.updateConfigValue(ConfigOption.DIFFICULTY, "Easy");
		    		Launcher.cHandler.difficulty = "Easy";
		    	}
		    	Launcher.difficultySettings = new DifficultySettings(Launcher.cHandler.getDifficulty());
		    	difficultyButton.setIcon(BUTTON_3_HOVER_INNER);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				difficultyButton.setIcon(BUTTON_3_HOVER_INNER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				difficultyButton.setIcon(BUTTON_3_INNER);
			}
		});

		JLabel playVSComputerButton = new JLabel(button3Icon);
		playVSComputerButton.setForeground(Color.black);
		playVSComputerButton.setFont(sizedFont);
		playVSComputerButton.setHorizontalTextPosition(JLabel.CENTER);
		playVSComputerButton.setText("PLAY VS COMPUTER");

		playVSComputerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				playVSComputerButton.setIcon(BUTTON_3_CLICKED_INNER);
				SoundHandler.playSound("button1", 1f);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				playVSComputerButton.setIcon(BUTTON_3_HOVER_INNER);

				back.setVisible(false);
		    	backButtonPanel.setVisible(false);
		    	multiplayerButtonPanel.setVisible(false);
		    	backOptions.setVisible(false);
				backMultiplayer.setVisible(false);

				if (Launcher.cHandler.ambienceToggle == true) {
					SoundHandler.playSound("ambience", 0.2f);
				}

				game.setGameState(GameState.Playing);
				game.setGameMode(GameMode.vsAI);
				game.start();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				playVSComputerButton.setIcon(BUTTON_3_HOVER_INNER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				playVSComputerButton.setIcon(BUTTON_3_INNER);
			}
		});

		JLabel createGameButton = new JLabel(button3Icon);
		createGameButton.setForeground(Color.black);
		createGameButton.setFont(sizedFont);
		createGameButton.setHorizontalTextPosition(JLabel.CENTER);
		createGameButton.setText("CREATE GAME");

		createGameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				createGameButton.setIcon(BUTTON_3_CLICKED_INNER);
				SoundHandler.playSound("button1", 1f);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				createGameButton.setIcon(BUTTON_3_HOVER_INNER);

				Boolean isAlive = false;
				try {
					DatagramSocket socket;
					socket = new DatagramSocket(1331);
					socket.close();

				} catch (SocketException e1) {
					isAlive = true;
				}

				if(isAlive == false) {
					Game.socketServer = new GameServer(game);

					Game.socketServer.start();
					Game.socketClient = new GameClient(game, "localhost");
					Game.socketClient.start();

					back.setVisible(false);
			    	backButtonPanel.setVisible(false);
			    	multiplayerButtonPanel.setVisible(false);
			    	backOptions.setVisible(false);
					backMultiplayer.setVisible(false);

			    	game.setGameState(GameState.Playing);
			    	game.setGameMode(GameMode.MULTIPLAYER);
			    	game.start();
				} else {
					multiplayerButtonPanel.setVisible(false);
					errorPanelText.setText("<html><center><p style='margin-top:50'>A server is<br><p style='margin-top:3'>already running</center></html>");
					errorPanel.setVisible(true);
				}
		}

			@Override
			public void mouseEntered(MouseEvent e) {
				createGameButton.setIcon(BUTTON_3_HOVER_INNER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				createGameButton.setIcon(BUTTON_3_INNER);
			}
		});

		JLabel joinGameButton = new JLabel(button3Icon);
		joinGameButton.setForeground(Color.black);
		joinGameButton.setFont(sizedFont);
		joinGameButton.setHorizontalTextPosition(JLabel.CENTER);
		joinGameButton.setText("JOIN GAME");

		joinGameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				joinGameButton.setIcon(BUTTON_3_CLICKED_INNER);
				SoundHandler.playSound("button1", 1f);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				joinGameButton.setIcon(BUTTON_3_HOVER_INNER);

		    	Boolean isAlive = false;
				try {
					DatagramSocket socket;
					socket = new DatagramSocket(1331);
					socket.close();
				} catch (SocketException e1) {
					isAlive = true;
				}

				if(isAlive == true) {
					Game.socketClient = new GameClient(game, "localhost");
					Game.socketClient.start();

					back.setVisible(false);
			    	backButtonPanel.setVisible(false);
			    	multiplayerButtonPanel.setVisible(false);
			    	backOptions.setVisible(false);
					backMultiplayer.setVisible(false);

					if (Launcher.cHandler.ambienceToggle == true) {
						SoundHandler.playSound("ambience", 0.2f);
					}

			    	game.setGameState(GameState.Playing);
					game.setGameMode(GameMode.MULTIPLAYER);
			    	game.start();
				} else {
					multiplayerButtonPanel.setVisible(false);
					errorPanelText.setText("<html><center><p style='margin-top:50'>A server must<br><p style='margin-top:3'>already be running<br><p style='margin-top:3'>for you to join a<br><p style='margin-top:3'>game.</center></html>");
					errorPanel.setVisible(true);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				joinGameButton.setIcon(BUTTON_3_HOVER_INNER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				joinGameButton.setIcon(BUTTON_3_INNER);
			}
		});

		mainMenu.add(singleplayerButton);
		mainMenu.add(multiplayerButton);
		mainMenu.add(optionsButton);
		mainMenu.add(quitButton);
		backButtonPanel.add(backButton);
		optionButtonPanel.add(toggleSoundEffectsButton);
		optionButtonPanel.add(toggleMusicButton);
		optionButtonPanel.add(toggleAmbienceButton);
		optionButtonPanel.add(difficultyButton);
		multiplayerButtonPanel.add(playVSComputerButton);
		multiplayerButtonPanel.add(createGameButton);
		multiplayerButtonPanel.add(joinGameButton);
		errorPanel.add(errorPanelText);
		errorPanel.add(backErrorButton);
		c.add(mainMenu);
		c.add(optionButtonPanel);
		c.add(multiplayerButtonPanel);
		c.add(backButtonPanel);
		c.add(errorPanel);
		c.add(back);
		c.add(backOptions);
		c.add(backMultiplayer);

		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * Used for accessing the mainMenu JPanel that holds the singleplayer, multiplayer, options, and quit buttons
	 * @return the mainMenu JPanel
	 */
	public static JPanel getMainMenu() {
		return mainMenu;
	}

	/**
	 * Sets the mainMenu JPanel to visible or invisible
	 * @param visible the boolean for visibility
	 */
	public static void setMainMenuVisible(boolean visible) {
		Window.mainMenu.setVisible(visible);
	}

	/**
	 * Sets the main menu background image to visible or invisible
	 * @param visible the boolean for visibility
	 */
	public static void setBack(boolean visible) {
		Window.back.setVisible(visible);
	}

	/**
	 * Sets the options background image to visible or invisible
	 * @param visible the boolean for visibility
	 */
	public static void setBackOptions(boolean visible) {
		Window.backOptions.setVisible(visible);
	}

	/**
	 * Sets the multiplayer background image to visible or invisible
	 * @param visible the boolean for visibility
	 */
	public static void setBackMultiplayer(boolean visible) {
		Window.backMultiplayer.setVisible(visible);
	}
}
