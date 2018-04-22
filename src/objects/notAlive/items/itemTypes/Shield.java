package objects.notAlive.items.itemTypes;

import objects.notAlive.items.Item;

public class Shield extends Item{

	public Shield()
	{
		
	}
	@Override
	public boolean checkInstance(Item item) {
		return item instanceof Shield;
	}
}
