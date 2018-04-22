package graphics.tiles;

import java.awt.image.BufferedImage;

public class Tile {

	private BufferedImage image;
	private boolean passable;
	public int height, width;
	public Tile(BufferedImage image, int height, int width)
	{
		this.image = image;
		
		this.height = height;
		this.width = width;
	}
	public Tile(BufferedImage image, boolean passable)
	{
		this.image = image; 
		this.passable = passable;
	}
	public void update(boolean passable)
	{
		this.passable = passable;
	}
	public BufferedImage getImage()
	{
		return this.image;
	}
	public boolean getPassable()
	{
		return this.passable;
	}
}
