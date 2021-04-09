package game;

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
