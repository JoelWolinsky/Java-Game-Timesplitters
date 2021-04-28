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
    	if(Launcher.cHandler.getDifficulty().equals("Easy")) {
    		difficultySettings = new DifficultySettings("src/difficultyConfig-easy.txt");
    	}
    	else if(Launcher.cHandler.getDifficulty().equals("Medium")) {
    		difficultySettings = new DifficultySettings("src/difficultyConfig-medium.txt");
    	}
    	else {
    		difficultySettings = new DifficultySettings("src/difficultyConfig-hard.txt");
    	}
    	
    	difficultySettings.printConfig();
		cHandler.createConfigFile();
		
		new Game();		
	}	
}
