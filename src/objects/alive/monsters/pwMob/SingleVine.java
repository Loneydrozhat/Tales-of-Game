package objects.alive.monsters.pwMob;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import boards.GameBoard;
import exceptions.CannotWalkException;
import graphics.Map;
import objects.alive.Alive;
import threads.WalkManagement;
import timers.Timer;
import utilities.Position;
import utilities.Vector;

public class SingleVine extends Alive {

	private Vine supervisor;
	private Semaphore lockPreviousMove,lockNextMove;
	private static BufferedImage[] turned;
	private static BufferedImage[][] walkAnimation;
	private long timeLast;
	private static long changeTime = 500;
	public SingleVine(BufferedImage image, Position position, Vine supervisor, Semaphore one, Semaphore two) {
		super(image);
		this.position = position;
		walkTime = 150;
		this.desc = "";
		this.isTall = true;
		this.supervisor = supervisor;
		this.passable = true;
		scale = 2.5;
		lockPreviousMove = one;
		lockNextMove = two;
		turned = new BufferedImage[4];
		for(int i=0;i<4;i++)
		{
			turned[i]=GameBoard.mobs.get(0*12+i*3);
		}
		walkAnimation = new BufferedImage[4][2];
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<2;j++)
			{
				walkAnimation[i][j]=GameBoard.mobs.get(0*12+i*3+j+1);
			}
		}
		timeLast = System.currentTimeMillis();
		Map.addElementToMap(this, position.clone());
	}
	
	private void take()
	{
		try {
			lockPreviousMove.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void give()
	{
		lockNextMove.release();
	}
	
	public void timerEnded(int index)
	{
		take();
		if(index==Timer.MOVING_STEP_ENDED)
		{
			lockOffset.lock();
			Vector vector = vectorList.get(0).clone();
			setXOffset(getXOffset()-vector.getXVector()*64);
			setYOffset(getYOffset()-vector.getYVector()*64);
			if(System.currentTimeMillis() - timeLast>changeTime)
			{
				image = walkAnimation[vector.direction()][animationFrame];
				animationFrame++;
				animationFrame%=2;
				timeLast = System.currentTimeMillis();
			}
			lockOffset.unlock();
		}
		else if(index==Timer.MOVING_ENDED)
		{
			lockOffset.lock();
			setXOffset(0);
			setYOffset(0);
			Vector vector = vectorList.get(0).clone();
			image = turned[vector.direction()];
			vectorList.remove(0);
			lockOffset.unlock();
			supervisor.notice();
		}
		give();
	}
	
	public void addVector(Vector vector)
	{
		changingVector.lock();
		vectorList.add(vector);
		changingVector.unlock();
	}
	
	public void move()
	{
		changingVector.lock();
		Vector vector = vectorList.get(0).clone();
		image = turned[vector.direction()];
		setXOffset(getXOffset()-vector.getXVector()*64);
		setYOffset(getYOffset()-vector.getYVector()*64);
		new WalkManagement(this,walkTime,vectorList.get(0));
		changingVector.unlock();
	}

}
