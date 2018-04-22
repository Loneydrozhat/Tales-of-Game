package utilities;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import boards.GameBoard;

public class MapTileListener extends JPanel
{
	private boolean rightClicked = false, leftClicked = false;
	private Position position;
	private boolean ifExited;
	public MapTileListener(Position position, int x, int y)
	{
		super(null);
		setOpaque(true);
		setBounds(x,y,64,64);
		this.position = position;
	}
	
	public void setListenerLocation(int x, int y)
	{
		setLocation(0,0);
	}
	public Position getPosition()
	{
		return position.clone();
	}
}
