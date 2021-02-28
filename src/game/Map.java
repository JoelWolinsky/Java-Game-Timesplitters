package game;

import game.entities.MovingPlatform;
import game.entities.Platform;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map {


    private int horizontalIndex = 0;
    private int verticalIndex = 0;
    private int setX = 426;
    private int setY = 384;
    private String lastDirection = "";
    private String currentTheme = "A";


    public void mapParser(Level currentLevel, String url) {
        Chunk c;
        String goUrl = "";
        String texturePlatformDefault = "";
        String texturePlatformInverted = "";
        String textureFloor = "";
        String textureBackground = "";

        try {
            File myObj = new File(url);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {

                String data = myReader.nextLine();
                String[] splited = data.split("\\s+");

                switch (splited[0]) {

                    case "Theme":
                        if (!splited[1].equals(currentTheme)) {
                            setX = Integer.parseInt(splited[2]);
                            setY = Integer.parseInt(splited[3]);
                            currentTheme = splited[1];
                        }
                        texturePlatformDefault = splited[4];
                        texturePlatformInverted = splited[5];
                        textureFloor = splited[6];
                        textureBackground = splited[7];
                        break;

                    case "Chunk":
                        lastDirection = splited[2];
                        switch (splited[2]) {
                            case "E":
                                horizontalIndex = horizontalIndex + setX;
                                break;
                            case "W":
                                horizontalIndex = horizontalIndex - setX;
                                break;
                            case "N":
                                verticalIndex = verticalIndex - setY;
                                break;
                            case "S":
                                verticalIndex = verticalIndex + setY;
                                break;
                        }
                        c = new Chunk(horizontalIndex - setX, verticalIndex, setX, setY, splited[3]);
                        currentLevel.addChunk(c);

                        break;

                    case "Platform":
                        Platform p;

                        switch (splited[1]) {
                            case "Default":
                                goUrl = texturePlatformDefault;
                                break;
                            case "Inverted":
                                goUrl = texturePlatformInverted;
                                break;
                            case "Custom":
                                goUrl = splited[4];
                                break;
                        }

                        p = new Platform(horizontalIndex - setX + Integer.parseInt(splited[2]), verticalIndex + Integer.parseInt(splited[3]), 0, 0, goUrl);
                        currentLevel.addPlatform(p);
                        break;

                    case "Floor":
                        goUrl = textureFloor;
                        p = new Platform(horizontalIndex - setX, verticalIndex + setY, setX, 0, goUrl);
                        currentLevel.addPlatform(p);
                        break;
					case "MovingPlatform":
						MovingPlatform mp = new MovingPlatform(horizontalIndex + Integer.parseInt(splited[1]), verticalIndex + Integer.parseInt(splited[2]) , 0,0,Integer.parseInt(splited[3]),Boolean.parseBoolean(splited[4]),Integer.parseInt(splited[5])	,splited[6]);
						currentLevel.addPlatform(mp);
                    case "Respawn":
                        RespawnPoint rp = new RespawnPoint(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,splited[3]);
                        currentLevel.addRespawnPoint(rp);
                        break;
                    case "AreaDmg":
                        AreaDmg ad = new AreaDmg(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,"./img/diamond-01.png", "./img/diamond-02.png", "./img/diamond-03.png","./img/diamond-04.png","./img/diamond-05.png");
                        currentLevel.addAreaDmg(ad);
                        break;
                    case "Revert":
                        switch (lastDirection) {
                            case "E":
                                horizontalIndex = horizontalIndex - setX;
                                break;
                            case "W":
                                horizontalIndex = horizontalIndex + setX;
                                break;
                            case "N":
                                verticalIndex = verticalIndex + setY;
                                break;
                            case "S":
                                verticalIndex = verticalIndex - setY;
                                break;

                        }
                        break;

                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}