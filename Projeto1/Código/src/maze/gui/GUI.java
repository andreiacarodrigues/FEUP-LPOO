package maze.gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JPanel;


public class GUI {

	private JFrame frame;
	private ImageIcon hero; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public GUI() throws IOException {
		hero = new ImageIcon("src\\maze\\resources\\menuDragon.png");
		initialize();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Labirinto");
		frame.getContentPane().setBackground(new Color(245, 245, 245));
		frame.setBounds(100, 100, 398, 399);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnIniciar = new JButton("Jogar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Configurations windowC = new Configurations();
					frame.dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnIniciar.setForeground(Color.BLACK);
		btnIniciar.setFont(new Font("Calibri", Font.BOLD, 16));
		btnIniciar.setBackground(new Color(211, 211, 211));
		btnIniciar.setBounds(90, 218, 200, 33);
		frame.getContentPane().add(btnIniciar);
		
		JButton btnRules = new JButton("Regras");
		btnRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Rules windowR = new Rules();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnRules.setForeground(Color.BLACK);
		btnRules.setFont(new Font("Calibri", Font.BOLD, 16));
		btnRules.setBackground(new Color(211, 211, 211));
		btnRules.setBounds(90, 262, 200, 33);
		frame.getContentPane().add(btnRules);
		
		JButton btnExit = new JButton("Sair");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnExit.setForeground(Color.BLACK);
		btnExit.setFont(new Font("Calibri", Font.BOLD, 16));
		btnExit.setBackground(new Color(211, 211, 211));
		btnExit.setBounds(90, 306, 200, 33);
		frame.getContentPane().add(btnExit);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(245, 245, 245));
		panel.setBounds(90, 0, 200, 207);
		frame.getContentPane().add(panel);
		JLabel imagem = new JLabel(hero);
		imagem.setBounds(300, 30, 70, 96);
		imagem.setSize(150, 150);
		imagem.setVisible(true);
		panel.add(imagem);
	}
}
