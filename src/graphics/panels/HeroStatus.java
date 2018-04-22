package graphics.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import abilities.Openable;
import boards.GameBoard;
import graphics.Map;
import graphics.panels.statusPanels.ContainerPanel;
import graphics.panels.statusPanels.ItemSlotPanel;
import graphics.panels.statusPanels.StatisticsPanel;
import graphics.panels.statusPanels.StatusPanel;
import graphics.panels.statusPanels.controlButtons.ControlButton;
import graphics.panels.statusPanels.controlButtons.StatsButton;
import objects.MainObject;
import objects.notAlive.items.Item;

public class HeroStatus extends JPanel implements PanelOnScreen
{
	private int statusPanelHeight;
	private int statusPanelWidth;
	private BufferedImage statusScene;
	private Font font;
	private BufferedImage hpBarFiller;
	private BufferedImage manaBarFiller;
	public static ItemSlotPanel eq[];
	private static int containersMinHeight = 330;
	private static int containersHeight = 0;
	private static ArrayList <StatusPanel> containers;
	private static ArrayList <JPanel> waitAddList;
	private static ArrayList <JPanel> waitRemoveList;
	private static Lock containersLock;
	private static int ite=0;
	public static final int HELMET = ite++; //0
	public static final int AMULET = ite++; //1
	public static final int BACKPACK = ite++; //2
 	public static final int LEFT_HAND = ite++; //3
	public static final int ARMOR = ite++; //4
	public static final int RIGHT_HAND = ite++; //5
	public static final int RING = ite++; //6
	public static final int LEGS = ite++; //7
	public static final int AMMO = ite++; //8
	public static final int BOOTS = ite++; //9
	private static StatsButton statsButton;
	public HeroStatus()
	{
		super(null);
		this.setBounds(1740, 0, 180, 1080);
		statusPanelHeight = 1080;
		statusPanelWidth = 180;
		statusScene = null;
		try {
			statusScene = ImageIO.read(getClass().getResourceAsStream("/StatusPanel.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		font = new Font("Ariel",Font.BOLD,15);
		try {
			hpBarFiller = ImageIO.read(getClass().getResourceAsStream("/BarColor.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			manaBarFiller = ImageIO.read(getClass().getResourceAsStream("/BarColorMana.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		initEqPanels();
		containers = new ArrayList <StatusPanel>();
		waitAddList = new ArrayList <JPanel>();
		waitRemoveList = new ArrayList <JPanel>();
		containersLock = new ReentrantLock();
		statsButton = new StatsButton(130,200,"Stats");
		add(statsButton);
	}
	
	private void initEqPanels()
	{
		int ite = 0;
		eq = new ItemSlotPanel[10];
		String[] names = new String[10];
		String[] nazwy = 
				{
				"Helmet Slot",
				"Amulet Slot",
				"Backpack Slot",
				"Left Hand Slot",
				"Armor Slot",
				"Right Hand Slot",
				"Ring Slot",
				"Legs Slot",
				"Ammo Slot",
				"Boots Slot"
				};
		eq[ite] = new ItemSlotPanel(51,176,ItemSlotPanel.SLOT_HELMET,null);
		add(eq[ite]);
		eq[ite].setName(nazwy[ite]);
		eq[ite].setDrawBcg(false);
		ite++;
		
		eq[ite] = new ItemSlotPanel(14,190,ItemSlotPanel.SLOT_AMULET,null);
		add(eq[ite]);
		eq[ite].setName(nazwy[ite]);
		eq[ite].setDrawBcg(false);
		ite++;
		
		eq[ite] = new ItemSlotPanel(88,190,ItemSlotPanel.SLOT_BACKPACK,null);
		add(eq[ite]);
		eq[ite].setName(nazwy[ite]);
		eq[ite].setDrawBcg(false);
		ite++;
		
		eq[ite] = new ItemSlotPanel(14,227,ItemSlotPanel.SLOT_LEFTHAND,null);
		add(eq[ite]);
		eq[ite].setName(nazwy[ite]);
		eq[ite].setDrawBcg(false);
		ite++;
		
		eq[ite] = new ItemSlotPanel(51,213,ItemSlotPanel.SLOT_ARMOR,null);
		add(eq[ite]);
		eq[ite].setName(nazwy[ite]);
		eq[ite].setDrawBcg(false);
		ite++;
		
		eq[ite] = new ItemSlotPanel(88,227,ItemSlotPanel.SLOT_RIGHTHAND,null);
		add(eq[ite]);
		eq[ite].setName(nazwy[ite]);
		eq[ite].setDrawBcg(false);
		ite++;
		
		eq[ite] = new ItemSlotPanel(14,264,ItemSlotPanel.SLOT_RING,null);
		add(eq[ite]);
		eq[ite].setName(nazwy[ite]);
		eq[ite].setDrawBcg(false);
		ite++;
		
		eq[ite] = new ItemSlotPanel(51,250,ItemSlotPanel.SLOT_LEGS,null);
		add(eq[ite]);
		eq[ite].setName(nazwy[ite]);
		eq[ite].setDrawBcg(false);
		ite++;
		
		eq[ite] = new ItemSlotPanel(88,264,ItemSlotPanel.SLOT_AMMO,null);
		add(eq[ite]);
		eq[ite].setName(nazwy[ite]);
		eq[ite].setDrawBcg(false);
		ite++;
		
		eq[ite] = new ItemSlotPanel(51,287,ItemSlotPanel.SLOT_BOOTS,null);
		add(eq[ite]);
		eq[ite].setName(nazwy[ite]);
		eq[ite].setDrawBcg(false);
	}
	
	private void drawHpBar(Graphics2D g)
	{
		int hp = GameBoard.hero.getHp();
		int maxHp = GameBoard.hero.getMaxHp();
		float percOfLife = (float)hp/(float)maxHp;
		int hpBarWidth = (int) (125*percOfLife);
		String hpString = new String (String.valueOf(hp));
		int mana = GameBoard.hero.mana;
		int maxMana = GameBoard.hero.maxMana;
		float percOfMana = (float) 0.0;
		if(maxMana==0)
		{
			percOfMana = 1;
		}
		else percOfMana = (float)mana/(float)maxMana;
		int manaBarWidth = (int) (125*percOfMana);
		String manaString = new String (String.valueOf(mana));
		g.setColor(Color.BLACK);
		g.fillRect(0,0,180, 1080);
		/*
		for(int i=0;i<hpBarWidth;i++)
		{
			g.drawImage(hpBarFiller,10+i,120,1,12,null);
		}
		*/
		g.drawImage(hpBarFiller,10,120,hpBarWidth+1,12,null);
		g.drawImage(manaBarFiller,10,136,manaBarWidth+1,12,null);
		g.drawImage(statusScene,0,0,180, 1080, null);
		for(int i=0;i<10;i++)
		{
			eq[i].drawItem(g,0);
		}
		int yOffset = containersMinHeight;
		containersLock.lock();
		for(int i=0;i<containers.size();i++)
		{
			Container cont = containers.get(i);
			if(cont instanceof StatusPanel)
			{
				containers.get(i).draw(g,yOffset);
				yOffset+=containers.get(i).getHeight();
			}
		}
		containersLock.unlock();
		g.setColor(Color.BLACK);
		g.setFont(font);
		for(int i=-1;i<=1;i++)
		{
			for(int j=-1;j<=1;j++)
			{
				g.drawString(hpString,140+i,132+j);
				g.drawString(manaString,140+i,147+j);
			}
		}
		g.setColor(new Color(165,160,160));
		g.drawString(hpString, 140,132);
		g.drawString(manaString,140,147);
		
		statsButton.Draw(g);
	}
	
	public void draw(Graphics2D g)
	{
		Map.drawingChanges.lock();
		addFromWaiting();
		removeFromWaiting();
		drawHpBar(g);
		Map.drawingChanges.unlock();
	}
	
	private void addFromWaiting()
	{
		if(waitAddList.size()>0)
		{
			containersLock.lock();
			add(waitAddList.get(0));
			waitAddList.remove(0);
			revalidate();
			containersLock.unlock();
		}
	}
	
	private void removeFromWaiting()
	{
		if(waitRemoveList.size()>0)
		{
			containersLock.lock();
			remove(waitRemoveList.get(0));
			waitRemoveList.remove(0);
			revalidate();
			containersLock.unlock();
		}
	}

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); 
        drawHpBar((Graphics2D) g);
    }
    
	private ItemSlotPanel getSlot(MouseEvent e)
	{
		Point point = e.getPoint();
		point.translate(-1740,0);
		Component comp = getComponentAt(point);
		ItemSlotPanel slot = null;
		if(comp instanceof ItemSlotPanel)
		{
			slot = (ItemSlotPanel) getComponentAt(point);
		}
		else if(comp instanceof StatusPanel)
		{
			StatusPanel cont = (StatusPanel) getComponentAt(point);
			point.translate(0, -cont.getY());
			slot = cont.getSlotPanel(point, e);
		}
		return slot;
	}
	public MainObject getObject(MouseEvent e)
	{
		ItemSlotPanel slot = getSlot(e);
		if(slot==null) return null;
		else return slot.getItem();
	}
	
	private Component getComponent(Point point)
	{
		return getComponentAt(point);
	}
	
	public void mousePressed(MouseEvent e)
	{
		Point point = e.getPoint();
		point.translate(-1740, 0);
		Component comp = getComponent(point);
		if(comp instanceof ControlButton)
		{
			if(SwingUtilities.isLeftMouseButton(e))
			{
				((ControlButton) comp).clicked(e);
			}
		}
		else
		{
			ItemSlotPanel slot = getSlot(e);
			if(SwingUtilities.isLeftMouseButton(e))
			{
				if(slot!=null)
				{
					if(slot.hasItem()) GameBoard.itemOnMouse = slot.getItem();
				}
			}
			else if(SwingUtilities.isRightMouseButton(e))
			{
				if(slot!=null)
				{
					if(slot.hasItem())
					{
						Item item = slot.getItem();
						if(item instanceof Openable)
						{
							((Openable) item).open();
						}
					}
				}
			}
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{
		if(SwingUtilities.isLeftMouseButton(e))
		{
			if(GameBoard.itemOnMouse!=null)
			{
				ItemSlotPanel slot = getSlot(e);
				if(slot!=null)
				{
					Item item = (Item) GameBoard.itemOnMouse;
					item.addMeToSlot(slot);
				}
			}
		}
	}
	
	public void putItemIntoSlot(MouseEvent e, Item item)
	{
		item.addMeToSlot(getSlot(e));
	}
	
	
	private static int getHeight(int number)
	{
		int height = 0;
		for(int i=number;i<containers.size();i++)
		{
			height+=containers.get(i).getHeight();
		}
		return height;
	}
	
	public static void removeContainer(StatusPanel container)
	{
		int number = containers.indexOf(container);
		containersLock.lock();
		containers.remove(container);
		int offsetY = container.getHeight();
		for(int i=number;i<containers.size();i++)
		{
			Container element = containers.get(i);
			if(element instanceof StatusPanel)
			{
				StatusPanel cont = (StatusPanel) element;
				cont.setLocation(0, cont.getY() - offsetY);
			}
		}
		waitRemoveList.add(container);
		containersLock.unlock();
	}
	
	public static void addContainer(StatusPanel container)
	{
		if(containers.contains(container))
		{
			removeContainer(container);
		}
		else 
		{
			container.setBounds(0, containersMinHeight + getHeight(0), 180, container.getHeight());
			containersLock.lock();
			containers.add(container);
			waitAddList.add(container);
			containersLock.unlock();
		}
	}
	
}
