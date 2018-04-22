package objects.notAlive.items.itemTypes;

import objects.notAlive.items.Item;

public class Legs extends Item{

	public Legs()
	{
		
	}
	@Override
	public boolean checkInstance(Item item) {
		return item instanceof Legs;
	}
}
