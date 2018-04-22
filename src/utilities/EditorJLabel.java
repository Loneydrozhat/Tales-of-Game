package utilities;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import boards.GameBoard;
import objects.MainObject;

@SuppressWarnings("serial")
public class EditorJLabel extends JLabel implements MouseListener {

	private MainObject tile;
	private String desc;
	public EditorJLabel(int xAxis, MainObject object)
	{
		ImageIcon image = new ImageIcon(object.getImage());
		setIcon(image);
		setText(object.getDesc());
		setBounds(0, xAxis, (int)(192*1.5), image.getIconHeight());
		setVerticalTextPosition(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.LEFT);
		tile = object;
		addMouseListener(this);
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		String text = getText();
		desc = text;
		text += " Active";
		setText(text);
		if(GameBoard.focusedObject!=null) GameBoard.focusedObject.deactivate();
		GameBoard.focusedObject = this;
		
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void deactivate()
	{
		setText(tile.getDesc());
	}
	
	public MainObject getObject()
	{
		return this.tile;
	}

}
