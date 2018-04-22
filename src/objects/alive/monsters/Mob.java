package objects.alive.monsters;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import boards.GameBoard;
import exceptions.CannotWalkException;
import graphics.Map;
import objects.MainObject;
import objects.alive.Alive;
import objects.alive.Hero;
import objects.notAlive.textObjects.MobText;
import threads.AttackManagement;
import threads.WalkManagement;
import timers.Timer;
import timers.textTimer.NormalTimer;
import utilities.Position;
import utilities.Vector;

public class Mob extends Alive {

	public Mob(int indexGraphic, Position position) {
		super(GameBoard.mobs.get(indexGraphic*12));
		this.isTall=true;
		this.desc = "Wolf";
		this.position = position.clone();
		this.attackSpeed = 800;
		this.attacked = false;
		scale = 2.5;
		turned = new BufferedImage[4];
		for(int i=0;i<4;i++)
		{
			turned[i]=GameBoard.mobs.get(indexGraphic*12+i*3);
		}
		walkAnimation = new BufferedImage[4][2];
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<2;j++)
			{
				walkAnimation[i][j]=GameBoard.mobs.get(indexGraphic*12+i*3+j+1);
			}
		}
		minAttack = 6;
		maxAttack = 17;
		new Thread(this,"Creature").start();
	}
	
	public void timerEnded(int index)
	{
		if(index==Timer.MOVING_STEP_ENDED)
		{
			if(alive)
			{
				lockOffset.lock();
				Vector vector = vectorList.get(0).clone();
				animationIterator++;
				animationChange = (int)(walkTime/99.9);
				if(animationIterator%animationChange==0)
				{
					animationIterator = 0;
					animationFrame++;
					animationFrame%=2;
					this.image = walkAnimation[faceWay][animationFrame];
				}
				setXOffset(getXOffset()-vector.getXVector()*64);
				setYOffset(getYOffset()-vector.getYVector()*64);
				lockOffset.unlock();
			}
			else Map.deleteElementFromMap(this, position);
		}
		else if(index==Timer.MOVING_ENDED)
		{
			if(alive)
			{
				lockOffset.lock();
				setXOffset(0);
				setYOffset(0);
				this.image = turned[faceWay];
				vectorList.remove(0);
				lockOffset.unlock();
			}
			else Map.deleteElementFromMap(this, position);
		}
		else if(index==Timer.ATTACKING_ENDED_SUCCESSFULLY)
		{
			if(alive) attack(focus);
		}
		else if(index==Timer.ATTACKING_ENDED_UNSECCESSFULLY)
		{
			if(alive) attacked = false;
		}
	}
	
	public void sendMessage(String text)
	{
		Map.drawingChanges.lock();
		Position position = this.getPosition().clone();
		new MobText(text, position.getXPosition(), position.getYPosition());
		Map.drawingChanges.unlock();
	}
	
	public void newArrival(MainObject thisObject)
	{
		if(thisObject instanceof Hero)
		{
			int distance = position.distance(thisObject.getPosition());
			if(distance<=3)
			{
				focus = (Alive) thisObject;
				if(!attacked)
				{
					attacked = true;
					attack(focus);
				}
			}
			else if(distance>=6)
			{
				focus = null;
			}
		}
	}
	
	public void receiveText(String text, MainObject thisObject)
	{
		if(thisObject!=this)
		{
			if(text.toLowerCase().matches("hi")) sendMessage("Woof");
		}
	}
	public void receiveText(String text)
	{
		sendMessage("Woof");
	}
}
