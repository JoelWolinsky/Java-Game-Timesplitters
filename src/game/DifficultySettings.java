package game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DifficultySettings {
	
	private float deathWallSpeed;
	private float projectileSpeed;
	private float mindlessAISpeed;
	private float rocksSpeed; //speed of falling rocks\
	private float stonesSpeed;
	private float arrowSpeed;
	
	public DifficultySettings(String fileName) {
		File difficultyConfig = new File(fileName);
		
		deathWallSpeed = getDifficultyValue("deathWallSpeed",difficultyConfig);
		projectileSpeed = getDifficultyValue("projectileSpeed",difficultyConfig);
		mindlessAISpeed = getDifficultyValue("mindlessAISpeed",difficultyConfig);
		rocksSpeed = getDifficultyValue("rocksSpeed",difficultyConfig);
		stonesSpeed = getDifficultyValue("stonesSpeed",difficultyConfig);
		arrowSpeed = getDifficultyValue("arrowSpeed",difficultyConfig);
	}
	
	public float getDifficultyValue(String arg, File difficultyConfig) {
		
		try {
			Scanner sc = new Scanner(difficultyConfig);
			
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] parts = line.split("=");
				if(parts[0].equals(arg)) {
					sc.close();
					return Float.parseFloat(parts[1]);
				}
			}
			sc.close();
			return -1f;
		}
		catch(IOException e){
			System.out.println("File IO Error");
			e.printStackTrace();
		}
		return -1f;
	}
	
	public void printConfig() {
		System.out.println("DIFFICULTY SETTINGS");
		System.out.println(deathWallSpeed);
		System.out.println(projectileSpeed);
		System.out.println(mindlessAISpeed);
		
	}
	
	public float getDeathWallSpeed () {
		return deathWallSpeed;
	}
	public float getProjectileSpeed() {
		return projectileSpeed;
	}
	public float getMindlessAISpeed() {
		return mindlessAISpeed;
	}

	public float getRocksSpeed() {
		return rocksSpeed;
	}

	public float getStonesSpeed() {
		return stonesSpeed;
	}

	public float getArrowSpeed() {
		return arrowSpeed;
	}
}
