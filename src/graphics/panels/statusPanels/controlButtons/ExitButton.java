package graphics.panels.statusPanels.controlButtons;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import graphics.panels.HeroStatus;
import graphics.panels.statusPanels.ContainerPanel;
import graphics.panels.statusPanels.StatusPanel;
import objects.MainObject;

public class ExitButton extends JPanel implements ControlButton
{
	private static BufferedImage icon = null;
	public ExitButton() {
		
		if(icon == null)
		{
			try {
				BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/panelControls.png"));
				icon = image.getSubimage(0, 0, 13, 13);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		setOpaque(false);
	}
	
	public void draw(Graphics2D g, int y)
	{
		g.drawImage(icon,180-15,y,13,13,null);
	}

	@Override
	public void clicked(MouseEvent e) {
		StatusPanel cont = (StatusPanel) getParent();
		HeroStatus.removeContainer(cont);
	}

}
