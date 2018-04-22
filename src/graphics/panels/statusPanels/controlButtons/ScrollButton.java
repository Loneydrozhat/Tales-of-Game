package graphics.panels.statusPanels.controlButtons;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import exceptions.CannotDragException;
import graphics.panels.statusPanels.ContainerPanel;
import graphics.panels.statusPanels.StatusPanel;

public class ScrollButton extends JPanel implements ControlButton
{
	private static BufferedImage top = null;
	private static BufferedImage middle = null;
	private static BufferedImage bottom = null;
	private int height = 0;
	private StatusPanel holder;
	private int y = 0;
	private int draggedOffset = 0;
	public ScrollButton(StatusPanel statsPanel)
	{
		setOpaque(false);
		this.holder = statsPanel;
		if(top==null)
		{
			BufferedImage input = null;
			try {
				input = ImageIO.read(getClass().getResourceAsStream("/ScrollButton.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			top = input.getSubimage(0, 0, 12, 5);
			middle = input.getSubimage(0, 5, 12, 1);
			bottom = input.getSubimage(0, 6, 12, 6);
		}
	}

	public void setHeight(int height)
	{
		this.height = height;
		setBounds(168,15,12,height);
	}
	
	public void draw(int x, int y, Graphics2D g)
	{
		int middleHeight = height - 11;
		g.drawImage(top,x,y,12,5,null);
		g.drawImage(middle,x,y+5,12,middleHeight+1,null);
		g.drawImage(bottom, x, y+height-6, 12, 6, null);
		
	}
	
	@Override
	public void clicked(MouseEvent e) 
	{
		y = e.getY();
	}
	
	public void drag(MouseEvent e)
	{
		y-=e.getY();
		if(SwingUtilities.isLeftMouseButton(e))
		{
			try
			{
				holder.scrollDragged(-y);
				draggedOffset+=-y;
			}
			catch (CannotDragException exception)
			{
				
			}
		}
		y = e.getY();
	}
	
}
