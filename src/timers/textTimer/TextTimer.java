package timers.textTimer;

import objects.MainObject;
import timers.Timer;

public class TextTimer extends Timer{

	private MainObject toNotice;
	private int index;
	public TextTimer(long waitTime, MainObject toNotice, int index)
	{
		super();
		this.waitTime = waitTime;
		this.toNotice = toNotice;
		this.index = index;
		new Thread(this,"Timer").start();
	}
	@Override
	protected void ended() 
	{
		toNotice.timerEnded(index);
	}

}
