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
	
	private int ghostSpeedIndex;
	private int bloodcellSpeedIndex;
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
		
		bloodcellXSpeed = getGroupDifficultyValue("bloodcellXSpeed",difficultyConfig);
		bloodcellYSpeed = getGroupDifficultyValue("bloodcellYSpeed",difficultyConfig);
		bloodcellSpeedIndex = bloodcellXSpeed.size()-1;
		
		setDifficultyForAllFiles();
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
						
					case "books":
						splitted[4]= Float.toString(booksSpeed);
						break;
						
					case "bookShelf":
						splitted[4] = Float.toString(bookshelfSpeed);
						break;
						
					case "ghost":
						if(ghostSpeedIndex == -1) {
							ghostSpeedIndex = ghostSpeed.size()-1;
						}
						splitted[3] = Float.toString(ghostSpeed.get(ghostSpeedIndex));
						ghostSpeedIndex--;
						break;
						
					case "bloodcell":
						if(bloodcellSpeedIndex == -1) {
							bloodcellSpeedIndex = bloodcellXSpeed.size()-1;
						}
						splitted[3] = Float.toString(bloodcellXSpeed.get(bloodcellSpeedIndex));
						splitted[4] = Float.toString(bloodcellYSpeed.get(bloodcellSpeedIndex));
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
		
		System.out.println("return string: " + returnStr);
		return returnStr;
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