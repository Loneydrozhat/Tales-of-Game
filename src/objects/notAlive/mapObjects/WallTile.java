package objects.notAlive.mapObjects;

import java.awt.image.BufferedImage;

public class WallTile extends MapTile{
	public WallTile(BufferedImage image,boolean passable)
	{
		super(image,passable, "",1);
	}

}
