package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import game.entities.Platform;

public class Map {
	
	static int horizontalIndex = 0;
	static int verticalIndex = 0;
	static int newOffsetX = 0;
	static int newOffsetY = 0;
	static int t = 0;
	
	/**
	 * WHAT DOES IT DO?
	 * @param masterOffsetX
	 * @param masterOffsetY
	 * @param currentLevel
	 * @param url
	 */
	public static void mapParser(int masterOffsetX, int masterOffsetY, Level currentLevel, String url)
	{

		Platform p;
		Chunk c = new Chunk();

		try {
			File myObj = new File(url); //
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {

				String data = myReader.nextLine();
				String[] splited = data.split("\\s+");

				switch (splited[0]){
					case "Theme":
						newOffsetX = Integer.parseInt(splited[2]);
						newOffsetY = Integer.parseInt(splited[3]);
						t = 1;
						break;
					case "Chunk":
						currentLevel.addChunk(c);
						c = new Chunk();
						if (t==0) {
							masterOffsetX = newOffsetX;
							masterOffsetY = newOffsetY;
						}
						switch (splited[2]){
							case "E":
								horizontalIndex= horizontalIndex + masterOffsetX;
								break;
							case "W":
								horizontalIndex= horizontalIndex - masterOffsetX;
								break;
							case "N":
								verticalIndex = verticalIndex - masterOffsetY;
								break;
							case "S":
								verticalIndex = verticalIndex + masterOffsetY;
								break;
						}
						t--;
						break;
					case "Platform":
						System.out.println(masterOffsetX);
						System.out.println(newOffsetX);
						System.out.println(horizontalIndex);
						p = new Platform(horizontalIndex + Integer.parseInt(splited[1]), verticalIndex + Integer.parseInt(splited[2]) , Integer.parseInt(splited[3]), Integer.parseInt(splited[4]));
						c.addPlatform(p);
						break;
				}
				System.out.println(data);

			}
			currentLevel.addChunk(c);
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

}
