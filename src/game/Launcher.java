package game;

/**
 * Contains the main function. All setup that must be done before the game is started can go here
 */
public class Launcher {
	public static ConfigHandler cHandler;
	public static DifficultySettings difficultySettings;
	public static void main(String[] args) {
		cHandler = new ConfigHandler("src/config.txt");
		//Difficulty Settings Handling
		difficultySettings = new DifficultySettings(cHandler.getDifficulty());
		
		cHandler.createConfigFile();
		
		new Game();		
	}	
}
