package threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import objects.alive.Alive;

public class HpManagement{

	private Alive object;
	public Lock waitLock = new ReentrantLock();
	
	public HpManagement(Alive object)
	{
		this.object = object;
	}
	
	public void attack(int hp)
	{
		System.out.println(object + " Losing Life");
		object.reduceLife(hp);
	}

}
