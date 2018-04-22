package objects.notAlive.mapObjects;

import java.awt.image.BufferedImage;

import objects.notAlive.NotAlive;

public class MapTile extends NotAlive{

	protected boolean passable;
	protected int tileType;
	/*
	public MapTile(String iconSrc) {
		super(iconSrc);
		// TODO Auto-generated constructor stub
	}
	*/
	public MapTile(BufferedImage image, boolean passable, String desc, int tileType)
	{
		super(image);
		if(tileType==0)isTall=false;
		else isTall=true;
		this.passable = passable;
		this.desc = desc;
		this.tileType = tileType;
	}
	public boolean getPassable()
	{
		return this.passable;
	}
	
	public int getTileType()
	{
		return this.tileType;
	}
}
