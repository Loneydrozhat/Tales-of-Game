package objects.notAlive.items.itemTypes;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import objects.notAlive.items.Item;

public class Weapon extends Item {

	public Weapon()
	{
		
	}

	public Weapon(BufferedImage image, String string, String params) 
	{
		super(image,string,params);
	}

	@Override
	public boolean checkInstance(Item item) {
		return item instanceof Weapon;
	}
}
