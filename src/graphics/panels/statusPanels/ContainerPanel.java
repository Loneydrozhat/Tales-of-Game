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
import javax.swing.SwingUtilities;

import boards.GameBoard;
import exceptions.CannotDragException;
import exceptions.CannotMoveItemException;
import exceptions.ReputItemException;
import graphics.panels.statusPanels.controlButtons.ControlButton;
import graphics.panels.statusPanels.controlButtons.ExitButton;
import graphics.panels.statusPanels.controlButtons.ScrollButton;
import objects.notAlive.items.Item;
import objects.notAlive.items.itemTypes.Bag;

public class ContainerPanel extends StatusPanel
{
	private static BufferedImage itemBcg = null;
	public static final int downSpace = 8;
	public static final int rightSpace = 7;
	private Bag holder;
	public ContainerPanel(int height, Bag holder)
	{
		super(height);
		this.holder = holder;
		setName("Container");
		setOpaque(false);
		if(itemBcg==null)
		{
			try {
				itemBcg = ImageIO.read(getClass().getResourceAsStream("/ItemBcg.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int slotsNumber = holder.getSlotsQuantity();
		int rows = (int) Math.ceil(slotsNumber/4);
		if(slotsNumber%4!=0) rows++;
		slotsHeight = rows*32+(rows-1)*downSpace;
		this.height = slotsHeight+12;
		scene = new BufferedImage(160,slotsHeight+15,BufferedImage.TYPE_INT_RGB);
		scrollButton = new ScrollButton(this);
		exitButton.setBounds(180-15,0,13,13);
		scrollHeight = ((float)(slotsHeight - hidden));
		percent = ((float)(slotsHeight-hidden))/slotsHeight;
		scrollHeight*=percent;
		scrollButton.setHeight((int)scrollHeight);
		add(exitButton);
		add(scrollButton);
	}
	
	public void returnOffset()
	{
		for(Component element : getComponents())
		{
			ItemSlotPanel slot = (ItemSlotPanel)element;
			slot.setLocation((int)slot.getX(), (int)slot.getY()-offsetY);
		}
	}
	
	public void draw(Graphics2D g, int yOffset)
	{
		lockScroll.lock();
		offsetY = yOffset;
		Graphics2D gScene = (Graphics2D) scene.getGraphics();
		for(int bcgHeight = 0;bcgHeight<height;bcgHeight+=panelBcg.getHeight())
		{
			gScene.drawImage(panelBcg, 0, bcgHeight+15,panelBcg.getWidth(),panelBcg.getHeight(), null);
		}
		for(Component element : getComponents())
		{
			if(element instanceof ItemSlotPanel)
			{
				ItemSlotPanel slot = (ItemSlotPanel)element;
				slot.drawItem(gScene , 0);
				g.drawImage(scene.getSubimage(10, 15+scrollOffset, scene.getWidth()-10, slotsHeight-hidden),10,yOffset+15,150,slotsHeight-hidden,null);
			}
			else if( element instanceof ExitButton)
			{
				ExitButton control = (ExitButton) element;
				control.draw(g, yOffset);
			}
			else if( element instanceof ScrollButton)
			{
				ScrollButton control = (ScrollButton) element;
				control.draw(165,15+yOffset+(int)(scrollOffset*percent),g);
			}
		}
		lockScroll.unlock();
	}
}
