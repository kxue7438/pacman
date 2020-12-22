
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.imageio.ImageIO;
import javax.swing.*;
//import javax.swing.KeyStroke;
import java.awt.image.ImageObserver;

public class AdventureGame extends JFrame implements KeyListener{
	private JFrame frame;
	//		private JPanel panel;
	private GamePanel gamePanel;
	private int myX = 100;
	private int myY = 100;

	public AdventureGame(){
		createUI();
	}

	private void createUI(){
		frame = new JFrame("Adventure Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		gamePanel = new GamePanel();
		//frame.add(gamePanel);

		gamePanel.setFocusable(true);


		frame.setContentPane(gamePanel);
		frame.pack();
		frame.setVisible(true);

	}

	public static void main(String[] args){
		new AdventureGame();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}

