package objects.notAlive.textObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import graphics.Map;
import objects.notAlive.NotAlive;
import timers.textTimer.TextTimer;

public class SpellText extends AbstractText{

	private static final Font font = new Font("Arial",Font.BOLD|Font.ITALIC, 11);
	private static final Color color = new Color(255,180,0);
	public SpellText(String text, int x, int y)
	{
		super(text,x,y,2500);
		this.drawOrder = 1;
	}
	
	@Override
	public void timerEnded(int index)
	{
		Map.drawingChanges.lock();
		Map.map[x][y].deleteObject(this);
		Map.drawingChanges.unlock();
	}
	
	@Override
	protected Font getFont() {
		// TODO Auto-generated method stub
		return font;
	}
	@Override
	protected Color getColor() {
		// TODO Auto-generated method stub
		return color;
	}
}
