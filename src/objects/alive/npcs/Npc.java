package objects.alive.npcs;

import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import boards.GameBoard;
import exceptions.CannotWalkException;
import graphics.Map;
import objects.MainObject;
import objects.alive.Alive;
import objects.alive.Hero;
import objects.notAlive.textObjects.ChatText;
import objects.notAlive.textObjects.MobText;
import threads.WalkManagement;
import timers.Timer;
import timers.textTimer.NormalTimer;
import utilities.Position;
import utilities.Vector;

public class Npc extends Alive 
{

	public Npc(int outfitIndex, Position position) {

		super(GameBoard.outfits.get(outfitIndex*12));
		turned = new BufferedImage[4];
		for(int i=0;i<4;i++)
		{
			turned[i]=GameBoard.outfits.get(outfitIndex*12+i*3);
		}
		walkAnimation = new BufferedImage[4][2];
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<2;j++)
			{
				walkAnimation[i][j]=GameBoard.outfits.get(outfitIndex*12+i*3+j+1);
			}
		}
		scale = 2.5;
		
		this.isTall=true;
		this.desc = "Taylor";
		this.position = position.clone();
		this.walkTime = 3000;
		animationChange = (int)(walkTime/99.9);
		new Thread(this,"Creature").start();
	}
	
	public void timerEnded(int index)
	{
		if(index==Timer.MOVING_STEP_ENDED)
		{
			lockOffset.lock();
			Vector vector = vectorList.get(0).clone();
			setXOffset(getXOffset()-vector.getXVector()*64);
			setYOffset(getYOffset()-vector.getYVector()*64);
			animationIterator++;
			if(animationIterator%animationChange==0)
			{
				animationIterator = 0;
				animationFrame++;
				animationFrame%=2;
				this.image = walkAnimation[faceWay][animationFrame];
			}
			lockOffset.unlock();
		}
		else if(index==Timer.MOVING_ENDED)
		{
			lockOffset.lock();
			setXOffset(0);
			setYOffset(0);
			this.image = turned[faceWay];
			vectorList.remove(0);
			lockOffset.unlock();
		}
	}
	public void sendMessage(String text)
	{
		Map.drawingChanges.lock();
		Position position = this.getPosition().clone();
		new ChatText(text, position.getXPosition(), position.getYPosition(), this);
		Map.drawingChanges.unlock();
	}
	public void newArrival(MainObject thisObject)
	{
		if(thisObject instanceof Hero)
		{
			thisObject.inRange(this);
			//new MobText("Hello Hero!", position.getXPosition(), position.getYPosition());
		}
	}
	
	public void receiveText(String text, MainObject thisObject)
	{
		if(thisObject!=this)
		{
			if(text.toLowerCase().matches("hi")) sendMessage("Hello");
		}
	}
}
