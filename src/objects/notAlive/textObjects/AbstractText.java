package objects.notAlive.textObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import graphics.Map;
import objects.notAlive.NotAlive;
import timers.textTimer.TextTimer;

public abstract class AbstractText extends NotAlive 
{
	String text;
	protected int x, y;
	protected long waitTime;
	protected Color color;
	protected Font font;
	public AbstractText(String text, int x, int y, long waitTime)
	{
		this.text = text;
		this.desc = text;
		this.drawOrder = 1;
		this.x = x;
		this.y = y;
		this.passable = true;
		Map.drawingChanges.lock();
		Map.map[x][y].add(this);
		new TextTimer(waitTime,this,0);
		Map.drawingChanges.unlock();
	}
	
	protected abstract Font getFont();
	protected abstract Color getColor();
	
	public void draw(int x, int y, Graphics2D g)
	{
		FontMetrics fm = g.getFontMetrics(getFont());
		g.setColor(Color.BLACK);
		g.setFont(getFont());
		g.drawString(text, x-fm.stringWidth(text)/2-1, y-5-1);
		g.drawString(text, x-fm.stringWidth(text)/2-1, y-5+1);
		g.drawString(text, x-fm.stringWidth(text)/2+1, y-5-1);
		g.drawString(text, x-fm.stringWidth(text)/2+1, y-5+1);
		g.setColor(getColor());
		g.drawString(text, x-fm.stringWidth(text)/2, y-5);
	}
	
	@Override
	public void timerEnded(int index)
	{
		Map.drawingChanges.lock();
		Map.map[x][y].deleteObject(this);
		Map.drawingChanges.unlock();
	}
}
