package objects.notAlive.items.itemTypes;

import objects.notAlive.items.Item;

public class Amulet extends Item{
	
	public Amulet()
	{
		
	}
	@Override
	public boolean checkInstance(Item item) {
		return item instanceof Amulet;
	}
}
