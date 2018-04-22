package boards;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import gamePanels.GamePanel;

public class BoardHandler {

	private ArrayList<Board> plansze;
	private int curState;
	private int gameState = 0;
	public BoardHandler(GamePanel panel)
	{
		plansze = new ArrayList<Board>();
		plansze.add(new GameBoard(this, panel));
		curState = gameState;	
	}
	
	public void setState(int index)
	{
		curState = index;
	}
	public void update()
	{
		plansze.get(curState).update();
	}
	public void draw()
	{
		plansze.get(curState).draw();
	}
	public void keyPressed(KeyEvent ev)
	{
		plansze.get(curState).keyPressed(ev);
	}
	public void keyReleased(KeyEvent ev)
	{
		plansze.get(curState).keyReleased(ev);
	}
}
