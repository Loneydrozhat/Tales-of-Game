package threads;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import objects.alive.Alive;
import timers.ThreadTimer;
import timers.Timer;
import timers.textTimer.NormalTimer;
import utilities.Position;

public class AttackManagement implements Runnable{


	private Alive toAttack;
	private Alive fromAttack;
	private double attackSpeed;
	private int minAttack;
	private int maxAttack;
	public AttackManagement(Alive fromAttack, Alive toAttack, double attackSpeed, int minAttack, int maxAttack)
	{
		this.fromAttack = fromAttack;
		this.toAttack = toAttack;
		this.attackSpeed = attackSpeed;
		this.minAttack = minAttack;
		this.maxAttack = maxAttack;
		System.out.println(fromAttack + " attacking " + toAttack);
		new Thread(this,fromAttack + " attacking " + toAttack).start();
	}
	
	@Override
	public void run() 
	{
		Position attackingPosition = fromAttack.getPosition();
		Position attackedPosition = toAttack.getPosition();
		int distance = attackedPosition.distance(attackingPosition);
		if(toAttack.hpManagement!=null)
		{
			if(distance<=1)
			{
				Random generator = new Random();
				toAttack.hpManagement.attack(generator.nextInt(maxAttack - minAttack+1)+minAttack);
				new NormalTimer((long) attackSpeed,fromAttack,Timer.ATTACKING_ENDED_SUCCESSFULLY);
			}
			else fromAttack.timerEnded(Timer.ATTACKING_ENDED_UNSECCESSFULLY);
		}
		else
		{
			fromAttack.timerEnded(Timer.ATTACKING_ENDED_UNSECCESSFULLY);
		}
		
	}

}
