package graphics.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelSetPref extends JPanel
{
	private int sizeX = 1;
	private int sizeY = 1;
	private BufferedImage scene;
	public PanelSetPref()
	{
		super(null);
	}
	
	public void changeSize(int xSize, int ySize)
	{
		this.sizeX = xSize;
		this.sizeY = ySize;
		this.setSize(xSize, ySize);
	}
	
	public void changeScene(BufferedImage scene)
	{
		this.scene = scene;
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(sizeX,sizeY);
	}
	@Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); 
        Graphics2D a = (Graphics2D)g;
        a.setColor(Color.RED);
        a.drawImage(scene,0,0,sizeX,sizeY,null);
    }
}
