package maze.logic;

/**
 * Classe que constroi um elemento presente no labirinto.
 * Permite-nos ter acesso aos elementos presentes no labirinto facilmente, com as suas
 * coordenadas guardadas e o simbolo que o identifica.
 * @author In�s Gomes e Andreia Rodrigues
 */

public class Elements {

	private Point coordenates = new Point(0,0); 
	private char symbol;
	
	/**
	 * Construtor da classe
	 * Cria um elemento com o ponto com as coordenadas do pr�prio e o simbolo que o indentifica
	 * recebidos como par�metros. 
	 * @param p Ponto com as coordenadas do elemento.
	 * @param symbol Simbolo do elemento.
	 */
	
	public Elements(Point p, char symbol){
		coordenates = p;
		this.symbol = symbol;
	}
	
	/**
	 * Este m�todo permite alterar a posi��o x do elemento.
	 * @param xpos Inteiro com a nova posi��o x
	 */
	
	public void setXpos(int xpos){	
		coordenates.setX(xpos);
	}
	
	/**
	 * Este m�todo permite alterar a posi��o y do elemento.
	 * @param ypos Inteiro com a nova posi��o y
	 */
	
	public void setYpos(int ypos){	
		coordenates.setY(ypos);
	}
	
	/**
	 * Este m�todo permite aceder � posi��o y do elemento.
	 * @return Inteiro com a posi��o x do elemento.
	 */
	
	public int getXpos(){	
		return coordenates.getX();
	}
	
	/**
	 * Este m�todo permite aceder � posi��o y do elemento.
	 * @return Inteiro com a posi��o y do elemento.
	 */
	
	public int getYpos(){	
		return coordenates.getY();
	}
	
	/**
	 * Este m�todo permite aceder ao Point que guarda as coordenadas do elemento.
	 * @return Point com as coordenadas x e y do elemento.
	 */
	
	public Point getPoint(){
		return coordenates;
	}
	
	/**
	 * Este m�todo permite alterar as coordenadas x e y do elemento.
	 * @param p Ponto onde est�o guardadas as coordenadas x e y do elemento.
	 */
	
	public void setPoint(Point p){
		this.coordenates = p;
	}
	
	/**
	 * Este m�todo permite aceder ao simbolo que identifica o elemento no labirinto.
	 * @return Char do simbolo que identifica o elemento.
	 */
	
	public char getSymbol(){
		return symbol;
	}
	
	/**
	 * Este m�todo permite alterar ao simbolo que identifica o elemento no labirinto.
	 * @param newSymbol Novo simbolo a utilizar para identifica��o do elemento.
	 */
	
	public void setSymbol(char newSymbol){
		this.symbol = newSymbol;
	}
	
}
