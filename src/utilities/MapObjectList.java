package utilities;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import objects.MainObject;
import objects.alive.Alive;
import objects.notAlive.items.Item;
import objects.notAlive.mapObjects.FloorTile;
import objects.notAlive.mapObjects.MapTile;

public class MapObjectList {

	private class ListElement
	{
		MainObject element;
		ListElement left, right;
		public void setLeft(ListElement element)
		{
			this.left = element;
		}
		public ListElement getLeft()
		{
			return this.left;
		}
		public ListElement getRight()
		{
			return this.right;
		}
		public MainObject getObject()
		{
			return this.element;
		}
		public ListElementValue add(MainObject element)
		{
			this.left = new ListElementValue(element);
			this.left.right = this;
			return (ListElementValue) this.left;
		}
	}
	private class ListElementValue extends ListElement
	{
		ListElementValue(MainObject element)
		{
			this.element = element;
			this.left = null;
		}
		public String toString()
		{
			return this.element.toString();
		}
	}
	private ListElement begginig;
	private ListElementValue lastValue;
	private MapObjectList nextList;
	private MapObjectList prevList;
	private MapObjectList toWakeUp;
	private boolean drawRightFirst;
	private int xPosition, yPosition;
	private boolean toWait;
	private boolean ifWakeUp;
	private Lock waitLock = new ReentrantLock();
	private Lock changingList = new ReentrantLock();
	private int leftToDraw=0;
	private ArrayList <MainObject> list;
	private boolean hasMoving;
	private boolean drawLeftFirst;
	private boolean drawAlive;
	private MapObjectList drawAliveFirst;
	private ArrayList <Item> itemList;
	public MapObjectList()
	{
		begginig = new ListElement();
		lastValue = null;
		nextList = null;
		toWait = false;
		list = null;
		hasMoving = false;
		drawLeftFirst = false;
		itemList = new ArrayList<Item>();
	}
	public void add(MainObject element)
	{
		changingList.lock();
		if(list==null)
		{
			list = new ArrayList<MainObject>();
		}
		list.add(element);
		if(element instanceof Item)
		{
			itemList.add((Item) element);
		}
		changingList.unlock();
	}
	
	public void setNextList(MapObjectList next)
	{
		waitLock.lock();
		this.nextList = next;
		waitLock.unlock();
	}
	
	public void setPrevList(MapObjectList prev)
	{
		waitLock.lock();
		this.prevList = prev;
		waitLock.unlock();
	}
	
	public void setToWait(boolean toWait)
	{
		this.toWait = toWait;
	}
	
	public void setToWakeUp(boolean ifWakeUp, MapObjectList toWakeUp)
	{
		this.ifWakeUp = ifWakeUp;
		this.toWakeUp = toWakeUp;
	}
	
	public void setToDrawAlive(boolean ifWakeUp, MapObjectList toDrawAlive)
	{
		this.drawAlive = ifWakeUp;
		this.drawAliveFirst = toDrawAlive;
	}
	
	public void drawRightFirst(boolean value)
	{
		drawRightFirst = value;
	}
	
	public void setDrawLeftFirst(boolean value)
	{
		drawLeftFirst = value;
	}
	
	public void wakeUp(Graphics2D g)
	{
		toWait = !toWait;
		this.drawTall(xPosition,  yPosition, g, leftToDraw-1);
		toWait = !toWait;
	}
	
	public void setHasMoving(boolean value)
	{
		this.hasMoving = value;
	}
	
	public int size()
	{
		changingList.lock();
		int value = list.size();
		changingList.unlock();
		return value;
	}
	
	public MainObject getObject(int indeks)
	{
		if(indeks>=list.size()) return null;
		changingList.lock();
		MainObject element = list.get(list.size()-indeks-1);
		changingList.unlock();
		return element;
	}
	public void removeSecond()
	{
		if(list.size()>1)
		{
			list.remove(list.size()-2);
		}
	}
	/*
	public MainObject getMoveable()
	{
		for(int j=0;j<list.size();j++)
		{
			MainObject element = list.get(j);
			if(element instanceof Moveable)
			{
				return element;
			}
		}
		return null;
	}
	*/
	public MainObject getLastAddedObject()
	{
		return getObject(list.size()-1);
	}
	
	public void removeLastAdded()
	{
		changingList.lock();
		if(list.size()>0)
		{
			list.remove(list.size()-1);
		}
		changingList.unlock();
	}
	
	public void draw(int x, int y, Graphics2D g)
	{
		if(list.size()>0)
		{
			for(int j=0;j<list.size();j++)
			{
				list.get(j).draw(x, y, g);
			}
		}
	}
	
	private MapObjectList drawRightAndBack(int x, int y, Graphics2D g, int counter)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.leftToDraw = counter;
		if(!toWait)
		{
			if(drawRightFirst)
			{
				nextList.drawTall(x+64, y, g, leftToDraw-1);
			}
			if(list.size()>0)
			{
				for(int j=0;j<list.size();j++)
				{
					MainObject element = list.get(j);
					if(element.getDrawOrder()==0)
					{
						if(element.isTall())
						{
							list.get(j).draw(x, y, g);
						}
					}
				}
			}
			if(ifWakeUp)
			{
				toWakeUp.wakeUp(g);
			}
		}
		return this.nextList;
	}
	
	private void drawPrevTall(int x, int y, Graphics2D g, int counter)
	{
		this.leftToDraw = counter;
		if(!toWait)
		{
			if(list.size()>0)
			{
				for(int j=0;j<list.size();j++)
				{
					MainObject element = list.get(j);
					if(element.getDrawOrder()==0)
					{
						if(element.isTall())
						{
							element.draw(x, y, g);
						}
					}
				}
			}
		}
	}
	
	public void drawTall(int x, int y, Graphics2D g, int counter)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.leftToDraw = counter;
		if(!toWait)
		{
			MapObjectList next = nextList;
			if(drawRightFirst)
			{
				next = nextList.drawRightAndBack(x+64, y, g, leftToDraw-1);
			}
			if(list.size()>0)
			{
				if(hasMoving)
				{
					ArrayList <MainObject> drawLater = new ArrayList <MainObject>();
					for(int i=0;i<list.size();i++)
					{
						MainObject element = list.get(i);
						if(element.isTall())
						{
							if(element instanceof Alive)
							{
								element.draw(x, y, g);
							}
							else drawLater.add(element);
						}
					}
					if(drawLeftFirst) 
					{
						prevList.drawPrevTall(x-64, y, g, counter+1);
					}
					for(int i=0;i<drawLater.size();i++)
					{
						drawLater.get(i).draw(x, y, g);
					}
				}
				else
				{
					for(int j=0;j<list.size();j++)
					{
						MainObject element = list.get(j);
						if(element.getDrawOrder()==0)
						{
							if(element.isTall())
							{
								list.get(j).draw(x, y, g);
							}
						}
					}
				}
			}
			if(ifWakeUp)
			{
				toWakeUp.wakeUp(g);
			}
			if(this.drawAlive)
			{
				this.drawAliveFirst.drawAlive(x,y-64,g);
			}
			if(next!=null)
			{
				if(counter>0)
				{
					if(drawRightFirst)
					{
						next.drawTall(x+128, y, g, leftToDraw-1);
					}
					else
					{
						next.drawTall(x+64,y,g, leftToDraw-1);
					}
				}
			}
		}
	}
	
	public void drawAlive(int x, int y, Graphics2D g)
	{
	for(int j=0;j<list.size();j++)
		{
			MainObject element = list.get(j);
			if(element instanceof Alive)
			{
				element.draw(x, y, g);
			}
		}
	}
	
	public void drawNotTall(int x, int y, Graphics2D g)
	{
		if(list.size()>0)
		{
			for(int j=0;j<list.size();j++)
			{
				MainObject element = list.get(j);
				if(element.getDrawOrder()==0)
				{
					if(!element.isTall())
					{
						list.get(j).draw(x, y, g);
					}
				}
			}
		}
	}
	
	public boolean checkIfAnyPassable()
	{
		MainObject element = null;
		for(int j = 0;j<list.size();j++)
		{
			element = list.get(j);
			if(!element.getPassable())
			{
				return false;
			}
		}
		return true;
	}
	
	public void deleteObject(MainObject thisObject)
	{
		boolean done = false;
		changingList.lock();
		if(list.size()>0)
		{
			for(int j=0;j<list.size();j++)
			{
				MainObject element = list.get(j);
				if(element==thisObject)
				{
					list.remove(j);
				}
			}
		}
		if(thisObject instanceof Item)
		{
			for(int j=0;j<itemList.size();j++)
			{
				MainObject element = itemList.get(j);
				if(element==thisObject)
				{
					itemList.remove(j);
				}
			}
		}
		changingList.unlock();
	}
	
	public void drawChat(int x, int y, Graphics2D g)
	{
		changingList.lock();
		if(list.size()>0)
		{
			for(int j=0;j<list.size();j++)
			{
				MainObject element = list.get(j);
				if(element.getDrawOrder()==1)
				{
					if(!element.isTall())
					{
						element.draw(x, y, g);
					}
				}
			}
		}
		changingList.unlock();
	}
	
	public void sendText(String text, MainObject thisObject)
	{
		changingList.lock();
		MainObject element = null;
		for(int j = 0; j<list.size();j++)
		{
			list.get(j).receiveText(text, thisObject);
		}
		changingList.unlock();
	}
	
	public void noticeArrival(MainObject thisObject)
	{
		changingList.lock();
		MainObject element = null;
		for(int j = 0; j<list.size();j++)
		{
			element = list.get(j);
			element.newArrival(thisObject);
		}
		changingList.unlock();
	}
	
	public String toString()
	{
		String output=new String("");
		for(int i=0;i<list.size();i++)
		{
			MainObject element = list.get(i);
			output+=","+element;
		}
		return output;
	}
	
	public MainObject getItem()
	{
		if(itemList.size()>0)
		{
			return itemList.get(itemList.size()-1);
		}
		else return null;
	}
	
	public MainObject getAlive()
	{
		for(int i=0;i<list.size();i++)
		{
			MainObject object = list.get(i);
			System.out.println(object);
			if(object instanceof Alive)
			{
				return object;
			}
		}
		return null;
	}
	
	public String toSave()
	{
		String output = "";
		MainObject element = null;
		for(int j=0;j<list.size();j++)
		{
			element = list.get(j);
			if(element instanceof Alive)
			{
				
			}
			else
			{ 
				if(element instanceof MapTile)
				{
					if(j!=0)output+=";";
					output+=element.getId();
				}
			}
		}
		if(list.size()==0)output+='0';
		output+=".";
		return output;
	}
}
