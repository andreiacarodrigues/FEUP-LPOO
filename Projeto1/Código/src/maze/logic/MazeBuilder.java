package maze.logic;

import java.util.Random;
import java.util.Stack;

/**
 * Classe que constroi um labirinto aleatório, tendo apenas por base o tamanho do labirinto
 * e a quantidade de dragões que nele devem existir (parâmetros introduzidos pelo
 * utilizador).
 * @author Inês Gomes e Andreia Rodrigues
 */

public class MazeBuilder {
	
	/**
	 * Construtor vazio da classe
	 */
	public MazeBuilder(){}

	/**
	 * Método que permite a construção de um labirinto aleatório apenas tendo como parâmetros
	 * de referência o tamanho e o número total de dragões que este deve ter.
	 * Este método vai verificar se estes parâmetros são adequados.
	 * Para a construção do labirinto, vai começar por criar um labirinto cujo preenchimento 
	 * será de espaços vazios intercalados com paredes. Vai então depois determinar onde 
	 * colocar a saida do labirinto.
	 * Em seguida vai utilizar um array bidimensional auxiliar "visitedCells" no qual vai
	 * guardar o caminho gerado aleatóriamente pelo programa, que vai permitir ver se é possivel 
	 * criar um caminho a partir da celula atual numa dada direção. Se sim, elimina uma parede, e
	 * a nova celula que vai ter em conta nos calculos será esta nova onde eliminou a parede, e 
	 * adiciona-a a uma stack.
	 * Essa celula vai ser dada como visitada no array auxiliar.
	 * Esta stack vai manter registo da célula com que estamos a trabalhar nos calculos. Caso não
	 * seja possivel fazer mais calculos com ela (não é possivel criar mais caminho á sua volta 
	 * porque todas as células ao seu redor já foram visitadas), ela faz pop da celula atualmente no
	 * seu topo e dá como célula atual a célula que estava abaixo da anterior.
	 * A função termina a criação do labirinto no caso de todas as celulas do array auxiliar
	 * já tiverem sido dadas como visitadas e ao mesmo tempo, quando a stack estiver vazia.
	 * No final são colocados os restantes elementos no labirinto.
	 * @param size Tamanho do labirinto 
	 * @param numDragons Numero de dragões a colocar no labirinto
	 * @return Array bi-dimensional com o labirinto gerado aleatóriamente
	 */
	public char[][] buildMaze(int size, int numDragons)
	{
		// Só recebe tamanhos impares 

		if((size%2 == 0) || (size < 5))
			throw new IllegalArgumentException(Integer.toString(size));

		Random r = new Random();

		char maze[][] = new char[size][size];

		char visitedCells[][] = new char[(size-1)/2][(size-1)/2];

		Point guideCell = new Point (0,0);		

		Stack<Point> pathHistory = new Stack<Point>();

		// Preenche o labirinto com 'x' 

		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				maze[i][j] = 'x';

		// Apaga 'x' das posições impares 

		for(int i = 1; i < size; i = i+2)
			for(int j = 1; j < size; j = j+2)
				maze[i][j] = ' ';

		// Coloca a saida no labirinto

		placesExit(maze, guideCell, size);

		// Preenche visitedCells c/ '.' 

		for(int i = 0; i < (size-1)/2; i++)
			for(int j = 0; j < (size-1)/2; j++)
				visitedCells[i][j] = '.';

		// Coloca guideCell na stack

		pathHistory.push(guideCell);
	
		// Marca-a como visitada 
		
		visitedCells[guideCell.getY()][guideCell.getX()] = '+';	

		int direction;
		boolean allVisited = false;
		
		while(!pathHistory.isEmpty())
		{
			direction = r.nextInt(4);

			// Vê se chegámos a um dead end 

			allVisited = true;

			for(int i = 0; i < (size-1)/2; i++){
				for(int j = 0; j < (size-1)/2; j++)
				{
					if(visitedCells[i][j] == '.')
						allVisited = false;
				}
			}
			
			if(allVisited)
				break;
			else
			{
				// Chegou a um dead end; temos de fazer pop da stack
				
				if(deadEnd(guideCell, visitedCells, size) && pathHistory.size()>1)
				{
					guideCell = pathHistory.peek();
					pathHistory.pop();
				}
			}

			if(direction == 0) // cima
			{
				if(guideCell.getY()-1 >= 0)
				{
					guideCell = new Point(guideCell.getX(), guideCell.getY()-1);

					if(visitedCells[guideCell.getY()][guideCell.getX()] == '.')
					{
						if(maze[guideCell.getY()*2+2][guideCell.getX()*2+1] == 'x')
						{
							maze[guideCell.getY()*2+2][guideCell.getX()*2+1] = ' ';
							visitedCells[guideCell.getY()][guideCell.getX()] = '+';
							pathHistory.push(guideCell);
						}
					}
				}
			}
			if(direction == 1) // baixo
			{
				if(guideCell.getY()+1 < (size-1)/2)
				{
					guideCell = new Point(guideCell.getX(), guideCell.getY()+1);

					if(visitedCells[guideCell.getY()][guideCell.getX()] == '.')
					{
						if(maze[guideCell.getY()*2][guideCell.getX()*2+1] == 'x')
						{
							maze[guideCell.getY()*2][guideCell.getX()*2+1] = ' ';
							visitedCells[guideCell.getY()][guideCell.getX()] = '+';
							pathHistory.push(guideCell);

						}
					}
				}
			}

			if(direction == 2) // direita
			{
				if(guideCell.getX()+1 < (size-1)/2)
				{
					guideCell = new Point(guideCell.getX()+1, guideCell.getY());

					if(visitedCells[guideCell.getY()][guideCell.getX()] == '.')
					{
						if(maze[guideCell.getY()*2+1][guideCell.getX()*2] == 'x')
						{

							maze[guideCell.getY()*2+1][guideCell.getX()*2] = ' ';
							visitedCells[guideCell.getY()][guideCell.getX()] = '+';
							pathHistory.push(guideCell);

						}
					}
				}
			}
			if(direction == 3) // esquerda
			{
				if(guideCell.getX()-1 >= 0)
				{
					guideCell = new Point(guideCell.getX()-1, guideCell.getY());

					if(visitedCells[guideCell.getY()][guideCell.getX()] == '.')
					{
						if(maze[guideCell.getY()*2+1][guideCell.getX()*2+2] == 'x')
						{
							maze[guideCell.getY()*2+1][guideCell.getX()*2+2] = ' ';
							visitedCells[guideCell.getY()][guideCell.getX()] = '+';
							pathHistory.push(guideCell);
						}
					}
				}
			}
		
			
		}
	
		// Coloca os elementos no labirinto
		
		placeElements(maze, size, numDragons);
		
		return maze;
	}

	/**
	 * Este método permite colocar os elementos(espada, dragões e heroi) no labirinto.
	 * É chamada no final da construção do labirinto aleatório.
	 * @param maze Array bi-dimensional do labirinto
	 * @param size Tamanho do labirinto
	 * @param numDragons Numero de dragões a colocar no labirinto
	 */
	private void placeElements(char[][] maze, int size, int numDragons)
	{
		Random r = new Random();
		Point p;

		// Coloca espada no labirinto

		p = new Point(r.nextInt(size-2)+1, r.nextInt(size-2)+1);
		while(maze[p.getY()][p.getX()] == 'x')
			p = new Point(r.nextInt(size-2)+1, r.nextInt(size-2)+1);

		maze[p.getY()][p.getX()] = 'E';


		// Coloca um conjunto de dragões no labirinto

		for(int i = 0; i < numDragons; i++){

			p = new Point(r.nextInt(size-2)+1, r.nextInt(size-2)+1);
			while((maze[p.getY()][p.getX()] == 'x') || (maze[p.getY()][p.getX()] == 'E') || (maze[p.getY()][p.getX()] == 'D'))
				p = new Point(r.nextInt(size-2)+1, r.nextInt(size-2)+1);

			maze[p.getY()][p.getX()] = 'D';
		}
		
		// Coloca heroi no labirinto
		p = new Point(r.nextInt(size-2)+1, r.nextInt(size-2)+1);
		while((maze[p.getY()][p.getX()] == 'x') || (maze[p.getY()][p.getX()] == 'E') ||
				(maze[p.getY()+1][p.getX()] == 'D') || (maze[p.getY()-1][p.getX()] == 'D') ||
				(maze[p.getY()][p.getX()+1] == 'D') || (maze[p.getY()][p.getX()-1] == 'D') ||
				(maze[p.getY()][p.getX()] == 'D'))
			p = new Point(r.nextInt(size-2)+1, r.nextInt(size-2)+1);

		maze[p.getY()][p.getX()] = 'H';
		return;
	}

	/**
	 * Este método verifica se, no array auxiliar do labirinto aleatório, as células á volta 
	 * da célula atual já foram todas visitadas, ou seja, se chegámos a um ponto onde é 
	 * necessário mudar-mos a nossa casa de referência. 
	 * Retorna true neste caso ou false caso contrário.
	 * @param guideCell Célula atual de referência no vetor visitedCells
	 * @param visitedCells Array bi-dimensional auxiliar que indica, para todas as células
	 * possiveis do labirinto, quais já foram os pontos visitados ou não
	 * @param size Tamanho do Labirinto
	 * @return Boleano que me indica se com a célula de referência atual é possivel fazer mais
	 * calculos ou nao (todas as células á sua volta já foram visitadas ou não)
	 */
	private boolean deadEnd(Point guideCell, char[][] visitedCells, int size)
	{
		if(((guideCell.getX()+1 >= (size-1)/2) || (visitedCells[guideCell.getY()][guideCell.getX()+1] == '+'))
				&&((guideCell.getY()+1 >= (size-1)/2) || (visitedCells[guideCell.getY()+1][guideCell.getX()] == '+'))
			&& ((guideCell.getX()-1 < 0) || (visitedCells[guideCell.getY()][guideCell.getX()-1] == '+'))
			&& ((guideCell.getY()-1 < 0) || (visitedCells[guideCell.getY()-1][guideCell.getX()] == '+')))
			return true;
			
		return false;
	}	

	/**
	 * Este método coloca a saida do labirinto aleatóriamente, num dos seus 4 lados.
	 * Tem em conta que não a é possivel colocar nas extremidades de cada lado.
	 * @param maze Array bidimensional com o labirinto
	 * @param guideCell Ponto atual de referência no vetor visitedCells
	 * @param size Tamanho do Labirinto
	 */
	private void placesExit(char[][] maze, Point guideCell, int size)
	{
		// exitSide: Dá-me um nº random que me diz em que parede vou meter a saida
		// 0 - esquerda, 1 - cima, 2 - direita e 3 - baixo
		// exitIndex: Dá-me um nº random que me diz em que indice vou colocar

		Random r = new Random();
		int exitSide = r.nextInt(4);
		int exitIndex = r.nextInt(size-1);

		if(exitSide == 0) // esquerda
		{
			while((exitIndex == 0) || (exitIndex == size-1) || maze[exitIndex][1] == 'x')
				exitIndex = r.nextInt(size-1);
			maze[exitIndex][0] = 'S';
			guideCell = new Point(1/2, exitIndex/2);
		}

		if(exitSide == 1) // cima
		{
			while((exitIndex == 0) || (exitIndex == size-1) || maze[1][exitIndex] == 'x')
				exitIndex = r.nextInt(size-1);
			maze[0][exitIndex] = 'S';
			guideCell = new Point(exitIndex/2, 1/2);
		}
		if(exitSide == 2) // direita
		{
			while((exitIndex == 0) || (exitIndex == size-1) || maze[exitIndex][size-2] == 'x')
				exitIndex = r.nextInt(size-1);
			maze[exitIndex][size-1] = 'S';
			guideCell = new Point((size-2)/2, exitIndex/2);
		}
		if(exitSide == 3) // baixo
		{
			while((exitIndex == 0) || (exitIndex == size-1) || maze[size-2][exitIndex] == 'x')
				exitIndex = r.nextInt(size-1);
			maze[size-1][exitIndex] = 'S';
			guideCell = new Point(exitIndex/2, (size-2)/2);
		}
	}
}