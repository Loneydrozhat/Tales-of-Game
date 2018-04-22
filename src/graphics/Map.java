package graphics;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import abilities.Openable;
import boards.GameBoard;
import exceptions.CannotWalkException;
import graphics.panels.PanelOnScreen;
import graphics.tiles.ImageFloorTiles;
import graphics.tiles.Tiles;
import graphics.tiles.ImageWallTiles;
import objects.MainObject;
import objects.alive.monsters.Mob;
import objects.notAlive.items.Item;
import objects.notAlive.mapObjects.*;
import utilities.MapObjectList;
import utilities.MapTileListener;
import utilities.Position;


public class Map extends JPanel implements PanelOnScreen
{
	private ImageFloorTiles floorTiles;
	private ImageWallTiles wallTiles;
	private static int mapWidth = 110;
	private static int mapHeight = 110;
	private ArrayList <Tiles> tiles;
	public static Lock drawingChanges = new ReentrantLock();
	public static Lock offsetLock = new ReentrantLock();
	private static Lock mapListLock = new ReentrantLock();
	public static MapObjectList listOfTiles;
	public static MapObjectList map[][];
	private static volatile double xOffset=0;
	private static volatile double yOffset=0;
	private MapTileListener listeners[][];
	public static int lastXModified = 0;
	public static int lastYModified = 0;
	public Map()
	{
		super(null);
		setBounds((int)((1920-180)/2 - 32*17), 0, (int) (64*17), 64*13);
		listOfTiles = new MapObjectList();
		map = new MapObjectList[mapWidth][mapHeight];
		tiles = new ArrayList<Tiles>();
		wallTiles = new ImageWallTiles("/WallTiles.png");
		floorTiles = new ImageFloorTiles("/FloorTiles.png");
		tiles.add(floorTiles);
		tiles.add(wallTiles);
		newLoadDB();
		newLoadMap();
		Position position = GameBoard.hero.getPosition();
		map[position.getXPosition()][position.getYPosition()].add(GameBoard.hero);
		listeners = new MapTileListener[17][13];
		for(int i=0;i<13;i++)
		{
			for(int j=0;j<17;j++)
				{
					listeners[j][i] = new MapTileListener(new Position(j,i),(int)((1920-180)/2 - 32*17)+j*64,i*64);
					listeners[j][i].setName("Listener on: " + i + "," + j);
					add(listeners[j][i]);
				}
		}
		revalidate();
	}
	@SuppressWarnings("unused")
	public void newLoadDB()
	{
		File file = new File("MapFiles//Map//ItemSources.mc");
		Scanner input = null;
		try
		{
			input = new Scanner(file);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Cos nie dziala");
		}
		int x = 0;
		int idFloor = 0;
		int idWall = 0;
		while(input.hasNextLine())
		{
			String inputline = input.nextLine();
			int ite=0;
			char[] line = inputline.toCharArray();
			String code = "";
			boolean passable;
			int tileType;
			while(line[ite]!=',')
			{
				code+=line[ite++];
			}
			ite++;
			if(line[ite]=='1') passable=false;
			else passable=true;
			ite+=2;
			if(line[ite]=='1') tileType = 1;
			else tileType = 0;
			ite++;
			String desc = "";
			for(ite++;ite<line.length;ite++)
			{
				desc+=line[ite];
			} 	
			MapTile tile = null;
			BufferedImage image = null;
			int id = 0;
			if(tileType == 1) 
			{
				id = idWall;
				image = tiles.get(tileType).getTile(id).getImage();
				idWall++;
			}
			else 
			{
				id = idFloor;
				image = tiles.get(tileType).getTile(id).getImage();
				idFloor++;
			}
			tile = new MapTile(image, passable, desc,tileType);
			tile.setId(id);
			listOfTiles.add(tile);
		}
		for(int i=0;i<listOfTiles.size();i++)
		{
			listOfTiles.getObject(i).setId(listOfTiles.size() - i - 1);
		}
	}
	/**
	 *  £aduje mapê z pliku do pamiêci
	 */
	public void newLoadMap()
	{
		File file = new File("MapFiles//Map//ElkiaNew.mp");
		Scanner input = null; 
		try 
		{
			input = new Scanner(file);
		} 
		catch (FileNotFoundException e) 
		{
			
		}
		String inputline;
		map = new MapObjectList[mapWidth][mapHeight];
		int found = 0;
		int elementCount = 0;
		for(int i=0;i<mapHeight;i++)
		{
			inputline = input.nextLine();
			char[] inputtable = inputline.toCharArray();
			int ite = 0;
			MapObjectList prev = null;
			for(int j=0;j<mapWidth;j++)
			{
				elementCount++;
				int howMany = 1;
				int prob = ite;
				while(inputtable[prob]!='.')
				{
					if(inputtable[prob]==';')
					{
						howMany++;
					}
					prob++;
				}
				map[j][i] = new MapObjectList();
				for(int kafel = 0;kafel<howMany;kafel++)
				{
					String value = "";
					while(inputtable[ite]>='0'&&inputtable[ite]<='9')
					{
						try
						{
							value+=inputtable[ite++];
						}
						catch (Exception e)
						{
							
						}
					}
					found++;
					map[j][i].add(listOfTiles.getObject(listOfTiles.size() - 1 - Integer.valueOf(value)));
					ite++;
				}
				if(prev!=null)
				{
					prev.setNextList(map[j][i]);
					map[j][i].setPrevList(prev);
				}
				prev = map[j][i];
			}
		}
	}
	
	public static void saveMap()
	{
		drawingChanges.lock();
		Position position = GameBoard.hero.getPosition();
		String input;
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("MapFiles//Map//ElkiaNew.mp", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int ky=0;ky<600;ky++)
		{
			input = "";
			for(int kx=0;kx<600;kx++)
			{
				if(kx>100|ky>100)
				{
					input+="0.";
				}
				else
				{
					System.out.println(kx + " " + ky);
					input+=map[kx][ky].toSave();
				}
			}
			writer.println(input);
		}
		System.out.println("Zapisane");
		map[position.getXPosition()][position.getYPosition()].add(GameBoard.hero);
		drawingChanges.unlock();
	}
	public static void removeLastAdded(int x, int y)
	{
		drawingChanges.lock();
		map[x][y].removeLastAdded();
		drawingChanges.unlock();
	}
	public static void removeSecond(int x, int y)
	{
		drawingChanges.lock();
		map[x][y].removeSecond();
		drawingChanges.unlock();
	}
	
	public static void setXOffset(float value)
	{
		offsetLock.lock();
		xOffset = value;
		offsetLock.unlock();
	}
	
	public static void setXYOffset(double d, double e)
	{
		offsetLock.lock();
		xOffset = d;
		yOffset = e;
		offsetLock.unlock();
	}
	
	public static void setYOffset(float value)
	{
		offsetLock.lock();
		yOffset = value;
		offsetLock.unlock();
	}
	
	public static int getXOffset()
	{
		int value;
		offsetLock.lock();
		value = (int)xOffset;
		return value;
	}
	
	public static int getYOffset()
	{
		int value;
		value = (int)yOffset;
		offsetLock.unlock();
		return value;
	}
	
	/**
	 * 
	 * @param xOffset Pozycja x na mapie od którego ma wyœwietlaæ 
	 * @param yOffset Pozycja y na mapie od którego ma wyœwietlaæ
	 * @param g Graphics na którym rysuje mapê
	 */
	public synchronized void draw(int xOffset, int yOffset, Graphics2D g)
	{
		drawingChanges.lock();
		int heroXOffset = getXOffset();
		int heroYOffset = getYOffset();
		for(int x=-2;x<=21;x++)
		{
			for(int y=-1;y<=15;y++)
			{
				try
				{
					map[x+xOffset][y+yOffset].drawNotTall(x*64+heroXOffset, y*64+heroYOffset, g);
				}
				catch (IndexOutOfBoundsException e)
				{
					
				}
			}
		}
		for(int x=-2;x<=21;x++)
		{
			
			try
			{
				map[xOffset-1][x+yOffset].drawTall((-1)*64+heroXOffset, x*64+heroYOffset, g,24);
			}
			catch (IndexOutOfBoundsException e)
			{
				
			}
		}
		for(int x=-2;x<=21;x++)
		{
			for(int y=-1;y<=15;y++)
			{
				try
				{
					map[x+xOffset][y+yOffset].drawChat(x*64+heroXOffset, y*64+heroYOffset, g);
				}
				catch (IndexOutOfBoundsException e)
				{
					
				}
			}
		}
		drawingChanges.unlock();
	}
	
	public static void addElementToMap(MainObject element, Position position)
	{
		drawingChanges.lock();
		mapListLock.lock();
		map[position.getXPosition()][position.getYPosition()].add(element);
		mapListLock.unlock();
		drawingChanges.unlock();
	}
	
	public static void deleteElementFromMap(MainObject element, Position position)
	{
		drawingChanges.lock();
		mapListLock.lock();
		map[position.getXPosition()][position.getYPosition()].deleteObject(element);
		mapListLock.unlock();
		drawingChanges.unlock();
	}
	
	public static void checkPassable(int x, int y)
	{
		if(!map[x][y].checkIfAnyPassable())
		{
			throw new CannotWalkException();
		}
	}
	
	public void deleteTerrainFromMap(MouseEvent e)
	{
		Component el = null;
		for(Component element : getComponents())
		{
			if(e.getX() >element.getX() && e.getX()<element.getX()+64 && e.getY() < element.getY()+64 & e.getY() > element.getY())
			{
				el = element;
			}
		}
		if(el instanceof MapTileListener)
		{
			Position position;
			position = ((MapTileListener) el).getPosition();
			int x = position.getXPosition() + GameBoard.xOffset;
			int y = position.getYPosition() + GameBoard.yOffset;
			if(x!=lastXModified || y!= lastYModified)
			{
				lastXModified = x;
				lastYModified = y;
				Map.map[x][y].removeLastAdded();
			}
		}
	}
	
	public void addTerrainToMap(MainObject object, MouseEvent e)
	{
		Component el = null;
		for(Component element : getComponents())
		{
			if(e.getX() >element.getX() && e.getX()<element.getX()+64 && e.getY() < element.getY()+64 & e.getY() > element.getY())
			{
				el = element;
			}
		}
		if(el instanceof MapTileListener)
		{
			Position position;
			position = ((MapTileListener) el).getPosition();
			int x = position.getXPosition() + GameBoard.xOffset;
			int y = position.getYPosition() + GameBoard.yOffset;
			System.out.println(x + " " + lastXModified);
			if(x!=lastXModified || y!=lastYModified)
			{
				lastXModified = x;
				lastYModified = y;
				Map.map[x][y].add(object);
			}
		}
	}
	
	public MainObject getObject(MouseEvent e)
	{
		Component element = getComponentAt(e.getPoint());
		if(element instanceof MapTileListener)
		{
			Position position;
			position = ((MapTileListener) element).getPosition();
			int x = position.getXPosition() + GameBoard.xOffset;
			int y = position.getYPosition() + GameBoard.yOffset;
			MainObject object = map[x][y].getAlive();
			if(object == null) return map[x][y].getItem();
			else return object;
		}
		else return null;
	}
	
	public void mousePressed(MouseEvent e)
	{
		if(SwingUtilities.isLeftMouseButton(e))
		{
			Component element = getComponentAt(e.getPoint());
			if(element instanceof MapTileListener)
			{
				Position position;
				position = ((MapTileListener) element).getPosition();
				int x = position.getXPosition() + GameBoard.xOffset;
				int y = position.getYPosition() + GameBoard.yOffset;
				MainObject el = map[x][y].getItem();
				if(el instanceof Item)
				{
					GameBoard.itemOnMouse = el;
				}
			}
		}
		else if(SwingUtilities.isRightMouseButton(e))
		{
			Component element = getComponentAt(e.getPoint());
			if(element instanceof MapTileListener)
			{
				Position position;
				position = ((MapTileListener) element).getPosition();
				int x = position.getXPosition() + GameBoard.xOffset;
				int y = position.getYPosition() + GameBoard.yOffset;
				MainObject el = null;
				el = map[x][y].getAlive();
				if(el instanceof Mob)
				{
					GameBoard.hero.setFocus((Mob)el);
				}
				else
				{
					el = map[x][y].getItem();
					if(el instanceof Openable)
					{
						((Openable) el).open();
					}
				}
			}
		}
	}
			
	
	public void mouseReleased(MouseEvent e)
	{
		Component element = getComponentAt(e.getPoint());
		if(element instanceof MapTileListener)
		{
			if(SwingUtilities.isLeftMouseButton(e))
			{
				if(GameBoard.itemOnMouse!=null)
				{
					if(GameBoard.itemOnMouse instanceof Item)
					{
						Position position = ((MapTileListener) element).getPosition();
						int x = position.getXPosition() + GameBoard.xOffset;
						int y = position.getYPosition() + GameBoard.yOffset;
						Item item = (Item) GameBoard.itemOnMouse;
						item.addMeToMap(new Position(x,y));
					}
				}
			}
		}
	}
}
