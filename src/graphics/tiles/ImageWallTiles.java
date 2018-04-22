package graphics.tiles;

public class ImageWallTiles extends Tiles{
	
	public ImageWallTiles(String src)
	{
		this.width = 64;
		this.height = 64;
		loadTiles(src);
	}

}
