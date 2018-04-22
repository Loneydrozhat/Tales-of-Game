package objects.notAlive.items.itemTypes;

import objects.notAlive.items.Item;

public class Armor extends Item{

	public Armor()
	{
		
	}
	@Override
	public boolean checkInstance(Item item) {
		return item instanceof Armor;
	}
}
