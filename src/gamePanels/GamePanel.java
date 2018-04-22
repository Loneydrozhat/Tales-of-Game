package gamePanels;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import boards.BoardHandler;

public class GamePanel extends JPanel implements KeyListener, Runnable
{
	private static final long serialVersionUID = 1L;
	public int width;
	public int height;
	private Thread thread;
	
	private int fps;
	private long timePerFrap;
	private boolean isOn;
	
	private BoardHandler board;
	
	public GamePanel()
	{
		super();
		setPreferredSize(new Dimension(width=1920,height=1080));
		setFocusable(true);
		requestFocus();
		setLayout(null);
	}


	public void addNotify()
	{
		super.addNotify();
		if(thread == null)
		{
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent ev) 
	{
		board.keyPressed(ev);
	}

	@Override
	public void keyReleased(KeyEvent ev) 
	{
		board.keyReleased(ev);
	}

	@Override
	public void keyTyped(KeyEvent ev)
	{
		// TODO Auto-generated method stub
		
	}

	private void init()
	{
		isOn = true;
		fps = 60;
		this.timePerFrap = 1000 / fps;
		board = new BoardHandler(this);
	}
	
	@Override
	public void run() 
	{
		long beggining;
		long passed;
		long wait;
		init();
		int i = 0;
		while(isOn)
		{
			beggining = System.nanoTime();
			//update();
			draw();
			//drawToScreen();
			passed = System.nanoTime() - beggining;
			
			wait = timePerFrap - passed / 1000000;
			if(wait>0)
			{
				try
				{
					Thread.sleep(wait);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
	}
	private void draw()
	{
		board.draw();
	}
}
