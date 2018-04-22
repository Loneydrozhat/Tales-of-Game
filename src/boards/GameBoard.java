package boards;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import abilities.Openable;
import exceptions.CannotWalkException;
import exceptions.SpellExhaustException;
import gamePanels.GamePanel;
import graphics.Map;
import graphics.panels.ChatScrollPanel;
import graphics.panels.HeroStatus;
import graphics.panels.PanelSetPref;
import graphics.panels.statusPanels.ItemSlotPanel;
import graphics.panels.statusPanels.controlButtons.ControlButton;
import graphics.panels.statusPanels.controlButtons.ScrollButton;
import objects.MainObject;
import objects.alive.Alive;
import objects.alive.Hero;
import objects.alive.monsters.Mob;
import objects.alive.monsters.pwMob.Vine;
import objects.alive.npcs.Npc;
import objects.notAlive.items.*;
import objects.notAlive.items.itemTypes.Bag;
import objects.notAlive.items.itemTypes.Boots;
import objects.notAlive.items.itemTypes.OneHandedWeapon;
import objects.notAlive.textObjects.InfoText;
import utilities.Chat;
import utilities.EditorJLabel;
import utilities.MapObjectList;
import utilities.Position;
import utilities.Vector;

public class GameBoard extends Board implements Runnable, MouseInputListener
{
	private Map map;
	private boolean upArrowKeyPress = false;
	private boolean downArrowKeyPress = false;
	private boolean rightArrowKeyPress = false;
	private boolean leftArrowKeyPress = false;
	private boolean downLeftKeyPress = false;
	private boolean downRightKeyPress = false;
	private boolean upLeftKeyPress = false;
	private boolean upRightKeyPress = false;
	private boolean rightShiftKeyPress = false;
	private boolean lifted = true;
	public static volatile int xOffset;
	public static volatile int yOffset;
	public static boolean walk = false;
	public static Hero hero;
	public static Lock movements;
	
	
	private Graphics2D  gMap;
	private BufferedImage statusScene;
	private Graphics2D gStatus;
	private BufferedImage mapScene;
	
	
	public static Semaphore keyManagement;
	private JPanel mainPanel;
	private PanelSetPref chatTextArea;
	private JLabel writingBufor;
	private String actualBufor;
	private ArrayList <String> bufor;
	private JPanel bcgMapPanelLeft;
	private JPanel bcgMapPanelRight;
	private JPanel editingPanel;
	private HeroStatus heroStatusPanel;
	private int chatIterator;
	private static InfoText infoText;
	private static boolean godMod = false;
	public static EditorJLabel focusedObject;
	public static MapObjectList canineMobs;
	public static MapObjectList npcs;
	public static ArrayList<BufferedImage> outfits;
	public static ArrayList<BufferedImage> mobs;
	private JScrollPane editingScrollPane;
	//private JPanel statusPanelReplacement;
	public static ChatScrollPanel chatScroll;
	public static ArrayList<BufferedImage> itemGraphics;
	public static ArrayList<Item>items;
	public static MainObject itemOnMouse;
	public static ControlButton buttonOnMouse;
	private static int mapLeftBorderX;
	private static int mapRightBorderX;
	//private static Position lastDemolish;
	//private static boolean moustRightClick;
	//private MouseSceneListener mHandler;
	
	public GameBoard(BoardHandler handler, GamePanel panel)
	{
		mapLeftBorderX = (int)((1920-180)/2 - 32*17);
		infoText = new InfoText();
		this.handler = handler;
		loadOutfits();
		loadItemGraphics();
		hero = new Hero(1);
		editingPanel = new JPanel(null);
		editingPanel.setBounds(1920-180, 0, 180, 1080);
		map = new Map();
		itemOnMouse = null;
		xOffset = 13;
		yOffset = 13;
		movements = new ReentrantLock();
		keyManagement = new Semaphore(0);
		mainPanel = panel;
		
		mapScene = new BufferedImage(1920, 1080,BufferedImage.TYPE_INT_RGB);
		gMap = (Graphics2D) mapScene.getGraphics();
		statusScene = new BufferedImage(180,1080,BufferedImage.TYPE_INT_RGB);
		gStatus = (Graphics2D) statusScene.getGraphics();
		heroStatusPanel = new HeroStatus();
		bcgMapPanelLeft = new JPanel(null);
		mapRightBorderX =(int)((1920-180)/2 - 32*17 +64*17);
		bcgMapPanelLeft.setBounds(0, 0, mapLeftBorderX, 64*13);
		bcgMapPanelLeft.setBackground(Color.gray);
		bcgMapPanelRight = new JPanel(null);
		bcgMapPanelRight.setBounds(mapRightBorderX, 0, mapLeftBorderX, 64*13);
		bcgMapPanelRight.setBackground(Color.gray);
		mainPanel.add(bcgMapPanelLeft);
		mainPanel.add(bcgMapPanelRight);
		mainPanel.add(map);
		mainPanel.add(heroStatusPanel);
		initChatPanel();
		loadMobs();
		loadNpcs();
		initEditingPanel();
		loadItemsToMap();
		mainPanel.addMouseListener(this);
		mainPanel.addMouseMotionListener(this);
		mainPanel.revalidate();
		new Thread(this, "KeyManagement").start();
	}
	
	private void loadItemsToMap()
	{
		items = new ArrayList<Item>();
		File file = new File("ItemFiles//ItemList.ml");
		Scanner input = null;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String inputLine = input.nextLine();
		char[] inputLineTable = inputLine.toCharArray();
		while(inputLineTable[0]!='/')
		{
			inputLine = input.nextLine();
			inputLineTable = inputLine.toCharArray();
		}
		while(input.hasNextLine())
		{
			inputLine = input.nextLine();
			//System.out.println(inputLine);
			inputLineTable = inputLine.toCharArray();
			
			
			int start = 0;
			int end = start;
			while(inputLineTable[end]!=',')
			{
				end++;
			}
			int graphicsId=Integer.parseInt(inputLine.substring(start, end));
			
			
			start = end+1;
			end = start;
			while(inputLineTable[end]!=',')
			{
				end++;
			}
			String desc = inputLine.substring(start, end);
			
			
			start = end+1;
			end = start;
			while(inputLineTable[end]!=',')
			{
				end++;
			}
			boolean equipable;
			if(Integer.parseInt(inputLine.substring(start, end))==1)
			{
				equipable = true;
			}
			else 
			{
				equipable = false;
			}
			
			start = end+1;
			end = start;
			while(inputLineTable[end]!=',')
			{
				end++;
			}
			String object = inputLine.substring(start, end);
			
			String params;
			start = end+1;
			end = start;
			while(inputLineTable[end]!=',')
			{
				end++;
			}
			params = inputLine.substring(start, end);
			
			
			Class klasa = null;
			try 
			{
				object = "objects.notAlive.items.itemTypes." + object;
				klasa = Class.forName(object);
				try 
				{
					Constructor cons = null;
					try {
						Class[] args = new Class[3];
						args[0] = BufferedImage.class;
						args[1] = String.class;
						args[2] = String.class;
						cons = klasa.getDeclaredConstructor(args);
					} catch (NoSuchMethodException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						
						Item item = (Item) cons.newInstance(itemGraphics.get(graphicsId),desc,params);
						items.add(item);
					} catch (IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//item = (Item) cons.newInstance(itemGraphics.get(graphicsId),desc));
				} 
				catch (InstantiationException | IllegalAccessException e) 
				{
					e.printStackTrace();
				}
			} 
			catch (ClassNotFoundException e) 
			{
			}
		}
		
		
		Item item = (Item) items.get(0).clone();
		item.addMeToMap(new Position(22,15));	
		item = (Item) items.get(1).clone();
		item.addMeToSlot(HeroStatus.eq[Item.BACKPACK]);
		item = items.get(0).clone();
		item.addMeToSlot(HeroStatus.eq[Item.ONE_HANDED_SWORD]);
		item = items.get(0).clone();
		item.addMeToMap(new Position(22,17));	
		item = (Item) items.get(1).clone();
		item.addMeToMap(new Position(22,16));
		/*
		item = new Bag(itemGraphics.get(1),new String ("Bag"), Item.BACKPACK,11);
		item.addMeToMap(new Position(23,15));

		item = new Bag(itemGraphics.get(1),new String ("Bag"), Item.BACKPACK,5);
		item.addMeToMap(new Position(23,18));
		
		item = new OneHandedWeapon(itemGraphics.get(0),new String ("Sword"));
		item.addMeToSlot(HeroStatus.eq[HeroStatus.LEFT_HAND]);
		
		item = new Bag(itemGraphics.get(1),new String ("Bag"), Item.BACKPACK,16);
		item.addMeToSlot(HeroStatus.eq[HeroStatus.BACKPACK]);
		
		item = new OneHandedWeapon(itemGraphics.get(0),new String ("Sword"));
		item.addMeToMap(new Position(22,16));	
		*/
	}
	
	private void loadItemGraphics()
	{
		itemGraphics = new ArrayList <BufferedImage>();
		npcs = new MapObjectList();
		String src = "/Items.png";
		BufferedImage resources = null;
		try {
			resources = ImageIO.read(getClass().getResourceAsStream(src));
			int cols= resources.getWidth()/32,rows = resources.getHeight()/32;
			for(int i=0; i<rows; i++)
			{
				for(int j=0; j<cols; j++)
				{
					BufferedImage Image = resources.getSubimage(j*32, i*32, 32, 32);
					itemGraphics.add(Image);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Map.addElementToMap(new BodyArmorItem(items.get(0),new Position(24,15),new String ("Sword")), new Position(24,15));
		
		
	}
	
	private void loadNpcs()
	{
		npcs = new MapObjectList();
		npcs.add(new Npc(0,new Position(29,16)));
	}
	
	private void loadOutfits()
	{
		outfits = new ArrayList<BufferedImage>();
		npcs = new MapObjectList();
		String src = "/OutfitSprites.png";
		BufferedImage resources = null;
		try {
			resources = ImageIO.read(getClass().getResourceAsStream(src));
			int cols= resources.getWidth()/64,rows = resources.getHeight()/64;
			for(int i=0; i<rows; i++)
			{
				for(int j=0; j<cols; j++)
				{
					BufferedImage Image = resources.getSubimage(j*64, i*64, 64, 64);
					outfits.add(Image);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadMobs()
	{
		canineMobs = new MapObjectList();
		mobs = new ArrayList <BufferedImage>();
		String src = "/Canines.png";
		BufferedImage resources = null;
		try {
			resources = ImageIO.read(getClass().getResourceAsStream(src));
			int cols= resources.getWidth()/64,rows = resources.getHeight()/64;
			for(int i=0; i<rows; i++)
			{
				for(int j=0; j<cols; j++)
				{
					BufferedImage mobImage = resources.getSubimage(j*64, i*64, 64, 64);
					mobs.add(mobImage);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		int plusX=30, plusY=0;
		canineMobs.add(new Mob(0,new Position(16+plusX,13)));
		canineMobs.add(new Mob(0,new Position(14+plusX,20)));
		canineMobs.add(new Mob(0,new Position(16+plusX,12)));
		canineMobs.add(new Mob(0,new Position(17+plusX,13)));
		canineMobs.add(new Mob(0,new Position(18+plusX,14)));
		canineMobs.add(new Mob(0,new Position(17+plusX,16)));
		canineMobs.add(new Mob(0,new Position(14+plusX,16)));
		canineMobs.add(new Mob(0,new Position(16+plusX,14)));
		canineMobs.add(new Mob(0,new Position(19+plusX,12)));
		new Vine(mobs.get(0),new Position(23,36),3);
		new Vine(mobs.get(0),new Position(16,39),2);
		new Vine(mobs.get(0),new Position(16,23),1);
	}
	
	private void initChatPanel()
	{
		chatTextArea = new PanelSetPref();
		chatTextArea.setPreferredSize(new Dimension((int)(192*8.5),0));
		chatTextArea.setLocation(0, 0);
		chatScroll = new ChatScrollPanel(0, 64*13+1, 1920-180, 1080-64*13-1-25,chatTextArea);
		writingBufor = new JLabel();
		bufor = new ArrayList<String>();
		bufor.add("");
		actualBufor = new String("");
		chatIterator = 1;
		writingBufor.setBounds(0,1080-24,(int)(1920-180),24);
		mainPanel.add(chatScroll, BorderLayout.CENTER);
		mainPanel.add(writingBufor, BorderLayout.CENTER);
		chatScroll.revalidate();
		//chatScroll.repaint();
		//mainPanel.repaint();
		mainPanel.revalidate();
	}
	
	private void initEditingPanel()
	{
		Map.drawingChanges.lock();
		editingScrollPane = new JScrollPane(editingPanel);
		editingPanel.setLocation(0, 0);
		int size = Map.listOfTiles.size();
		int xAxis = 0;
		for(int i=size-1;i>=0;i--)
		{
			MainObject element = Map.listOfTiles.getObject(i);
			EditorJLabel label = new EditorJLabel(xAxis, element);
			xAxis += element.getImage().getHeight();
			editingPanel.add(label);
		}
		editingPanel.setPreferredSize(new Dimension(180, xAxis));
		editingScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editingScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		editingScrollPane.scrollRectToVisible(new Rectangle((int)(192*1.5), 1080));
		editingScrollPane.setBounds(1920-180,0,180,1080);
		Map.drawingChanges.unlock();
	}

	
	@Override
	public void update() 
	{
	}

	@Override
	public void draw() 
	{
		int xMoving=0;
		int yMoving=0;
		
		map.draw(xOffset,yOffset,gMap);
		infoText.draw(gMap);
		heroStatusPanel.draw(gStatus);
		chatScroll.draw();
		Graphics gMap = map.getGraphics();
		Graphics2D gStatus = (Graphics2D) heroStatusPanel.getGraphics();
		gMap.drawImage(mapScene,0,0,1920,1080,null);
		if(gStatus!=null)
		{
			gStatus.drawImage(statusScene,0,0,180,1080,null);
			gStatus.dispose();
		}
		gMap.dispose();

		bcgMapPanelLeft.repaint();
		bcgMapPanelRight.repaint();
	}

	private void addToBufor(char code)
	{
		actualBufor+=code;
		writingBufor.setText(actualBufor);
	}
	
	private void removeFromBufor()
	{
		if(actualBufor.length()>0)
		{
			actualBufor = actualBufor.substring(0, actualBufor.length()-1);
			writingBufor.setText(actualBufor);
		}
	}
	
	private void handleCommand(int commandType)
	{
		if(commandType==Chat.COMMAND_GODMOD)
		{
			if(!godMod)
			{
				godMod = true;
				mainPanel.add(editingScrollPane);
				editingScrollPane.revalidate();
				//editingScrollPane.repaint();
				mainPanel.remove(heroStatusPanel);
				//mainPanel.remove(listener);
				//listener = new MapTileListener(1920-180);
				//mainPanel.add(listener);
				//lastDemolish = new Position(-1,-1);
			}
			else
			{
				godMod = false;
				mainPanel.add(heroStatusPanel);
				mainPanel.remove(editingScrollPane);
				//mainPanel.remove(listener);
				//listener = new MapTileListener(1920);
				//mainPanel.add(listener);
			}
		}
		else if(commandType == Chat.COMMAND_PUT)
		{
			if(godMod)
			{
				Position position = hero.getPosition();
				System.out.println("Dodaje: " + focusedObject);
				//Map.addElement(position.getXPosition(), position.getYPosition(), focusedObject.getObject());
			}
		}
		else if(commandType == Chat.COMMAND_TAKE)
		{
			if(godMod)
			{
				Position position = hero.getPosition();
				Map.removeSecond(position.getXPosition(), position.getYPosition());
			}
		}
		else if(commandType == Chat.COMMAND_LEVELUP)
		{
			hero.lvlUp();
		}
		else if(commandType == Chat.COMMAND_SAVE)
		{
			if(godMod)
			{
				Map.saveMap();
			}
		}
	}
	
	private void handleSpell(int spellType)
	{
		if(spellType == Chat.SPELL_EXURA)
		{
			try
			{
				hero.exura();
			}
			catch (SpellExhaustException e)
			{
				System.out.println("You are exhausted");
			}
		}
		else if(spellType==Chat.SPELL_HASTE)
		{
			hero.haste();
		}
	}
	
	private void sendToChat()
	{
		if(actualBufor.length()>0)
		{
			int inputType = Chat.checkInput(actualBufor);
			if(inputType==Chat.SPELL)
			{
				handleSpell(Chat.getSpellType());
			}
			else if(inputType == Chat.COMMAND)
			{
				handleCommand(Chat.getCommandType());
			}
			else if(inputType == Chat.TEXT)
			{
				hero.sendMessage(actualBufor);
			}
			bufor.add(actualBufor);
			actualBufor = "";
			chatIterator=bufor.size();
			writingBufor.setText(actualBufor);
		}
	}
	
	private void getLastMessage()
	{
		if(chatIterator>1)
		{
			actualBufor = bufor.get(--chatIterator);
			writingBufor.setText(actualBufor);
		}
	}
	
	private void getNextMessage()
	{
		if(chatIterator<bufor.size()-1)
		{
			actualBufor = bufor.get(++chatIterator);
			writingBufor.setText(actualBufor);
		}
	}
	
	public void keyPressed(KeyEvent ev) 
	{
		if(ev.getKeyCode()==KeyEvent.VK_SHIFT)
		{
			rightShiftKeyPress = true;
		}
		if(ev.getKeyCode()==KeyEvent.VK_DOWN) 
		{
			if(rightShiftKeyPress)
			{
				if(!lifted) getNextMessage();
			}
			else downArrowKeyPress=true;
		}
		else if(ev.getKeyCode()==KeyEvent.VK_UP)
		{
			if(rightShiftKeyPress)
			{
				if(lifted)getLastMessage();
			}
			else upArrowKeyPress=true;
		}
		else if(ev.getKeyCode()==KeyEvent.VK_LEFT)
		{
			leftArrowKeyPress=true;
		}
		else if(ev.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			rightArrowKeyPress=true;
		}
		else if(ev.getKeyCode()==KeyEvent.VK_MINUS)
		{
			hero.reduceLife(20);
		}
		else if(ev.getKeyCode()==KeyEvent.VK_EQUALS)
		{
			hero.healLife(30);
		}
		else if(ev.getKeyCode()==35)
		{
			downLeftKeyPress = true;
		}
		else if(ev.getKeyCode()==34)
		{
			downRightKeyPress = true;
		}
		else if(ev.getKeyCode()==36)
		{
			upLeftKeyPress = true;
		}
		else if(ev.getKeyCode()==33)
		{
			upRightKeyPress = true;
		}
		else if(ev.getKeyCode() == KeyEvent.VK_F1)
		{
			actualBufor = "exura";
			sendToChat();
		}
		else if(ev.getKeyCode()>='A'&&ev.getKeyCode()<='z')
		{
			addToBufor(ev.getKeyChar());
		}
		else if(ev.getKeyCode()==KeyEvent.VK_SPACE)
		{
			addToBufor(' ');
		}
		else if(ev.getKeyCode()==KeyEvent.VK_SLASH)
		{
			addToBufor('/');
		}
		else if(ev.getKeyCode()==KeyEvent.VK_BACK_SPACE)
		{
			removeFromBufor();
		}
		else if(ev.getKeyCode()==KeyEvent.VK_ENTER)
		{
			sendToChat();
		}
		else	
		{
			//System.out.println(ev.getKeyCode());
		}
		if(lifted)
		{
			lifted = false;
			//System.out.println("Pressed Key");
			keyManagement.release();
		}
	}
	
	public void keyReleased(KeyEvent ev) 
	{
		if(ev.getKeyCode()==KeyEvent.VK_SHIFT)
		{
			rightShiftKeyPress = false;
		}
		if(ev.getKeyCode()==KeyEvent.VK_DOWN) 
		{
			downArrowKeyPress=false;
		}
		else if(ev.getKeyCode()==KeyEvent.VK_UP) 
		{
			upArrowKeyPress=false;
		}
		else if(ev.getKeyCode()==KeyEvent.VK_RIGHT) 
		{
			rightArrowKeyPress=false;
		}
		else if(ev.getKeyCode()==KeyEvent.VK_LEFT) 
		{
			leftArrowKeyPress=false;
		}
		else if(ev.getKeyCode()==35)
		{
			downLeftKeyPress = false;
		}
		else if(ev.getKeyCode()==34)
		{
			downRightKeyPress = false;
		}
		else if(ev.getKeyCode()==36)
		{
			upLeftKeyPress = false;
		}
		else if(ev.getKeyCode()==33)
		{
			upRightKeyPress = false;
		}
	}
	
	private void moveHero(int x, int y)
	{
		walk = true;
		try
		{
			Map.drawingChanges.lock();
			if(!godMod) Map.checkPassable(xOffset+x+9, yOffset+y+6);
			Map.drawingChanges.unlock();
			//System.out.println("Can Walk In");
			hero.moveHero(x, y);
		}
		catch (CannotWalkException e)
		{
			Map.drawingChanges.unlock();
			//System.out.println("Cannot Walk In");
			addOffset(new Vector(0,0));
		}
	}
	public static void addOffset(Vector vector)
	{
		Map.drawingChanges.lock();
		GameBoard.xOffset+=vector.getXVector();
		GameBoard.yOffset+=vector.getYVector();
		Map.drawingChanges.unlock();
		walk = false;
		//System.out.println("Ended Moving Thread");
		keyManagement.release();
	}
	@Override
	public void run() {
		
		while(true)
		{
			try {
				//System.out.println("Gonna sleep");
				keyManagement.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			boolean walkKey = false;
			int x=0,y=0;
			if(downArrowKeyPress)
			{
				walkKey = true;
				y=1;
			}
			else if(upArrowKeyPress)
			{
				walkKey = true;
				y=-1;
			}
			else if(leftArrowKeyPress)
			{
				walkKey = true;
				x=-1;
			}
			else if(rightArrowKeyPress)
			{
				walkKey = true;
				x=1;
			}
			else if(downLeftKeyPress)
			{
				walkKey = true;
				x=-1;
				y=1;
			}
			else if(downRightKeyPress)
			{
				walkKey = true;
				x=1;
				y=1;
			}
			else if(upLeftKeyPress)
			{
				walkKey = true;
				x=-1;
				y=-1;
			}
			else if(upRightKeyPress)
			{
				walkKey = true;
				x=1;
				y=-1;
			}
			else
			{
				lifted = true;
			}
			if(walkKey)
			{
				if(!walk)
				{
					//System.out.println("Gonna Move!");
					moveHero(x,y);
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		if(!godMod)
		{
			Point point = e.getPoint();
			Component comp = mainPanel.getComponentAt(point);
			if(comp instanceof Map)
			{
				((Map) comp).mousePressed(e);
			}
			else if(comp instanceof HeroStatus)
			{
				heroStatusPanel.mousePressed(e);
			}
		}
		else
		{
			Point point = e.getPoint();
			Component comp = mainPanel.getComponentAt(point);
			if(comp instanceof Map)
			{
				if(SwingUtilities.isLeftMouseButton(e)) map.addTerrainToMap(focusedObject.getObject(), e);
				else if(SwingUtilities.isRightMouseButton(e)) map.deleteTerrainFromMap(e);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		Point point = e.getPoint();
		Component comp = mainPanel.getComponentAt(point);
		if(comp instanceof Map)
		{
			((Map) comp).mouseReleased(e);
		}
		else if(comp instanceof HeroStatus)
		{
			((HeroStatus) comp).mouseReleased(e);
		}
		itemOnMouse = null;
		buttonOnMouse = null;
		if(godMod)
		{
			Map.lastXModified = 0;
			Map.lastYModified = 0;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		if(godMod)
		{
			Point point = e.getPoint();
			Component comp = mainPanel.getComponentAt(point);
			if(comp instanceof Map)
			{
				if(SwingUtilities.isLeftMouseButton(e)) map.addTerrainToMap(focusedObject.getObject(), e);
				else if(SwingUtilities.isRightMouseButton(e)) map.deleteTerrainFromMap(e);
			}
		}
		else if(buttonOnMouse!=null)
		{
			if(buttonOnMouse instanceof ScrollButton)
			{
				((ScrollButton) buttonOnMouse).drag(e);
			}
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) 
	{
	}
	
}
