package maze.logic;
import java.util.ArrayList;

/**
 * Classe que constroi o jogo.
 * Esta classe verifica se o jogo ainda está a correr e constrói os város objetos a partir de um labirinto inicial.
 * Tambem faz o ciclo de logica do jogo.
 * @author Inês Gomes e Andreia Rodrigues
 */
public class Game {
	/*
	 * easy - 0 - dragao parado
	 * medium - 1 - dragao com movimentacao aleatoria intercalada com adormecimento
	 * hard - 2 - dragao com movimentacao aleatoria
	 */
	public enum Strategy {EASY, MEDIUM, HARD}; 
	private Strategy currentStrategy;
	private boolean running;
	private Hero hero;
	private ArrayList<Dragon> dragons;
	private Sword sword;
	private Maze maze;
	private char[][] randomMaze;

	/**
	 * Construtor por omissão.
	 * Apenas inicializa os vários elementos.
	 */
	public Game(){
		running = true;
		currentStrategy =  Strategy.values()[0];
		maze = new Maze();
		hero = new Hero(0, 0, 'H');
		dragons = new ArrayList<>();
		sword = new Sword(0, 0, 'E');		
	}

	/**
	 * Construtor para labirintos aleatórios.
	 * Construtor principal. Recebe como parâmetro os valores para construir um labirinto aleatório.
	 * Constrói um labirinto aleatório e de seguida lê o mesmo e cria os objetos Fero, Dragon e Sword.
	 * @param modoJogo Número (0, 1 ou 2) que indica se o jogo vai ser disputado, respetivamente em modo fácil, médio ou dificil.
	 * @param tamanho Dimensao do labirinto. O tabuleiro é sempre quadrado (largura = comprimento).
	 * @param numDragoes Numero de dragoes que o labirinto tera.
	 */
	public Game (int modoJogo, int tamanho, int numDragoes) {
		running = true;
		currentStrategy = Strategy.values()[modoJogo];

		MazeBuilder mazeBuilder = new MazeBuilder();
		randomMaze = mazeBuilder.buildMaze(tamanho, numDragoes);
		maze = new Maze(randomMaze);

		hero = maze.getHero();
		dragons = maze.getDragons();
		sword = maze.getSword();
	}

	/**
	 * Construtor para testes unitários.
	 * Recebe o labirinto que vai ser testado no teste unitário e a estrategia de jogo.
	 * @param m Labirinto a ser testado
	 * @param strategy Estrategia de jogo.
	 */
	public Game(char[][] m, int strategy){
		running = true;
		currentStrategy = Strategy.values()[strategy];
		maze = new Maze(m);
		hero = maze.getHero();
		dragons = maze.getDragons();
		sword = maze.getSword();	
	}

	/**
	 * Método que executa a lógica principal do jogo.
	 * Este metodo e invocado pelo metodo main que o executa ciclicamente ate o return ser false.
	 * O metodo recebe uma string com a movimentacao do boneco e move-o, faz as verificoes de acordo com a estrategia de jogo e no final atualiza o labirinto.
	 * @param move String com a movimentacao a efetuar pelo heroi (c - cima, b - baixo, d -direita, e - esquerda)
	 * @return Verdadeiro caso seja para continuar a jogar, ou falso caso contrário (heroi morre ou ganha)
	 */
	public boolean gameLogic(String move){

		if(!hero.move(move, maze, dragons, sword)){
			running = false;
			return false;
		}

		for(Dragon d : dragons)
			if(!d.isDead()){
				if(currentStrategy == Strategy.EASY)
					hero.dragonAdjacent(maze,d,sword); //depois de se mexer verifica se ficou adjacente ao dragao
				if(currentStrategy == Strategy.MEDIUM)
					d.randomOption(hero, maze, sword);
				if(currentStrategy == Strategy.HARD)
					d.move(hero, maze, sword);
			}

		if(hero.isDead())
			running = false;

		maze.updateMaze(dragons, hero, sword);

		return running;
	}

	/**
	 * Metodo que retorna a string com o labirinto do objeto Maze.
	 * @return String com o labirinto
	 */
	public String gamePrintMaze(){
		return maze.printMazeString();
	}

	/**
	 * Metodo que retorna o parametro privado 'running'
	 * @return Paramtro 'running'
	 */
	public boolean getRunning(){
		return running;
	}

	/**
	 * Metodo que devolve o heroi da Classe Game.
	 * @return Objeto do tipo Hero.
	 */
	public Hero getHero(){
		return hero;
	}

	/**
	 * Metodo que devolve a espada da Classe Game.
	 * @return Objeto do tipo Sword,
	 */
	public Sword getSword(){
		return sword;
	}

	/**
	 * Metodo que devolve uma colecao dos dragoes da classe Game.
	 * @return ArrayList do tipo Dragon.
	 */
	public ArrayList<Dragon> getDragons(){
		return dragons;
	}

	/**
	 * Metodo que devolve o labirinto da classe Game.
	 * @return Objeto do tipo Maze.
	 */
	public Maze getMaze(){
		return maze;
	}

	/**
	 * Metodo que retorna o numero da estrategia previamente selecionada.
	 * Retorna -1 em caso de erro ou ainda não ter selecionado a estratégia.
	 * @return Estratégia de jogo.
	 */
	public int getStrategy(){
		for(int i = 0; i < 3; i++)
			if(currentStrategy == Strategy.values()[i])
				return i;
		return -1; //erro
	}
	
	/**
	 * Metodo que atualiza os objetos no labirinto, por exemplo, se um objeto nao existia e passa a existir, este metodo atualiza o game com o novo objeto.
	 */
	public void mazeUpdate(){
		hero = maze.getHero();
		dragons = maze.getDragons();
		sword = maze.getSword();
	}
}
