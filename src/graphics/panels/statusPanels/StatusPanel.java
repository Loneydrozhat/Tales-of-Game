package graphics.panels.statusPanels;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import boards.GameBoard;
import exceptions.CannotDragException;
import graphics.panels.statusPanels.controlButtons.ControlButton;
import graphics.panels.statusPanels.controlButtons.ExitButton;
import graphics.panels.statusPanels.controlButtons.ScrollButton;

public abstract class StatusPanel extends JPanel
{
	protected int height;
	protected int hidden;
	protected Lock lockScroll;
	protected int offsetY;
	protected ExitButton exitButton;
	protected ScrollButton scrollButton;
	protected float percent;
	protected float scrollHeight;
	protected int slotsHeight;
	protected int scrollOffset = 0;
	protected BufferedImage scene;
	protected static BufferedImage panelBcg = null;
	public StatusPanel(int height)
	{
		super(null);
		setOpaque(false);
		hidden = 0;
		lockScroll = new ReentrantLock();
		exitButton = new ExitButton();
		this.height = height;
		setBounds(180-10,0,180,height);
		if(panelBcg==null)
		{
			try {
				panelBcg = ImageIO.read(getClass().getResourceAsStream("/StatusBcg.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public abstract void draw(Graphics2D g, int yOffset);
	
	public int getHeight()
	{
		return height - hidden + 12;
	}

	public ItemSlotPanel getSlotPanel(Point p, MouseEvent e)
	{
		if(p.getX()<168)p.translate(0, scrollOffset);
		Component element = null;
		for(Component comp : getComponents())
		{
			if(comp.getBounds().contains(p)) element = comp;
		}
		if(element instanceof ItemSlotPanel) 
		{
			ItemSlotPanel slot = (ItemSlotPanel) element;
			return slot;
		}
		else if(element instanceof ControlButton) 
		{
			GameBoard.buttonOnMouse = (ControlButton) element;
			((ControlButton) element).clicked(e);
			return null;
		}
		else return null;
	}
	
	public void setOffset(int x, int y)
	{
		offsetY = y;
		for(Component element : getComponents())
		{
			ItemSlotPanel slot = (ItemSlotPanel)element;
			slot.setLocation((int)slot.getX()+x, (int)slot.getY()+offsetY);
		}
	}
	
	public void setScrollOffset(int x)
	{
		scrollOffset+=x;
		scrollButton.setBounds(168,15+(int)(scrollOffset*percent),12,(int)scrollHeight);
	}
	
	
	public void scrollDragged(int x)
	{
		lockScroll.lock();
		if(x<0) 
		{
			if(scrollOffset+x>=0) 
			{
				 setScrollOffset(x);
			}
			else 
			{
				lockScroll.unlock();
				throw new CannotDragException();
			}
		}
		else if(x>0)
		{
			if(scrollOffset+x<=hidden) setScrollOffset(x);
			else 
			{
				lockScroll.unlock();
				throw new CannotDragException();
			}
		}
		lockScroll.unlock();
	}
}
