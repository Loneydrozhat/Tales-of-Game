package objects.notAlive.items.itemTypes;

import objects.notAlive.items.Item;

public class Boots extends Item {

	public Boots()
	{
		
	}
	@Override
	public boolean checkInstance(Item item) {
		return item instanceof Boots;
	}
}
