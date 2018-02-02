package maze.gui;

import javax.swing.JFrame;
import maze.logic.Game;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameRunning implements KeyListener{

	private static JFrame frame;
	private StatusPanel statusPanel;
	private MazePanel mazePanel;
	private Game game;
	private JButton button;
	private JButton reiniciar;
	private boolean running;
	private JButton up;
	private JButton down;
	private JButton left;
	private JButton right;
	private JButton btn_NovoJogo;

	/**
	 * Create the application.
	 * @param game 
	 */
	public GameRunning(Game game) throws IOException{
		this.game = game;
		this.running = true;
		initialize();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.requestFocus(); // to receive keyboard events
		frame.addKeyListener(this);
	}

	private void moveHeroGUI(String s)
	{
		if(!running)
			return;

		running = game.gameLogic(s);

		if(game.getHero().isDead())
			statusPanel.changeStatus(-1);
		else
			if(!running)
				statusPanel.changeStatus(2);

		if(!running || game.getHero().isDead())
		{
			up.setEnabled(false);
			down.setEnabled(false);
			right.setEnabled(false);
			left.setEnabled(false);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_LEFT: 
			moveHeroGUI("e");
			break;

		case KeyEvent.VK_RIGHT: 
			moveHeroGUI("d");
			break;

		case KeyEvent.VK_UP: 
			moveHeroGUI("c");
			break;

		case KeyEvent.VK_DOWN: 
			moveHeroGUI("b");
			break;
		}
		mazePanel.repaint();
	}

	// Ignored keyboard events			
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}


	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame("Labirinto");
		frame.getContentPane().setBackground(new Color(245, 245, 245));
		frame.setBounds(100, 100, 1054, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		reiniciar = new JButton("Voltar ao menu");
		reiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					GUI window = new GUI();
					frame.dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		reiniciar.setForeground(Color.BLACK);
		reiniciar.setFont(new Font("Calibri", Font.BOLD, 16));
		reiniciar.setBackground(new Color(211, 211, 211));
		reiniciar.setBounds(825, 516, 170, 35);
		frame.getContentPane().add(reiniciar);

		mazePanel = new MazePanel(game.getMaze());
		mazePanel.setForeground(Color.BLACK);
		mazePanel.setBackground(new Color(245, 245, 245));
		mazePanel.setBounds(30, 30, 550, 550);
		frame.getContentPane().add(mazePanel); 

		statusPanel = new StatusPanel();
		statusPanel.setForeground(Color.BLACK);
		statusPanel.setBackground(new Color(245, 245, 245));
		statusPanel.setBounds(628, 11, 400, 362);
		frame.getContentPane().add(statusPanel);

		button = new JButton("Terminar programa");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		button.setForeground(Color.BLACK);
		button.setFont(new Font("Calibri", Font.BOLD, 16));
		button.setBackground(new Color(211, 211, 211));
		button.setBounds(731, 562, 170, 35);
		frame.getContentPane().add(button);

		up = new JButton("\uF070");
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveHeroGUI("c");
				mazePanel.repaint();
				frame.requestFocus();
			}
		});
		up.setForeground(Color.BLACK);
		up.setBackground(new Color(211, 211, 211));
		up.setFont(new Font("Wingdings 3", Font.BOLD, 15));
		up.setBounds(773, 400, 89, 23);
		frame.getContentPane().add(up);

		down = new JButton("\uF071");
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveHeroGUI("b");
				mazePanel.repaint();
				frame.requestFocus();
			}
		});
		down.setBackground(new Color(211, 211, 211));
		down.setForeground(Color.BLACK);
		down.setFont(new Font("Wingdings 3", Font.BOLD, 15));
		down.setBounds(773, 459, 89, 23);
		frame.getContentPane().add(down);

		left = new JButton("\uF074");
		left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveHeroGUI("e");
				mazePanel.repaint();
				frame.requestFocus();
			}
		});
		left.setBackground(new Color(211, 211, 211));
		left.setForeground(Color.BLACK);
		left.setFont(new Font("Wingdings 3", Font.BOLD, 15));
		left.setBounds(718, 430, 97, 23);
		frame.getContentPane().add(left);

		right = new JButton("\uF075");
		right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveHeroGUI("d");
				mazePanel.repaint();
				frame.requestFocus();
			}
		});
		right.setBackground(new Color(211, 211, 211));
		right.setForeground(Color.BLACK);
		right.setFont(new Font("Wingdings 3", Font.BOLD, 15));
		right.setBounds(825, 429, 96, 23);
		frame.getContentPane().add(right);

		btn_NovoJogo = new JButton("Novo jogo");
		btn_NovoJogo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game g = new Game(game.getStrategy(), game.getMaze().getLength(), game.getDragons().size());
				game = g;
				running = true;
				up.setEnabled(true);
				down.setEnabled(true);
				right.setEnabled(true);
				left.setEnabled(true);
				frame.requestFocus(); // to receive keyboard events
				statusPanel.changeStatus(1);
				mazePanel.repaint();
			}
		});
		btn_NovoJogo.setForeground(Color.BLACK);
		btn_NovoJogo.setFont(new Font("Calibri", Font.BOLD, 16));
		btn_NovoJogo.setBackground(new Color(211, 211, 211));
		btn_NovoJogo.setBounds(644, 516, 170, 35);
		frame.getContentPane().add(btn_NovoJogo);

		statusPanel.changeStatus(1);

	}
}
