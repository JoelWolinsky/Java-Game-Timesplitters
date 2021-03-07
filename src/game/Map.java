package game;

import game.entities.areas.*;
import game.entities.platforms.CrushingPlatform;
import game.entities.platforms.MovingPlatform;
import game.entities.platforms.Platform;
import game.entities.platforms.TimerPlatform;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Map {


    private int horizontalIndex = 0;
    private int verticalIndex = 0;
    private int setX = 426;
    private int setY = 384;
    private String lastDirection = "";
    private String currentTheme = "A";


    public void mapParser(Level currentLevel, String url) {

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

                        Area a;
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
                        a = new Area(horizontalIndex - setX, verticalIndex, setX, setY, "./img/".concat(splited[3]));
                        currentLevel.addArea(a);
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
						MovingPlatform mp = new MovingPlatform(horizontalIndex - setX  + Integer.parseInt(splited[1]), verticalIndex + Integer.parseInt(splited[2]) , 0,0,Integer.parseInt(splited[3]),Boolean.parseBoolean(splited[4]),Float.parseFloat(splited[5])	,splited[6]);
						currentLevel.addPlatform(mp);
						break;
                    case "TimerPlatform":
                        TimerPlatform tp = new TimerPlatform(horizontalIndex - setX + Integer.parseInt(splited[1]), verticalIndex + Integer.parseInt(splited[2]) , 0,0,splited[3],Integer.parseInt(splited[4]), Integer.parseInt(splited[5]));
                        currentLevel.addPlatform(tp);
                        break;
                    case "CrushingPlatform":
                        CrushingPlatform cp = new CrushingPlatform(horizontalIndex - setX  + Integer.parseInt(splited[1]), verticalIndex + Integer.parseInt(splited[2]) , 0,0,Integer.parseInt(splited[3]),Float.parseFloat(splited[4])	,splited[5],Integer.parseInt(splited[6]),splited[7], Float.parseFloat(splited[8]));
                        currentLevel.addPlatform(cp);
                        break;
                    case "Respawn":
                        RespawnPoint rp = new RespawnPoint(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,"./img/".concat(splited[3]));
                        currentLevel.addArea(rp);
                        break;
                    case "Area":

                        Area ad;
                        ad = new Area(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,"./img/".concat(splited[3]));
                        currentLevel.addArea(ad);

                        break;
                    case "TimerDamageZone":

                        //prepares a string array with the urls
                        List<String> list = new ArrayList<String>();
                        for (int l = 5;l < 5+Integer.parseInt(splited[4]);l++)
                            list.add("./img/".concat(splited[l]));

                        String[] arr = list.toArray(new String[0]);


                        TimerDamageZone tmz;
                        tmz = new TimerDamageZone(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Integer.parseInt(splited[3]),arr);
                        currentLevel.addArea(tmz);

                        break;
                    case "Projectile":

                        List<String> list2 = new ArrayList<String>();
                        for (int j = 9;j < 9+Integer.parseInt(splited[8]);j++)
                            list2.add("./img/".concat(splited[j]));
                        String[] arr2 = list2.toArray(new String[0]);

                        Projectile pj;
                        pj = new Projectile(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Float.parseFloat(splited[3]),Float.parseFloat(splited[4]),Float.parseFloat(splited[5]),Float.parseFloat(splited[6]), Integer.parseInt(splited[7]),arr2);
                        currentLevel.addArea(pj);
                        break;
                    case "AnimArea":
                        AnimArea cao;
                        List<String> list3 = new ArrayList<String>();
                        for (int l = 4;l < 4+Integer.parseInt(splited[3]);l++)
                            list3.add("./img/".concat(splited[l]));

                        String[] arr3 = list3.toArray(new String[0]);

                        cao = new AnimArea(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,arr3);

                        currentLevel.addArea(cao);
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