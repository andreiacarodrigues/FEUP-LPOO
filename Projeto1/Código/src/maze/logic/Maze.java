package maze.logic;

import java.util.ArrayList;

/**
 * Esta classe constroi e guarda todas as informa��es relativas a um labirinto.
 * Um labirinto e uma matriz bidimensional onde a largura e o comprimento sao iguais (labirintos quadrados).
 * Os labirintos tem tamanho minimo de 5 e maximo de 21. Os tamanhos sao sempre imapares.
 * @author In�s Gomes e Andreia Rodrigues
 */
public class Maze {

	private int width;		// saber o tamanho do labirinto automaticamente
	private int height;
	private static char Maze[][];

	/**
	 * Construtor default da classe
	 */
	public Maze(){
		
	}
	
	/**
	 * Construtor da classe
	 * Cria um labirinto com a matriz recebida como par�metro. 
	 * L� a largura e comprimento da matriz fornecida e guarda em 'width' e 'height'.
	 * @param maze Matriz bidimensional com o labirinto.
	 */
	public Maze(char maze[][]){
		Maze = maze;
		width = maze[0].length;
		height = maze.length;
	}

	/**
	 * M�todo que atualiza o labirinto.
	 * Este m�todo percorre o labirinto e sempre que aparece um dragao, heroi ou espada, substitui o caracter por um espa�o vazio.
	 * De seguida, coloca o her�i, espada e drag�es recebidos por par�metro nas posi��es fornecidas.
	 * Antes dos objetos serem colocados no labirinto, t�m de verificar algumas condi��es
	 * o her�i e os drag�es t�m de estar vivos e a espada visivel.
	 * @param dragons ArrayList com um conjunto de Objetos do tipo Dragon
	 * @param h	Objeto do tipo Hero
	 * @param s Objeto do tipo Sword
	 */
	public void updateMaze(ArrayList<Dragon> dragons, Hero h, Sword s){

		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++)
				if(Maze[i][j] != 'x' && Maze[i][j] != ' ' && Maze[i][j] != 'S')
					Maze[i][j] = ' ';
		}

		for(int i = 0; i < dragons.size(); i++){
			Dragon d = dragons.get(i);
			if(!d.isDead())
				Maze[d.getXpos()][d.getYpos()] = d.getSymbol();
		}
		if(!h.isDead())	
			Maze[h.getXpos()][h.getYpos()] = h.getSymbol();
		if(s.isVisible())
			Maze[s.getXpos()][s.getYpos()] = s.getSymbol();
	}

	/**
	 * M�todo que retorna o simbolo localizado na posicao dada.
	 * @param x Coordenada x
	 * @param y Coordenada y
	 * @return Simbolo na matriz.
	 */
	public char getSymbol(int x, int y){
		return Maze[x][y];
	}
	
	/**
	 * M�todo que retorna o tamanho do labirinto.
	 * @return Tamanho do labirinto.
	 */
	public int getLength() {
		return width;
	}
	
	/**
	 * Este m�todo retorna o labirinto em forma de String.
	 * @return String com o labirinto
	 */
	public String printMazeString() {

		String s = "";
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++)
				s = s + Maze[i][j]+" ";
			s = s + ("\n");	
		}
		return s;
	}

	/**
	 * Este m�todo recebe um ponto (posi��o no labirinto) e um simbolo que corresponde a um objeto.
	 * A sua fun��o � verificar no labirinto se as coordenadas fornecidas corresponde ao simbolo fornecido.
	 * O m�todo retorna verdadeiro ou falso consoante a condi��o se verifica ou n�o.
	 * @param p	Objeto do tipo Ponto que cont�m as coordenadas a ser visitadas no labirinto.
	 * @param symbol Char com o simbolo a ser comparado
	 * @return Boleano de acordo com a verifica��o da condi��o
	 */
	public boolean compareElement(Point p, char symbol){
		return (Maze[p.getX()][p.getY()] == symbol);
	}

	/**
	 * M�todo usado aquando a cria��o do jogo. 
	 * Este m�todo � invocado apenas nos construtores do jogo, quando ainda n�o foi efetuada nenhuma jogada.
	 * Para encontrar um her�i, este m�todo percorre o labirinto at� encontrar o simbolo 'H' (simbolo do her�i aquando o inicio do jogo).
	 * Quando encontra, cria um Objeto do tipo Hero com as coordenadas da posi��o onde foi encontrado.
	 * @return Objeto do tipo Hero
	 */
	public Hero getHero(){
	
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++)
				if(Maze[i][j] == 'H')
					return new Hero(i,j,'H');
				
		}
		return null;
	}

	/**
	 * M�todo usado aquando a cria��o do jogo. 
	 * Este m�todo � invocado apenas nos construtores do jogo, quando ainda n�o foi efetuada nenhuma jogada.
	 * Para encontrar uma espada, este m�todo percorre o labirinto at� encontrar o simbolo 'E' (simbolo da espada aquando o inicio do jogo)
	 * Quando encontra, cria um Objeto do tipo Sword com as coordenadas da posi��o onde foi encontrado.
	 * @return Objeto do tipo Sword
	 */
	public Sword getSword(){

		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++)
				if(Maze[i][j] == 'E')
					return new Sword(i,j,'E');
		}
		return null;
	}

	/**
	 * M�todo usado aquando a cria��o do jogo. 
	 * Este m�todo � invocado apenas nos construtores do jogo, quando ainda n�o foi efetuada nenhuma jogada.
	 * Para encontrar um ou mais drag�es, este m�todo percorre todo o labirinto.
	 * Quando encontra o simbolo 'D' (simbolo do drag�o aquando o inicio do jogo), � criado um Objeto do tipo Dragon com as coordenadas atuais, que � adicionado a uma cole��o (neste caso ArrayList).
	 * @return Cole��o do tipo ArrayList de Dragon
	 */
	public ArrayList<Dragon> getDragons(){
		ArrayList<Dragon> listOfDragons = new ArrayList<Dragon>();

		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++)
				if(Maze[i][j] == 'D')
					listOfDragons.add(new Dragon(i,j,'D'));
		}
		return listOfDragons;
	}

	/**
	 * Metodo usado para colocar um simbolo numa dada posicao do labirinto.
	 * @param x Coordenada x do simbolo.
	 * @param j Coordenada y do simbolo.
	 * @param symbol Simbolo que ser� colocado no labirinto.
	 */
	public void setSymbol(int x, int y, char symbol) {
		Maze[x][y] = symbol;
	}
	
	/**
	 * M�todo que devolve o array bidimensional com todos os carateres do labirinto
	 * @return Array bidimensional com os carateres do labirinto
	 */
	public char[][] getCharMaze() { return Maze;}
}
