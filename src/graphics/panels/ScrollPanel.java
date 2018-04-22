package graphics.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ScrollPanel extends JScrollPane
{
	protected PanelSetPref client;
	protected int height;
	protected int width;
	public ScrollPanel(int x, int y, int width, int height, PanelSetPref client)
	{
		super(client);
		this.height = 0;
		this.width = width;
		this.client = client;
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollRectToVisible(new Rectangle(width,height));
		setBounds(x, y, width, height);
	}
	
	public void addSomething(Component something)
	{
		for(Component comp : client.getComponents())
		{
			comp.setLocation(comp.getX(), comp.getY()-something.getHeight());
		}
		client.add(something);
		height+=something.getHeight();
		client.setPreferredSize(new Dimension(width,height));
		repaint();
	}
}
