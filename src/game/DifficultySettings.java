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
	private float mindlessAISpeed;
	
	//Projectile Speeds
	private float rocksSpeed; //speed of falling rocks\
	private float stonesSpeed;
	private float arrowSpeed;
	private float booksSpeed;
	private float bookshelfSpeed;
	
	//Segment Projectile (Group projectiles)
	
	private ArrayList<Float> ghostSpeed;
	private ArrayList<Float> bloodcellXSpeed;
	private ArrayList<Float> bloodcellYSpeed;

	private ArrayList<String> ghostGroup;
	
	private int ghostSpeedIndex;
	private int bloodcellSpeedIndex;
	
	//EventDangerZone
	private ArrayList<String>statueGroup;
	
	
	//mindlessAI
	private int maxChickenSpawn;
	
	
	//load easy,medium,hard config file
	public DifficultySettings(String difficulty) {
		
		File difficultyConfig = new File("./src/difficultyConfig-".concat(difficulty).concat(".txt"));
		
		
		deathWallSpeed = getDifficultyValue("deathWallSpeed",difficultyConfig);
		mindlessAISpeed = getDifficultyValue("mindlessAISpeed",difficultyConfig);
		rocksSpeed = getDifficultyValue("rocksSpeed",difficultyConfig);
		stonesSpeed = getDifficultyValue("stonesSpeed",difficultyConfig);
		arrowSpeed = getDifficultyValue("arrowSpeed",difficultyConfig);
		booksSpeed = getDifficultyValue("booksSpeed",difficultyConfig);
		bookshelfSpeed = getDifficultyValue("bookshelfSpeed",difficultyConfig);
		
		
		ghostSpeed = getGroupDifficultyValue("ghostSpeed",difficultyConfig);
		ghostSpeedIndex = ghostSpeed.size()-1;
		ghostGroup = getGroupRawConfig("ghostGroup",difficultyConfig);
		
		bloodcellXSpeed = getGroupDifficultyValue("bloodcellXSpeed",difficultyConfig);
		bloodcellYSpeed = getGroupDifficultyValue("bloodcellYSpeed",difficultyConfig);
		bloodcellSpeedIndex = bloodcellXSpeed.size()-1;
	
		statueGroup = getGroupRawConfig("statueGroup",difficultyConfig);
		maxChickenSpawn = (int) getDifficultyValue("maxChickenSpawn",difficultyConfig);
		
		//System.out.println(statueGroup.toString());
		//System.out.println(ghostGroup.toString());
		setDifficultyForAllFiles();
	}
	public ArrayList<String>getGroupRawConfig(String arg, File difficultyConfig){
		
		try {
			Scanner sc = new Scanner(difficultyConfig);
			ArrayList<String> returnArr = new ArrayList<String>();
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				
				if(line.equals("*STARTGROUP*")) {
					String line2 = sc.nextLine();
					boolean firstTime = true;
					while(!line2.equals("*ENDGROUP*")&&sc.hasNextLine()) {
						
						if(firstTime && !line2.equals(arg)) {
							
							break;
						}
						else {
							firstTime = false;
							returnArr.add(line2);
						}
						
						line2 = sc.nextLine();
					}
				}
			}
			sc.close();
			return returnArr;
		}
		catch(IOException e){
			System.out.println("File IO Error");
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Float> getGroupDifficultyValue(String arg, File difficultyConfig) {
		try {
			Scanner sc = new Scanner(difficultyConfig);
			
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] parts = line.split("=");
				
				if(parts[0].equals(arg)) {
					String[] tempArr = parts[1].split("\s");
					sc.close();
					ArrayList<Float> returnArr = new ArrayList<Float>();
					for(int i=tempArr.length-1;i>-1;i--) {
						returnArr.add(Float.parseFloat(tempArr[i]));
					}
					return returnArr;
				}
			}
			sc.close();
			return null;
		}
		catch(IOException e){
			System.out.println("File IO Error");
			e.printStackTrace();
		}
		return null;
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
            
            //System.out.println("Now at file: " + url );
            
            Scanner sc = new Scanner(segFile);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                String[] splitted =  data.split("\\s+");
                //Settings Update
                
                data = updateConfigStr(splitted);
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
            //System.out.println("End Of File");
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
		
			case"MindlessAISpawner":
				splitted[5] = Integer.toString(maxChickenSpawn);
				break;
		
			case"!EventDamageZone":
				switch(splitted[9]){
					case"statue":
						splitted[0] = "EventDamageZone";
						String temp = joinStrArr(splitted);
						if(statueGroup.contains(temp)) {
							statueGroup.remove(temp);
						}
						else {
							splitted[0]= "!EventDamageZone";
						}
						break;
				}
				break;
		
			case"EventDamageZone":
				switch(splitted[9]) {
					
					case"statue":
						
						String temp = joinStrArr(splitted);
						//System.out.println(statueGroup.get(3));
						
							if(statueGroup.contains(temp)){
								statueGroup.remove(temp);
							}
							else {
								splitted[0] = "!"+ splitted[0];
							}
						
						break;
				}
				break;
			
			case"!Projectile":
				switch(splitted[10]) {
					case"ghost":
						splitted[0] = "Projectile";
						String temp = joinStrArr(splitted);
						if(ghostGroup.contains(temp)) {
							ghostGroup.remove(temp);
						}
						else {
							splitted[0]= "!Projectile";
						}
						break;
						
				}
				break;
				
			case"Projectile":
				
				switch(splitted[10]) {
				
					case "rocks":
					
						splitted[4] = Float.toString(rocksSpeed);
			
						break;
				
					case "stones":
						
						splitted[4] = Float.toString(stonesSpeed);
				
						break;
					
					case "arrow":
			
						splitted[4] = Float.toString(arrowSpeed);

						break;
						
					case "books":
						splitted[4]= Float.toString(booksSpeed);
						break;
						
					case "bookShelf":
						splitted[4] = Float.toString(bookshelfSpeed);
						break;
						
					case "ghost":
						
						String temp = joinStrArr(splitted);
						
						
						if(ghostGroup.contains(temp)){
							ghostGroup.remove(temp);
						}
						else if (!ghostGroup.contains(temp)){
							splitted[0] = "!"+ splitted[0];
						}
					
						/*
						 * if(ghostSpeedIndex == -1) { ghostSpeedIndex = ghostSpeed.size()-1; }
						 * if(Launcher.cHandler.getDifficulty().equals("Hard")) { splitted[3] =
						 * Float.toString(ghostSpeed.get(ghostSpeedIndex)); } else
						 * if(Launcher.cHandler.getDifficulty().equals("Medium")) { splitted[3] =
						 * Float.toString(ghostSpeed.get(ghostSpeedIndex)/1.5f); } else { splitted[3] =
						 * Float.toString(ghostSpeed.get(ghostSpeedIndex)/2); } ghostSpeedIndex--;
						 */
						break;
						
					case "bloodcell":
						if(bloodcellSpeedIndex == -1) {
							bloodcellSpeedIndex = bloodcellXSpeed.size()-1;
						}
						if(Launcher.cHandler.getDifficulty().equals("Hard")) {
							splitted[3] = Float.toString(bloodcellXSpeed.get(bloodcellSpeedIndex));
							splitted[4] = Float.toString(bloodcellYSpeed.get(bloodcellSpeedIndex));
						}
						else if(Launcher.cHandler.getDifficulty().equals("Medium")) {
							splitted[3] = Float.toString(bloodcellXSpeed.get(bloodcellSpeedIndex)/1.5f);
							splitted[4] = Float.toString(bloodcellYSpeed.get(bloodcellSpeedIndex)/1.5f);
						}
						else {
							splitted[3] = Float.toString(bloodcellXSpeed.get(bloodcellSpeedIndex)/2f);
							splitted[4] = Float.toString(bloodcellYSpeed.get(bloodcellSpeedIndex)/2f);
						}
						bloodcellSpeedIndex--;
						break;
				}
				break;
			
		}
		
		String returnStr = "";
		for(int i=0;i < splitted.length-1;i++) {
			returnStr += (splitted[i]+"\s");
		}
		returnStr += (splitted[splitted.length-1]);
		
		return returnStr;
	}
	
	public String joinStrArr(String[] splitted) {
		String tempStr = "";
		
		for(int i=0;i<splitted.length-1;i++) {
			tempStr += (splitted[i]+ "\s");
			
		}
		tempStr += splitted[splitted.length-1];
		return tempStr;
	}
	
	public void printConfig() {
		System.out.println("DIFFICULTY SETTINGS");
		System.out.println(deathWallSpeed);
		System.out.println(mindlessAISpeed);
		
	}
	
	public float getDeathWallSpeed () {
		return deathWallSpeed;
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


	public float getBooksSpeed() {
		return booksSpeed;
	}


	public float getBookshelfSpeed() {
		return bookshelfSpeed;
	}

}