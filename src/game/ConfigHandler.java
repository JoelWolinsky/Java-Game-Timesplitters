package game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ConfigHandler {
	
	String fileName;
	Boolean soundEffectsToggle;
	Boolean musicToggle;
	
	public enum ConfigOption {
		SOUNDEFFECTS,
		MUSIC
	}
	
	
	public ConfigHandler(String fileNameArg) {
		this.fileName = fileNameArg;
		this.soundEffectsToggle = null;
		this.musicToggle = null;
	}
	
	public Boolean getSoundEffectsToggle() {
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
			System.out.println("An error occured");
			e.printStackTrace();
		}
		return null;
	}
	
	public Boolean getMusicToggle() {
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
			System.out.println("An error occured");
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateConfigValue(ConfigOption option, String value) {
		
		String updatedFile = "";
		String rest = "";
		File configFile = new File(this.fileName);
		
		try {
			Scanner reader = new Scanner(configFile);
			
			if (option == ConfigOption.SOUNDEFFECTS) {
				String newLineValue = "toggleSoundEffects=" + value + "\n";
				reader.nextLine();
				
				while (reader.hasNextLine()) {
					rest = reader.nextLine();
				}
					
				updatedFile = newLineValue + rest;
				
			} else if (option == ConfigOption.MUSIC) {
				String newLineValue = "\ntoggleMusic=" + value;
				rest = reader.nextLine();
				
				updatedFile = rest + newLineValue;
			}
			reader.close();
			
			FileWriter writer = new FileWriter(this.fileName);
			writer.write(updatedFile);
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Boolean createConfigFile() {
		try {
			File configFile = new File(this.fileName);
			if (configFile.createNewFile()) {
				FileWriter writer = new FileWriter(this.fileName);
				writer.write("toggleSoundEffects=True\ntoggleMusic=True");
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
