package graphics.panels.statusPanels.controlButtons;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import boards.GameBoard;
import graphics.panels.HeroStatus;
import graphics.panels.statusPanels.StatisticsPanel;

public class StatsButton extends JPanel implements ControlButton
{
	private static BufferedImage bcg = null;
	private String text;
	private int x;
	private int y;
	private static Font font;
	private static StatisticsPanel stats;
	public StatsButton(int x, int y, String text)
	{
		super(null);
		setOpaque(false);
		if(bcg==null)
		{
			try {
				bcg = ImageIO.read(getClass().getResourceAsStream("/statusBcg.png")).getSubimage(0, 0, 45, 25);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(font == null)
		{
			font = new Font("Ariel",Font.BOLD,13);
		}
		this.text = text;
		this.x = x;
		this.y = y;
		setBounds(x,y,40,20);
		stats = new StatisticsPanel(200);
	}
	
	public void Draw(Graphics2D g)
	{
		g.drawImage(bcg,x,y,45,25,null);
		g.setFont(font);
		g.setColor(Color.BLACK);
		int newX = x+7;
		int newY = y+17;
		for(int i=-2;i<=2;i++)
		{
			for(int j=-2;j<=2;j++)
			{
				g.drawString(text, newX+i, newY+j);
			}
		}
		g.setColor(Color.WHITE);
		g.drawString(text, newX, newY);
	}
	
	@Override
	public void clicked(MouseEvent e) {
		HeroStatus.addContainer(stats);
	}

}
