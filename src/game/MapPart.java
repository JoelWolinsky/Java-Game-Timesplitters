package game;

/**
 * Compound class to represent the different pieces/sections of the progressbar/minimap
 */

public class MapPart {

	private String url;
	private int nrBlocks;

	/**
	 * @param url Image of the progressbar/minimap piece/section
	 * @param nrBlocks The nr of total blocks in that section to be used when calculating how to split the minimap in
	 *                 even parts
	 */

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
