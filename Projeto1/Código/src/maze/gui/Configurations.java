package maze.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import maze.logic.Game;

import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JCheckBox;

public class Configurations{

	private JFrame frame;
	private JTextField labDimension;
	private JTextField NumDragons;
	private boolean rand;

	/**
	 * Create the application.
	 */
	public Configurations() throws IOException {
		rand = false;
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
		frame.setBounds(100, 100, 552, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblLabDimensions = new JLabel("Dimens\u00E3o do Labirinto");
		lblLabDimensions.setFont(new Font("Calibri", Font.BOLD, 16));
		lblLabDimensions.setBounds(30, 40, 191, 29);
		frame.getContentPane().add(lblLabDimensions);

		labDimension = new JTextField();
		labDimension.setHorizontalAlignment(SwingConstants.CENTER);
		labDimension.setFont(new Font("Calibri", Font.PLAIN, 15));
		labDimension.setColumns(10);
		labDimension.setBounds(250, 40, 65, 26);
		frame.getContentPane().add(labDimension);

		JLabel lblNumDragons = new JLabel("N\u00FAmero de Drag\u00F5es");
		lblNumDragons.setFont(new Font("Calibri", Font.BOLD, 16));
		lblNumDragons.setBounds(30, 80, 257, 29);
		frame.getContentPane().add(lblNumDragons);

		NumDragons = new JTextField();
		NumDragons.setHorizontalAlignment(SwingConstants.CENTER);
		NumDragons.setFont(new Font("Calibri", Font.PLAIN, 15));
		NumDragons.setColumns(10);
		NumDragons.setBounds(250, 80, 65, 26);
		frame.getContentPane().add(NumDragons);

		JLabel lblTypeDragons = new JLabel("Tipo de Drag\u00F5es");
		lblTypeDragons.setFont(new Font("Calibri", Font.BOLD, 16));
		lblTypeDragons.setBounds(30, 120, 257, 29);
		frame.getContentPane().add(lblTypeDragons);

		String[] options = {"Estáticos", "Movimento Aleatório e Adormecimento", "Movimento Aleatório"};
		@SuppressWarnings("unchecked")
		JComboBox cbGameDifficulty = new JComboBox(options);
		cbGameDifficulty.setBackground(new Color(211, 211, 211));
		cbGameDifficulty.setMaximumRowCount(3);
		cbGameDifficulty.setFont(new Font("Calibri", Font.PLAIN, 15));
		cbGameDifficulty.setBounds(250, 120, 260, 29);
		frame.getContentPane().add(cbGameDifficulty);

		JButton buttonTerminator = new JButton("Terminar programa");
		buttonTerminator.setForeground(Color.BLACK);
		buttonTerminator.setFont(new Font("Calibri", Font.BOLD, 16));
		buttonTerminator.setBackground(new Color(211, 211, 211));
		buttonTerminator.setBounds(82, 206, 170, 33);
		frame.getContentPane().add(buttonTerminator);

		JButton buttonGenerator = new JButton("Gerar labirinto");
		buttonGenerator.setForeground(Color.BLACK);
		buttonGenerator.setFont(new Font("Calibri", Font.BOLD, 16));
		buttonGenerator.setBackground(new Color(211, 211, 211));
		buttonGenerator.setBounds(284, 206, 210, 33);
		frame.getContentPane().add(buttonGenerator);

		JLabel lblAleatorio = new JLabel("Labirinto Personalizado");
		lblAleatorio.setFont(new Font("Calibri", Font.BOLD, 16));
		lblAleatorio.setBounds(30, 160, 199, 29);
		frame.getContentPane().add(lblAleatorio);

		JCheckBox aleatorio = new JCheckBox("");
		aleatorio.setBackground(new Color(245, 245, 245));
		aleatorio.setBounds(250, 156, 97, 23);
		frame.getContentPane().add(aleatorio);
		aleatorio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(aleatorio.isSelected()){
					NumDragons.setEditable(false);
					NumDragons.setEnabled(false);
					rand = true;
				}else{
					NumDragons.setEditable(true);
					NumDragons.setEnabled(true);
					rand = false;
				}
			}
		});
		
		buttonTerminator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		buttonGenerator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if((!labDimension.getText().matches("\\d+$")))
				{
					JOptionPane.showMessageDialog(frame, "Input inválido!");
					return;
				}
				if((Integer.parseInt(labDimension.getText()) < 5)){
					JOptionPane.showMessageDialog(frame, "Labirinto demasiado pequeno!");
					return;
				}
				if(Integer.parseInt(labDimension.getText()) > 18)
				{
					JOptionPane.showMessageDialog(frame, "Labirinto demasiado grande!");
					return;
				}
				if((Integer.parseInt(labDimension.getText())%2 == 0))
				{
					JOptionPane.showMessageDialog(frame, "As dimensões do labirinto têm de ser impar!");
					return;
				}

				if(rand){
					int dim = Integer.parseInt(labDimension.getText());
					char maze[][] = new char[dim][dim];

					for(int i = 0; i < dim; i++)
						maze[0][i] = 'x';

					for(int j = 1; j < dim-1; j++){
						for(int k = 0; k < dim;k++){
							if(k == 0 || k == dim-1)
								maze[j][k] = 'x';
							else
								maze[j][k] = ' ';
						}
					}

					for(int l = 0; l < dim; l++)
						maze[dim-1][l] = 'x';

					Game game = new Game (maze, cbGameDifficulty.getSelectedIndex());

					try {
						CustomMaze window = new CustomMaze(game);
					} catch (IOException e1) {
						e1.printStackTrace();
					}					
				}else{
					if((!NumDragons.getText().matches("\\d+$")))
					{
						JOptionPane.showMessageDialog(frame, "Input inválido!");
						return;
					}
					if(Integer.parseInt(NumDragons.getText()) <= 0)
					{
						JOptionPane.showMessageDialog(frame, "Tem de haver pelo menos 1 dragão!");
						return;
					}
					int dragonMax = Integer.parseInt(labDimension.getText())/3;
					if(Integer.parseInt(NumDragons.getText()) > dragonMax)
					{
						JOptionPane.showMessageDialog(frame, "Demasiados dragões!");
						return;
					}

					Game game = new Game (cbGameDifficulty.getSelectedIndex(), Integer.parseInt(labDimension.getText()), Integer.parseInt(NumDragons.getText()));

					try {
						GameRunning window = new GameRunning(game);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				frame.dispose();
			}
		});
	}
}
