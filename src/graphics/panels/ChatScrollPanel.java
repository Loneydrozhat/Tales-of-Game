package graphics.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JPanel;

public class ChatScrollPanel extends ScrollPanel
{
	private ArrayList <String> chatHistory;
	protected static Font font;
	protected static Color color;
	private BufferedImage scene;
	private Lock gettingTextLock = new ReentrantLock();
	private int originalHeight;
	private JPanel bcg;
	private boolean changed = false;
	public ChatScrollPanel(int x, int y, int width, int height, PanelSetPref client) {
		super(x, y, width, height, client);
		chatHistory = new ArrayList<String>();
		font = new Font("Arial",Font.BOLD, 12);
		color = Color.YELLOW;
		scene = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
		this.originalHeight = height;
		this.height = 1;
		changeScene();
		getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
	        public void adjustmentValueChanged(AdjustmentEvent e) {  
	        	if(changed)
	        	{
	        		changed = false;
	        		e.getAdjustable().setValue(e.getAdjustable().getMaximum());
	        	}
	        }
	    });
		//revalidate();
		// TODO Auto-generated constructor stub
	}
	
	public void addSomething(String text)
	{
		gettingTextLock.lock();
		chatHistory.add(text);
		if(chatHistory.size()>50)
		{
			chatHistory.remove(50);
		}
		else
		{
			height+=15;
		}
		client.changeScene(scene);
		if(height<originalHeight)
		client.changeSize(width, originalHeight);
		else client.changeSize(width, height);
		client.setSize(new Dimension(width, height));
		changed = true;
		changeScene();
		gettingTextLock.unlock();
	}
	
	public void changeScene()
	{
		int height = this.height;
		if(height<originalHeight) height = originalHeight;
		scene = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) scene.getGraphics();
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, width, height);
		g.setFont(font);
		for(int i=0;i<chatHistory.size();i++)
		{
			String text = chatHistory.get(chatHistory.size()-1-i);
			int textHeight = 5;
			g.setColor(Color.BLACK);
			g.drawString(text, 0-1, height-i*15-1-textHeight);
			g.drawString(text, 0-1, height-i*15+1-textHeight);
			g.drawString(text, 0+1, height-i*15-1-textHeight);
			g.drawString(text, 0+1, height-i*15+1-textHeight);
			g.drawString(text, 0, height-i*15-1-textHeight);
			g.drawString(text, 0, height-i*15+1-textHeight);
			g.drawString(text, 0-1, height-i*15-textHeight);
			g.drawString(text, 0+1, height-i*15-textHeight);
			g.setColor(color);
			g.drawString(text, 0, height-i*15-textHeight);
		}
	}

	
	public void draw()
	{
		gettingTextLock.lock();
		int height = this.height;
		if(height<originalHeight) height = originalHeight;
		Graphics2D secondG = (Graphics2D) client.getGraphics();
		secondG.drawImage(scene,0,0,width,height,null);
		gettingTextLock.unlock();
	}
}
