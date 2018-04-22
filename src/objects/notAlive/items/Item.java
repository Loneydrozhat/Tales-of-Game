package objects.notAlive.items;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import exceptions.CannotMoveItemException;
import exceptions.ReputItemException;
import graphics.Map;
import graphics.panels.statusPanels.ItemSlotPanel;
import objects.notAlive.NotAlive;
import objects.notAlive.items.itemTypes.Weapon;
import utilities.Position;

public class Item extends NotAlive implements Cloneable
{
	public final int map = 1;
	public final int slot = 2;
	public final int nowhere = 0;
	private static int ite=0;
	public static final int HELMET = ite++; //2
	public static final int AMULET = ite++; //3
	public static final int BACKPACK = ite++; //4
 	public static final int ONE_HANDED_SWORD = ite++; //5
	public static final int TWO_HANDED_SWORD = ite++; //6
	public static final int SHIELD = ite++; //7
	public static final int ARMOR = ite++; //8
	public static final int RING = ite++; //9
	public static final int LEGS = ite++; //10
	public static final int AMMO = ite++; //11
	public static final int BOOTS = ite++; //12
	private int whereAmI;
	private ItemSlotPanel slotIAmOn;
	private int itemType;
	private boolean equipable;
	private String params;
	public Item()
	{
		super();
	}
	public Item (BufferedImage image, String desc, String params) {
		super(image);
		this.params = params;
		this.desc = desc;
		this.whereAmI = nowhere;
		this.passable = true;
		// TODO Auto-generated constructor stub
	}
	
	public Item (BufferedImage image, String desc, int itemType) {
		super(image);
		this.desc = desc;
		this.whereAmI = nowhere;
		this.passable = true;
		this.itemType = itemType;
		// TODO Auto-generated constructor stub
	}

	
	public void setParams(int param)
	{
		System.out.println("Setting params");
	}
	
	private void removeMeFromMap()
	{
		Map.deleteElementFromMap(this, position);
	}
	
	private void removeMeFromSlot()
	{
		slotIAmOn.removeItem();
	}
	
	private void removeMe()
	{
		if(whereAmI == map)
		{
			removeMeFromMap();
		}
		else if(whereAmI == slot)
		{
			removeMeFromSlot();
		}
		else if(whereAmI == nowhere)
		{
			
		}
	}
	
	public void addMeToMap(Position newLocation)
	{
		removeMe();
		Map.addElementToMap(this, newLocation);
		position = newLocation;
		whereAmI = map;
	}
			
	public void addMeToSlot(ItemSlotPanel intoSlot)
	{
		try 
		{
			intoSlot.putItem(this);
			removeMe();
			slotIAmOn = intoSlot;
			whereAmI = slot;
		}
		catch (CannotMoveItemException e)
		{
		}
		catch (ReputItemException e)
		{
			
		}
	}
	public boolean checkInstance(Item item)
	{
		return item instanceof Item;
	}
	
	public Item clone()
	{
		Class klasa = this.getClass();
		Constructor cons = null;
		Class args[] = new Class[3];
		args[0] = BufferedImage.class;
		args[1] = String.class;
		args[2] = String.class;
		try {
			cons = klasa.getDeclaredConstructor(args);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Item item = null;
		try {
			item = (Item) cons.newInstance(image,desc,params);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return item;
	}
}
