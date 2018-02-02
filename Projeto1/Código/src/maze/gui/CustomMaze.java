package maze.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Font;

import maze.logic.*;
import maze.test.TestMazeBuilder.Point;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.JSeparator;
import javax.swing.JTable;

public class CustomMaze {

	private JFrame frame;
	private JButton btnHero;
	private JButton btnDragon;
	private JButton btnExit;
	private JButton btnSword;
	private JButton btnWall;
	private JButton btnFloor;
	private MazePanel mazePanel;
	private Game game;
	private int numDragons;
	private boolean heroFlag;
	private boolean dragonFlag;
	private boolean exitFlag;
	private boolean swordFlag;
	private boolean wallFlag;
	private boolean floorFlag;
	private JButton btnRegras;

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public CustomMaze(Game game) throws IOException {
		this.game = game;
		heroFlag = false;
		dragonFlag = false;
		exitFlag = false;
		swordFlag = false;
		wallFlag = false;
		floorFlag = false;
		numDragons = 0;

		initialize();

		frame.setLocationRelativeTo(null);

		mazePanel.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(heroFlag){
					if(paintMaze(e.getX(),e.getY(),'H')){
						heroFlag = false;
						btnHero.setEnabled(false);
					}
				}else if(dragonFlag){
					if(paintMaze(e.getX(),e.getY(),'D'))
						numDragons++;
					if(numDragons >= game.getMaze().getLength()/3){
						btnDragon.setEnabled(false);
						dragonFlag = false;
					}
				}else if(swordFlag){
					if(paintMaze(e.getX(),e.getY(),'E')){
						swordFlag = false;
						btnSword.setEnabled(false);
					}
				}else if(exitFlag){
					if(paintMaze(e.getX(),e.getY(),'S')){
						exitFlag = false;
						btnExit.setEnabled(false);
					}
				}else if(wallFlag){
					paintMaze(e.getX(),e.getY(),'x');
				}else if(floorFlag){
					paintMaze(e.getX(),e.getY(),' ');
				}
				mazePanel.repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}	
		});

		frame.setVisible(true);
	}

	public boolean paintMaze(int x, int y, char symbol){
		int dim = game.getMaze().getLength();
		int image = 32; // tamanho de uma imagem

		//limites do labirinto
		if(x < 0 || y < 0 || x > dim*image || y > dim*image)
			return false;

		for(int i = 0; i < dim; i++){
			for(int j = 0; j < dim; j++){
				if(x >= i*image && x <= (i+1)*image && y >= j*image && y <= (j+1)*image){

					switch (game.getMaze().getSymbol(j, i)) {
					case 'H':
						btnHero.setEnabled(true);
						break;
					case 'D':
						btnDragon.setEnabled(true);
						numDragons--;
						break;
					case 'E':
						btnSword.setEnabled(true);
						break;
					case 'S':
						btnExit.setEnabled(true);
						break;
					default:
						break;
					}

					if(symbol == 'S')
					{
						if(((j == 0 || j == dim-1)&&((i == 0|| i == dim-1))))
						{
							JOptionPane.showMessageDialog(frame, "Não pode colocar a saída nos cantos do labirinto!");
							return false;
						}

						if(!((j == 0 || j == dim-1)||((i == 0|| i == dim-1))))
						{
							JOptionPane.showMessageDialog(frame, "A saida tem de estar numa extremidade do labirinto!");
							return false;
						}
					}
					if(!((symbol == 'x')|| (symbol == 'S')))
					{
						if(((j == 0 || j == dim-1) || ((i == 0|| i == dim-1))))
						{
							JOptionPane.showMessageDialog(frame, "Não pode colocar este elemento nas extremidades do labirinto!");
							return false;
						}
					}
					game.getMaze().setSymbol(j,i,symbol); //coloca na matriz o objeto
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {

		frame = new JFrame("Labirinto");
		frame.getContentPane().setBackground(new Color(245, 245, 245));
		frame.setBounds(100, 100, 700, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		ImageIcon hero = new ImageIcon("src\\maze\\resources\\heroArmed.jpg");
		ImageIcon dragon = new ImageIcon("src\\maze\\resources\\dragonAwake.jpg");
		ImageIcon sword = new ImageIcon("src\\maze\\resources\\sword.jpg");
		ImageIcon exit = new ImageIcon("src\\maze\\resources\\exitClose.jpg");
		ImageIcon wall = new ImageIcon("src\\maze\\resources\\wall.jpg");
		ImageIcon floor = new ImageIcon("src\\maze\\resources\\floor.jpg");

		JButton btnTerminar = new JButton("Terminar programa");
		btnTerminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		JButton btnMenu = new JButton("Voltar ao menu");
		btnMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					GUI windowM = new GUI();
					frame.dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnMenu.setForeground(Color.BLACK);
		btnMenu.setFont(new Font("Calibri", Font.BOLD, 16));
		btnMenu.setBackground(new Color(211, 211, 211));
		btnMenu.setBounds(333, 610, 146, 35);
		frame.getContentPane().add(btnMenu);
		btnTerminar.setForeground(Color.BLACK);
		btnTerminar.setFont(new Font("Calibri", Font.BOLD, 16));
		btnTerminar.setBackground(new Color(211, 211, 211));
		btnTerminar.setBounds(489, 610, 168, 35);
		frame.getContentPane().add(btnTerminar);

		JButton btnVerificarLabirinto = new JButton("Verificar Labirinto");
		btnVerificarLabirinto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Maze maze = game.getMaze();

				if(!testMaze(maze))
				{}
				else
				{	
					try {
						game.mazeUpdate();
						GameRunning windowG = new GameRunning(game);
						frame.dispose();
					} catch (IOException a) {
						a.printStackTrace();
					}
				}
			}
		});

		btnRegras = new JButton("Regras");
		btnRegras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Rules windowR = new Rules();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnRegras.setForeground(Color.BLACK);
		btnRegras.setFont(new Font("Calibri", Font.BOLD, 16));
		btnRegras.setBackground(new Color(211, 211, 211));
		btnRegras.setBounds(24, 610, 129, 35);
		frame.getContentPane().add(btnRegras);
		btnVerificarLabirinto.setForeground(Color.BLACK);
		btnVerificarLabirinto.setFont(new Font("Calibri", Font.BOLD, 16));
		btnVerificarLabirinto.setBackground(new Color(211, 211, 211));
		btnVerificarLabirinto.setBounds(164, 610, 159, 35);
		frame.getContentPane().add(btnVerificarLabirinto);

		btnHero = new JButton(hero);
		btnHero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				heroFlag = true;
				//se alguma estiver ativa temos de desativar
				dragonFlag = false;
				exitFlag = false;
				swordFlag = false;
				floorFlag = false;
				wallFlag = false;
			}
		});
		btnHero.setBounds(600, 228, 50, 50);
		frame.getContentPane().add(btnHero);

		btnDragon = new JButton(dragon);
		btnDragon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dragonFlag = true;
				//se alguma estiver ativa temos de desativar
				heroFlag = false;
				exitFlag = false;
				swordFlag = false;
				floorFlag = false;
				wallFlag = false;
			}
		});
		btnDragon.setBounds(600, 428, 50, 50);
		frame.getContentPane().add(btnDragon);

		btnExit = new JButton(exit);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitFlag = true;
				//se alguma estiver ativa temos de desativar
				heroFlag = false;
				dragonFlag = false;
				swordFlag = false;
				floorFlag = false;
				wallFlag = false;
			}
		});
		btnExit.setBounds(600, 128, 50, 50);
		frame.getContentPane().add(btnExit);

		btnSword = new JButton(sword);
		btnSword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				swordFlag = true;
				//se alguma estiver ativa temos de desativar
				heroFlag = false;
				dragonFlag = false;
				exitFlag = false;
				floorFlag = false;
				wallFlag = false;
			}
		});
		btnSword.setBounds(600, 328, 50, 50);
		frame.getContentPane().add(btnSword);

		btnWall = new JButton(wall);
		btnWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wallFlag = true;
				//se alguma estiver ativa temos de desativar
				heroFlag = false;
				dragonFlag = false;
				exitFlag = false;
				swordFlag = false;
				floorFlag = false;
			}
		});
		btnWall.setBounds(600, 28, 50, 50);
		frame.getContentPane().add(btnWall);

		btnFloor = new JButton(floor);
		btnFloor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				floorFlag = true;
				//se alguma estiver ativa temos de desativar
				heroFlag = false;
				dragonFlag = false;
				exitFlag = false;
				swordFlag = false;
				wallFlag = false;
			}
		});
		btnFloor.setBounds(600, 528, 50, 50);
		frame.getContentPane().add(btnFloor);

		mazePanel = new MazePanel(game.getMaze());
		mazePanel.setForeground(Color.BLACK);
		mazePanel.setBackground(new Color(245, 245, 245));
		mazePanel.setBounds(30, 30, 550, 550);
		frame.getContentPane().add(mazePanel);
	}

	// Esta função vê se o labirinto tem espaços vazios/demasiadas paredes no seu interior( 2x2, 3x3 e 4x4 )
	private boolean hasSquare(char[][] maze, char[][] square) {
		for (int i = 0; i < maze.length - square.length; i++)
			for (int j = 0; j < maze.length - square.length; j++) {
				boolean match = true;
				for (int y = 0; y < square.length; y++)
					for (int x = 0; x < square.length; x++) {
						if (maze[i+y][j+x] != square[y][x])
							match = false;
					}
				if (match)
					return true;
			}		
		return false; 
	}

	// Função auxiliar para testes
	private Point findPos(char [][] maze, char c) {
		for (int x = 0; x < maze.length; x++)			
			for (int y = 0; y < maze.length; y++)
				if (maze[y][x] == c)
					return new Point(y, x);
		return null;		
	}

	// Vê se existe um caminho de qualquer espaço vazio até á saida do labirinto
	private boolean checkExitReachable(char [][] maze) {
		Point p = findPos(maze, 'S');
		boolean [][] visited = new boolean[maze.length] [maze.length];

		visit(maze, p.getY(), p.getx(), visited);

		for (int i = 0; i < maze.length; i++)
			for (int j = 0; j < maze.length; j++)
				if (maze[i][j] != 'x' && ! visited[i][j] )
					return false;

		return true; 
	}

	// Função auxiliar para testes
	private void visit(char[][] m, int i, int j, boolean [][] visited) {
		if (i < 0 || i >= m.length || j < 0 || j >= m.length)
			return;
		if (m[i][j] == 'x' || visited[i][j])
			return;
		visited[i][j] = true;
		visit(m, i-1, j, visited);
		visit(m, i+1, j, visited);
		visit(m, i, j-1, visited);
		visit(m, i, j+1, visited);
	}

	// Função auxiliar para testes
	public String str(char [][] maze) {
		StringBuilder s = new StringBuilder();
		for (char [] line : maze) {
			s.append(Arrays.toString(line));
			s.append("\n");
		}
		return s.toString();
	}

	public boolean testMaze(Maze maze)
	{
		char[][] badWalls = {
				{'x', 'x', 'x'},
				{'x', 'x', 'x'},
				{'x', 'x', 'x'}};
		char[][] badSpaces = {
				{' ', ' '},
				{' ', ' '}};
		char[][] badDiagonalDown = {
				{'x', ' '},
				{' ', 'x'}};
		char[][] badDiagonalUp = {
				{' ', 'x'},
				{'x', ' '}};

		char[][] m = maze.getCharMaze();
		String message = new String("");
		boolean errorFound = false;

		// vê se todos os elementos foram colocados
		Hero h = maze.getHero();
		Sword s = maze.getSword();
		ArrayList<Dragon> dragons = maze.getDragons();

		if(h == null || s == null || dragons.size() == 0 || findPos(m, 'S') == null){
			message += "* Esqueceu-se de colocar alguns elementos: ";
			if(findPos(m, 'S') == null)
				message += "saida ";
			if(h == null)
				message += "heroi ";
			if(dragons.size() == 0)
				message += "dragao ";
			if(s == null)
				message += "espada ";

			JOptionPane.showMessageDialog(frame, message, "Labirinto", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		//Retira todos os elementos do labirinto para teste de espaços vazios
		maze.setSymbol(maze.getHero().getXpos(), maze.getHero().getYpos(), ' ');
		maze.setSymbol(maze.getSword().getXpos(), maze.getSword().getYpos(), ' ');
		for(Dragon d : dragons)
			maze.setSymbol(d.getXpos(), d.getYpos(), ' ');

		//Faz teste espaços vazios e paredes
		if((hasSquare(m, badWalls)) || (hasSquare(m, badSpaces)) || (hasSquare(m, badDiagonalDown)) || (hasSquare(m, badDiagonalUp))){
			errorFound = true;
			message += "* O labirinto não foi construido corretamente. Verifique quais foram as condições violadas. \n";
		}

		// Volta a colocar todos os elementos no labirinto
		maze.setSymbol(h.getXpos(), h.getYpos(), 'H');
		maze.setSymbol(s.getXpos(), s.getYpos(), 'E');
		for(Dragon d : dragons)
			maze.setSymbol(d.getXpos(), d.getYpos(), 'D');

		//testa se é possivel chegar a saida
		if(!checkExitReachable(m)){
			errorFound = true;
			message += "\n* O labirinto não lhe permite chegar á saida.";
		}

		//testa se o dragao e o heroi e encontram em posicoes adjacentes
		for(Dragon d : dragons)
			if(h.dragonAdjacent(maze, d, s)){
				errorFound = true;
				message += "\n* Existe um dragao adjacente ao heroi.";
			}

		//enviar a mensagem de erro completa
		if(errorFound){
			message += "\n \n * Altere o labirinto e verifique novamente.";
			JOptionPane.showMessageDialog(frame, message, "Labirinto", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		//labirinto sem problemas
		return true;
	}
}
