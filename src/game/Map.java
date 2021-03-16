package game;

import game.entities.areas.*;
import game.entities.platforms.CrushingPlatform;
import game.entities.platforms.MovingPlatform;
import game.entities.platforms.Platform;
import game.entities.platforms.TimerPlatform;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
                        currentLevel.addEntity(a);
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
                        currentLevel.addEntity(p);
                        break;

                    case "Floor":
                        goUrl = textureFloor;
                        p = new Platform(horizontalIndex - setX, verticalIndex + setY, setX, 0, goUrl);
                        currentLevel.addEntity(p);
                        break;
					case "MovingPlatform":
						MovingPlatform mp = new MovingPlatform(horizontalIndex - setX  + Integer.parseInt(splited[1]), verticalIndex + Integer.parseInt(splited[2]) , 0,0,Integer.parseInt(splited[3]),Boolean.parseBoolean(splited[4]),Float.parseFloat(splited[5])	,splited[6]);
						currentLevel.addEntity(mp);
						break;
                    case "TimerPlatform":
                        TimerPlatform tp = new TimerPlatform(horizontalIndex - setX + Integer.parseInt(splited[1]), verticalIndex + Integer.parseInt(splited[2]) , 0,0,splited[3],Float.parseFloat(splited[4]), Integer.parseInt(splited[5]));
                        currentLevel.addEntity(tp);
                        break;
                    case "CrushingPlatform":
                        CrushingPlatform cp = new CrushingPlatform(horizontalIndex - setX  + Integer.parseInt(splited[1]), verticalIndex + Integer.parseInt(splited[2]) , 0,0,Integer.parseInt(splited[3]),Float.parseFloat(splited[4])	,splited[5],Integer.parseInt(splited[6]),splited[7], Float.parseFloat(splited[8]));
                        currentLevel.addEntity(cp);
                        break;
                    case "Respawn":
                        RespawnPoint rp = new RespawnPoint(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,"./img/".concat(splited[3]));
                        currentLevel.addEntity(rp);
                        break;
                    case "Area":

                        Area ad;
                        ad = new Area(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,"./img/".concat(splited[3]));
                        currentLevel.addEntity(ad);

                        break;
                    case "DamageZone":

                        //prepares a string array with the urls
                        List<String> list = new ArrayList<String>();
                        for (int l = 9;l < 9+Integer.parseInt(splited[8]);l++)
                            list.add("./img/".concat(splited[l]));

                        String[] arr = list.toArray(new String[0]);


                        DamageZone tmz;
                        tmz = new DamageZone(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Integer.parseInt(splited[3]),Integer.parseInt(splited[4]),Integer.parseInt(splited[5]),Integer.parseInt(splited[6]),splited[7],arr);
                        currentLevel.addEntity(tmz);

                        break;
                    case "ScriptedDamageZone":

                        //prepares a string array with the urls
                        List<String> list5 = new ArrayList<String>();
                        for (int l = 6+(3*Integer.parseInt(splited[5])) + 1 ;l < 6+(3*Integer.parseInt(splited[5])) + 1 +Integer.parseInt(splited[6+(3*Integer.parseInt(splited[5]))]);l++)
                            list5.add("./img/".concat(splited[l]));

                        String[] arr5 = list5.toArray(new String[0]);


                        ScriptedDamageZone adz;
                        adz = new ScriptedDamageZone(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Float.parseFloat(splited[3]),Integer.parseInt(splited[4]),arr5);
                        currentLevel.addEntity(adz);

                        Point adak;


                        adz.addPoint(new Point(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),1));

                        for (int l = 6;l < 6+(3*Integer.parseInt(splited[5]));l+=3)
                        {
                            adak= new Point(horizontalIndex - setX + Integer.parseInt(splited[l]),verticalIndex + Integer.parseInt(splited[l+1]),Integer.parseInt(splited[l+2]));
                            adz.addPoint(adak);
                        }

                        break;
                    case "EventDamageZone":

                        //prepares a string array with the urls
                        List<String> list4 = new ArrayList<String>();
                        for (int l = 10+(2*Integer.parseInt(splited[9])) + 1;l < 10+(2*Integer.parseInt(splited[9])) + 1 + Integer.parseInt(splited[10+(2*Integer.parseInt(splited[9]))]);l++)
                            list4.add("./img/".concat(splited[l]));

                        String[] arr4 = list4.toArray(new String[0]);


                        EventDamageZone edz;
                        edz = new EventDamageZone(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Integer.parseInt(splited[3]),Integer.parseInt(splited[4]),splited[5],Integer.parseInt(splited[6]),Integer.parseInt(splited[7]),arr4);


                        BufferedImage img;
                        int awidth=0,aheight=0;
                        try
                        {
                            //sets the width and height of the platform based on the provided image width and height
                            img = ImageIO.read( new File("./img/".concat(splited[8])));
                            awidth = img.getWidth();
                            aheight = img.getHeight();
                        }
                        catch ( IOException exc )
                        {
                            //TODO: Handle exception.
                        }

                        Area alol;
                        for (int l = 10;l < 10+(2*Integer.parseInt(splited[9]));l+=2)
                        {
                            alol= new Area(horizontalIndex - setX + Integer.parseInt(splited[1]) + Integer.parseInt(splited[l]),verticalIndex + Integer.parseInt(splited[2]) + Integer.parseInt(splited[l+1]),awidth,aheight,"");
                            edz.addArea(alol);
                            currentLevel.addEntity(alol);
                        }

                        currentLevel.addEntity(edz);

                        break;
                    case "Projectile":

                        List<String> list2 = new ArrayList<String>();
                        for (int j = 11;j < 11+Integer.parseInt(splited[10]);j++)
                            list2.add("./img/".concat(splited[j]));
                        String[] arr2 = list2.toArray(new String[0]);

                        Projectile pj;
                        pj = new Projectile(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Float.parseFloat(splited[3]),Float.parseFloat(splited[4]),Float.parseFloat(splited[5]),Float.parseFloat(splited[6]),Float.parseFloat(splited[7]), Integer.parseInt(splited[8]),splited[9],arr2);
                        currentLevel.addEntity(pj);
                        break;
                    case "AnimArea":
                        AnimArea cao;
                        List<String> list3 = new ArrayList<String>();
                        for (int l = 4;l < 4+Integer.parseInt(splited[3]);l++)
                            list3.add("./img/".concat(splited[l]));

                        String[] arr3 = list3.toArray(new String[0]);

                        cao = new AnimArea(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,arr3);

                        currentLevel.addEntity(cao);
                        break;
                    case "OnReachAnimArea":
                        OnReachAnimArea oraa;
                        List<String> list6 = new ArrayList<String>();
                        for (int l = 4;l < 4+Integer.parseInt(splited[3]);l++)
                            list6.add("./img/".concat(splited[l]));

                        String[] arr6 = list6.toArray(new String[0]);

                        oraa = new OnReachAnimArea(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,arr6);

                        currentLevel.addEntity(oraa);
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