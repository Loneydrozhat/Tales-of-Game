package objects.alive.monsters.pwMob;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import graphics.Map;
import utilities.Position;
import utilities.Vector;

public class Vine implements Runnable {

	protected ArrayList <Vector> vectorForwardList = new ArrayList <Vector>();
	protected ArrayList <Vector> vectorBackwardList = new ArrayList <Vector>();
	private ArrayList <SingleVine> vineList = new ArrayList <SingleVine>();
	private int howMany = 0;
	private int iterator = 0;
	private int listIterator = 0;
	private int walkTime = 0;
	private int type;
	private Lock lock = new ReentrantLock();
	private static Semaphore lockCritOne = new Semaphore(1);
	private static Semaphore lockCritTwo = new Semaphore(1);
	private static Semaphore lockCritThree = new Semaphore(1);
	private static Semaphore lockCritFour = new Semaphore(1);
	private static Semaphore lockVineOne = new Semaphore(0);
	private static Semaphore lockVineTwo = new Semaphore(0);
	private static Semaphore lockVineThree = new Semaphore(1);
	private Object synchronizer = new Object();
	private boolean forward = true;
	private static boolean waiting[] = {false,false,false,false};
	public Vine(BufferedImage image, Position position, int type) 
	{

		Semaphore lockOne,lockTwo,lockThree;
		lockOne = new Semaphore(1);
		lockTwo = new Semaphore(1);
		lockThree = new Semaphore(1);
		try {
			lockTwo.acquire(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			lockOne.acquire(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.type = type;
		if(type==3)
		{
			for(int i=0;i<7;i++)
			{
				vectorForwardList.add(new Vector(0,-1));
			}
			for(int i=0;i<5;i++)
			{
				vectorForwardList.add(new Vector(-1,0));
			}
			for(int i=0;i<8;i++)
			{
				vectorForwardList.add(new Vector(0,-1));
			}
			for(int i=0;i<4;i++)
			{
				vectorForwardList.add(new Vector(-1,0));
			}
			
			
			for(int i=0;i<2;i++)
			{
				vectorForwardList.add(new Vector(0,0));
			}
			
			
			
			for(int i=0;i<4;i++)
			{
				vectorBackwardList.add(new Vector(1,0));
			}
			
			
			for(int i=0;i<8;i++)
			{
				vectorBackwardList.add(new Vector(0,1));
			}
			for(int i=0;i<5;i++)
			{
				vectorBackwardList.add(new Vector(1,0));
			}
			for(int i=0;i<7;i++)
			{
				vectorBackwardList.add(new Vector(0,1));
			}
			for(int i=0;i<2;i++)
			{
				vectorForwardList.add(new Vector(0,0));
			}
			vineList.add(new SingleVine(image,new Position(position.getXPosition(),position.getYPosition()),this,lockThree,lockOne));
			vineList.add(new SingleVine(image,new Position(position.getXPosition(),position.getYPosition()+1),this,lockOne, lockTwo));
			vineList.add(new SingleVine(image,new Position(position.getXPosition(),position.getYPosition()+2),this,lockTwo,lockThree));
			walkTime = 22;
		}
		else if(type == 2)
		{
			for(int i=0;i<4;i++)
			{
				vectorForwardList.add(new Vector(1,0));
			}
			for(int i=0;i<8;i++)
			{
				vectorForwardList.add(new Vector(0,-1));
			}
			for(int i=0;i<5;i++)
			{
				vectorForwardList.add(new Vector(-1,0));
			}
			for(int i=0;i<7;i++)
			{
				vectorForwardList.add(new Vector(0,-1));
			}
			
			
			for(int i=0;i<2;i++)
			{
				vectorForwardList.add(new Vector(0,0));
			}
			
			
			
			for(int i=0;i<7;i++)
			{
				vectorBackwardList.add(new Vector(0,1));
			}
			for(int i=0;i<5;i++)
			{
				vectorBackwardList.add(new Vector(1,0));
			}
			for(int i=0;i<8;i++)
			{
				vectorBackwardList.add(new Vector(0,1));
			}
			for(int i=0;i<4;i++)
			{
				vectorBackwardList.add(new Vector(-1,0));
			}
			
			
			for(int i=0;i<2;i++)
			{
				vectorForwardList.add(new Vector(0,0));
			}
			vineList.add(new SingleVine(image,new Position(position.getXPosition(),position.getYPosition()),this,lockThree,lockOne));
			vineList.add(new SingleVine(image,new Position(position.getXPosition()-1,position.getYPosition()),this,lockOne,lockTwo));
			vineList.add(new SingleVine(image,new Position(position.getXPosition()-2,position.getYPosition()),this,lockTwo,lockThree));
			walkTime = 22;
		}
		else
		{
			for(int i=0;i<4;i++)
			{
				vectorForwardList.add(new Vector(1,0));
			}
			for(int i=0;i<16;i++)
			{
				vectorForwardList.add(new Vector(0,1));
			}
			for(int i=0;i<4;i++)
			{
				vectorForwardList.add(new Vector(1,0));
			}
			for(int i=0;i<2;i++)
			{
				vectorForwardList.add(new Vector(0,0));
			}
			
			
			
			for(int i=0;i<4;i++)
			{
				vectorBackwardList.add(new Vector(-1,0));
			}
			for(int i=0;i<16;i++)
			{
				vectorBackwardList.add(new Vector(0,-1));
			}
			for(int i=0;i<4;i++)
			{
				vectorBackwardList.add(new Vector(-1,0));
			}
			for(int i=0;i<2;i++)
			{
				vectorForwardList.add(new Vector(0,0));
			}
			vineList.add(new SingleVine(image,new Position(position.getXPosition(),position.getYPosition()),this,lockThree,lockOne));
			vineList.add(new SingleVine(image,new Position(position.getXPosition()-1,position.getYPosition()),this,lockOne,lockTwo));
			vineList.add(new SingleVine(image,new Position(position.getXPosition()-2,position.getYPosition()),this,lockTwo,lockThree));
			walkTime = 22;
		}
		howMany = 3;
		new Thread(this,"").start();
	}
	
	private synchronized void take(int index)
	{
		if(index == 1)
		{
			if(!lockCritOne.tryAcquire())
			{
				waiting[type]=true;
				try {
					lockCritOne.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				waiting[type]=false;
			}
		}
		else if(index == 2)
		{
			if(!lockCritTwo.tryAcquire())
			{
				waiting[type]=true;
				try {
					lockCritTwo.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				waiting[type]=false;
			}
		}
		else if(index == 3)
		{
			if(!lockCritThree.tryAcquire())
			{
				waiting[type]=true;
				try {
					lockCritThree.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				waiting[type]=false;
			}
		}
		else if(index == 5)
		{
			if(!lockCritFour.tryAcquire())
			{
				waiting[type]=true;
				try {
					lockCritFour.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				waiting[type]=false;
			}
		}
	}
	
	private synchronized void give(int index)
	{
		if(index == 1)
		{
			lockCritOne.release();
		}
		else if(index == 2)
		{
			lockCritTwo.release();
		}
		else if(index == 3)
		{
			lockCritThree.release();
		}
		else if(index == 5)
		{
			lockCritFour.release();
		}
	}
	
	public void draw(int x, int y, Graphics2D g)
	{
		Map.drawingChanges.lock();
		for(int i=0;i<vineList.size();i++)
		{
			SingleVine vine = vineList.get(i);
			Position position = vine.getPosition();
			vine.draw(position.getXPosition()*64+(int)vine.getXOffset(), position.getYPosition()*64+(int)vine.getYOffset(), g);
		}
		Map.drawingChanges.unlock();
	}
	
	public synchronized void notice()
	{
		iterator++;
		if(iterator==howMany)
		{
			if(forward)
			{
				if(type == 1)
				{
					if(listIterator == 1)
					{
						take(1);
					}
					if(listIterator == 9)
					{
						take(2);
						take(5);
						take(3);
					}
				}
				else if(type == 2)
				{
					if(listIterator == 1)
					{
						take(2);
						take(3);
					}
					else if(listIterator == 9)
					{
						take(5);
					}
				}
				else if(type == 3)
				{
					if(listIterator == 9)
					{
						take(1);
						take(5);
					}
				}
			}
			else
			{
				if(type == 1)
				{
					if(listIterator == 1)
					{
						take(2);
						take(3);
					}
					else if(listIterator == 9)
					{
						take(1);
						take(5);
					}
				}
				else if(type == 2)
				{
					if(listIterator == 9)
					{
						take(2);
						take(5);
						take(3);
					}
				}
				else if(type == 3)
				{
					if(listIterator ==9)
					{
						take(5);
					}
				}
			}
			iterator = 0;
			new Thread(this).start();
		}
	}
	
	public void run() 
	{
		if(type==1&&!waiting[3])
		{
			try {
				lockVineThree.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(type==2&&!waiting[1])
		{
			try {
				lockVineOne.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(type==1&&!waiting[2])
		{
			try {
				lockVineTwo.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Map.drawingChanges.lock();
		if(listIterator == walkTime) 
		{
			listIterator = 0;
			forward = !forward;
		}
		if(forward)
		{
			vineList.get(0).addVector(vectorForwardList.get(listIterator+2));
			vineList.get(0).move();
			vineList.get(1).addVector(vectorForwardList.get(listIterator+1));
			vineList.get(1).move();
			vineList.get(2).addVector(vectorForwardList.get(listIterator++));
			vineList.get(2).move();
		}
		else
		{
			vineList.get(2).addVector(vectorBackwardList.get(listIterator+2));
			vineList.get(2).move();
			vineList.get(1).addVector(vectorBackwardList.get(listIterator+1));
			vineList.get(1).move();
			vineList.get(0).addVector(vectorBackwardList.get(listIterator++));
			vineList.get(0).move();
		}
		Map.drawingChanges.unlock();
		if(type==1)
		{
			lockVineOne.release();
		}
		else if(type==2)
		{
			lockVineTwo.release();
		}
		else if(type==3)
		{
			lockVineThree.release();
		}
		if(forward)
		{
			if(type == 3)
			{
				if(listIterator == 14)
				{
					give(5);
				}
			}
			else if(type == 2)
			{
				if(listIterator == 14)
				{
					give(2);
					give(5);
					give(3);
				}
			}
			else if(type == 1)
			{
				if(listIterator == 14)
				{
					give(1);
					give(5);
				}
				else if(listIterator == 21)
				{
					give(2);
					give(3);
				}
			}
		}
		else
		{
			if(type==1)
			{
				if(listIterator==14)
				{
					give(2);
					give(5);
					give(3);
				}
				else if(listIterator==21)
				{
					give(1);
					give(5);
				}
			}
			else if(type==2)
			{
				if(listIterator==14)
				{
					give(5);
				}
				else if(listIterator==21)
				{
					give(2);
					give(3);
				}
			}	
			else if(type==3)
			{
				if(listIterator==13)
				{
					give(1);
					give(5);
				}
			}
		}
		
	}
}
