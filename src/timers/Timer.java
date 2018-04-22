package timers;

public abstract class Timer implements Runnable 
{

	public static final int MOVING_ENDED = 10;
	public static final int MOVING_STEP_ENDED = 9;
	public static final int ATTACKING_ENDED_SUCCESSFULLY = 11;
	public static final int ATTACKING_ENDED_UNSECCESSFULLY = 12;
	public static final int FEEDING_INTERVAL = 13;
	protected long waitTime;
	protected Timer()
	{
		waitTime = 1500;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.ended();
	}
	
	protected abstract void ended();

}
