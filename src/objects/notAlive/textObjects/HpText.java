package objects.notAlive.textObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import graphics.Map;
import objects.notAlive.NotAlive;
import timers.textTimer.TextTimer;

public class HpText extends AbstractText{


	private static Font font = new Font("Arial",Font.PLAIN, 13);
	private int iterations = 10;
	private final static long showTime = 1000;
	private int height = 0;
	public HpText(String text, int x, int y, Color color)
	{
		super(text,x,y,100);
		this.color = color;
	}
	
	public void draw(int x, int y, Graphics2D g)
	{
		FontMetrics fm = g.getFontMetrics(font);
		g.setColor(Color.BLACK);
		g.setFont(font);
		g.drawString(text, x-fm.stringWidth(text)/2-1, y-5-1-height);
		g.drawString(text, x-fm.stringWidth(text)/2-1, y-5+1-height);
		g.drawString(text, x-fm.stringWidth(text)/2+1, y-5-1-height);
		g.drawString(text, x-fm.stringWidth(text)/2+1, y-5+1-height);
		g.setColor(color);
		g.drawString(text, x-fm.stringWidth(text)/2, y-5-height);
	}
	
	@Override
	public void timerEnded(int index)
	{
		if(index==0)
		{
			iterations--;
			if(iterations==0)
			{
				Map.drawingChanges.lock();
				Map.map[x][y].deleteObject(this);
				Map.drawingChanges.unlock();
			}
			else
			{
				height+=5;
				new TextTimer(100,this,0);
			}
		}
	}

	@Override
	protected Font getFont() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Color getColor() {
		// TODO Auto-generated method stub
		return null;
	}
}
