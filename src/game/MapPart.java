package game;

import game.entities.GameObject;
import game.entities.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapPart {

	private String url;
	private int nrBlocks;

	public MapPart(String url, int nrBlocks) {

		this.url=url;
		this.nrBlocks=nrBlocks;

	}

	public int getNrBlocks() {
		return nrBlocks;
	}

	public String getUrl() {
		return url;
	}

	public void setNrBlocks(int nrBlocks) {
		this.nrBlocks = nrBlocks;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
