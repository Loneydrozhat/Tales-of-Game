package timers;

public class ThreadTimer implements Runnable
{

	private long time;
	private Thread thread;
	public ThreadTimer(long time, Thread thread)
	{
		this.time = time;
		this.thread = thread;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized(thread)
		{
			thread.notify();
		}
		// TODO Auto-generated method stub
		
	}
}
