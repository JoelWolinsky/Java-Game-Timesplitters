package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Outfit {
	
	public static Outfits currentOutfit = getOutfitFromFile();
	
	public static enum Outfits {
		DEFAULT,
		PURPLE,
		BROWN,
		GREEN
	}
	
	/**
	 * Cycles left between all outfits
	 */
	public static void cycleLeftOutfit() {
		if (currentOutfit == Outfits.DEFAULT) {
			
			currentOutfit = Outfits.BROWN;
			writeToFile(Outfits.BROWN.name());
			
		} else if (currentOutfit == Outfits.BROWN) {
			
			currentOutfit = Outfits.GREEN;
			writeToFile(Outfits.GREEN.name());
			
		} else if (currentOutfit == Outfits.PURPLE) {
			
			currentOutfit = Outfits.DEFAULT;
			writeToFile(Outfits.DEFAULT.name());
			
		}
	}
	
	/**
	 * Cycles right between all outfits
	 */
	public static void cycleRightOutfit() {
		if (currentOutfit == Outfits.DEFAULT) {
			
			currentOutfit = Outfits.PURPLE;
			writeToFile(Outfits.PURPLE.name());
			
		} else if (currentOutfit == Outfits.BROWN) {
			
			currentOutfit = Outfits.DEFAULT;
			writeToFile(Outfits.DEFAULT.name());
			
		} else if (currentOutfit == Outfits.GREEN) {
			
			currentOutfit = Outfits.BROWN;
			writeToFile(Outfits.BROWN.name());
			
		}
	}
	
	/**
	 * Writes a string to the outfit config file
	 * @param input the string to write
	 */
	public static void writeToFile(String input) {
		createOutfitFile();
				
		try {
			FileWriter writer = new FileWriter("outfit.txt");
			writer.write(input);
			writer.close();
					
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	/**
	 * Reads the outfit that is contained in the outfit config file
	 * @returns the Outfit that is in the file
	 */
	public static Outfits getOutfitFromFile() {
		createOutfitFile();
		
		File file = new File("outfit.txt");
		Scanner reader;
		try {
			reader = new Scanner(file);
			
			String fileString = reader.nextLine();
			reader.close();
			
			if (fileString.equals("DEFAULT")) {
				return Outfits.DEFAULT;
			} else if (fileString.equals("GREEN")) {
				return Outfits.GREEN;
			} else if (fileString.equals("PURPLE")) {
				return Outfits.PURPLE;
			} else {
				return Outfits.BROWN;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return currentOutfit;
	}
	
	/**
	 * Creates the outfit config file and populates it with the current outfit
	 * @returns true if the file does not exist, false if the file already exists
	 */
	public static Boolean createOutfitFile() {
		try {
			File configFile = new File("outfit.txt");
			if (configFile.createNewFile()) {
				FileWriter writer = new FileWriter("outfit.txt");
				writer.write(currentOutfit.name());
				writer.close();
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return null;
	}
}
