package objects;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;

import graphics.Map;
import objects.alive.Alive;
import objects.alive.Hero;
import objects.notAlive.textObjects.MobText;
import utilities.Position;

public abstract class MainObject {
	public Lock lockOffset = new ReentrantLock();
	private Lock lockPosition = new ReentrantLock();
	protected double xOffset = 0;
	protected double yOffset = 0;
	protected Position position;
	protected BufferedImage image;
	public String desc;
	protected int iconHeight;
	protected int iconWidth;
	protected double scale=2;
	protected int id;
	protected char code;
	protected boolean isTall=false;
	protected int drawOrder;
	protected boolean passable;
	/*
	public MainObject(String iconSrc)
	{
		position = new Position(10,10);
		try {
			if(iconSrc!="")
			{
				this.image = ImageIO.read(getClass().getResourceAsStream(iconSrc));
				this.iconHeight = image.getHeight();
				this.iconWidth = image.getWidth();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		drawOrder = 0;
	}
	*/
	public MainObject()
	{
		
	}
	public MainObject(BufferedImage image)
	{
		position = new Position(10,10);
		this.image = image;
		this.iconHeight = image.getHeight();
		this.iconWidth = image.getWidth();
		drawOrder = 0;
	}
	
	public void setXOffset(double value)
	{
		xOffset = value;
	}
	public void setYOffset(double value)
	{
		yOffset = value;
	}
	public double getXOffset()
	{
		double value;
		value = xOffset;
		return value;
	}
	public double getYOffset()
	{
		double value;
		value = yOffset;
		return value;
	}
	
	public void draw(int x, int y, Graphics2D g)
	{
		Map.drawingChanges.lock();
		lockOffset.lock();
		g.drawImage(image,x-(int)(iconWidth*scale)+64+(int)getXOffset(),y-(int)(iconHeight*scale)+64+(int)getYOffset(),(int)(iconWidth*scale),(int)(iconHeight*scale),null);
		lockOffset.unlock();
		Map.drawingChanges.unlock();
	}
	public BufferedImage getImage()
	{
		return this.image;
	}
	public String toString()
	{
		return this.desc;
	}
	public String getDesc()
	{
		return this.desc;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getId()
	{
		return this.id;
	}
	public char getCode()
	{
		return this.code;
	}
	public void setCode(char code)
	{
		this.code=code;
	}
	
	public Position getPosition()
	{
		lockPosition.lock();
		Position clone = position.clone();
		lockPosition.unlock();
		return clone;
	}
	
	public void setPosition(Position position)
	{
		lockPosition.lock();
		this.position = position;
		lockPosition.unlock();
	}
	
	
	public void timerEnded(int index)
	{
		
	}
	
	public void receiveText(String text, MainObject thisObject)
	{
		
	}
	
	public void newArrival(MainObject thisObject)
	{

	}
	
	public void inRange(MainObject thisObject)
	{
		
	}
	
	
	public int getDrawOrder()
	{
		return this.drawOrder;
	}
	public boolean getPassable() {
		// TODO Auto-generated method stub
		return this.passable;
	}
	public boolean isTall()
	{
		return this.isTall;
	}
	public boolean isMoving()
	{
		return false;
	}
	
}

