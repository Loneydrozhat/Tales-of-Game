package objects.notAlive.textObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.time.LocalTime;

import boards.GameBoard;
import graphics.Map;
import objects.MainObject;
import objects.alive.Alive;
import objects.notAlive.NotAlive;
import timers.textTimer.TextTimer;

public class ChatText extends AbstractText{


	protected static Font font = new Font("Arial",Font.PLAIN, 13);
	protected static Color color = Color.YELLOW;
	
	public ChatText(String text, int x, int y, MainObject thisObject)
	{
		super(text,x,y,2500);
		String buforToAdd = new String("");
		buforToAdd += thisObject.desc;
		buforToAdd += " ";
		if(thisObject instanceof Alive)
		{
			if(((Alive)thisObject).lvl>0)
			{
				buforToAdd += "[" + ((Alive)thisObject).lvl + "] ";
			}
		}
		GameBoard.chatScroll.addSomething(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + " " + buforToAdd + ": " + text);
		for(int xOffset=-3;xOffset<=3;xOffset++)
		{
			for(int yOffset=-3;yOffset<=3;yOffset++)
			{
				Map.map[x+xOffset][y+yOffset].sendText(text, thisObject);
			}
		}

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
