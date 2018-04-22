package objects.notAlive.items.itemTypes;

import objects.notAlive.items.Item;

public class Ring extends Item {

	public Ring()
	{
		
	}
	@Override
	public boolean checkInstance(Item item) {
		return item instanceof Ring;
	}
}
