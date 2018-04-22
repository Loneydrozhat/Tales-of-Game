package objects.notAlive.items.itemTypes;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import abilities.Openable;
import boards.GameBoard;
import exceptions.CannotMoveItemException;
import exceptions.ReputItemException;
import graphics.panels.HeroStatus;
import graphics.panels.PanelSetPref;
import graphics.panels.ScrollPanel;
import graphics.panels.statusPanels.ContainerPanel;
import graphics.panels.statusPanels.ItemSlotPanel;
import objects.notAlive.items.Item;

public class Bag extends Item implements Openable {

	private ItemSlotPanel itemsInBag[];
	private ContainerPanel panel;
	public static boolean added = false;
	private int spaceQuantity;
	
	public Bag()
	{
		
	}
	
	public Bag(BufferedImage image, String desc, String params) {
		super(image, desc, params);
		this.spaceQuantity = 8;
		itemsInBag = new ItemSlotPanel[spaceQuantity];
		int right = 0;
		int down = 0;
		int spaceRight = ContainerPanel.rightSpace;
		int spaceDown = ContainerPanel.downSpace;
		int offsetRight = 0;
		int height = 0;
		for(int i=0;i<spaceQuantity;i++)
		{
			itemsInBag[i] = new ItemSlotPanel(10+offsetRight + right*32+right*spaceRight,15+down*32+down*spaceDown,ItemSlotPanel.SLOT_EVERYTHING, this);
			//Item item = new Item(GameBoard.items.get(0),"Sword",Item.ONE_HANDED_SWORD);
			//item.addMeToSlot(itemsInBag[i]);
			height = down*32+down*spaceDown;
			right+=1;
			if(right%4==0)
			{
				right = 0;
				down++;
			}
		}
		panel = new ContainerPanel(height+32, this);
	}
	
	public int getSlotsQuantity()
	{
		return this.spaceQuantity;
	}
	
	public void addItemToMe(Item item)
	{
		boolean hasSpace = false;
		for(int i=0;i<spaceQuantity;i++)
		{
			if(!itemsInBag[i].hasItem())
			{
				hasSpace = true;
				item.addMeToSlot(itemsInBag[i]);
				throw new ReputItemException();
			}
		}
		if(!hasSpace) throw new CannotMoveItemException();
	}
	public ItemSlotPanel[] getSlots()
	{
		return itemsInBag;
	}
	
	@Override
	public void open() 
	{
		panel.setBounds(0, 0, 180, 32*2);
		for(int i=0;i<spaceQuantity;i++)
		{
			panel.add(itemsInBag[i]);
		}
		HeroStatus.addContainer(panel);
	}

	@Override
	public boolean checkInstance(Item item) {
		return item instanceof Bag;
	}
}
