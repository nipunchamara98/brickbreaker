import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer; 
import javax.swing.JPanel;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
	private int bricksTotal = 32;
	private Timer time;
	private int delay = 7;
	private int score =0;
	private int ballpsx = 100;
	private int ballpsy = 250;
	private int xDirect = -1;
	private int yDirect = -2;
	private boolean play = false;
	private int player= 310;
	
	
	private MapBrick map; 
	
	public GamePlay() {
		map = new MapBrick (4,8);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		time = new Timer(delay, this);
		time.start();
	}
	
	public void paint(Graphics g) {
		//background option
		g.setColor(Color.black);
		g.fillRect(1,1, 692, 592);
		map.draw((Graphics2D)g);
		
		//borders of the interface
		g.setColor(Color.red);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0,0 , 692, 3);
		g.fillRect(691,0,3, 592);
		
		//scores
		g.setColor(Color.yellow);
		g.setFont(new Font("Verdana", Font.BOLD, 25));
		g.drawString(""+score, 500, 30);

		//ball settings
		g.setColor(Color.yellow);
		g.fillOval(ballpsx,ballpsy, 20, 20);

		//for paddle
		g.setColor(Color.blue);
		g.fillRect(player, 550, 100,8);
		
		
		if(bricksTotal <=0) {
			play = false;
			xDirect = 0;
			yDirect = 0;
			g.setColor(Color.green);
			g.setFont(new Font("Verdana", Font.BOLD, 30));
			g.drawString("You won the game", 200, 300);
			g.setFont(new Font("Verdana", Font.BOLD, 30));
			g.drawString("Press Enter to restart the game",250, 350);
		}
		
		if (ballpsy > 570) {
			play = false;
			xDirect = 0;
			yDirect = 0;
			g.setColor(Color.blue);
			g.setFont(new Font("Verdana", Font.BOLD, 30));
			g.drawString("Press ENTER to restart the game",250, 350);
			g.setFont(new Font("Verdana", Font.BOLD, 35));
			g.drawString("You lost the game  ", 200, 300);
		}
		g.dispose();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		time.start();
		if(play) {
			//detecting interstion of two objects
			if (new Rectangle(ballpsx,ballpsy, 20, 20).intersects(new Rectangle(player, 550, 100, 8))) {
				yDirect = -yDirect; 
			}
			
			B: for (int i=0; i< map.map.length; i++) {
				for (int j=0; j<map.map[0].length; j++) {
					if (map.map[i][j]>0) {
						int brickwidth = map.brickWidth;
						int brickheight = map.brickHeight;
						int brickx = j*map.brickWidth + 80;
						int bricky = i* map.brickHeight + 50; 
						
						Rectangle ballrect = new Rectangle(ballpsx, ballpsy, 20, 20);
						Rectangle rect = new Rectangle(brickx, bricky, brickwidth, brickheight);
						Rectangle brickrect = rect; 
						
						if(ballrect.intersects(brickrect)) {
							map.setBrickval(0,i,j);
							bricksTotal--;
							score += 10;
							
							if(ballpsx + 19 <= brickrect.x || ballpsx + 1 >= brickrect.x + brickrect.width) {
								xDirect = - xDirect;
							} else {
								yDirect = -yDirect; 
							}
							
							break B;
						}
					}
				}
			}
			ballpsx += xDirect;
			ballpsy += yDirect;
			if (ballpsx < 0) { 
				xDirect = - xDirect;
			}
			if (ballpsy < 0) { 
				yDirect = - yDirect;
			}
			if (ballpsx > 670) { 
				xDirect = - xDirect;
			}
			
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(player <10) {
				player = 10;
			} else {
				moveleft();
			}
		}

		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(player >= 600) {
				player = 600;
			} else {
				moveright();
			}
		}
		
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!play) {
				play = true;
				ballpsx = 120;
				ballpsy = 350;
				xDirect = -1;
				yDirect = -2;
				player = 310;
				score = 0;
				bricksTotal = 21;
				map = new MapBrick(4,8);
				
				repaint();
			};
			
		}
	}

	public void moveleft() {
		play = true;
		player -=20;
	}
	
	public void moveright() {
		play = true;
		player+=20;
	}
	
}
