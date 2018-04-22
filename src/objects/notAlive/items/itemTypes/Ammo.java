package objects.notAlive.items.itemTypes;

import objects.notAlive.items.Item;

public class Ammo extends Item{

	public Ammo()
	{
		
	}

	@Override
	public boolean checkInstance(Item item) {
		if(item instanceof Ammo) System.out.println(item + "Jest");
		else System.out.println(item + "Nie jest");
		return false;
	}
}
