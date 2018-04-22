package graphics.tiles;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Tiles 
{
	protected int numCol;
	protected int numRow;
	protected int width;
	protected int height;
	private int tilesInRow = 20;
	protected Tile[] tiles;
	public BufferedImage resources;
	public Tiles()
	{
	}
	protected void loadTiles(String src)
	{
		try
		{
			resources = ImageIO.read(getClass().getResourceAsStream(src));
			numRow = resources.getHeight()/height;
			numCol = resources.getWidth()/width;
			tiles = new Tile[numCol*numRow];
			int column = -1;
			for(int i = 0; i<numRow*numCol;i++)
			{
				if(i%tilesInRow==0) column ++; 
				BufferedImage singleTile = resources.getSubimage(i*width, column*height, width, height);
				tiles[i] = new Tile(singleTile, height, width);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public Tile getTile(int index)
	{
		return this.tiles[index];
	}
}
