package threads;

import boards.GameBoard;
import graphics.Map;
import objects.alive.Alive;
import timers.Timer;
import utilities.Position;
import utilities.Vector;

public class WalkManagement implements Runnable
{

	private Alive thisObject;
	private double totalTime;
	private int xPosition;
	private int yPosition;
	private int vectorX;
	private int vectorY;
	private double howManySteps;
	private double stepTime;
	private int changeType = 0;
	public WalkManagement(Alive thisObject, long walkTime, Vector vector)
	{
		if(thisObject.isAlive())
		{
			this.thisObject = thisObject;
			this.totalTime = walkTime;
			Position position = thisObject.getPosition();
			xPosition = position.getXPosition();
			yPosition = position.getYPosition();
			this.vectorX = vector.getXVector();
			this.vectorY = vector.getYVector();
			thisObject.setPosition(new Position(xPosition+ vectorX,yPosition+vectorY));
			howManySteps = walkTime/33.3;
			stepTime = walkTime/howManySteps;
			sendMovement();
			initMapChanges();
			new Thread(this,"Walk Thread").start();
		}
	}
	
	private void sendMovement()
	{
		for(int x=-8;x<=8;x++)
		{
			for(int y=-6;y<=6;y++)
			{
				Map.map[xPosition+vectorX+x][yPosition+vectorY+y].noticeArrival(thisObject);
			}
		}
	}
	
	private void initMapChanges()
	{
		Map.addElementToMap(thisObject, new Position(xPosition+vectorX,yPosition+vectorY));
		if(xPosition>GameBoard.xOffset+2 && yPosition>GameBoard.yOffset+2)
		{
			Map.map[xPosition+vectorX][yPosition+vectorY].setHasMoving(true);
			if(vectorY == -1 && vectorX == -1)
			{
				changeType = 1;
				Map.map[xPosition+vectorX][yPosition+vectorY].drawRightFirst(true);
				Map.map[xPosition+vectorX][yPosition+vectorY+1].setToDrawAlive(true, Map.map[xPosition+vectorX][yPosition+vectorY]);
			}
			else if(vectorY == -1 && vectorX == 1)
			{
			}
			else if(vectorY == -1)
			{
				changeType = 2;
				Map.map[xPosition+vectorX][yPosition+vectorY].setToWait(true);
				Map.map[xPosition+vectorX-1][yPosition+vectorY+1].setToWakeUp(true, Map.map[xPosition+vectorX][yPosition+vectorY]);
			}
			else if(vectorY == 1)
			{
				changeType = 3;
				Map.map[xPosition+vectorX][yPosition+vectorY].setToWakeUp(true, Map.map[xPosition+vectorX+1][yPosition+vectorY-1]);
				Map.map[xPosition+vectorX+1][yPosition+vectorY-1].setToWait(true);
			}
			else if(vectorX == 1)
			{
				Map.map[xPosition+vectorX][yPosition+vectorY].setDrawLeftFirst(true);
				changeType = 4;
			}
		}
		Map.map[xPosition][yPosition].setHasMoving(false);
		Map.deleteElementFromMap(thisObject, new Position(xPosition,yPosition));
	}
	
	private void endMapChanges()
	{
		if(changeType>0)
		{
			Map.drawingChanges.lock();
			if(thisObject.isAlive())
			{
				if(changeType == 1)
				{
					Map.map[xPosition+vectorX][yPosition+vectorY].drawRightFirst(false);
					Map.map[xPosition+vectorX][yPosition+vectorY+1].setToDrawAlive(false,null);
					Map.map[xPosition+vectorX][yPosition+vectorY].setHasMoving(true);
				}
				else if (changeType == 2)
				{
					Map.map[xPosition+vectorX][yPosition+vectorY].setToWait(false);
					Map.map[xPosition+vectorX-1][yPosition+vectorY+1].setToWakeUp(false,null);
				}
				else if(changeType == 3)
				{
					Map.map[xPosition+vectorX][yPosition+vectorY].setToWakeUp(false, null);
					Map.map[xPosition+vectorX+1][yPosition+vectorY-1].setToWait(false);
				}
				else if(changeType == 4)
				{
					Map.map[xPosition+vectorX][yPosition+vectorY].setDrawLeftFirst(false);
				}
			}
			Map.drawingChanges.unlock();
		}
	}
	
	@Override
	public void run()
	{
		double sum = 0;
		double lastStep = System.currentTimeMillis();
		while(sum<totalTime)
		{
			try {
				if(stepTime - (System.currentTimeMillis()-lastStep)>0)
				{
					Thread.sleep((long) (stepTime - (System.currentTimeMillis()-lastStep)));
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lastStep = System.currentTimeMillis();
			if(thisObject.isAlive())
			{
				thisObject.lockOffset.lock();
				thisObject.setXOffset((sum/totalTime)*64*vectorX);
				thisObject.setYOffset((sum/totalTime)*64*vectorY);
				thisObject.lockOffset.unlock();
				thisObject.timerEnded(Timer.MOVING_STEP_ENDED);
			}
			sum+=stepTime;
		}
		endMapChanges();
		//System.out.println("Ending walking Thread for: " + thisObject);
		thisObject.timerEnded(Timer.MOVING_ENDED);
	}
}
