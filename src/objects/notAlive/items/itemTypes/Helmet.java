package objects.notAlive.items.itemTypes;

import java.awt.image.BufferedImage;

import objects.notAlive.items.Item;

public class Helmet extends Item{

	public Helmet()
	{
		
	}
	@Override
	public boolean checkInstance(Item item) {
		return item instanceof Helmet;
	}
}
