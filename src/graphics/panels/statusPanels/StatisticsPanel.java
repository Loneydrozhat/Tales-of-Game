package graphics.panels.statusPanels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphics.panels.statusPanels.controlButtons.ExitButton;
import graphics.panels.statusPanels.controlButtons.ScrollButton;
import objects.alive.Hero;

public class StatisticsPanel extends StatusPanel
{

	private static Font font = new Font("Ariel",Font.BOLD,12); 
	
	public StatisticsPanel(int height)
	{
		super(height);
		scene = new BufferedImage(165,height-13,BufferedImage.TYPE_INT_RGB);
		scrollButton = new ScrollButton(this);
		exitButton.setBounds(180-15,0,13,13);
		hidden = 50;
		scrollHeight = ((float)(height- 15 - hidden));
		percent = ((float)(height-hidden))/height;
		scrollHeight*=percent;
		scrollButton.setHeight((int)scrollHeight);
		add(exitButton);
		add(scrollButton);
	}
	
	private void drawString(int x, int y, Graphics2D g, String text, Color color)
	{
		g.setFont(font);
		g.setColor(Color.black);
		int border = 1;
		for(int i=-border;i<=border;i++)
		{
			for(int j=-border;j<=border;j++)
			{
				g.drawString(text, x+i, y+j);
			}
		}
		g.setColor(color);
		g.drawString(text, x, y);
	}
	
	@Override
	public void draw(Graphics2D g, int yOffset) {
		lockScroll.lock();
		offsetY = yOffset;
		Graphics2D gScene = (Graphics2D) scene.getGraphics();
		for(int bcgHeight = 0;bcgHeight<height;bcgHeight+=panelBcg.getHeight())
		{
			gScene.drawImage(panelBcg, 0, bcgHeight,panelBcg.getWidth(),panelBcg.getHeight(), null);
		}
		int width = g.getFontMetrics().stringWidth(String.valueOf(Hero.lvl));
		drawString(5,20,gScene,"Level", Color.white);
		drawString(160-width-5,20,gScene,String.valueOf(Hero.lvl),Color.white);
		
		g.drawImage(scene.getSubimage(0, scrollOffset, scene.getWidth(), height-hidden-15),0,yOffset+15,scene.getWidth(),height-hidden-15,null);
		
		for(Component element : getComponents())
		{
			if( element instanceof ExitButton)
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
