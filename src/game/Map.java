package game;

import game.entities.GameObject;
import game.entities.players.Player;
import game.entities.areas.*;
import game.entities.platforms.CrushingPlatform;
import game.entities.platforms.MovingPlatform;
import game.entities.platforms.Platform;
import game.entities.platforms.TimerPlatform;
import game.graphics.GameMode;
import game.graphics.Image;
import game.graphics.MapMode;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Map {

    private int horizontalIndex = 0;
    private int verticalIndex = 0;
    private int setX = 426;
    private int setY = 384;
    private final LinkedList<String> lastDirection = new LinkedList<>();
    private String currentTheme = "A";
    String texturePlatformDefault = "";
    String texturePlatformInverted = "";
    String textureFloor = "";
    String goUrl = "";
    int index = 0;
    int bottom;
    int top;
    private int newIndex=0;
    public ArrayList<MapPart> mps = new ArrayList<>();
    BackgroundController ctrlr;

    public Level currentLevel;
    int defaultSeed = 0;

    ArrayList<String> segments3,segments1,segments2,segments4,wizard,introDimension,castleEntrance,throneRoom, segments5;

    public Map (MapMode mapMode, GameMode gameMode){

        currentLevel = new Level();

        switch (mapMode)
        {
            case debug:

                parseFile(currentLevel, "intro1");
                //parseFile(currentLevel,"segmentA2");
                parseFile(currentLevel,"segmentA1");
			/*
			*** LEGEND ***
			m.mapParser(currentLevel, "intro1");				// No go zone 								-- NO X VERSION
			m.mapParser(currentLevel, "intro2");				// Basic chandelier room
			m.mapParser(currentLevel, "introDimension");		// Pink portal

			m.mapParser(currentLevel, "segmentA1");				// web segment
			m.mapParser(currentLevel, "segmentA2");				// electric one
			m.mapParser(currentLevel, "segmentA3");				// aesthetic hall 1
			m.mapParser(currentLevel, "segmentA4");				// aesthetic hall 2
			m.mapParser(currentLevel, "segmentA5");				// aesthetic hall 3
			m.mapParser(currentLevel, "segmentA6");				// ghosts
			m.mapParser(currentLevel, "segmentA7");				// platforms
			m.mapParser(currentLevel, "segmentA8");				// disappearing long and small platforms 	-- NEEDS WORK
			m.mapParser(currentLevel, "segmentA9");				// spinning fireball one 					-- NOT DOING AI VERSION
			m.mapParser(currentLevel, "segmentA10");			// long corridor
			m.mapParser(currentLevel, "segmentA11");			// disappearing platforms over acid
			m.mapParser(currentLevel, "segmentA12");			// bookshelf pyramid
			m.mapParser(currentLevel, "segmentA13");			// wizard and crushing bookshelves 			-- NOT DOING AI VERSION
			m.mapParser(currentLevel, "segmentA14");			// interstellar bookshelf columns 			-- WAIT UNTIL DEBUGGED

			m.mapParser(currentLevel, "intersegmentA1"); 		// skeletons throwing objects down
			m.mapParser(currentLevel, "intersegmentA2");		// falling rocks
			m.mapParser(currentLevel, "intersegmentA2up");		// falling chandeliers
			m.mapParser(currentLevel, "intersegmentA2down");	// falling chandeliers
			m.mapParser(currentLevel, "intersegmentA3");		// hands one
		    */
                break;

            case RNG:
                //create different segment pools for the different parts of the game
                //we want them separated in order to keep a specific order in our game
                segments1 = new ArrayList<String>(Arrays.asList("segmentA1","segmentA2","intersegmentA2","intersegmentA1","intersegmentA2up","intersegmentA2down"));
                segments2 = new ArrayList<String>(Arrays.asList("segmentA3","segmentA4","segmentA4"));
                castleEntrance = new ArrayList<String>(Arrays.asList("intro2"));
                throneRoom = new ArrayList<String>(Arrays.asList("segmentA5"));
                segments3 = new ArrayList<String>(Arrays.asList("segmentA6","segmentA7","segmentA8","segmentA9","segmentA10","segmentA11"));
                segments4 = new ArrayList<String>(Arrays.asList("segmentA15","segmentA12","segmentA15"));
                wizard = new ArrayList<String>(Arrays.asList("segmentA13"));
                introDimension = new ArrayList<String>(Arrays.asList("introDimension"));
                segments5 = new ArrayList<String>(Arrays.asList("segmentA14","segmentA16"));

                //choose the limits of your map this should also be related to how many levels you have defined
                //eg. you don't want to have 20 levels but a top limit of only 2 since 17 of the leves will never be used
                bottom=0;
                top=2;

                //add levels to the background controller which manages the current position within the horizontal panorama
                ctrlr = new BackgroundController(new BackgroundStates("ground1.png","ground2.png"),new BackgroundStates("sky1.png","sky2.png"),new BackgroundStates("sky3.png","sky4.png"),new BackgroundStates("sky5.png","sky6.png"));

                //generate the segment pools - order is important
                parseFile(currentLevel, "intro1");
                ArrayList<Integer> allParts;
                if (gameMode == GameMode.SINGLEPLAYER) {
	                allParts = new ArrayList<>(Arrays.asList(
	                        randomGenerate(segments1),
	                        randomGenerate(castleEntrance) + randomGenerate(segments2) + randomGenerate(throneRoom),
	                        randomGenerate(segments3),
	                        randomGenerate(segments4) + randomGenerate(wizard) + randomGenerate(introDimension),
	                        randomGenerate(segments5)));
                } else {
                	allParts = new ArrayList<>(Arrays.asList(
	                        randomGenerate(segments1, defaultSeed),
	                        randomGenerate(castleEntrance, defaultSeed) + randomGenerate(segments2, defaultSeed) + randomGenerate(throneRoom, defaultSeed),
	                        randomGenerate(segments3, defaultSeed),
	                        randomGenerate(segments4, defaultSeed) + randomGenerate(wizard, defaultSeed) + randomGenerate(introDimension, defaultSeed),
	                        randomGenerate(segments5, defaultSeed)));
                }

                //create specific progressbar parts for the generated segment pools
                for (Integer i =0 ; i<allParts.size();i++)
                {
                    mps.add(new MapPart("./img/minipart".concat(i.toString()).concat(".png"),allParts.get(i)));
                }
                break;

        }



    }

    /**
     * Parses a single command
     *
     * @param currentLevel The level towards which we wish to parse
     * @param command      The command we desire to parse
     */
    public void parseCommand(Level currentLevel, String command) {
        interpret(currentLevel, command.split("\\s+"));
    }

    /**
     * Parse an entire text file
     *
     * @param currentLevel The level towards which we wish to parse
     * @param url          Name of the file we wish to parse
     */
    public void parseFile(Level currentLevel, String url) {

        try {
            File myObj = new File("./src/game/segments/".concat(url).concat(".txt"));
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                interpret(currentLevel, data.split("\\s+"));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * The interpreter. Translates String commands into elements to be placed within the level
     *
     * @param currentLevel The level towards which we wish to parse
     * @param splitted     The splitted command into specific elements the interpreter can understand
     */
    public void interpret(Level currentLevel, String[] splitted) {

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
                BufferedImage newBufferedImage = null;

                try {
                    //sets the width and height of the platform based on the provided image width and height
                    newBufferedImage = ImageIO.read(new File("./img/".concat(splitted[3])));

                } catch (IOException exc) {
                    //TODO: Handle exception.
                }

                if (newBufferedImage != null)
                    a = new Area(horizontalIndex - setX, verticalIndex, setX, setY, newBufferedImage);
                else
                    a = new Area(horizontalIndex - setX, verticalIndex, setX, setY);

                currentLevel.addEntity(a);
                break;

            case "Platform":

                goUrl = "";
                int tempWidth = 0, tempHeight = 0;
                switch (splitted[1]) {
                    case "Default":
                        goUrl = texturePlatformDefault;
                        break;
                    case "Inverted":
                        goUrl = texturePlatformInverted;
                        break;
                    case "Custom":
                        if (splitted.length == 5)
                            goUrl = splitted[4];
                        else if (splitted.length == 6) {
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

                currentLevel.addEntity(new MovingPlatform(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Integer.parseInt(splitted[3]), Boolean.parseBoolean(splitted[4]), Float.parseFloat(splitted[5]), splitted[6]));
                break;

            case "TimerPlatform":

                currentLevel.addEntity(new TimerPlatform(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, splitted[3], Float.parseFloat(splitted[4]), Integer.parseInt(splitted[5])));
                break;

            case "CrushingPlatform":

                currentLevel.addEntity(new CrushingPlatform(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Integer.parseInt(splitted[3]), Float.parseFloat(splitted[4]), splitted[5], Integer.parseInt(splitted[6]), splitted[7], Float.parseFloat(splitted[8])));
                break;

            case "Respawn":

                currentLevel.addEntity(new RespawnPoint(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Integer.parseInt(splitted[3]), Integer.parseInt(splitted[4]), "./img/".concat(splitted[5])));
                break;

            case "ExtendRespawn":

                currentLevel.addEntity(new ExtendedRespawnPoint(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]), Integer.parseInt(splitted[4]), "./img/".concat(splitted[5])));
                break;

            case "Waypoint":

                currentLevel.addEntity(new Waypoint(horizontalIndex - setX + Float.parseFloat(splitted[1]), verticalIndex + Float.parseFloat(splitted[2]), 0, 0, "./img/".concat(splitted[3]), splitted[4], splitted[5], Integer.parseInt(splitted[6])));
                break;

            case "Area":

                currentLevel.addEntity(new Area(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Image.loadImage("./img/".concat(splitted[3]))));
                break;

            case "DamageZone":

                currentLevel.addEntity(new DamageZone(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Integer.parseInt(splitted[3]), Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]), Integer.parseInt(splitted[6]), splitted[7], splitted[8]));
                break;

            case "ScriptedDamageZone":

                LinkedList<Point> points = new LinkedList<>();
                newIndex = createPoints(points, 5, splitted);
                currentLevel.addEntity(new ScriptedDamageZone(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Float.parseFloat(splitted[3]), points, Integer.parseInt(splitted[4]), splitted[newIndex]));
                break;

            case "EventDamageZone":

                EventDamageZone eventDamageZone = new EventDamageZone(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Integer.parseInt(splitted[3]), Integer.parseInt(splitted[4]), splitted[5], Integer.parseInt(splitted[6]), Integer.parseInt(splitted[7]), Boolean.parseBoolean(splitted[8]), splitted[9]);
                addAreas(currentLevel, eventDamageZone, 10, splitted);
                currentLevel.addEntity(eventDamageZone);
                break;

            case "EventScriptedDamageZone":

                LinkedList<Point> pntz = new LinkedList<>();
                newIndex = createPoints(pntz, 5, splitted);
                EventScriptedDamageZone eventScriptedDamageZone = new EventScriptedDamageZone(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Float.parseFloat(splitted[3]), pntz, Integer.parseInt(splitted[4]), splitted[newIndex]);
                addAreas(currentLevel, eventScriptedDamageZone, newIndex + 1, splitted);
                currentLevel.addEntity(eventScriptedDamageZone);

                break;
            case "TrackedScriptedDamageZoneSpawner":

                LinkedList<Point> pntzz = new LinkedList<>();
                newIndex = createPoints(pntzz, 5, splitted);

                //Create one for each player
                TrackedScriptedDamageZoneSpawner trackedScriptedDamageZoneSpawner = new TrackedScriptedDamageZoneSpawner(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Float.parseFloat(splitted[3]), pntzz, addAreasList(currentLevel,newIndex + 1, splitted), Integer.parseInt(splitted[4]), splitted[newIndex]);
                currentLevel.addEntity(trackedScriptedDamageZoneSpawner);
                break;

            case "TrackingAISpawner":

                LinkedList<Point> pntzzz = new LinkedList<>();
                newIndex = createPoints(pntzzz, 5, splitted);

                TrackingAISpawner trackingAISpawner = new TrackingAISpawner(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Float.parseFloat(splitted[3]), pntzzz, addAreasList(currentLevel,newIndex + 1, splitted), Integer.parseInt(splitted[4]), splitted[newIndex]);
                currentLevel.addEntity(trackingAISpawner);
                break;

            case "Projectile":

                float randomRangeX = 0;
                float randomRangeY = 0;

                if (Float.parseFloat(splitted[6]) != 0)
                    randomRangeX = horizontalIndex - setX + Float.parseFloat(splitted[6]);
                if (Float.parseFloat(splitted[7]) != 0)
                    randomRangeY = verticalIndex + Float.parseFloat(splitted[7]);

                currentLevel.addEntity(new Projectile(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Float.parseFloat(splitted[3]), Float.parseFloat(splitted[4]), Float.parseFloat(splitted[5]), randomRangeX, randomRangeY, Integer.parseInt(splitted[8]), splitted[9], splitted[10]));
                break;

            case "AnimArea":

                currentLevel.addEntity(new AnimArea(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, splitted[3]));
                break;

            case "MindlessAI":

                currentLevel.addEntity(new MindlessAI(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Integer.parseInt(splitted[3]), Integer.parseInt(splitted[4]), splitted[5]));
                break;

            case "MindlessAISpawner":

                currentLevel.addEntity(new MindlessAISpawner(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Integer.parseInt(splitted[3]), Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]), splitted[6]));
                break;

            case "Chest":

                currentLevel.addEntity(new Chest(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, splitted[3]));
                break;

            case "SlowArea":

                currentLevel.addEntity(new SlowArea(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, splitted[3]));
                break;

            case "Portal":

                currentLevel.addEntity(new Portal(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, Integer.parseInt(splitted[3]) * setY, Integer.parseInt(splitted[4]), Integer.parseInt(splitted[5]), horizontalIndex - setX, verticalIndex, splitted[6]));
                break;

            case "OnReachAnimArea":

                currentLevel.addEntity(new OnReachAnimArea(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, splitted[3]));
                break;
            case "GameEndingObject":

                currentLevel.addEntity(new GameEndingObject(horizontalIndex - setX + Integer.parseInt(splitted[1]), verticalIndex + Integer.parseInt(splitted[2]), 0, 0, splitted[3]));
                break;

            case "Revert":

                if (!lastDirection.isEmpty()) {
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
     *
     * @param points  The linkedlist in which we will store the points
     * @param index   The starting point and number of points
     * @param splited String array containing the points data
     * @return returns the current index of the input string after the points
     */
    public int createPoints(LinkedList<Point> points, int index, String[] splited) {

        Point adak;
        points.add(new Point(horizontalIndex - setX + Integer.parseInt(splited[1]), verticalIndex + Integer.parseInt(splited[2]), 1));

        for (int l = index + 1; l < index + 1 + (3 * Integer.parseInt(splited[index])); l += 3) {
            adak = new Point(horizontalIndex - setX + Integer.parseInt(splited[l]), verticalIndex + Integer.parseInt(splited[l + 1]), Float.parseFloat(splited[l + 2]));
            points.add(adak);
        }

        return index + 1 + (3 * Integer.parseInt(splited[index]));
    }

    /**
     * Add index amount of areas to the gameobject provided, from index from the string array provided
     *
     * @param currentLevel the level to which the areas will be added
     * @param o            the object towards which the areas are associated
     * @param index        the index at which the function will start adding the areas from
     * @param splited      String array containing all of the input data
     * @return returns the current index of the input string after the areas
     */

    public int addAreas(Level currentLevel, GameObject o, int index, String[] splited) {

        Area area;
        for (int l = index + 1; l < index + 1 + (4 * Integer.parseInt(splited[index])); l += 4) {
            area = new Area(horizontalIndex - setX + Integer.parseInt(splited[1]) + Integer.parseInt(splited[l]), verticalIndex + Integer.parseInt(splited[2]) + Integer.parseInt(splited[l + 1]), Integer.parseInt(splited[l + 2]), Integer.parseInt(splited[l + 3]));
            if (o instanceof EventScriptedDamageZone)
                ((EventScriptedDamageZone) o).addArea(area);
            else if (o instanceof EventDamageZone)
                ((EventDamageZone) o).addArea(area);
            currentLevel.addEntity(area);
        }

        return index + 1 + (4 * Integer.parseInt(splited[index]));

    }

    public ArrayList<Area> addAreasList(Level currentLevel, int index, String[] splited) {

        ArrayList<Area> areasOfEffect = new ArrayList<>();
        Area area;
        for (int l = index + 1; l < index + 1 + (4 * Integer.parseInt(splited[index])); l += 4) {
            area = new Area(horizontalIndex - setX + Integer.parseInt(splited[1]) + Integer.parseInt(splited[l]), verticalIndex + Integer.parseInt(splited[2]) + Integer.parseInt(splited[l + 1]), Integer.parseInt(splited[l + 2]), Integer.parseInt(splited[l + 3]));
            currentLevel.addEntity(area);
            areasOfEffect.add(area);
        }

        return areasOfEffect;

    }
    
    public int randomGenerate(ArrayList<String> segmentPool) {
    	return randomGenerate(segmentPool, new Random().nextInt());
    }

    public int randomGenerate(ArrayList<String> segmentPool, int seed) {

        int nrBlocks=0;
        int rnd1;

        while (!segmentPool.isEmpty()){

            //choose segment at random from segment pool
            rnd1 = new Random(seed).nextInt(segmentPool.size());

            //if picked segment is not allowed when on top level or bottom level reroll till it's something valid
            //when the map is on level 0 we don't want to go further down
            if (index == bottom)
                while (segmentPool.get(rnd1).equals("intersegmentA2down")) {
                    rnd1 = new Random().nextInt(segmentPool.size());
                }

            //when the map is on the top most level chosen we don't want to go further up
            if (index == top)
                while (segmentPool.get(rnd1).equals("intersegmentA2up")) {
                    rnd1 = new Random().nextInt(segmentPool.size());
                }

            //custom behaviour for Castle Dungeon -- put an intersegmentA3 before each segment
            if (belongsTo(segmentPool.get(rnd1),segments3))
                nrBlocks = nrBlocks + randomGenerate(new ArrayList<String>(Arrays.asList("intersegmentA3")));

            //advance by 1 block
            parseCommand(currentLevel, "Chunk 1 E asdgasdg.png");

            try
            {
                //initialize object to read first line of the picked segment
                File myObj = new File("./src/game/segments/".concat(segmentPool.get(rnd1)).concat(".txt"));
                Scanner myReader;
                myReader = new Scanner(myObj);
                String data = myReader.nextLine();
                String[] splix = data.split("\\s+");

                //check if segment is of size 1 or 2 or custom and generate background accordingly
                switch (splix[8]){

                    //default 1 block size segment -- no level change
                    case "1":

                        //custom behaviour for segments 1
                        if (belongsTo(segmentPool.get(rnd1),segments1))
                        {
                            if (index == 0) {
                                parseCommand(currentLevel, "Platform Custom -86 384 customFloor3.png");
                                parseCommand(currentLevel, "Platform Custom 340 384 customFloor3.png");
                            }
                            else {
                                parseCommand(currentLevel, "Platform Custom 54 384 edge1.png");
                                parseCommand(currentLevel, "Platform Custom 338 384 edge1inv.png");
                            }
                        }

                        parseCommand(currentLevel, "Area 0 -384 ".concat(ctrlr.getCurrent(index+1)));
                        parseCommand(currentLevel, "Area 0 0 ".concat(ctrlr.getCurrent(index)));

                        if (index>0)
                        {
                            parseCommand(currentLevel, "Area 0 384 ".concat(ctrlr.getCurrent(index-1)));
                            if (index>1)
                                parseCommand(currentLevel, "Area 0 768 ".concat(ctrlr.getCurrent(index-2)));
                        }

                        ctrlr.incrementStateIndex();
                        nrBlocks++;
                        break;

                    //default 2 block size segment -- no level change
                    case "2":

                        parseCommand(currentLevel, "Area 0 -384 ".concat(ctrlr.getCurrent(index+1)));
                        parseCommand(currentLevel, "Area 426 -384 ".concat(ctrlr.getNext(index+1)));

                        parseCommand(currentLevel, "Area 0 0 ".concat(ctrlr.getCurrent(index)));
                        parseCommand(currentLevel, "Area 426 0 ".concat(ctrlr.getNext(index)));

                        if (index>0)
                        {
                            parseCommand(currentLevel, "Area 0 384 ".concat(ctrlr.getCurrent(index-1)));
                            parseCommand(currentLevel, "Area 426 384 ".concat(ctrlr.getNext(index-1)));

                            parseCommand(currentLevel, "Area 0 384 pillars.png");
                            parseCommand(currentLevel, "Area 426 384 pillars.png");
                            parseCommand(currentLevel, "Platform Custom 0 768 floorA.png");
                            parseCommand(currentLevel, "Platform Custom 426 768 floorA.png");

                            if (index>1)
                            {
                                parseCommand(currentLevel, "Area 0 768 ".concat(ctrlr.getCurrent(index-2)));
                                parseCommand(currentLevel, "Area 426 768 ".concat(ctrlr.getNext(index-2)));

                                parseCommand(currentLevel, "Area 0 768 pillars.png");
                                parseCommand(currentLevel, "Area 426 768 pillars.png");

                            }
                        }

                        ctrlr.incrementStateIndex();
                        ctrlr.incrementStateIndex();
                        nrBlocks+=2;
                        break;

                    case "Custom":

                        //here you can define special behaviour for certain segments
                        switch (splix[9]){

                            //this segment elevates the level
                            case "intersegmentA2up":
                                if (index == 0) {
                                    parseCommand(currentLevel, "Platform Custom -86 384 customFloor3.png");
                                    parseCommand(currentLevel, "Platform Custom 340 384 customFloor3.png");
                                } else {
                                    parseCommand(currentLevel, "Platform Custom 54 384 edge1.png");
                                    parseCommand(currentLevel, "Platform Custom 340 384 customFloor3.png");
                                }

                                if (index>0)
                                {
                                    parseCommand(currentLevel, "Area 0 384 ".concat(ctrlr.getCurrent(index-1)));
                                    if (index>1)
                                        parseCommand(currentLevel, "Area 0 768 ".concat(ctrlr.getCurrent(index-2)));
                                }

                                parseCommand(currentLevel, "Area 0 0 ".concat(ctrlr.getCurrent(index)));
                                parseCommand(currentLevel, "Area 0 -384 ".concat(ctrlr.getCurrent(index+1)));
                                parseCommand(currentLevel, "Area 0 -768 ".concat(ctrlr.getCurrent(index+2)));
                                parseCommand(currentLevel, "Area -426 -768 ".concat(ctrlr.getPrevious(index+2)));


                                index++;

                                ctrlr.incrementStateIndex();
                                nrBlocks++;
                                break;

                            //this segment decreses elevation of the level
                            case "intersegmentA2down":
                                if (index == 1) {
                                    parseCommand(currentLevel, "Platform Custom -86 768 customFloor3.png");
                                    parseCommand(currentLevel, "Platform Custom 340 768 customFloor3.png");
                                } else {
                                    parseCommand(currentLevel, "Platform Custom 338 768 edge1inv.png");
                                    parseCommand(currentLevel, "Platform Custom -86 768 customFloor3.png");
                                }

                                parseCommand(currentLevel, "Area 0 -384 ".concat(ctrlr.getCurrent(index+1)));
                                parseCommand(currentLevel, "Area 426 -384 ".concat(ctrlr.getNext(index+1)));
                                parseCommand(currentLevel, "Area 0 0 ".concat(ctrlr.getCurrent(index)));
                                if (index>0)
                                {
                                    parseCommand(currentLevel, "Area 0 384 ".concat(ctrlr.getCurrent(index-1)));
                                    if(index>1)
                                        parseCommand(currentLevel, "Area 0 768 ".concat(ctrlr.getCurrent(index-2)));
                                }

                                index--;

                                ctrlr.incrementStateIndex();
                                nrBlocks++;
                                break;
                        }
                        break;
                }


            }
            catch (FileNotFoundException e)
            {
            }

            //revert the 1 block advancement at the start of the while loop
            parseCommand(currentLevel, "Revert");
            //draw the contents of the segment
            parseFile(currentLevel, segmentPool.get(rnd1));
            //rremove the segment from the segment pool
            segmentPool.remove(rnd1);


        }

        return nrBlocks;

    }

    public boolean belongsTo(String s, ArrayList<String> al)
    {
        for (String z : al) {
            if (z.equals(s)){
                return true;
            }
        }
        return false;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }
}