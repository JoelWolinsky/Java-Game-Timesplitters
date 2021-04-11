package game;

import game.entities.GameObject;
import game.entities.players.Player;
import game.entities.areas.*;
import game.entities.platforms.CrushingPlatform;
import game.entities.platforms.MovingPlatform;
import game.entities.platforms.Platform;
import game.entities.platforms.TimerPlatform;
import game.graphics.Image;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Map {


    private int horizontalIndex = 0;
    private int verticalIndex = 0;
    private int setX = 426;
    private int setY = 384;
    private LinkedList<String> lastDirection = new LinkedList<>();
    private String currentTheme = "A";
    String texturePlatformDefault = "";
    String texturePlatformInverted = "";
    String textureFloor = "";
    String goUrl = "";


    /**
     * Parses a single command
     * @param currentLevel The level towards which we wish to parse
     * @param command The command we desire to parse
     */
    public void parseCommand(Level currentLevel, String command)
    {
        interpret(currentLevel,command.split("\\s+"));
    }

    /**
     * Parse an entire text file
     * @param currentLevel The level towards which we wish to parse
     * @param url Name of the file we wish to parse
     */
    public void parseFile(Level currentLevel, String url) {

        try {
            File myObj = new File("./src/game/segments/".concat(url).concat(".txt"));
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                interpret(currentLevel,data.split("\\s+"));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * The interpreter. Translates String commands into elements to be placed within the level
     * @param currentLevel The level towards which we wish to parse
     * @param splitted The splitted command into specific elements the interpreter can understand
     */
    public void interpret(Level currentLevel, String[] splitted){

        switch (splitted[0]) {

            case "Theme":
                if (!splitted[1].equals(currentTheme)) {
                    setX = Integer.parseInt(splitted[2]);
                    setY = Integer.parseInt(splitted[3]);
                    currentTheme = splitted[1];
                }
                texturePlatformDefault = splitted[4];
                texturePlatformInverted = splitted[5];
                textureFloor = splitted[6];
                break;

            case "Chunk":

                Area a;
                lastDirection.add(splitted[2]);
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
                BufferedImage newBufferedImage=null;

                try
                {
                    //sets the width and height of the platform based on the provided image width and height
                    newBufferedImage = ImageIO.read( new File("./img/".concat(splitted[3])));

                }
                catch ( IOException exc )
                {
                    //TODO: Handle exception.
                }

                if (newBufferedImage!=null)
                    a = new Area(horizontalIndex - setX, verticalIndex, setX, setY, newBufferedImage);
                else
                    a = new Area(horizontalIndex - setX, verticalIndex, setX, setY);

                currentLevel.addEntity(a);
                break;

            case "Platform":

                goUrl = "";
                int tempWidth=0,tempHeight=0;
                switch (splitted[1]) {
                    case "Default":
                        goUrl = texturePlatformDefault;
                        break;
                    case "Inverted":
                        goUrl = texturePlatformInverted;
                        break;
                    case "Custom":
                        if (splitted.length==5)
                            goUrl = splitted[4];
                        else if (splitted.length==6) {
                            tempWidth = Integer.parseInt(splitted[4]);
                            tempHeight = Integer.parseInt(splitted[5]);
                        }
                        break;
                }

                currentLevel.addEntity(new Platform(horizontalIndex - setX + Integer.parseInt(splitted[2]), verticalIndex + Integer.parseInt(splitted[3]), tempWidth, tempHeight, goUrl));
                break;

            case "Floor":

                currentLevel.addEntity(new Platform(horizontalIndex - setX, verticalIndex + setY, setX, 0, textureFloor));
                break;

            case "MovingPlatform":

                currentLevel.addEntity(new MovingPlatform(horizontalIndex - setX  + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]) , 0,0,Integer.parseInt(splitted[3]),Boolean.parseBoolean(splitted[4]),Float.parseFloat(splitted[5]),splitted[6]));
                break;

            case "TimerPlatform":

                currentLevel.addEntity(new TimerPlatform(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]) , 0,0,splitted[3],Float.parseFloat(splitted[4]), Integer.parseInt(splitted[5])));
                break;

            case "CrushingPlatform":

                currentLevel.addEntity(new CrushingPlatform(horizontalIndex - setX  + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]) , 0,0,Integer.parseInt(splitted[3]),Float.parseFloat(splitted[4])	,splitted[5],Integer.parseInt(splitted[6]),splitted[7], Float.parseFloat(splitted[8])));
                break;

            case "Respawn":

                currentLevel.addEntity(new RespawnPoint(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,Integer.parseInt(splitted[3]),Integer.parseInt(splitted[4]),"./img/".concat(splitted[5])));
                break;

            case "ExtendRespawn":

                currentLevel.addEntity(new ExtendedRespawnPoint(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),Integer.parseInt(splitted[3]),Integer.parseInt(splitted[4]),"./img/".concat(splitted[5])));
                break;

            case "Waypoint":

                currentLevel.addEntity(new Waypoint(horizontalIndex - setX + Float.parseFloat(splitted[1]),verticalIndex + Float.parseFloat(splitted[2]),0,0,"./img/".concat(splitted[3]), splitted[4], splitted[5], Integer.parseInt(splitted[6])));
                break;

            case "Area":

                currentLevel.addEntity(new Area(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,Image.loadImage("./img/".concat(splitted[3]))));
                break;

            case "DamageZone":

                currentLevel.addEntity(new DamageZone(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,Integer.parseInt(splitted[3]),Integer.parseInt(splitted[4]),Integer.parseInt(splitted[5]),Integer.parseInt(splitted[6]),splitted[7],splitted[8]));
                break;

            case "ScriptedDamageZone":

                LinkedList<Point> points = new LinkedList<>();
                int newIndex = createPoints(points,5,splitted);
                currentLevel.addEntity(new ScriptedDamageZone(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,Float.parseFloat(splitted[3]),points,Integer.parseInt(splitted[4]),splitted[newIndex]));

                break;

            case "EventDamageZone":


                EventDamageZone edz;
                edz = new EventDamageZone(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,Integer.parseInt(splitted[3]),Integer.parseInt(splitted[4]),splitted[5],Integer.parseInt(splitted[6]),Integer.parseInt(splitted[7]),Boolean.parseBoolean(splitted[8]),splitted[10+(4*Integer.parseInt(splitted[9]))]);

                Area alol;
                for (int l = 10;l < 10+(4*Integer.parseInt(splitted[9]));l+=4)
                {
                    alol= new Area(horizontalIndex - setX + Integer.parseInt(splitted[1]) + Integer.parseInt(splitted[l]),verticalIndex + Integer.parseInt(splitted[2]) + Integer.parseInt(splitted[l+1]),Integer.parseInt(splitted[l+2]),Integer.parseInt(splitted[l+3]));
                    edz.addArea(alol);
                    currentLevel.addEntity(alol);
                }

                currentLevel.addEntity(edz);

                break;

            case "EventScriptedDamageZone":

                //prepares a string array with the urls



                LinkedList<Point> pntz = new LinkedList<>();
                int newInd = createPoints(pntz,5,splitted);

                EventScriptedDamageZone esdz;
                esdz = new EventScriptedDamageZone(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,Float.parseFloat(splitted[3]),pntz,Integer.parseInt(splitted[4]),splitted[newInd]);



                Area kol;
                for (int l = newInd+1 +1 ;l < newInd+1 + 1 +(4*Integer.parseInt(splitted[newInd+1]));l+=4)
                {
                    kol= new Area(horizontalIndex - setX + Integer.parseInt(splitted[1]) + Integer.parseInt(splitted[l]),verticalIndex + Integer.parseInt(splitted[2]) + Integer.parseInt(splitted[l+1]),Integer.parseInt(splitted[l+2]),Integer.parseInt(splitted[l+3]));
                    esdz.addArea(kol);
                    currentLevel.addEntity(kol);
                }

                currentLevel.addEntity(esdz);

                break;
            case "TrackedScriptedDamageZone":

                //prepares a string array with the urls


                LinkedList<Point> pntzz = new LinkedList<>();

                int newInddd = createPoints(pntzz,5,splitted);


                for (Player player : currentLevel.getPlayers())
                {
                    TrackedScriptedDamageZone tsdz;
                    tsdz = new TrackedScriptedDamageZone(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,Float.parseFloat(splitted[3]),pntzz,Integer.parseInt(splitted[4]),player,splitted[newInddd]);

                    Area area;
                    for (int l = newInddd+1 + 1 ;l < newInddd+1 + 1 +(4*Integer.parseInt(splitted[newInddd+1]));l+=4)
                    {
                        area= new Area(horizontalIndex - setX + Integer.parseInt(splitted[1]) + Integer.parseInt(splitted[l]),verticalIndex + Integer.parseInt(splitted[2]) + Integer.parseInt(splitted[l+1]),Integer.parseInt(splitted[l+2]),Integer.parseInt(splitted[l+3]));
                        tsdz.addArea(area);
                        currentLevel.addEntity(area);
                    }

                    currentLevel.addEntity(tsdz);
                }



                break;
            case "TrackingAI":


                LinkedList<Point> pntzzz = new LinkedList<>();

                int newIndx = createPoints(pntzzz,5,splitted);



                for (Player player : currentLevel.getPlayers())
                {
                    TrackingAI tssdz;
                    tssdz = new TrackingAI(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,Float.parseFloat(splitted[3]),pntzzz,Integer.parseInt(splitted[4]),player,splitted[newIndx]);

                    Area arrea;
                    for (int l = newIndx+1 + 1 ;l < newIndx+1 + 1 +(4*Integer.parseInt(splitted[newIndx+1]));l+=4)
                    {
                        arrea= new Area(horizontalIndex - setX + Integer.parseInt(splitted[1]) + Integer.parseInt(splitted[l]),verticalIndex + Integer.parseInt(splitted[2]) + Integer.parseInt(splitted[l+1]),Integer.parseInt(splitted[l+2]),Integer.parseInt(splitted[l+3]));
                        tssdz.addArea(arrea);
                        currentLevel.addEntity(arrea);
                    }

                    currentLevel.addEntity(tssdz);
                }

                break;

            case "Projectile":

                float randomRangeX=0;
                float randomRangeY=0;

                if (Float.parseFloat(splitted[6])!=0)
                    randomRangeX = horizontalIndex - setX + Float.parseFloat(splitted[6]);
                if (Float.parseFloat(splitted[7])!=0)
                    randomRangeY = verticalIndex + Float.parseFloat(splitted[7]);

                currentLevel.addEntity(new Projectile(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,Float.parseFloat(splitted[3]),Float.parseFloat(splitted[4]),Float.parseFloat(splitted[5]),randomRangeX,randomRangeY, Integer.parseInt(splitted[8]),splitted[9],splitted[10]));
                break;

            case "AnimArea":

                currentLevel.addEntity(new AnimArea(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,splitted[3]));
                break;

            case "MindlessAI":

                currentLevel.addEntity(new MindlessAI(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,Integer.parseInt(splitted[3]),Integer.parseInt(splitted[4]),splitted[5]));
                break;

            case "MindlessAISpawner":

                currentLevel.addEntity(new MindlessAISpawner(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,Integer.parseInt(splitted[3]),Integer.parseInt(splitted[4]),Integer.parseInt(splitted[5]),splitted[6]));
                break;

            case "Chest":

                currentLevel.addEntity(new Chest(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,splitted[3]));
                break;

            case "SlowArea":

                currentLevel.addEntity(new SlowArea(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,splitted[3]));
                break;

            case "Portal":

                currentLevel.addEntity(new Portal(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,Integer.parseInt(splitted[3])*setY,Integer.parseInt(splitted[4]),Integer.parseInt(splitted[5]),horizontalIndex - setX,verticalIndex,splitted[6]));
                break;

            case "OnReachAnimArea":

                currentLevel.addEntity(new OnReachAnimArea(horizontalIndex - setX + Integer.parseInt(splitted[1]),verticalIndex + Integer.parseInt(splitted[2]),0,0,splitted[3]));
                break;

            case "Revert":

                if(!lastDirection.isEmpty())
                {
                    switch (lastDirection.getLast()) {
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
                    lastDirection.removeLast();
                }
                break;

        }
    }

    /**
     * Add index amount of points to the linkedlist provided, from index from the string array provided
     * @param points The linkedlist in which we will store the points
     * @param index The starting point and number of points
     * @param splited String array containing the points data
     */
    public int createPoints(LinkedList<Point> points,int index,String[] splited)
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