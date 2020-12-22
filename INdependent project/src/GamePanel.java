import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener {

	public static final int GRASS = 0;
	public static final int WALL = 1;
	public double elapsed = 0.0;
	public Image tileImages[];
	public int tiles[][];

	public ArrayList<MovingObject> objects;
	public MovingObject player = new MovingObject(7.0, 5.0, 1.0, 2);
	public MovingObject button = new MovingObject(8, 5, 1, 4);
	public boolean upPressed = false;
	public boolean rightPressed = false;
	public boolean downPressed = false;
	public boolean leftPressed = false;
	public int score = 5000;

	public GamePanel() {
		objects = new ArrayList<MovingObject>();
		objects.add(player);

		objects.add(new MovingObject(1.0, 2.0, 1.0, 3));
		objects.add(new MovingObject(2.0, 9.0, 1.0, 3));
		objects.add(new MovingObject(13.0, 2.0, 1.0, 3));
		objects.add(new MovingObject(13.0, 9.0, 1.0, 3));
		objects.add(new MovingObject(3.0, 1.0, 1.0, 3));
		objects.add(new MovingObject(11.0, 9.0, 1.0, 3));


		tileImages = new Image[5];
		tiles = new int[][] {
			new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			new int[] {1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1},
			new int[] {1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1},
			new int[] {1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1},
			new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
			new int[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
			new int[] {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1},
			new int[] {1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1},
			new int[] {1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1},
			new int[] {1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1},
			new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
		};

		try {
			tileImages[0] = ImageIO.read(new File("grass.jpg"));
			tileImages[1] = ImageIO.read(new File("wall.jpg"));	
			tileImages[2] = ImageIO.read(new File("ghost.png"));
			tileImages[3] = ImageIO.read(new File("yellowCircle.png")); // CHANGE LATER
			tileImages[4] = ImageIO.read(new File("button.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0); // quit program
		}

		Timer timer = new Timer(16, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doPhysics();
				repaint();
			}
		}
				);
		timer.start();
		addKeyListener(this);
	}

	private void doPhysics() {
		elapsed += 0.016;
		if (objects.size() > 1)
			score -= 1;

		double dx = 0, dy = 0;
		if (upPressed) dy -= 1;
		if (downPressed) dy += 1;
		if (leftPressed) dx -= 1;
		if (rightPressed) dx += 1;
		for (int cycles = 0; cycles < 3; cycles++) {
			if (dx != 0 || dy != 0) {
				double len = Math.sqrt(dx * dx + dy * dy);
				dx /= len;
				dy /= len;
				player.setX(player.getX() + dx * 0.04);
				player.setY(player.getY() + dy * 0.04);
			}
			airun();
			collisions();
		}
	}

	public void changeLevel(){
		for(int y=1; y<tiles.length-1; y++){
			for(int x=1; x<tiles[0].length-1; x++){
				if(!(x<9&&x>7))
					if(!(y<6&&y>4))
						if(!(x==7&&y==4))
							if(!(x==9&&y==4))
								if(!(x==7&&y==6))
									if(!(x==9&&y==6))
										tiles[y][x] = (int)(Math.random()*2);
			}
		}
		repaint();
	}

	private void airun() {
		for (MovingObject mo : objects) {
			if (mo != player) {
				double px = player.getX()+.5;
				double py = player.getY()+.5;
				double cx = mo.getX()+.5;
				double cy = mo.getY()+.5;
				double distance = Math.sqrt((py-cy)*(py-cy)+(px-cx)*(px-cx));
				double push = 0.03;
				if (distance < 5)
					push += 0.04 * (5 - distance);
				double outx = (cx-px)/distance;
				double outy = (cy-py)/distance;
				double ang = 1 * Math.sin(12 * elapsed + mo.hashCode() % 100) + 0.5;
				double noutx = outx*Math.cos(ang)-outy*Math.sin(ang);
				double nouty = outy*Math.cos(ang)+outx*Math.sin(ang);
				double cdx = 7 - cx;
				double cdy = 5 - cy;
				double cdist = Math.sqrt(cdx * cdx + cdy * cdy);
				cdx /= cdist;
				cdy /= cdist;
				if (cdist > 2) {
					double pull = 0.003 * (cdist - 2);
					mo.setX(mo.getX() + cdx * pull);
					mo.setY(mo.getY() + cdy * pull);
				}
				mo.setX(mo.getX() + noutx * push);
				mo.setY(mo.getY() + nouty * push);

			}
		}
	}

	private void collisions() {

		for (MovingObject mo : objects) {
			double px = mo.getX();
			double py = mo.getY();
			for (int y=0; y<11; y++) {
				for (int x=0; x<15; x++) {
					if (tiles[y][x] == 1) { // stone

						double dx = px - x;
						double dy = py - y;
						//top
						if (dx >= -0.91 && dx <= 0.91 && dy >= -0.91 && dy <= -0.7) {
							dy = -0.911;
							if (dx >= 0.75) {
								dx += 0.01;
							} else if (dx <= -0.75) {
								dx -= 0.01;
							}
						}
						// on bottom
						else if (dx >= -0.91 && dx <= 0.91 && dy <= 0.91 && dy >= .7) {
							dy = 0.911;
							if (dx >= 0.75) {
								dx += 0.01;
							} else if (dx <= -0.75) {
								dx -= 0.01;
							}
						}
						// on left
						else if (dx >= -0.91 && dx <= -0.7 && dy >= -0.91 && dy <= 0.91) {
							dx = -0.911;
							if (dy >= 0.75) {
								dy += 0.01;
							} else if (dy <= -0.75) {
								dy -= 0.01;
							}
						}
						// on right
						else if (dx <= 0.91 && dx >= 0.7 && dy >= -0.91 && dy <= 0.91) {
							dx = 0.911;
							if (dy >= 0.75) {
								dy += 0.01;
							} else if (dy <= -0.75) {
								dy -= 0.01;
							}
						}
						px = dx + x;
						py = dy + y;
					}
				}
			}
			mo.setX(px);
			mo.setY(py);
		}

		ArrayList<MovingObject> toRemove = new ArrayList<MovingObject>();

		for (int i = 0; i < objects.size(); i++) {
			for (int k = i + 1; k < objects.size(); k++) {
				double px = objects.get(i).getX()+.5;
				double py = objects.get(i).getY()+.5;
				double cx = objects.get(k).getX()+.5;
				double cy = objects.get(k).getY()+.5;
				double distance = Math.sqrt((py-cy)*(py-cy)+(px-cx)*(px-cx));
				if (distance < 0.8) {
					if (i == 0) { // player
						toRemove.add(objects.get(k));
						score += 200;
					}
				} else if (distance < 1.6 && i != 0) {
					double push = 0.005*(distance-3)*(distance-3);
					if (distance > 0.01) {
						double outx = (px-cx)/distance;
						double outy = (py-cy)/distance;
						objects.get(i).setX(objects.get(i).getX() + outx * push);
						objects.get(i).setY(objects.get(i).getY() + outy * push);
						objects.get(k).setX(objects.get(k).getX() - outx * push);
						objects.get(k).setX(objects.get(k).getX() - outx * push);
					}
				}
			}
		}
		double px = player.getX()+.5;
		double py = player.getY()+.5;
		double cx = button.getX()+.5;
		double cy = button.getY()+.5;
		double distance = Math.sqrt((py-cy)*(py-cy)+(px-cx)*(px-cx));
		if (distance < 0.3) {
			changeLevel();
		}

		// if x < 0, add width of board to x
		// if x > width, subtract width of board from x
		// same for y

		for (MovingObject k : toRemove) {
			objects.remove(k);
			System.out.println(objects.size());
		}
	}

	public void drawMovingObject(MovingObject mo, Graphics g) {
		g.drawImage(
				tileImages[mo.getTileIndex()],
				(int)(mo.getX()*60),
				(int)(mo.getY()*60),
				(int)(mo.getSize()*60),
				(int)(mo.getSize()*60), null);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(900, 660);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int y=0; y<11; y++) 
			for (int x=0; x<15; x++)
				g.drawImage(tileImages[tiles[y][x]], 60*x, 60*y, 60, 60, null);
		drawMovingObject(button, g);
		for (MovingObject mo : objects) {
			drawMovingObject(mo, g);
		}
		g.setFont(new Font( "", Font.BOLD, 36));
		g.drawString("Score: " + score, 350, 45);
		if(objects.size()==1){
			g.drawString("Game Over", 400, 300);
		}
		g.dispose();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W) {
			upPressed = true;
		}
		if (key == KeyEvent.VK_D) {
			rightPressed = true;
		}
		if (key == KeyEvent.VK_S) {
			downPressed = true;
		}
		if (key == KeyEvent.VK_A) {
			leftPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W) {
			upPressed = false;
		}
		if (key == KeyEvent.VK_D) {
			rightPressed = false;
		}
		if (key == KeyEvent.VK_S) {
			downPressed = false;
		}
		if (key == KeyEvent.VK_A) {
			leftPressed = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
