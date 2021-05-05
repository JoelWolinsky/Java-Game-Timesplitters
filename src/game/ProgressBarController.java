package game;

import game.entities.GameObject;
import game.entities.players.Player;
import game.graphics.LevelState;

import java.awt.*;
import java.util.ArrayList;

import static game.Level.*;

public class ProgressBarController extends GameObject {

    private int offset = 20;
    private int totalNrBlocks;
    private ArrayList<Player> listOfAddedBlips = new ArrayList<>();
    private static ArrayList<UIElement> myParts = new ArrayList<>();
    private static ArrayList<Blip> myBlips = new ArrayList<>();
    private String headType = "";

    public ProgressBarController(float x, float y, int width, int height, Level currentLevel, ArrayList<MapPart> mps) {
        super(x, y, 3, width, height);

        for (MapPart st : mps) {
            totalNrBlocks = totalNrBlocks + st.getNrBlocks();

        }
        for (MapPart st : mps) {
            UIElement uiElement = new UIElement(this.x + offset, this.y + 10, (int) ((((float) ((st.getNrBlocks() * 426) * 100) / (totalNrBlocks * 426)) / 100) * 600), 57, st.getUrl());
            myParts.add(uiElement);
            currentLevel.addToAddQueue(uiElement);
            offset = offset + (int) ((((float) ((st.getNrBlocks() * 426) * 100) / (totalNrBlocks * 426)) / 100) * 600);
        }

        this.width = width;

        if (getWallOfDeath() != null) {
            Blip bp = new Blip(this.x + getWallOfDeath().getX() / 2, this.y + 10, 20, 20, getWallOfDeath(), totalNrBlocks, "./img/dethHead.png");
            myBlips.add(bp);
            addToAddQueue(bp);
        }

    }

    /**
     * This adds a blip for each player in the progress bar when the game is loading
     */
    public void tick() {

        for (Player p : getPlayers()) {

            if (getLevelState() == LevelState.Waiting || getLevelState() == LevelState.Starting)
                if (!listOfAddedBlips.contains(p)) {
                    headType = p.getObjectModel().substring(p.getObjectModel().length() - 1);
                    Blip bp = new Blip(this.x, this.y + 20, 20, 20, p, totalNrBlocks, "./img/head".concat(headType).concat(".png"));
                    myBlips.add(bp);
                    addToAddQueue(bp);
                    listOfAddedBlips.add(p);
                }
        }

    }

    public void render(Graphics g, float xOffset, float yOffset) {

    }

    public static ArrayList<Blip> getAllBlips() {
        return myBlips;
    }

    public static ArrayList<UIElement> getProgressBarElements() {
        return myParts;
    }
}
