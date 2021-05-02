package game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ConfigHandler {
	
	String fileName;
	public Boolean soundEffectsToggle;
	public Boolean musicToggle;
	public String difficulty;
	public Boolean ambienceToggle;
	
	public enum ConfigOption {
		SOUNDEFFECTS,
		MUSIC,
		DIFFICULTY,
		AMBIENCE
	}
	
	public ConfigHandler(String fileNameArg) {
		this.fileName = fileNameArg;
		this.soundEffectsToggle = getSoundEffectsToggle();
		this.musicToggle = getMusicToggle();
		this.difficulty = getDifficulty();
		this.ambienceToggle = getAmbienceToggle();
	}
	
	/**
	 * Returns the value of toggleSoundEffects from the config file
	 * @return A boolean value of toggleSoundEffects
	 */
	private Boolean getSoundEffectsToggle() {
		createConfigFile();
		
		try {
			File configFile = new File(this.fileName);
			Scanner reader = new Scanner(configFile);
			
			String line = reader.nextLine();
			String toggle = line.substring(19);
			reader.close();
			
			if (toggle.equals("True")) {
				return true;
			} else {
				return false;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns the value of toggleMusic from the config file
	 * @return A boolean value of toggleMusic
	 */
	private Boolean getMusicToggle() {
		createConfigFile();
		
		try {
			File configFile = new File(this.fileName);
			Scanner reader = new Scanner(configFile);
			
			reader.nextLine();
			String line = reader.nextLine();
			String toggle = line.substring(12);
			reader.close();
			
			if (toggle.equals("True")) {
				return true;
			} else {
				return false;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns the value of difficulty from the config file
	 * @return A string value of difficulty
	 */
	private String getDifficulty() {
		createConfigFile();
		
		try {
			File configFile = new File(this.fileName);
			Scanner reader = new Scanner(configFile);
			
			reader.nextLine();
			reader.nextLine();
			String line = reader.nextLine();
			String toggle = line.substring(11);
			reader.close();
			
			if (toggle.equals("Easy")) {
				return "Easy";
			} else if (toggle.equals("Medium")){
				return "Medium";
			} else {
				return "Hard";
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns the value of toggleAmbience from the config file
	 * @return A boolean value of toggleMusic
	 */
	private Boolean getAmbienceToggle() {
		createConfigFile();
		
		try {
			File configFile = new File(this.fileName);
			Scanner reader = new Scanner(configFile);
			
			reader.nextLine();
			reader.nextLine();
			reader.nextLine();
			String line = reader.nextLine();
			String toggle = line.substring(15);
			reader.close();
			
			if (toggle.equals("True")) {
				return true;
			} else {
				return false;
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Updates the value of a given option in the config file
	 * @param option The option to be updated
	 * @param value The value to which the option should be updated
	 */
	public void updateConfigValue(ConfigOption option, String value) {
		
		String updatedFile = "";
		String restAfter = "";
		String restBefore = "";
		File configFile = new File(this.fileName);
		
		try {
			Scanner reader = new Scanner(configFile);
			
			if (option == ConfigOption.SOUNDEFFECTS) {
				String newLineValue = "toggleSoundEffects=" + value + "\n";
				reader.nextLine();

				restAfter = reader.nextLine() + "\n";
				restAfter += reader.nextLine() + "\n";
				restAfter += reader.nextLine();
					
				updatedFile = newLineValue + restAfter;
				
			} else if (option == ConfigOption.MUSIC) {
				String newLineValue = "\ntoggleMusic=" + value + "\n";
				restBefore = reader.nextLine();
				reader.nextLine();
				restAfter = reader.nextLine() + "\n";
				restAfter += reader.nextLine();
				
				updatedFile = restBefore + newLineValue + restAfter;
				
			} else if (option == ConfigOption.DIFFICULTY) {
				String newLineValue = "\ndifficulty=" + value + "\n";
				
				restBefore = reader.nextLine() + "\n";
				restBefore += reader.nextLine();
				reader.nextLine();
				restAfter = reader.nextLine();
				
				updatedFile = restBefore + newLineValue + restAfter;
			} else if (option == ConfigOption.AMBIENCE) {
				String newLineValue = "\ntoggleAmbience=" + value;
				
				restBefore = reader.nextLine() + "\n";
				restBefore += reader.nextLine() + "\n";
				restBefore += reader.nextLine();
				
				updatedFile = restBefore + newLineValue;
			}
			reader.close();
			
			FileWriter writer = new FileWriter(this.fileName);
			writer.write(updatedFile);
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Creates a config file and populates it with default values
	 * @return true if successful, false if not
	 */
	public Boolean createConfigFile() {
		try {
			File configFile = new File(this.fileName);
			if (configFile.createNewFile()) {
				FileWriter writer = new FileWriter(this.fileName);
				writer.write("toggleSoundEffects=True\ntoggleMusic=True\ndifficulty=Medium\ntoggleAmbience=True");
				writer.close();
				return true;
			} else {
				return false;
			}
		} 
		catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return null;
	}
}
