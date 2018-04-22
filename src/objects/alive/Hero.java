package objects.alive;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import boards.GameBoard;
import exceptions.SpellExhaustException;
import graphics.Map;
import graphics.panels.HeroStatus;
import graphics.panels.statusPanels.ItemSlotPanel;
import objects.MainObject;
import objects.alive.monsters.Mob;
import objects.notAlive.textObjects.HpText;
import threads.AttackManagement;
import threads.WalkManagement;
import timers.Timer;
import timers.textTimer.NormalTimer;
import utilities.Position;
import utilities.Vector;

public class Hero extends Alive{

	public static long walkTime;
	private double howManySteps;
	private static boolean hasteWait = false;
	private boolean spellExhaust = false;
	private double scale = 2.5;
	public static int lvl;
	public static int mana;
	public static int maxMana;
	public Hero(int outfitIndex)
	{
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
		lvl = 30;
		setHp(300);
		setMaxHp(300);
		this.desc = "Hero";
		walkTime = 200;
		howManySteps = walkTime/20.0;
		stepTime = walkTime/howManySteps;
		position = new Position(22,19);
		this.isTall = true;
		animationIterator= 0;
		animationFrame = 0;
		minAttack = 10;
		maxAttack = 15;
		attackSpeed = 300;
		attacked = false;
		mana = 100;
		maxMana = 150;
		maxHp = 130+lvl*15;
		hp = maxHp;
		maxMana = -30+lvl*5;
		if(maxMana<0)maxMana = 0;
		mana = maxMana;
		new NormalTimer(5000,this,Timer.FEEDING_INTERVAL);
	}

	public void lvlUp()
	{
		lvl++;
		maxHp = 130+lvl*15;
		hp = maxHp;
		maxMana = -30+lvl*5;
		if(maxMana<0)maxMana = 0;
		mana = maxMana;
	}
	
	public void exura()
	{
		if(!spellExhaust)
		{
			if(mana>=50)
			{
				setMana(-50);
				spellExhaust = true;
				useSpell("Exura");
				healLife(20);
				new NormalTimer(1500,this,2);
			}
		}
		else
		{
			throw new SpellExhaustException();
		}
	}
	
	public void haste()
	{
		if(!hasteWait)
		{
			if(mana>=80)
			{
				setMana(-80);
				hasteWait = true;
				useSpell("Utani Hur");
				walkTime /=1.5;
				new NormalTimer(10000,this,1);
			}
		}
	}
	
	public synchronized void timerEnded(int index)
	{
		if(index==1)
		{
			lockMoving.lock();
			hasteWait = false;
			walkTime*=1.5;
			lockMoving.unlock();
		}
		else if(index==2)
		{
			spellExhaust = false;
		}
		else if(index==Timer.MOVING_STEP_ENDED)
		{
			Map.drawingChanges.lock();
			lockOffset.lock();
			Map.setXYOffset(-getXOffset(), -getYOffset());
			animationIterator++;
			animationChange = (int)(walkTime/99.9);
			if(animationIterator%animationChange==0)
			{
				animationIterator = 0;
				animationFrame++;
				animationFrame%=2;
				this.image = walkAnimation[faceWay][animationFrame];
			}
			lockOffset.unlock();
			Map.drawingChanges.unlock();
		}
		else if(index==Timer.MOVING_ENDED)
		{
			Map.drawingChanges.lock();
			changingVector.lock();
			GameBoard.addOffset(vectorList.get(0).clone());
			vectorList.remove(0);
			changingVector.unlock();
			Map.setXYOffset(0,0);
			isMoving = false;
			this.image = turned[faceWay];
			Map.drawingChanges.unlock();
		}
		else if(index==Timer.ATTACKING_ENDED_SUCCESSFULLY)
		{
			new AttackManagement(this,focus,attackSpeed,minAttack,maxAttack);
		}
		else if(index==Timer.ATTACKING_ENDED_UNSECCESSFULLY)
		{
			System.out.println(this + " Failed");
			if(!focus.alive) focus = null;
			attacked = false;
		}
		else if(index==Timer.FEEDING_INTERVAL)
		{
			this.healLife(30);
			this.setMana(10);
			new NormalTimer(5000,this,Timer.FEEDING_INTERVAL);
		}
	}
	
	public void setMana(int value)
	{
		int newMana = mana+value;
		if(newMana<0) newMana = 0;
		else if(newMana>maxMana) newMana = maxMana;
		mana = newMana;
	}
	
	public void setFocus(Mob focus)
	{
		this.focus = focus;
		if(!attacked)
		{
			int attackAddValue = 0;
			if(HeroStatus.eq[HeroStatus.LEFT_HAND].getItem().desc == "Sword") attackAddValue +=10;
			new AttackManagement(this,focus,attackSpeed,minAttack+attackAddValue,maxAttack+attackAddValue);
		}
	}
	
	public void moveHero(int vectorX, int vectorY)
	{
		changingVector.lock();
		vectorList.add(new Vector(vectorX,vectorY));
		faceWay = vectorList.get(0).direction();
		this.image = turned[faceWay];
		changingVector.unlock();
		new WalkManagement(this,walkTime,vectorList.get(0).clone());
		attack(focus);
	}
	@Override
	public void run() 
	{
	}
	public void draw(int x, int y, Graphics2D g)
	{
		Map.drawingChanges.lock();
		//drawStatusBar(9*64-(int)(iconWidth*scale)+64,6*64-(int)(iconHeight*scale)+64,g);
		g.drawImage(image,9*64-(int)(iconWidth*scale)+64,6*64-(int)(iconHeight*scale)+64,(int)(iconWidth*scale),(int)(iconHeight*scale),null);
		Map.drawingChanges.unlock();
	}
	
	public void inRange(MainObject thisObject)
	{
		//System.out.println(thisObject);
	}
	
}
