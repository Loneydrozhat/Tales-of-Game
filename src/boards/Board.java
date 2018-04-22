package boards;
import java.awt.event.KeyEvent;

public abstract class Board 
{
	protected BoardHandler handler;
	public abstract void update();
	public abstract void draw();
	public abstract void keyPressed(KeyEvent ev);
	public abstract void keyReleased(KeyEvent ev);
}
