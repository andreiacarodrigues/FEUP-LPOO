package maze.gui;


import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Rules {

	private JFrame frame;

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public Rules() throws IOException {
		initialize();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame("Labirinto");
		frame.getContentPane().setBackground(new Color(245, 245, 245));
		frame.setBounds(100, 100, 659, 646);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		String everything;
		try(BufferedReader br = new BufferedReader(new FileReader("src\\maze\\resources\\regras.txt"))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    everything = sb.toString();
		}
		
		JTextArea txtRules = new JTextArea();
		txtRules.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtRules.setText(everything);
		txtRules.setEditable(false);
		txtRules.setBounds(20, 60, 600, 475);
		frame.getContentPane().add(txtRules);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		btnOk.setForeground(Color.BLACK);
		btnOk.setFont(new Font("Calibri", Font.BOLD, 16));
		btnOk.setBackground(Color.LIGHT_GRAY);
		btnOk.setBounds(230, 546, 200, 33);
		frame.getContentPane().add(btnOk);
		
		JLabel lblRegrasDoJogo = new JLabel("Regras do Jogo");
		lblRegrasDoJogo.setFont(new Font("Calibri", Font.BOLD, 17));
		lblRegrasDoJogo.setBounds(279, 20, 114, 20);
		frame.getContentPane().add(lblRegrasDoJogo);
	}
}
