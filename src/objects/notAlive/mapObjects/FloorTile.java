package objects.notAlive.mapObjects;

import java.awt.image.BufferedImage;

public class FloorTile extends MapTile{
	/*
	public FloorTile(String iconSrc) {
		super(iconSrc);
	}
	*/
	public FloorTile(BufferedImage image, boolean passable)
	{
		super(image,passable, "Klocek",0);
	}

}
