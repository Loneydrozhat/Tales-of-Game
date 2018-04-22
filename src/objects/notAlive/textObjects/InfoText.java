package objects.notAlive.textObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import objects.notAlive.NotAlive;
import timers.textTimer.NormalTimer;

public class InfoText extends NotAlive{

	String text;
	private final int x=(int)(64*19)/2,y=200;
	private final Font font = new Font("Arial",Font.BOLD, 15);
	private int ite = 0;
	public InfoText()
	{
		this.desc = "Info displayer";
		this.text = new String("");
	}
	
	public void changeText(String text)
	{
		this.text = text;
		ite++;
		new NormalTimer(5000,this,ite);
	}
	
	public void draw(Graphics2D g)
	{
		FontMetrics fm = g.getFontMetrics(font);
		g.setColor(Color.BLACK);
		g.drawString(text, x-fm.stringWidth(text)/2-1, y-5-1);
		g.drawString(text, x-fm.stringWidth(text)/2-1, y-5+1);
		g.drawString(text, x-fm.stringWidth(text)/2+1, y-5-1);
		g.drawString(text, x-fm.stringWidth(text)/2+1, y-5+1);
		g.setColor(Color.GREEN);
		g.drawString(text, x-fm.stringWidth(text)/2, y-5);
		/*
		TextLayout border = new TextLayout(text,font,gMap.getFontRenderContext());
		Shape shape = border.getOutline(null);
		gMap.setColor(Color.BLACK);
		gMap.draw(shape);
		*/
	}
	
	@Override
	public void timerEnded(int index)
	{
		if(index==ite) text = "";
	}
}
