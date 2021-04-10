package game;

import game.entities.GameObject;
import game.entities.players.Player;
import game.entities.areas.*;
import game.entities.platforms.CrushingPlatform;
import game.entities.platforms.MovingPlatform;
import game.entities.platforms.Platform;
import game.entities.platforms.TimerPlatform;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Map {


    private int horizontalIndex = 0;
    private int verticalIndex = 0;
    private int setX = 426;
    private int setY = 384;
    private String lastDirection = "";
    private String currentTheme = "A";
    String goUrl = "";
    private String[] splited;


    public void parseCommand(Level currentLevel, String command)
    {
        String[] splitted = command.split("\\s+");

        switch (splitted[0]){
            case "Chunk":

                Area a;
                lastDirection = splitted[2];
                switch (splitted[2]) {
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
                a = new Area(horizontalIndex - setX, verticalIndex, setX, setY, "./img/".concat(splitted[3]));
                currentLevel.addEntity(a);
                break;
            case "Area":

                Area aa;
                aa= new Area(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), setX, setY, "./img/".concat(splitted[3]));
                currentLevel.addEntity(aa);
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
            case "Platform":
                Platform p;
                int tempWidth=0,tempHeight=0;

                        if (splitted.length==5)
                            goUrl = splitted[4];
                        else if (splitted.length==6) {
                            tempWidth = Integer.parseInt(splitted[4]);
                            tempHeight = Integer.parseInt(splitted[5]);
                        }


                p = new Platform(horizontalIndex - setX + Integer.parseInt(splitted[2]), verticalIndex + Integer.parseInt(splitted[3]), tempWidth, tempHeight, goUrl);
                currentLevel.addEntity(p);
                break;


        }
    }

    public void mapParser(Level currentLevel, String url) {

        String texturePlatformDefault = "";
        String texturePlatformInverted = "";
        String textureFloor = "";

        try {
            File myObj = new File("./src/game/segments/".concat(url).concat(".txt"));
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {

                String data = myReader.nextLine();
                splited = data.split("\\s+");

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

                        Area a = new Area(horizontalIndex - setX, verticalIndex, setX, setY, "./img/".concat(splited[3]));
                        currentLevel.addEntity(a);
                        break;

                    case "Platform":

                        goUrl = "";
                        int tempWidth=0,tempHeight=0;
                        switch (splited[1]) {
                            case "Default":
                                goUrl = texturePlatformDefault;
                                break;
                            case "Inverted":
                                goUrl = texturePlatformInverted;
                                break;
                            case "Custom":
                                if (splited.length==5)
                                    goUrl = splited[4];
                                else if (splited.length==6) {
                                    tempWidth = Integer.parseInt(splited[4]);
                                    tempHeight = Integer.parseInt(splited[5]);
                                }
                                break;
                        }

                        Platform p = new Platform(horizontalIndex - setX + Integer.parseInt(splited[2]), verticalIndex + Integer.parseInt(splited[3]), tempWidth, tempHeight, goUrl);
                        currentLevel.addEntity(p);
                        break;

                    case "Floor":

                        Platform floor = new Platform(horizontalIndex - setX, verticalIndex + setY, setX, 0, textureFloor);
                        currentLevel.addEntity(floor);
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

                        RespawnPoint rp = new RespawnPoint(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Integer.parseInt(splited[3]),Integer.parseInt(splited[4]),"./img/".concat(splited[5]));
                        currentLevel.addEntity(rp);
                        break;

                    case "ExtendRespawn":

                        ExtendedRespawnPoint er = new ExtendedRespawnPoint(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),Integer.parseInt(splited[3]),Integer.parseInt(splited[4]),"./img/".concat(splited[3]));
                        currentLevel.addEntity(er);
                        break;

                    case "Waypoint":

                        Waypoint wp = new Waypoint(horizontalIndex - setX + Float.parseFloat(splited[1]),verticalIndex + Float.parseFloat(splited[2]),0,0,"./img/".concat(splited[3]), splited[4], splited[5], Integer.parseInt(splited[6]));
                        currentLevel.addEntity(wp);
                        break;

                    case "Area":

                        Area ad;
                        ad = new Area(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,"./img/".concat(splited[3]));
                        currentLevel.addEntity(ad);
                        break;

                    case "DamageZone":

                        DamageZone damageZone;
                        damageZone = new DamageZone(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Integer.parseInt(splited[3]),Integer.parseInt(splited[4]),Integer.parseInt(splited[5]),Integer.parseInt(splited[6]),splited[7],splited[8]);
                        currentLevel.addEntity(damageZone);
                        break;

                    case "ScriptedDamageZone":

                        LinkedList<Point> points = new LinkedList<>();
                        int newIndex = createPoints(points,5);
                        ScriptedDamageZone adz;
                        adz = new ScriptedDamageZone(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Float.parseFloat(splited[3]),points,Integer.parseInt(splited[4]),splited[newIndex]);
                        currentLevel.addEntity(adz);

                        break;

                    case "EventDamageZone":


                        EventDamageZone edz;
                        edz = new EventDamageZone(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Integer.parseInt(splited[3]),Integer.parseInt(splited[4]),splited[5],Integer.parseInt(splited[6]),Integer.parseInt(splited[7]),Boolean.parseBoolean(splited[8]),splited[10+(4*Integer.parseInt(splited[9]))]);

                        Area alol;
                        for (int l = 10;l < 10+(4*Integer.parseInt(splited[9]));l+=4)
                        {
                            alol= new Area(horizontalIndex - setX + Integer.parseInt(splited[1]) + Integer.parseInt(splited[l]),verticalIndex + Integer.parseInt(splited[2]) + Integer.parseInt(splited[l+1]),Integer.parseInt(splited[l+2]),Integer.parseInt(splited[l+3]),"");
                            edz.addArea(alol);
                            currentLevel.addEntity(alol);
                        }

                        currentLevel.addEntity(edz);

                        break;

                    case "EventScriptedDamageZone":

                        //prepares a string array with the urls



                        LinkedList<Point> pntz = new LinkedList<>();
                        int newInd = createPoints(pntz,5);

                        EventScriptedDamageZone esdz;
                        esdz = new EventScriptedDamageZone(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Float.parseFloat(splited[3]),pntz,Integer.parseInt(splited[4]),splited[newInd]);



                        Area kol;
                        for (int l = newInd+1 +1 ;l < newInd+1 + 1 +(4*Integer.parseInt(splited[newInd+1]));l+=4)
                        {
                            kol= new Area(horizontalIndex - setX + Integer.parseInt(splited[1]) + Integer.parseInt(splited[l]),verticalIndex + Integer.parseInt(splited[2]) + Integer.parseInt(splited[l+1]),Integer.parseInt(splited[l+2]),Integer.parseInt(splited[l+3]),"");
                            esdz.addArea(kol);
                            currentLevel.addEntity(kol);
                        }

                        currentLevel.addEntity(esdz);

                        break;
                    case "TrackedScriptedDamageZone":

                        //prepares a string array with the urls


                        LinkedList<Point> pntzz = new LinkedList<>();

                        int newInddd = createPoints(pntzz,5);


                        for (Player player : currentLevel.getPlayers())
                        {
                            TrackedScriptedDamageZone tsdz;
                            tsdz = new TrackedScriptedDamageZone(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Float.parseFloat(splited[3]),pntzz,Integer.parseInt(splited[4]),player,splited[newInddd]);

                            Area area;
                            for (int l = newInddd+1 + 1 ;l < newInddd+1 + 1 +(4*Integer.parseInt(splited[newInddd+1]));l+=4)
                            {
                                area= new Area(horizontalIndex - setX + Integer.parseInt(splited[1]) + Integer.parseInt(splited[l]),verticalIndex + Integer.parseInt(splited[2]) + Integer.parseInt(splited[l+1]),Integer.parseInt(splited[l+2]),Integer.parseInt(splited[l+3]),"");
                                tsdz.addArea(area);
                                currentLevel.addEntity(area);
                            }

                            currentLevel.addEntity(tsdz);
                        }



                        break;
                    case "TrackingAI":


                        LinkedList<Point> pntzzz = new LinkedList<>();

                        int newIndx = createPoints(pntzzz,5);



                        for (Player player : currentLevel.getPlayers())
                        {
                            TrackingAI tssdz;
                            tssdz = new TrackingAI(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Float.parseFloat(splited[3]),pntzzz,Integer.parseInt(splited[4]),player,splited[newIndx]);

                            Area arrea;
                            for (int l = newIndx+1 + 1 ;l < newIndx+1 + 1 +(4*Integer.parseInt(splited[newIndx+1]));l+=4)
                            {
                                arrea= new Area(horizontalIndex - setX + Integer.parseInt(splited[1]) + Integer.parseInt(splited[l]),verticalIndex + Integer.parseInt(splited[2]) + Integer.parseInt(splited[l+1]),Integer.parseInt(splited[l+2]),Integer.parseInt(splited[l+3]),"");
                                tssdz.addArea(arrea);
                                currentLevel.addEntity(arrea);
                            }

                            currentLevel.addEntity(tssdz);
                        }





                        break;
                    case "Projectile":

                        float randomRangeX=0;
                        float randomRangeY=0;

                        if (Float.parseFloat(splited[6])!=0)
                            randomRangeX = horizontalIndex - setX + Float.parseFloat(splited[6]);
                        if (Float.parseFloat(splited[7])!=0)
                            randomRangeY = verticalIndex + Float.parseFloat(splited[7]);

                        Projectile projectile;
                        projectile = new Projectile(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Float.parseFloat(splited[3]),Float.parseFloat(splited[4]),Float.parseFloat(splited[5]),randomRangeX,randomRangeY, Integer.parseInt(splited[8]),splited[9],splited[10]);
                        currentLevel.addEntity(projectile);
                        break;

                    case "AnimArea":

                        AnimArea animArea;
                        animArea = new AnimArea(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,splited[3]);
                        currentLevel.addEntity(animArea);
                        break;

                    case "MindlessAI":

                        MindlessAI mindlessAI;
                        mindlessAI = new MindlessAI(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Integer.parseInt(splited[3]),Integer.parseInt(splited[4]),splited[5]);
                        currentLevel.addEntity(mindlessAI);
                        break;

                    case "MindlessAISpawner":

                        MindlessAISpawner mindlessAISpawner;
                        mindlessAISpawner = new MindlessAISpawner(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Integer.parseInt(splited[3]),Integer.parseInt(splited[4]),Integer.parseInt(splited[5]),splited[6]);
                        currentLevel.addEntity(mindlessAISpawner);
                        break;

                    case "Chest":

                        Chest chest;
                        chest = new Chest(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,splited[3]);
                        currentLevel.addEntity(chest);
                        break;

                    case "SlowArea":

                        SlowArea slowArea;
                        slowArea = new SlowArea(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,splited[3]);
                        currentLevel.addEntity(slowArea);
                        break;

                    case "Portal":

                        Portal portal;
                        portal = new Portal(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,Integer.parseInt(splited[3])*setY,Integer.parseInt(splited[4]),Integer.parseInt(splited[5]),horizontalIndex - setX,verticalIndex,splited[6]);
                        currentLevel.addEntity(portal);
                        break;

                    case "OnReachAnimArea":

                        OnReachAnimArea onReachAnimArea;
                        onReachAnimArea = new OnReachAnimArea(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),0,0,splited[3]);
                        currentLevel.addEntity(onReachAnimArea);
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

    public String[] createURLS(int index)
    {
        //prepares a string array with the urls
        List<String> list = new ArrayList<String>();
        for (int l = index+1 ; l < index+1 + Integer.parseInt(splited[index]); l++)
            list.add("./img/".concat(splited[l]));

        String[] arr = list.toArray(new String[0]);

        return arr;
    }

    public int createPoints(LinkedList<Point> points,int index)
    {

        Point adak;
        points.add(new Point(horizontalIndex - setX + Integer.parseInt(splited[1]),verticalIndex + Integer.parseInt(splited[2]),1));

        for (int l = index+1 ;l < index+1 +(3*Integer.parseInt(splited[index]));l+=3)
        {
            adak= new Point(horizontalIndex - setX + Integer.parseInt(splited[l]),verticalIndex + Integer.parseInt(splited[l+1]),Float.parseFloat(splited[l+2]));
            points.add(adak);
        }

        return index+1 +(3*Integer.parseInt(splited[index]));
    }
    public void addAreas(GameObject o)
    {


    }
}