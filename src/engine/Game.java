package engine;

import javax.swing.JFrame;

import gamePanels.GamePanel;


public class Game
{
	private JFrame window = new JFrame("Game");
	public Game()
	{
		window.setSize(1920, 1080);
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setUndecorated(true);
		window.setVisible(true);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
}
