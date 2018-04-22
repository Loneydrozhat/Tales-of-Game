package graphics.panels.statusPanels;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import abilities.Openable;
import boards.GameBoard;
import exceptions.CannotMoveItemException;
import exceptions.ReputItemException;
import objects.MainObject;
import objects.notAlive.items.Item;
import objects.notAlive.items.itemTypes.*;

public class ItemSlotPanel extends JPanel 
{
	public static final Helmet SLOT_HELMET = new Helmet();
	public static final Amulet SLOT_AMULET = new Amulet();
	public static final Bag SLOT_BACKPACK = new Bag();
	public static final Weapon SLOT_LEFTHAND = new Weapon();
	public static final Armor SLOT_ARMOR = new Armor();
	public static final Shield SLOT_RIGHTHAND = new Shield();
	public static final Ring SLOT_RING = new Ring();
	public static final Legs SLOT_LEGS = new Legs();
	public static final Ammo SLOT_AMMO = new Ammo();
	public static final Boots SLOT_BOOTS = new Boots();
	public static final Item SLOT_EVERYTHING = new Item();
	private Item slotType = null;
	private boolean hasItem;
	private Item itemInSlot;
	private BufferedImage bcg = null;
	private boolean drawBcg;
	private Item slotOwner;
	private int locationX, locationY;
	
	public ItemSlotPanel(int x, int y, Item type, Item slotOwner)
	{
		super(null);
		setBounds(x,y,32,32);
		setOpaque(false);
		locationX = x;
		locationY = y;
		slotType = type;
		hasItem = false;
		drawBcg = true;
		this.slotOwner = slotOwner;
		if(bcg==null)
		{
			try {
				bcg = ImageIO.read(getClass().getResourceAsStream("/ItemBcg.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setDrawBcg(boolean value)
	{
		drawBcg = value;
	}
	
	public boolean hasItem()
	{
		return hasItem;
	}
	
	public void setLocation(int x, int y)
	{
		locationX = x;
		locationY = y;
	}
	
	public int getX()
	{
		return locationX;
	}
	
	public int getY()
	{
		return locationY;
	}
	
	public void putItem(Item item)
	{

		if(item == slotOwner) throw new CannotMoveItemException();
		else if(itemInSlot instanceof Openable)
		{
			((Openable) itemInSlot).addItemToMe(item);
			
		}
		else if(slotType.checkInstance(item))
		{
			if(hasItem == true) 
			{
				throw new CannotMoveItemException();
			}
			else
			{
				itemInSlot = item;
				hasItem = true;
			}
		}
		else throw new CannotMoveItemException();
	}
	
	public Item getItem()
	{
		return itemInSlot;
	}
	
	public void removeItem()
	{
		hasItem = false;
		itemInSlot = null;
	}
	
	public void drawItem(Graphics2D g, int yOffset)
	{
		if(drawBcg)g.drawImage(bcg,locationX,locationY+yOffset,32,32,null);
		else if(hasItem) g.drawImage(bcg,locationX,locationY+yOffset,32,32,null);
		if(hasItem)
		{
			g.drawImage(itemInSlot.getImage(),locationX,locationY+yOffset,32,32,null);
		}
		else
		{
			
		}
	}
	
	public void mousePress(MouseEvent e)
	{
		
	}
	
	public void mouseRelease(MouseEvent e)
	{
		
	}
	
}
