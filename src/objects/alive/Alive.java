package objects.alive;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JLabel;
import javax.swing.JPanel;

import boards.GameBoard;
import exceptions.CannotWalkException;
import graphics.Map;
import objects.MainObject;
import objects.notAlive.items.Item;
import objects.notAlive.items.itemTypes.Bag;
import objects.notAlive.textObjects.ChatText;
import objects.notAlive.textObjects.HpText;
import objects.notAlive.textObjects.SpellText;
import threads.AttackManagement;
import threads.HpManagement;
import threads.WalkManagement;
import timers.Timer;
import utilities.Position;
import utilities.Vector;

public abstract class Alive extends MainObject implements Runnable
{
	protected ArrayList <Vector> vectorList = new ArrayList <Vector>();
	protected int hp, maxHp;
	protected Lock hpLock = new ReentrantLock();
	public long walkTime;
	protected Position position;
	protected double howManySteps;
	protected Lock lockMoving = new ReentrantLock();
	public Lock changingVector = new ReentrantLock();
	protected boolean alive = true;
	protected boolean isMoving = false;
	protected double stepTime;
	protected double attackSpeed;
	protected boolean attacked;
	public HpManagement hpManagement;
	protected AttackManagement attackManagement;
	protected boolean isAttacking = false;
	public int lvl;
	protected int faceWay = 0;
	protected BufferedImage turned[];
	protected BufferedImage walkAnimation[][];
	protected int animationIterator;
	protected int animationFrame;
	protected int animationChange;
	protected Font font = new Font("Arial",Font.BOLD, 15);

	protected Alive focus;
	protected int minAttack;
	protected int maxAttack;
	
	public Alive(BufferedImage image) {
		super(image); 
		lvl = 0;
		position = new Position(10,10);
		walkTime = 300;
		howManySteps = walkTime/16.0;
		hpManagement = new HpManagement(this);
		attacked = false;
		hp = 200;
		maxHp = 200;
	}
	public Position getPosition()
	{
		return position.clone();
	}
	
	public boolean isAlive()
	{
		return alive;
	}
	
	public void setPosition(Position position)
	{
		this.position = position;
	}
	
	public synchronized void timerEnded(int index)
	{
		if(index==Timer.MOVING_ENDED)
		{
			//System.out.println("Hello");
		}
	}
	
	protected void setHp(int value)
	{
		this.hp = value;
	}
	
	protected void setMaxHp(int value)
	{
		hpLock.lock();
		this.maxHp = value;
		hpLock.unlock();
	}
	public int getHp()
	{
		hpLock.lock();
		int hpget = this.hp;
		hpLock.unlock();
		return hpget;
	}
	public int getMaxHp()
	{
		hpLock.lock();
		int hpget = this.maxHp;
		hpLock.unlock();
		return hpget;
	}
	
	public void healLife(int howMuch)
	{
		if(alive)
		{
			Map.drawingChanges.lock();
			hpLock.lock();
			int newValue = this.hp + howMuch;
			if(newValue>this.maxHp)newValue = maxHp;
			this.hp = newValue;
			new HpText("+" + howMuch,position.getXPosition(), position.getYPosition(), Color.GREEN);
			hpLock.unlock();
			Map.drawingChanges.unlock();
		}
	}
	
	public void reduceLife(int howMuch)
	{
		Map.drawingChanges.lock();
		hpLock.lock();
		int newValue = this.hp - howMuch;
		if(newValue<0)
		{
			newValue = 0;
			kill();
			Item deadBody = new Bag(GameBoard.itemGraphics.get(2),"Dead Body","8");
			deadBody.addMeToMap(getPosition());
		}
		this.hp = newValue;
		System.out.println("Setting Life " + this);
		new HpText("-" + howMuch,position.getXPosition(), position.getYPosition(), Color.RED);
		hpLock.unlock();
		Map.drawingChanges.unlock();
	}

	public void kill()
	{
		alive = false;
		hpManagement = null;
		Map.deleteElementFromMap(this, getPosition());
	}
	
	public void sendMessage(String text)
	{
		if(alive)
		{
			String buforToAdd = new String("");
			buforToAdd += this.desc;
			buforToAdd += " ";
			if(lvl>0)
			{
				buforToAdd += "[" + lvl + "] ";
			}
			Map.drawingChanges.lock();
			new ChatText(text, position.getXPosition(), position.getYPosition(), this);
			Map.drawingChanges.unlock();
		}
	}
	
	public void useSpell(String text)
	{
		Map.drawingChanges.lock();
		new SpellText(text, position.getXPosition(), position.getYPosition());
		Map.drawingChanges.unlock();
	}
	
	public void attack(Alive focus)
	{
		if(alive)
		{
			if(focus!=null)
			{	
				new AttackManagement(this,focus,attackSpeed,minAttack,maxAttack);
			}
		}
	}
	
	public void move(int vectorX, int vectorY)
	{
		vectorList.add(new Vector(vectorX, vectorY));
		faceWay = vectorList.get(0).direction();
		image = turned[faceWay];
		new WalkManagement(this,walkTime,vectorList.get(0));
		Map.drawingChanges.lock();
		setXOffset(-vectorX*64);
		setYOffset(-vectorY*64);
		Map.drawingChanges.unlock();
		try {
			Thread.sleep(walkTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void run() 
	{
		Random randomVector = new Random();
		Map.drawingChanges.lock();
		Map.map[position.getXPosition()][position.getYPosition()].add(this);
		Alive toAttack = GameBoard.hero;
		//startAttacking(toAttack);
		Map.drawingChanges.unlock();
		while(alive)
		{
			try {
				Thread.sleep(randomVector.nextInt(2000)+500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int i=0;
			int innerVectorX = randomVector.nextInt(3)-1;
			int innerVectorY = randomVector.nextInt(3)-1;
			try
			{
				Map.checkPassable(position.getXPosition()+innerVectorX, position.getYPosition()+innerVectorY);
				move(innerVectorX, innerVectorY);
			}
			catch (CannotWalkException e)
			{
			}
			lockMoving.lock();
			isMoving = false;
			lockMoving.unlock();
		}
	}
	
	protected void setGColor(Graphics2D g)
	{
		int maxHP = getMaxHp();
		int currHp = getHp();
		double ratio = (double)currHp/(double)maxHP;
		if(ratio>=0.9) g.setColor(Color.GREEN);
		else if(ratio > 0.5) g.setColor(new Color(0,150,0));
		else if(ratio > 0.2) g.setColor(Color.YELLOW);
		else if(ratio > 0.05) g.setColor(Color.red);
		else g.setColor(Color.BLACK);
	}
	
	protected void drawStatusBar(int x, int y, Graphics2D g)
	{
		g.setFont(font);
		g.setColor(Color.BLACK);
		for(int i=-1;i<=1;i++)
		{
			for(int j=-1;j<=1;j++)
			{
				g.drawString(desc, x+5+i, y-10+j);
			}
		}
		setGColor(g);
		g.drawString(desc, x+5, y-10);
	}
			
	public void draw(int x, int y, Graphics2D g)
	{
		Map.drawingChanges.lock();
		lockOffset.lock();
		g.drawImage(image,x-(int)(iconWidth*scale)+64+(int)getXOffset(),y-(int)(iconHeight*scale)+64+(int)getYOffset(),(int)(iconWidth*scale),(int)(iconHeight*scale),null);
		drawStatusBar(x-(int)(iconWidth*1.5)+96+(int)getXOffset(),y-(int)(iconHeight)+64+(int)getYOffset(),g);
		lockOffset.unlock();
		Map.drawingChanges.unlock();
	}
	
	public boolean getPassable()
	{
		return false;
	}
	public boolean isMoving()
	{
		lockMoving.lock();
		boolean answer = isMoving;
		lockMoving.unlock();
		return answer;
	}
}
