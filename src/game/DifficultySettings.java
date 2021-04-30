package game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DifficultySettings {
	
	
	private float deathWallSpeed;
	private float projectileSpeed;
	private float mindlessAISpeed;
	private float rocksSpeed; //speed of falling rocks\
	private float stonesSpeed;
	private float arrowSpeed;
	
	
	//load easy,medium,hard config file
	public DifficultySettings(String difficulty) {
		
		File difficultyConfig = new File("./src/difficultyConfig-".concat(difficulty).concat(".txt"));
		
		
		deathWallSpeed = getDifficultyValue("deathWallSpeed",difficultyConfig);
		projectileSpeed = getDifficultyValue("projectileSpeed",difficultyConfig);
		mindlessAISpeed = getDifficultyValue("mindlessAISpeed",difficultyConfig);
		rocksSpeed = getDifficultyValue("rocksSpeed",difficultyConfig);
		stonesSpeed = getDifficultyValue("stonesSpeed",difficultyConfig);
		arrowSpeed = getDifficultyValue("arrowSpeed",difficultyConfig);
		
		setDifficultyForAllFiles();
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
	
	public void setDifficultyForAllFiles() {
		
		setDifficultyForFile("intersegmentA1");
		setDifficultyForFile("intersegmentA2");
		setDifficultyForFile("intersegmentA2down");
		setDifficultyForFile("intersegmentA2up");
		setDifficultyForFile("intersegmentA3");
		setDifficultyForFile("intro1");
		setDifficultyForFile("intro2");
		setDifficultyForFile("introDimension");
		setDifficultyForFile("segmentA1");
		setDifficultyForFile("segmentA2");
		setDifficultyForFile("segmentA3");
		setDifficultyForFile("segmentA4");
		setDifficultyForFile("segmentA5");
		setDifficultyForFile("segmentA6");
		setDifficultyForFile("segmentA7");
		setDifficultyForFile("segmentA8");
		setDifficultyForFile("segmentA9");
		setDifficultyForFile("segmentA10");
		setDifficultyForFile("segmentA11");
		setDifficultyForFile("segmentA12");
		setDifficultyForFile("segmentA13");
		setDifficultyForFile("segmentA14");
		setDifficultyForFile("segmentA15");
		setDifficultyForFile("segmentA16");
		setDifficultyForFile("segmentEND");
		
	}
	
	public synchronized void setDifficultyForFile (String url) {
		try {
			ArrayList<String> lines = new ArrayList<String>();
            File segFile = new File("./src/game/segments/".concat(url).concat(".txt"));
            
            System.out.println("Now at file: " + url );
            
            Scanner sc = new Scanner(segFile);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                String[] splitted =  data.split("\\s+");
                //Settings Update
                switch(splitted[0]) {
                
                	case "Projectile":
                		data = updateConfigStr(splitted);
                		break;
                		
                }
                lines.add(data);
            }
            sc.close();
            
            FileWriter fw = new FileWriter(segFile);
            BufferedWriter out = new BufferedWriter(fw);
            
            for(String s:lines) {//Overwrites current file with updated values
            	out.write(s);
            	out.newLine();
            }
            out.flush();
            out.close();
            System.out.println("End Of File");
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
			System.out.println("IO Exception");
			e.printStackTrace();
		}
	}
	
	//Place Settings preferences here
	public String updateConfigStr(String[] splitted) {
		
		
		switch(splitted[0]) {
		
			case"Projectile":
				
				switch(splitted[10]) {
				
					case "rocks":
						System.out.print("Changed rocks from " + splitted[4]+ "to");
						splitted[4] = Float.toString(rocksSpeed);
						System.out.println(splitted[4]);
						break;
				
					case "stones":
						System.out.print("Changed stones from " + splitted[4]+ "to");
						splitted[4] = Float.toString(stonesSpeed);
						System.out.println(splitted[4]);
						break;
					
					case "arrow":
						System.out.print("Changed arrow from " + splitted[4]+ "to");
						splitted[4] = Float.toString(arrowSpeed);
						System.out.println(splitted[4]);
						break;
				}
			break;
			
		}
		
		String returnStr = "";
		for(int i=0;i < splitted.length-1;i++) {
			returnStr += (splitted[i]+"\s");
		}
		returnStr += (splitted[splitted.length-1]);
		
		System.out.println("return string: " + returnStr);
		return returnStr;
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